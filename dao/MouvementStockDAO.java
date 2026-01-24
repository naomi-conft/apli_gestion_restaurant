package com.restaurant.dao;

import com.restaurant.controllers.DBConnection;
import java.sql.*;

public class MouvementStockDAO {
    // Enregistrer une entrée ou sortie [cite: 47, 48]
    public boolean enregistrerMouvement(int idProduit, String type, int quantite, String motif) {
        String sqlMvt = "INSERT INTO MouvementStock (id_produit, type_mouvement, quantite, motif) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = (type.equals("ENTREE")) 
            ? "UPDATE Produit SET stockActuel = stockActuel + ? WHERE id = ?"
            : "UPDATE Produit SET stockActuel = stockActuel - ? WHERE id = ?";

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

            // 2. Mettre à jour le stock du produit [cite: 24]
            PreparedStatement pstUpd = conn.prepareStatement(sqlUpdateStock);
            pstUpd.setInt(1, quantite);
            pstUpd.setInt(2, idProduit);
            pstUpd.executeUpdate();

            conn.commit(); // Valider les deux opérations
            return true;
        } catch (SQLException e) {
            try { if(conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        }
    }
}