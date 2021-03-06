/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2008 Paul Lorenz
*/
package com.googlecode.sarasvati.example.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.example.CustomTestNode;
import com.googlecode.sarasvati.example.ExampleUtil;
import com.googlecode.sarasvati.example.LoggingExecutionListener;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.hib.HibGraph;
import com.googlecode.sarasvati.hib.HibGraphProcess;
import com.googlecode.sarasvati.load.DefaultNodeFactory;
import com.googlecode.sarasvati.rubric.env.DefaultRubricFunctionRepository;
import com.googlecode.sarasvati.rubric.env.RubricPredicate;

public class DbConsole
{
  public static boolean log = false;

  public static void main (String[] args) throws Exception
  {
    DefaultRubricFunctionRepository repository = DefaultRubricFunctionRepository.getGlobalInstance();

    repository.registerPredicate( "isRandOdd", new RubricPredicate()
    {
      @Override
      public boolean eval( Engine engine, NodeToken token )
      {
        return token.getEnv().getLongAttribute( "rand" ) % 2 == 1;
      }
    });

    repository.registerPredicate( "isRandEven", new RubricPredicate()
    {
      @Override
      public boolean eval( Engine engine, NodeToken token )
      {
        return token.getEnv().getLongAttribute( "rand" ) % 2 == 0;
      }
    });

    repository.registerPredicate( "isTenthIteration", new RubricPredicate()
    {
      @Override
      public boolean eval( Engine engine, NodeToken token )
      {
        return token.getEnv().getLongAttribute( "iter" ) == 10;
      }
    });

    TestSetup.init();

    DefaultNodeFactory.addGlobalCustomType( "customTest", CustomTestNode.class );

    while ( true )
    {
      Session session = TestSetup.openSession();
      Transaction t = session.beginTransaction();
      HibEngine engine = new HibEngine( session );

      HibGraph graph = getGraph( engine );

      HibGraphProcess process = (HibGraphProcess)engine.startProcess( graph );
      session.flush();
      t.commit();
      session.close();

      runWorkflow( process.getId() );
    }
  }

  @SuppressWarnings("unchecked")
  public static void runWorkflow (long processId)
  {
    while (true)
    {
      Session session = TestSetup.openSession();
      Transaction trans = session.beginTransaction();
      HibEngine engine = new HibEngine( session );

      HibGraphProcess p = engine.getRepository().loadProcess( processId );

      while ( !p.getExecutionQueue().isEmpty() )
      {
        trans.commit();
        session.close();
        session = TestSetup.openSession();
        trans = session.beginTransaction();
        engine = new HibEngine( session );

        p = engine.getRepository().loadProcess( processId );

        ExampleUtil.waitFor( 1000 );
        engine.executeQueuedArcTokens( p );
      }

      if ( p.isComplete() )
      {
        System.out.println( "Workflow complete" );
        return;
      }

      List<Task> tasks =
        session
          .createQuery( "from Task where nodeToken.process = :p order by state" )
          .setEntity( "p", p )
          .list();

      tasks.addAll(
        session
          .createQuery( "from Task where nodeToken.process.parentToken.process = :p order by state" )
          .setEntity( "p", p )
          .list() );

      Task t = null;

      while ( t == null )
      {
        int count = 0;
        for (Task task : tasks )
        {
          System.out.println( (++count) + ": " + task.getName() + " - " + task.getState().getDescription() );
        }

        System.out.print( "> " );

        String input = readLine();

        try
        {
          if ( "log".equals( input ) )
          {
            if ( p.getListeners().isEmpty() )
            {
              engine.addExecutionListener( p, new LoggingExecutionListener(), ExecutionEventType.values() );
            }
            else
            {
              engine.removeExecutionListener( p, new LoggingExecutionListener(), ExecutionEventType.values() );
            }
            break;
          }
          else
          {
            int line = Integer.parseInt( input );
            if ( line > 0 && line <= tasks.size() )
            {
              t = tasks.get( line - 1);
              processTask( t, engine );
            }
            else
            {
              System.out.println( "Please enter a valid number" );
            }
          }
        }
        catch( NumberFormatException nfe )
        {
          System.out.println( "Please enter a valid number" );
        }
      }

      session.flush();
      trans.commit();
      session.close();
    }
  }

  public static void processTask (Task t, HibEngine engine)
  {
    System.out.println( "Task " );
    System.out.println( "\tName        : "  + t.getName() );
    System.out.println( "\tDescription : "  + t.getDescription() );
    System.out.println( "\tState       : "  + t.getState().getDescription() );

    if ( t.getState().getId() != 0 )
    {
      return;
    }

    System.out.println( "1. Complete" );

    if ( t.isRejectable() )
    {
      System.out.println( "2. Reject" );
    }

    System.out.println( "Anything else to cancel" );

    System.out.print( "> " );
    String input = readLine();

    try
    {
      int line = Integer.parseInt( input );
      if ( line == 1 )
      {
        System.out.println( "Completing task" );
        t.setState( (TaskState) engine.getSession().load( TaskState.class, 1 ) );
        engine.completeExecution( t.getNodeToken(), Arc.DEFAULT_ARC );
      }
      else if ( line == 2 && t.isRejectable() )
      {
        System.out.println( "Rejecting task" );
        t.setState( (TaskState) engine.getSession().load( TaskState.class, 2 ) );
        engine.completeExecution( t.getNodeToken(), "reject" );
      }
      else
      {
        System.out.println( "Ok. Doing nothing" );
      }
    }
    catch( NumberFormatException nfe )
    {
      System.out.println( "Ok. Doing nothing" );
    }
  }

  public static HibGraph getGraph (HibEngine engine)
  {
    HibGraph graph = null;

    while ( graph == null )
    {
      List<HibGraph> graphs = engine.getRepository().getGraphs();

      int count = 0;
      for ( HibGraph g : graphs )
      {
        System.out.println( (++count) + ": " + g.getName() + ": version " + g.getVersion() );
      }

      System.out.print( "> " );
      String input = readLine();

      if ( "log".equals( input ) )
      {
        log = !log;
        if ( log )
        {
          engine.addExecutionListener( new LoggingExecutionListener(), ExecutionEventType.values() );
        }
        else
        {
          engine.removeExecutionListener( new LoggingExecutionListener() );
        }
        System.out.println( "Logging set to: " + log );
        continue;
      }

      try
      {
        int line = Integer.parseInt( input );
        if ( line > 0 && line <= graphs.size() )
        {
          graph = graphs.get( line - 1);
        }
        else
        {
          System.out.println( "Please enter a valid number" );
        }
      }
      catch( NumberFormatException nfe )
      {
        System.out.println( "Please enter a valid number" );
      }
    }

    return graph;
  }

  public static String readLine ()
  {
    try
    {
      return new BufferedReader( new InputStreamReader( System.in ) ).readLine();
    }
    catch (IOException ioe )
    {
      throw new RuntimeException( ioe );
    }
  }
}