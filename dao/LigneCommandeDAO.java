package com.restaurant.dao;

import com.restaurant.entités.LigneCommande;
import com.restaurant.entités.Produit;
import com.restaurant.controllers.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO {
    
    public boolean ajouter(LigneCommande ligne) {
        // Vérifier que le produit a assez de stock
        if (!stockDisponible(ligne.getProduit().getId(), ligne.getQuantite())) {
            return false;
        }
        
        String sql = "INSERT INTO LigneCommande (id_commande, id_produit, quantite, prixUnitaire) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, ligne.getIdCommande());
            pst.setInt(2, ligne.getProduit().getId());
            pst.setInt(3, ligne.getQuantite());
            pst.setDouble(4, ligne.getPrixunitaire());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean stockDisponible(int idProduit, int quantite) {
        String sql = "SELECT stockActuel FROM Produit WHERE id=?";
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
    
    public boolean modifierQuantite(int idLigne, int nouvelleQuantite) {
        String sql = "UPDATE LigneCommande SET quantite=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, nouvelleQuantite);
            pst.setInt(2, idLigne);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupérer toutes les lignes d'une commande spécifique
    public List<LigneCommande> listerParCommande(int idCommande) {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT lc.*, p.nom, p.prixVente FROM LigneCommande lc " +
                     "INNER JOIN Produit p ON lc.id_produit = p.id " +
                     "WHERE lc.id_commande = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, idCommande);
            ResultSet rs = pst.executeQuery();
            
            ProduitDAO produitDAO = new ProduitDAO();
            while (rs.next()) {
                Produit p = produitDAO.trouverParId(rs.getInt("id_produit"));
                LigneCommande lc = new LigneCommande(
                    rs.getInt("id"),
                    idCommande,
                    p,
                    rs.getInt("quantite"),
                    rs.getDouble("prixUnitaire")
                );
                lignes.add(lc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lignes;
    }

    // Supprimer une ligne
    public boolean supprimerLigne(int idLigne) {
        String sql = "DELETE FROM LigneCommande WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idLigne);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Pour les statistiques
    public List<Object[]> getTopProduits(Date debut, Date fin) {
        List<Object[]> topProduits = new ArrayList<>();
        String sql = "SELECT p.nom, SUM(lc.quantite) as quantite_vendue " +
                     "FROM LigneCommande lc " +
                     "INNER JOIN Produit p ON lc.id_produit = p.id " +
                     "INNER JOIN Commande c ON lc.id_commande = c.id " +
                     "WHERE c.etat='VALIDEE' AND DATE(c.dateCommande) BETWEEN ? AND ? " +
                     "GROUP BY p.id, p.nom " +
                     "ORDER BY quantite_vendue DESC " +
                     "LIMIT 10";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setDate(1, debut);
            pst.setDate(2, fin);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getString("nom");
                row[1] = rs.getInt("quantite_vendue");
                topProduits.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topProduits;
    }
}
