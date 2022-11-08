package com.cokeappingo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.class_utile.save_recipe;
import com.cokeappingo.list_account.list_adapter_acount;
import com.cokeappingo.list_save.list_adapter_save;
import com.cokeappingo.sql_lite_manager.sql_manager;

import java.util.ArrayList;
import java.util.List;

public class Save_activity extends Fragment {


    LottieAnimationView lottie_empty,no_search_result;
    SearchView searchView;
    TextView text_empty;

    RecyclerView list_recipe_account;

    ArrayList<save_recipe> all_recipe;

    sql_manager sql_manager;

    boolean on_search=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myLayout = inflater.inflate(R.layout.activity_save_activity, container, false);

        sql_manager=new sql_manager(getContext());

        lottie_empty = myLayout.findViewById(R.id.lottie_empty);
        no_search_result = myLayout.findViewById(R.id.lottie_no_serch_result);
        searchView= myLayout.findViewById(R.id.search_view);
        list_recipe_account = myLayout.findViewById(R.id.list_recipe);
        text_empty= myLayout.findViewById(R.id.text_empty);

        searchView.onActionViewExpanded();
        searchView.clearFocus();

        all_recipe=sql_manager.getAllData__SAVE();


        charger_date();

        final List<save_recipe> results = new ArrayList<>();
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
                    list_recipe_account.setAdapter(new list_adapter_save(getContext(), all_recipe,getActivity()));
                    no_search_result.setVisibility(View.INVISIBLE);
                    text_empty.setVisibility(View.INVISIBLE);
                    list_recipe_account.setVisibility(View.VISIBLE);
                    if(all_recipe.size()==0){
                        list_recipe_account.setVisibility(View.INVISIBLE);
                        no_search_result.setVisibility(View.VISIBLE);
                    }
                }else{
                    for (save_recipe M : all_recipe) {
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
                        list_recipe_account.setAdapter(new list_adapter_save(getContext(), results,getActivity()));
                        no_search_result.setVisibility(View.INVISIBLE);
                        text_empty.setVisibility(View.INVISIBLE);
                        list_recipe_account.setVisibility(View.VISIBLE);
                    }
                }


                return false;
            }
        });


        return myLayout;
    }



    String properCase (String inputVal) {
        if (inputVal.length() == 0) return "";
        return inputVal.substring(0,1).toLowerCase()
                + inputVal.substring(1).toLowerCase();
    }

    public void charger_date(){
        if(all_recipe.size()==0){
            lottie_empty.setVisibility(View.VISIBLE);
            text_empty.setVisibility(View.VISIBLE);
        }else {
            list_recipe_account.setVisibility(View.VISIBLE);
            list_recipe_account.setAdapter(new list_adapter_save(getContext(),sql_manager.getAllData__SAVE(),getActivity()));
            list_recipe_account.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }


}