package com.restaurant.entités;

/**
 * Entité Categorie - Représente une catégorie de produits
 * @author DELL
 */
public class Categorie {
    
    private int id;
    private String libelle;
    
    // Constructeurs
    public Categorie() {
    }
    
    public Categorie(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }
    
    public Categorie(String libelle) {
        this.libelle = libelle;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    // toString pour affichage dans les ComboBox
    @Override
    public String toString() {
        return libelle;
    }
    
    // equals et hashCode pour comparaisons
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categorie categorie = (Categorie) obj;
        return id == categorie.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
