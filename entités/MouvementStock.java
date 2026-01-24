package com.restaurant.entités;

import java.util.Date;

public class MouvementStock {
    private int id;
    private Produit produit;
    private String typeMouvement;
    private int quantite;
    private Date dateMouvement;
    private String motif;
    
    // constructeurs
    public MouvementStock(int id, Produit produit, String typeMouvement, int quantite, Date dateMouvement, String motif){
        this.id = id;
        this.produit = produit;
        this.typeMouvement = typeMouvement;
        this.quantite = quantite;
        this.motif = motif;
        this.dateMouvement = dateMouvement;
    }
    
    //Getters et Setters
    public int getId() { return id;}
    public void setId(int id) { this.id = id;}
    public Produit getProduit() { return produit;}
    public void setProduit(Produit produit) { this.produit = produit;}
    public String getTypemouvement() { return typeMouvement;}
    public void setTypemouvement(String typeMouvement) {this.typeMouvement = typeMouvement;}
    public int getQuantite() { return quantite;}
    public void setQuantite(int quantite) {
        if (quantite > 0){
        this.quantite = quantite;
        }
    }
    public Date getDatemouvement() {return dateMouvement;}
    public void setDatemouvement(Date dateMouvement) {this.dateMouvement = dateMouvement;}
    public String getMotif() { return motif;}
    public void setMotif(String motif) { this.motif = motif;}
    
}
