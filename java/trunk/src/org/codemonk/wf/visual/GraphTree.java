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
package org.codemonk.wf.visual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codemonk.wf.Arc;
import org.codemonk.wf.Node;
import org.codemonk.wf.db.HibGraph;
import org.codemonk.wf.db.HibNodeRef;

public class GraphTree
{
  protected Map<HibNodeRef, GraphTreeNode> nodeMap = new HashMap<HibNodeRef, GraphTreeNode>();

  protected GraphTreeNode root = new GraphTreeNode( null, null );

  protected List<List<GraphTreeNode>> layers = new LinkedList<List<GraphTreeNode>>();

  public GraphTree (HibGraph graph)
  {
    List<GraphTreeNode> nextLayer = new LinkedList<GraphTreeNode>();

    List<Node> startNodes = graph.getStartNodes();

    if ( startNodes.isEmpty() )
    {
      List<HibNodeRef> nodeRefs = graph.getNodeRefs();

      if ( !nodeRefs.isEmpty() )
      {
        startNodes = new ArrayList<Node>(1);
        startNodes.add( nodeRefs.get( 0 ) );
      }
    }

    for ( Node node : startNodes )
    {
      HibNodeRef nodeRef = (HibNodeRef)node;
      GraphTreeNode treeNode = GraphTreeNode.newInstance( root, nodeRef );

      nodeMap.put( nodeRef, treeNode );
      treeNode.addToLayer( nextLayer );
    }


    List<GraphTreeNode> layer = null;

    while ( !nextLayer.isEmpty() )
    {
      layers.add( nextLayer );
      layer = nextLayer;
      nextLayer = new LinkedList<GraphTreeNode>();

      for ( GraphTreeNode treeNode : layer )
      {
        List<Arc> arcs = graph.getOutputArcs( treeNode.getNode() );

        for ( Arc arc : arcs )
        {
          HibNodeRef target = (HibNodeRef)arc.getEndNode();
          GraphTreeNode targetTreeNode = nodeMap.get( target );

          if (targetTreeNode == null)
          {
            targetTreeNode = GraphTreeNode.newInstance( treeNode, target );
            nodeMap.put( target, targetTreeNode );
            targetTreeNode.addToLayer( nextLayer );
          }
        }
      }
    }
  }

  public GraphTreeNode getTreeNode (HibNodeRef node)
  {
    return nodeMap.get( node );
  }

  public int getLayerCount ()
  {
    return layers.size();
  }

  public List<GraphTreeNode> getLayer (int index)
  {
    return layers.get( index );
  }

  public List<List<GraphTreeNode>> getLayers ()
  {
    return layers;
  }

  public Iterable<GraphTreeNode> getGraphTreeNodes ()
  {
    return nodeMap.values();
  }
}