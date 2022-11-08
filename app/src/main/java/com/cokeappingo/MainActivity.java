package com.cokeappingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.class_reglage.Chrono;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.new_recipe_search;
import com.cokeappingo.list_activity_search_vide.RecyclerViewAdapter;
import com.cokeappingo.liste_view_acceuil.catugorie;
import com.cokeappingo.liste_view_acceuil.liste_adapter;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;
    RecyclerView List_Object_acceuil;
    private List<Object> RecyclerViewItems = new ArrayList<>();
    com.cokeappingo.list_activity_search.RecyclerViewAdapter adapter;
    int[] first_or_second=new int[1];
    private int last_add_position;
    int last_size;
    ArrayList<Object> new_list_load=new ArrayList<>();
    NestedScrollView nestedScrollView;

    Button btn_more_recipe;
    public static final int ITEMS_PER_AD = 6;
    private static final String AD_UNIT_ID = "ca-app-pub-8591526182350923/9535919911";

    ArrayList<int[]> packo=new ArrayList<>();
    int count=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myLayout = inflater.inflate(R.layout.activity_main, container, false);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        //new sql_manager(getContext()).Update_data_pointer(9);

        List_Object_acceuil=myLayout.findViewById(R.id.list_search_vide);
        btn_more_recipe=myLayout.findViewById(R.id.btn_more_recipe);
        nestedScrollView=myLayout.findViewById(R.id.list_view);


        List<Object> arrayList=new ArrayList<>();

        for(int i=0;i<11;i++){
            arrayList.add("ddd");
        }
        List_Object_acceuil.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(getContext(),arrayList);
        List_Object_acceuil.setAdapter(adapter);


        charge_recipe();

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                View view = v.getChildAt(v.getChildCount()-1);
                int d = view.getBottom();
                d -= (v.getHeight()+v.getScrollY());
                if(d==0)
                {
                    Log.e("place","you are in the bottom again");
                    btn_more_recipe.performClick();
                }
            }
        });

        btn_more_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last_size=RecyclerViewItems.size();
                btn_more_recipe.setClickable(false);
                count++;
                try {
                    next_time_need_to_add_recipe(packo.get(count));
                    new sql_manager(getContext()).Update_data_pointer(packo.get(count)[0]);
                }catch (IndexOutOfBoundsException error){
                    new sql_manager(getContext()).Update_data_pointer(0);
                    charge_recipe();
                }
            }
        });

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
            final AdView adView = new AdView(getContext());
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
                adapter = new com.cokeappingo.list_activity_search.RecyclerViewAdapter(getContext(),RecyclerViewItems);
                List_Object_acceuil.setAdapter(adapter);
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

    public void Start_get_the_recipe(int[] pack){
        RecyclerViewItems.clear();
        new data_user_onligne().get_keys_of_recipes_in_this_pack_into_cuisine("all_categories","all_cuisine", "" +pack[0], new data_user_onligne.get_recipe_exact_search() {
            @Override
            public void on_callback(ArrayList<new_recipe_search> recipes1) {
                new data_user_onligne().get_keys_of_recipes_in_this_pack_into_cuisine("all_categories", "all_cuisine", "" + pack[1], new data_user_onligne.get_recipe_exact_search() {
                    @Override
                    public void on_callback(ArrayList<new_recipe_search> recipes2) {
                        new data_user_onligne().get_keys_of_recipes_in_this_pack_into_cuisine("all_categories", "all_cuisine", "" + pack[2], new data_user_onligne.get_recipe_exact_search() {
                            @Override
                            public void on_callback(ArrayList<new_recipe_search> recipes3) {
                                RecyclerViewItems.addAll(recipes1);
                                RecyclerViewItems.addAll(recipes2);
                                RecyclerViewItems.addAll(recipes3);
                                first_or_second[0] = 0;
                                addBannerAds_s(0);
                                loadBannerAd(0);
                                btn_more_recipe.setClickable(true);
                            }
                        });
                    }
                });
            }
        });

    }

    public void next_time_need_to_add_recipe(int[] pack){
        new data_user_onligne().get_keys_of_recipes_in_this_pack_into_cuisine("all_categories","all_cuisine", "" +pack[0], new data_user_onligne.get_recipe_exact_search() {
            @Override
            public void on_callback(ArrayList<new_recipe_search> recipes1) {
                new data_user_onligne().get_keys_of_recipes_in_this_pack_into_cuisine("all_categories", "all_cuisine", "" + pack[1], new data_user_onligne.get_recipe_exact_search() {
                    @Override
                    public void on_callback(ArrayList<new_recipe_search> recipes2) {
                        new data_user_onligne().get_keys_of_recipes_in_this_pack_into_cuisine("all_categories", "all_cuisine", "" + pack[2], new data_user_onligne.get_recipe_exact_search() {
                            @Override
                            public void on_callback(ArrayList<new_recipe_search> recipes3) {
                                RecyclerViewItems.addAll(recipes1);
                                RecyclerViewItems.addAll(recipes2);
                                RecyclerViewItems.addAll(recipes3);
                                first_or_second[0] = 1;
                                addBannerAds_s(last_add_position + ITEMS_PER_AD);
                                loadBannerAd(last_add_position + ITEMS_PER_AD);
                                btn_more_recipe.setClickable(true);
                                Log.e("tab",pack[0]+" "+pack[1]+" "+pack[2]);
                            }
                        });
                    }
                });
            }
        });

    }

        private void charge_recipe(){
        int j=new sql_manager(getContext()).get_point();


        if(j==0) {
            new data_user_onligne().get_number_current_pack_of_a_specific_cuisine("all_categories", "all_cuisine", new data_user_onligne.number_current_pack() {
                @Override
                public void onCallback(String number) {
                    int top_number = Integer.parseInt(number);

                    new sql_manager(getContext()).Update_data_pointer(top_number);
                    Log.e("top_nomber", "a");
                    int l = top_number / 3;

                    for (int i = 0; i <= l; ++i) {
                        int[] k = new int[3];
                        k[0] = (top_number - i);
                        k[1] = (((top_number / 3) * 2) - i);
                        k[2] = ((top_number / 3) - i);
                        packo.add(k);
                        Log.e("tab",k[0]+" "+k[1]+" "+k[2]);
                    }

                    Start_get_the_recipe(packo.get(0));
                }
            });
        }
        else {
            int l=j / 3;
            Log.e("top_nomber","b");
            for(int i=0;i<=l;++i){
                int[] k=new int[3];
               // k[0]=((j*3)-i);
               // k[1]=((j*2)-i);
               // k[2]=(j-i);
                k[0] = (j - i);
                k[1] = (((j / 3) * 2) - i);
                k[2] = ((j / 3) - i);

                Log.e("tab",k[0]+" "+k[1]+" "+k[2]);

                packo.add(k);
            }

            Start_get_the_recipe(packo.get(0));
        }

    }
}