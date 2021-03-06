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

package com.googlecode.sarasvati;

public abstract class CustomNode implements Node
{
  protected CustomNodeWrapper nodeWrapper;

  public void setNodeWrapper (CustomNodeWrapper nodeWrapper)
  {
    this.nodeWrapper = nodeWrapper;
  }

  public Node getNodeWrapper ()
  {
    return nodeWrapper;
  }

  @Override
  public final Graph getGraph ()
  {
    return nodeWrapper.getGraph();
  }

  @Override
  public final String getGuard ()
  {
    return nodeWrapper.getGuard();
  }

  @Override
  public final Long getId ()
  {
    return nodeWrapper.getId();
  }

  @Override
  public final String getName ()
  {
    return nodeWrapper.getName();
  }

  @Override
  public final String getType ()
  {
    return nodeWrapper.getType();
  }

  @Override
  public final boolean isExternal ()
  {
    return nodeWrapper.isExternal();
  }

  @Override
  public final boolean isJoin ()
  {
    return nodeWrapper.isJoin();
  }

  @Override
  public final boolean isStart ()
  {
    return nodeWrapper.isStart();
  }

  /**
   * Does nothing by default. May be overridden by subclasses.
   *
   * <p>
   *
   * IMPORTANT NOTE: Do not call {@link CustomNodeWrapper#backtrack(NodeToken)} as that
   * will just call your isBacktrackable method again, resulting in an recursive call,
   * which will exit when your VM runs out of stack space.
   *
   * @see Node#isBacktrackable(NodeToken)
   */
  @Override public void backtrack (NodeToken token)
  {
    // does nothing by default
  }

  /**
   * Returns true by default. May be overridden by subclasses.
   *
   * <p>
   *
   * IMPORTANT NOTE: Do not call {@link CustomNodeWrapper#isBacktrackable(NodeToken)} as that
   * will just call your isBacktrackable method again, resulting in an recursive call,
   * which will exit when your VM runs out of stack space.
   *
   * @see Node#isBacktrackable(NodeToken)
   */
  @Override public boolean isBacktrackable (NodeToken token)
  {
    return true;
  }

  /**
   * Default implementation calls {@link CustomNodeWrapper#getDefaultAdaptor(Class)}.
   * May be overridden by subclasses.
   *
   * <p>
   *
   * IMPORTANT NOTE: Do not call {@link CustomNodeWrapper#getAdaptor(Class)} as that
   * will just call your getAdapter method again, resulting in an recursive call,
   * which will exit when your VM runs out of stack space.
   *
   * @see Node#getAdaptor(Class)
   */
  @Override public <T> T getAdaptor (Class<T> clazz)
  {
    return nodeWrapper.getDefaultAdaptor( clazz );
  }

  /**
   * Default implementation calls {@link CustomNodeWrapper#defaultGuard(Engine, NodeToken)}.
   * May be overridden by subclasses.
   *
   * <p>
   *
   * IMPORTANT NOTE: Do not call {@link CustomNodeWrapper#guard(Engine, NodeToken)} as that
   * will just call your guard method again, resulting in an recursive call,
   * which will exit when your VM runs out of stack space.
   *
   * @see Node#guard(Engine, NodeToken)
   */
  @Override public GuardResponse guard (Engine engine, NodeToken token)
  {
    return nodeWrapper.defaultGuard( engine, token );
  }
}