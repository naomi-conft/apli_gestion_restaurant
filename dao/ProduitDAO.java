package com.restaurant.dao;

import com.restaurant.entite.Produit;
import com.restaurant.entite.Categorie;
import com.restaurant.controllers.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO {
    
    public boolean ajouter(Produit p) {
        // Le prix doit être strictement positif
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
    
    public boolean modifier(Produit p) {
        if (p.getPrixVente() <= 0) return false;
        
        String sql = "UPDATE Produit SET nom=?, id_categorie=?, prixVente=?, stockActuel=?, seuilAlerte=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, p.getNom());
            pst.setInt(2, p.getCategorie().getId());
            pst.setDouble(3, p.getPrixVente());
            pst.setInt(4, p.getStockActuel());
            pst.setInt(5, p.getSeuilAlerte());
            pst.setInt(6, p.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean supprimer(int id) {
        String sql = "DELETE FROM Produit WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Produit> listerTous() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, c.libelle FROM Produit p INNER JOIN Categorie c ON p.id_categorie = c.id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Categorie cat = new Categorie(rs.getInt("id_categorie"), rs.getString("libelle"));
                Produit p = new Produit(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    cat,
                    rs.getDouble("prixVente"),
                    rs.getInt("stockActuel"),
                    rs.getInt("seuilAlerte")
                );
                produits.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }
    
    public Produit trouverParId(int id) {
        String sql = "SELECT p.*, c.libelle FROM Produit p INNER JOIN Categorie c ON p.id_categorie = c.id WHERE p.id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Categorie cat = new Categorie(rs.getInt("id_categorie"), rs.getString("libelle"));
                return new Produit(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    cat,
                    rs.getDouble("prixVente"),
                    rs.getInt("stockActuel"),
                    rs.getInt("seuilAlerte")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Méthode pour lister les produits en alerte
    public List<Produit> getProduitsEnAlerte() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, c.libelle FROM Produit p INNER JOIN Categorie c ON p.id_categorie = c.id WHERE p.stockActuel <= p.seuilAlerte";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Categorie cat = new Categorie(rs.getInt("id_categorie"), rs.getString("libelle"));
                Produit p = new Produit(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    cat,
                    rs.getDouble("prixVente"),
                    rs.getInt("stockActuel"),
                    rs.getInt("seuilAlerte")
                );
                produits.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }
    
    public boolean mettreAJourStock(int idProduit, int nouvelleQuantite) {
        String sql = "UPDATE Produit SET stockActuel=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, nouvelleQuantite);
            pst.setInt(2, idProduit);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
