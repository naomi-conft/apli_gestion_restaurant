package com.restaurant.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe de connexion à la base de données MySQL
 * Pattern Singleton pour une seule instance de connexion
 * @author DELL
 */
public class DBConnection {
    
    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
    
    // Configuration de la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_restaurant";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Modifie selon ta configuration
    
    // Paramètres de connexion
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Instance unique (Singleton - optionnel)
    private static Connection connection = null;
    
    /**
     * Constructeur privé pour empêcher l'instanciation
     */
    private DBConnection() {
    }
    
    /**
     * Obtenir une connexion à la base de données
     * @return Connection ou null en cas d'erreur
     */
    public static Connection getConnection() {
        try {
            // Charger le pilote MySQL
            Class.forName(DRIVER);
            
            // Créer la connexion
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            logger.log(Level.INFO, "Connexion à la base de données établie avec succès");
            return conn;
            
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Pilote MySQL introuvable : " + e.getMessage());
            System.err.println("ERREUR : Pilote MySQL introuvable !");
            System.err.println("Assurez-vous d'avoir ajouté mysql-connector-java.jar aux librairies du projet");
            return null;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur de connexion à la base de données : " + e.getMessage());
            System.err.println("ERREUR DE CONNEXION À LA BASE DE DONNÉES !");
            System.err.println("Détails : " + e.getMessage());
            System.err.println("\nVérifiez que :");
            System.err.println("1. MySQL est démarré (XAMPP/WAMP)");
            System.err.println("2. La base 'gestion_restaurant' existe");
            System.err.println("3. L'utilisateur et le mot de passe sont corrects");
            System.err.println("4. Le port 3306 est le bon");
            return null;
        }
    }
    
    /**
     * Version Singleton - obtenir ou créer une connexion unique
     * (Utiliser seulement si vous voulez une connexion partagée)
     */
    public static Connection getSingletonConnection() {
        if (connection == null) {
            connection = getConnection();
        }
        return connection;
    }
    
    /**
     * Fermer une connexion
     * @param conn Connection à fermer
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                logger.log(Level.INFO, "Connexion fermée avec succès");
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
    
    /**
     * Tester la connexion à la base de données
     * @return true si la connexion fonctionne
     */
    public static boolean testConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.close();
                System.out.println("✅ Test de connexion réussi !");
                return true;
            } catch (SQLException e) {
                System.err.println("❌ Erreur lors du test de connexion : " + e.getMessage());
                return false;
            }
        } else {
            System.err.println("❌ Test de connexion échoué !");
            return false;
        }
    }
    
    /**
     * Méthode main pour tester la connexion
     */
    public static void main(String[] args) {
        System.out.println("=== TEST DE CONNEXION À LA BASE DE DONNÉES ===\n");
        
        System.out.println("Configuration :");
        System.out.println("URL : " + URL);
        System.out.println("Utilisateur : " + USER);
        System.out.println("Mot de passe : " + (PASSWORD.isEmpty() ? "(vide)" : "****"));
        System.out.println("\nTest en cours...\n");
        
        if (testConnection()) {
            System.out.println("\n✅ La connexion à la base de données fonctionne parfaitement !");
            System.out.println("Vous pouvez maintenant lancer l'application.");
        } else {
            System.out.println("\n❌ La connexion a échoué.");
            System.out.println("Consultez les messages d'erreur ci-dessus pour corriger le problème.");
        }
    }
}