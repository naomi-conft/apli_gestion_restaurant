package com.restaurant.views;

import com.restaurant.dao.UtilisateurDAO;
import com.restaurant.entités.Utilisateurs;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Écran de connexion - Code source pur
 * @author DELL
 */
public class LoginView extends JFrame {
    
    // Composants
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JButton btnConnexion;
    private JLabel lblLogin;
    private JLabel lblPassword;
    private JLabel lblTitre;
    
    // DAO
    private UtilisateurDAO utilisateurDAO;
    
    public LoginView() {
        utilisateurDAO = new UtilisateurDAO();
        initComponents();
    }
    
    private void initComponents() {
        // Configuration de la fenêtre
        setTitle("Connexion - Gestion Restaurant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Titre
        lblTitre = new JLabel("🍽️ GESTION RESTAURANT");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(lblTitre, gbc);
        
        // Label Login
        lblLogin = new JLabel("Utilisateur :");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(lblLogin, gbc);
        
        // Champ Login
        txtLogin = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtLogin, gbc);
        
        // Label Password
        lblPassword = new JLabel("Mot de passe :");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(lblPassword, gbc);
        
        // Champ Password
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtPassword, gbc);
        
        // Bouton Connexion
        btnConnexion = new JButton("Se connecter");
        btnConnexion.addActionListener(e -> btnConnexionActionPerformed());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(btnConnexion, gbc);
        
        // Ajouter le panel à la fenêtre
        add(mainPanel);
        
        // Enter pour se connecter
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnConnexionActionPerformed();
                }
            }
        });
    }
    
    private void btnConnexionActionPerformed() {
        String login = txtLogin.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        // Validation
        if (login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez remplir tous les champs", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Authentification
        Utilisateurs user = utilisateurDAO.authentifier(login, password);
        
        if (user != null) {
            JOptionPane.showMessageDialog(this, 
                "Bienvenue " + user.getLogin() + " !", 
                "Succès", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Ouvrir le menu principal
            MainView mainView = new MainView();
            mainView.setVisible(true);
            
            // Fermer la fenêtre de connexion
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Login ou mot de passe incorrect", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
