package com.cokeappingo.liste_view_acceuil;

public class catugorie {
    int id;
    String nom;
    String url;

    public catugorie() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public String getUrl() {
        return url;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public catugorie(String nom, String url) {
        this.nom = nom;
        this.url = url;
    }
}
