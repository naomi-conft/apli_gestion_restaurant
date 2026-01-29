package com.restaurant.views;

import com.restaurant.dao.MouvementStockDAO;
import com.restaurant.dao.ProduitDAO;
import com.restaurant.entités.Produit;
import com.restaurant.entités.MouvementStock;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Gestion du stock - Code source pur
 * @author DELL
 */
public class StockView extends JFrame {
    
    // Composants
    private JTable tableMouvements;
    private DefaultTableModel modelMouvements;
    private JComboBox<Produit> cmbProduit;
    private JComboBox<String> cmbType;
    private JTextField txtQuantite;
    private JTextField txtMotif;
    private JButton btnEnregistrer;
    private JButton btnRafraichir;
    private JButton btnAlertes;
    private JButton btnFermer;
    
    // DAO
    private MouvementStockDAO mouvementDAO;
    private ProduitDAO produitDAO;
    
    public StockView() {
        mouvementDAO = new MouvementStockDAO();
        produitDAO = new ProduitDAO();
        initComponents();
        chargerProduits();
        chargerMouvements();
    }
    
    private void initComponents() {
        // Configuration de la fenêtre
        setTitle("Gestion du Stock");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel formulaire (haut)
        JPanel panelFormulaire = new JPanel(new GridBagLayout());
        panelFormulaire.setBorder(BorderFactory.createTitledBorder("Nouveau Mouvement"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Produit
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulaire.add(new JLabel("Produit :"), gbc);
        gbc.gridx = 1;
        cmbProduit = new JComboBox<>();
        cmbProduit.setPreferredSize(new Dimension(200, 25));
        panelFormulaire.add(cmbProduit, gbc);
        
        // Type
        gbc.gridx = 2; gbc.gridy = 0;
        panelFormulaire.add(new JLabel("Type :"), gbc);
        gbc.gridx = 3;
        cmbType = new JComboBox<>(new String[]{"ENTREE", "SORTIE"});
        panelFormulaire.add(cmbType, gbc);
        
        // Quantité
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulaire.add(new JLabel("Quantité :"), gbc);
        gbc.gridx = 1;
        txtQuantite = new JTextField(10);
        panelFormulaire.add(txtQuantite, gbc);
        
        // Motif
        gbc.gridx = 2; gbc.gridy = 1;
        panelFormulaire.add(new JLabel("Motif :"), gbc);
        gbc.gridx = 3;
        txtMotif = new JTextField(15);
        panelFormulaire.add(txtMotif, gbc);
        
        // Bouton enregistrer
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        btnEnregistrer = new JButton("Enregistrer le mouvement");
        btnEnregistrer.addActionListener(e -> enregistrerMouvement());
        panelFormulaire.add(btnEnregistrer, gbc);
        
        // Panel tableau
        JPanel panelTableau = new JPanel(new BorderLayout());
        panelTableau.setBorder(BorderFactory.createTitledBorder("Historique des Mouvements"));
        
        modelMouvements = new DefaultTableModel(
            new String[]{"ID", "Produit", "Type", "Quantité", "Date", "Motif"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMouvements = new JTable(modelMouvements);
        JScrollPane scrollPane = new JScrollPane(tableMouvements);
        panelTableau.add(scrollPane, BorderLayout.CENTER);
        
        // Panel boutons
        JPanel panelBoutons = new JPanel(new FlowLayout());
        btnRafraichir = new JButton("Rafraîchir");
        btnRafraichir.addActionListener(e -> chargerMouvements());
        panelBoutons.add(btnRafraichir);
        
        btnAlertes = new JButton("Afficher Alertes");
        btnAlertes.addActionListener(e -> afficherAlertes());
        panelBoutons.add(btnAlertes);
        
        btnFermer = new JButton("Fermer");
        btnFermer.addActionListener(e -> dispose());
        panelBoutons.add(btnFermer);
        
        // Assembler
        mainPanel.add(panelFormulaire, BorderLayout.NORTH);
        mainPanel.add(panelTableau, BorderLayout.CENTER);
        mainPanel.add(panelBoutons, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void chargerProduits() {
        cmbProduit.removeAllItems();
        List<Produit> produits = produitDAO.listerTous();
        for (Produit p : produits) {
            cmbProduit.addItem(p);
        }
    }
    
    private void chargerMouvements() {
        modelMouvements.setRowCount(0);
        List<MouvementStock> mouvements = mouvementDAO.listerTous();
        
        for (MouvementStock m : mouvements) {
            modelMouvements.addRow(new Object[]{
                m.getId(),
                m.getProduit().getNom(),
                m.getType(),
                m.getQuantite(),
                m.getDateMouvement(),
                m.getMotif()
            });
        }
    }
    
    private void enregistrerMouvement() {
        try {
            // Validation
            if (cmbProduit.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit");
                return;
            }
            
            if (txtQuantite.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir une quantité");
                return;
            }
            
            Produit produit = (Produit) cmbProduit.getSelectedItem();
            String type = (String) cmbType.getSelectedItem();
            int quantite = Integer.parseInt(txtQuantite.getText().trim());
            String motif = txtMotif.getText().trim();
            
            if (quantite <= 0) {
                JOptionPane.showMessageDialog(this, "La quantité doit être > 0");
                return;
            }
            
            // Enregistrer
            if (mouvementDAO.enregistrerMouvement(produit.getId(), type, quantite, motif)) {
                JOptionPane.showMessageDialog(this, 
                    "Mouvement enregistré avec succès\nLe stock a été mis à jour");
                chargerMouvements();
                chargerProduits(); // Recharger pour avoir les stocks à jour
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de l'enregistrement\nVérifiez que le stock est suffisant pour une sortie");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantité invalide");
        }
    }
    
    private void afficherAlertes() {
        List<Produit> alertes = produitDAO.getProduitsEnAlerte();
        
        if (alertes.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Aucun produit en alerte\nTous les stocks sont normaux !", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder("Produits en alerte :\n\n");
            for (Produit p : alertes) {
                message.append(String.format("• %s : %d unités (seuil: %d)\n", 
                    p.getNom(), p.getStockActuel(), p.getSeuilAlerte()));
            }
            
            JOptionPane.showMessageDialog(this, 
                message.toString(), 
                "Alerte Stock", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void viderChamps() {
        txtQuantite.setText("");
        txtMotif.setText("");
        if (cmbProduit.getItemCount() > 0) {
            cmbProduit.setSelectedIndex(0);
        }
        cmbType.setSelectedIndex(0);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StockView().setVisible(true);
        });
    }
}
