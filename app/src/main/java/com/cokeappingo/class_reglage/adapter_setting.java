package com.cokeappingo.class_reglage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.cokeappingo.MainActivity;
import com.cokeappingo.R;
import com.cokeappingo.setting;
import com.cokeappingo.sql_lite_manager.sql_manager;

public class adapter_setting {

    public void set_setting_theme(Context context){
        Cursor result=new sql_manager(context).getAllData_setting();
        result.moveToPosition(0);
         switch (result.getString(2)){
             case "وضع ليلي":
                 AppCompatDelegate .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                 break;
             case "وضع نهاري":
                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                 break;
             case "تلقائيا":
                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                 break;
         }

        }

    public void set_setting_text(Context context){
        Cursor result=new sql_manager(context).getAllData_setting();
        result.moveToPosition(2);
        switch (result.getString(2)){
            case "شكل 1":
                context.setTheme(R.style.AppTheme_NotificationBar);
                break;
            case "شكل 2":
                context.setTheme(R.style.AppTheme_NotificationBar2);
                break;
            case "شكل 3":
                context.setTheme(R.style.AppTheme_NotificationBar3);
                break;
            case "شكل 4":
                context.setTheme(R.style.AppTheme_NotificationBar4);
                break;
            case "شكل 5":
                context.setTheme(R.style.AppTheme_NotificationBar5);
                break;
        }


    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public Bitmap getResizedBitmap_added_to_fire_base(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        int newWidth,newHeight;

        if(width>1000)
        if (width/2<=1000){
            newWidth=width/2;
            newHeight=height/2;
        }else if(width/4<=1000){
            newWidth=width/4;
            newHeight=height/4;
        }else {
            newWidth=width/6;
            newHeight=height/6;
        }else {
            newWidth=width;
            newHeight=height;
        }

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public Bitmap getResizedBitmap_multiple(Bitmap bm,Context context) {
       try {
           int width = bm.getWidth();
           int height = bm.getHeight();

           int newWidth,newHeight;


           DisplayMetrics displayMetrics = new DisplayMetrics();
           ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
           int height_screen = displayMetrics.heightPixels;
           int width_screen = displayMetrics.widthPixels;


           double d=(double)width_screen/(double)width;
           newWidth = (int) (width*d);
           newHeight = (int) (height*d);

           float scaleWidth = ((float) newWidth) / width;
           float scaleHeight = ((float) newHeight) / height;
           // CREATE A MATRIX FOR THE MANIPULATION
           Matrix matrix = new Matrix();
           // RESIZE THE BIT MAP
           matrix.postScale(scaleWidth, scaleHeight);

           // "RECREATE" THE NEW BITMAP
           Bitmap resizedBitmap = Bitmap.createBitmap(
                   bm, 0, 0, width, height, matrix, false);
           return resizedBitmap;
       }catch (NullPointerException e){
           return null;
       }


    }

    public String adapter_number(String original,Context context){
        Cursor result=new sql_manager(context).getAllData_setting();
        result.moveToPosition(1);
        if(original==null)
            return "";
        if(result.getString(2).equals("عجمي")) {
            return original.replaceAll("٠", "0")
                    .replaceAll("١", "1")
                    .replaceAll("٢", "2")
                    .replaceAll("٣", "3")
                    .replaceAll("٤", "4")
                    .replaceAll("٥", "5")
                    .replaceAll("٦", "6")
                    .replaceAll("٧", "7")
                    .replaceAll("٨", "8")
                    .replaceAll("٩", "9");
        }else {
            return original.replaceAll("0","٠")
                    .replaceAll("1","١")
                    .replaceAll("2","٢")
                    .replaceAll("3","٣")
                    .replaceAll("4","٤")
                    .replaceAll("5","٥")
                    .replaceAll("6","٦")
                    .replaceAll("7","٧")
                    .replaceAll("8","٨")
                    .replaceAll("9","٩");
        }
    }



}

