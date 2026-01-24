package com.restaurant.entités;

public class LigneCommande {
    private int id;
    private Produit produit;
    private int quantite;
    private double prixUnitaire;
    private double montantLigne; // quantite * prixUnitaire

    public LigneCommande(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = produit.getPrixVente();
        this.montantLigne = quantite * this.prixUnitaire;
    }
    // Ajoutez les Getters/Setters...
    public int getId() {return id ;}
    public void setId(int id) {this.id = id ;}
    public Produit getProduit() {return produit ;}
    public void setProduit(Produit produit) {this.produit = produit;}
    public int getQuantite() {return quantite ;}
    public void setQuantite(int quantite) {this.quantite = quantite ;}
    public double getPrixunitaire() {return prixUnitaire ;}
    public void setPrixunitaire(double prixUnitaire) {this.prixUnitaire = prixUnitaire;}
    public double getMontantligne() {return montantLigne;}
    public void setMontantligne(double montantLigne) {
        this.montantLigne = quantite * prixUnitaire;
    }
}