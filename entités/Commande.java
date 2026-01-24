package com.restaurant.entités;
import java.util.Date;

public class Commande {
    private int id;
    private Date dateCommande;
    private String etat; // EN_COURS, VALIDÉE, ANNULÉE
    private double total;

    public Commande(int id, Date dateCommande, String etat) {
        this.id = id;
        this.dateCommande = dateCommande;
        this.etat = etat;
        this.total = 0.0;
    }
    // Ajoutez les Getters/Setters
    public int getId() {return id;}
    public void setId(int id) { this.id = id;}
    public Date getdateCommande() { return dateCommande;}
    public void setdateCommande(Date dateCommande) { this.dateCommande = dateCommande ;}
    public String getEtat() {return etat;}
    public void setEtat(String etat) { this.etat = etat;}
    public double getTotal() { return total;}
    public void setTotal( double total) { this.total = total;}
}