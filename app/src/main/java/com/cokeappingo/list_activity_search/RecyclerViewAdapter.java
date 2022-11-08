package com.cokeappingo.list_activity_search;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.Add_new_recipe;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.MainActivity;
import com.cokeappingo.R;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.comment;
import com.cokeappingo.class_utile.new_recipe_search;
import com.cokeappingo.show_this_recipe;
import com.google.android.gms.ads.AdView;
import com.joooonho.SelectableRoundedImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import pl.pawelkleczkowski.customgauge.CustomGauge;

/**
 * The {@link RecyclerViewAdapter} class.
 * <p>The adapter provides access to the items in the {@link MenuItemViewHolder}
 * or the {@link AdViewHolder}.</p>
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The banner ad view type.
    private static final int BANNER_AD_VIEW_TYPE = 1;

    // An Activity's Context.
    private final Context context;
    AppCompatActivity activity;

    // The list of banner ads and menu items.
    private final List<Object> recyclerViewItems;





    public RecyclerViewAdapter(Context context, List<Object> recyclerViewItems) {
        this.context = context;
        this.recyclerViewItems = recyclerViewItems;
    }

    public RecyclerViewAdapter(Context context, List<Object> recyclerViewItems, AppCompatActivity activity) {
        this.context = context;
        this.recyclerViewItems = recyclerViewItems;
        this.activity=activity;
    }




    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private TextView text_title_recipe;
        private CustomGauge progress_ratting;
        SelectableRoundedImageView image_recipe;
        Button btn_person,btn_time,btn_date,btn_load,btn_clic;



        MenuItemViewHolder(View view) {
            super(view);
            text_title_recipe= view.findViewById(R.id.text_title_recipe);
            progress_ratting= view.findViewById(R.id.progressbar_rating);
            image_recipe= view.findViewById(R.id.recipe_photo);
            btn_person= view.findViewById(R.id.button_person);
            btn_time= view.findViewById(R.id.button_time);
            btn_date= view.findViewById(R.id.button_date);
            btn_load=view.findViewById(R.id.button_image);
            btn_clic=view.findViewById(R.id.button_clic);
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        AdViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }


    @Override
    public int getItemViewType(int position) {
        return (position % com.cokeappingo.search_activity.ITEMS_PER_AD == 0) ? BANNER_AD_VIEW_TYPE
               : MENU_ITEM_VIEW_TYPE;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.items_activity_search, viewGroup, false);
                return new MenuItemViewHolder(menuItemLayoutView);
            case BANNER_AD_VIEW_TYPE:
                // fall through
            default:
                View bannerLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.banner_ad_container,
                        viewGroup, false);

                return new AdViewHolder(bannerLayoutView);
        }
    }


    /**
     * Replaces the content in the views that make up the menu item view and the
     * banner ad view. This method is invoked by the layout manager.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object item=recyclerViewItems.get(position);
        if (item instanceof new_recipe_search) {
            MenuItemViewHolder menuItemHolder = (MenuItemViewHolder) holder;

            new_recipe_search menuItem = (new_recipe_search) recyclerViewItems.get(position);


            menuItemHolder.text_title_recipe.setText(new adapter_setting().adapter_number(menuItem.getTitle(),context));


            Log.e("Id_push",menuItem.getId_push());
            Log.e("title",menuItem.getTitle());
            rempilre_image(menuItemHolder.image_recipe, menuItem.getLien_puc(), menuItemHolder.btn_load);

            change_color(menuItemHolder.btn_load);

            new data_user_onligne().get_all_comment_recipe(menuItem.getId_push(), new data_user_onligne.get_comment() {
                @Override
                public void on_callback(ArrayList<comment> comments) {
                    if(comments.size()!=0) {
                        ((MenuItemViewHolder) holder).progress_ratting.setEndValue(5);
                        int count = 0;
                        for (comment c : comments) {
                            count += c.getValue();
                        }
                        progress_aniation(0, (count / comments.size()), ((MenuItemViewHolder) holder).progress_ratting);

                        Log.e("count",""+(count / comments.size()));
                    }
                }
            });

            int person;
            try {
                person=Integer.parseInt(menuItem.getPerson());
            }catch (NumberFormatException e){
                person=0;
            }

            switch (person){
                case 0:
                    menuItemHolder.btn_person.setText(new adapter_setting().adapter_number("غير محدد",context));
                    break;
                case 1:
                    menuItemHolder.btn_person.setText(new adapter_setting().adapter_number("تكفي لشخص واحد",context));
                    break;
                case 2:
                    menuItemHolder.btn_person.setText(new adapter_setting().adapter_number("تكفي لشخصان",context));
                    break;
                case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    menuItemHolder.btn_person.setText(new adapter_setting().adapter_number("تكفي ل "+person+" أشخاص",context));
                    break;
                default:
                    menuItemHolder.btn_person.setText(new adapter_setting().adapter_number("تكفي ل "+person+" شخصاً",context));
                    break;
            }


            String[] dates = menuItem.getDate_publication().split(System.getProperty("line.separator"));
            menuItemHolder.btn_date.setText(new adapter_setting().adapter_number(dates[0]+" "+dates[1],context));


            int time;
            try {
                time=Integer.parseInt(menuItem.getCooking_time());
            }catch (NumberFormatException e){
                time=0;
            }
            switch (time){
                case 0:
                    menuItemHolder.btn_person.setText(new adapter_setting().adapter_number("غير محدد",context));
                    break;
                case 1:
                    menuItemHolder.btn_time.setText(new adapter_setting().adapter_number("تطهى خلال دقيقة واحدة",context));
                    break;
                case 2:
                    menuItemHolder.btn_time.setText(new adapter_setting().adapter_number("تطهى خلال دقيقتان",context));
                    break;
                case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                    menuItemHolder.btn_time.setText(new adapter_setting().adapter_number("تطهى خلال "+time+" دقائق",context));
                    break;
                default:
                    menuItemHolder.btn_time.setText(new adapter_setting().adapter_number("تطهى خلال "+time+" دقيقة",context));
                    break;
            }

            menuItemHolder.btn_clic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Id_of_this recipe",menuItem.getId_push());
                    //Log.e("categories",menuItem.getCategories());
                    //Log.e("cuisine",menuItem.getCuisine());



                    start_add_recipe_activity((new_recipe_search)recyclerViewItems.get(position));
                }
            });
        }
        else if(item instanceof AdView){
            AdViewHolder bannerHolder = (AdViewHolder) holder;
            AdView adView = (AdView) recyclerViewItems.get(position);
            ViewGroup adCardView = (ViewGroup) bannerHolder.itemView;
            // The AdViewHolder recycled by the RecyclerView may be a different
            // instance than the one used previously for this position. Clear the
            // AdViewHolder of any subviews in case it has a different
            // AdView associated with it, and make sure the AdView for this position doesn't
            // already have a parent of a different recycled AdViewHolder.
            if (adCardView.getChildCount() > 0) {
                adCardView.removeAllViews();
            }
            if (adView.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }

            adView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "this ad", Toast.LENGTH_SHORT).show();
                }
            });

            // Add the banner ad to the ad view.
            adCardView.addView(adView);
        }
    }

    public void rempilre_image(final SelectableRoundedImageView imageView, String url,Button btn_load){
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap bit=new adapter_setting().getResizedBitmap_multiple(resource,context);
                        imageView.setImageBitmap(bit);
                        btn_load.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }


    int m=0;


    public void start_add_recipe_activity(new_recipe_search recipe){
        show_this_recipe fragment2=new show_this_recipe();
        Bundle args = new Bundle();
        args.putString("recipe",recipe.getId_push());
        fragment2.setArguments(args);
        AppCompatActivity activity = (AppCompatActivity)  context;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(!activity.getSupportFragmentManager().findFragmentByTag(MainActivity.class.getName()).isVisible())
            fragmentTransaction.replace(R.id.fragment_show,fragment2,"sear");
        else
            fragmentTransaction.replace(R.id.fragment_show_acceuil,fragment2,"semr");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void change_color(Button button){
        final float[] from = new float[3],
                to =   new float[3];

        if(m==0) {
            Color.colorToHSV(context.getResources().getColor(R.color.background_card), from);   // from white
            Color.colorToHSV(context.getResources().getColor(R.color.background_snack), to);     // to red
            m=1;
        }else{
            Color.colorToHSV(context.getResources().getColor(R.color.background_card),to );   // from white
            Color.colorToHSV(context.getResources().getColor(R.color.background_snack), from);     // to red
            m=0;
        }



        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);   // animate from 0 to 1
        anim.setDuration(2000);                              // for 300 ms

        final float[] hsv  = new float[3];                  // transition color
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @SuppressLint("ResourceType")
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                // Transition along each axis of HSV (hue, saturation, value)
                hsv[0] = from[0] + (to[0] - from[0])*animation.getAnimatedFraction();
                hsv[1] = from[1] + (to[1] - from[1])*animation.getAnimatedFraction();
                hsv[2] = from[2] + (to[2] - from[2])*animation.getAnimatedFraction();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ViewCompat.setBackgroundTintList(
                            button,
                            ColorStateList.valueOf(Color.HSVToColor(hsv)));
                }
            }
        });
        anim.start();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                change_color(button);
            }
        }, 2000);
    }

    public void progress_aniation(int from, int to,CustomGauge progress_download){
        ObjectAnimator animation = ObjectAnimator.ofInt(progress_download, "Value", from,to); // see this max value coming back here, we animate towards that value
        animation.setDuration(900); // in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }
}


