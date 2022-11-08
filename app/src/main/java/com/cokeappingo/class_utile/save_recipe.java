package com.cokeappingo.class_utile;

public class save_recipe {

    String id_push;
    String Title;
    byte[] image;

    public String getId_push() {
        return id_push;
    }

    public save_recipe() {
    }

    public save_recipe(String id_push, String title, byte[] image) {
        this.id_push = id_push;
        Title = title;
        this.image = image;
    }

    public void setId_push(String id_push) {
        this.id_push = id_push;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
