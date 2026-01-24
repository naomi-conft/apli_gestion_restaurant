package com.restaurant.dao;

import com.restaurant.entités.Produit;
import com.restaurant.controllers.DBConnection;
import java.sql.*;

public class ProduitDAO {
    
    public boolean ajouter(Produit p) {
        // Le prix doit être strictement positif [cite: 40]
        if (p.getPrixVente() <= 0) return false;

        String sql = "INSERT INTO Produit (nom, id_categorie, prixVente, stockActuel, seuilAlerte) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, p.getNom());
            pst.setInt(2, p.getCategorie().getId());
            pst.setDouble(3, p.getPrixVente());
            pst.setInt(4, p.getStockActuel());
            pst.setInt(5, p.getSeuilAlerte());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Méthode pour lister les produits en alerte [cite: 50, 72]
    public ResultSet getProduitsEnAlerte() {
        String sql = "SELECT * FROM Produit WHERE stockActuel <= seuilAlerte";
        try {
            Connection conn = DBConnection.getConnection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            return null;
        }
    }
}