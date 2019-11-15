package com.hilbing.bandafinal.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hilbing.bandafinal.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_name_ET)
    EditText name;
    @BindView(R.id.register_email_ET)
    EditText email;
    @BindView(R.id.register_password_ET)
    EditText password;
    @BindView(R.id.register_BT)
    Button register;
    @BindView(R.id.register_login_TV)
    TextView login;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginEmailPassActivity.class);
                startActivity(intent);
            }
        });

        mProgress = new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){
                    Intent intent = new Intent(RegisterActivity.this, LoginEmailPassActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    private void startRegister() {
        final String nameString = name.getText().toString().trim();
        final String emailString = email.getText().toString().trim();
        final String passwordString = password.getText().toString().trim();

        if (!TextUtils.isEmpty(nameString) && !TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(passwordString)){
            mProgress.setMessage(getResources().getString(R.string.registering_please_wait));
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgress.dismiss();
                            if(task.isSuccessful()){
                                mAuth.signInWithEmailAndPassword(emailString, passwordString);
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                                DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                                currentUserDB.child("name").setValue(nameString);
                                currentUserDB.child("image").setValue("default");
                            } else {
                                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.error_registering_user), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}
