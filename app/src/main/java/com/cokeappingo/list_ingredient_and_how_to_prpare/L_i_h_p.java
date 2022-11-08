package com.cokeappingo.list_ingredient_and_how_to_prpare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.DialogTitle;
import androidx.recyclerview.widget.RecyclerView;

import com.cokeappingo.R;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.list_setting.objet_setting;
import com.cokeappingo.sql_lite_manager.sql_manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    public class L_i_h_p extends RecyclerView.Adapter<L_i_h_p.ViewHolder> {


        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView ingre;
            Button del;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                ingre = itemView.findViewById(R.id.ingredient_text);
                del = itemView.findViewById(R.id.btn_delet_ingredient);

            }
        }

        ArrayList<String> all_string;
        Context context;
        boolean updated;

        public L_i_h_p(Context context,ArrayList<String> list_setting,boolean updated){
            all_string=list_setting;
            this.context=context;
            this.updated=updated;
        }

        @NonNull
        @Override
        public L_i_h_p.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(context).inflate(R.layout.items_ingredients,parent,false);
            return new L_i_h_p.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final String m=all_string.get(position);

            holder.ingre.setText(new adapter_setting().adapter_number(m,context));
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    all_string.remove(position);
                    notifyDataSetChanged();
                }
            });

            if (!updated)
                holder.del.setVisibility(View.INVISIBLE);

        }



        @Override
        public int getItemCount() {
            return all_string.size();
        }


        public void update(String results) {
            all_string.add(results);
            notifyItemInserted(getItemCount()-1);
        }

        public ArrayList<String> get_list() {
            return all_string;
        }
    }
