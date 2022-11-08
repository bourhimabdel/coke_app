package com.cokeappingo.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAtxw48jI:APA91bEl81sh0FydqGnmRDdnPoxy8_Mhf_7bl-S3rXn5Z7TmhCXS5wA-b6sf_yH0pCtt4rV-UKQUHp8yyGk-4LqcCmljJexBGlHkGQGDSa4Vea2y8ayjSdgRXBMKrHww6ALiWdv6w30d" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

