package com.cokeappingo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.list_activity_search.RecyclerViewAdapter;
import com.cokeappingo.liste_view_acceuil.catugorie;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.shawnlin.numberpicker.NumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class child_smart_seartch extends Fragment {


    NumberPicker picker,picker_cuisine;

    Button btn_smart_search;
    ImageButton btn_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myLayout = inflater.inflate(R.layout.child_fragment_smarch_sarch, container, false);

        picker=myLayout.findViewById(R.id.number_picker);
        picker_cuisine=myLayout.findViewById(R.id.cuisine_picker);
        btn_smart_search=myLayout.findViewById(R.id.btn_search_smart);
        btn_cancel=myLayout.findViewById(R.id.btn_cancel);

        myLayout.findViewById(R.id.res).requestFocus();




        final String[] data =new String[34];
        get_catugorie().toArray(data);
        ArrayList<String> all_categorie=get_catugorie();
        picker.setMinValue(0);
        picker.setMaxValue(data.length-1);
        picker.setDisplayedValues(data);
        picker.setWrapSelectorWheel(false);

        final String[] data2 =new String[17];
        ArrayList<String> arrayList_all_cuisine=get_cuisine();
        arrayList_all_cuisine.add("كل المطابخ");
        arrayList_all_cuisine.toArray(data2);
        picker_cuisine.setMinValue(0);
        picker_cuisine.setMaxValue(data2.length-1);
        picker_cuisine.setDisplayedValues(data2);
        picker_cuisine.setWrapSelectorWheel(false);


        if(!getArguments().getString("cu").equals("")){
            picker_cuisine.setValue(arrayList_all_cuisine.indexOf(getArguments().getString("cu")));
            picker.setValue(all_categorie.indexOf(getArguments().getString("ca")));
        }


        btn_smart_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Categories", data[picker.getValue()]);
                intent.putExtra("Cuisine",data2[picker_cuisine.getValue()]);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag("holm")).commit();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag("holm")).commit();
            }
        });
        return myLayout;
    }

  //  @Override
  //  public void onDetach() {
  //      super.onDetach();
  //      Intent intent = new Intent();
  //      getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
  //      getFragmentManager().popBackStack();
  //  }

    public ArrayList<String> get_catugorie(){
        ArrayList<String> models = new ArrayList<>();
        try {
        InputStream is = getResources().openRawResource(R.raw.spinner_add_recipe);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String json = new String(buffer, "UTF-8");
        JSONObject jsonObject = new JSONObject(json).getJSONObject("spinner_1");
        for(int n = 1; n <35; n++)
        {
            models.add(jsonObject.getString("objet_"+n));
        }
        return models;
    } catch (JSONException | IOException e) {
            e.printStackTrace();
            return models;
        }

    }

    public ArrayList<String> get_cuisine(){
        ArrayList<String> modeels = new ArrayList<>();
        try {
            InputStream is = getResources().openRawResource(R.raw.spinner_add_recipe);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json).getJSONObject("spinner_2");
            for(int n = 1; n <17; n++)
            {
                modeels.add(jsonObject.getString("objet_"+n));
            }
            return modeels;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return modeels;
        }

    }

}