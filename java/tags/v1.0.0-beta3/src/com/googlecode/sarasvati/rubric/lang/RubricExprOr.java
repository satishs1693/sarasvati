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

package com.googlecode.sarasvati.rubric.lang;

import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.rubric.visitor.RubricVisitor;

public class RubricExprOr implements RubricExpr
{
  protected RubricExpr left;
  protected RubricExpr right;

  public RubricExprOr (RubricExpr left, RubricExpr right)
  {
    this.left = left;
    this.right = right;
  }

  public RubricExpr getLeft ()
  {
    return left;
  }

  public RubricExpr getRight ()
  {
    return right;
  }

  @Override
  public boolean eval (RubricEnv env)
  {
    return left.eval( env ) || right.eval( env );
  }

  @Override
  public void traverse (RubricVisitor visitor)
  {
    visitor.visit( this );
    left.traverse( visitor );
    right.traverse( visitor );
  }
}
