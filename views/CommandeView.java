package com.restaurant.views;

import com.restaurant.dao.CommandeDAO;
import com.restaurant.dao.LigneCommandeDAO;
import com.restaurant.dao.ProduitDAO;
import com.restaurant.entités.Produit;
import com.restaurant.entités.LigneCommande;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Gestion des commandes - Code source pur
 * @author DELL
 */
public class CommandeView extends JFrame {
    
    // Composants
    private JTable tableLignes;
    private DefaultTableModel modelLignes;
    private JComboBox<Produit> cmbProduit;
    private JTextField txtQuantite;
    private JButton btnAjouterLigne;
    private JButton btnSupprimerLigne;
    private JButton btnNouvelleCommande;
    private JButton btnValiderCommande;
    private JButton btnAnnulerCommande;
    private JButton btnFermer;
    private JLabel lblTotal;
    private JLabel lblEtat;
    
    // Variables
    private int commandeEnCours = 0;
    
    // DAO
    private CommandeDAO commandeDAO;
    private LigneCommandeDAO ligneCommandeDAO;
    private ProduitDAO produitDAO;
    
    public CommandeView() {
        commandeDAO = new CommandeDAO();
        ligneCommandeDAO = new LigneCommandeDAO();
        produitDAO = new ProduitDAO();
        initComponents();
        chargerProduits();
    }
    
    private void initComponents() {
        // Configuration de la fenêtre
        setTitle("Gestion des Commandes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel info commande (haut)
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Commande en cours"));
        lblEtat = new JLabel("Aucune commande en cours");
        lblEtat.setFont(new Font("Arial", Font.BOLD, 14));
        panelInfo.add(lblEtat);
        
        // Panel ajout produit
        JPanel panelAjout = new JPanel(new GridBagLayout());
        panelAjout.setBorder(BorderFactory.createTitledBorder("Ajouter un produit"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelAjout.add(new JLabel("Produit :"), gbc);
        gbc.gridx = 1;
        cmbProduit = new JComboBox<>();
        cmbProduit.setPreferredSize(new Dimension(200, 25));
        panelAjout.add(cmbProduit, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        panelAjout.add(new JLabel("Quantité :"), gbc);
        gbc.gridx = 3;
        txtQuantite = new JTextField(5);
        panelAjout.add(txtQuantite, gbc);
        
        gbc.gridx = 4; gbc.gridy = 0;
        btnAjouterLigne = new JButton("Ajouter");
        btnAjouterLigne.addActionListener(e -> ajouterLigne());
        btnAjouterLigne.setEnabled(false);
        panelAjout.add(btnAjouterLigne, gbc);
        
        // Panel tableau
        JPanel panelTableau = new JPanel(new BorderLayout());
        panelTableau.setBorder(BorderFactory.createTitledBorder("Lignes de la commande"));
        
        modelLignes = new DefaultTableModel(
            new String[]{"ID Ligne", "Produit", "Prix Unitaire", "Quantité", "Montant"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableLignes = new JTable(modelLignes);
        JScrollPane scrollPane = new JScrollPane(tableLignes);
        panelTableau.add(scrollPane, BorderLayout.CENTER);
        
        // Panel total
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total : 0 FCFA");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelTotal.add(lblTotal);
        panelTableau.add(panelTotal, BorderLayout.SOUTH);
        
        // Panel boutons
        JPanel panelBoutons = new JPanel(new GridLayout(2, 3, 10, 10));
        
        btnNouvelleCommande = new JButton("Nouvelle Commande");
        btnNouvelleCommande.addActionListener(e -> creerNouvelleCommande());
        panelBoutons.add(btnNouvelleCommande);
        
        btnSupprimerLigne = new JButton("Supprimer Ligne");
        btnSupprimerLigne.addActionListener(e -> supprimerLigne());
        btnSupprimerLigne.setEnabled(false);
        panelBoutons.add(btnSupprimerLigne);
        
        btnValiderCommande = new JButton("Valider Commande");
        btnValiderCommande.addActionListener(e -> validerCommande());
        btnValiderCommande.setEnabled(false);
        panelBoutons.add(btnValiderCommande);
        
        btnAnnulerCommande = new JButton("Annuler Commande");
        btnAnnulerCommande.addActionListener(e -> annulerCommande());
        btnAnnulerCommande.setEnabled(false);
        panelBoutons.add(btnAnnulerCommande);
        
        btnFermer = new JButton("Fermer");
        btnFermer.addActionListener(e -> dispose());
        panelBoutons.add(btnFermer);
        
        // Assembler
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(panelInfo, BorderLayout.NORTH);
        topPanel.add(panelAjout, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
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
    
    private void creerNouvelleCommande() {
        int id = commandeDAO.creerCommande();
        if (id > 0) {
            commandeEnCours = id;
            lblEtat.setText("Commande #" + id + " - EN COURS");
            modelLignes.setRowCount(0);
            lblTotal.setText("Total : 0 FCFA");
            
            btnAjouterLigne.setEnabled(true);
            btnSupprimerLigne.setEnabled(true);
            btnValiderCommande.setEnabled(true);
            btnAnnulerCommande.setEnabled(true);
            btnNouvelleCommande.setEnabled(false);
            
            JOptionPane.showMessageDialog(this, "Nouvelle commande créée #" + id);
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création");
        }
    }
    
    private void ajouterLigne() {
        if (commandeEnCours == 0) {
            JOptionPane.showMessageDialog(this, "Créez d'abord une commande");
            return;
        }
        
        try {
            if (cmbProduit.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un produit");
                return;
            }
            
            Produit produit = (Produit) cmbProduit.getSelectedItem();
            int quantite = Integer.parseInt(txtQuantite.getText().trim());
            
            if (quantite <= 0) {
                JOptionPane.showMessageDialog(this, "Quantité doit être > 0");
                return;
            }
            
            // Vérifier le stock
            if (produit.getStockActuel() < quantite) {
                JOptionPane.showMessageDialog(this, 
                    "Stock insuffisant !\nDisponible : " + produit.getStockActuel());
                return;
            }
            
            LigneCommande ligne = new LigneCommande(
                0, commandeEnCours, produit, quantite, produit.getPrixVente()
            );
            
            if (ligneCommandeDAO.ajouter(ligne)) {
                chargerLignes();
                calculerTotal();
                txtQuantite.setText("");
                JOptionPane.showMessageDialog(this, "Produit ajouté à la commande");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantité invalide");
        }
    }
    
    private void chargerLignes() {
        if (commandeEnCours == 0) return;
        
        modelLignes.setRowCount(0);
        List<LigneCommande> lignes = ligneCommandeDAO.listerParCommande(commandeEnCours);
        
        for (LigneCommande l : lignes) {
            modelLignes.addRow(new Object[]{
                l.getId(),
                l.getProduit().getNom(),
                l.getPrixunitaire(),
                l.getQuantite(),
                l.getMontantligne()
            });
        }
    }
    
    private void calculerTotal() {
        if (commandeEnCours == 0) return;
        
        double total = 0;
        for (int i = 0; i < modelLignes.getRowCount(); i++) {
            total += (double) modelLignes.getValueAt(i, 4);
        }
        
        lblTotal.setText(String.format("Total : %.0f FCFA", total));
        commandeDAO.mettreAJourTotal(commandeEnCours);
    }
    
    private void supprimerLigne() {
        int selectedRow = tableLignes.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une ligne");
            return;
        }
        
        int choix = JOptionPane.showConfirmDialog(this, 
            "Supprimer cette ligne ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (choix == JOptionPane.YES_OPTION) {
            int idLigne = (int) modelLignes.getValueAt(selectedRow, 0);
            
            if (ligneCommandeDAO.supprimerLigne(idLigne)) {
                chargerLignes();
                calculerTotal();
                JOptionPane.showMessageDialog(this, "Ligne supprimée");
            }
        }
    }
    
    private void validerCommande() {
        if (commandeEnCours == 0) return;
        
        if (modelLignes.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "La commande doit contenir au moins un produit");
            return;
        }
        
        int choix = JOptionPane.showConfirmDialog(this, 
            "Valider la commande ?\nLe stock sera automatiquement déduit.", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (choix == JOptionPane.YES_OPTION) {
            if (commandeDAO.validerCommande(commandeEnCours)) {
                JOptionPane.showMessageDialog(this, 
                    "Commande validée avec succès !\nLe stock a été mis à jour.");
                reinitialiserCommande();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la validation");
            }
        }
    }
    
    private void annulerCommande() {
        if (commandeEnCours == 0) return;
        
        int choix = JOptionPane.showConfirmDialog(this, 
            "Annuler la commande en cours ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (choix == JOptionPane.YES_OPTION) {
            if (commandeDAO.annulerCommande(commandeEnCours)) {
                JOptionPane.showMessageDialog(this, "Commande annulée");
                reinitialiserCommande();
            }
        }
    }
    
    private void reinitialiserCommande() {
        commandeEnCours = 0;
        lblEtat.setText("Aucune commande en cours");
        modelLignes.setRowCount(0);
        lblTotal.setText("Total : 0 FCFA");
        
        btnAjouterLigne.setEnabled(false);
        btnSupprimerLigne.setEnabled(false);
        btnValiderCommande.setEnabled(false);
        btnAnnulerCommande.setEnabled(false);
        btnNouvelleCommande.setEnabled(true);
        
        chargerProduits(); // Recharger pour avoir les stocks à jour
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CommandeView().setVisible(true);
        });
    }
}