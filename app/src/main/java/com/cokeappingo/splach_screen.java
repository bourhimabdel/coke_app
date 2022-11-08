package com.cokeappingo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.class_reglage.adapter_setting;

import com.cokeappingo.notification_morning.Alarm;
import com.cokeappingo.notification_morning.YourService;
import com.cokeappingo.sql_lite_manager.save_data_in_first_open;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class splach_screen extends Activity {
   // SharedPreferences prefs = null;
   Alarm alarm = new Alarm();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Splash_screen);
        super.onCreate(savedInstanceState);


        final String PREFS_NAME = "Coke_App_PrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);




        if (settings.getBoolean("Coke_App_first_time", true)) {
            new save_data_in_first_open().save_all_catugorie(this);
            new save_data_in_first_open().save_all_setting(this);
            new adapter_setting().set_setting_theme(this);
            new sql_manager(this).insert_data_into_pointer();
            FirebaseAuth.getInstance().signOut();

            startService(new Intent(this, YourService.class));

            settings.edit().putBoolean("Coke_App_first_time", false).apply();




            }else {
            new adapter_setting().set_setting_theme(this);
        }


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null) {
            Uri uri = getIntent().getData();
            if (uri != null) {
                List<String> parameters = uri.getPathSegments();

                String param = parameters.get(0);

                Intent intent = new Intent(getBaseContext(), open_form_Lien.class);
                intent.putExtra("recipe",param);
                startActivity(intent);
            }
            else
                startActivity(new Intent(splach_screen.this,praincipal_activity.class));
        }else {
            startActivity(new Intent(splach_screen.this,signin.class));
        }
    }





}
