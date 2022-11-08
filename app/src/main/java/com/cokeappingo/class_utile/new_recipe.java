package com.cokeappingo.class_utile;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class new_recipe implements Parcelable {

    ArrayList<String> ingredient;
    ArrayList<String> how_to_prepare;
    String status_recipe;
    String ID;
    String lien_puc;
    String title;
    String description;
    String date;
    String cooking_time;
    String person;
    String auteur;
    String auteur_ID;
    String categories;
    String cuisine;
    String date_publication;
    String cause_refuse;
    String id_push;

    protected new_recipe(Parcel in) {
        ingredient = in.createStringArrayList();
        how_to_prepare = in.createStringArrayList();
        status_recipe = in.readString();
        ID = in.readString();
        lien_puc = in.readString();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        cooking_time = in.readString();
        person = in.readString();
        auteur = in.readString();
        auteur_ID = in.readString();
        categories = in.readString();
        cuisine = in.readString();
        date_publication = in.readString();
        cause_refuse = in.readString();
        id_push = in.readString();
        image = in.createByteArray();
    }

    public static final Creator<new_recipe> CREATOR = new Creator<new_recipe>() {
        @Override
        public new_recipe createFromParcel(Parcel in) {
            return new new_recipe(in);
        }

        @Override
        public new_recipe[] newArray(int size) {
            return new new_recipe[size];
        }
    };

    public String getId_push() {
        return id_push;
    }

    public void setId_push(String id_push) {
        this.id_push = id_push;
    }

    byte[] image;



    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }




    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getAuteur_ID() {
        return auteur_ID;
    }

    public void setAuteur_ID(String auteur_ID) {
        this.auteur_ID = auteur_ID;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getDate_publication() {
        return date_publication;
    }

    public void setDate_publication(String date_publication) {
        this.date_publication = date_publication;
    }

    public String getCause_refuse() {
        return cause_refuse;
    }

    public void setCause_refuse(String cause_refuse) {
        this.cause_refuse = cause_refuse;
    }

    public String getStatus_recipe() {
        return status_recipe;
    }

    public void setStatus_recipe(String status_recipe) {
        this.status_recipe = status_recipe;
    }

    public new_recipe(){}


    public  ArrayList<String>  getIngredient() {
        return ingredient;
    }

    public void setIngredient( ArrayList<String>  ingredient) {
        this.ingredient = ingredient;
    }

    public  ArrayList<String>  getHow_to_prepare() {
        return how_to_prepare;
    }

    public void setHow_to_prepare( ArrayList<String>  how_to_prepare) {
        this.how_to_prepare = how_to_prepare;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(ingredient);
        dest.writeStringList(how_to_prepare);
        dest.writeString(status_recipe);
        dest.writeString(ID);
        dest.writeString(lien_puc);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(cooking_time);
        dest.writeString(person);
        dest.writeString(auteur);
        dest.writeString(auteur_ID);
        dest.writeString(categories);
        dest.writeString(cuisine);
        dest.writeString(date_publication);
        dest.writeString(cause_refuse);
        dest.writeString(id_push);
        dest.writeByteArray(image);
    }
}
