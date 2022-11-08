package com.cokeappingo.relation_between_json_java;

import android.content.Context;

import com.cokeappingo.R;
import com.cokeappingo.list_setting.objet_setting;
import com.cokeappingo.liste_view_acceuil.catugorie;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class manage_json_setting {

    public List<objet_setting> get_setting(Context context) {

        List<objet_setting> settings = new ArrayList<>();
        try {
           InputStream is = context.getResources().openRawResource(R.raw.setting);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();
            String json = new String(buffer, "UTF-8");


            JSONObject jsonObject = new JSONObject(json);

            JSONObject setting = jsonObject.getJSONObject("setting");

            for (int i = 1; i < 6; i++) {
                String ob_name = "setting_" + i;

                String name = setting.getJSONObject(ob_name).getString("name");
                String[] choise = get_choise(setting.getJSONObject(ob_name).getJSONArray("choise"));
                String exact_choise = setting.getJSONObject(ob_name).getString("exact_choise");

                int image;
                switch (i) {
                    case 1:
                        image = R.drawable.day_or_night;
                        break;
                    case 2:
                        image = R.drawable.notifications;
                        break;
                    case 3:
                        image = R.drawable.text_fonts;
                        break;
                    default:
                        image = 1;
                }


                settings.add(new objet_setting(name,choise,exact_choise,image));
            }

            return settings;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] get_choise(JSONArray jsonArray) {
        String[] intArray = new String[jsonArray.length()];
        for (int i = 0; i < intArray.length; ++i) {
            try {
                intArray[i] = jsonArray.optJSONObject(i).getString("t" + i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return intArray;
    }

    /*
    public void Update_setting(int position, int wish, String new_exact_choise, Context context) {

        List<objet_setting> all_setting = get_setting(context);

        switch (position) {
            case 0:
                switch (wish) {
                    case 0:
                        all_setting.get(0).setExact_choise(new_exact_choise);
                        break;
                    case 1:
                        all_setting.get(1).setExact_choise(new_exact_choise);
                        break;
                    case 2:
                        all_setting.get(2).setExact_choise(new_exact_choise);
                        break;
                }
            case 1:
                switch (wish) {
                    case 0:
                        all_setting.get(0).setExact_choise(new_exact_choise);
                        break;
                    case 1:
                        all_setting.get(1).setExact_choise(new_exact_choise);
                        break;
                }
            case 2:
                switch (wish) {
                    case 0:
                        all_setting.get(0).setExact_choise(new_exact_choise);
                        break;
                    case 1:
                        all_setting.get(1).setExact_choise(new_exact_choise);
                        break;
                    case 2:
                        all_setting.get(2).setExact_choise(new_exact_choise);
                        break;
                    case 3:
                        all_setting.get(3).setExact_choise(new_exact_choise);
                        break;
                }

        }

        try {
            InputStream is = context.getResources().openRawResource(R.raw.setting);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);

            JSONObject setting = jsonObject.getJSONObject("setting");


            int i=1;
            for(objet_setting ob:all_setting){
                setting.putOpt("setting_"+i,new Gson().toJson(ob));
                i++;
            }

            jsonObject.put("setting",setting);

            // Convert JsonObject to String Format
            String settingString = jsonObject.toString();

            // Convert JsonObject to String Format
            String userString = jsonObject.toString();

            // Define the File Path and its Name
            File file = new File(context.getFilesDir(),"setting");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userString);
            bufferedWriter.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

     */
}
