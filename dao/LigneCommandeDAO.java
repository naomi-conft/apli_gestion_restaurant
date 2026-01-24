package com.restaurant.dao;

import com.restaurant.entités.LigneCommande;
import com.restaurant.controllers.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO {

    // Récupérer toutes les lignes d'une commande spécifique
    public List<LigneCommande> listerParCommande(int idCommande) {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT lc.*, p.nom FROM LigneCommande lc " +
                     "JOIN Produit p ON lc.id_produit = p.id " +
                     "WHERE lc.id_commande = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, idCommande);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                // Ici, on crée l'objet ligne (en pratique, on chargerait l'objet Produit complet via ProduitDAO)
                // Mais pour l'affichage simple, on peut mapper les colonnes :
                // LigneCommande lc = new LigneCommande(...);
                // lignes.add(lc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lignes;
    }

    // Supprimer une ligne (utile pour la règle 11 : modification de commande)
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
}