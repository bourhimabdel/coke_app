package com.cokeappingo.SendNotificationPack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cokeappingo.Add_new_recipe;
import com.cokeappingo.MainActivity;
import com.cokeappingo.R;
import com.cokeappingo.account_activity;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.praincipal_activity;
import com.cokeappingo.splach_screen;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String id_push,date,status,cause_refuse;
    private NotificationManager notificationManager;
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);
            id_push=remoteMessage.getData().get("id_push");
            date=remoteMessage.getData().get("date");
            status=remoteMessage.getData().get("status");
            cause_refuse=remoteMessage.getData().get("cause_refuse");

            notificationManager = (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);


            if (status.equals("ok")){

                String CHANNEL_ID = "my_channel_01";// The id of the channel.

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    notificationManager.createNotificationChannel(mChannel);
                }


                 new_recipe recipe=new sql_manager(MyFireBaseMessagingService.this).getData_recipe_user_by_id_push(id_push);

                byte[] image_recipe=recipe.getImage();
                Bitmap decodedByte = BitmapFactory.decodeByteArray(image_recipe, 0, image_recipe.length);

                Intent intent;
                PendingIntent pendingIntent;

                intent = new Intent(this, splach_screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                Notification notification =
                        new NotificationCompat.Builder(this)
                                .setContentTitle("تمت الموافقة بنجاح")
                                //.setContentText("هنيئا لكم لقد تم نشر وصفتكم تحت عنوان '"+recipe.getTitle()+"'"+" بتاريخ "+"'"+date+"'"+" بنجاح. ")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(Html.fromHtml("هنيئا لكم لقد تم نشر وصفتكم تحت عنوان "+"<font color='#00F17B'>"+recipe.getTitle()+"</font>")))
                                .setContentText(Html.fromHtml("هنيئا لكم لقد تم نشر وصفتكم تحت عنوان "+"<font color='#00F17B'>"+recipe.getTitle()+"</font>"))
                                .setLargeIcon(decodedByte)
                                .setNumber(11)
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(R.drawable.ic_recipe_accepted)
                                .setColor(this.getResources().getColor(R.color.notification_accepted))
                                .setChannelId(CHANNEL_ID).build();


                //notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear  the notification
                notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
                notification.defaults |= Notification.DEFAULT_VIBRATE;//Vibration
                notification.defaults |= Notification.DEFAULT_SOUND; // Sound

                recipe.setStatus_recipe("ok");
                recipe.setDate_publication(date);
                new sql_manager(MyFireBaseMessagingService.this).Update_data_recipe(recipe);


                notificationManager.notify(11,notification);


                broadcaster.sendBroadcast(new Intent("MyData"));


            }else if (status.equals("comment")){
                String CHANNEL_ID = "my_channel_01";// The id of the channel.

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    notificationManager.createNotificationChannel(mChannel);
                }


                new_recipe recipe=new sql_manager(MyFireBaseMessagingService.this).getData_recipe_user_by_id_push(id_push);

                byte[] image_recipe=recipe.getImage();
                Bitmap decodedByte = BitmapFactory.decodeByteArray(image_recipe, 0, image_recipe.length);

                Intent intent;
                PendingIntent pendingIntent;

                intent = new Intent(this, praincipal_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                int value=Integer.parseInt(date);
                String m="";

                switch (value){
                    case 1:
                        m="بنجمة وحدة";
                        break;
                    case 2:
                        m="بنجمتين";
                        break;
                    case 3: case 4: case 5:
                        m="ب"+value+" نجوم ";
                        break;
                }

                Notification notification =
                        new NotificationCompat.Builder(this)
                                .setContentTitle("تقييم")
                                //.setContentText("هنيئا لكم لقد تم نشر وصفتكم تحت عنوان '"+recipe.getTitle()+"'"+" بتاريخ "+"'"+date+"'"+" بنجاح. ")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(Html.fromHtml("تم تقييم وصفتكم تحت عنوان  "+"<font color='#ECE118'>"+recipe.getTitle()+"</font>"+" "+m+" من طرف "+cause_refuse)))
                                .setContentText(Html.fromHtml("تم تقييم وصفتكم تحت عنوان  "+"<font color='#ECE118'>"+recipe.getTitle()+"</font>"+" "+m+" من طرف "+cause_refuse))
                                .setLargeIcon(decodedByte)
                                .setNumber(11)
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(R.drawable.ic_recipe_comment)
                                .setColor(this.getResources().getColor(R.color.notification_comment))
                                .setChannelId(CHANNEL_ID).build();


                notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
                notification.defaults |= Notification.DEFAULT_VIBRATE;//Vibration
                notification.defaults |= Notification.DEFAULT_SOUND; // Sound

                notificationManager.notify(11,notification);
            }
            else if (status.equals("title"))
            {
                new sql_manager(this).insert_data_into_TABLE_ALL_TITLE(id_push,date);
            }
            else {
                String CHANNEL_ID = "my_channel_01";// The id of the channel.

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    notificationManager.createNotificationChannel(mChannel);
                }


                new_recipe recipe=new sql_manager(MyFireBaseMessagingService.this).getData_recipe_user_by_id_push(id_push);

                byte[] image_recipe=recipe.getImage();
                Bitmap decodedByte = BitmapFactory.decodeByteArray(image_recipe, 0, image_recipe.length);

                Intent intent;
                PendingIntent pendingIntent;

                intent = new Intent(this, praincipal_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                Notification notification =
                        new NotificationCompat.Builder(this)
                                .setContentTitle("للاسف تم رفض الوصفة")
                                //.setContentText("هنيئا لكم لقد تم نشر وصفتكم تحت عنوان '"+recipe.getTitle()+"'"+" بتاريخ "+"'"+date+"'"+" بنجاح. ")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(Html.fromHtml("للاسف لقد تم رفض وصفتكم تحت عنوان "+"<font color='#E83838'>"+recipe.getTitle()+"</font>")))
                                .setContentText(Html.fromHtml("للاسف لقد تم رفض وصفتكم تحت عنوان "+"<font color='#E83838'>"+recipe.getTitle()+"</font>"))
                                .setLargeIcon(decodedByte)
                                .setNumber(11)
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(R.drawable.ic_recipe_refuse)
                                .setColor(this.getResources().getColor(R.color.notification_refuse))
                                .setChannelId(CHANNEL_ID).build();

                recipe.setStatus_recipe("refused");
                recipe.setCause_refuse(cause_refuse);
                recipe.setDate_publication(date);
                new sql_manager(MyFireBaseMessagingService.this).Update_data_recipe(recipe);

                notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
                notification.defaults |= Notification.DEFAULT_VIBRATE;//Vibration
                notification.defaults |= Notification.DEFAULT_SOUND; // Sound

                notificationManager.notify(11,notification);
                broadcaster.sendBroadcast(new Intent("MyData"));
            }

    }



}
