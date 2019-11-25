package com.hilbing.bandafinal.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookActivity;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.hilbing.bandafinal.ProfileActivity;
import com.hilbing.bandafinal.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FacebookUserActivity extends AppCompatActivity {

    @BindView(R.id.facebook_u_logout_BT)
    ImageButton logout;
    @BindView(R.id.facebook_u_image_IV)
    ImageView imageUser;
    @BindView(R.id.facebook_u_user_TV)
    TextView userName;
    @BindView(R.id.facebook_u_email_TV)
    TextView emailUser;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_user);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    for (UserInfo userInfo : user.getProviderData()) {
                        Log.d("TAG", userInfo.getProviderId());
                    }

                    userName.setText(user.getDisplayName());
                    emailUser.setText(user.getEmail());
                    Picasso.get().load(user.getPhotoUrl()).into(imageUser);
                } else {
                    Intent intent = new Intent(FacebookUserActivity.this, ProfileActivity.class);

                    intent.putExtra("logout", true);
                    startActivity(intent);
                    finish();
                }

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}
