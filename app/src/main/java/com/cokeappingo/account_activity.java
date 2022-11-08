package com.cokeappingo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.SendNotificationPack.Token;
import com.cokeappingo.class_reglage.Chrono;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.account;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.list_account.list_adapter_acount;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.cokeappingo.user_info.user_info;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.okhttp.internal.http.StatusLine;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_dropdown_item_1line;

public class account_activity extends Fragment {




    ///objet_login_google//////////////////////
    private GoogleSignInClient mgoogleSignInClient;
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN=1;
    ////////////////////////
    RecyclerView list_recipe_account;

    //////////////////////
    pl.pawelkleczkowski.customgauge.CustomGauge progress_bar;
   // ImageView button_setting,button_home,button_save,button_search,button_account;
    LottieAnimationView lottie_empty,lottie_download,no_search_result;


    ImageButton button_add_recipe_oval;

    TextView text_download,text_empty;


    SearchView searchView;
    Spinner spn_status;

    ///////////////////////
    ConstraintLayout bottom_bar;


    list_adapter_acount adapter;
    List<new_recipe> all_recipe;

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;

    boolean on_search=false;


    float  dX;
    ConstraintLayout place_top_button;
    View cancel_line;



  // @Override
  // protected void onCreate(final Bundle savedInstanceState) {
  //     super.onCreate(savedInstanceState);
  //     setContentView(R.layout.activity_account_activity);
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
      View myLayout = inflater.inflate(R.layout.activity_account_activity, container, false);




       // hide_navigate_bottom();

        button_add_recipe_oval=myLayout.findViewById(R.id.btn_add_recipe_oval);







        /////////////////////////
       // constraintLayout1= itemView.findViewById(R.id.top_element_in_activity_account);
       // button_home = findViewById(R.id.button_1);
       // button_setting = findViewById(R.id.button_2);
       // button_save = findViewById(R.id.button_4);
       // button_search = findViewById(R.id.button_5);
       // button_account = findViewById(R.id.button_6);

        lottie_empty = myLayout.findViewById(R.id.lottie_empty);
        lottie_download = myLayout.findViewById(R.id.lottie_download);
        no_search_result = myLayout.findViewById(R.id.lottie_no_serch_result);






        progress_bar=myLayout.findViewById(R.id.progressbar);
        text_download= myLayout.findViewById(R.id.text_download);
        text_empty= myLayout.findViewById(R.id.text_empty);


        searchView= myLayout.findViewById(R.id.search_view);
        spn_status= myLayout.findViewById(R.id.spn_status);

        bottom_bar=myLayout.findViewById(R.id.bottom_bar);

        place_top_button=myLayout.findViewById(R.id.place_top_bar);
        cancel_line=myLayout.findViewById(R.id.cancel_line);
        //////////////////////





      // button_setting.setOnClickListener(new View.OnClickListener() {
      //     @Override
      //     public void onClick(View v) {
      //         startActivity(new Intent(account_activity.this, setting.class));
      //     }
      // });
      // button_home.setOnClickListener(new View.OnClickListener() {
      //     @Override
      //     public void onClick(View v) {
      //         startActivity(new Intent(account_activity.this, MainActivity.class));
      //     }
      // });

      // button_search.setOnClickListener(new View.OnClickListener() {
      //     @Override
      //     public void onClick(View v) {
      //         startActivity(new Intent(account_activity.this,search_activity.class));
      //     }
      // });



        list_recipe_account = myLayout.findViewById(R.id.list_recipe);

        all_recipe = get_Objet_recipe();
        update_adapter();

            /////////////////


        /////////////////////////////////////////
        /////////////////////////////////////////

        searchView.onActionViewExpanded();
        searchView.clearFocus();




        final List<new_recipe> results = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;}

            @Override
            public boolean onQueryTextChange(String newText) {


                searchView.requestFocus();
                on_search=true;
                results.clear();
                if (newText.length()==0){
                    //adapter.update(all_recipe);
                    list_recipe_account.setAdapter(new list_adapter_acount(getContext(), all_recipe,getActivity()));
                    no_search_result.setVisibility(View.INVISIBLE);
                    text_empty.setVisibility(View.INVISIBLE);
                    list_recipe_account.setVisibility(View.VISIBLE);
                    if(all_recipe.size()==0){
                        list_recipe_account.setVisibility(View.INVISIBLE);
                        no_search_result.setVisibility(View.VISIBLE);
                    }
                }else{
                    for (new_recipe M : all_recipe) {
                        if (properCase(M.getTitle()).contains(properCase(newText)))
                            results.add(M);
                    }
                    if (results.size() == 0) {
                        no_search_result.setVisibility(View.VISIBLE);
                        text_empty.setVisibility(View.VISIBLE);

                        lottie_empty.setVisibility(View.INVISIBLE);
                        list_recipe_account.setAdapter(null);
                        //text_empty.setText(new adapter_setting().adapter_number("  لاتوجد اي نتائج بحث ب ' " + newText + " ' ");
                        String first = "  لاتوجد اي نتائج بحث ب ' ";
                        String next = "<font color='#FD5C5C'>"+newText+"</font>";
                        String last=" ' ";
                        text_empty.setText(Html.fromHtml(first + next+last));
                    }else{
                       // adapter.update(results);
                        list_recipe_account.setAdapter(new list_adapter_acount(getContext(), results,getActivity()));
                        no_search_result.setVisibility(View.INVISIBLE);
                        text_empty.setVisibility(View.INVISIBLE);
                        list_recipe_account.setVisibility(View.VISIBLE);
                    }
                }


                return false;
            }
        });




        String[] st=new String[]{"الكل","تم النشر","يتم معالاجتها","غير مكتملة","مرفوضة"};
        @SuppressLint("ResourceType")
        ArrayAdapter<String> adapter_cui =new ArrayAdapter(getContext(),R.layout.spn_item,st);
        // Specify the layout to use when the list of choices appears
        adapter_cui.setDropDownViewResource(simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        spn_status.setAdapter(adapter_cui);



            spn_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    boolean b=true;
                    list_recipe_account.setVisibility(View.VISIBLE);
                        all_recipe = get_Objet_recipe();
                        switch (position) {
                            case 0:
                                adapter = new list_adapter_acount(getContext(), get_Objet_recipe(),getActivity());
                                list_recipe_account.setAdapter(adapter);
                                list_recipe_account.setLayoutManager(new LinearLayoutManager(getContext()));

                                all_recipe=get_Objet_recipe();
                                if(get_Objet_recipe().size()==0)
                                    b=false;
                                break;
                            case 1:
                                ArrayList<new_recipe> selected = new ArrayList<>();
                                for (new_recipe T : all_recipe) {
                                    if (T.getStatus_recipe().equals("ok"))
                                        selected.add(T);
                                }
                                all_recipe = selected;
                                if (all_recipe.size() == 0){
                                    String first="لاتوجد اي وصفات ";
                                    String next = "<font color='#FD5C5C'>تم نشرها</font>";
                                    text_empty.setText(Html.fromHtml(first + next));
                                }
                                adapter = new list_adapter_acount(getContext(), selected,getActivity());
                                list_recipe_account.setAdapter(adapter);
                                list_recipe_account.setLayoutManager(new LinearLayoutManager(getContext()));
                                break;
                            case 2:
                                ArrayList<new_recipe> selected2 = new ArrayList<>();
                                for (new_recipe T : all_recipe) {
                                    if (T.getStatus_recipe().equals("pas encore"))
                                        selected2.add(T);
                                }
                                all_recipe = selected2;
                                adapter = new list_adapter_acount(getContext(), selected2,getActivity());
                                list_recipe_account.setAdapter(adapter);
                                list_recipe_account.setLayoutManager(new LinearLayoutManager(getContext()));
                                if (all_recipe.size() == 0){
                                    String first="لاتوجد اي وصفات ";
                                    String next = "<font color='#FD5C5C'>يتم معالجتها</font>";
                                    text_empty.setText(Html.fromHtml(first + next));
                                }
                                break;
                            case 3:
                                ArrayList<new_recipe> selected3 = new ArrayList<>();
                                for (new_recipe T : all_recipe) {
                                    if (T.getStatus_recipe().equals("not complete"))
                                        selected3.add(T);
                                }
                                all_recipe = selected3;
                                if (all_recipe.size() == 0){
                                    String first="لاتوجد اي وصفات ";
                                    String next = "<font color='#FD5C5C'>غير مكتملة</font>";
                                    text_empty.setText(Html.fromHtml(first + next));
                                }
                                adapter = new list_adapter_acount(getContext(), selected3,getActivity());
                                list_recipe_account.setAdapter(adapter);
                                list_recipe_account.setLayoutManager(new LinearLayoutManager(getContext()));
                                break;
                            case 4:
                                ArrayList<new_recipe> selected4 = new ArrayList<>();
                                for (new_recipe T : all_recipe) {
                                    if (T.getStatus_recipe().equals("refused"))
                                        selected4.add(T);
                                }
                                all_recipe = selected4;
                                if (all_recipe.size() == 0) {
                                    String first="لاتوجد اي وصفات ";
                                    String next = "<font color='#FD5C5C'>مرفوضة</font>";
                                    text_empty.setText(Html.fromHtml(first + next));
                                }
                                adapter = new list_adapter_acount(getContext(), selected4,getActivity());
                                list_recipe_account.setAdapter(adapter);
                                list_recipe_account.setLayoutManager(new LinearLayoutManager(getContext()));
                                break;
                        }
                        if (all_recipe.size() == 0 && b){
                            no_search_result.setVisibility(View.VISIBLE);
                            text_empty.setVisibility(View.VISIBLE);
                            lottie_empty.setVisibility(View.INVISIBLE);
                        }else if(!b){
                            no_search_result.setVisibility(View.INVISIBLE);
                            text_empty.setVisibility(View.VISIBLE);
                            text_empty.setText("حاول(ي) أن تضيف(ي) إحدى وصفاتك المذهلة");
                            lottie_empty.setVisibility(View.VISIBLE);
                        }else{
                            no_search_result.setVisibility(View.INVISIBLE);
                            text_empty.setVisibility(View.INVISIBLE);
                            lottie_empty.setVisibility(View.INVISIBLE);
                        }


                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });





        ///objet_login_google//////////////////////
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(getContext(),gso);




        button_add_recipe_oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                  //  ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                } else {
                start_add_recipe_activity("new");}
            }
        });


        //btn_edit_info_user.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        startActivity(new Intent(account_activity.this,edit_info_user.class));
        //    }
        //});




      list_recipe_account.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
              adapter.dimiss_snackbar_bar(null);
              return false;
          }
      });




      return myLayout;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            update_adapter();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData")
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
    }

    private List<new_recipe> get_Objet_recipe() {
        ArrayList<new_recipe> all_recipec;
        sql_manager db=new sql_manager(getContext());
        all_recipec= db.getAllData_recipe_user();
        return all_recipec;
    }


    //@Override
    //public void onBackPressed() {
    //}
//
    //@Override
    //protected void onStart() {
    //    super.onStart();
    //    new adapter_setting().set_setting_text(this);
    //}

   // @Override
   // protected void onRestart() {
   //     super.onRestart();
   //     Handler handler=new Handler();
   //     handler.postDelayed(new Runnable() {
   //         public void run() {
   //             button_add_recipe.setVisibility(View.INVISIBLE);
   //             button_add_recipe_oval.setVisibility(View.VISIBLE);
   //         }
   //     }, 1500);
   //     try {
   //         update_adapter();
   //     }catch (Exception e){}
//
   // }

    public void sum_data_added(){
        try {
            update_adapter();
        }catch (Exception e){}
    }



   // @Override
   // public void onWindowFocusChanged(boolean hasFocus) {
   //     super.onWindowFocusChanged(hasFocus);
   //     if (hasFocus) {
   //         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
   //             getWindow().getDecorView().setSystemUiVisibility(
   //                     View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
   //                             | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
   //         }
   //     }
   // }
//





    public void update_adapter(){
        all_recipe = get_Objet_recipe();
        if(all_recipe.size()==0){
            lottie_empty.setVisibility(View.VISIBLE);
            text_empty.setVisibility(View.VISIBLE);
            no_search_result.setVisibility(View.INVISIBLE);
        }else{
            lottie_empty.setVisibility(View.INVISIBLE);
            text_empty.setVisibility(View.INVISIBLE);
            no_search_result.setVisibility(View.INVISIBLE);
            list_recipe_account.setVisibility(View.VISIBLE);
            adapter = new list_adapter_acount(getContext(), all_recipe,getActivity());
            list_recipe_account.setAdapter(adapter);
            list_recipe_account.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }


    interface endanimation {
        void oncallback();
    }

    String properCase (String inputVal) {
        if (inputVal.length() == 0) return "";
        return inputVal.substring(0,1).toLowerCase()
                + inputVal.substring(1).toLowerCase();
    }



    public void start_add_recipe_activity(String id){
        Add_new_recipe fragment2 = new Add_new_recipe();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle args = new Bundle();
        args.putString("recipe",id);
        fragment2.setArguments(args);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment2);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_MEDIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start_add_recipe_activity("new");
            } else {
                Toast.makeText(getContext(), "لايمكنك اضافة وصفة بدون هده الخاصية", Toast.LENGTH_SHORT).show();
            }
        }
    }

}