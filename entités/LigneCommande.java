package com.restaurant.entités;

/**
 * Entité LigneCommande - Représente une ligne dans une commande
 * @author DELL
 */
public class LigneCommande {
    
    private int id;
    private int idCommande;
    private Produit produit;
    private int quantite;
    private double prixUnitaire;
    private double montantLigne;
    
    // Constructeurs
    public LigneCommande() {
    }
    
    public LigneCommande(int id, int idCommande, Produit produit, int quantite, double prixUnitaire) {
        this.id = id;
        this.idCommande = idCommande;
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.montantLigne = quantite * prixUnitaire;
    }
    
    public LigneCommande(int idCommande, Produit produit, int quantite, double prixUnitaire) {
        this.idCommande = idCommande;
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.montantLigne = quantite * prixUnitaire;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdCommande() {
        return idCommande;
    }
    
    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }
    
    public Produit getProduit() {
        return produit;
    }
    
    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
        this.montantLigne = this.quantite * this.prixUnitaire;
    }
    
    public double getPrixunitaire() {
        return prixUnitaire;
    }
    
    public void setPrixunitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        this.montantLigne = this.quantite * this.prixUnitaire;
    }
    
    public double getMontantligne() {
        return montantLigne;
    }
    
    public void setMontantligne(double montantLigne) {
        this.montantLigne = montantLigne;
    }
    
    // Méthode utilitaire pour recalculer le montant
    public void calculerMontant() {
        this.montantLigne = this.quantite * this.prixUnitaire;
    }
    
    // toString
    @Override
    public String toString() {
        return produit.getNom() + " x" + quantite + " = " + montantLigne + " FCFA";
    }
    
    // equals et hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LigneCommande ligne = (LigneCommande) obj;
        return id == ligne.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}