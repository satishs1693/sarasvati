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

package com.googlecode.sarasvati.editor.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import com.googlecode.sarasvati.editor.command.CommandStack;
import com.googlecode.sarasvati.editor.model.EditorGraph;
import com.googlecode.sarasvati.editor.model.EditorNode;
import com.googlecode.sarasvati.editor.model.GraphState;

/**
 * @author Paul Lorenz
 */
@SuppressWarnings({"synthetic-access", "rawtypes", "unchecked"})
public class GraphPropertiesPanel extends javax.swing.JPanel {

  private static final long serialVersionUID = 1L;

    /** Creates new form ArcPropertiesPanel */
    public GraphPropertiesPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fromLabel = new javax.swing.JLabel();
        defaultInNodeInput = new javax.swing.JComboBox();
        toLabel = new javax.swing.JLabel();
        defaultOutNodeInput = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();

        fromLabel.setText(org.openide.util.NbBundle.getMessage(GraphPropertiesPanel.class, "GraphPropertiesPanel.fromLabel.text")); // NOI18N

        defaultInNodeInput.setModel(getDefaultInNodeComboBoxModel());

        toLabel.setText(org.openide.util.NbBundle.getMessage(GraphPropertiesPanel.class, "GraphPropertiesPanel.toLabel.text")); // NOI18N

        defaultOutNodeInput.setModel(getDefaultOutNodeComboBoxModel());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        cancelButton.setText(org.openide.util.NbBundle.getMessage(GraphPropertiesPanel.class, "GraphPropertiesPanel.cancelButton.text")); // NOI18N
        cancelButton.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel1.add(cancelButton);

        applyButton.setText(org.openide.util.NbBundle.getMessage(GraphPropertiesPanel.class, "GraphPropertiesPanel.applyButton.text")); // NOI18N
        applyButton.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel1.add(applyButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fromLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(defaultInNodeInput, 0, 286, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(toLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(defaultOutNodeInput, 0, 285, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fromLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(defaultInNodeInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(defaultOutNodeInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(toLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox defaultInNodeInput;
    private javax.swing.JComboBox defaultOutNodeInput;
    private javax.swing.JLabel fromLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel toLabel;
    // End of variables declaration//GEN-END:variables

    private final DefaultComboBoxModel defaultInNodeComboBoxModel = new DefaultComboBoxModel();
    private final DefaultComboBoxModel defaultOutNodeComboBoxModel = new DefaultComboBoxModel();

    private ComboBoxModel getDefaultInNodeComboBoxModel ()
    {
      return defaultInNodeComboBoxModel;
    }

    private ComboBoxModel getDefaultOutNodeComboBoxModel ()
    {
      return defaultOutNodeComboBoxModel;
    }

    public void setup (final JDialog dialog,
                       final EditorGraph graph)
    {
      cancelButton.addActionListener( new ActionListener()
      {
        @Override
        public void actionPerformed (final ActionEvent e)
        {
          dialog.setVisible( false );
          dialog.dispose();
        }
      });

      applyButton.addActionListener( new ActionListener()
      {
        @Override
        public void actionPerformed (final ActionEvent e)
        {
          GraphState newState = new GraphState( (String)defaultInNodeInput.getSelectedItem(),
                                                (String)defaultOutNodeInput.getSelectedItem() );

          if ( !newState.equals( graph.getState() ) )
          {
            CommandStack.editGraph( graph, newState );
          }

          dialog.setVisible( false );
          dialog.dispose();
        }
      });

      defaultInNodeComboBoxModel.addElement( null );
      defaultOutNodeComboBoxModel.addElement( null );

      for ( EditorNode node : graph.getNodes() )
      {
        defaultInNodeComboBoxModel.addElement( node.getName() );
        defaultOutNodeComboBoxModel.addElement( node.getName() );
      }

      defaultInNodeComboBoxModel.setSelectedItem( graph.getState().getDefaultNodeForIncomingArcs() );
      defaultOutNodeComboBoxModel.setSelectedItem( graph.getState().getDefaultNodeForOutgoingArcs() );
    }
}
