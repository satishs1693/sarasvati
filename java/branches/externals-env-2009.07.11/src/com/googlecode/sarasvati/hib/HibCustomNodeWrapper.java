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

package com.googlecode.sarasvati.hib;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.CustomNodeWrapper;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GuardResponse;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.SarasvatiException;
import com.googlecode.sarasvati.annotations.NodeType;
import com.googlecode.sarasvati.load.LoadException;
import com.googlecode.sarasvati.load.NodeFactory;
import com.googlecode.sarasvati.load.properties.DOMToObjectLoadHelper;

@Entity
@DiscriminatorValue( "custom" )
@NodeType("Generic node allowing for many inputs, many outputs and guards")
public class HibCustomNodeWrapper extends HibPropertyNode implements CustomNodeWrapper
{
  @Transient
  protected CustomNode customNode;

  public HibCustomNodeWrapper () { /* Default constructor for Hibernate */ }

  public HibCustomNodeWrapper (CustomNode customNode)
  {
    this.customNode = customNode;
    customNode.setNodeWrapper( this );
  }

  public CustomNode getCustomNode (Engine engine)
  {
    if ( customNode == null )
    {
      try
      {
        NodeFactory factory = engine.getFactory().getNodeFactory( getType() );
        customNode = (CustomNode)factory.newNode( getType() );
        customNode.setNodeWrapper( this );
        DOMToObjectLoadHelper.setValues( customNode, attrMap );
      }
      catch ( LoadException le )
      {
        throw new SarasvatiException( "Unabled to create CustomNode of type: " + getType(), le );
      }
    }
    return customNode;
  }

  @Override
  public GuardResponse defaultGuard (Engine engine, NodeToken token)
  {
    return super.guard( engine, token );
  }

  @Override
  public <T> T getDefaultAdaptor (Class<T> clazz)
  {
    return super.getAdaptor( clazz );
  }

  @Override
  public void execute (Engine engine, NodeToken token)
  {
    getCustomNode( engine ).execute( engine, token );
  }

  @Override
  public void backtrack (Engine engine, NodeToken token)
  {
    getCustomNode( engine ).backtrack( engine, token );
  }

  @Override
  public boolean isBacktrackable (Engine engine, NodeToken token)
  {
    return getCustomNode( engine ).isBacktrackable( engine, token );
  }
}