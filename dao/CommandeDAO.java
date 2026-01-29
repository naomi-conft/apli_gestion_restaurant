package com.restaurant.dao;

import com.restaurant.entités.Commande;
import com.restaurant.entités.LigneCommande;
import com.restaurant.controllers.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {
    
    public int creerCommande() {
        String sql = "INSERT INTO Commande (etat, total) VALUES ('EN_COURS', 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean validerCommande(int idCommande) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Récupérer les lignes de la commande
            String sqlLignes = "SELECT id_produit, quantite FROM LigneCommande WHERE id_commande=?";
            PreparedStatement pstLignes = conn.prepareStatement(sqlLignes);
            pstLignes.setInt(1, idCommande);
            ResultSet rs = pstLignes.executeQuery();
            
            // Mettre à jour le stock pour chaque produit
            String sqlStock = "UPDATE Produit SET stockActuel = stockActuel - ? WHERE id = ?";
            PreparedStatement pstStock = conn.prepareStatement(sqlStock);
            
            while (rs.next()) {
                pstStock.setInt(1, rs.getInt("quantite"));
                pstStock.setInt(2, rs.getInt("id_produit"));
                pstStock.executeUpdate();
            }
            
            // Mettre à jour l'état de la commande
            String sqlCmd = "UPDATE Commande SET etat='VALIDEE' WHERE id=?";
            PreparedStatement pstCmd = conn.prepareStatement(sqlCmd);
            pstCmd.setInt(1, idCommande);
            pstCmd.executeUpdate();
            
            conn.commit();
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
    
    public boolean annulerCommande(int idCommande) {
        String sql = "UPDATE Commande SET etat='ANNULEE' WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idCommande);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean mettreAJourTotal(int idCommande) {
        String sql = "UPDATE Commande SET total = (SELECT COALESCE(SUM(montantLigne), 0) FROM LigneCommande WHERE id_commande=?) WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idCommande);
            pst.setInt(2, idCommande);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Commande> listerTous() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM Commande ORDER BY dateCommande DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Commande cmd = new Commande(
                    rs.getInt("id"),
                    rs.getTimestamp("dateCommande"),
                    rs.getString("etat"),
                    rs.getDouble("total")
                );
                commandes.add(cmd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }
    
    public Commande trouverParId(int id) {
        String sql = "SELECT * FROM Commande WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Commande(
                    rs.getInt("id"),
                    rs.getTimestamp("dateCommande"),
                    rs.getString("etat"),
                    rs.getDouble("total")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Méthode pour les statistiques
    public double obtenirCAParJour(Date date) {
        String sql = "SELECT SUM(total) as ca FROM Commande WHERE DATE(dateCommande)=? AND etat='VALIDEE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setDate(1, date);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("ca");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    public double obtenirCAPeriode(Date debut, Date fin) {
        String sql = "SELECT SUM(total) as ca FROM Commande WHERE DATE(dateCommande) BETWEEN ? AND ? AND etat='VALIDEE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setDate(1, debut);
            pst.setDate(2, fin);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("ca");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
