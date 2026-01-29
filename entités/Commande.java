package com.restaurant.entités;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Entité Commande - Représente une commande client
 * @author DELL
 */
public class Commande {
    
    private int id;
    private Timestamp dateCommande;
    private String etat; // EN_COURS, VALIDEE, ANNULEE
    private double total;
    
    // Constructeurs
    public Commande() {
    }
    
    public Commande(int id, Timestamp dateCommande, String etat, double total) {
        this.id = id;
        this.dateCommande = dateCommande;
        this.etat = etat;
        this.total = total;
    }
    
    public Commande(String etat, double total) {
        this.etat = etat;
        this.total = total;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Timestamp getDateCommande() {
        return dateCommande;
    }
    
    public void setDateCommande(Timestamp dateCommande) {
        this.dateCommande = dateCommande;
    }
    
    public String getEtat() {
        return etat;
    }
    
    public void setEtat(String etat) {
        this.etat = etat;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    // Méthodes utilitaires
    public boolean estEnCours() {
        return "EN_COURS".equals(etat);
    }
    
    public boolean estValidee() {
        return "VALIDEE".equals(etat);
    }
    
    public boolean estAnnulee() {
        return "ANNULEE".equals(etat);
    }
    
    // toString
    @Override
    public String toString() {
        return "Commande #" + id + " - " + etat + " - " + total + " FCFA";
    }
    
    // equals et hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Commande commande = (Commande) obj;
        return id == commande.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
