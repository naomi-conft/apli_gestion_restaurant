package com.restaurant.views;

import com.restaurant.dao.CommandeDAO;
import com.restaurant.dao.LigneCommandeDAO;
import com.restaurant.entités.Commande;
import com.restaurant.entités.LigneCommande;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Interface de liste et gestion des commandes
 * @author DELL
 */
public class ListeCommandeView extends JFrame {
    
    // Composants principaux
    private JTable tableCommandes;
    private DefaultTableModel modelCommandes;
    private JTable tableDetails;
    private DefaultTableModel modelDetails;
    
    // Filtres
    private JComboBox<String> cmbFiltre;
    private JTextField txtRecherche;
    private JButton btnRechercher;
    private JButton btnRafraichir;
    
    // Actions
    private JButton btnVoirDetails;
    private JButton btnChangerEtat;
    private JButton btnSupprimerCommande;
    private JButton btnFermer;
    
    // Labels info
    private JLabel lblTotal;
    private JLabel lblCommandeSelectionnee;
    
    // DAO
    private final CommandeDAO commandeDAO;
    private final LigneCommandeDAO ligneCommandeDAO;
    
    // Commande sélectionnée
    private int commandeSelectionnee = 0;
    
    public ListeCommandeView() {
        commandeDAO = new CommandeDAO();
        ligneCommandeDAO = new LigneCommandeDAO();
        
        initComponents();
        chargerCommandes();
    }
    
    private void initComponents() {
        setTitle("📋 Liste des Commandes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // === PANEL FILTRES (HAUT) ===
        JPanel panelFiltres = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltres.setBorder(BorderFactory.createTitledBorder("Filtres"));
        
        panelFiltres.add(new JLabel("État :"));
        cmbFiltre = new JComboBox<>(new String[]{
            "TOUTES",
            "EN_COURS",
            "VALIDEE",
            "ANNULEE"
        });
        cmbFiltre.addActionListener(e -> filtrerCommandes());
        panelFiltres.add(cmbFiltre);
        
        panelFiltres.add(Box.createHorizontalStrut(20));
        
        panelFiltres.add(new JLabel("Recherche (ID) :"));
        txtRecherche = new JTextField(10);
        panelFiltres.add(txtRecherche);
        
        btnRechercher = new JButton("🔍 Rechercher");
        btnRechercher.addActionListener(e -> rechercherCommande());
        panelFiltres.add(btnRechercher);
        
        btnRafraichir = new JButton("🔄 Rafraîchir");
        btnRafraichir.setBackground(new Color(33, 150, 243));
        btnRafraichir.setForeground(Color.WHITE);
        btnRafraichir.addActionListener(e -> chargerCommandes());
        panelFiltres.add(btnRafraichir);
        
        // === PANEL CENTRE (SPLIT) ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        splitPane.setDividerLocation(350);
        
        // Panel tableau commandes (haut du split)
        JPanel panelCommandes = new JPanel(new BorderLayout());
        panelCommandes.setBorder(BorderFactory.createTitledBorder("Liste des Commandes"));
        
        modelCommandes = new DefaultTableModel(
            new String[]{"ID", "Date", "État", "Total (FCFA)", "Nb Articles"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableCommandes = new JTable(modelCommandes);
        tableCommandes.setRowHeight(30);
        tableCommandes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCommandes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                afficherDetailsCommande();
            }
        });
        
        // Renderer pour colorer selon l'état
        tableCommandes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String etat = (String) table.getValueAt(row, 2);
                    switch (etat) {
                        case "EN_COURS":
                            c.setBackground(new Color(255, 243, 224)); // Orange clair
                            break;
                        case "VALIDEE":
                            c.setBackground(new Color(232, 245, 233)); // Vert clair
                            break;
                        case "ANNULEE":
                            c.setBackground(new Color(255, 235, 238)); // Rouge clair
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                    }
                }
                
                // Colorer l'état avec une couleur plus vive
                if (column == 2) {
                    String etat = value.toString();
                    Font boldFont = new Font(c.getFont().getName(), Font.BOLD, 12);
                    c.setFont(boldFont);
                    switch (etat) {
                        case "EN_COURS":
                            c.setForeground(new Color(255, 152, 0));
                            break;
                        case "VALIDEE":
                            c.setForeground(new Color(76, 175, 80));
                            break;
                        case "ANNULEE":
                            c.setForeground(new Color(244, 67, 54));
                            break;
                    }
                } else if (!isSelected) {
                    c.setForeground(Color.BLACK);
                }
                
                // Centrer toutes les colonnes sauf la date
                if (column != 1) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                }
                
                return c;
            }
        });
        
        JScrollPane scrollCommandes = new JScrollPane(tableCommandes);
        panelCommandes.add(scrollCommandes, BorderLayout.CENTER);
        
        // Panel info commande sélectionnée
        JPanel panelInfoCommande = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblCommandeSelectionnee = new JLabel("Sélectionnez une commande pour voir les détails");
        lblCommandeSelectionnee.setFont(new Font("Arial", Font.BOLD, 12));
        panelInfoCommande.add(lblCommandeSelectionnee);
        panelCommandes.add(panelInfoCommande, BorderLayout.SOUTH);
        
        // Panel détails (bas du split)
        JPanel panelDetails = new JPanel(new BorderLayout());
        panelDetails.setBorder(BorderFactory.createTitledBorder("Détails de la Commande"));
        
        modelDetails = new DefaultTableModel(
            new String[]{"Produit", "Prix Unitaire", "Quantité", "Montant"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableDetails = new JTable(modelDetails);
        tableDetails.setRowHeight(25);
        JScrollPane scrollDetails = new JScrollPane(tableDetails);
        panelDetails.add(scrollDetails, BorderLayout.CENTER);
        
        // Total
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total : 0 FCFA");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(new Color(0, 128, 0));
        panelTotal.add(lblTotal);
        panelDetails.add(panelTotal, BorderLayout.SOUTH);
        
        // Ajouter au split pane
        splitPane.setTopComponent(panelCommandes);
        splitPane.setBottomComponent(panelDetails);
        
        // === PANEL ACTIONS (BAS) ===
        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnVoirDetails = new JButton("👁️ Voir Détails");
        btnVoirDetails.setEnabled(false);
        btnVoirDetails.addActionListener(e -> afficherDetailsComplets());
        panelActions.add(btnVoirDetails);
        
        btnChangerEtat = new JButton("🔄 Changer État");
        btnChangerEtat.setBackground(new Color(255, 152, 0));
        btnChangerEtat.setForeground(Color.WHITE);
        btnChangerEtat.setEnabled(false);
        btnChangerEtat.addActionListener(e -> changerEtatCommande());
        panelActions.add(btnChangerEtat);
        
        btnSupprimerCommande = new JButton("🗑️ Supprimer");
        btnSupprimerCommande.setBackground(new Color(244, 67, 54));
        btnSupprimerCommande.setForeground(Color.WHITE);
        btnSupprimerCommande.setEnabled(false);
        btnSupprimerCommande.addActionListener(e -> supprimerCommande());
        panelActions.add(btnSupprimerCommande);
        
        btnFermer = new JButton("❌ Fermer");
        btnFermer.addActionListener(e -> dispose());
        panelActions.add(btnFermer);
        
        // Assembler
        mainPanel.add(panelFiltres, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(panelActions, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void chargerCommandes() {
        System.out.println("\n=== CHARGEMENT DES COMMANDES ===");
        
        modelCommandes.setRowCount(0);
        commandeSelectionnee = 0;
        modelDetails.setRowCount(0);
        lblCommandeSelectionnee.setText("Sélectionnez une commande pour voir les détails");
        lblTotal.setText("Total : 0 FCFA");
        
        try {
            List<Commande> commandes = commandeDAO.listerTous();
            System.out.println("Nombre de commandes : " + commandes.size());
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Commande c : commandes) {
                // Compter les articles
                List<LigneCommande> lignes = ligneCommandeDAO.listerParCommande(c.getId());
                int nbArticles = lignes.stream().mapToInt(LigneCommande::getQuantite).sum();
                
                modelCommandes.addRow(new Object[]{
                    c.getId(),
                    sdf.format(c.getDateCommande()),
                    c.getEtat(),
                    String.format("%.0f", c.getTotal()),
                    nbArticles
                });
            }
            
            System.out.println("✅ Commandes chargées");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur chargement commandes :");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des commandes :\n" + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void filtrerCommandes() {
        String filtre = (String) cmbFiltre.getSelectedItem();
        System.out.println("Filtre : " + filtre);
        
        if ("TOUTES".equals(filtre)) {
            chargerCommandes();
            return;
        }
        
        modelCommandes.setRowCount(0);
        
        try {
            List<Commande> commandes = commandeDAO.listerTous();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Commande c : commandes) {
                if (c.getEtat().equals(filtre)) {
                    List<LigneCommande> lignes = ligneCommandeDAO.listerParCommande(c.getId());
                    int nbArticles = lignes.stream().mapToInt(LigneCommande::getQuantite).sum();
                    
                    modelCommandes.addRow(new Object[]{
                        c.getId(),
                        sdf.format(c.getDateCommande()),
                        c.getEtat(),
                        String.format("%.0f", c.getTotal()),
                        nbArticles
                    });
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void rechercherCommande() {
        String recherche = txtRecherche.getText().trim();
        
        if (recherche.isEmpty()) {
            chargerCommandes();
            return;
        }
        
        try {
            int id = Integer.parseInt(recherche);
            
            modelCommandes.setRowCount(0);
            Commande c = commandeDAO.trouverParId(id);
            
            if (c != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                List<LigneCommande> lignes = ligneCommandeDAO.listerParCommande(c.getId());
                int nbArticles = lignes.stream().mapToInt(LigneCommande::getQuantite).sum();
                
                modelCommandes.addRow(new Object[]{
                    c.getId(),
                    sdf.format(c.getDateCommande()),
                    c.getEtat(),
                    String.format("%.0f", c.getTotal()),
                    nbArticles
                });
            } else {
                JOptionPane.showMessageDialog(this,
                    "Aucune commande trouvée avec l'ID : " + id,
                    "Recherche",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Veuillez saisir un numéro de commande valide",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void afficherDetailsCommande() {
        int selectedRow = tableCommandes.getSelectedRow();
        
        if (selectedRow < 0) {
            btnVoirDetails.setEnabled(false);
            btnChangerEtat.setEnabled(false);
            btnSupprimerCommande.setEnabled(false);
            commandeSelectionnee = 0;
            modelDetails.setRowCount(0);
            lblCommandeSelectionnee.setText("Sélectionnez une commande pour voir les détails");
            lblTotal.setText("Total : 0 FCFA");
            return;
        }
        
        btnVoirDetails.setEnabled(true);
        btnChangerEtat.setEnabled(true);
        btnSupprimerCommande.setEnabled(true);
        
        commandeSelectionnee = (int) modelCommandes.getValueAt(selectedRow, 0);
        String etat = (String) modelCommandes.getValueAt(selectedRow, 2);
        String total = (String) modelCommandes.getValueAt(selectedRow, 3);
        
        lblCommandeSelectionnee.setText("Commande #" + commandeSelectionnee + " - " + etat);
        lblTotal.setText("Total : " + total + " FCFA");
        
        // Charger les lignes
        modelDetails.setRowCount(0);
        
        try {
            List<LigneCommande> lignes = ligneCommandeDAO.listerParCommande(commandeSelectionnee);
            
            for (LigneCommande l : lignes) {
                modelDetails.addRow(new Object[]{
                    l.getProduit().getNom(),
                    String.format("%.0f", l.getPrixunitaire()),
                    l.getQuantite(),
                    String.format("%.0f", l.getMontantligne())
                });
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void afficherDetailsComplets() {
        if (commandeSelectionnee == 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une commande",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            Commande c = commandeDAO.trouverParId(commandeSelectionnee);
            List<LigneCommande> lignes = ligneCommandeDAO.listerParCommande(commandeSelectionnee);
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy à HH:mm");
            
            StringBuilder details = new StringBuilder();
            details.append("═════════════════════════════════════════════════════════════════════════════════\n");
            details.append("           COMMANDE #").append(c.getId()).append("\n");
            details.append("═════════════════════════════════════════════════════════════════════════════════\n\n");
            details.append("📅 Date : ").append(sdf.format(c.getDateCommande())).append("\n");
            details.append("📊 État : ").append(c.getEtat()).append("\n");
            details.append("💰 Total : ").append(String.format("%.0f", c.getTotal())).append(" FCFA\n\n");
            details.append("─────────────────────────────────────────────────────────────────────────────────\n");
            details.append("DÉTAILS DES ARTICLES\n");
            details.append("─────────────────────────────────────────────────────────────────────────────────\n\n");
            
            for (LigneCommande l : lignes) {
                details.append("• ").append(l.getProduit().getNom()).append("\n");
                details.append("  ").append(l.getQuantite()).append(" x ")
                       .append(String.format("%.0f", l.getPrixunitaire()))
                       .append(" FCFA = ")
                       .append(String.format("%.0f", l.getMontantligne()))
                       .append(" FCFA\n\n");
            }
            
            details.append("══════════════════════════════════════════════════════════════════════════════════s\n");
            
            // JTextArea et JScrollPane remplacés par DetailsCommandeDialog
            // JTextArea textArea = new JTextArea(details.toString());
            // ...

            
            /* 
            JOptionPane.showMessageDialog(this,
                scrollPane,
                "Détails Commande #" + commandeSelectionnee,
                JOptionPane.INFORMATION_MESSAGE);
            */
            
            // Utilisation du nouveau dialogue avec bouton d'impression
            DetailsCommandeDialog dialog = new DetailsCommandeDialog(
                this, 
                "Détails Commande #" + commandeSelectionnee, 
                details.toString()
            );
            dialog.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void changerEtatCommande() {
        if (commandeSelectionnee == 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une commande",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            Commande c = commandeDAO.trouverParId(commandeSelectionnee);
            String etatActuel = c.getEtat();
            
            // Créer le dialog de changement d'état
            String[] etats = {"EN_COURS", "VALIDEE", "ANNULEE"};
            
            JComboBox<String> comboEtats = new JComboBox<>(etats);
            comboEtats.setSelectedItem(etatActuel);
            
            JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
            panel.add(new JLabel("Commande #" + commandeSelectionnee));
            panel.add(new JLabel("État actuel : " + etatActuel));
            panel.add(new JLabel("Nouvel état :"));
            panel.add(comboEtats);
            
            int result = JOptionPane.showConfirmDialog(this,
                panel,
                "Changer l'état de la commande",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String nouvelEtat = (String) comboEtats.getSelectedItem();
                
                if (nouvelEtat.equals(etatActuel)) {
                    JOptionPane.showMessageDialog(this,
                        "L'état n'a pas changé",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                // Validation selon les règles métier
                if (etatActuel.equals("VALIDEE") && nouvelEtat.equals("EN_COURS")) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Une commande validée ne peut pas repasser en cours !",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (etatActuel.equals("ANNULEE") && nouvelEtat.equals("VALIDEE")) {
                    JOptionPane.showMessageDialog(this,
                        "❌ Une commande annulée ne peut pas être validée !",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Confirmer le changement
                String message = "Confirmer le changement d'état ?\n\n" +
                                 "Commande #" + commandeSelectionnee + "\n" +
                                 etatActuel + " → " + nouvelEtat;
                
                if (nouvelEtat.equals("VALIDEE")) {
                    message += "\n\n⚠️ ATTENTION : Le stock sera déduit !";
                }
                
                int confirm = JOptionPane.showConfirmDialog(this,
                    message,
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = false;
                    
                    // Appliquer le changement selon le nouvel état
                    switch (nouvelEtat) {
                        case "VALIDEE":
                            success = commandeDAO.validerCommande(commandeSelectionnee);
                            break;
                        case "ANNULEE":
                            success = commandeDAO.annulerCommande(commandeSelectionnee);
                            break;
                        case "EN_COURS":
                            // Modifier directement l'état
                            success = commandeDAO.changerEtat(commandeSelectionnee, "EN_COURS");
                            break;
                    }
                    
                    if (success) {
                        JOptionPane.showMessageDialog(this,
                            "✅ État modifié avec succès !\n\n" +
                            "Commande #" + commandeSelectionnee + " : " + nouvelEtat,
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                        chargerCommandes();
                        // Resélectionner la même commande
                        for (int i = 0; i < modelCommandes.getRowCount(); i++) {
                            if ((int)modelCommandes.getValueAt(i, 0) == commandeSelectionnee) {
                                tableCommandes.setRowSelectionInterval(i, i);
                                break;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "❌ Erreur lors du changement d'état",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erreur : " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void supprimerCommande() {
        if (commandeSelectionnee == 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une commande",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            Commande c = commandeDAO.trouverParId(commandeSelectionnee);
            
            // Empêcher la suppression des commandes validées
            if (c.getEtat().equals("VALIDEE")) {
                JOptionPane.showMessageDialog(this,
                    "❌ Impossible de supprimer une commande validée !\n\n" +
                    "Pour supprimer cette commande, vous devez d'abord\n" +
                    "la passer en état ANNULEE.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "⚠️ ATTENTION : Supprimer définitivement la commande ?\n\n" +
                "Commande #" + commandeSelectionnee + "\n" +
                "État : " + c.getEtat() + "\n" +
                "Total : " + String.format("%.0f", c.getTotal()) + " FCFA\n\n" +
                "Cette action est IRRÉVERSIBLE !",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (commandeDAO.supprimer(commandeSelectionnee)) {
                    JOptionPane.showMessageDialog(this,
                        "✅ Commande supprimée avec succès",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                    chargerCommandes();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "❌ Erreur lors de la suppression",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erreur : " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ListeCommandeView().setVisible(true);
        });
    }
}