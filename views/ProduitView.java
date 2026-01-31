package com.restaurant.views;

import com.restaurant.dao.ProduitDAO;
import com.restaurant.dao.CategorieDAO;
import com.restaurant.entités.Produit;
import com.restaurant.entités.Categorie;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Gestion des produits - Code source pur
 * @author DELL
 */
public class ProduitView extends JFrame {
    
    // Composants
    private JTable tableProduits;
    private DefaultTableModel modelProduits;
    private JTextField txtNom;
    private JComboBox<Categorie> cmbCategorie;
    private JTextField txtPrix;
    private JTextField txtStock;
    private JTextField txtSeuil;
    private JButton btnAjouter;
    private JButton btnModifier;
    private JButton btnSupprimer;
    private JButton btnRafraichir;
    private JButton btnFermer;
    
    // DAO
    private final ProduitDAO produitDAO;
    private final CategorieDAO categorieDAO;
    
    public ProduitView() {
        produitDAO = new ProduitDAO();
        categorieDAO = new CategorieDAO();
        initComponents();
        chargerProduits();
        chargerCategories();
    }
    
    private void initComponents() {
        // Configuration de la fenêtre
        setTitle("Gestion des Produits");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.getHSBColor(255,204,204));
        
        // Panel formulaire (gauche)
        JPanel panelFormulaire = new JPanel(new GridBagLayout());
        panelFormulaire.setBorder(BorderFactory.createTitledBorder("Informations Produit"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nom
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulaire.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        txtNom = new JTextField(15);
        panelFormulaire.add(txtNom, gbc);
        
        // Catégorie
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulaire.add(new JLabel("Catégorie :"), gbc);
        gbc.gridx = 1;
        cmbCategorie = new JComboBox<>();
        panelFormulaire.add(cmbCategorie, gbc);
        
        // Prix
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulaire.add(new JLabel("Prix (FCFA) :"), gbc);
        gbc.gridx = 1;
        txtPrix = new JTextField(15);
        panelFormulaire.add(txtPrix, gbc);
        
        // Stock
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulaire.add(new JLabel("Stock actuel :"), gbc);
        gbc.gridx = 1;
        txtStock = new JTextField(15);
        panelFormulaire.add(txtStock, gbc);
        
        // Seuil
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulaire.add(new JLabel("Seuil alerte :"), gbc);
        gbc.gridx = 1;
        txtSeuil = new JTextField(15);
        panelFormulaire.add(txtSeuil, gbc);
        
        // Boutons d'action
        JPanel panelBoutons = new JPanel(new GridLayout(4, 1, 5, 5));
        btnAjouter = new JButton("Ajouter");
        btnAjouter.setFont(new Font("Times new Romain", Font.PLAIN, 13));
        btnAjouter.setBackground(Color.getHSBColor(0,153,102));
        btnAjouter.addActionListener(e -> ajouterProduit());
        panelBoutons.add(btnAjouter);
        
        btnModifier = new JButton("Modifier");
        btnModifier.setFont(new Font("Times new Romain", Font.PLAIN, 13));
        btnModifier.setBackground(Color.getHSBColor(0,153,102));
        btnModifier.addActionListener(e -> modifierProduit());
        panelBoutons.add(btnModifier);
        
        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setFont(new Font("Times new Romain", Font.PLAIN, 13));
        btnSupprimer.setBackground(Color.getHSBColor(0,153,102));
        btnSupprimer.addActionListener(e -> supprimerProduit());
        panelBoutons.add(btnSupprimer);
        
        btnRafraichir = new JButton("Rafraîchir");
        btnRafraichir.setFont(new Font("Times new Romain", Font.PLAIN, 13));
        btnRafraichir.setBackground(Color.getHSBColor(0,153,102));
        btnRafraichir.addActionListener(e -> chargerProduits());
        panelBoutons.add(btnRafraichir);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        panelFormulaire.add(panelBoutons, gbc);
        
        // Panel tableau (droite)
        JPanel panelTableau = new JPanel(new BorderLayout());
        panelTableau.setBorder(BorderFactory.createTitledBorder("Liste des Produits"));
        
        modelProduits = new DefaultTableModel(
            new String[]{"ID", "Nom", "Catégorie", "Prix", "Stock", "Seuil"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableProduits = new JTable(modelProduits);
        tableProduits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableProduits.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectionnerProduit();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableProduits);
        panelTableau.add(scrollPane, BorderLayout.CENTER);
        
        // Bouton fermer
        JPanel panelFermer = new JPanel();
        panelFermer.setBackground(Color.getHSBColor(255,204,204));
        btnFermer = new JButton("Fermer");
        btnFermer.setFont(new Font("Times new Romain", Font.PLAIN, 13));
        btnFermer.setBackground(Color.getHSBColor(0,153,102));
        btnFermer.addActionListener(e -> dispose());
        panelFermer.add(btnFermer);
        
        // Assembler
        mainPanel.add(panelFormulaire, BorderLayout.WEST);
        mainPanel.add(panelTableau, BorderLayout.CENTER);
        mainPanel.add(panelFermer, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void chargerProduits() {
        modelProduits.setRowCount(0);
        List<Produit> produits = produitDAO.listerTous();
        
        for (Produit p : produits) {
            modelProduits.addRow(new Object[]{
                p.getId(),
                p.getNom(),
                p.getCategorie().getLibelle(),
                p.getPrixVente(),
                p.getStockActuel(),
                p.getSeuilAlerte()
            });
        }
    }
    
    private void chargerCategories() {
        cmbCategorie.removeAllItems();
        List<Categorie> categories = categorieDAO.listerTous();
        for (Categorie c : categories) {
            cmbCategorie.addItem(c);
        }
    }
    
    private void selectionnerProduit() {
        int selectedRow = tableProduits.getSelectedRow();
        if (selectedRow >= 0) {
            txtNom.setText(modelProduits.getValueAt(selectedRow, 1).toString());
            String categorie = modelProduits.getValueAt(selectedRow, 2).toString();
            for (int i = 0; i < cmbCategorie.getItemCount(); i++) {
                if (cmbCategorie.getItemAt(i).getLibelle().equals(categorie)) {
                    cmbCategorie.setSelectedIndex(i);
                    break;
                }
            }
            txtPrix.setText(modelProduits.getValueAt(selectedRow, 3).toString());
            txtStock.setText(modelProduits.getValueAt(selectedRow, 4).toString());
            txtSeuil.setText(modelProduits.getValueAt(selectedRow, 5).toString());
        }
    }
    
    private void ajouterProduit() {
        try {
            // Validation
            if (txtNom.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom est obligatoire");
                return;
            }
            
            String nom = txtNom.getText().trim();
            Categorie categorie = (Categorie) cmbCategorie.getSelectedItem();
            double prix = Double.parseDouble(txtPrix.getText());
            int stock = Integer.parseInt(txtStock.getText());
            int seuil = Integer.parseInt(txtSeuil.getText());
            
            if (prix <= 0) {
                JOptionPane.showMessageDialog(this, "Le prix doit être > 0");
                return;
            }
            
            if (stock < 0 || seuil < 0) {
                JOptionPane.showMessageDialog(this, "Stock et seuil doivent être >= 0");
                return;
            }
            
            Produit produit = new Produit(0, nom, categorie, prix, stock, seuil);
            
            if (produitDAO.ajouter(produit)) {
                JOptionPane.showMessageDialog(this, "Produit ajouté avec succès");
                chargerProduits();
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erreur : vérifiez les valeurs numériques");
        }
    }
    
    private void modifierProduit() {
        int selectedRow = tableProduits.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit");
            return;
        }
        
        try {
            int id = (int) modelProduits.getValueAt(selectedRow, 0);
            String nom = txtNom.getText().trim();
            Categorie categorie = (Categorie) cmbCategorie.getSelectedItem();
            double prix = Double.parseDouble(txtPrix.getText());
            int stock = Integer.parseInt(txtStock.getText());
            int seuil = Integer.parseInt(txtSeuil.getText());
            
            Produit produit = new Produit(id, nom, categorie, prix, stock, seuil);
            
            if (produitDAO.modifier(produit)) {
                JOptionPane.showMessageDialog(this, "Produit modifié avec succès");
                chargerProduits();
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erreur : vérifiez les valeurs numériques");
        }
    }
    
    private void supprimerProduit() {
        int selectedRow = tableProduits.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit");
            return;
        }
        
        int choix = JOptionPane.showConfirmDialog(this, 
            "Voulez-vous vraiment supprimer ce produit ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (choix == JOptionPane.YES_OPTION) {
            int id = (int) modelProduits.getValueAt(selectedRow, 0);
            
            if (produitDAO.supprimer(id)) {
                JOptionPane.showMessageDialog(this, "Produit supprimé avec succès");
                chargerProduits();
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression");
            }
        }
    }
    
    private void viderChamps() {
        txtNom.setText("");
        txtPrix.setText("");
        txtStock.setText("");
        txtSeuil.setText("");
        if (cmbCategorie.getItemCount() > 0) {
            cmbCategorie.setSelectedIndex(0);
        }
        tableProduits.clearSelection();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProduitView().setVisible(true);
        });
    }
}