package com.restaurant.entités;

/**
 * Entité Utilisateurs - Représente un utilisateur du système
 * @author DELL
 */
public class Utilisateurs {
    
    private int id;
    private String login;
    private String motDePasse;
    
    // Constructeurs
    public Utilisateurs() {
    }
    
    public Utilisateurs(int id, String login, String motDePasse) {
        this.id = id;
        this.login = login;
        this.motDePasse = motDePasse;
    }
    
    public Utilisateurs(String login, String motDePasse) {
        this.login = login;
        this.motDePasse = motDePasse;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getMotDePasse() {
        return motDePasse;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    // toString
    @Override
    public String toString() {
        return "Utilisateur{" + "login=" + login + '}';
    }
    
    // equals et hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Utilisateurs user = (Utilisateurs) obj;
        return id == user.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
