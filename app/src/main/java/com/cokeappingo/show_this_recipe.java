package com.cokeappingo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.SendNotificationPack.APIService;
import com.cokeappingo.SendNotificationPack.Client;
import com.cokeappingo.SendNotificationPack.Data;
import com.cokeappingo.SendNotificationPack.MyResponse;
import com.cokeappingo.SendNotificationPack.NotificationSender;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.account;
import com.cokeappingo.class_utile.comment;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.comment.list_comment;
import com.cokeappingo.list_ingredient_and_how_to_prpare.L_i_h_p;
import com.cokeappingo.list_ingredient_and_how_to_prpare.L_i_h_p_in_show_vertical;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.cokeappingo.user_info.user_info;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joooonho.SelectableRoundedImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class show_this_recipe extends Fragment implements View.OnClickListener{


    RecyclerView list_ingredient,list_how_to_prepare,list_comment;
    ImageButton change_list_ingredient_to_vertical,change_list_prepare_to_vertical,start1,start2,start3,start4,start5
            ,change_list_comment_to_vertical,btn_cancel,save_this_recipe,share_this_recipe;

    EditText input_comment;

    TextView title_recipe,description_recipe,name_auteur,description_auteur,no_comment_recipe,btn_date;

    Button share_comment,btn_person,btn_time;

    SelectableRoundedImageView image_recipe,image_profile;

    NestedScrollView nestedScrollView;
    LottieAnimationView download_data;


    ConstraintLayout constraintLayout_recipe_not_found;

    new_recipe the_recipe_come;


    data_user_onligne db;

    ArrayList<String> all_ingredient;
    ArrayList<String> first_list=new ArrayList<>();
    ArrayList<String> second_list=new ArrayList<>();

    ArrayList<String> all_prepare;
    ArrayList<String> first_list_p=new ArrayList<>();
    ArrayList<String> second_list_p=new ArrayList<>();

    ArrayList<comment> all_comment;
    ArrayList<comment> first_list_c=new ArrayList<>();
    ArrayList<comment> second_list_c=new ArrayList<>();

    String id_recipe;
    int rating_value=0;
    int more_comment=0;

    Bitmap Image_recipe_bitmap;

     private APIService apiService;
    private AdView adView;




    private void create_ads(View view){
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(getContext());

        adView = new AdView(getContext(), "IMG_16_9_APP_INSTALL#500481751304928_600863537933415", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = view.findViewById(R.id.adView_show);
        adContainer.addView(adView);

        load_banners();
    }



    private void load_banners(){
        com.facebook.ads.AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
                Log.e("ads load","banner");
                Handler handler=new Handler();
                handler.postDelayed(() -> load_banners(), 60000);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        };

        // Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myLayout = inflater.inflate(R.layout.activity_show_this_recipe, container, false);


        sql_manager sql_manager=new sql_manager(getContext());

        list_ingredient=myLayout.findViewById(R.id.list_ingredients);
        list_how_to_prepare=myLayout.findViewById(R.id.list_prepare);
        list_comment=myLayout.findViewById(R.id.list_commentaire);
        change_list_ingredient_to_vertical=myLayout.findViewById(R.id.list_vertical);
        change_list_prepare_to_vertical=myLayout.findViewById(R.id.list_vertical_prepare);
        change_list_comment_to_vertical=myLayout.findViewById(R.id.list_vertical_commentaire);
        input_comment=myLayout.findViewById(R.id.input_commentaire);
        share_comment=myLayout.findViewById(R.id.button_share_comm);
        title_recipe=myLayout.findViewById(R.id.text_title_recipe);
        description_recipe=myLayout.findViewById(R.id.text_descriprtion_recipe);
        image_recipe=myLayout.findViewById(R.id.image_image_recipe);
        btn_cancel=myLayout.findViewById(R.id.btn_cancel);
        btn_person=myLayout.findViewById(R.id.button_person);
        btn_time=myLayout.findViewById(R.id.button_time);
        btn_date=myLayout.findViewById(R.id.button_date);
        name_auteur=myLayout.findViewById(R.id.name_profile);
        description_auteur=myLayout.findViewById(R.id.description_profile);
        image_profile=myLayout.findViewById(R.id.image_profil);
        no_comment_recipe=myLayout.findViewById(R.id.no_commentaire_recipe);
        nestedScrollView=myLayout.findViewById(R.id.nesto);
        download_data=myLayout.findViewById(R.id.lottie_download_data);
        save_this_recipe=myLayout.findViewById(R.id.btn_save_recipe);
        constraintLayout_recipe_not_found=myLayout.findViewById(R.id.recipe_not_found);
        share_this_recipe=myLayout.findViewById(R.id.btn_share);
        create_ads(myLayout);

        start1=myLayout.findViewById(R.id.start5);
        start2=myLayout.findViewById(R.id.start4);
        start3=myLayout.findViewById(R.id.start3);
        start4=myLayout.findViewById(R.id.start2);
        start5=myLayout.findViewById(R.id.start1);

        start1.setOnClickListener(this);
        start2.setOnClickListener(this);
        start3.setOnClickListener(this);
        start4.setOnClickListener(this);
        start5.setOnClickListener(this);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof open_form_Lien)
                    getActivity().finish();
                else
                    getFragmentManager().popBackStack();
            }
        });

        id_recipe=getArguments().getString("recipe");


        db=new data_user_onligne();

        db.retrive_recipe_user_by_KEY(id_recipe, new data_user_onligne.retrive_recipe() {
            @Override
            public void onCallback(new_recipe recipe) {

                if (getActivity() == null){
                    return;
                }

                if(recipe != null) {
                    Log.e("rexx",recipe.getAuteur_ID());
                    the_recipe_come = recipe;
                    title_recipe.setText(recipe.getTitle());
                    description_recipe.setText(recipe.getDescription());


                    all_ingredient = recipe.getIngredient();
                    all_prepare = recipe.getHow_to_prepare();

                    charge_ingredient();
                    charger_image_recipe(recipe.getLien_puc());
                    charge_how_to_prepare();
                    charger_more_details(recipe);
                    charger_data_user_online(recipe.getAuteur_ID());

                    nestedScrollView.setVisibility(View.VISIBLE);
                }else{
                    constraintLayout_recipe_not_found.setVisibility(View.VISIBLE);
                }
                download_data.setVisibility(View.GONE);
            }
        });

        if(sql_manager.exist_recipe_saved(id_recipe)){
            save_this_recipe.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bookmark));
        }else {
            save_this_recipe.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bookmark_border));
        }

        save_this_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sql_manager.exist_recipe_saved(id_recipe)){
                    sql_manager.delet_recipe_saved(id_recipe);
                    save_this_recipe.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bookmark_border));
                    Toast.makeText(getContext(), "تم الغاء الحفظ بنجاح", Toast.LENGTH_SHORT).show();
                }else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Image_recipe_bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos); // bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    sql_manager.insert_data_into_SAVE(id_recipe,the_recipe_come.getTitle(),b);
                    save_this_recipe.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bookmark));
                    Toast.makeText(getContext(), "تم الحفظ بنجاح", Toast.LENGTH_SHORT).show();
                }
            }
        });

        share_this_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String share=the_recipe_come.getTitle();
                share+="  وصفة رائعة جدا  ";
                share +="\n\n";
                share+="رابط التطبيق في المتجر (في حالة كان تطبيق شاف طبيخة غير مثبت على هاتفك)";
                share+="\n\n";
                share+="https://play.google.com/store/apps/details?id=" + getContext().getPackageName();
                share+="\n\n";
                share+="رابط الوصفة في التطبيق";
                share+="\n\n";
                share+="www.cokeappingo.com/" + the_recipe_come.getId_push();

                ShareCompat.IntentBuilder
                        .from(getActivity())
                        .setText(share)
                        .setType("text/plain")
                        .setChooserTitle("شاف طبيخة")
                        .startChooser();

            }
        });

        /////////////////////////////////////// ingredient
        change_list_ingredient_to_vertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_list.addAll(second_list);
                list_ingredient.getAdapter().notifyItemRangeInserted(3,second_list.size());
                v.setVisibility(View.GONE);
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////




        /////////////////////////////////////// how to prepare
        change_list_prepare_to_vertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_list_p.addAll(second_list_p);
                list_how_to_prepare.getAdapter().notifyItemRangeInserted(3,second_list_p.size());
                v.setVisibility(View.GONE);
            }
        });
        /////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////// Comment

        db.get_all_comment_recipe(id_recipe, new data_user_onligne.get_comment() {
            @Override
            public void on_callback(ArrayList<comment> comments) {
                all_comment=comments;
                charge_comment();
            }
        });

        change_list_comment_to_vertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_list_c.addAll(second_list_c);
                list_comment.getAdapter().notifyItemRangeInserted(3,second_list_c.size());
                v.setVisibility(View.GONE);
                change_list_comment_to_vertical.setClickable(false);
                more_comment=1;
            }
        });

        share_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(more_comment==0)
                change_list_comment_to_vertical.performClick();

                comment comment=new comment();
                comment.setComment(input_comment.getText().toString().trim());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd'\n'HH:mm", Locale.US);
                comment.setDate(formatter.format(new Date()));
                comment.setId_visitor(new user_info().get_info_account(GoogleSignIn.getLastSignedInAccount(getContext())).getAccount_ID());
                comment.setValue(rating_value);

                if (comment.getComment().equals("")){
                    switch (comment.getValue()){
                        case 1:
                            comment.setComment("وصفة سيئة");
                            break;
                        case 2:
                            comment.setComment("وصفة ضعيفة");
                            break;
                        case 3:
                            comment.setComment("وصفة متوسطة");
                            break;
                        case 4:
                            comment.setComment("وصفة رائعة");
                            break;
                        case 5:
                            comment.setComment("وصفة رائعة جدا");
                            break;
                    }
                }

                boolean is=false;

                comment comment2 = null;
                for (comment comment1:first_list_c){
                 if(comment1.getId_visitor().equals(comment.getId_visitor())) {
                     is=true;
                     comment2=comment1;
                 }
                }

                if(is)
                    update_comment(comment,comment2);
                else
                    save_a_comment(comment);

                new data_user_onligne().retrive_Tocken(the_recipe_come.getAuteur(), new data_user_onligne.return_id_push() {
                    @Override
                    public void add_complet(String usertoken) {
                        sendNotifications(usertoken,the_recipe_come.getId_push(),""+rating_value,"comment",""+
                                new sql_manager(getContext()).get__account().getName());
                    }
                });


            }
        });

        return myLayout;
    }



    @Override
    public void onClick(View v) {
        change_background_star(v);
        input_comment.setVisibility(View.VISIBLE);
        share_comment.setVisibility(View.VISIBLE);
        switch (v.getId()){
            case R.id.start5:
                rating_value=1;
                break;
            case R.id.start4:
                rating_value=2;
                break;
            case R.id.start3:
                rating_value=3;
                break;
            case R.id.start2:
                rating_value=4;
                break;
            case R.id.start1:
                rating_value=5;
                break;
        }
    }

    public void change_background_star(View view){
        switch (view.getId()){
            case R.id.start5:
                start1.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start2.setBackground(getContext().getResources().getDrawable(R.drawable.selector_star));
                start3.setBackground(getContext().getResources().getDrawable(R.drawable.selector_star));
                start4.setBackground(getContext().getResources().getDrawable(R.drawable.selector_star));
                start5.setBackground(getContext().getResources().getDrawable(R.drawable.selector_star));
                break;
            case R.id.start4:
                start3.setBackground(getContext().getResources().getDrawable(R.drawable.selector_star));
                start4.setBackground(getContext().getResources().getDrawable(R.drawable.selector_star));
                start5.setBackground(getContext().getResources().getDrawable(R.drawable.selector_star));

                start1.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start2.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                break;
            case R.id.start3:
                start3.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start2.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start1.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start4.setBackground(getContext().getResources().getDrawable(R.drawable.selector_star));
                start5.setBackground(getContext().getResources().getDrawable(R.drawable.selector_star));
                break;
            case R.id.start2:
                start4.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start3.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start2.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start1.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start5.setBackground(getContext().getResources().getDrawable(R.drawable.selector_star));
                break;
            case R.id.start1:
                start5.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start4.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start3.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start2.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                start1.setBackground(getContext().getResources().getDrawable(R.drawable.star));
                break;
        }
    }

    public void charge_ingredient(){
        int l=0;

        for (String m:all_ingredient){
            if (l>2)
                second_list.add(m);
            else
                first_list.add(m);
            l++;
        }

        if (all_ingredient.size()<=3)
            change_list_ingredient_to_vertical.setVisibility(View.GONE);

        list_ingredient.setAdapter(new L_i_h_p(getContext(), first_list,false));
        list_ingredient.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void charge_how_to_prepare(){
        int l=0;

        for (String m:all_prepare){
            if (l>2)
                second_list_p.add(m);
            else
                first_list_p.add(m);
            l++;
        }

        if (all_prepare.size()<=3)
            change_list_prepare_to_vertical.setVisibility(View.GONE);


        list_how_to_prepare.setAdapter(new L_i_h_p(getContext(), first_list_p,false));
        list_how_to_prepare.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    public void charge_comment(){
        int l=0;

        for (comment m:all_comment){
                if (l>2)
                    second_list_c.add(m);
                else
                    first_list_c.add(m);
                l++;


        }

        if (all_comment.size()<=3 && all_comment.size()!=0) {
            change_list_comment_to_vertical.setVisibility(View.GONE);
            more_comment=1;
        }
        else if (all_comment.size()==0){
            change_list_comment_to_vertical.setVisibility(View.GONE);
            no_comment_recipe.setVisibility(View.VISIBLE);
            more_comment=1;
        }

        list_comment.setAdapter(new list_comment(getContext(),first_list_c,id_recipe));
        list_comment.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void charger_image_recipe(String lien_puc){
        Glide.with(getContext())
                .asBitmap()
                .load(lien_puc)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                      image_recipe.setImageBitmap(new adapter_setting().getResizedBitmap_multiple(resource,getContext()));
                      Image_recipe_bitmap=resource;
                      save_this_recipe.setVisibility(View.VISIBLE);
                      share_this_recipe.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void charger_more_details(new_recipe n){
        int person;
        try {
            person=Integer.parseInt(n.getPerson());
        }catch (NumberFormatException e){
            person=0;
        }

        switch (person){
            case 0:
                btn_person.setText(new adapter_setting().adapter_number("غير محدد",getContext()));
                break;
            case 1:
                btn_person.setText(new adapter_setting().adapter_number("تكفي لشخص واحد",getContext()));
                break;
            case 2:
                btn_person.setText(new adapter_setting().adapter_number("تكفي لشخصان",getContext()));
                break;
            case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                btn_person.setText(new adapter_setting().adapter_number("تكفي ل "+person+" أشخاص",getContext()));
                break;
            default:
                btn_person.setText(new adapter_setting().adapter_number("تكفي ل "+person+" شخصاً",getContext()));
                break;
        }

        int time;
        try {
            time=Integer.parseInt(n.getCooking_time());
        }catch (NumberFormatException e){
            time=0;
        }
        switch (time){
            case 0:
                btn_time.setText(new adapter_setting().adapter_number("غير محدد",getContext()));
                break;
            case 1:
                btn_time.setText(new adapter_setting().adapter_number("تطهى خلال دقيقة واحدة",getContext()));
                break;
            case 2:
                btn_time.setText(new adapter_setting().adapter_number("تطهى خلال دقيقتان",getContext()));
                break;
            case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                btn_time.setText(new adapter_setting().adapter_number("تطهى خلال "+time+" دقائق",getContext()));
                break;
            default:
                btn_time.setText(new adapter_setting().adapter_number("تطهى خلال "+time+" دقيقة",getContext()));
                break;
        }

        String[] dates = n.getDate_publication().split(System.getProperty("line.separator"));
        btn_date.setText(new adapter_setting().adapter_number("تمت المصادقة على نشر هذه الوصفة بتاريخ "+dates[0]+" على الساعة "+dates[1],getContext()));

    }

    public void charger_data_user_online(String id_user){
        db.if_user_exist(id_user, new data_user_onligne.user_exist() {
            @Override
            public void exist(boolean etat, account r) {
                if (getActivity() == null) {
                    return;
                }
                name_auteur.setText(r.getName());
                description_auteur.setText(r.getBio());
                Glide.with(getContext())
                        .asBitmap()
                        .load(r.getImage())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                image_profile.setImageBitmap(resource);
                            }
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }
        });
    }

    public void save_a_comment(comment comment){
        db.add_a_comment_to_recipe(id_recipe, comment, new data_user_onligne.add_succeful() {
            @Override
            public void add_complet(boolean etat) {

            }
        });

        first_list_c.add(comment);
        list_comment.getAdapter().notifyItemInserted(all_comment.size());
        no_comment_recipe.setVisibility(View.INVISIBLE);
    }


    private void update_comment(comment comment,comment last) {
        db.add_a_comment_to_recipe(id_recipe, comment, new data_user_onligne.add_succeful() {
            @Override
            public void add_complet(boolean etat) {

            }
        });


        first_list_c.set( first_list_c.indexOf(last),comment);
        list_comment.getAdapter().notifyItemChanged(first_list_c.indexOf(comment));
        no_comment_recipe.setVisibility(View.INVISIBLE);
    }

    public void sendNotifications(String usertoken,String id_push,String date,String status,String cause) {
        Data data = new Data(id_push,date,status);
        data.setCause_refuse(cause);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(getContext(), "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}