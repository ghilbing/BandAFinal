package com.hilbing.bandafinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hilbing.bandafinal.activities.auth.FacebookLoginActivity;
import com.hilbing.bandafinal.activities.auth.GoogleLoginActivity;
import com.hilbing.bandafinal.activities.auth.LoginEmailPassActivity;
import com.hilbing.bandafinal.activities.auth.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void register(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void login(View view){
        startActivity(new Intent(this, LoginEmailPassActivity.class));
    }

    public void manageAccount(View view){
        startActivity(new Intent(this, AccountActivity.class));

    }

    public void facebookLogin(View view){
        startActivity(new Intent(this, FacebookLoginActivity.class));
    }

    public void googleLogin(View view){
        startActivity(new Intent(this, GoogleLoginActivity.class));
    }
}
