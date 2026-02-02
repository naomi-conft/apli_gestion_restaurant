package com.restaurant.views;

import com.restaurant.dao.CommandeDAO;
import com.restaurant.dao.LigneCommandeDAO;
import com.restaurant.dao.ProduitDAO;
import com.restaurant.entités.Produit;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Interface statistiques - Code source pur
 * @author DELL
 */
public class StatistiqueView extends JFrame {
    
    // Composants
    private JTextField txtDateJour;
    private JTextField txtDateDebut;
    private JTextField txtDateFin;
    private JLabel lblResultatJour;
    private JLabel lblResultatPeriode;
    private JTable tableStats;
    private DefaultTableModel modelStats;
    private JButton btnCalculerJour;
    private JButton btnCalculerPeriode;
    private JButton btnTopProduits;
    private JButton btnAlertes;
    private JButton btnFermer;
    
    // DAO
    private final CommandeDAO commandeDAO;
    private final LigneCommandeDAO ligneCommandeDAO;
    private final ProduitDAO produitDAO;
    
    public StatistiqueView() {
        commandeDAO = new CommandeDAO();
        ligneCommandeDAO = new LigneCommandeDAO();
        produitDAO = new ProduitDAO();
        initComponents();
        initialiserDates();
    }
    
    private void initComponents() {
        // Configuration de la fenêtre
        setTitle("Statistiques");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.getHSBColor(255,204,204));
        
        // Panel gauche (formulaires)
        JPanel panelGauche = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // CA du jour
        JPanel panelJour = new JPanel(new GridBagLayout());
        panelJour.setBorder(BorderFactory.createTitledBorder("CA du Jour"));
        GridBagConstraints gbcJour = new GridBagConstraints();
        gbcJour.insets = new Insets(5, 5, 5, 5);
        gbcJour.fill = GridBagConstraints.HORIZONTAL;
        
        gbcJour.gridx = 0; gbcJour.gridy = 0;
        panelJour.add(new JLabel("Date :"), gbcJour);
        gbcJour.gridx = 1;
        txtDateJour = new JTextField(10);
        panelJour.add(txtDateJour, gbcJour);
        
        gbcJour.gridx = 0; gbcJour.gridy = 1;
        gbcJour.gridwidth = 2;
        btnCalculerJour = new JButton("Calculer");
        btnCalculerJour.setFont(new Font("Times new Romain", Font.PLAIN, 13));
        btnCalculerJour.setBackground(Color.getHSBColor(0,153,102));
        btnCalculerJour.addActionListener(e -> calculerCAJour());
        panelJour.add(btnCalculerJour, gbcJour);
        
        gbcJour.gridy = 2;
        lblResultatJour = new JLabel("CA : 0 FCFA");
        lblResultatJour.setFont(new Font("Arial", Font.BOLD, 14));
        panelJour.add(lblResultatJour, gbcJour);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelGauche.add(panelJour, gbc);
        
        // CA période
        JPanel panelPeriode = new JPanel(new GridBagLayout());
        panelPeriode.setBorder(BorderFactory.createTitledBorder("CA sur Période"));
        GridBagConstraints gbcPeriode = new GridBagConstraints();
        gbcPeriode.insets = new Insets(5, 5, 5, 5);
        gbcPeriode.fill = GridBagConstraints.HORIZONTAL;
        
        gbcPeriode.gridx = 0; gbcPeriode.gridy = 0;
        panelPeriode.add(new JLabel("Date début :"), gbcPeriode);
        gbcPeriode.gridx = 1;
        txtDateDebut = new JTextField(10);
        panelPeriode.add(txtDateDebut, gbcPeriode);
        
        gbcPeriode.gridx = 0; gbcPeriode.gridy = 1;
        panelPeriode.add(new JLabel("Date fin :"), gbcPeriode);
        gbcPeriode.gridx = 1;
        txtDateFin = new JTextField(10);
        panelPeriode.add(txtDateFin, gbcPeriode);
        
        gbcPeriode.gridx = 0; gbcPeriode.gridy = 2;
        gbcPeriode.gridwidth = 2;
        btnCalculerPeriode = new JButton("Calculer");
        btnCalculerPeriode.setFont(new Font("Times new Romain", Font.PLAIN, 13));
        btnCalculerPeriode.setBackground(Color.getHSBColor(0,153,102));
        btnCalculerPeriode.addActionListener(e -> calculerCAPeriode());
        panelPeriode.add(btnCalculerPeriode, gbcPeriode);
        
        gbcPeriode.gridy = 3;
        lblResultatPeriode = new JLabel("CA : 0 FCFA");
        lblResultatPeriode.setFont(new Font("Arial", Font.BOLD, 14));
        panelPeriode.add(lblResultatPeriode, gbcPeriode);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelGauche.add(panelPeriode, gbc);
        
        // Boutons actions
        JPanel panelActions = new JPanel(new GridLayout(2, 1, 5, 5));
        btnTopProduits = new JButton("Top Produits");
        btnTopProduits.setFont(new Font("Times new Romain", Font.PLAIN, 13));
        btnTopProduits.setBackground(Color.getHSBColor(0,153,102));
        btnTopProduits.addActionListener(e -> afficherTopProduits());
        panelActions.add(btnTopProduits);
        
        btnAlertes = new JButton("Alertes Stock");
        btnAlertes.setFont(new Font("Times new Romain", Font.PLAIN, 13));
        btnAlertes.setBackground(Color.getHSBColor(0,153,102));
        btnAlertes.addActionListener(e -> afficherAlertes());
        panelActions.add(btnAlertes);
        
        gbc.gridy = 2;
        panelGauche.add(panelActions, gbc);
        
        // Panel tableau (droite)
        JPanel panelTableau = new JPanel(new BorderLayout());
        panelTableau.setBorder(BorderFactory.createTitledBorder("Résultats"));
        
        modelStats = new DefaultTableModel(
            new String[]{"Produit", "Quantité vendue", "Stock restant", "Seuil", "État"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableStats = new JTable(modelStats);
        JScrollPane scrollPane = new JScrollPane(tableStats);
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
        mainPanel.add(panelGauche, BorderLayout.WEST);
        mainPanel.add(panelTableau, BorderLayout.CENTER);
        mainPanel.add(panelFermer, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void initialiserDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String aujourdHui = LocalDate.now().format(formatter);
        String debutMois = LocalDate.now().withDayOfMonth(1).format(formatter);
        
        txtDateJour.setText(aujourdHui);
        txtDateDebut.setText(debutMois);
        txtDateFin.setText(aujourdHui);
    }
    
    private void calculerCAJour() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(txtDateJour.getText().trim(), formatter);
            
            Date sqlDate = Date.valueOf(date);
            double ca = commandeDAO.obtenirCAParJour(sqlDate);
            
            lblResultatJour.setText(String.format("CA : %.0f FCFA", ca));
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Format de date invalide\nUtilisez : dd/MM/yyyy", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void calculerCAPeriode() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate debut = LocalDate.parse(txtDateDebut.getText().trim(), formatter);
            LocalDate fin = LocalDate.parse(txtDateFin.getText().trim(), formatter);
            
            if (fin.isBefore(debut)) {
                JOptionPane.showMessageDialog(this, 
                    "La date de fin doit être après la date de début");
                return;
            }
            
            Date sqlDebut = Date.valueOf(debut);
            Date sqlFin = Date.valueOf(fin);
            double ca = commandeDAO.obtenirCAPeriode(sqlDebut, sqlFin);
            
            lblResultatPeriode.setText(String.format("CA : %.0f FCFA", ca));
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Format de date invalide\nUtilisez : dd/MM/yyyy", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void afficherTopProduits() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate debut = LocalDate.parse(txtDateDebut.getText().trim(), formatter);
            LocalDate fin = LocalDate.parse(txtDateFin.getText().trim(), formatter);
            
            modelStats.setRowCount(0);
            
            Date sqlDebut = Date.valueOf(debut);
            Date sqlFin = Date.valueOf(fin);
            
            List<Object[]> topProduits = ligneCommandeDAO.getTopProduits(sqlDebut, sqlFin);
            
            for (Object[] row : topProduits) {
                String nomProduit = (String) row[0];
                int quantiteVendue = (int) row[1];
                
                // Récupérer les infos de stock
                List<Produit> produits = produitDAO.listerTous();
                Produit produit = produits.stream()
                    .filter(p -> p.getNom().equals(nomProduit))
                    .findFirst()
                    .orElse(null);
                
                if (produit != null) {
                    int stock = produit.getStockActuel();
                    int seuil = produit.getSeuilAlerte();
                    String etat = stock == 0 ? "Rupture" : 
                                  stock <= seuil ? "Stock faible" : 
                                  "Normal";
                    
                    modelStats.addRow(new Object[]{
                        nomProduit, quantiteVendue, stock, seuil, etat
                    });
                }
            }
            
            if (topProduits.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Aucune vente sur cette période");
            }
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide");
        }
    }
    
    private void afficherAlertes() {
        modelStats.setRowCount(0);
        
        List<Produit> alertes = produitDAO.getProduitsEnAlerte();
        
        for (Produit p : alertes) {
            String etat = p.getStockActuel() == 0 ? "Rupture" : "Stock faible";
            
            modelStats.addRow(new Object[]{
                p.getNom(),
                "N/A",
                p.getStockActuel(),
                p.getSeuilAlerte(),
                etat
            });
        }
        
        if (alertes.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Aucun produit en alerte\nTous les stocks sont normaux !");
        } else {
            JOptionPane.showMessageDialog(this, 
                alertes.size() + " produit(s) en alerte !", 
                "Alerte Stock", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StatistiqueView().setVisible(true);
        });
    }
}