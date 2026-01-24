package com.restaurant.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_restaurant";
    private static final String USER = "root"; // Par défaut sur XAMPP/WAMP
    private static final String PASSWORD = "M@SQL25MD"; // Vide par défaut

    public static Connection getConnection() {
        try {
            // Chargement du pilote
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            return null;
        }
    }
}