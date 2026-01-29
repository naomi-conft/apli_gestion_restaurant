package com.restaurant.views;

import javax.swing.*;
import java.awt.*;

/**
 * Menu principal - Code source pur
 * @author DELL
 */
public class MainView extends JFrame {
    
    // Composants
    private JButton btnProduits;
    private JButton btnStock;
    private JButton btnCommandes;
    private JButton btnStatistiques;
    private JButton btnDeconnexion;
    private JLabel lblTitre;
    
    public MainView() {
        initComponents();
    }
    
    private void initComponents() {
        // Configuration de la fenêtre
        setTitle("Menu Principal - Gestion Restaurant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel titre
        JPanel panelTitre = new JPanel();
        panelTitre.setBackground(new Color(245, 245, 245));
        lblTitre = new JLabel("🍽️ GESTION RESTAURANT");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
        panelTitre.add(lblTitre);
        
        // Panel des boutons
        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new GridLayout(4, 1, 10, 15));
        panelBoutons.setBackground(new Color(245, 245, 245));
        
        // Bouton Produits
        btnProduits = new JButton("📦 Gestion des Produits");
        btnProduits.setFont(new Font("Arial", Font.BOLD, 16));
        btnProduits.setPreferredSize(new Dimension(400, 60));
        btnProduits.addActionListener(e -> ouvrirProduits());
        panelBoutons.add(btnProduits);
        
        // Bouton Stock
        btnStock = new JButton("📊 Gestion du Stock");
        btnStock.setFont(new Font("Arial", Font.BOLD, 16));
        btnStock.setPreferredSize(new Dimension(400, 60));
        btnStock.addActionListener(e -> ouvrirStock());
        panelBoutons.add(btnStock);
        
        // Bouton Commandes
        btnCommandes = new JButton("🛒 Gestion des Commandes");
        btnCommandes.setFont(new Font("Arial", Font.BOLD, 16));
        btnCommandes.setPreferredSize(new Dimension(400, 60));
        btnCommandes.addActionListener(e -> ouvrirCommandes());
        panelBoutons.add(btnCommandes);
        
        // Bouton Statistiques
        btnStatistiques = new JButton("📈 Statistiques");
        btnStatistiques.setFont(new Font("Arial", Font.BOLD, 16));
        btnStatistiques.setPreferredSize(new Dimension(400, 60));
        btnStatistiques.addActionListener(e -> ouvrirStatistiques());
        panelBoutons.add(btnStatistiques);
        
        // Panel déconnexion
        JPanel panelDeconnexion = new JPanel();
        panelDeconnexion.setBackground(new Color(245, 245, 245));
        btnDeconnexion = new JButton("🚪 Déconnexion");
        btnDeconnexion.setFont(new Font("Arial", Font.PLAIN, 14));
        btnDeconnexion.addActionListener(e -> deconnexion());
        panelDeconnexion.add(btnDeconnexion);
        
        // Ajouter les panels
        mainPanel.add(panelTitre, BorderLayout.NORTH);
        mainPanel.add(panelBoutons, BorderLayout.CENTER);
        mainPanel.add(panelDeconnexion, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void ouvrirProduits() {
        ProduitView produitView = new ProduitView();
        produitView.setVisible(true);
    }
    
    private void ouvrirStock() {
        StockView stockView = new StockView();
        stockView.setVisible(true);
    }
    
    private void ouvrirCommandes() {
        CommandeView commandeView = new CommandeView();
        commandeView.setVisible(true);
    }
    
    private void ouvrirStatistiques() {
        StatistiqueView statistiqueView = new StatistiqueView();
        statistiqueView.setVisible(true);
    }
    
    private void deconnexion() {
        int choix = JOptionPane.showConfirmDialog(this, 
            "Voulez-vous vraiment vous déconnecter ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (choix == JOptionPane.YES_OPTION) {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            dispose();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainView().setVisible(true);
        });
    }
}