package com.restaurant.dao;

import com.restaurant.entités.Categorie;
import com.restaurant.controllers.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {
    
    public boolean ajouter(Categorie cat) {
        // Vérifier l'unicité du libellé
        if (libelleExiste(cat.getLibelle())) {
            return false;
        }
        
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
    
    public boolean modifier(Categorie cat) {
        String sql = "UPDATE Categorie SET libelle=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, cat.getLibelle());
            pst.setInt(2, cat.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean supprimer(int id) {
        // Vérifier s'il y a des produits liés
        if (aDesProduits(id)) {
            return false; // On ne peut pas supprimer une catégorie avec des produits
        }
        
        String sql = "DELETE FROM Categorie WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean libelleExiste(String libelle) {
        String sql = "SELECT COUNT(*) FROM Categorie WHERE libelle=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, libelle);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean aDesProduits(int idCategorie) {
        String sql = "SELECT COUNT(*) FROM Produit WHERE id_categorie=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idCategorie);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
    
    public Categorie trouverParId(int id) {
        String sql = "SELECT * FROM Categorie WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Categorie(rs.getInt("id"), rs.getString("libelle"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
