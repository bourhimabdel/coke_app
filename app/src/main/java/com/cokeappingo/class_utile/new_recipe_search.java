package com.cokeappingo.class_utile;

import java.util.ArrayList;

public class new_recipe_search {

    String lien_puc;
    String title;
    String date_publication;
    String cooking_time;
    String person;
    String id_push;

    ArrayList<Integer> puck;

    public ArrayList<Integer> getPuck() {
        return puck;
    }

    public void setPuck(ArrayList<Integer> puck) {
        this.puck = puck;
    }

    public new_recipe_search(String lien_puc, String title, String date_publication, String cooking_time, String person, String id_push) {
        this.lien_puc = lien_puc;
        this.title = title;
        this.date_publication = date_publication;
        this.cooking_time = cooking_time;
        this.person = person;
        this.id_push = id_push;
    }

    public new_recipe_search(){};

    public String getLien_puc() {
        return lien_puc;
    }

    public void setLien_puc(String lien_puc) {
        this.lien_puc = lien_puc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_publication() {
        return date_publication;
    }

    public void setDate_publication(String date_publication) {
        this.date_publication = date_publication;
    }

    public String getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getId_push() {
        return id_push;
    }

    public void setId_push(String id_push) {
        this.id_push = id_push;
    }
}
