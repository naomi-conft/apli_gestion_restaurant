package com.restaurant.entités;

public class Produit {
    private int id;
    private String nom;
    private Categorie categorie; // Association avec la classe Categorie
    private double prixVente;
    private int stockActuel;
    private final int seuilAlerte;

    // Constructeur complet
    public Produit(int id, String nom, Categorie categorie, double prixVente, int stockActuel, int seuilAlerte) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.prixVente = prixVente;
        this.stockActuel = stockActuel;
        this.seuilAlerte = seuilAlerte;
    }

    // Getters et Setters (Encapsulation) [cite: 16]
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getPrixVente() { return prixVente; }
    
    // Règle de gestion : prix strictement positif [cite: 40]
    public void setPrixVente(double prixVente) {
        if(prixVente > 0) this.prixVente = prixVente;
    }

    public int getStockActuel() { return stockActuel; }
    public void setStockActuel(int stockActuel) { this.stockActuel = stockActuel; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public int getSeuilAlerte() { return seuilAlerte; }
}