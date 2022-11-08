package com.cokeappingo.liste_view_acceuil;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.R;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.sql_lite_manager.save_data_in_first_open;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.joooonho.SelectableRoundedImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
//extends PagerAdapter
public class liste_adapter  {
/*

    private List<catugorie> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public liste_adapter(List<catugorie> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.items_accuil_page,container,false);



        SelectableRoundedImageView image1,image2,image3,image4,image5,image6;
        TextView text1,text2,text3,text4,text5,text6;
        LottieAnimationView lottie_load1,lottie_load2,lottie_load3,lottie_load4,lottie_load5,lottie_load6;

        image1 = view.findViewById(R.id.image);
        image2 = view.findViewById(R.id.image2);
        image3 = view.findViewById(R.id.image3);
        image4 = view.findViewById(R.id.image4);
        image5 = view.findViewById(R.id.image5);
        image6 = view.findViewById(R.id.image6);

        text1 = view.findViewById(R.id.title);
        text2 = view.findViewById(R.id.title2);
        text3 = view.findViewById(R.id.title3);
        text4 = view.findViewById(R.id.title4);
        text5 = view.findViewById(R.id.title5);
        text6 = view.findViewById(R.id.title6);

        lottie_load1= view.findViewById(R.id.lottie_load1);
        lottie_load2= view.findViewById(R.id.lottie_load2);
        lottie_load3= view.findViewById(R.id.lottie_load3);
        lottie_load4= view.findViewById(R.id.lottie_load4);
        lottie_load5= view.findViewById(R.id.lottie_load5);
        lottie_load6= view.findViewById(R.id.lottie_load6);

        final CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6;

        cardView1=view.findViewById(R.id.cardview);
        cardView2=view.findViewById(R.id.cardview2);
        cardView3=view.findViewById(R.id.cardview3);
        cardView4=view.findViewById(R.id.cardview4);
        cardView5=view.findViewById(R.id.cardview5);
        cardView6=view.findViewById(R.id.cardview6);

        switch (position) {
            case 0:
                rempilre_image(text1,image1,models.get(0).getUrl(),models.get(0).getId(),lottie_load1);
                text1.setText(new adapter_setting().adapter_number(models.get(0).getNom());
                rempilre_image(text2,image2,models.get(1).getUrl(),models.get(1).getId(),lottie_load2);
                text2.setText(new adapter_setting().adapter_number(models.get(1).getNom());
                rempilre_image(text3,image3,models.get(2).getUrl(),models.get(2).getId(),lottie_load3);
                text3.setText(new adapter_setting().adapter_number(models.get(2).getNom());
                rempilre_image(text4,image4,models.get(3).getUrl(),models.get(3).getId(),lottie_load4);
                text4.setText(new adapter_setting().adapter_number(models.get(3).getNom(),context));
                rempilre_image(text5,image5,models.get(4).getUrl(),models.get(4).getId(),lottie_load5);
                text5.setText(new adapter_setting().adapter_number(models.get(4).getNom());
                rempilre_image(text6,image6,models.get(5).getUrl(),models.get(5).getId(),lottie_load6);
                text6.setText(new adapter_setting().adapter_number(models.get(5).getNom());
                break;
            case 1:
                rempilre_image(text1,image1,models.get(6).getUrl(),models.get(6).getId(),lottie_load1);
                text1.setText(new adapter_setting().adapter_number(models.get(6).getNom());
                rempilre_image(text2,image2,models.get(7).getUrl(),models.get(7).getId(),lottie_load2);
                text2.setText(new adapter_setting().adapter_number(models.get(7).getNom());
                rempilre_image(text3,image3,models.get(8).getUrl(),models.get(8).getId(),lottie_load3);
                text3.setText(new adapter_setting().adapter_number(models.get(8).getNom());
                rempilre_image(text4,image4,models.get(9).getUrl(),models.get(9).getId(),lottie_load4);
                text4.setText(new adapter_setting().adapter_number(models.get(9).getNom());
                rempilre_image(text5,image5,models.get(10).getUrl(),models.get(10).getId(),lottie_load5);
                text5.setText(new adapter_setting().adapter_number(models.get(10).getNom());
                rempilre_image(text6,image6,models.get(11).getUrl(),models.get(11).getId(),lottie_load6);
                text6.setText(new adapter_setting().adapter_number(models.get(11).getNom());
                break;
            case 2:
                rempilre_image(text1,image1,models.get(12).getUrl(),models.get(12).getId(),lottie_load1);
                text1.setText(new adapter_setting().adapter_number(models.get(12).getNom());
                rempilre_image(text2,image2,models.get(13).getUrl(),models.get(13).getId(),lottie_load2);
                text2.setText(new adapter_setting().adapter_number(models.get(13).getNom());
                rempilre_image(text3,image3,models.get(14).getUrl(),models.get(14).getId(),lottie_load3);
                text3.setText(new adapter_setting().adapter_number(models.get(14).getNom());
                rempilre_image(text4,image4,models.get(15).getUrl(),models.get(15).getId(),lottie_load4);
                text4.setText(new adapter_setting().adapter_number(models.get(15).getNom());
                rempilre_image(text5,image5,models.get(16).getUrl(),models.get(16).getId(),lottie_load5);
                text5.setText(new adapter_setting().adapter_number(models.get(16).getNom());
                rempilre_image(text6,image6,models.get(17).getUrl(),models.get(17).getId(),lottie_load6);
                text6.setText(new adapter_setting().adapter_number(models.get(17).getNom());
                break;
            case 3:
                rempilre_image(text1,image1,models.get(18).getUrl(),models.get(18).getId(),lottie_load1);
                text1.setText(new adapter_setting().adapter_number(models.get(18).getNom());
                rempilre_image(text2,image2,models.get(19).getUrl(),models.get(19).getId(),lottie_load2);
                text2.setText(new adapter_setting().adapter_number(models.get(19).getNom());
                rempilre_image(text3,image3,models.get(20).getUrl(),models.get(20).getId(),lottie_load3);
                text3.setText(new adapter_setting().adapter_number(models.get(20).getNom());
                rempilre_image(text4,image4,models.get(21).getUrl(),models.get(21).getId(),lottie_load4);
                text4.setText(new adapter_setting().adapter_number(models.get(21).getNom());
                rempilre_image(text5,image5,models.get(22).getUrl(),models.get(22).getId(),lottie_load5);
                text5.setText(new adapter_setting().adapter_number(models.get(22).getNom());
                rempilre_image(text6,image6,models.get(23).getUrl(),models.get(23).getId(),lottie_load6);
                text6.setText(new adapter_setting().adapter_number(models.get(23).getNom());
                break;
            case 4:
                rempilre_image(text1,image1,models.get(24).getUrl(),models.get(24).getId(),lottie_load1);
                text1.setText(new adapter_setting().adapter_number(models.get(24).getNom());
                rempilre_image(text2,image2,models.get(25).getUrl(),models.get(25).getId(),lottie_load2);
                text2.setText(new adapter_setting().adapter_number(models.get(25).getNom());
                rempilre_image(text3,image3,models.get(26).getUrl(),models.get(26).getId(),lottie_load3);
                text3.setText(new adapter_setting().adapter_number(models.get(26).getNom());
                rempilre_image(text4,image4,models.get(27).getUrl(),models.get(27).getId(),lottie_load4);
                text4.setText(new adapter_setting().adapter_number(models.get(27).getNom());
                rempilre_image(text5,image5,models.get(28).getUrl(),models.get(28).getId(),lottie_load5);
                text5.setText(new adapter_setting().adapter_number(models.get(28).getNom());
                rempilre_image(text6,image6,models.get(29).getUrl(),models.get(29).getId(),lottie_load6);
                text6.setText(new adapter_setting().adapter_number(models.get(29).getNom());
                break;
            case 5:
                rempilre_image(text1,image1,models.get(30).getUrl(),models.get(30).getId(),lottie_load1);
                text1.setText(new adapter_setting().adapter_number(models.get(30).getNom());
                rempilre_image(text2,image2,models.get(31).getUrl(),models.get(31).getId(),lottie_load2);
                text2.setText(new adapter_setting().adapter_number(models.get(31).getNom());
                rempilre_image(text3,image3,models.get(32).getUrl(),models.get(32).getId(),lottie_load3);
                text3.setText(new adapter_setting().adapter_number(models.get(32).getNom());
                rempilre_image(text4,image4,models.get(33).getUrl(),models.get(33).getId(),lottie_load4);
                text4.setText(new adapter_setting().adapter_number(models.get(33).getNom());
                break;

        }

       if(position==5){
           cardView5.setVisibility(View.GONE);
           cardView6.setVisibility(View.GONE);
       }

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


    public void rempilre_image(final TextView txt,final ImageView imageView, String url, final int id,final LottieAnimationView lottie_load){
        try {
            sql_manager db = new sql_manager(context);
            Cursor cursor=db.getAllData_Image(String.valueOf(id));
            byte[] b=null;
            while (cursor.moveToNext()){
                b=cursor.getBlob(1);}
            Bitmap decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length);
            imageView.setImageBitmap(decodedByte);
            lottie_load.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);

        }
        catch (Exception e){
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    //    .override((int)(width_screen) , (int)(height_screen*0.5) )
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                            lottie_load.setVisibility(View.INVISIBLE);
                            txt.setVisibility(View.VISIBLE);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                            byte[] b = baos.toByteArray();

                            sql_manager db = new sql_manager(context);
                            db.insert_data_into_IMAGE(String.valueOf(id),b);
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        }

 */
    }

