package com.cokeappingo.list_account;


import android.app.Activity;
import android.app.AlertDialog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.Add_new_recipe;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.MainActivity;
import com.cokeappingo.R;
import com.cokeappingo.account_activity;
import com.cokeappingo.class_reglage.Chrono;

import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.account;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.praincipal_activity;
import com.cokeappingo.show_this_recipe;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.cokeappingo.user_info.user_info;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.CDATASection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.http.Url;

public class list_adapter_acount extends RecyclerView.Adapter<list_adapter_acount.ViewHolder>{


    List<new_recipe> all_recipe;
    Context context;
    Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        //////////////////////
        ConstraintLayout constraintLayout2;
        ImageView image_recipe1,image_recipe2;
        TextView text_title1,text_title2,text_no_image1,text_no_image2,text_date1,text_date2;
        CardView cardView1,cardView2;
        LottieAnimationView lottie_photo_r1,lottie_photo_r2;
        Button btn_first_items,btn_second_items,text_status1,text_status2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);



            /////////////////
            constraintLayout2= itemView.findViewById(R.id.list_element_in_activity_account);
            image_recipe1= itemView.findViewById(R.id.recipe_photo);
            image_recipe2= itemView.findViewById(R.id.recipe_photo2);
            text_title1= itemView.findViewById(R.id.recipe_title);
            text_title2= itemView.findViewById(R.id.recipe_title2);
            text_no_image1= itemView.findViewById(R.id.image_not_found);
            text_no_image2= itemView.findViewById(R.id.image_not_found2);
            text_status1= itemView.findViewById(R.id.text_status);
            text_status2= itemView.findViewById(R.id.text_status2);
            text_date1= itemView.findViewById(R.id.txt_date);
            text_date2= itemView.findViewById(R.id.txt_date2);
            cardView1= itemView.findViewById(R.id.cardview1);
            cardView2= itemView.findViewById(R.id.cardview2);
            lottie_photo_r1=itemView.findViewById(R.id.lottie_load_r1);
            lottie_photo_r2=itemView.findViewById(R.id.lottie_load_r2);
            btn_first_items=itemView.findViewById(R.id.button_first_items);
            btn_second_items=itemView.findViewById(R.id.button_second_items);


            ////////////////////
        }
    }

    public list_adapter_acount(Context context, List<new_recipe> list_recipe,Activity activity){
        Collections.reverse(list_recipe);
        all_recipe=list_recipe;
        this.context=context;
        this.activity=activity;
    }

    @NonNull
    @Override
    public list_adapter_acount.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.items_layout_account,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final list_adapter_acount.ViewHolder holder, int position) {
        new_recipe recipe1 = null;
        new_recipe recipe2 = null;


        try {

           if ((all_recipe.size()/2)==position && all_recipe.size()%2!=0) {
                holder.cardView2.setVisibility(View.GONE);
           }



            recipe1= all_recipe.get(position*2);

            switch (recipe1.getStatus_recipe()){
               case "ok":
                   holder.text_title1.setText(new adapter_setting().adapter_number(recipe1.getTitle(),context));
                   holder.text_date1.setText(new adapter_setting().adapter_number(recipe1.getDate(),context));

                   try {
                   byte[] tof_saved1=recipe1.getImage();
                   Bitmap decodedByte1 = BitmapFactory.decodeByteArray(tof_saved1, 0, tof_saved1.length);
                   holder.lottie_photo_r1.setVisibility(View.GONE);
                   holder.image_recipe1.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte1,600,400));
                   }catch (Exception e){
                       remplire_Image_items(context,recipe1,holder.image_recipe1,holder.lottie_photo_r1);
                   }catch (OutOfMemoryError error){
                       Log.e("Error","OutOfMemoryError");
                   }

                   holder.text_status1.setText(new adapter_setting().adapter_number("تم النشر",context));
                   holder.text_status1.setBackground(context.getResources().getDrawable(R.drawable.back_status_ok));
                   holder.text_status1.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.check),null,null,null);
                   break;
               case "pas encore":
                   holder.text_title1.setText(new adapter_setting().adapter_number(recipe1.getTitle(),context));
                   holder.text_date1.setText(new adapter_setting().adapter_number(recipe1.getDate(),context));

                   try{
                       byte[] tof_saved2=recipe1.getImage();
                       Bitmap decodedByte2 = BitmapFactory.decodeByteArray(tof_saved2, 0, tof_saved2.length);
                       holder.image_recipe1.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte2,600,400));
                       holder.lottie_photo_r1.setVisibility(View.GONE);
                   }catch (Exception e){
                       remplire_Image_items(context,recipe1,holder.image_recipe1,holder.lottie_photo_r1);
                   }catch (OutOfMemoryError error){
                       Log.e("Error","OutOfMemoryError");
                   }


                   holder.text_status1.setText(new adapter_setting().adapter_number("يتم معالاجتها",context));
                   holder.text_status1.setBackground(context.getResources().getDrawable(R.drawable.back_status_en_cour));
                   holder.text_status1.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.update),null,null,null);
                   break;
               case "not complete":
                   holder.lottie_photo_r1.setVisibility(View.GONE);
                   holder.text_date1.setText(new adapter_setting().adapter_number(recipe1.getDate(),context));
                   if(!recipe1.getTitle().equals(""))
                       holder.text_title1.setText(new adapter_setting().adapter_number(recipe1.getTitle(),context));
                   else
                       holder.text_title1.setText(new adapter_setting().adapter_number("لا يوجد عنوان",context));

                   try {
                       byte[] tof_saved=recipe1.getImage();
                       Bitmap decodedByte = BitmapFactory.decodeByteArray(tof_saved, 0, tof_saved.length);
                       holder.image_recipe1.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte,600,400));
                   }catch (Exception e){
                       holder.image_recipe1.setVisibility(View.INVISIBLE);
                       holder.text_no_image1.setVisibility(View.VISIBLE);
                   }catch (OutOfMemoryError error){
                       Log.e("Error","OutOfMemoryError");
                   }

                   holder.text_status1.setText(new adapter_setting().adapter_number("غير مكتملة",context));
                   holder.text_status1.setBackground(context.getResources().getDrawable(R.drawable.back_status_not_com));
                   holder.text_status1.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.edit),null,null,null);
                   break;
               case "refused":
                   holder.text_title1.setText(new adapter_setting().adapter_number(recipe1.getTitle(),context));
                   holder.text_date1.setText(new adapter_setting().adapter_number(recipe1.getDate(),context));


                   try {
                   byte[] tof_saved4=recipe1.getImage();
                   Bitmap decodedByte4 = BitmapFactory.decodeByteArray(tof_saved4, 0, tof_saved4.length);
                   holder.lottie_photo_r1.setVisibility(View.GONE);
                   holder.image_recipe1.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte4,600,400));
                   }catch (Exception e){
                         remplire_Image_items(context,recipe1,holder.image_recipe1,holder.lottie_photo_r1);
                   }catch (OutOfMemoryError error){
                       Log.e("Error","OutOfMemoryError");
                   }

                   holder.text_status1.setText(new adapter_setting().adapter_number("مرفوضة",context));
                   holder.text_status1.setBackground(context.getResources().getDrawable(R.drawable.back_status_ref));
                   holder.text_status1.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.cancel),null,null,null);
                   break;
                default:
           }



           recipe2= all_recipe.get((position*2)+1);

          switch (recipe2.getStatus_recipe()){
                case "ok":
                    holder.text_title2.setText(new adapter_setting().adapter_number(recipe2.getTitle(),context));
                    holder.text_date2.setText(new adapter_setting().adapter_number(recipe2.getDate(),context));
                    try {
                    byte[] tof_saved11=recipe2.getImage();
                    Bitmap decodedByte11 = BitmapFactory.decodeByteArray(tof_saved11, 0, tof_saved11.length);
                    holder.image_recipe2.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte11,600,400));
                    holder.lottie_photo_r2.setVisibility(View.GONE);
                    }catch (Exception e){
                        remplire_Image_items(context,recipe2,holder.image_recipe2,holder.lottie_photo_r2);
                    }catch (OutOfMemoryError error){
                        Log.e("Error","OutOfMemoryError");
                    }

                    holder.text_status2.setText(new adapter_setting().adapter_number("تم النشر",context));
                    holder.text_status2.setBackground(context.getResources().getDrawable(R.drawable.back_status_ok));
                    holder.text_status2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.check),null,null,null);

                    break;
                case "pas encore":
                    holder.text_title2.setText(new adapter_setting().adapter_number(recipe2.getTitle(),context));
                    holder.text_date2.setText(new adapter_setting().adapter_number(recipe2.getDate(),context));
                    try {
                    byte[] tof_saved21=recipe2.getImage();
                    Bitmap decodedByte21 = BitmapFactory.decodeByteArray(tof_saved21, 0, tof_saved21.length);
                    holder.lottie_photo_r2.setVisibility(View.GONE);
                    holder.image_recipe2.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte21,600,400));
                    }catch (Exception e){
                        remplire_Image_items(context,recipe2,holder.image_recipe2,holder.lottie_photo_r2);
                    }catch (OutOfMemoryError error){
                        Log.e("Error","OutOfMemoryError");
                    }


                    holder.text_status2.setText(new adapter_setting().adapter_number("يتم معالاجتها",context));
                    holder.text_status2.setBackground(context.getResources().getDrawable(R.drawable.back_status_en_cour));
                    holder.text_status2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.update),null,null,null);

                    break;
                case "not complete":
                    holder.lottie_photo_r2.setVisibility(View.GONE);
                    holder.text_date2.setText(new adapter_setting().adapter_number(recipe2.getDate(),context));
                    if(!recipe2.getTitle().equals(""))
                        holder.text_title2.setText(new adapter_setting().adapter_number(recipe2.getTitle(),context));
                    else
                        holder.text_title2.setText(new adapter_setting().adapter_number("لا يوجد عنوان",context));

                    try {
                        byte[] tof_saved5=recipe2.getImage();
                        Bitmap decodedByte5 = BitmapFactory.decodeByteArray(tof_saved5, 0, tof_saved5.length);
                        holder.image_recipe2.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte5,600,400));
                    }catch (Exception e){
                        holder.image_recipe2.setVisibility(View.INVISIBLE);
                        holder.text_no_image2.setVisibility(View.VISIBLE);
                    }catch (OutOfMemoryError error){
                        Log.e("Error","OutOfMemoryError");
                    }

                    holder.text_status2.setText(new adapter_setting().adapter_number("غير مكتملة",context));
                    holder.text_status2.setBackground(context.getResources().getDrawable(R.drawable.back_status_not_com));
                    holder.text_status2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.edit),null,null,null);

                    break;
                case "refused":
                    holder.text_title2.setText(new adapter_setting().adapter_number(recipe2.getTitle(),context));
                    holder.text_date2.setText(new adapter_setting().adapter_number(recipe2.getDate(),context));

                    try {
                    byte[] tof_saved41=recipe2.getImage();
                    Bitmap decodedByte41 = BitmapFactory.decodeByteArray(tof_saved41, 0, tof_saved41.length);
                    holder.lottie_photo_r2.setVisibility(View.GONE);
                    holder.image_recipe2.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte41,600,400));
                    }catch (Exception e){
                        remplire_Image_items(context,recipe2,holder.image_recipe2,holder.lottie_photo_r2);
                    }catch (OutOfMemoryError error){
                        Log.e("Error","OutOfMemoryError");
                    }

                    holder.text_status2.setText(new adapter_setting().adapter_number("مرفوضة",context));
                    holder.text_status2.setBackground(context.getResources().getDrawable(R.drawable.back_status_ref));
                    holder.text_status2.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.cancel),null,null,null);

                    break;
              default:
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        final new_recipe finalRecipe1 = recipe1;
        holder.btn_first_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               start_add_recipe_activity(finalRecipe1,v);
               dimiss_snackbar_bar(v);
            }
        });
        final int[] f = {0};
        new_recipe finalRecipe2 = recipe1;
        holder.btn_first_items.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               snack_bar_reglage(finalRecipe2,v,position);
                v.setBackground(context.getResources().getDrawable(R.drawable.bg_items_account_long));
                return true;
            }
        });

        final new_recipe finalRecipe = recipe2;
        holder.btn_second_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_add_recipe_activity(finalRecipe,v);
                dimiss_snackbar_bar(v);
            }
        });

        new_recipe finalRecipe3 = recipe2;
        holder.btn_second_items.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                snack_bar_reglage(finalRecipe3,v,position);
                v.setBackground(context.getResources().getDrawable(R.drawable.bg_items_account_long));
                return true;
            }
        });




    }

    @Override
    public int getItemCount() {
        if (all_recipe.size()%2!=0)
            return (all_recipe.size()/2+1);
        else
            return (all_recipe.size()/2);
    }

    public void remplire_Image_items(final Context context, final new_recipe recipe,final ImageView imageView, final LottieAnimationView lotie_recipe){
        Glide.with(context)
                .asBitmap()
                .load(recipe.getLien_puc())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        lotie_recipe.setVisibility(View.GONE);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 30, baos); // bm is the bitmap object
                        byte[] b = baos.toByteArray();
                        imageView.setImageBitmap(new adapter_setting().getResizedBitmap(resource,600,400));
                        recipe.setImage(b);
                        new sql_manager(context).Update_data_recipe(recipe);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void update(List<new_recipe> m) {
        all_recipe.clear();
        notifyDataSetChanged();
        Collections.reverse(m);
        all_recipe.addAll(m);
        notifyDataSetChanged();
    }

    public void start_add_recipe_activity(new_recipe recipe,View view){
        if(recipe.getStatus_recipe().equals("ok")) {
            start_ok_recipe_activity(recipe);
            return;
        }
        Add_new_recipe fragment2=new Add_new_recipe();
        Bundle args = new Bundle();
        args.putString("recipe",recipe.getID());
        fragment2.setArguments(args);
       // AppCompatActivity activity = (AppCompatActivity) view.getContext().getApplicationContext();
        AppCompatActivity activity = (AppCompatActivity)  context;
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment2).addToBackStack(null).commit();
    }


    Snackbar snackbar;
    private ProgressDialog progressDialog;

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }


    public void snack_bar_reglage(new_recipe recipe_edited,View vt,int position){
        // Create the Snackbar
         snackbar= Snackbar.make(activity.getWindow().getDecorView().getRootView(), "", Snackbar.LENGTH_INDEFINITE);
        // Get the Snackbar's layout view
        final Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        snackbar.getView().getRootView().setBackgroundResource(android.R.color.transparent);


        snackbar.addCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                vt.setBackground(context.getResources().getDrawable(R.drawable.clique_items_layout_acount));
            }

            @Override
            public void onShown(Snackbar snackbar) {

            }
        });

        // Inflate our custom view
        View snackView = activity.getLayoutInflater().inflate(R.layout.on_long_click_recipe_user, null);

        // Configure the view
        ImageButton btn_edit= snackView.findViewById(R.id.btn_edit_recipe_oval);
        ImageButton btn_cancel= snackView.findViewById(R.id.btn_delet_recipe_oval);
        final View cancel_line=snackView.findViewById(R.id.cancel_line);

        switch (recipe_edited.getStatus_recipe()){
            case "ok": case "refused":
                btn_edit.setImageResource(R.drawable.eye);
                break;

        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipe_edited.getStatus_recipe().equals("ok"))
                    start_ok_recipe_activity(recipe_edited);
                else
                    start_add_recipe_activity(recipe_edited,v);

                snackbar.dismiss();
            }
        });



        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("انتباه !");
                builder.setMessage("سوف يتم مسح هذه الوصفة بشكل نهائي");
                builder.setPositiveButton("نعم اريد مسحها", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        vt.setBackground(context.getResources().getDrawable(R.drawable.bg_items_account_delet));
                        final Runnable r = new Runnable()
                        {
                            public void run()
                            {
                                switch (recipe_edited.getStatus_recipe()){
                                    case "ok": case "refused": case "pas encore":
                                        new data_user_onligne().delet_recipe_from_firebase(recipe_edited,new sql_manager(context).get__account().getAccount_ID(),recipe_edited.getLien_puc()
                                                , recipe_edited.getStatus_recipe(), recipe_edited.getId_push(), new data_user_onligne.add_succeful() {
                                                    @Override
                                                    public void add_complet(boolean etat) {
                                                        account user=new sql_manager(context).get__account();
                                                        final int number_recipe =(user.getNumber_recipe()-1);
                                                        new data_user_onligne().increment_number_recipe(number_recipe, user, new data_user_onligne.add_succeful() {
                                                            @Override
                                                            public void add_complet(boolean etat) {
                                                                activity.runOnUiThread(new Runnable() {
                                                                    public void run() {
                                                                        new sql_manager(context).Update_data_account(user.getAccount_ID(),user.getName(),user.getBio()
                                                                                ,user.getPhoto_saved(),(number_recipe),user.getImage());
                                                                        new sql_manager(context).delet_data_recipe(recipe_edited.getID());
                                                                        progressDialog.dismiss();
                                                                        dialog.dismiss();
                                                                        snackbar.dismiss();
                                                                         praincipal_activity ac=(praincipal_activity) context;
                                                                         ac.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new account_activity(),"activity_account").commit();

                                                                    }
                                                                });
                                                            }
                                                        });


                                                    }
                                                });
                                        break;
                                    case "not complete":
                                        new sql_manager(context).delet_data_recipe(recipe_edited.getID());
                                        activity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                progressDialog.dismiss();
                                                dialog.dismiss();
                                                snackbar.dismiss();
                                               // all_recipe=get_Objet_recipe();
                                                praincipal_activity ac=(praincipal_activity) context;
                                                ac.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new account_activity(),"activity_account").commit();
                                            }
                                        });
                                        break;
                                }
                            }
                        };

                        progressDialog =new ProgressDialog(activity);

                        progressDialog.setTitle("يتم مسح الوصفة المرجو الانتظار");
                        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                            @Override
                            public void onShow(DialogInterface dialog) {
                                progressDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL

                            }
                        });

                        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        WindowManager.LayoutParams wmlp = progressDialog.getWindow().getAttributes();

                        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;

                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        performOnBackgroundThread(r);


                    }
                });
                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        snackbar.dismiss();
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
                        cancel_line.setBackground(activity.getResources().getDrawable(R.drawable.line));
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
                        cancel_line.setBackground(activity.getResources().getDrawable(R.drawable.line_b_clique));
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

    public void dimiss_snackbar_bar(View v){
        if(v!=null)
            v.setBackground(context.getResources().getDrawable(R.drawable.clique_items_layout_acount));
        if (snackbar!=null && snackbar.isShown())
            snackbar.dismiss();
    }

    public void start_ok_recipe_activity(new_recipe recipe){
        show_this_recipe fragment2=new show_this_recipe();
        Bundle args = new Bundle();
        args.putString("recipe",recipe.getId_push());
        fragment2.setArguments(args);
        // AppCompatActivity activity = (AppCompatActivity) view.getContext().getApplicationContext();
        AppCompatActivity activity = (AppCompatActivity)  context;
        /// activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment2).addToBackStack(null).commit();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_show_account,fragment2,"holo");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}




