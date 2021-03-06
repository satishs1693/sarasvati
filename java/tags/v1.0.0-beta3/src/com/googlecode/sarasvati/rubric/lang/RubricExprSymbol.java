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

public class RubricExprSymbol implements RubricExpr
{
  protected String symbol;

  public RubricExprSymbol (String symbol)
  {
    this.symbol = symbol;
  }

  public String getSymbol ()
  {
    return symbol;
  }

  @Override
  public boolean eval (RubricEnv env)
  {
    return env.evalPredicate( symbol );
  }

  @Override
  public void traverse (RubricVisitor visitor)
  {
    visitor.visit( this );
  }
}
