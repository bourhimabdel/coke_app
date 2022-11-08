package com.cokeappingo.list_ingredient_and_how_to_prpare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cokeappingo.R;
import com.cokeappingo.class_reglage.adapter_setting;

import java.util.ArrayList;

public class L_i_h_p_in_show_vertical extends RecyclerView.Adapter<L_i_h_p_in_show_vertical.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView ingre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ingre = itemView.findViewById(R.id.ingredient_text);

        }
    }

    ArrayList<String> all_string;
    Context context;
    boolean updated;

    public L_i_h_p_in_show_vertical(Context context, ArrayList<String> list_setting){
        all_string=list_setting;
        this.context=context;
        this.updated=updated;
    }

    @NonNull
    @Override
    public L_i_h_p_in_show_vertical.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.items_ingredients_show_vertical,parent,false);
        return new L_i_h_p_in_show_vertical.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String m=all_string.get(position);

        holder.ingre.setText(new adapter_setting().adapter_number(m,context));


    }



    @Override
    public int getItemCount() {
        return all_string.size();
    }



}
