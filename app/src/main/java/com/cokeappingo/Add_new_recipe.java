package com.cokeappingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne.add_succeful_puc;
import com.cokeappingo.class_reglage.Chrono;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.account;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.les_alerts.alert;
import com.cokeappingo.list_ingredient_and_how_to_prpare.L_i_h_p;
import com.cokeappingo.local_notification_manger.noti_upload_data;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.cokeappingo.user_info.user_info;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.joooonho.SelectableRoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.pawelkleczkowski.customgauge.CustomGauge;

import static android.R.layout.expandable_list_content;
import static android.R.layout.simple_dropdown_item_1line;
import static android.R.layout.simple_spinner_dropdown_item;
import static android.app.Activity.RESULT_OK;

public class Add_new_recipe extends Fragment {



    //////// Vide //////////
    Button btn_add_recipe,btn_cancel_recipe;

    //////// en_cour + accept + not_complete///////
    Button btn_update;
    ConstraintLayout view_en_cour_accept;
    TextView txt_description_en_cour_accept;

    /////// refusse /////////
    ConstraintLayout view_refuse;
    TextView txt_refuse;




    EditText edt_title_recipe,edt_description_recipe,edt_ingredient,edt_prepare;
    RecyclerView rcv_ingredient,rcv_prepare;
    Spinner spn_person,spn_time,spn_categories,spn_cuisine;
    TextView txv_title_count,txv_description_count,txv_ingredient_count,txv_prepare_count;
    ImageButton btn_add_ingredient,btn_add_prepare,btn_select_image;
    View v_spinner_person,v_spinner_time,v_spinner_categories,v_spinner_cuisine,v_a,v_b;
    SelectableRoundedImageView img_recipe;
    ImageView img_check_image,img_check_title,img_check_description,img_check_ingredient,img_check_prepare,img_check_person,img_check_time
            ,img_check_categories,img_check_cuisine;


    ArrayList<Integer> arl_person,arl_time;
    ArrayList<String> arl_cui=new ArrayList<>();
    ArrayList<String> arl_cat=new ArrayList<>();



    private Uri mCropImageUri,IMAGE_CROPING;
    int focus=0;

    int back_pressed=0;
    L_i_h_p adapter,adapter2;

    boolean recipe_not_complet_deja_exist=false;
    boolean data_uploaded=false;
    new_recipe the_recipe_come;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myLayout = inflater.inflate(R.layout.activity_add_new_recipe, container, false);


        btn_add_ingredient =myLayout.findViewById(R.id.btn_add_ingredient);
        btn_add_prepare=myLayout.findViewById(R.id.btn_add_prepare);
        btn_select_image=myLayout.findViewById(R.id.btn_image_recipe);
        btn_add_recipe=myLayout.findViewById(R.id.btn_add);
        btn_cancel_recipe=myLayout.findViewById(R.id.btn_cancel);
        btn_update=myLayout.findViewById(R.id.btn_update);

        edt_ingredient =myLayout.findViewById(R.id.input_ingredient);
        edt_title_recipe=myLayout.findViewById(R.id.input_title);
        edt_description_recipe=myLayout.findViewById(R.id.input_description);
        edt_prepare=myLayout.findViewById(R.id.input_prepare);

        spn_person=myLayout.findViewById(R.id.number_person);
        spn_time=myLayout.findViewById(R.id.number_time);
        spn_categories=myLayout.findViewById(R.id.number_categories);
        spn_cuisine=myLayout.findViewById(R.id.number_cuisine);

        rcv_ingredient=myLayout.findViewById(R.id.list_ingredients);
        rcv_prepare=myLayout.findViewById(R.id.list_prepare);

        txv_title_count=myLayout.findViewById(R.id.title_count);
        txv_description_count=myLayout.findViewById(R.id.description_count);
        txv_ingredient_count=myLayout.findViewById(R.id.ingredient_count);
        txv_prepare_count=myLayout.findViewById(R.id.prepare_count);
        txt_refuse=myLayout.findViewById(R.id.text_description_reason_refuse);
        txt_description_en_cour_accept=myLayout.findViewById(R.id.text_description_info_update);

        v_spinner_person=myLayout.findViewById(R.id.view_spinner_person);
        v_spinner_time=myLayout.findViewById(R.id.view_spinner_time);
        v_spinner_categories=myLayout.findViewById(R.id.view_spinner_categories);
        v_spinner_cuisine=myLayout.findViewById(R.id.view_spinner_cuisine);
        view_en_cour_accept=myLayout.findViewById(R.id.view_info_update);
        view_refuse=myLayout.findViewById(R.id.view_raison_to_refuse);
        v_a=myLayout.findViewById(R.id.vbi);
        v_b=myLayout.findViewById(R.id.vbp);

        img_recipe=myLayout.findViewById(R.id.image_image_recipe);

        img_check_image=myLayout.findViewById(R.id.check_image);
        img_check_title=myLayout.findViewById(R.id.check_title);
        img_check_description=myLayout.findViewById(R.id.check_description);
        img_check_ingredient=myLayout.findViewById(R.id.check_ingredient);
        img_check_prepare=myLayout.findViewById(R.id.check_prepare);
        img_check_person=myLayout.findViewById(R.id.check_person);
        img_check_time=myLayout.findViewById(R.id.check_time);
        img_check_categories=myLayout.findViewById(R.id.check_categories);
        img_check_cuisine=myLayout.findViewById(R.id.check_cuisine);

        arl_person=new ArrayList<>();
        arl_time=new ArrayList<>();



        /////////////////  Select_image_recipe //////////////////////////////////////////////////////
        btn_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onSelectImageClick();
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////



        ///////////////// Remplire_title_recipe ////////////////////////////////////////////////////

        edt_title_recipe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txv_title_count.setVisibility(View.VISIBLE);
                txv_title_count.setText(new adapter_setting().adapter_number(35 - s.toString().length() + "/35",getContext()));
            }
        });

        edt_title_recipe.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edt_title_recipe.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(edt_title_recipe.getText().toString().isEmpty())
                        edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                    else {
                        edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));
                        img_check_title.setBackground(getResources().getDrawable(R.drawable.complet));
                    }
                }
                return false;
            }
        });

        edt_title_recipe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus){
                    if(edt_title_recipe.getText().toString().isEmpty())
                    edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                    else {
                        edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));
                        img_check_title.setBackground(getResources().getDrawable(R.drawable.complet));
                    }
                }
                else {
                    edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                    img_check_title.setBackground(getResources().getDrawable(R.drawable.not_complet));
                }
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////


        ///////////////// Remplire_Description_recipe ////////////////////////////////////////////////////

        edt_description_recipe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txv_description_count.setVisibility(View.VISIBLE);
                txv_description_count.setText(new adapter_setting().adapter_number(200 - s.toString().length() + "/200",getContext()));
            }
        });


        edt_description_recipe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if(edt_description_recipe.getText().toString().isEmpty())
                        edt_description_recipe.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                    else {
                        edt_description_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));
                        img_check_description.setBackground(getResources().getDrawable(R.drawable.complet));
                    }
                }
                else {
                    focus=2;
                    edt_description_recipe.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                    img_check_description.setBackground(getResources().getDrawable(R.drawable.not_complet));
                }
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////


        /////////////////// stoke_ingredients //////////////////////////////////////////////////////

        adapter = new L_i_h_p(getContext(),new ArrayList<String>(),true);
        rcv_ingredient.setAdapter(adapter);
        rcv_ingredient.setLayoutManager(new LinearLayoutManager(getContext()));

        final boolean[] set_show_ingredient = {true};
        btn_add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(set_show_ingredient[0]){
                    rcv_ingredient.setVisibility(View.VISIBLE);
                    set_show_ingredient[0] =false;
                }
                if(!edt_ingredient.getText().toString().equals("")){
                adapter.update(edt_ingredient.getText().toString().trim());
                 rcv_ingredient.getAdapter().notifyItemInserted(adapter.getItemCount()-1);
                edt_ingredient.setText(new adapter_setting().adapter_number(null,getContext()));}
            }
        });

        edt_ingredient.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edt_ingredient.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(set_show_ingredient[0]){
                        rcv_ingredient.setVisibility(View.VISIBLE);
                        set_show_ingredient[0] =false;
                    }
                    if(!edt_ingredient.getText().toString().equals("")){
                        adapter.update(edt_ingredient.getText().toString().trim());
                        edt_ingredient.setText(new adapter_setting().adapter_number(null,getContext()));}
                    if(adapter.get_list().size()==0)
                        img_check_ingredient.setBackground(getResources().getDrawable(R.drawable.not_complet));
                    else
                        img_check_ingredient.setBackground(getResources().getDrawable(R.drawable.complet));
                }
                return false;
            }
        });
        edt_ingredient.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(adapter.get_list().size()==0)
                        img_check_ingredient.setBackground(getResources().getDrawable(R.drawable.not_complet));
                    else
                        img_check_ingredient.setBackground(getResources().getDrawable(R.drawable.complet));
                }
                else {
                    img_check_ingredient.setBackground(getResources().getDrawable(R.drawable.not_complet));
                }
            }
        });

        edt_ingredient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txv_ingredient_count.setVisibility(View.VISIBLE);
                txv_ingredient_count.setText(new adapter_setting().adapter_number(30 - s.toString().length() + "/30",getContext()));
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////


        /////////////////// stoke_prepare //////////////////////////////////////////////////////

        adapter2 = new L_i_h_p(getContext(),new ArrayList<String>(),true);
        rcv_prepare.setAdapter(adapter2);
        rcv_prepare.setLayoutManager(new LinearLayoutManager(getContext()));


        final boolean[] set_show_prepare = {true};
        btn_add_prepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(set_show_prepare[0]){
                    rcv_prepare.setVisibility(View.VISIBLE);
                    set_show_prepare[0] =false;
                }
                if(!edt_prepare.getText().toString().equals("")){
                adapter2.update(edt_prepare.getText().toString().trim());
                rcv_prepare.getAdapter().notifyItemInserted(adapter2.getItemCount()-1);
                edt_prepare.setText(new adapter_setting().adapter_number(null,getContext()));}
            }
        });

        edt_prepare.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edt_prepare.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(set_show_prepare[0]){
                        rcv_prepare.setVisibility(View.VISIBLE);
                        set_show_prepare[0] =false;
                    }
                    if(!edt_prepare.getText().toString().equals("")){
                        adapter2.update(edt_prepare.getText().toString().trim());
                        edt_prepare.setText(new adapter_setting().adapter_number(null,getContext()));}
                    if(adapter2.get_list().size()==0)
                        img_check_prepare.setBackground(getResources().getDrawable(R.drawable.not_complet));
                    else
                        img_check_prepare.setBackground(getResources().getDrawable(R.drawable.complet));
                }
                return false;
            }
        });
        edt_prepare.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                  if(adapter2.get_list().size()==0)
                      img_check_prepare.setBackground(getResources().getDrawable(R.drawable.not_complet));
                  else
                      img_check_prepare.setBackground(getResources().getDrawable(R.drawable.complet));
                }
                else {
                    focus=4;
                    img_check_prepare.setBackground(getResources().getDrawable(R.drawable.not_complet));
                }
            }
        });

        edt_prepare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txv_prepare_count.setVisibility(View.VISIBLE);
                txv_prepare_count.setText(new adapter_setting().adapter_number(150 - s.toString().length() + "/150",getContext()));
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////// Number_person_partie /////////////////////////////////////////////////////

       for (int i=0;i<=15;i++){
            arl_person.add(i);
       }
        @SuppressLint("ResourceType")
        ArrayAdapter<CharSequence> adapter_I =new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,arl_person);
                 // Specify the layout to use when the list of choices appears
        adapter_I.setDropDownViewResource(simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spn_person.setAdapter(adapter_I);

        spn_person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    img_check_person.setBackground(getResources().getDrawable(R.drawable.not_complet));
                    v_spinner_person.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                }else{
                    v_spinner_person.setBackground(getResources().getDrawable(R.drawable.write_good));
                    img_check_person.setBackground(getResources().getDrawable(R.drawable.complet));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////// Number_time_partie /////////////////////////////////////////////////////

        for (int i=0;i<=200;i++){
            arl_time.add(i);
        }
        @SuppressLint("ResourceType")
        ArrayAdapter<CharSequence> adapter_T =new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,arl_time);
        // Specify the layout to use when the list of choices appears
        adapter_T.setDropDownViewResource(simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spn_time.setAdapter(adapter_T);

        spn_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    img_check_time.setBackground(getResources().getDrawable(R.drawable.not_complet));
                    v_spinner_time.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                }else{
                    v_spinner_time.setBackground(getResources().getDrawable(R.drawable.write_good));
                    img_check_time.setBackground(getResources().getDrawable(R.drawable.complet));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////




        ////////////////// categories_partie /////////////////////////////////////////////////////


        try {
        InputStream is = getResources().openRawResource(R.raw.spinner_add_recipe);
        int size = is.available();
        byte[] buffer = new byte[size];

        is.read(buffer);

        is.close();
        String json = new String(buffer, "UTF-8");

        JSONObject jsonObject = new JSONObject(json).getJSONObject("spinner_1");
            for(int n = 0; n <35; n++)
            {
            arl_cat.add(jsonObject.getString("objet_"+n));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


        @SuppressLint("ResourceType")
        ArrayAdapter<String> adapter_Cat =new ArrayAdapter(getContext(),android.R.layout.simple_dropdown_item_1line,arl_cat);
        // Specify the layout to use when the list of choices appears
        adapter_Cat.setDropDownViewResource(simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        spn_categories.setAdapter(adapter_Cat);

        spn_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    img_check_categories.setBackground(getResources().getDrawable(R.drawable.not_complet));
                    v_spinner_categories.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                }else{
                    v_spinner_categories.setBackground(getResources().getDrawable(R.drawable.write_good));
                    img_check_categories.setBackground(getResources().getDrawable(R.drawable.complet));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////





        ////////////////// cuisine_partie /////////////////////////////////////////////////////


        try {
            InputStream is = getResources().openRawResource(R.raw.spinner_add_recipe);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json).getJSONObject("spinner_2");
            for(int n = 0; n <17; n++)
            {
                arl_cui.add(jsonObject.getString("objet_"+n));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


        @SuppressLint("ResourceType")
        ArrayAdapter<String> adapter_cui =new ArrayAdapter(getContext(),android.R.layout.simple_dropdown_item_1line,arl_cui);
        // Specify the layout to use when the list of choices appears
        adapter_cui.setDropDownViewResource(simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        spn_cuisine.setAdapter(adapter_cui);

        spn_cuisine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    img_check_cuisine.setBackground(getResources().getDrawable(R.drawable.not_complet));
                    v_spinner_cuisine.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                }else{
                    v_spinner_cuisine.setBackground(getResources().getDrawable(R.drawable.write_good));
                    img_check_cuisine.setBackground(getResources().getDrawable(R.drawable.complet));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////









        ////////////////// ADD_RECIPE_PARTIE  //////////////////////////////////////////////////////////
        final noti_upload_data noti =new noti_upload_data(getContext());

        btn_add_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_recipe.getDrawable() == null) {
                    focus_to_Check(img_check_image);
                    return;
                } else if (edt_title_recipe.getText().toString().equals("")) {
                    focus_to_Check(img_check_title);
                    edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                } else if (edt_description_recipe.getText().toString().equals("")) {
                    focus_to_Check(img_check_description);
                    edt_description_recipe.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                } else if (adapter.get_list().size() == 0) {
                    focus_to_Check(img_check_ingredient);
                    return;
                } else if (adapter2.get_list().size() == 0) {
                    focus_to_Check(img_check_prepare);
                    return;
                } else if ((int) spn_person.getSelectedItem() == 0) {
                    focus_to_Check(img_check_person);
                    v_spinner_person.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                } else if ((int) spn_time.getSelectedItem() == 0) {
                    focus_to_Check(img_check_time);
                    v_spinner_time.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                } else if (spn_categories.getSelectedItemPosition() == 0) {
                    focus_to_Check(img_check_categories);
                    v_spinner_categories.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                } else if (spn_cuisine.getSelectedItemPosition() == 0) {
                    focus_to_Check(img_check_cuisine);
                    v_spinner_cuisine.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                }

                edt_description_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));
                img_check_description.setBackground(getResources().getDrawable(R.drawable.complet));

                img_check_prepare.setBackground(getResources().getDrawable(R.drawable.complet));

                final new_recipe my_recipe = new new_recipe();

                my_recipe.setHow_to_prepare(adapter2.get_list());
                my_recipe.setIngredient(adapter.get_list());


                my_recipe.setTitle(edt_title_recipe.getText().toString().trim());
                my_recipe.setDescription(edt_description_recipe.getText().toString().trim());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd'\n'HH:mm", Locale.US);

                my_recipe.setDate(formatter.format(new Date()));
                my_recipe.setCooking_time(spn_time.getSelectedItem() + "");
                my_recipe.setPerson(spn_person.getSelectedItem() + "");
                my_recipe.setStatus_recipe("pas encore");

                my_recipe.setAuteur(FirebaseAuth.getInstance().getCurrentUser().getUid());
                my_recipe.setAuteur_ID(new user_info().get_info_account(GoogleSignIn.getLastSignedInAccount(getContext())).getAccount_ID());

                Uri image_hight_resolution_for_the_the_device=null;

                if (the_recipe_come.getStatus_recipe().equals("not complete") && IMAGE_CROPING==null && the_recipe_come.getImage()!=null){
                    byte[] tof_saved = the_recipe_come.getImage();
                    IMAGE_CROPING=getImageUri(getContext(),
                            new adapter_setting().getResizedBitmap_added_to_fire_base(
                            BitmapFactory.decodeByteArray(tof_saved, 0, tof_saved.length)));
                    image_hight_resolution_for_the_the_device=IMAGE_CROPING;
                }else{
                    ///////////////////// pour diminer la qualiter to 25
                    ContentResolver cr = getContext().getContentResolver();
                    try {
                        image_hight_resolution_for_the_the_device=IMAGE_CROPING;
                        Bitmap bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, IMAGE_CROPING);
                        bitmap=new adapter_setting().getResizedBitmap_added_to_fire_base(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
                        byte[] b = baos.toByteArray();
                        IMAGE_CROPING=getImageUri(getContext(),BitmapFactory.decodeByteArray(b, 0, b.length));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                noti.notif_upload(4,0);

                alert(4);
                final Uri finalImage_hight_resolution_for_the_the_device = image_hight_resolution_for_the_the_device;
                new data_user_onligne().add_picture(IMAGE_CROPING, new add_succeful_puc() {
                        @Override
                        public void add_complet(final String Url, final int id) {
                            progress_aniation(0, 1, new endanimation() {
                                @Override
                                public void oncallback() {
                                    noti.notif_upload(4,1);
                                    my_recipe.setID(id + "");
                                    my_recipe.setLien_puc(Url);
                                    my_recipe.setCategories(spn_categories.getSelectedItem().toString());
                                    my_recipe.setCuisine(spn_cuisine.getSelectedItem().toString());
                                    final account account = new sql_manager(getContext()).get__account();

                                    new data_user_onligne().update_number_recipe_user(account, new data_user_onligne.total_number_recipe_update() {
                                        @Override
                                        public void callback(final int new_number) {
                                            progress_aniation(1, 2, new endanimation() {
                                                @Override
                                                public void oncallback() {
                                                    noti.notif_upload(4,2);
                                                    new data_user_onligne().add_new_recipe(my_recipe, new data_user_onligne.return_id_push() {
                                                        @Override
                                                        public void add_complet(final String id_push) {
                                                            progress_aniation(2, 3, new endanimation() {
                                                                @Override
                                                                public void oncallback() {
                                                                    noti.notif_upload(4,3);
                                                                    new data_user_onligne().add_recipe_to_List_attent_admin(id_push, new data_user_onligne.add_succeful() {
                                                                        @Override
                                                                        public void add_complet(boolean etat) {
                                                                            progress_aniation(3, 4, new endanimation() {
                                                                                @Override
                                                                                public void oncallback() {
                                                                                    noti.notif_upload(4,4);
                                                                                    ContentResolver cr = getContext().getContentResolver();
                                                                                    try {
                                                                                        Bitmap bitmap = android.provider.MediaStore.Images.Media
                                                                                                .getBitmap(cr, finalImage_hight_resolution_for_the_the_device);
                                                                                        bitmap=new adapter_setting().getResizedBitmap(bitmap,864,550);
                                                                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
                                                                                        byte[] b = baos.toByteArray();
                                                                                        my_recipe.setImage(b);
                                                                                        my_recipe.setId_push(id_push);
                                                                                        delet_file(IMAGE_CROPING);
                                                                                      //delet_file(finalImage_hight_resolution_for_the_the_device);
                                                                                        if (recipe_not_complet_deja_exist)
                                                                                            new sql_manager(getContext()).delet_data_recipe(the_recipe_come.getID());
                                                                                        new sql_manager(getContext()).insert_data_into_recipe(my_recipe);

                                                                                        progress.setVisibility(View.INVISIBLE);
                                                                                        lottie.setVisibility(View.INVISIBLE);
                                                                                        lottie_succes.setVisibility(View.VISIBLE);
                                                                                        lottie_succes.playAnimation();
                                                                                        text_download.setText(new adapter_setting().adapter_number("تم الحفظ بنجاح",getContext()));
                                                                                        data_uploaded=true;

                                                                                        sql_manager sql_manager=new sql_manager(getContext());
                                                                                        account update_number=sql_manager.get__account();
                                                                                        sql_manager.Update_data_account(update_number.getAccount_ID()
                                                                                        ,update_number.getName(),update_number.getBio(),update_number.getPhoto_saved()
                                                                                                ,new_number,update_number.getImage());


                                                                                        Handler handler = new Handler();
                                                                                        handler.postDelayed(new Runnable() {
                                                                                            public void run() {
                                                                                                back_pressed = 1;
                                                                                                    dialog.dismiss();
                                                                                                    onBackPressed();

                                                                                            }
                                                                                        }, 2000);

                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////


        btn_cancel_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_recipe.getDrawable()!=null || !edt_title_recipe.getText().toString().equals("") ||
                        !edt_description_recipe.getText().toString().equals("") ||
                        adapter.get_list().size()!=0 || adapter2.get_list().size()!=0){
                    snack_bar_cancel();
                }else
                    onBackPressed();
            }
        });



        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_recipe.getDrawable() == null) {
                    focus_to_Check(img_check_image);
                    return;
                }
                else if (edt_title_recipe.getText().toString().equals("")) {
                    focus_to_Check(img_check_title);
                    edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                }
                else if (edt_description_recipe.getText().toString().equals("")) {
                    focus_to_Check(img_check_description);
                    edt_description_recipe.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                }
                else if (adapter.get_list().size() == 0) {
                    focus_to_Check(img_check_ingredient);
                    return;
                }
                else if (adapter2.get_list().size() == 0) {
                    focus_to_Check(img_check_prepare);
                    return;
                }
                else if ((int) spn_person.getSelectedItem() == 0) {
                    focus_to_Check(img_check_person);
                    v_spinner_person.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                }
                else if ((int) spn_time.getSelectedItem() == 0) {
                    focus_to_Check(img_check_time);
                    v_spinner_time.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                }
                else if (spn_categories.getSelectedItemPosition() == 0) {
                    focus_to_Check(img_check_categories);
                    v_spinner_categories.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                }
                else if (spn_cuisine.getSelectedItemPosition() == 0) {
                    focus_to_Check(img_check_cuisine);
                    v_spinner_cuisine.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                }


                the_recipe_come.setTitle(edt_title_recipe.getText().toString().trim());
                the_recipe_come.setDescription(edt_description_recipe.getText().toString().trim());
                the_recipe_come.setCooking_time(spn_time.getSelectedItem() + "");
                the_recipe_come.setPerson(spn_person.getSelectedItem() + "");
                the_recipe_come.setCategories(spn_categories.getSelectedItem().toString());
                the_recipe_come.setCuisine(spn_cuisine.getSelectedItem().toString());
                the_recipe_come.setHow_to_prepare(adapter2.get_list());
                the_recipe_come.setIngredient(adapter.get_list());

                the_recipe_come.setAuteur(FirebaseAuth.getInstance().getCurrentUser().getUid());
                the_recipe_come.setAuteur_ID(new user_info().get_info_account(GoogleSignIn.getLastSignedInAccount(getContext())).getAccount_ID());

                final Uri image_hight_resolution_for_the_the_device=IMAGE_CROPING;

                if (IMAGE_CROPING!=null){
                    noti.notif_upload(3,0);
                    alert(3);
                    progress_aniation(0, 1, new endanimation() {
                        @Override
                        public void oncallback() {
                            noti.notif_upload(3,1);
                            ContentResolver cr = getContext().getContentResolver();
                            try {
                                Bitmap bitmap = android.provider.MediaStore.Images.Media
                                        .getBitmap(cr, IMAGE_CROPING);
                                bitmap=new adapter_setting().getResizedBitmap_added_to_fire_base(bitmap);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
                                byte[] b = baos.toByteArray();
                                IMAGE_CROPING=getImageUri(getContext(),BitmapFactory.decodeByteArray(b, 0, b.length));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            new data_user_onligne().update_picture_recipe(the_recipe_come.getID(), IMAGE_CROPING, new add_succeful_puc() {
                                @Override
                                public void add_complet(String Url, int id) {
                                    the_recipe_come.setLien_puc(Url);
                                    progress_aniation(1, 2, new endanimation() {
                                        @Override
                                        public void oncallback() {
                                            noti.notif_upload(3,2);
                                            the_recipe_come.setImage(null);
                                            new data_user_onligne().up_date_recipe(the_recipe_come, new data_user_onligne.add_succeful() {
                                                @Override
                                                public void add_complet(boolean etat) {
                                                    progress_aniation(2, 3, new endanimation() {
                                                        @Override
                                                        public void oncallback() {
                                                            noti.notif_upload(3,3);
                                                            delet_file(IMAGE_CROPING);
                                                            ContentResolver cr = getContext().getContentResolver();
                                                            try {
                                                                Bitmap bitmap = android.provider.MediaStore.Images.Media
                                                                        .getBitmap(cr, image_hight_resolution_for_the_the_device);
                                                                bitmap=new adapter_setting().getResizedBitmap(bitmap,864,550);
                                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
                                                                delet_file(image_hight_resolution_for_the_the_device);
                                                                byte[] b = baos.toByteArray();
                                                                the_recipe_come.setImage(b);
                                                                new sql_manager(getContext()).Update_data_recipe(the_recipe_come);
                                                                data_uploaded=true;
                                                                progress.setVisibility(View.INVISIBLE);
                                                                lottie.setVisibility(View.INVISIBLE);
                                                                lottie_succes.setVisibility(View.VISIBLE);
                                                                lottie_succes.playAnimation();
                                                                text_download.setText(new adapter_setting().adapter_number("تم التحديث بنجاح",getContext()));

                                                                Handler handler = new Handler();
                                                                handler.postDelayed(new Runnable() {
                                                                    public void run() {
                                                                        back_pressed = 1;
                                                                        dialog.dismiss();
                                                                        onBackPressed();
                                                                    }
                                                                }, 2000);

                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else{
                    alert(2);
                    noti.notif_upload(2,0);
                    progress_aniation(0, 1, new endanimation() {
                        @Override
                        public void oncallback() {
                            noti.notif_upload(2,1);
                            final byte[] my_image=the_recipe_come.getImage();
                            the_recipe_come.setImage(null);
                            new data_user_onligne().up_date_recipe(the_recipe_come, new data_user_onligne.add_succeful() {
                                @Override
                                public void add_complet(boolean etat) {
                                    progress_aniation(1, 2, new endanimation() {
                                        @Override
                                        public void oncallback() {
                                            noti.notif_upload(2,2);
                                                the_recipe_come.setImage(my_image);
                                                new sql_manager(getContext()).Update_data_recipe(the_recipe_come);

                                                progress.setVisibility(View.INVISIBLE);
                                                lottie.setVisibility(View.INVISIBLE);
                                                lottie_succes.setVisibility(View.VISIBLE);
                                                lottie_succes.playAnimation();
                                                text_download.setText(new adapter_setting().adapter_number("تم التحديث بنجاح",getContext()));
                                                data_uploaded=true;
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    public void run() {
                                                        back_pressed = 1;
                                                        dialog.dismiss();
                                                        onBackPressed();
                                                    }
                                                }, 2000);

                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });



            String id_recipe=getArguments().getString("recipe");
            if(!id_recipe.equals("new"))
                the_recipe_come =new sql_manager(getContext()).getData_recipe_user_by_id(id_recipe);
            else {
                the_recipe_come = new new_recipe();
                the_recipe_come.setStatus_recipe("rr");
            }
            ///////////////////   GET NAME OPERATION  ////////////////////////
            switch (the_recipe_come.getStatus_recipe()) {
                case "ok":
                /*    view_en_cour_accept.setVisibility(View.VISIBLE);
                    view_en_cour_accept.setBackground(getResources().getDrawable(R.drawable.back_info_ok_recipe));
                    txt_description_en_cour_accept.setText(new adapter_setting().adapter_number(" تهانينا \uD83C\uDF89\uD83C\uDF89\uD83C\uDF89   \n \nلقد تم نشر هاته الوصفة بتاريخ " + the_recipe_come.getDate_publication(),getContext()));

                    btn_cancel_recipe.setVisibility(View.GONE);
                    btn_add_recipe.setVisibility(View.GONE);
                    btn_update.setVisibility(View.GONE);

                    byte[] tof_saved = the_recipe_come.getImage();
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(tof_saved, 0, tof_saved.length);
                    img_recipe.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte,864,550));
                    img_recipe.setVisibility(View.VISIBLE);
                    btn_select_image.setVisibility(View.GONE);
                    img_check_image.setBackground(getResources().getDrawable(R.drawable.complet));

                    edt_title_recipe.setText(new adapter_setting().adapter_number(the_recipe_come.getTitle(),getContext()));
                    img_check_title.setBackground(getResources().getDrawable(R.drawable.complet));
                    edt_title_recipe.setFocusable(false);
                    edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));


                    edt_description_recipe.setText(new adapter_setting().adapter_number(the_recipe_come.getDescription(),getContext()));
                    img_check_description.setBackground(getResources().getDrawable(R.drawable.complet));
                    edt_description_recipe.setFocusable(false);
                    edt_description_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));

                    img_check_ingredient.setBackground(getResources().getDrawable(R.drawable.complet));
                    img_check_prepare.setBackground(getResources().getDrawable(R.drawable.complet));

                    btn_add_prepare.setVisibility(View.INVISIBLE);
                    btn_add_ingredient.setVisibility(View.INVISIBLE);
                    v_a.setVisibility(View.INVISIBLE);
                    v_b.setVisibility(View.INVISIBLE);
                    v_spinner_categories.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_cuisine.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_time.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_person.setBackground(getResources().getDrawable(R.drawable.write_good));

                    rcv_ingredient.setAdapter(new L_i_h_p(getContext(), the_recipe_come.getIngredient(), false));
                    rcv_ingredient.setLayoutManager(new LinearLayoutManager(getContext()));
                    edt_ingredient.setVisibility(View.GONE);
                    rcv_prepare.setVisibility(View.VISIBLE);

                    rcv_prepare.setAdapter(new L_i_h_p(getContext(), the_recipe_come.getHow_to_prepare(), false));
                    rcv_prepare.setLayoutManager(new LinearLayoutManager(getContext()));
                    edt_prepare.setVisibility(View.GONE);
                    rcv_ingredient.setVisibility(View.VISIBLE);



                    spn_time.setSelection(arl_time.indexOf(Integer.parseInt(the_recipe_come.getCooking_time())), false);
                    spn_person.setSelection(arl_person.indexOf(Integer.parseInt(the_recipe_come.getPerson())), false);

                    spn_cuisine.setSelection(arl_cui.indexOf(the_recipe_come.getCuisine()), false);
                    spn_categories.setSelection(arl_cat.indexOf(the_recipe_come.getCategories()), false);

                    back_pressed=1;

                 */
                    break;
                case "pas encore":
                    Log.e("tt",the_recipe_come.getHow_to_prepare().toString());

                    view_en_cour_accept.setVisibility(View.VISIBLE);
                    view_en_cour_accept.setBackground(getResources().getDrawable(R.drawable.back_info_update_recipe));
                    txt_description_en_cour_accept.setText(new adapter_setting().adapter_number("هاته الوصفة لا زالت في طور المعالجة ، يمكنكم تحديث اي معطى من معطياتها",getContext()));

                    btn_cancel_recipe.setVisibility(View.INVISIBLE);
                    btn_add_recipe.setVisibility(View.INVISIBLE);
                    btn_update.setVisibility(View.VISIBLE);

                    byte[] tof_saved2 = the_recipe_come.getImage();
                    Bitmap decodedByte2 = BitmapFactory.decodeByteArray(tof_saved2, 0, tof_saved2.length);
                    img_recipe.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte2,864,550));
                    img_recipe.setVisibility(View.VISIBLE);
                    img_check_image.setBackground(getResources().getDrawable(R.drawable.complet));

                    edt_title_recipe.setText(new adapter_setting().adapter_number(the_recipe_come.getTitle(),getContext()));
                    img_check_title.setBackground(getResources().getDrawable(R.drawable.complet));
                    edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));


                    edt_description_recipe.setText(new adapter_setting().adapter_number(the_recipe_come.getDescription(),getContext()));
                    img_check_description.setBackground(getResources().getDrawable(R.drawable.complet));
                    edt_description_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));

                    img_check_ingredient.setBackground(getResources().getDrawable(R.drawable.complet));
                    img_check_prepare.setBackground(getResources().getDrawable(R.drawable.complet));

                    v_spinner_categories.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_cuisine.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_time.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_person.setBackground(getResources().getDrawable(R.drawable.write_good));

                    rcv_ingredient.setAdapter(new L_i_h_p(getContext(), the_recipe_come.getIngredient(), true));
                    adapter=new L_i_h_p(getContext(), the_recipe_come.getIngredient(), true);
                    rcv_ingredient.setLayoutManager(new LinearLayoutManager(getContext()));
                    rcv_prepare.setVisibility(View.VISIBLE);

                    rcv_prepare.setAdapter(new L_i_h_p(getContext(), the_recipe_come.getHow_to_prepare(), true));
                    adapter2=new L_i_h_p(getContext(), the_recipe_come.getHow_to_prepare(), true);
                    rcv_prepare.setLayoutManager(new LinearLayoutManager(getContext()));
                    rcv_ingredient.setVisibility(View.VISIBLE);

                    spn_time.setSelection(arl_time.indexOf(Integer.parseInt(the_recipe_come.getCooking_time())), false);
                    spn_person.setSelection(arl_person.indexOf(Integer.parseInt(the_recipe_come.getPerson())), false);

                    spn_cuisine.setSelection(arl_cui.indexOf(the_recipe_come.getCuisine()), false);
                    spn_categories.setSelection(arl_cat.indexOf(the_recipe_come.getCategories()), false);

                    back_pressed=1;
                    break;
                case "not complete":
                    recipe_not_complet_deja_exist=true;
                    btn_cancel_recipe.setVisibility(View.VISIBLE);
                    btn_add_recipe.setVisibility(View.VISIBLE);
                    btn_update.setVisibility(View.INVISIBLE);

                    try {
                        byte[] tof_saved4 = the_recipe_come.getImage();
                        Bitmap decodedByte4 = BitmapFactory.decodeByteArray(tof_saved4, 0, tof_saved4.length);
                        img_recipe.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte4,864,550));
                        img_recipe.setVisibility(View.VISIBLE);
                        img_check_image.setBackground(getResources().getDrawable(R.drawable.complet));
                    }catch (Exception e){}

                    try{
                    edt_title_recipe.setText(new adapter_setting().adapter_number(the_recipe_come.getTitle(),getContext()));
                    img_check_title.setBackground(getResources().getDrawable(R.drawable.complet));
                    edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));
                    }catch (Exception e){}


                    try {
                    edt_description_recipe.setText(new adapter_setting().adapter_number(the_recipe_come.getDescription(),getContext()));
                    if (!the_recipe_come.getDescription().equals("")) {
                        img_check_description.setBackground(getResources().getDrawable(R.drawable.complet));
                        edt_description_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));
                    }
                    }catch (Exception e){}



                    try {
                    rcv_ingredient.setAdapter(new L_i_h_p(getContext(), the_recipe_come.getIngredient(), true));
                    adapter=new L_i_h_p(getContext(), the_recipe_come.getIngredient(), true);
                    rcv_ingredient.setLayoutManager(new LinearLayoutManager(getContext()));
                    rcv_prepare.setVisibility(View.VISIBLE);
                    if (!(the_recipe_come.getIngredient().size()==0)) {
                        img_check_ingredient.setBackground(getResources().getDrawable(R.drawable.complet));
                    }
                    }catch (Exception e){}

                    try {
                    rcv_prepare.setAdapter(new L_i_h_p(getContext(), the_recipe_come.getHow_to_prepare(), true));
                    adapter2=new L_i_h_p(getContext(), the_recipe_come.getHow_to_prepare(), true);
                    rcv_prepare.setLayoutManager(new LinearLayoutManager(getContext()));
                    rcv_ingredient.setVisibility(View.VISIBLE);
                    if (!(the_recipe_come.getHow_to_prepare().size()==0)) {
                        img_check_prepare.setBackground(getResources().getDrawable(R.drawable.complet));
                    }
                    }catch (Exception e){}



                    v_spinner_categories.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_cuisine.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_time.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_person.setBackground(getResources().getDrawable(R.drawable.write_good));
                    spn_time.setSelection(arl_time.indexOf(Integer.parseInt(the_recipe_come.getCooking_time())), false);
                    spn_person.setSelection(arl_person.indexOf(Integer.parseInt(the_recipe_come.getPerson())), false);

                    spn_cuisine.setSelection(arl_cui.indexOf(the_recipe_come.getCuisine()), false);
                    spn_categories.setSelection(arl_cat.indexOf(the_recipe_come.getCategories()), false);
                    break;
                case "refused":
                    view_refuse.setVisibility(View.VISIBLE);
                    view_refuse.setBackground(getResources().getDrawable(R.drawable.back_reason_to_refuse));
                    txt_refuse.setText(new adapter_setting().adapter_number(the_recipe_come.getCause_refuse(),getContext()));

                    btn_cancel_recipe.setVisibility(View.INVISIBLE);
                    btn_add_recipe.setVisibility(View.INVISIBLE);
                    btn_update.setVisibility(View.INVISIBLE);

                    byte[] tof_saved3 = the_recipe_come.getImage();
                    Bitmap decodedByte3 = BitmapFactory.decodeByteArray(tof_saved3, 0, tof_saved3.length);
                    img_recipe.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte3,864,550));
                    img_recipe.setVisibility(View.VISIBLE);
                    btn_select_image.setVisibility(View.GONE);
                    img_check_image.setBackground(getResources().getDrawable(R.drawable.complet));

                    edt_title_recipe.setText(new adapter_setting().adapter_number(the_recipe_come.getTitle(),getContext()));
                    img_check_title.setBackground(getResources().getDrawable(R.drawable.complet));
                    edt_title_recipe.setFocusable(false);
                    edt_title_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));


                    edt_description_recipe.setText(new adapter_setting().adapter_number(the_recipe_come.getDescription(),getContext()));
                    img_check_description.setBackground(getResources().getDrawable(R.drawable.complet));
                    edt_description_recipe.setFocusable(false);
                    edt_description_recipe.setBackground(getResources().getDrawable(R.drawable.write_good));

                    img_check_ingredient.setBackground(getResources().getDrawable(R.drawable.complet));
                    img_check_prepare.setBackground(getResources().getDrawable(R.drawable.complet));

                    btn_add_prepare.setVisibility(View.INVISIBLE);
                    btn_add_ingredient.setVisibility(View.INVISIBLE);
                    v_a.setVisibility(View.INVISIBLE);
                    v_b.setVisibility(View.INVISIBLE);
                    v_spinner_categories.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_cuisine.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_time.setBackground(getResources().getDrawable(R.drawable.write_good));
                    v_spinner_person.setBackground(getResources().getDrawable(R.drawable.write_good));

                    rcv_ingredient.setAdapter(new L_i_h_p(getContext(), the_recipe_come.getIngredient(), false));
                    rcv_ingredient.setLayoutManager(new LinearLayoutManager(getContext()));
                    edt_ingredient.setVisibility(View.GONE);
                    rcv_prepare.setVisibility(View.VISIBLE);

                    rcv_prepare.setAdapter(new L_i_h_p(getContext(), the_recipe_come.getHow_to_prepare(), false));
                    rcv_prepare.setLayoutManager(new LinearLayoutManager(getContext()));
                    edt_prepare.setVisibility(View.GONE);
                    rcv_ingredient.setVisibility(View.VISIBLE);



                    spn_time.setSelection(arl_time.indexOf(Integer.parseInt(the_recipe_come.getCooking_time())), false);
                    spn_person.setSelection(arl_person.indexOf(Integer.parseInt(the_recipe_come.getPerson())), false);

                    spn_cuisine.setSelection(arl_cui.indexOf(the_recipe_come.getCuisine()), false);
                    spn_categories.setSelection(arl_cat.indexOf(the_recipe_come.getCategories()), false);

                    back_pressed=1;
                    break;
                default:
            }



            return myLayout;
    }




    ////////////////////////////////////////   Select_Image  /////////////////////////////////////////////////
    public void onSelectImageClick() {
        Intent intent=CropImage.getPickImageChooserIntent(getContext());
        startActivityForResult(intent,111);
    }



    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            if (requestCode == 111 && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

               /////////////
                IMAGE_CROPING=result.getUri();

                ContentResolver cr = getContext().getContentResolver();
                try {
                    Bitmap bitmap = android.provider.MediaStore.Images.Media
                            .getBitmap(cr, IMAGE_CROPING);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
                    img_recipe.setImageBitmap(new adapter_setting().getResizedBitmap(bitmap,864,550));
                    byte[] b = baos.toByteArray();
                    bitmap.recycle();
                    bitmap=null;
                    IMAGE_CROPING=getImageUri(getContext(),BitmapFactory.decodeByteArray(b, 0, b.length));
                    img_recipe.setVisibility(View.VISIBLE);
                    img_check_image.setBackground(getResources().getDrawable(R.drawable.complet));

                } catch (IOException e) {
                    e.printStackTrace();
                }catch (OutOfMemoryError e){
                    Toast.makeText(getContext(), "ذاكرة هاتفك الحية [RAM] لا تتحمل لأن الصورة كبيرة", Toast.LENGTH_SHORT).show();
                }
                ////////////

            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getContext(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(getContext(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setFixAspectRatio(true)
                .setAspectRatio(3,2)
                .start(getContext(), this);
//
     //   startActivityForResult(CropImage.activity(imageUri).start(getContext(),this)), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        //CropImage.activity(imageUri)
        //        .start(getContext(), this);
        //CropImage.activity(imageUri).getIntent(getContext());
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void focus_to_Check(ImageView imageView){
        imageView.setFocusable(true);
        imageView.setFocusableInTouchMode(true);///add this line
        imageView.requestFocus();
        imageView.clearFocus();
        imageView.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.error_check,getActivity().getTheme()));
    }


    //@Override
    //public void onWindowFocusChanged(boolean hasFocus) {
    //    super.onWindowFocusChanged(hasFocus);
    //    if (hasFocus) {
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    //            getWindow().getDecorView().setSystemUiVisibility(
    //                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    //                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    //        }
    //    }
    //}



    public void snack_bar_cancel(){
        // Create the Snackbar
        final Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "", Snackbar.LENGTH_INDEFINITE);
        // Get the Snackbar's layout view
        final Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        snackbar.getView().getRootView().setBackgroundResource(android.R.color.transparent);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.attention_befoer_quite, null);

        // Configure the view
        Button btn_add= snackView.findViewById(R.id.btn_add_o);
        ImageButton btn_cancel= snackView.findViewById(R.id.btn_cancel_o);
        final View cancel_line=snackView.findViewById(R.id.cancel_line);



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final new_recipe my_recipe=new new_recipe();

                my_recipe.setHow_to_prepare(adapter2.get_list());

                my_recipe.setIngredient(adapter.get_list());


                my_recipe.setTitle(edt_title_recipe.getText().toString().trim());
                my_recipe.setDescription(edt_description_recipe.getText().toString().trim());

                my_recipe.setCooking_time(spn_time.getSelectedItem()+"");
                my_recipe.setPerson(spn_person.getSelectedItem()+"");
                my_recipe.setStatus_recipe("not complete");

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd'\n'HH:mm", Locale.US);

                my_recipe.setDate(formatter.format(new Date()));

                my_recipe.setAuteur(FirebaseAuth.getInstance().getCurrentUser().getUid());
                my_recipe.setAuteur_ID(new user_info().get_info_account(GoogleSignIn.getLastSignedInAccount(getContext())).getAccount_ID());

                my_recipe.setID(new Date().getSeconds()+new Date().getMinutes()+new Date().getHours()+new Date().getDay()
                        +new Date().getMonth()+new Date().getYear()+"");
                my_recipe.setLien_puc(null);
                my_recipe.setCategories(spn_categories.getSelectedItem().toString());
                my_recipe.setCuisine(spn_cuisine.getSelectedItem().toString());
                ContentResolver cr = getContext().getContentResolver();
                try {
                    Bitmap bitmap = android.provider.MediaStore.Images.Media
                            .getBitmap(cr, IMAGE_CROPING);
                    bitmap=new adapter_setting().getResizedBitmap(bitmap,864,550);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
                    bitmap.recycle();
                    bitmap=null;
                    byte[] b = baos.toByteArray();
                    my_recipe.setImage(b);
                }
                catch (IOException e){
                    if(the_recipe_come.getImage()!=null)
                        my_recipe.setImage(the_recipe_come.getImage());
                    else
                        my_recipe.setImage(null);
                    e.printStackTrace();
                }
                catch (NullPointerException n){
                    if(the_recipe_come.getImage()!=null)
                        my_recipe.setImage(the_recipe_come.getImage());
                    else
                        my_recipe.setImage(null);
                }
                if (recipe_not_complet_deja_exist)
                    new sql_manager(getContext()).delet_data_recipe(the_recipe_come.getID());
                new sql_manager(getContext()).insert_data_into_recipe(my_recipe);
                back_pressed=1;
                onBackPressed();
                snackbar.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_pressed=1;
                onBackPressed();
                snackbar.dismiss();
            }
        });


        //If the view is not covering the whole snackbar layout, add this line
        layout.setPadding(0,0,0,0);

        // Add the view to the Snackbar's layout
        layout.addView(snackView, 0);


        // Show the Snackbar
        snackbar.show();

        final int[] a = {0};
        final float[] y = new float[1];
        final float[] decalge = new float[1];
        final Chrono chrono = new Chrono();

        snackbar.getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if(a[0] ==0) {
                     y[0] = v.getY();
                     a[0]++;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        chrono.start();
                        dY = y[0]- event.getRawY();
                        decalge[0] = event.getRawY()-y[0];
                        cancel_line.setBackground(getResources().getDrawable(R.drawable.line));
                        break;
                        case MotionEvent.ACTION_MOVE:
                            if (!(event.getRawY()-decalge[0] < y[0])) {
                                ViewPropertyAnimator nn = v.animate();
                                nn.y(event.getRawY() + dY);
                                nn.setDuration(0);
                                nn.start();
                            }else
                               {
                                   ViewPropertyAnimator nn = v.animate();
                                   nn.y(y[0]);
                                   nn.setDuration(0);
                                   nn.start();
                               }
                            break;
                            case MotionEvent.ACTION_UP:
                                cancel_line.setBackground(getResources().getDrawable(R.drawable.line_b_clique));
                                chrono.stop();
                               if (event.getRawY()>y[0]) {
                                   if(chrono.getDureeMs()<1300) {
                                       ViewPropertyAnimator nn = v.animate();
                                       nn.y(y[0] + v.getHeight());
                                       nn.setDuration(150);
                                       nn.start();
                                       Handler handler2 = new Handler();
                                       handler2.postDelayed(new Runnable() {
                                           public void run() {
                                               snackbar.getView().setVisibility(View.INVISIBLE);
                                               snackbar.dismiss();
                                           }
                                       }, 150);
                                   }
                                   else{
                                       ViewPropertyAnimator nn = v.animate();
                                       nn.y(y[0]);
                                       nn.setDuration(0);
                                       nn.start();
                                   }
                               }

                            break;
                            default:
                                return false;
                }
            return true;
        }});
    }


    float  dY;

    //@Override
    //public void onBackPressed(){
    //    if (back_pressed==1)
    //        try {
    //            super.onBackPressed();
    //        }catch (Exception e){}
//
    //    else if (img_recipe.getDrawable()!=null || !edt_title_recipe.getText().toString().equals("") ||
    //            !edt_description_recipe.getText().toString().equals("") ||
    //            adapter.get_list().size()!=0 || adapter2.get_list().size()!=0) {
    //        snack_bar_cancel();
    //    }else
    //        super.onBackPressed();
    //}

    public int onBackPressed(){
        if (back_pressed==1)
            try {
                cancel_activity();
                return 1;
            }catch (Exception e){
                return 0;
            }
        else if (img_recipe.getDrawable()!=null || !edt_title_recipe.getText().toString().equals("") ||
                !edt_description_recipe.getText().toString().equals("") ||
                adapter.get_list().size()!=0 || adapter2.get_list().size()!=0) {
            snack_bar_cancel();
            return 0;
        }else{
            cancel_activity();
            return 1;}
    }


    LottieAnimationView lottie,lottie_succes;
    CustomGauge progress;
    TextView text_download;

    Dialog dialog;

    public void alert(int end_value){

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.alert_updoad);

        lottie        =dialog.findViewById(R.id.lottie_download);
        progress      =dialog.findViewById(R.id.progressbar);
        lottie_succes= dialog.findViewById(R.id.lottie_ok);
        text_download= dialog.findViewById(R.id.text_download);
        progress.setEndValue(end_value);



        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
       try {
           dialog.show();
       }catch (Exception e){
       }

    }



    //@Override
    //protected void onRestart() {
    //   if(data_uploaded)
    //       onBackPressed();
    //    super.onRestart();
    //}

    protected int onRestart(){
        if(data_uploaded)
           return 0;
        return 1;
    };

    public void progress_aniation(int from, int to, final endanimation endanimation){
        ObjectAnimator animation = ObjectAnimator.ofInt(progress, "Value", from,to); // see this max value coming back here, we animate towards that value
        animation.setDuration(1500); // in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                endanimation.oncallback();
            }
        }, 1500);
    }

    interface endanimation {
        void oncallback();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
            return Uri.parse(path);

    }

    public void delet_file(Uri path){
        ContentResolver contentResolver = getContext().getContentResolver();
        contentResolver.delete(path, null, null);
    }


    public int get_bottom_bar_height(){
        Resources resources = Add_new_recipe.this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    public void cancel_activity(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(getActivity().isDestroyed()){
                praincipal_activity praincipal_activity=(praincipal_activity) getActivity();
                praincipal_activity.recreate();
            }else {
                account_activity fragment2 = new account_activity();
                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, fragment2);
                fragmentTransaction.commit();
            }
        }
    }

    public void cancel_activity_r(){
       // account_activity fragment2 = new account_activity();
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.fragmentContainer, fragment2);
        fragmentTransaction.remove(getParentFragment());
    }

}