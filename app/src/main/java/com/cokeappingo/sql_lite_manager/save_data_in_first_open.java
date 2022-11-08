package com.cokeappingo.sql_lite_manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.R;
import com.cokeappingo.list_setting.objet_setting;
import com.cokeappingo.liste_view_acceuil.catugorie;
import com.cokeappingo.relation_between_json_java.manage_json_setting;
import com.cokeappingo.setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class save_data_in_first_open {

    public void save_all_setting(Context context) {
        sql_manager db = new sql_manager(context);
        List<objet_setting> all_setting = new manage_json_setting().get_setting(context);
        int i = 1;
        for (objet_setting c : all_setting) {
            db.insert_data_into_Setting(c.getName(), c.getExact_choise());
            String[] les_chois = c.getChoise();
            for (String s : les_chois){
                db.insert_data_into_setting_choix(String.valueOf(i), s);
            }
            i++;
        }
    }


    public void save_all_catugorie(Context context) {
        final List<catugorie> models = new ArrayList<>();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.accuil_information);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();
            String json = new String(buffer, "UTF-8");


            JSONObject jsonObject = new JSONObject(json);

            JSONObject setting = jsonObject.getJSONObject("info");

            for (int i = 1; i < 35; i++) {
                String ob_name = "objet_" + i;

                final String name = setting.getJSONObject(ob_name).getString("nom");
                String photo = setting.getJSONObject(ob_name).getString("photo");
                models.add(new catugorie(name,photo));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

       sql_manager db = new sql_manager(context);

        for (catugorie c : models) {
            db.insert_data_into_accuil_information(c.getNom(), c.getUrl());
        }
    }

}
