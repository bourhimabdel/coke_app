package com.cokeappingo.local_notification_manger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.cokeappingo.Add_new_recipe;
import com.cokeappingo.R;
import com.cokeappingo.SendNotificationPack.MyFireBaseMessagingService;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.praincipal_activity;
import com.cokeappingo.splach_screen;
import com.cokeappingo.sql_lite_manager.sql_manager;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

public class noti_upload_data {

    Context context;
    private NotificationManager notificationManager;

    public noti_upload_data(Context context){
        this.context=context;
        notificationManager = (NotificationManager)  context.getSystemService(NOTIFICATION_SERVICE);
    }


    public void notif_upload(int max_progress,int progress){

        Cursor result=new sql_manager(context).getAllData_setting();
        result.moveToPosition(3);

        if(result.getString(2).equals("غير مفعلة"))
            return;


            String CHANNEL_ID = "my_channel_02";// The id of the channel.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.channel_notif_upload);// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setSound(null,null);

                notificationManager.createNotificationChannel(mChannel);
            }


            Notification notification =
                    new NotificationCompat.Builder(context)
                            .setContentTitle("يتم رفع البيانات ...")
                            .setNumber(12)
                            .setColor(context.getResources().getColor(R.color.notification_upload))
                            .setSmallIcon(R.drawable.ic_recipe_data_uploaded)
                            .setProgress(max_progress, progress, false)
                            .setSound(null)
                            .setChannelId(CHANNEL_ID).build();

        //if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    notification.setSmallIcon(R.drawable.icon_transperent);
        //    notification.setColor(getResources().getColor(R.color.notification_color));
        //} else {
        //    notification.setSmallIcon(R.drawable.icon);
        //}



        notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear  the notification
            //notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
            //notification.defaults |= Notification.DEFAULT_VIBRATE;//Vibration
            //notification.defaults |= Notification.DEFAULT_SOUND; // Sound


            notificationManager.notify(12, notification);

        Intent intent;
        PendingIntent pendingIntent;

        intent = new Intent(context, praincipal_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            if (max_progress==progress){
                notificationManager.cancel(12);
                Notification notificationL =
                        new NotificationCompat.Builder(context)
                                .setContentTitle("تم رفع البيانات بنجاح")
                                .setNumber(13)
                                .setColor(context.getResources().getColor(R.color.notification_accepted))
                                .setSmallIcon(R.drawable.ic_recipe_accepted)
                                .setSound(null)
                                .setContentIntent(pendingIntent)
                                .setChannelId(CHANNEL_ID).build();

                notification.defaults |= Notification.DEFAULT_VIBRATE;//Vibration
                notificationManager.notify(13, notificationL);
            }
    }


    public void notification_morning(){
        String CHANNEL_ID = "my_channel_03";// The id of the channel.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_notif_upload);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setSound(null,null);

            notificationManager.createNotificationChannel(mChannel);
        }

        String text=" نتمنى لك يوم ";

        Date date=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        switch(dayOfWeek)
        {
            case 1:
                text+="أحد";
                break;
            case 2:
                text+="اثنين";
                break;
            case 3:
                text+="ثلاثاء";
                break;
            case 4:
                text+="أربعاء";
                break;
            case 5:
                text+="خميس";
                break;
            case 6:
                text+="جمعة";
                break;
            default:
                text+="سبت";
                break;
        }

        text+=" رائع ";

        Intent intent;
        PendingIntent pendingIntent;

        intent = new Intent(context, splach_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notification =
                new NotificationCompat.Builder(context)
                        .setContentTitle(" صباح الخير " + new sql_manager(context).get__account().getName())
                        .setContentText(text)
                        .setNumber(2)
                        .setColor(context.getResources().getColor(R.color.notification_refuse))
                        .setSmallIcon(R.drawable.ic_morning)
                        .setContentIntent(pendingIntent)
                        .setSound(null)
                        .setChannelId(CHANNEL_ID).build();

        //if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    notification.setSmallIcon(R.drawable.icon_transperent);
        //    notification.setColor(getResources().getColor(R.color.notification_color));
        //} else {
        //    notification.setSmallIcon(R.drawable.icon);
        //}





        //notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear  the notification
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE;//Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound


        notificationManager.notify(2, notification);
    }
}
