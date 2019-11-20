package com.hilbing.bandafinal.activities.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.hilbing.bandafinal.MainActivity;
import com.hilbing.bandafinal.ProfileActivity;
import com.hilbing.bandafinal.R;
import com.hilbing.bandafinal.models.Musician;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.email_signin_ET)
    EditText emailET;
    @BindView(R.id.password_signin_ET)
    EditText passwordET;
    @BindView(R.id.signup_BT)
    Button signUp;
    @BindView(R.id.have_account_signin_TV)
    TextView haveAccount;
    @BindView(R.id.login_facebook_BT)
    LoginButton facebookBT;
    @BindView(R.id.login_google_BT)
    SignInButton googleBT;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabaseMusicians;

    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseMusicians = FirebaseDatabase.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailET.getText().toString().trim();
                String passw = passwordET.getText().toString().trim();
                if (email.isEmpty()) {
                    emailET.setError(getResources().getString(R.string.enter_email_address));
                    emailET.requestFocus();
                    return;

                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailET.setError(getResources().getString(R.string.please_enter_a_valid_email));
                    emailET.requestFocus();
                    return;
                }

                if (passw.length() < 6){
                    passwordET.setError(getResources().getString(R.string.minimum_length_of_password_should_be_6));
                    passwordET.requestFocus();
                    return;
                }

                else if (passw.isEmpty()) {
                    passwordET.setError(getResources().getString(R.string.prompt_password));
                    passwordET.requestFocus();
                    return;
                }

                else if (email.isEmpty() && passw.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.fields_are_empty), Toast.LENGTH_LONG).show();
                }
                else if (!(email.isEmpty() && passw.isEmpty())) {

                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, passw).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                finish();
                                Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
                }

            }
        });

        facebookBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, FacebookLoginActivity.class));
            }
        });

        googleBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, GoogleLoginActivity.class));
            }
        });


        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(SignUpActivity.this, LoginEmailPassActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }
}
