package com.cokeappingo.list_setting;

import org.json.JSONException;
import org.json.JSONObject;

public class objet_setting {
    String name;
    int image;
    String[] choise;
    String exact_choise;

    public objet_setting(String name, String[] choise, String exact_choise,int image) {
        this.image=image;
        this.name = name;
        this.choise = choise;
        this.exact_choise = exact_choise;
    }

    public objet_setting() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getChoise() {
        return choise;
    }

    public void setChoise(String[] choise) {
        this.choise = choise;
    }

    public String getExact_choise() {
        return exact_choise;
    }

    public void setExact_choise(String exact_choise) {
        this.exact_choise = exact_choise;
    }

  /*  public String objetjson(objet_setting objet_setting){
        JSONObject jsonObject = new JSONObject();
         try {
           jsonObject.put("name", objet_setting.getName());
            jsonObject.pu
            jsonObject.put("choise", Enrollment Number);
            jsonObject.put("Mobile", Mobile);
            jsonObject.put("Address", Address);
            jsonObject.put("Branch", Branch);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

   */

}
