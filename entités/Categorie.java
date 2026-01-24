package com.restaurant.entités;

public class Categorie {
    private int id;
    private String libelle;

    // Constructeurs
    public Categorie() {}
    public Categorie(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    // Getters et Setters (indispensables pour l'encapsulation) [cite: 16]
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    @Override
    public String toString() { return libelle; } // Utile pour les JComboBox plus tard
}