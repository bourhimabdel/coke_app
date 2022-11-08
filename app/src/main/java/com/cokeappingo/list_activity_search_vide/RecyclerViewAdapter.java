package com.cokeappingo.list_activity_search_vide;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.R;
import com.cokeappingo.class_reglage.adapter_setting;
import com.google.android.gms.ads.AdView;
import com.google.android.material.transition.Hold;
import com.joooonho.SelectableRoundedImageView;

import java.util.HashMap;
import java.util.List;

import pl.pawelkleczkowski.customgauge.CustomGauge;

/**
 * The {@link RecyclerViewAdapter} class.
 * <p>The adapter provides access to the items in the {@link MenuItemViewHolder}

 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // An Activity's Context.
    private final Context context;

    // The list of banner ads and menu items.
    private final List<Object> recyclerViewItems;


    public RecyclerViewAdapter(Context context, List<Object> recyclerViewItems) {
        this.context = context;
        this.recyclerViewItems = recyclerViewItems;
    }




    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        Button a,b,c,d,e,f;
        MenuItemViewHolder(View view) {
            super(view);
            a=view.findViewById(R.id.button_title);
            b=view.findViewById(R.id.button_image);
            c=view.findViewById(R.id.button_start);
            d=view.findViewById(R.id.button_person);
            e=view.findViewById(R.id.button_time);
            f=view.findViewById(R.id.button_date);

            items.put(a,0);
            items.put(b,0);
            items.put(c,0);
            items.put(d,0);
            items.put(e,0);
            items.put(f,0);

        }
    }


    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
      View menuItemLayoutView = LayoutInflater.from(context).inflate(R.layout.items_activity_search_vide, viewGroup, false);
      return new MenuItemViewHolder(menuItemLayoutView);
    }


    /**
     * Replaces the content in the views that make up the menu item view and the
     * banner ad view. This method is invoked by the layout manager.
     */
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object item=recyclerViewItems.get(position);

        holder.setIsRecyclable(false);
        MenuItemViewHolder a=(MenuItemViewHolder) holder;

        Log.e("this line",(String)item);
        change_color(a.a);
        change_color(a.b);
        change_color(a.c);
        change_color(a.d);
        change_color(a.e);
        change_color(a.f);

    }

    HashMap<Button,Integer> items=new HashMap<>();


    public void change_color(Button button){
        final float[] from = new float[3],
                to =   new float[3];

        if(items.get(button)==0) {
            Color.colorToHSV(context.getResources().getColor(R.color.background_card), from);   // from white
            Color.colorToHSV(context.getResources().getColor(R.color.background_snack), to);     // to red
           items.remove(button);
           items.put(button,1);
        }else{
            Color.colorToHSV(context.getResources().getColor(R.color.background_card),to );   // from white
            Color.colorToHSV(context.getResources().getColor(R.color.background_snack), from);     // to red
            items.remove(button);
            items.put(button,0);
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

    public interface colored{
        void oncallback(boolean all_is_coroled);
    }
}


