/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PreferencesPanel.java
 *
 * Created on Jul 3, 2009, 1:18:54 PM
 */

package com.googlecode.sarasvati.editor.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author paul
 */
public class PreferencesPanel extends javax.swing.JPanel {

  private static final long serialVersionUID = 1L;

    /** Creates new form PreferencesPanel */
    public PreferencesPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        prefsTree = new javax.swing.JTree();

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Preferences");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("General");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Node Types");
        treeNode1.add(treeNode2);
        prefsTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(prefsTree);

        splitPane.setLeftComponent(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree prefsTree;
    private javax.swing.JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables

    protected List<BasePrefsPage> prefPages = new ArrayList<BasePrefsPage>( 3 );

    public void setup ()
    {
      jScrollPane1.setMinimumSize( jScrollPane1.getPreferredSize() );
      prefPages.add( new BasePrefsPage() );
      prefPages.add( new GeneralPreferencesPanel() );
      prefPages.add( new NodeTypePreferencesPanel() );

      splitPane.setRightComponent( prefPages.get( 0 ) );

      prefsTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
      prefsTree.getSelectionModel().addTreeSelectionListener( new TreeSelectionListener()
      {
        @Override
        public void valueChanged (TreeSelectionEvent e)
        {
          int[] rows = prefsTree.getSelectionModel().getSelectionRows();
          BasePrefsPage prefsPage = null;
          if ( rows == null || rows.length == 0 )
          {
            prefsPage = prefPages.get( 0 );
          }
          else
          {
            prefsPage = prefPages.get( rows[0] );
          }

          splitPane.setRightComponent( prefsPage );

          if ( !prefsPage.isSetupDone() )
          {
            prefsPage.setup();
            prefsPage.setSetupDone( true );
          }
          prefsPage.displayPage();
        }
      });
    }

    public void selectGeneral ()
    {
      Object root = prefsTree.getModel().getRoot();
      Object general = prefsTree.getModel().getChild( root, 0 );
      TreePath path = new TreePath( new Object[] { root, general } );
      prefsTree.addSelectionPath( path );
    }
}
