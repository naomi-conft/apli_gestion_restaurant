package com.restaurant.dao;

import com.restaurant.entités.Commande;
import com.restaurant.entités.LigneCommande;
import com.restaurant.controllers.DBConnection;
import java.sql.*;
import java.util.List;

public class CommandeDAO {
    public boolean validerCommande(Commande cmd, List<LigneCommande> lignes) {
        if (lignes.isEmpty()) return false; // Règle : au moins une ligne 

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Insérer l'en-tête de la commande 
            String sqlCmd = "INSERT INTO Commande (total, etat) VALUES (?, 'VALIDEE')";
            PreparedStatement pstCmd = conn.prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS);
            pstCmd.setDouble(1, cmd.getTotal());
            pstCmd.executeUpdate();

            ResultSet rs = pstCmd.getGeneratedKeys();
            int cmdId = rs.next() ? rs.getInt(1) : 0;

            // 2. Insérer les lignes et déduire le stock [cite: 58, 63]
            String sqlLigne = "INSERT INTO LigneCommande (id_commande, id_produit, quantite, prixUnitaire, montantLigne) VALUES (?, ?, ?, ?, ?)";
            String sqlStock = "UPDATE Produit SET stockActuel = stockActuel - ? WHERE id = ?";

            for (LigneCommande ligne : lignes) {
                // Insertion ligne
                PreparedStatement pstL = conn.prepareStatement(sqlLigne);
                pstL.setInt(1, cmdId);
                pstL.setInt(2, ligne.getProduit().getId());
                pstL.setInt(3, ligne.getQuantite());
                pstL.setDouble(4, ligne.getPrixunitaire());
                pstL.setDouble(5, ligne.getMontantligne());
                pstL.executeUpdate();

                // Mise à jour stock
                PreparedStatement pstS = conn.prepareStatement(sqlStock);
                pstS.setInt(1, ligne.getQuantite());
                pstS.setInt(2, ligne.getProduit().getId());
                pstS.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if(conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        }
    }
}