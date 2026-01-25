package com.restaurant.dao;

import com.restaurant.entités.Categorie;
import com.restaurant.controllers.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {
    
    public boolean ajouter(Categorie cat) {
        String sql = "INSERT INTO Categorie (libelle) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, cat.getLibelle());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Categorie> listerTous() {
        List<Categorie> liste = new ArrayList<>();
        String sql = "SELECT * FROM Categorie";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Categorie(rs.getInt("id"), rs.getString("libelle")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
}