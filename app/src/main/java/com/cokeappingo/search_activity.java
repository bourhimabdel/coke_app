package com.cokeappingo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.class_utile.new_recipe_search;
import com.cokeappingo.list_activity_search.RecyclerViewAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;
import java.util.List;

public class search_activity extends Fragment {



    // A banner ad is placed in every 8th position in the RecyclerView.
    public static final int ITEMS_PER_AD = 6;
    private static final String AD_UNIT_ID = "ca-app-pub-8591526182350923/7494502559";



    // List of banner ads and Recipe that populate the RecyclerView.
    private List<Object> RecyclerViewItems = new ArrayList<>();
    RecyclerViewAdapter adapter;
    RecyclerView list_search;

    //


    // items for organise the recipe
    private int last_add_position;
    Button btn_more_recipe;
    NestedScrollView nestedScrollView;
    ArrayList<Object> new_list_load=new ArrayList<>();
    int[] first_or_second=new int[1];
    data_user_onligne get_data_on_line=new data_user_onligne();
    String current_categ="";
    String current_cuisin="";
    int current_pack;
    int last_size;




    // Items smart search
    TextView text_empty;
    ImageButton smart_search_button;
    public static final int FRAGMENT_CODE =123;


    // Items search by title
    int poi=0;
    SearchView searchView;
    Button btn_search;
    //ConstraintLayout view_download_title;
    String Query_search="";
  //  HashMap<String,String> all_title_and_keys=new HashMap<>();
 //   final ArrayList<String> all_title=new ArrayList<>();
  //  final HashMap<String,ArrayList<String>> diveseur=new HashMap<>();
    ArrayList<new_recipe_search> keys_available_in_this_search= new ArrayList<>();
    int Last_download=0;

    LottieAnimationView search,no_search_result;


    int pack_Number=0;
    int PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES;

    ConstraintLayout items_search_attend;

    String TYPE_OF_SEARCH="";

    data_user_onligne db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myLayout = inflater.inflate(R.layout.activity_search_activity, container, false);

        db=new data_user_onligne();
       // view_download_title = myLayout.findViewById(R.id.view_download);

       // all_title_and_keys = new sql_manager(getContext()).get_TABLE_ALL_TITLE();


      // for (Map.Entry mapentry : all_title_and_keys.entrySet()) {
      //     all_title.add(mapentry.getValue().toString());
      // }

        search= myLayout.findViewById(R.id.lottie_search);
        no_search_result= myLayout.findViewById(R.id.lottie_no_serch_result);



        smart_search_button = myLayout.findViewById(R.id.spn_status);
        searchView=myLayout.findViewById(R.id.search_v);
        items_search_attend=myLayout.findViewById(R.id.first_view);
        btn_more_recipe=myLayout.findViewById(R.id.btn_more_recipe);
        list_search=myLayout.findViewById(R.id.list_search);
        text_empty=myLayout.findViewById(R.id.text_empty);
        btn_search=myLayout.findViewById(R.id.btn_compete_search);

        nestedScrollView=myLayout.findViewById(R.id.activity_searchh);


        child_smart_seartch ch=new child_smart_seartch();
        ch.setTargetFragment(this, FRAGMENT_CODE);


        searchView.onActionViewExpanded();
        searchView.clearFocus();


        smart_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_and_show("hide");
                btn_search.setVisibility(View.INVISIBLE);
                poi=0;

                Bundle args = new Bundle();
                if(current_cuisin.equals("all_cuisine"))
                    args.putString("cu","كل المطابخ");
                else
                    args.putString("cu",current_cuisin);

                args.putString("ca",current_categ);
                ch.setArguments(args);

                child_smart_seartch myFragment = (child_smart_seartch)getActivity().getSupportFragmentManager().findFragmentByTag("holm");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if (myFragment == null || !myFragment.isVisible()){
                    fragmentTransaction.replace(R.id.fragment_smart,ch,"holm");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else {
                    Intent intent = new Intent();
                    onActivityResult(123, Activity.RESULT_OK, intent);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag("holm")).commit();
                }
            }
        });


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                btn_search.setVisibility(View.INVISIBLE);

                View view = v.getChildAt(v.getChildCount()-1);
                int d = view.getBottom();
                d -= (v.getHeight()+v.getScrollY());
                if(d==0)
                {
                   Log.e("place","you are in the bottom again");
                   if(btn_more_recipe.isClickable())
                       btn_more_recipe.performClick();
                }
            }
        });

        btn_more_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last_size=RecyclerViewItems.size();
                btn_more_recipe.setClickable(false);

                if(TYPE_OF_SEARCH.equals("smart"))
                    next_time_need_to_add_recipe();
                else if(TYPE_OF_SEARCH.equals("title"))
                    next_get_recipes_fro_search_by_title(get_five_by_five(Last_download));
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                btn_search.performClick();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (poi==0){
                btn_search.setText(Query_search);
                btn_search.setVisibility(View.VISIBLE);
                hide_and_show("hide");
                poi=1;
                }


                Query_search=newText;

                btn_search.setText(Query_search);
                return false;
            }

        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
             @Override
             public boolean onClose() {
                 btn_search.setVisibility(View.INVISIBLE);
                 return true;
             }
         });



        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
                btn_search.setVisibility(View.INVISIBLE);
                poi=0;
                on_choice_detected();
                Last_download=0;

                Query_search=btn_search.getText().toString().trim();
                keys_available_in_this_search.clear();
                new data_user_onligne().Search_for_this_text(Query_search, new data_user_onligne.get_recipe_exact_search() {
                    @Override
                    public void on_callback(ArrayList<new_recipe_search> recipes) {
                        //keys_available_in_this_search=get_all_String_contains_this_search(Query_search);
                        keys_available_in_this_search.addAll(recipes);
//
                        //if(get_all_String_contains_this_search(Query_search).size()==0)
                        //    hide_and_show("show title");
                        //else {
                        //    on_choice_detected();
                        //    Start_get_recipes_fro_search_by_title(get_five_by_five(Last_download));
                        //}
//
                        if(recipes.size()==0)
                            hide_and_show("show title");
                        else {
                            Start_get_recipes_fro_search_by_title(get_five_by_five(Last_download));
                        }
//
//
                    }
                });

            }
        });

     /*   if (all_title.size()==0){
            db.get_all_Title(getContext(), new data_user_onligne.get_titles() {
                @Override
                public void on_callback(HashMap<String, String> title) {
                    String data="";
                    int a=0;
                    for (Map.Entry mapentry : title.entrySet()) {
                        a++;
                       data+="('"+mapentry.getKey().toString()+"','"+mapentry.getValue().toString()+"')";
                       if(a==title.size())
                           data+=";";
                       else
                           data+=",";
                    }

                    new sql_manager(getContext()).insert_data_into_TABLE_ALL_TITLE_test(data);
                    all_title_and_keys = new sql_manager(getContext()).get_TABLE_ALL_TITLE();


                    for (Map.Entry mapentry : all_title_and_keys.entrySet()) {
                        all_title.add(mapentry.getValue().toString());
                    }
                    view_download_title.setVisibility(View.GONE);
                }
            });
        }
        else
            view_download_title.setVisibility(View.GONE);

      */

        return myLayout;
    }



    @Override
    public void onResume() {
        for (Object item : RecyclerViewItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.resume();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        for (Object item : RecyclerViewItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.pause();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        for (Object item : RecyclerViewItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.destroy();
            }
        }
        super.onDestroy();
    }

    private void addBannerAds_s(int last_place) {
        // Loop through the items array and place a new banner ad in every ith position in
        // the items List.
        for (int i = last_place; i <= RecyclerViewItems.size(); i += ITEMS_PER_AD) {
            final AdView adView = new AdView(getActivity());
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(AD_UNIT_ID);
            RecyclerViewItems.add(i, adView);

        }
    }

    private void loadBannerAd(final int index) {

        if (index >= RecyclerViewItems.size()) {
            if (first_or_second[0]==1) {
               adapter.notifyItemRangeInserted(last_size,(RecyclerViewItems.size())-last_size);
               Log.e("====================>", "" + RecyclerViewItems.size());
            }else{
                adapter = new RecyclerViewAdapter(getContext(),RecyclerViewItems);
                list_search.setAdapter(adapter);
            }
            return;
        }


        Object item = RecyclerViewItems.get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at ind ex " + index + " to be a banner ad"
                    + " ad.");
        }

        last_add_position=index;


        Log.e("the last ad position is","****"+index+"****");

        final AdView adView = (AdView) item;

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(
                new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        // The previous banner ad loaded successfully, call this method again to
                        // load the next ad in the items list.
                       // loadBannerAd(index + ITEMS_PER_AD);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // The previous banner ad failed to load. Call this method again to load
                        // the next ad in the items list.
                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Log.e(
                                "MainActivity",
                                "The previous banner ad failed to load with error: "
                                        + error
                                        + ". Attempting to"
                                        + " load the next banner ad in the items list.");
                      //  loadBannerAd(index + ITEMS_PER_AD);
                    }
                });

        loadBannerAd(index + ITEMS_PER_AD);
        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       searchView.clearFocus();
        if(requestCode == FRAGMENT_CODE && resultCode == Activity.RESULT_OK) {
            if(data.getStringExtra("Cuisine") != null) {
                String categories = data.getStringExtra("Categories");
                String cuisine = data.getStringExtra("Cuisine");

                on_choice_detected();
                current_categ=categories;

                if(cuisine.equals("كل المطابخ"))
                    current_cuisin="all_cuisine";
                else
                        current_cuisin=cuisine;

                get_data_on_line.get_number_current_pack_of_a_specific_cuisine(current_categ, current_cuisin, new data_user_onligne.number_current_pack() {
                    @Override
                    public void onCallback(String number) {
                        PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES = Integer.parseInt(number);
                        Start_get_the_recipe(current_categ,current_cuisin,PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES);
                    }
                });

                //hide_and_show("hide");


            }else{
                hide_and_show("show null");
            }
        }
    }

    public void on_choice_detected(){
        List<Object> arrayList=new ArrayList<>();
        for(int i=0;i<11;i++){
            arrayList.add("ddd");
        }
        list_search.setLayoutManager(new LinearLayoutManager(getActivity()));
        com.cokeappingo.list_activity_search_vide.RecyclerViewAdapter adapter=new com.cokeappingo.list_activity_search_vide.RecyclerViewAdapter(getContext(),arrayList);
        list_search.setAdapter(adapter);
        btn_more_recipe.setText("يتم تحميل المزيد من الوصفات ...");
    }

    public void hide_and_show(String etat){
        if(etat.equals("hide")){
            items_search_attend.setVisibility(View.INVISIBLE);
            list_search.setVisibility(View.VISIBLE);
            list_search.setAdapter(null);
            btn_more_recipe.setVisibility(View.INVISIBLE);
        }
        else if(etat.equals("show")){
            items_search_attend.setVisibility(View.VISIBLE);
            search.setVisibility(View.INVISIBLE);
            no_search_result.setVisibility(View.VISIBLE);
            String first = "  لاتوجد اي نتائج بحث بالمطبخ ' ";
            String next = "<font color='#FD5C5C'>"+current_cuisin+"</font>";
            String vo = "' في صنف ' ";
            String dod = "<font color='#FD5C5C'>"+current_categ+"</font>";
            String last=" ' ";
            text_empty.setText(Html.fromHtml(first + next+vo+dod+last));
            list_search.setVisibility(View.INVISIBLE);
        }
        else if(etat.equals("show title")){
            items_search_attend.setVisibility(View.VISIBLE);
            search.setVisibility(View.INVISIBLE);
            no_search_result.setVisibility(View.VISIBLE);
            String first = "  لاتوجد اي نتائج بحث ب' ";
            String next = "<font color='#FD5C5C'>"+Query_search+"</font>";
            String last=" ' ";
            text_empty.setText(Html.fromHtml(first + next+last));
            list_search.setVisibility(View.INVISIBLE);
        }
        else if (etat.equals("show null")){
            items_search_attend.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            no_search_result.setVisibility(View.INVISIBLE);
            text_empty.setText(Html.fromHtml("ابحث عن طريق العنوان او عن طريق زر البحث الذكي"));
            list_search.setVisibility(View.INVISIBLE);
        }
    }

    public void Start_get_the_recipe(String Categories, String Cuisine,int pack){
         //int current_packo=new sql_manager(getContext()).get_current_pack_of_this_smart_search(Categories, Cuisine);
         //current_pack=current_packo;
         int[] count={0};
         pack_Number=0;
         new_list_load.clear();
         RecyclerViewItems.clear();

         get_data_on_line.get_keys_of_recipes_in_this_pack_into_cuisine(Categories, Cuisine, "" +pack, new data_user_onligne.get_recipe_exact_search() {
             @Override
             public void on_callback(ArrayList<new_recipe_search> recipes) {
                 if(recipes.size()==0 && PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES==0)
                     hide_and_show("show");
                 else if(recipes.size() == 0) {
                     PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES--;
                    Start_get_the_recipe(Categories,Cuisine,PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES);
                 }
                 for (new_recipe_search recipe : recipes) {
                     count[0]++;
                     if(recipe!=null)
                         new_list_load.add(recipe);
                     if (count[0] == recipes.size()) {
                         if (PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES==0)
                             btn_more_recipe.setText("لا مزيد من الوصفات في هذا البحث");
                         else
                             btn_more_recipe.setClickable(true);
                         if (new_list_load.size()==0 ){
                             hide_and_show("show");
                         }
                         else {
                             RecyclerViewItems.addAll(new_list_load);
                             first_or_second[0] = 0;
                             addBannerAds_s(0);
                             loadBannerAd(0);
                             TYPE_OF_SEARCH="smart";
                             //   new sql_manager(getContext()).Update_data_into_current_pack(Categories, Cuisine, current_packo + 1);
                             btn_more_recipe.setVisibility(View.VISIBLE);
                             //PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES--;
                             if((recipes.size()==0 || recipes.size()==1) && PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES--!=0)
                                 btn_more_recipe.performClick();
                             else
                                 PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES--;
                         }
                     }
                 }


             }
         });

    }

    public void next_time_need_to_add_recipe(){
        int[] count={0};
        new_list_load.clear();
        //current_pack++;
        get_data_on_line.get_keys_of_recipes_in_this_pack_into_cuisine(current_categ, current_cuisin, "" + PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES, new data_user_onligne.get_recipe_exact_search() {
            @Override
            public void on_callback(ArrayList<new_recipe_search> recipes) {

                if(recipes.size()==0 && PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES!=0){
                    btn_more_recipe.setClickable(true);
                    PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES--;
                    btn_more_recipe.performClick();
                }

                for (new_recipe_search recipe : recipes) {
                            count[0]++;
                            if(recipe!=null) {
                                new_list_load.add(recipe);
                                Log.e("recipe title",recipe.getTitle());
                            }

                            if(count[0] == recipes.size()) {

                                first_or_second[0] = 1;
                                RecyclerViewItems.addAll(new_list_load);


                                addBannerAds_s(last_add_position + ITEMS_PER_AD);
                                loadBannerAd(last_add_position + ITEMS_PER_AD);
                                //   new sql_manager(getContext()).Update_data_into_current_pack(current_categ,current_cuisin,current_pack+1);

                                if(PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES==0)
                                    btn_more_recipe.setText("لا مزيد من الوصفات في هذا البحث");
                                else
                                    btn_more_recipe.setClickable(true);

                                if((recipes.size()==0 || recipes.size()==1) && PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES--!=0)
                                    btn_more_recipe.performClick();
                                else
                                    PACK_NUMBER_IN_SUM_CUISINE_CATEGORIES--;
                            }
                        }


            }
        });

    }





    //public ArrayList<String> get_all_String_contains_this_search(String search){
    //    ArrayList<String> tio=new ArrayList<>();
//
    //    //for (Map.Entry mapentry : all_title_and_keys.entrySet()) {
    //    //    if(mapentry.getValue().toString().contains(search) || mapentry.getValue().toString().equals(search.trim())){
    //    //        tio.add(mapentry.getKey().toString());
    //    //    }
    //    //}
    //    db.Search_for_this_text(getContext(), search, new data_user_onligne.key_user_recipe() {
    //        @Override
    //        public void on_callback(ArrayList<String> keys) {
    //
    //        }
    //    });
//
    //    return tio;
    //}

//  public String get_the_top_key(HashMap<String,ArrayList<String>> kk){
//      String the_top="";
//      int his_size=0;

//      for (Map.Entry mapentry : kk.entrySet()) {
//          ArrayList<String> j=(ArrayList<String>)mapentry.getValue();
//          if (j.size()>his_size){
//              the_top=mapentry.getKey().toString();
//              his_size=j.size();
//          }
//      }
//      return the_top;
//  }

//  public ArrayList<String> get_the_string_en_form_line(String default_string){
//      ArrayList<String> cup=new ArrayList<>();
//      StringTokenizer tokenizer = new StringTokenizer(default_string);
//      while (tokenizer.hasMoreTokens()){
//          String word = tokenizer.nextToken();
//          cup.add(word); // word you are looking in
//      }

//      return cup;
//  }

//  public ArrayList<ArrayList<String>> get_the_sentence_possible(ArrayList<String> words){
//      ArrayList<ArrayList<String>> sentence_possible=new ArrayList<>();
//      ArrayList<String> m=new ArrayList<>();
//      for (int i=0;i<words.size();i++){
//          String sentence="";
//          for (int j=0;j<=i;j++){
//              sentence+=words.get(j);
//              sentence+=" ";
//          }
//          m.add(sentence);
//      }
//      sentence_possible.add(m);
//      return sentence_possible;
//  }/*  public String get_Top_search(String text_search){
//        for(String sentence:all_title){
//            for(ArrayList<String> table_sentence_possible:get_the_sentence_possible(get_the_string_en_form_line(sentence))) {
//                for(String this_sentence:table_sentence_possible){
//                    if(diveseur.containsKey(this_sentence)) {
//                        ArrayList<String> jn=diveseur.get(this_sentence);
//                        for (int i=table_sentence_possible.indexOf(this_sentence)+1;i<table_sentence_possible.size();i++){
//                            if(!jn.contains(table_sentence_possible.get(i)))
//                                jn.add(table_sentence_possible.get(i));
//                        }
//                        diveseur.put(this_sentence,jn);
//                    }else {
//                        diveseur.put(this_sentence,new ArrayList<String>());
//                    }
//                }
//            }
//        }
//        for(String sentence:all_title){
//            for(ArrayList<String> table_sentence_possible:get_the_sentence_possible(get_the_string_en_form_line(sentence))) {
//                for(String this_sentence:table_sentence_possible){
//                    if(diveseur.containsKey(this_sentence)) {
//                        ArrayList<String> jn=diveseur.get(this_sentence);
//                        for (int i=table_sentence_possible.indexOf(this_sentence)+1;i<table_sentence_possible.size();i++){
//                            if(!jn.contains(table_sentence_possible.get(i)) || sentence.equals(table_sentence_possible.get(i)))
//                                jn.add(table_sentence_possible.get(i));
//                        }
//                        diveseur.put(this_sentence,jn);
//                    }else {
//                        diveseur.put(this_sentence,new ArrayList<String>());
//                    }
//                }
//            }
//        }
//
//
//
//        String string=text_search;
//
//        final HashMap<String,ArrayList<String>> exact_list=new HashMap<>();
//
//
//        for (Map.Entry mapentry : diveseur.entrySet()) {
//            if (mapentry.getKey().toString().contains(string))
//                exact_list.put(mapentry.getKey().toString(),diveseur.get(mapentry.getKey().toString()));
//        }
//
//        String the_top_search_is="";
//
//        if(exact_list.size()>1)
//            the_top_search_is=get_the_top_key(exact_list);
//        else if(exact_list.size()==1){
//            for (Map.Entry mapentry : exact_list.entrySet()) {
//                the_top_search_is=mapentry.getKey().toString();
//            }
//        }
//
//
//        diveseur.clear();
//        return the_top_search_is;
//    }
//
//   */

    int[] count={0};
    public void Start_get_recipes_fro_search_by_title(ArrayList<new_recipe_search> recipes){

        count[0]=0;
        new_list_load.clear();
        RecyclerViewItems.clear();

        Log.e("recipes taille",recipes.size()+"");

        for (new_recipe_search recipe : recipes) {
            count[0]++;
            if(recipe!=null)
                new_list_load.add(recipe);
            if (count[0] == recipes.size()) {

                btn_more_recipe.setClickable(true);


                    RecyclerViewItems.addAll(new_list_load);
                    first_or_second[0] = 0;
                    addBannerAds_s(0);
                    loadBannerAd(0);

                    TYPE_OF_SEARCH="title";

                    //   new sql_manager(getContext()).Update_data_into_current_pack(Categories, Cuisine, current_packo + 1);
                    btn_more_recipe.setVisibility(View.VISIBLE);

            }
        }
    }

    public void next_get_recipes_fro_search_by_title(ArrayList<new_recipe_search> recipes) {

            count[0]=0;
            new_list_load.clear();

        if (recipes.size()==0){
            btn_more_recipe.setText("لا مزيد من الوصفات في هذا البحث");
        }

        for (new_recipe_search recipe : recipes) {
            count[0]++;
            if(recipe!=null)
                new_list_load.add(recipe);
            if (count[0] == recipes.size()) {

                btn_more_recipe.setClickable(true);


                 RecyclerViewItems.addAll(new_list_load);
                 first_or_second[0] = 1;

                 addBannerAds_s(last_add_position + ITEMS_PER_AD);
                 loadBannerAd(last_add_position + ITEMS_PER_AD);

                 TYPE_OF_SEARCH="title";


                 //   new sql_manager(getContext()).Update_data_into_current_pack(Categories, Cuisine, current_packo + 1);
                 btn_more_recipe.setVisibility(View.VISIBLE);

            }
        }


    }

    public ArrayList<new_recipe_search> get_five_by_five(int last_place){
        ArrayList<new_recipe_search> items=new ArrayList<>();

        for (int i=last_place;i<(last_place+5);i++){
            if (keys_available_in_this_search.size()>i)
                items.add(keys_available_in_this_search.get(i));
            Last_download++;
        }


        return items;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
          if (getActivity().getSupportFragmentManager().findFragmentByTag("holm")!=null )
                items_search_attend.setVisibility(View.INVISIBLE);
    }


}