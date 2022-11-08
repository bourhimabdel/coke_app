package com.cokeappingo.user_info;

import com.cokeappingo.class_utile.account;
import com.cokeappingo.signin;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class user_info {

    public account get_info_account(GoogleSignInAccount account_G){

        FirebaseUser ff=FirebaseAuth.getInstance().getCurrentUser();

        account account = new account();
        account.setName(ff.getDisplayName());
        account.setImage(ff.getPhotoUrl().toString());
        account.setAccount_ID(ff.getUid());
        account.setBio(null);
        return account;
    }
}
