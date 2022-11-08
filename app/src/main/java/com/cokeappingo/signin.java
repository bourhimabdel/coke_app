package com.cokeappingo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.SendNotificationPack.Token;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.account;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.notification_morning.Alarm;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.cokeappingo.user_info.user_info;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.joooonho.SelectableRoundedImageView;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class signin extends AppCompatActivity {

    Button fb_LOG,google_LOG;
    TextView sign_in;
    CallbackManager mCallbackManager;

    private FirebaseAuth mAuth_f;

    LottieAnimationView lottie_download;
    CustomGauge progress_download;
    TextView t1,t2;

    ///objet_login_google//////////////////////
    private GoogleSignInClient mgoogleSignInClient;
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN=1;
    ////////////////////////




    @Override
    protected void onCreate(Bundle savedInstanceState){
        new adapter_setting().set_setting_text(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);







        ///objet_login_google//////////////////////
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this,gso);

        ////////////////////////////////


        FacebookSdk.sdkInitialize(getApplicationContext());
        mAuth_f = FirebaseAuth.getInstance();


        ///Hide_bottom_bar//////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        else
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        ///////////////////////////////////////

        sign_in=findViewById(R.id.sign);
        fb_LOG=findViewById(R.id.fb_log);
        google_LOG=findViewById(R.id.google_log);
        lottie_download = findViewById(R.id.lottie_download);
        progress_download = findViewById(R.id.progressbar);
        t1= findViewById(R.id.pourcentage);
        t2= findViewById(R.id.pourcent);


        google_LOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        mCallbackManager = CallbackManager.Factory.create();
        fb_LOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(signin.this,
                        Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("FB", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("FB", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("FB", "facebook:onError", error);
                    }
                });
            }
        });
    }

    //////////// Object Login Facebook /////////////
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FB", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hide();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FB", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            UpdateToken(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FB", "signInWithCredential:failure", task.getException());
                            Toast.makeText(signin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }





    ///objet_login_google//////////////////////
    public void signIn() {
        Intent signInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            super.onActivityResult(requestCode, resultCode, data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInresult(task);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInresult(Task<GoogleSignInAccount> task) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            sign_in.setText("يتم تسجيل الدخول ...");
            sign_in.setClickable(false);
            firebaseAuthWithGoogle(account);
           // UpdateToken();
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Toast.makeText(this, "حاول مرة اخرى", Toast.LENGTH_SHORT).show();
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            hide();
                            UpdateToken(user);
                         //   startActivity(new Intent(signin.this,MainActivity.class));
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }

    private void UpdateToken(FirebaseUser firebaseUser){
        //FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        progress_download.setEndValue(5);
        progress_download.setVisibility(View.VISIBLE);
        t1.setVisibility(View.VISIBLE);
        t1.setText("0");
        t2.setVisibility(View.VISIBLE);
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseFirestore.getInstance().document("Tokens/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).set(token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                progress_aniation(0, 1, new account_activity.endanimation() {
                    @Override
                    public void oncallback() {

                        t1.setText("20");

                                progress_aniation(1, 2, new account_activity.endanimation() {
                                    @Override
                                    public void oncallback() {
                                        t1.setText("40");
                                        final account account = new user_info().get_info_account(GoogleSignIn.getLastSignedInAccount(signin.this));
                                        new data_user_onligne().if_user_exist(account.getAccount_ID(), new data_user_onligne.user_exist() {
                                            @Override
                                            public void exist(boolean etat, final account r) {
                                                if(etat){
                                                    if(r.getNumber_recipe()!=0) {
                                                        remplire_Image(true,signin.this,r);
                                                        progress_aniation(2, 3, new account_activity.endanimation() {
                                                            @Override
                                                            public void oncallback() {
                                                                t1.setText("60");
                                                                final int[] count={0};
                                                                new data_user_onligne().get_all_key_user_recipe(r.getAccount_ID(), new data_user_onligne.key_user_recipe() {
                                                                    @Override
                                                                    public void on_callback(final ArrayList<String> keys){
                                                                        progress_aniation(3, 4, new account_activity.endanimation() {
                                                                            @Override
                                                                            public void oncallback() {
                                                                                t1.setText("80");
                                                                                Log.e("gg",keys.size()+"");
                                                                                for (String key:keys){
                                                                                    new data_user_onligne().retrive_recipe_user_by_KEY(key, new data_user_onligne.retrive_recipe() {
                                                                                        @Override
                                                                                        public void onCallback(new_recipe recipe) {
                                                                                            count[0]++;
                                                                                            if(recipe != null)
                                                                                                new sql_manager(signin.this).insert_data_into_recipe(recipe);

                                                                                            Log.e("gg",recipe.getIngredient().toString());
                                                                                            if(count[0]==r.getNumber_recipe()) {
                                                                                                ////////////// animation
                                                                                                sign_in.setText("تم تسجيل الدخول بنجاح");
                                                                                                progress_aniation(4, 5, new account_activity.endanimation() {
                                                                                                    @Override
                                                                                                    public void oncallback() {
                                                                                                        t1.setText("100");
                                                                                                        t1.setVisibility(View.INVISIBLE);
                                                                                                        t2.setVisibility(View.INVISIBLE);
                                                                                                        lottie_download.setVisibility(View.VISIBLE);
                                                                                                        lottie_download.playAnimation();
                                                                                                        progress_download.setVisibility(View.INVISIBLE);
                                                                                                        Handler handler=new Handler();
                                                                                                        handler.postDelayed(new Runnable() {
                                                                                                            public void run() {
                                                                                                                startActivity(new Intent(signin.this,praincipal_activity.class));
                                                                                                            }
                                                                                                        }, 2000);
                                                                                                    }
                                                                                                });
                                                                                            }

                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                            }
                                                        });

                                                    }else {

                                                        remplire_Image(true, signin.this, r);

                                                    }
                                                }else{
                                                    account.setNumber_recipe(0);
                                                    remplire_Image(false,signin.this,account);
                                                }
                                            }
                                        });

                                    }
                                });



                    }
                });

            }
        });
    }


    private void updateUI(FirebaseUser user) {
        if(user!=null){
            sign_in.setText("يتم تسجيل الدخول ...");
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    public void progress_aniation(int from, int to, final account_activity.endanimation endanimation){
        ObjectAnimator animation = ObjectAnimator.ofInt(progress_download, "Value", from,to); // see this max value coming back here, we animate towards that value
        animation.setDuration(750); // in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                endanimation.oncallback();
            }
        }, 750);
    }

    public void remplire_Image(final boolean etat, final Context context, final account account){
        Glide.with(context)
                .asBitmap()
                .load(account.getImage())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 30, baos); // bm is the bitmap object
                        final byte[] b = baos.toByteArray();

                        if (etat){

                            if(account.getNumber_recipe()!=0){

                                sql_manager db = new sql_manager(context);
                                db.insert_data_into_account(account.getAccount_ID(),account.getName(),account.getBio(),b, account.getNumber_recipe(),account.getImage());
                                //Toast.makeText(signin.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();

                            }else {
                                sign_in.setText("تم تسجيل الدخول بنجاح");
                                t1.setText("100");
                                progress_aniation(2, 5, new account_activity.endanimation() {
                                    @Override
                                    public void oncallback() {
                                        t1.setVisibility(View.INVISIBLE);
                                        t2.setVisibility(View.INVISIBLE);;
                                        sql_manager db = new sql_manager(context);
                                        db.insert_data_into_account(account.getAccount_ID(),account.getName(),account.getBio(),b, account.getNumber_recipe(),account.getImage());
                                      //  Toast.makeText(signin.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                                        lottie_download.setVisibility(View.VISIBLE);
                                        lottie_download.playAnimation();
                                        progress_download.setVisibility(View.INVISIBLE);
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                startActivity(new Intent(signin.this,praincipal_activity.class));
                                            }
                                        }, 2000);

                                    }
                                });
                            }

                        }else{
                            progress_aniation(2, 3, new account_activity.endanimation() {
                                @Override
                                public void oncallback() {
                                    t1.setText("60");
                                    new data_user_onligne().add_new_user(account, new data_user_onligne.add_succeful() {
                                        @Override
                                        public void add_complet(boolean etat) {
                                            sign_in.setText("تم تسجيل الدخول بنجاح");
                                            progress_aniation(3, 5, new account_activity.endanimation() {
                                                @Override
                                                public void oncallback() {
                                                    t1.setText("100");
                                                    t1.setVisibility(View.INVISIBLE);
                                                    t2.setVisibility(View.INVISIBLE);
                                                    sql_manager db = new sql_manager(context);
                                                    db.insert_data_into_account(account.getAccount_ID(),account.getName(),account.getBio(),b,0,account.getImage());
                                                   // Toast.makeText(signin.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                                                    lottie_download.setVisibility(View.VISIBLE);
                                                    lottie_download.playAnimation();
                                                    progress_download.setVisibility(View.INVISIBLE);
                                                    Handler handler=new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        public void run() {
                                                            startActivity(new Intent(signin.this,praincipal_activity.class));
                                                        }
                                                    }, 2000);
                                                }

                                            });
                                        }
                                    });
                                }
                            });

                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void hide(){
        fb_LOG.setVisibility(View.INVISIBLE);
        google_LOG.setVisibility(View.INVISIBLE);
        sign_in.setVisibility(View.VISIBLE);
    }
}