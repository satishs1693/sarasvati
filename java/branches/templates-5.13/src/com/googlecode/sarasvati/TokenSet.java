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

    Copyright 2009 Paul Lorenz
*/

package com.googlecode.sarasvati;

import java.util.List;

public interface TokenSet
{
  GraphProcess getProcess ();
  String getName ();

  List<ArcTokenSetMember> getActiveArcTokenSetMembers ();
  List<NodeTokenSetMember> getActiveNodeTokenSetMembers ();

  void addArcTokenSetMember (ArcTokenSetMember setMember);
  void addNodeTokenSetMember (NodeTokenSetMember setMember);

  void arcTokenSetMemberCompleted (ArcTokenSetMember setMember);
  void nodeTokenSetMemberCompleted (NodeTokenSetMember setMember);

  boolean isComplete ();

  /**
   * Marks this token set as being complete, in the sense that a
   * token set join has been performed on the token set.
   */
  void markComplete (Engine engine);
}
