package com.cokeappingo;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.cokeappingo.FIRE_BASE_DATA.data_user_onligne;
import com.cokeappingo.class_reglage.adapter_setting;
import com.cokeappingo.class_utile.account;
import com.cokeappingo.local_notification_manger.noti_upload_data;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.google.firebase.auth.FirebaseAuth;
import com.joooonho.SelectableRoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import pl.pawelkleczkowski.customgauge.CustomGauge;

import static android.app.Activity.RESULT_OK;

public class edit_info_user extends Fragment {

    ImageView check_title,check_description;
    EditText edt_title,edt_description;
    ImageButton btn_dimiss;
    SelectableRoundedImageView image_profile;
    Button btn_update;
    TextView txv_count_title,txv_count_description;
    account account;

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;

    private Uri mCropImageUri,IMAGE_CROPING;
    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myLayout = inflater.inflate(R.layout.edit_user_info_snack_bar, container, false);







        check_title=myLayout.findViewById(R.id.check_title);
        check_description=myLayout.findViewById(R.id.check_description);
        edt_title=myLayout.findViewById(R.id.input_title);
        edt_description=myLayout.findViewById(R.id.input_description);
        image_profile=myLayout.findViewById(R.id.image_image_recipe);
        btn_update=myLayout.findViewById(R.id.btn_edit);
        txv_count_title=myLayout.findViewById(R.id.title_count);
        txv_count_description=myLayout.findViewById(R.id.description_count);
        btn_dimiss=myLayout.findViewById(R.id.btn_cancel);

        //////////////////////////////////////////////////////////////////////////////////// GET INFO USER
        sql_manager db = new sql_manager(getContext());
        account=db.get__account();
        byte[] tof_saved=account.getPhoto_saved();
        Bitmap decodedByte = BitmapFactory.decodeByteArray(tof_saved, 0, tof_saved.length);
        image_profile.setImageBitmap(decodedByte);
        edt_title.setText(new adapter_setting().adapter_number(account.getName(),getContext()));
        edt_description.setText(new adapter_setting().adapter_number(account.getBio(),getContext()));
        /////////////////////////////////////////////////////////////////////////////////////



        /////////////////////////////////////////////////////////////////////image

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                   requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                } else {
                    onSelectImageClick();
                    data_uplaoded=false;
                }

            }
        });


        ////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////// title
        edt_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txv_count_title.setVisibility(View.VISIBLE);
                txv_count_title.setText(new adapter_setting().adapter_number(25 - s.toString().length() + "/25",getContext()));
            }
        });


        edt_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(edt_title.getText().toString().isEmpty())
                        edt_title.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                    else {
                        edt_title.setBackground(getResources().getDrawable(R.drawable.write_good));
                        check_title.setBackground(getResources().getDrawable(R.drawable.complet));
                    }
                }
                return false;
            }
        });

        edt_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus){
                    if(edt_title.getText().toString().isEmpty())
                        edt_title.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                    else {
                        edt_title.setBackground(getResources().getDrawable(R.drawable.write_good));
                        check_title.setBackground(getResources().getDrawable(R.drawable.complet));
                    }
                }
                else {
                    edt_title.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                    check_title.setBackground(getResources().getDrawable(R.drawable.not_complet));
                }
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////////// description
        edt_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txv_count_description.setVisibility(View.VISIBLE);
                txv_count_description.setText(new adapter_setting().adapter_number(200 - s.toString().length() + "/200",getContext()));
            }
        });

        edt_description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if(edt_description.getText().toString().isEmpty())
                        edt_description.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                    else {
                        edt_description.setBackground(getResources().getDrawable(R.drawable.write_good));
                        check_description.setBackground(getResources().getDrawable(R.drawable.complet));
                    }
                }
                else {
                    edt_description.setBackground(getResources().getDrawable(R.drawable.edit_text_before_write));
                    check_description.setBackground(getResources().getDrawable(R.drawable.not_complet));
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////////////////////////////Button Update
        final noti_upload_data noti =new noti_upload_data(getContext());
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_title.getText().toString().equals("")){
                    focus_to_Check(check_title);
                    edt_title.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                }else if (edt_description.getText().toString().equals("")){
                    focus_to_Check(check_description);
                    edt_description.setBackground(getResources().getDrawable(R.drawable.after_write));
                    return;
                }

                edt_title.setBackground(getResources().getDrawable(R.drawable.write_good));
                check_title.setBackground(getResources().getDrawable(R.drawable.complet));

                edt_description.setBackground(getResources().getDrawable(R.drawable.write_good));
                check_description.setBackground(getResources().getDrawable(R.drawable.complet));

                data_uplaoded=false;

                final account Update_info=new account();

                Update_info.setBio(edt_description.getText().toString().trim());
                Update_info.setName(edt_title.getText().toString().trim());
                Update_info.setAccount_ID(account.getAccount_ID());
                Update_info.setImage(account.getImage());
                Update_info.setNumber_recipe(new sql_manager(getContext()).get__account().getNumber_recipe());
                alert();
                text_download.setText(new adapter_setting().adapter_number("رفع البيانات ...",getContext()));

                final Uri dd;

                final data_user_onligne db= new data_user_onligne();
                if (IMAGE_CROPING!=null) {
                    dd=IMAGE_CROPING;
                    progress.setEndValue(3);
                    noti.notif_upload(3,0);
                    progress_aniation(0, 1, new endanimation() {
                        @Override
                        public void oncallback() {
                            noti.notif_upload(3,1);
                            final ContentResolver cr = getContext().getContentResolver();
                            try {
                                Bitmap bitmap = android.provider.MediaStore.Images.Media
                                        .getBitmap(cr, IMAGE_CROPING);
                                bitmap=new adapter_setting().getResizedBitmap(bitmap,200,200);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
                                byte[] b = baos.toByteArray();
                                IMAGE_CROPING=getImageUri(getContext(),BitmapFactory.decodeByteArray(b, 0, b.length));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            db.update_profile_puc(account.getAccount_ID(), IMAGE_CROPING, new data_user_onligne.update_profile() {
                                @Override
                                public void oncallback(final String Url) {
                                    progress_aniation(1, 2, new endanimation() {
                                        @Override
                                        public void oncallback() {
                                            noti.notif_upload(3,2);
                                            Update_info.setImage(Url);
                                            db.add_new_user(Update_info, new data_user_onligne.add_succeful() {
                                                @Override
                                                public void add_complet(boolean etat) {
                                                 progress_aniation(2, 3, new endanimation() {
                                                     @Override
                                                     public void oncallback() {
                                                         noti.notif_upload(3,3);
                                                         InputStream iStream = null;
                                                         try {

                                                             Bitmap bitmap = android.provider.MediaStore.Images.Media
                                                                     .getBitmap(cr,dd);
                                                             ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                             bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
                                                             byte[] b = baos.toByteArray();
                                                             delet_file(dd);
                                                             delet_file(IMAGE_CROPING);
                                                             new sql_manager(getContext())
                                                                     .Update_data_account(Update_info.getAccount_ID(), Update_info.getName(), Update_info.getBio(), b,
                                                                             Update_info.getNumber_recipe(),Update_info.getImage());

                                                             progress.setVisibility(View.INVISIBLE);
                                                             lottie.setVisibility(View.INVISIBLE);
                                                             lottie_succes.setVisibility(View.VISIBLE);
                                                             lottie_succes.playAnimation();
                                                             text_download.setText(new adapter_setting().adapter_number("تم التحديث بنجاح",getContext()));
                                                             data_uplaoded=true;
                                                             Handler handler=new Handler();
                                                             handler.postDelayed(new Runnable() {
                                                                 public void run() {
                                                                     dialog.dismiss();
                                                                     onBackPressed();
                                                                 }
                                                             }, 2000);
                                                         } catch (FileNotFoundException e) {
                                                             e.printStackTrace();
                                                         } catch (IOException e) {
                                                             e.printStackTrace();
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
                    });

                }else {
                    final account Update_info2=new account();
                    progress.setEndValue(2);
                    noti.notif_upload(2,0);
                    Update_info2.setBio(edt_description.getText().toString().trim());
                    Update_info2.setName(edt_title.getText().toString().trim());
                    Update_info2.setAccount_ID(account.getAccount_ID());
                    Update_info2.setNumber_recipe(Update_info.getNumber_recipe());
                    Update_info2.setImage(account.getImage());
                    progress_aniation(0, 1, new endanimation() {
                        @Override
                        public void oncallback() {
                            noti.notif_upload(2,1);
                            db.add_new_user(Update_info2, new data_user_onligne.add_succeful() {
                                @Override
                                public void add_complet(boolean etat) {
                                progress_aniation(1, 2, new endanimation() {
                                    @Override
                                    public void oncallback() {
                                        noti.notif_upload(2,2);
                                        new sql_manager(getContext())
                                                .Update_data_account(Update_info.getAccount_ID(), Update_info.getName(), Update_info.getBio(), null,
                                                        Update_info.getNumber_recipe(),Update_info.getImage());

                                        progress.setVisibility(View.INVISIBLE);
                                        lottie.setVisibility(View.INVISIBLE);
                                        lottie_succes.setVisibility(View.VISIBLE);
                                        lottie_succes.playAnimation();
                                        text_download.setText(new adapter_setting().adapter_number("تم التحديث بنجاح",getContext()));
                                        data_uplaoded=true;
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                onBackPressed();
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
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////

        btn_dimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        return myLayout;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void focus_to_Check(ImageView imageView){
        imageView.setFocusable(true);
        imageView.setFocusableInTouchMode(true);///add this line
        imageView.requestFocus();
        imageView.clearFocus();
        imageView.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.error_check,getContext().getTheme()));
    }

    boolean data_uplaoded=true;


    public int onBackPressed() {
        if (data_uplaoded) {
            try {
                cancel_activity();
                return 1;
            } catch (Exception e) {
                return 0;
            }
        }else return 0;
    }

    protected int onRestart(){
        if(data_uplaoded)
            return 0;
        return 1;
    }

    ////////////////////////////////////////   Select_Image  /////////////////////////////////////////////////
    public void onSelectImageClick() {
        Intent intent=CropImage.getPickImageChooserIntent(getContext());
        startActivityForResult(intent,122);
    }

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 122 && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
               // image_profile.setImageURI(result.getUri());
                /////////////
                IMAGE_CROPING=result.getUri();
                ////////////

                ContentResolver cr = getContext().getContentResolver();
                try {
                    Bitmap bitmap = android.provider.MediaStore.Images.Media
                            .getBitmap(cr, IMAGE_CROPING);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
                    image_profile.setImageBitmap(new adapter_setting().getResizedBitmap(bitmap,1000,1000));
                    byte[] b = baos.toByteArray();
                    bitmap.recycle();
                    bitmap=null;
                    IMAGE_CROPING=getImageUri(getContext(),BitmapFactory.decodeByteArray(b, 0, b.length));
                    image_profile.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                }catch (OutOfMemoryError e){
                    Toast.makeText(getContext(), "ذاكرة هاتفك الحية [RAM] لا تتحمل لأن الصورة كبيرة", Toast.LENGTH_SHORT).show();
                }

            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                data_uplaoded=true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        }else {
           // Toast.makeText(getContext(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_MEDIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onSelectImageClick();
                data_uplaoded=false;
            } else {
                Toast.makeText(getContext(), "لايمكنك اضافة صورة بدون هده الخاصية", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setFixAspectRatio(true)
                .setAspectRatio(1,1)
                .start(getContext(), this);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   // @Override
   // public void onWindowFocusChanged(boolean hasFocus) {
   //     super.onWindowFocusChanged(hasFocus);
   //     if (hasFocus) {
   //         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
   //             getWindow().getDecorView().setSystemUiVisibility(
   //                     View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
   //                             | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
   //         }
   //     }
   // }

    LottieAnimationView lottie,lottie_succes;
    CustomGauge progress;
    TextView text_download;

     Dialog dialog;

    public void alert(){
       // LayoutInflater factory = LayoutInflater.from(edit_info_user.this);

        //text_entry is an Layout XML file containing two text field to display in alert dialog
       // final View textEntryView = factory.inflate(R.layout.to, null);

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.alert_updoad);

        lottie=        dialog.findViewById(R.id.lottie_download);
        progress      =dialog.findViewById(R.id.progressbar);
        lottie_succes= dialog.findViewById(R.id.lottie_ok);
        text_download= dialog.findViewById(R.id.text_download);

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        try {
            dialog.show();
        }catch (Exception e){
        }

    }

    public void progress_aniation(int from, int to, final endanimation endanimation){
        ObjectAnimator animation = ObjectAnimator.ofInt(progress, "Value", from,to); // see this max value coming back here, we animate towards that value
        animation.setDuration(1500); // in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                endanimation.oncallback();
            }
        }, 1500);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);

    }

    public void delet_file(Uri path){
        ContentResolver contentResolver = getContext().getContentResolver();
        contentResolver.delete(path, null, null);
    }


    interface endanimation {
        void oncallback();
    }


    public void cancel_activity(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(getActivity().isDestroyed()){
                praincipal_activity praincipal_activity=(praincipal_activity) getActivity();
                praincipal_activity.recreate();
            }else {
                setting fragment2 = new setting();
                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, fragment2);
                fragmentTransaction.commit();
            }
        }

    }




}
