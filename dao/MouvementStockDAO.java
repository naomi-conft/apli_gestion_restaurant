package com.restaurant.dao;

import com.restaurant.entités.MouvementStock;
import com.restaurant.entités.Produit;
import com.restaurant.controllers.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MouvementStockDAO {
    
    // Enregistrer une entrée ou sortie
    public boolean enregistrerMouvement(int idProduit, String type, int quantite, String motif) {
        // Validation
        if (quantite <= 0) return false;
        
        // Pour une sortie, vérifier la disponibilité du stock
        if (type.equals("SORTIE")) {
            if (!stockDisponible(idProduit, quantite)) {
                return false;
            }
        }
        
        String sqlMvt = "INSERT INTO mouvementstock (id_produit, type_mouvement, quantite, motif) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = (type.equals("ENTREE")) 
            ? "UPDATE produit SET stockActuel = stockActuel + ? WHERE id = ?"
            : "UPDATE produit SET stockActuel = stockActuel - ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Début transaction

            // 1. Créer le mouvement
            PreparedStatement pstMvt = conn.prepareStatement(sqlMvt);
            pstMvt.setInt(1, idProduit);
            pstMvt.setString(2, type);
            pstMvt.setInt(3, quantite);
            pstMvt.setString(4, motif);
            pstMvt.executeUpdate();

            // 2. Mettre à jour le stock du produit
            PreparedStatement pstUpd = conn.prepareStatement(sqlUpdateStock);
            pstUpd.setInt(1, quantite);
            pstUpd.setInt(2, idProduit);
            pstUpd.executeUpdate();

            conn.commit(); // Valider les deux opérations
            return true;
        } catch (SQLException e) {
            try { 
                if(conn != null) conn.rollback(); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private boolean stockDisponible(int idProduit, int quantite) {
        String sql = "SELECT stockActuel FROM produit WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idProduit);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("stockActuel") >= quantite;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<MouvementStock> listerTous() {
        List<MouvementStock> mouvements = new ArrayList<>();
        String sql = "SELECT m.*, p.nom FROM mouvementstock m INNER JOIN produit p ON m.id_produit = p.id ORDER BY m.dateMouvement DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            ProduitDAO produitDAO = new ProduitDAO();
            while (rs.next()) {
                Produit p = produitDAO.trouverParId(rs.getInt("id_produit"));
                MouvementStock mvt = new MouvementStock(
                    rs.getInt("id"),
                    p,
                    rs.getString("type_mouvement"),
                    rs.getInt("quantite"),
                    rs.getTimestamp("dateMouvement"),
                    rs.getString("motif")
                );
                mouvements.add(mvt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mouvements;
    }
    
    public List<MouvementStock> listerParProduit(int idProduit) {
        List<MouvementStock> mouvements = new ArrayList<>();
        String sql = "SELECT * FROM mouvementstock WHERE id_produit=? ORDER BY dateMouvement DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idProduit);
            ResultSet rs = pst.executeQuery();
            
            ProduitDAO produitDAO = new ProduitDAO();
            while (rs.next()) {
                Produit p = produitDAO.trouverParId(rs.getInt("id_produit"));
                MouvementStock mvt = new MouvementStock(
                    rs.getInt("id"),
                    p,
                    rs.getString("type_mouvement"),
                    rs.getInt("quantite"),
                    rs.getTimestamp("dateMouvement"),
                    rs.getString("motif")
                );
                mouvements.add(mvt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mouvements;
    }
}
