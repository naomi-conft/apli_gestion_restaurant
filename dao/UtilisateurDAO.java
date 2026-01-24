package com.restaurant.dao;

import com.restaurant.entités.Utilisateurs;
import com.restaurant.controllers.DBConnection;
import java.sql.*;

public class UtilisateurDAO {
    // Vérifier les identifiants pour la redirection vers le menu principal [cite: 78]
    public Utilisateurs authentifier(String login, String password) {
        String sql = "SELECT * FROM Utilisateur WHERE login = ? AND motDePasse = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, login);
            pst.setString(2, password); // Note : En production, comparez des hashs [cite: 97]
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Utilisateurs(rs.getInt("id"), rs.getString("login"), rs.getString("motDePasse"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}