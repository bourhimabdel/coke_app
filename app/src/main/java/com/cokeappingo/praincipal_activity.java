package com.cokeappingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.cokeappingo.search_activity;


import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;


public class praincipal_activity extends AppCompatActivity implements View.OnClickListener {



    private FirebaseAnalytics mFirebaseAnalytics;
    TextView text_ad;
    private InterstitialAd mInterstitialAd;

    public static FragmentManager fragmentManager;
    ImageButton button_setting,button_home,button_save,button_search,button_account;


    public List<Object> all_recipe_search;

    private static final String KEY_TEXT_VALUE = "textValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praincipal_activity);
        new adapter_setting().set_setting_theme(this);
        new adapter_setting().set_setting_text(this);

        // intent in our uri.


        // checking if the uri is null or not.

        load_Interstitiel_ad();


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        button_home = findViewById(R.id.button_1);
        button_setting = findViewById(R.id.button_2);
        button_save = findViewById(R.id.button_4);
        button_search = findViewById(R.id.button_5);
        button_account = findViewById(R.id.button_6);
        text_ad=findViewById(R.id.text_ad);


        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null && savedInstanceState.getCharSequence(KEY_TEXT_VALUE) !=null)
            {
            if(savedInstanceState.getCharSequence(KEY_TEXT_VALUE).equals("hollla") ||
                    savedInstanceState.getCharSequence(KEY_TEXT_VALUE).equals("edit")){
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new setting(),setting.class.getName()).commit();
            Me_is_the_big_one(button_setting);
            }
          else if(savedInstanceState.getCharSequence(KEY_TEXT_VALUE).equals("bollla")){
              fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new account_activity(),account_activity.class.getName()).commit();
              Me_is_the_big_one(button_account);
          }


        }
        else
            {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer_accueil,new MainActivity(),MainActivity.class.getName()).commit();
            //fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new show_this_recipe(),MainActivity.class.getName()).commit();
        }




        button_home.setOnClickListener(this);
        button_setting.setOnClickListener(this);
        button_save.setOnClickListener(this);
        button_search.setOnClickListener(this);
        button_account.setOnClickListener(this);


        ////////////////////////////////////// Listner to the Keybord //////////////////////////////////////////////////////////////////
        final View activityRootView = findViewById(R.id.principal_layout);                                                          //
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {      //
            @Override                                                                                                         //
            public void onGlobalLayout() {                                                                                    //
                Rect r = new Rect();                                                                                          //
                //
                activityRootView.getWindowVisibleDisplayFrame(r);                                                             //
                //
                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);                             //
                if (heightDiff > 100) {                                                                                       //
                    hide_navigate_bottom();                                                                                   //
                   // searchView.setFocusable(true);                                                                          //
                }else{                                                                                                        //
                    ///when hiden                                                                                             //
                }                                                                                                             //
            }                                                                                                                 //
        });                                                                                                                   //
         ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

       hide_navigate_bottom();


    }

    private void load_Interstitiel_ad() {

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                AdRequest adRequest = new AdRequest.Builder().build();

                Fragment acceuil = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer_accueil);
                Fragment search =      getSupportFragmentManager().findFragmentById(R.id.fragmentContainer_search);

                InterstitialAd.load(praincipal_activity.this,"ca-app-pub-8591526182350923/6714037183", adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                        long maxCounter = 4000;
                        long diff = 1000;

                        new CountDownTimer(maxCounter , diff ) {

                            public void onTick(long millisUntilFinished) {
                                text_ad.setVisibility(View.VISIBLE);
                                text_ad.setText("" +millisUntilFinished / 1000 +"");
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                if (mInterstitialAd != null && acceuil.isVisible()) {
                                    mInterstitialAd.show(praincipal_activity.this);
                                } else if(search != null) {
                                    if (search.isVisible())
                                    mInterstitialAd.show(praincipal_activity.this);
                                }else {
                                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                }
                                load_Interstitiel_ad();
                                text_ad.setVisibility(View.INVISIBLE);
                            }

                        }.start();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                        load_Interstitiel_ad();
                    }



                });
            }
        }, 65000);

    }


    public void hide_navigate_bottom(){

        Cursor result=new sql_manager(this).getAllData_setting();
        result.moveToPosition(4);
        if(result.getString(2).equals("مفعل"))
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        }else {
            ///Hide_bottom_bar//////////////////////
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
        ///////////////////////////////////////
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hide_navigate_bottom();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        new adapter_setting().set_setting_text(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Reload current fragment
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (f instanceof search_activity){

        }
        else if(f instanceof MainActivity)
        {

        }
        else if (f instanceof account_activity)
        {

        }
        else if(f instanceof setting)
        {

        }
        else if(f instanceof Add_new_recipe)
        {
            Add_new_recipe add_new_recipe=(Add_new_recipe) f;

            int number_return=add_new_recipe.onRestart();

            if (number_return==0){
                this.recreate();
            }
        }
        else if(f instanceof edit_info_user)
        {
            edit_info_user add_new_recipe=(edit_info_user) f;

            int number_return=add_new_recipe.onRestart();

            if (number_return==0){
                this.recreate();
            }
        }
    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (f instanceof search_activity){

        }
        else if(f instanceof MainActivity)
        {

        }
        else if (f instanceof account_activity)
        {

        }
        else if(f instanceof setting)
        {

        }
        else if(f instanceof Add_new_recipe)
        {
            Add_new_recipe add_new_recipe=(Add_new_recipe) f;
            int number_return=add_new_recipe.onBackPressed();
        }
        else if(f instanceof edit_info_user)
        {
            edit_info_user add_new_recipe=(edit_info_user) f;
            int number_return=add_new_recipe.onBackPressed();
        }
    }


    @Override
    public void onClick(View v) {

        int place;
        if (m)
            place=In_activity_add_new_recipe(v);
        else
            place=0;

        if (place == 0){
            switch (v.getId()) {
                case R.id.button_1:
                    show_or_hide("a");
                    Me_is_the_big_one(button_home);

                    Fragment fl = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer_accueil);
                    FragmentManager fml = getSupportFragmentManager();

                    if (fl==null) {
                        Fragment fragment = new MainActivity();

                        FragmentManager fm3 = getSupportFragmentManager();
                        FragmentTransaction transaction3 = fm3.beginTransaction();
                        transaction3.replace(R.id.fragmentContainer_accueil, fragment, MainActivity.class.getName());
                        transaction3.commit();
                    }else {
                        fml.beginTransaction()
                                .show(fl)
                                .commit();
                    }
                    break;
                case R.id.button_2:
                    Me_is_the_big_one(button_setting);
                    show_or_hide("e");

                    Fragment fragment4 = new setting();

                    FragmentManager fm4 = getSupportFragmentManager();
                    FragmentTransaction transaction4 = fm4.beginTransaction();
                    transaction4.replace(R.id.fragmentContainer, fragment4,setting.class.getName());
                    transaction4.commit();

                    break;
                case R.id.button_4:
                    Me_is_the_big_one(button_save);
                    show_or_hide("c");

                    Fragment fragment0 = new Save_activity();

                    FragmentManager fm0 = getSupportFragmentManager();
                    FragmentTransaction transaction0 = fm0.beginTransaction();
                    transaction0.replace(R.id.fragmentContainer, fragment0,Save_activity.class.getName());
                    transaction0.commit();
                    break;
                case R.id.button_5:
                    Me_is_the_big_one(button_search);
                    show_or_hide("b");
                    Fragment ff = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer_search);
                    FragmentManager fmb = getSupportFragmentManager();

                    if (ff==null) {
                        Fragment fragment3 = new search_activity();

                        FragmentManager fm3 = getSupportFragmentManager();
                        FragmentTransaction transaction3 = fm3.beginTransaction();
                        transaction3.replace(R.id.fragmentContainer_search, fragment3, search_activity.class.getName());
                        transaction3.commit();
                    }else {
                        fmb.beginTransaction()
                                .show(ff)
                                .commit();
                    }
                    break;
                case R.id.button_6:
                    Me_is_the_big_one(button_account);
                    Fragment fragment6 = new account_activity();
                    show_or_hide("d");
                    FragmentManager fm6 = getSupportFragmentManager();
                    FragmentTransaction transaction6 = fm6.beginTransaction();
                    transaction6.replace(R.id.fragmentContainer, fragment6,account_activity.class.getName());
                    transaction6.commit();
                    break;
            }
        }


    }

    public void Me_is_the_big_one(ImageButton Me){
        if (Me.equals(button_home)){

           keep_this_button_big(button_home,R.id.button_1,R.id.place_top_bar,ConstraintSet.START
           ,R.id.view_2,ConstraintSet.END,R.id.place_top_bar,ConstraintSet.TOP,R.id.place_top_bar,ConstraintSet.BOTTOM);

           int[] id_btn={R.id.button_2,R.id.button_4,R.id.button_5,R.id.button_6};
           boucler_to_small_button(id_btn);

        }else if(Me.equals(button_setting)){

            keep_this_button_big(button_setting,R.id.button_2,R.id.view_5,ConstraintSet.END
                    ,R.id.place_top_bar,ConstraintSet.END,R.id.place_top_bar,ConstraintSet.TOP,R.id.place_top_bar,ConstraintSet.BOTTOM);
            int[] id_btn={R.id.button_1,R.id.button_4,R.id.button_5,R.id.button_6};
            boucler_to_small_button(id_btn);

        }else if(Me.equals(button_save)){

            keep_this_button_big(button_save,R.id.button_4,R.id.view_3,ConstraintSet.END
                    ,R.id.view_4,ConstraintSet.START,R.id.place_top_bar,ConstraintSet.TOP,R.id.place_top_bar,ConstraintSet.BOTTOM);
            int[] id_btn={R.id.button_1,R.id.button_2,R.id.button_5,R.id.button_6};
            boucler_to_small_button(id_btn);

        }else if(Me.equals(button_search)){

            keep_this_button_big(button_search,R.id.button_5,R.id.view_2,ConstraintSet.END
                    ,R.id.view_3,ConstraintSet.START,R.id.place_top_bar,ConstraintSet.TOP,R.id.place_top_bar,ConstraintSet.BOTTOM);
            int[] id_btn={R.id.button_1,R.id.button_2,R.id.button_4,R.id.button_6};
            boucler_to_small_button(id_btn);

        }else if(Me.equals(button_account)){

            keep_this_button_big(button_account,R.id.button_6,R.id.view_4,ConstraintSet.END
                    ,R.id.view_5,ConstraintSet.START,R.id.place_top_bar,ConstraintSet.TOP,R.id.place_top_bar,ConstraintSet.BOTTOM);

            int[] id_btn={R.id.button_1,R.id.button_2,R.id.button_4,R.id.button_5};
            boucler_to_small_button(id_btn);

        }
    }

    public void keep_this_button_big(ImageButton Me,int id,int start,int startPoint,int end,int endPoint,int top,int topPoint,int bottom,int bottomtPoint){
            Me.setLayoutParams(new ConstraintLayout.LayoutParams((int) getResources().getDimension(R.dimen.dimens_130px)
                    , (int)getResources().getDimension(R.dimen.dimens_130px)));

            ConstraintLayout constraintLayout = findViewById(R.id.place_top_bar);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);


            constraintSet.connect(id,ConstraintSet.END,end,endPoint,0);
            constraintSet.connect(id,ConstraintSet.START,start,startPoint,0);
            constraintSet.connect(id,ConstraintSet.TOP,top,topPoint,0);
            constraintSet.connect(id,ConstraintSet.BOTTOM,bottom,bottomtPoint,(int)getResources().getDimension(R.dimen.dimens_11px));
            constraintSet.applyTo(constraintLayout);
    }

    public void keep_this_button_small(ImageButton Me,int id,int start,int startPoint,int end,int endPoint,int top,int topPoint,int bottom,int bottomtPoint){
        Me.setLayoutParams(new ConstraintLayout.LayoutParams((int) getResources().getDimension(R.dimen.dimens_85px)
                , (int)getResources().getDimension(R.dimen.dimens_85px)));

        ConstraintLayout constraintLayout = findViewById(R.id.place_top_bar);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);


        constraintSet.connect(id,ConstraintSet.END,end,endPoint,0);
        constraintSet.connect(id,ConstraintSet.START,start,startPoint,0);
        constraintSet.connect(id,ConstraintSet.TOP,top,topPoint,0);
        constraintSet.connect(id,ConstraintSet.BOTTOM,bottom,bottomtPoint,0);
        constraintSet.applyTo(constraintLayout);
    }

    public void boucler_to_small_button(int[] all_button){
        for (int id :all_button){
            switch (id){
                case R.id.button_1:
                    keep_this_button_small(button_home,R.id.button_1,R.id.place_top_bar,ConstraintSet.START
                            ,R.id.view_2,ConstraintSet.END,R.id.place_top_bar,ConstraintSet.TOP,R.id.place_top_bar,ConstraintSet.BOTTOM);
                    break;
                case R.id.button_2:
                    keep_this_button_small(button_setting,R.id.button_2,R.id.view_5,ConstraintSet.END
                            ,R.id.place_top_bar,ConstraintSet.END,R.id.place_top_bar,ConstraintSet.TOP,R.id.place_top_bar,ConstraintSet.BOTTOM);
                    break;
                case R.id.button_4:
                    keep_this_button_small(button_save,R.id.button_4,R.id.view_3,ConstraintSet.END
                            ,R.id.view_4,ConstraintSet.START,R.id.place_top_bar,ConstraintSet.TOP,R.id.place_top_bar,ConstraintSet.BOTTOM);
                    break;
                case R.id.button_5:
                    keep_this_button_small(button_search,R.id.button_5,R.id.view_2,ConstraintSet.END
                            ,R.id.view_3,ConstraintSet.START,R.id.place_top_bar,ConstraintSet.TOP,R.id.place_top_bar,ConstraintSet.BOTTOM);
                    break;
                case R.id.button_6:
                    keep_this_button_small(button_account,R.id.button_6,R.id.view_4,ConstraintSet.END
                            ,R.id.view_5,ConstraintSet.START,R.id.place_top_bar,ConstraintSet.TOP,R.id.place_top_bar,ConstraintSet.BOTTOM);
                    break;
            }
        }
    }


    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        Fragment frg = null;
        frg = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(frg instanceof setting)
            outState.putCharSequence(KEY_TEXT_VALUE, "hollla");
        else if(frg instanceof Add_new_recipe)
            outState.putCharSequence(KEY_TEXT_VALUE, "bollla");
        else if(frg instanceof edit_info_user)
            outState.putCharSequence(KEY_TEXT_VALUE, "edit");
    }

    public int In_activity_add_new_recipe(View v){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        Fragment g = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer_accueil);

        if (f instanceof search_activity){
            return 0;
        }
        else if(g instanceof MainActivity)
        {
            return 0;
        }
        else if (f instanceof account_activity)
        {
            return 0;
        }
        else if(f instanceof setting)
        {
            return 0;
        }else if(f instanceof Save_activity)
        {
            return 0;
        }
        else if(f instanceof edit_info_user)
        {
            return 0;
        }
        else if(f instanceof Add_new_recipe)
        {
            Add_new_recipe add_new_recipe=(Add_new_recipe) f;
            int ret=add_new_recipe.onBackPressed();

            if (ret==1) {
                m=false;
                switch (v.getId()) {
                    case R.id.button_1:
                        button_home.performClick();
                        break;
                    case R.id.button_2:
                        button_setting.performClick();
                        break;
                    case R.id.button_4:
                        button_save.performClick();
                        break;
                    case R.id.button_5:
                        button_search.performClick();
                        break;
                    case R.id.button_6:
                        button_account.performClick();
                        break;
                }
                m=true;
                return 0;
            }
            else return 1;
        }
        else
            return -1;

    }


    public void show_or_hide(String name){
        Fragment acceuil = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer_accueil);
        Fragment ff =      getSupportFragmentManager().findFragmentById(R.id.fragmentContainer_search);
        Fragment fo = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        FragmentManager fmb = getSupportFragmentManager();

        switch (name){
            case "a":
                if(fo!=null)
                fmb.beginTransaction()
                        .hide(fo)
                        .commit();

                if(ff != null)
                fmb.beginTransaction()
                        .hide(ff)
                        .commit();


                    fmb.beginTransaction()
                            .show(acceuil)
                            .commit();
                break;
            case "c": case "d": case "e":
                if(ff!=null)
                    fmb.beginTransaction()
                            .hide(ff)
                            .commit();

                if(acceuil!=null)
                    fmb.beginTransaction()
                            .hide(acceuil)
                            .commit();

                if(fo != null)
                fmb.beginTransaction()
                        .show(fo)
                        .commit();
                break;
            case "b":
                if(fo != null)
                fmb.beginTransaction()
                        .hide(fo)
                        .commit();

                fmb.beginTransaction()
                        .hide(acceuil)
                        .commit();

                if(ff != null)
                fmb.beginTransaction()
                        .show(ff)
                        .commit();

                break;

        }

    }


    boolean m=true;
}