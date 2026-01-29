package com.restaurant.entités;

import java.sql.Timestamp;

/**
 * Entité MouvementStock - Représente un mouvement de stock (entrée/sortie)
 * @author DELL
 */
public class MouvementStock {
    
    private int id;
    private Produit produit;
    private String type; // ENTREE ou SORTIE
    private int quantite;
    private Timestamp dateMouvement;
    private String motif;
    
    // Constructeurs
    public MouvementStock() {
    }
    
    public MouvementStock(int id, Produit produit, String type, int quantite, Timestamp dateMouvement, String motif) {
        this.id = id;
        this.produit = produit;
        this.type = type;
        this.quantite = quantite;
        this.dateMouvement = dateMouvement;
        this.motif = motif;
    }
    
    public MouvementStock(Produit produit, String type, int quantite, String motif) {
        this.produit = produit;
        this.type = type;
        this.quantite = quantite;
        this.motif = motif;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Produit getProduit() {
        return produit;
    }
    
    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    public Timestamp getDateMouvement() {
        return dateMouvement;
    }
    
    public void setDateMouvement(Timestamp dateMouvement) {
        this.dateMouvement = dateMouvement;
    }
    
    public String getMotif() {
        return motif;
    }
    
    public void setMotif(String motif) {
        this.motif = motif;
    }
    
    // Méthodes utilitaires
    public boolean estEntree() {
        return "ENTREE".equals(type);
    }
    
    public boolean estSortie() {
        return "SORTIE".equals(type);
    }
    
    // toString
    @Override
    public String toString() {
        return type + " - " + produit.getNom() + " x" + quantite + " (" + motif + ")";
    }
    
    // equals et hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MouvementStock mvt = (MouvementStock) obj;
        return id == mvt.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}