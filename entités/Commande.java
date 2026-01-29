package com.restaurant.entités;
import java.util.Date;

public class Commande {
    private int id;
    private Date dateCommande;
    
    private double total;

    public Commande(int id, Date dateCommande, double total) {
        this.id = id;
        this.dateCommande = dateCommande;
        
        this.total = 0.0;
    }
    // Ajoutez les Getters/Setters
    public int getId() {return id;}
    public void setId(int id) { this.id = id;}
    public Date getdateCommande() { return dateCommande;}
    public void setdateCommande(Date dateCommande) { this.dateCommande = dateCommande ;}
    
    public double getTotal() { return total;}
    public void setTotal( double total) { this.total = total;}

    public Object getDateCommande() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}