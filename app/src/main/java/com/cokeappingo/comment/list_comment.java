package com.cokeappingo.comment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.R;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.account;
import com.cokeappingo.class_utile.comment;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.cokeappingo.user_info.user_info;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.joooonho.SelectableRoundedImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class list_comment extends RecyclerView.Adapter<list_comment.ViewHolder> {



    data_user_onligne db;
    ArrayList<comment> all_comment;
    Context context;
    String id_push;


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,date,comment;
        ImageButton start1,start2,start3,start4,start5;
        SelectableRoundedImageView image_profile;
        View line;
        Button delete_comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.text_name);
            date=itemView.findViewById(R.id.text_date);
            comment=itemView.findViewById(R.id.text_comment);
            start1=itemView.findViewById(R.id.start5);
            start2=itemView.findViewById(R.id.start4);
            start3=itemView.findViewById(R.id.start3);
            start4=itemView.findViewById(R.id.start2);
            start5=itemView.findViewById(R.id.start1);
            image_profile=itemView.findViewById(R.id.image_profil);
            line=itemView.findViewById(R.id.line);
            delete_comment=itemView.findViewById(R.id.btn_delet_comment);
        }
    }



    public list_comment(Context context, ArrayList<comment> list_comment,String id_push){
        all_comment=list_comment;
        this.context=context;
        db =new data_user_onligne();
        this.id_push=id_push;
    }

    @NonNull
    @Override
    public list_comment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.items_commentaire,parent,false);
        return new list_comment.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull list_comment.ViewHolder holder, int position) {

        final comment comment=all_comment.get(position);

        int number_start=comment.getValue();

        switch (number_start){
            case 1:
                holder.start1.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start2.setBackground(context.getResources().getDrawable(R.drawable.star_border));
                holder.start3.setBackground(context.getResources().getDrawable(R.drawable.star_border));
                holder.start4.setBackground(context.getResources().getDrawable(R.drawable.star_border));
                holder.start5.setBackground(context.getResources().getDrawable(R.drawable.star_border));

                break;
            case 2:
                holder.start1.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start2.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start3.setBackground(context.getResources().getDrawable(R.drawable.star_border));
                holder.start4.setBackground(context.getResources().getDrawable(R.drawable.star_border));
                holder.start5.setBackground(context.getResources().getDrawable(R.drawable.star_border));
                break;
            case 3:
                holder.start1.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start2.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start3.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start4.setBackground(context.getResources().getDrawable(R.drawable.star_border));
                holder.start5.setBackground(context.getResources().getDrawable(R.drawable.star_border));
                break;
            case 4:
                holder.start1.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start2.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start3.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start4.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start5.setBackground(context.getResources().getDrawable(R.drawable.star_border));
                break;
            case 5:
                holder.start1.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start2.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start3.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start4.setBackground(context.getResources().getDrawable(R.drawable.star));
                holder.start5.setBackground(context.getResources().getDrawable(R.drawable.star));
                break;
        }

        holder.comment.setText(comment.getComment());


        holder.date.setText(comment.getDate());

        db.if_user_exist(comment.getId_visitor(), new data_user_onligne.user_exist() {
            @Override
            public void exist(boolean etat, account r) {
                holder.name.setText(r.getName());
                remplire_Image_items(context,r.getImage(),holder.image_profile);
            }
        });

        if (position==0)
            holder.line.setVisibility(View.INVISIBLE);

        if (comment.getComment().equals(""))
            holder.comment.setVisibility(View.INVISIBLE);

        if(comment.getId_visitor().equals(new user_info().get_info_account(GoogleSignIn.getLastSignedInAccount(context)).getAccount_ID()))
            holder.delete_comment.setVisibility(View.VISIBLE);

        holder.delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete_comment(id_push, comment, new data_user_onligne.add_succeful() {
                    @Override
                    public void add_complet(boolean etat) {

                    }
                });

                int a=all_comment.indexOf(comment);
                all_comment.remove(comment);
                notifyItemRemoved(a);
            }
        });


    }

    @Override
    public int getItemCount(){
        return all_comment.size();
    }

    public void remplire_Image_items(final Context context,String lien, final SelectableRoundedImageView imageView){
        Glide.with(context)
                .asBitmap()
                .load(lien)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                       imageView.setImageBitmap(resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

}
