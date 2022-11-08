package com.cokeappingo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.class_reglage.Chrono;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.account;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.joooonho.SelectableRoundedImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_dropdown_item_1line;

public class setting extends Fragment {


    private FirebaseAuth mAuth;


    TextView G_text1,G_text2,G_text3,P_text1,P_text2,P_text3;
    SwitchMaterial switch1,switch2;
    Spinner spinner_text;


    TextView p_text_notif,p_text_bottom_bar;
    SwitchMaterial switch_notif,switch_bottom_bar;
    Button btn_share,btn_rate,btn_deconexion,btn_update;


    LottieAnimationView download_image_profile;
    TextView title,bio;

    SelectableRoundedImageView image_profile;



    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myLayout = inflater.inflate(R.layout.activity_setting, container, false);

        mAuth = FirebaseAuth.getInstance();

       // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);



        G_text1= myLayout.findViewById(R.id.text_day_or_night);
        G_text2= myLayout.findViewById(R.id.text_number);
        G_text3= myLayout.findViewById(R.id.text_text);
        P_text1= myLayout.findViewById(R.id.text_small_day_or_night);
        P_text2= myLayout.findViewById(R.id.text_small_number);
        P_text3= myLayout.findViewById(R.id.text_small_text);
        switch1= myLayout.findViewById(R.id.Switch_day_or_night);
        switch2= myLayout.findViewById(R.id.Switch_number);
        spinner_text=myLayout.findViewById(R.id.spinner_text);


        p_text_notif= myLayout.findViewById(R.id.text_small_notif);
        p_text_bottom_bar= myLayout.findViewById(R.id.text_small_bottom_bar);


        switch_notif= myLayout.findViewById(R.id.spinner_notif);
        switch_bottom_bar= myLayout.findViewById(R.id.spinner_bottom_bar);


        btn_share= myLayout.findViewById(R.id.btn_share);
        btn_rate= myLayout.findViewById(R.id.btn_rate);
        btn_deconexion= myLayout.findViewById(R.id.btn_Log_out);

        btn_update= myLayout.findViewById(R.id.btn_edit_info_userr);
        download_image_profile= myLayout.findViewById(R.id.lottie_load_image);
        title= myLayout.findViewById(R.id.name_profile);
        bio= myLayout.findViewById(R.id.text_bio);
        image_profile= myLayout.findViewById(R.id.image_profil);

        get_Objet_setting();

        sql_manager db=new sql_manager(getContext());

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isChecked) {
                            P_text1.setText(new adapter_setting().adapter_number("وضع ليلي",getContext()));
                        }
                        else{
                            P_text1.setText(new adapter_setting().adapter_number("وضع نهاري",getContext()));
                        }
                        db.Update_data_setting("1",P_text1.getText().toString());
                        new adapter_setting().set_setting_theme(getContext());
                    }
                });
            }
        });


        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isChecked) {
                            db.Update_data_setting("2", "عربي");
                            P_text2.setText(new adapter_setting().adapter_number("عربي",getContext()));
                        }
                        else{
                            db.Update_data_setting("2","عجمي");
                            P_text2.setText(new adapter_setting().adapter_number("عجمي",getContext()));
                        }
                    }
                });
            }
        });

        spinner_text.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0:
                        db.Update_data_setting("3", "شكل 1");
                        P_text3.setText(new adapter_setting().adapter_number("شكل 1",getContext()));
                        break;
                    case 1:
                        db.Update_data_setting("3", "شكل 2");
                        P_text3.setText(new adapter_setting().adapter_number("شكل 2",getContext()));
                        break;
                    case 2:
                        db.Update_data_setting("3", "شكل 3");
                        P_text3.setText(new adapter_setting().adapter_number("شكل 3",getContext()));
                        break;
                    case 3:
                        db.Update_data_setting("3", "شكل 4");
                        P_text3.setText(new adapter_setting().adapter_number("شكل 4",getContext()));
                        break;
                    case 4:
                        db.Update_data_setting("3", "شكل 5");
                        P_text3.setText(new adapter_setting().adapter_number("شكل 5",getContext()));
                        break;
                }
                new adapter_setting().set_setting_text(getActivity());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switch_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isChecked) {
                            db.Update_data_setting("4", "مفعلة");
                            p_text_notif.setText(new adapter_setting().adapter_number("مفعلة",getContext()));
                        }
                        else{
                            db.Update_data_setting("4","غير مفعلة");
                            p_text_notif.setText(new adapter_setting().adapter_number("غير مفعلة",getContext()));
                        }
                    }
                });
            }
        });


        switch_bottom_bar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        praincipal_activity praincipal_activity=(praincipal_activity) getActivity();
                        if(isChecked) {
                            db.Update_data_setting("5", "مفعل");
                            p_text_bottom_bar.setText(new adapter_setting().adapter_number("مفعل",getContext()));
                            praincipal_activity.hide_navigate_bottom();
                            praincipal_activity.recreate();
                        }
                        else{
                            db.Update_data_setting("5","غير مفعل");
                            p_text_bottom_bar.setText(new adapter_setting().adapter_number("غير مفعل",getContext()));
                            praincipal_activity.hide_navigate_bottom();
                        }
                    }
                });


            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_edit_recipe_activity();
            }
        });

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });


        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String share="ننتظرك(ي) بفارغ الصبر لتنظم(ي) الى عالمنا المليء بوصفات الطبخ";
                share +="\n\n";
                share+="رابط التطبيق في المتجر";
                share+="\n\n";
                share+="https://play.google.com/store/apps/details?id=" + getContext().getPackageName();

                ShareCompat.IntentBuilder
                        .from(getActivity())
                        .setText(share)
                        .setType("text/plain")
                        .setChooserTitle("Apk coke app")
                        .startChooser();

            }
        });




        btn_deconexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("تسجيل الخروج");
                builder.setMessage("تسجيل الخروج سوف يفقدك(ي) الوصفات الغير مكتملة فقط");
                builder.setPositiveButton("نعم اريد تسجيل الخروج", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // FirebaseAuth.getInstance().signOut();
                                FirebaseAuth.getInstance().signOut();

                                disconnectFromFacebook();

                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();
                                GoogleSignInClient mgoogleSignInClient;
                                mgoogleSignInClient = GoogleSignIn.getClient(getContext(),gso);

                                mgoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        new sql_manager(getContext()).delet_all_data_account();
                                        new sql_manager(getContext()).delet_all_data_recipe();
                                        startActivity(new Intent(getActivity(),signin.class));
                                    }
                                });
                            }
                        });
                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onShow(DialogInterface dlg) {
                        dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    }
                });

                dialog.show();

            }
        });

        try {
            get_user_info();
        }catch (Exception e){

        }


        return myLayout;
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();
                new sql_manager(getContext()).delet_all_data_account();
                new sql_manager(getContext()).delet_all_data_recipe();
                startActivity(new Intent(getActivity(),signin.class));

            }
        }).executeAsync();
    }

    public void get_user_info(){
        sql_manager db = new sql_manager(getContext());
        account account=db.get__account();
        byte[] tof_saved=account.getPhoto_saved();
        Bitmap decodedByte = BitmapFactory.decodeByteArray(tof_saved, 0, tof_saved.length);
        image_profile.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte,120,120));
        title.setText(new adapter_setting().adapter_number(account.getName(),getContext()));
        bio.setText(new adapter_setting().adapter_number(account.getBio(),getContext()));
        download_image_profile.setVisibility(View.INVISIBLE);
    }


    public void get_Objet_setting(){

        sql_manager db=new sql_manager(getContext());
        Cursor res=db.getAllData_setting();




        int a=0;
        while (res.moveToNext()) {

            switch (a){
                case 0:
                    G_text1.setText(new adapter_setting().adapter_number(res.getString(1),getContext()));
                    P_text1.setText(new adapter_setting().adapter_number(res.getString(2),getContext()));
                    switch (res.getString(2)){
                        case "وضع ليلي":
                            switch1.setChecked(true);
                            break;
                        case "وضع نهاري":
                            switch1.setChecked(false);
                            break;
                    }
                break;
                case 1:
                    G_text2.setText(new adapter_setting().adapter_number(res.getString(1),getContext()));
                    P_text2.setText(new adapter_setting().adapter_number(res.getString(2),getContext()));
                    switch (res.getString(2)){
                        case "عربي":
                            switch2.setChecked(true);
                            break;
                        case "عجمي":
                            switch2.setChecked(false);
                            break;
                    }
                    break;
                case 2:
                    G_text3.setText(new adapter_setting().adapter_number(res.getString(1),getContext()));
                    P_text3.setText(new adapter_setting().adapter_number(res.getString(2),getContext()));

                    String[] st=new String[]{"شكل 1","شكل 2","شكل 3","شكل 4","شكل 5"};
                    @SuppressLint("ResourceType")
                    ArrayAdapter<String> adapter_cui =new ArrayAdapter(getContext(),R.layout.spn_item,st);
                    // Specify the layout to use when the list of choices appears
                    adapter_cui.setDropDownViewResource(simple_dropdown_item_1line);
                    // Apply the adapter to the spinner
                    spinner_text.setAdapter(adapter_cui);

                    ArrayList<String> n=new ArrayList<>();
                    for (String m:st){
                        n.add(m);
                    }
                    spinner_text.setSelection(n.indexOf(res.getString(2)), false);
                 break;
                case 3:
                   // G_text2.setText(new adapter_setting().adapter_number(res.getString(1));
                    p_text_notif.setText(new adapter_setting().adapter_number(res.getString(2),getContext()));
                    switch (res.getString(2)){
                        case "مفعلة":
                            switch_notif.setChecked(true);
                            break;
                        case "غير مفعلة":
                            switch_notif.setChecked(false);
                            break;
                    }
                    break;
                case 4:
                    // G_text2.setText(new adapter_setting().adapter_number(res.getString(1));
                    p_text_bottom_bar.setText(new adapter_setting().adapter_number(res.getString(2),getContext()));
                    switch (res.getString(2)){
                        case "مفعل":
                            switch_bottom_bar.setChecked(true);
                            break;
                        case "غير مفعل":
                            switch_bottom_bar.setChecked(false);
                            break;
                    }
                    break;
            }
            a++;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }




  /*  public void remplire_Image_items(){
        sql_manager db = new sql_manager(getContext());
        account account=db.get__account();
        Glide.with(this)
                .asBitmap()
                .load(account.getImage())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 30, baos); // bm is the bitmap object
                        byte[] b = baos.toByteArray();
                       db.Update_data_account(account.getAccount_ID(),
                               account.getName(),account.getBio(),b,account.getNumber_recipe());
                       get_user_info();
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

   */


    public void start_edit_recipe_activity(){
        edit_info_user fragment2 = new edit_info_user();
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment2);
        fragmentTransaction.commit();
    }


}