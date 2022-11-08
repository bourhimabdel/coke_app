package com.cokeappingo.class_utile;

public class old_recipe extends recipe {

    String ingredient;
    String how_to_prepare;

    public old_recipe(String ID, String lien_puc, String title, String description, String date, String cooking_time, String person, String auteur, String ingredient, String how_to_prepare) {
        super(ID, lien_puc, title, description, date, cooking_time, person, auteur);
        this.ingredient = ingredient;
        this.how_to_prepare = how_to_prepare;
    }

    public old_recipe(){}

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getHow_to_prepare() {
        return how_to_prepare;
    }

    public void setHow_to_prepare(String how_to_prepare) {
        this.how_to_prepare = how_to_prepare;
    }
}
