package com.cokeappingo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class open_form_Lien extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_form__lien);

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null){
            String id_recipe=bundle.getString("recipe");

            show_this_recipe fragment2=new show_this_recipe();
            Bundle args = new Bundle();
            args.putString("recipe",id_recipe);
            fragment2.setArguments(args);
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fss,fragment2,"sinm");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}