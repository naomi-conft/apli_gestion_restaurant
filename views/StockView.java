/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.restaurant.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;




   

public class StockView extends javax.swing.JFrame {
     
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StockView.class.getName());

    private DefaultTableModel modelStock;
    
    public StockView() {
        initComponents();
        setTitle("Gestion du stock");
        setLocationRelativeTo(null);
        initialiserStock();
    }

    private void initialiserStock() {
        modelStock = new DefaultTableModel(
            new Object[]{"ID", "Produit", "Catégorie", "Stock"}, 0
        );

        tableStock.setModel(modelStock);

        // Données de test
        modelStock.addRow(new Object[]{1, "Pizza", "Plat", 15});
        modelStock.addRow(new Object[]{2, "Burger", "Plat", 10});
        modelStock.addRow(new Object[]{3, "Jus", "Boisson", 25});
    }
    private void modifierStock(boolean entree) {
        int row = tableStock.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit");
            return;
        }

        int quantite;
        try {
            quantite = Integer.parseInt(txtQuantite.getText());
            if (quantite <= 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Quantité invalide");
            return;
        }

        int stockActuel = (int) modelStock.getValueAt(row, 3);
        int nouveauStock = entree ? stockActuel + quantite : stockActuel - quantite;

        if (nouveauStock < 2) {
            JOptionPane.showMessageDialog(this, "Seuil attends!");
            return;
        }

        modelStock.setValueAt(nouveauStock, row, 3);
        txtQuantite.setText("");
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableStock = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtQuantite = new javax.swing.JTextField();
        btnEntrer = new javax.swing.JButton();
        btnSortie = new javax.swing.JButton();
        btnFermer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Produit", "Categorie", "Stock"
            }
        ));
        jScrollPane1.setViewportView(tableStock);

        jLabel1.setText("Quantite voulu :");

        btnEntrer.setText("Entrer");
        btnEntrer.addActionListener(this::btnEntrerActionPerformed);

        btnSortie.setText("Sortie");
        btnSortie.addActionListener(this::btnSortieActionPerformed);

        btnFermer.setText("Fermer");
        btnFermer.addActionListener(this::btnFermerActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQuantite, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSortie)
                            .addComponent(btnEntrer)
                            .addComponent(btnFermer))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtQuantite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addComponent(btnEntrer)
                        .addGap(34, 34, 34)
                        .addComponent(btnSortie)
                        .addGap(32, 32, 32)
                        .addComponent(btnFermer)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEntrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrerActionPerformed
        // TODO add your handling code here:
        modifierStock(true);
    }//GEN-LAST:event_btnEntrerActionPerformed

    private void btnSortieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortieActionPerformed
        // TODO add your handling code here:
        modifierStock(false);
    }//GEN-LAST:event_btnSortieActionPerformed

    private void btnFermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFermerActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnFermerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new StockView().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntrer;
    private javax.swing.JButton btnFermer;
    private javax.swing.JButton btnSortie;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableStock;
    private javax.swing.JTextField txtQuantite;
    // End of variables declaration//GEN-END:variables
    }
