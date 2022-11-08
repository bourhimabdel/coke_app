package com.cokeappingo.class_utile;

import android.database.Cursor;

import com.cokeappingo.sql_lite_manager.sql_manager;

import java.util.ArrayList;

public class account {
    String account_ID;
    String name;
    String Bio;
    String Image;
    int number_recipe;
    byte[] photo_saved;


    public int getNumber_recipe() {
        return number_recipe;
    }

    public void setNumber_recipe(int number_recipe) {
        this.number_recipe = number_recipe;
    }



    public byte[] getPhoto_saved() {
        return photo_saved;
    }

    public void setPhoto_saved(byte[] photo_saved) {
        this.photo_saved = photo_saved;
    }



    public account(){}

    public account(String name, String bio, String image, ArrayList<recipe> my_recipe) {
        this.name = name;
        Bio = bio;
        Image = image;
    }


    public String getAccount_ID() {
        return account_ID;
    }

    public void setAccount_ID(String account_ID) {
        this.account_ID = account_ID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
