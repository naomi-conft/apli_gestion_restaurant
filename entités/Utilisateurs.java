package com.restaurant.entités;

public class Utilisateurs {
    private int id;
    private String login;
    private String motDePasse;

    public Utilisateurs(int id, String login, String motDePasse) {
        this.id = id;
        this.login = login;
        this.motDePasse = motDePasse;
    }
    // Getters/Setters
    public int getId() { return id;}
    public void setId(int id) { this.id = id;}
    public String getLogin() { return login;}
    public void setLogin(String login) { this.login = login;}
    public String getmotDePasse() { return motDePasse;}
    public void setmotDePasse(String motDePasse) { this.motDePasse = motDePasse;}
}