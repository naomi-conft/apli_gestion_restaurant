package com.restaurant.entités;

/**
 * Entité Produit - Représente un produit vendu
 * @author DELL
 */
public class Produit {
    
    private int id;
    private String nom;
    private Categorie categorie;
    private double prixVente;
    private int stockActuel;
    private int seuilAlerte;
    
    // Constructeurs
    public Produit() {
    }
    
    public Produit(int id, String nom, Categorie categorie, double prixVente, int stockActuel, int seuilAlerte) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.prixVente = prixVente;
        this.stockActuel = stockActuel;
        this.seuilAlerte = seuilAlerte;
    }
    
    public Produit(String nom, Categorie categorie, double prixVente, int stockActuel, int seuilAlerte) {
        this.nom = nom;
        this.categorie = categorie;
        this.prixVente = prixVente;
        this.stockActuel = stockActuel;
        this.seuilAlerte = seuilAlerte;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public Categorie getCategorie() {
        return categorie;
    }
    
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
    
    public double getPrixVente() {
        return prixVente;
    }
    
    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }
    
    public int getStockActuel() {
        return stockActuel;
    }
    
    public void setStockActuel(int stockActuel) {
        this.stockActuel = stockActuel;
    }
    
    public int getSeuilAlerte() {
        return seuilAlerte;
    }
    
    public void setSeuilAlerte(int seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
    }
    
    // Méthodes utilitaires
    public boolean estEnAlerte() {
        return stockActuel <= seuilAlerte;
    }
    
    public boolean estEnRupture() {
        return stockActuel == 0;
    }
    
    // toString pour affichage dans les ComboBox
    @Override
    public String toString() {
        return nom + " (" + prixVente + " FCFA)";
    }
    
    // equals et hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produit produit = (Produit) obj;
        return id == produit.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
