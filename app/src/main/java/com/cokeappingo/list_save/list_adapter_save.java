package com.cokeappingo.list_save;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.Add_new_recipe;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.R;
import com.cokeappingo.account_activity;
import com.cokeappingo.class_reglage.Chrono;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.account;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.class_utile.save_recipe;
import com.cokeappingo.praincipal_activity;
import com.cokeappingo.show_this_recipe;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

public class list_adapter_save extends RecyclerView.Adapter<list_adapter_save.ViewHolder>{


    List<save_recipe> all_recipe;
    Context context;
    Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        //////////////////////
        ConstraintLayout constraintLayout2;
        ImageView image_recipe1,image_recipe2;
        TextView text_title1,text_title2;
        CardView cardView1,cardView2;
        Button btn_first_items,btn_second_items;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);



            /////////////////
            constraintLayout2= itemView.findViewById(R.id.list_element_in_activity_account);
            image_recipe1= itemView.findViewById(R.id.recipe_photo);
            image_recipe2= itemView.findViewById(R.id.recipe_photo2);
            text_title1= itemView.findViewById(R.id.recipe_title);
            text_title2= itemView.findViewById(R.id.recipe_title2);
            cardView1= itemView.findViewById(R.id.cardview1);
            cardView2= itemView.findViewById(R.id.cardview2);
            btn_first_items=itemView.findViewById(R.id.button_first_items);
            btn_second_items=itemView.findViewById(R.id.button_first_items2);


            ////////////////////
        }
    }

    public list_adapter_save(Context context, List<save_recipe> list_recipe, Activity activity){
        Collections.reverse(list_recipe);
        all_recipe=list_recipe;
        this.context=context;
        this.activity=activity;
    }

    @NonNull
    @Override
    public list_adapter_save.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.items_layout_save,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final list_adapter_save.ViewHolder holder, int position) {
        save_recipe recipe1 = null;
        save_recipe recipe2 = null;


        try {

           if ((all_recipe.size()/2)==position && all_recipe.size()%2!=0) {
                holder.cardView2.setVisibility(View.GONE);
           }



            recipe1= all_recipe.get(position*2);

            byte[] tof_saved1=recipe1.getImage();
            Bitmap decodedByte1 = BitmapFactory.decodeByteArray(tof_saved1, 0, tof_saved1.length);
            holder.image_recipe1.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte1,600,400));
            holder.text_title1.setText(new adapter_setting().adapter_number(recipe1.getTitle(),context));

            save_recipe finalRecipe = recipe1;
            holder.btn_first_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start_ok_recipe_activity(finalRecipe);
                }
            });

           recipe2= all_recipe.get((position*2)+1);

            byte[] tof_saved2=recipe2.getImage();
            Bitmap decodedByte2 = BitmapFactory.decodeByteArray(tof_saved2, 0, tof_saved2.length);
            holder.image_recipe2.setImageBitmap(new adapter_setting().getResizedBitmap(decodedByte2,600,400));
            holder.text_title2.setText(new adapter_setting().adapter_number(recipe2.getTitle(),context));


            save_recipe finalRecipe2 = recipe2;
            holder.btn_second_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start_ok_recipe_activity(finalRecipe2);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }






    }

    @Override
    public int getItemCount() {
        if (all_recipe.size()%2!=0)
            return (all_recipe.size()/2+1);
        else
            return (all_recipe.size()/2);
    }



    public void update(List<save_recipe> m) {
        all_recipe.clear();
        notifyDataSetChanged();
        Collections.reverse(m);
        all_recipe.addAll(m);
        notifyDataSetChanged();
    }



    public void start_ok_recipe_activity(save_recipe recipe){
        show_this_recipe fragment2=new show_this_recipe();
        Bundle args = new Bundle();
        args.putString("recipe",recipe.getId_push());
        fragment2.setArguments(args);
        // AppCompatActivity activity = (AppCompatActivity) view.getContext().getApplicationContext();
        AppCompatActivity activity = (AppCompatActivity)  context;
        /// activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment2).addToBackStack(null).commit();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_show_save,fragment2,"holo");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}




