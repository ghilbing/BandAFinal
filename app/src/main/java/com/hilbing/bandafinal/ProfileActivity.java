package com.hilbing.bandafinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hilbing.bandafinal.activities.auth.LoginEmailPassActivity;
import com.hilbing.bandafinal.models.Musician;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 1;
    @BindView(R.id.profile_image_IV)
    ImageView photoIV;
    @BindView(R.id.profile_name_ET)
    EditText nameET;
    @BindView(R.id.profile_phone_ET)
    EditText phoneET;
    @BindView(R.id.profile_addMusician_BT)
    Button addMusicianBT;
    @BindView(R.id.profile_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.profile_verified_TV)
    TextView verifiedTV;
    @BindView(R.id.profile_toolbar)
    Toolbar toolbar;

    String profileImageURL;

    private SharedPreferences sharedPreferences;
    private static String PREF_STRING = "pref_values";

    DatabaseReference databaseMusicians;
    FirebaseAuth mAuth;

    Uri uriProfileImage;
    Uri imageGoogle;
    Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        databaseMusicians = FirebaseDatabase.getInstance().getReference("musicians");

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        loadUserData();



        photoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        addMusicianBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
                //loadUserData();
            }
        });

    }

    private void loadUserData() {

        final FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            if (user.getPhotoUrl() != null) {
                Picasso.get().load(user.getPhotoUrl().toString()).into(photoIV);
            }
            if (user.getDisplayName() != null) {
                nameET.setText(user.getDisplayName());
            }
            if(user.isEmailVerified()){
                verifiedTV.setText(getResources().getString(R.string.email_verified));
            } else {
                verifiedTV.setText(getResources().getString(R.string.email_not_verified));
                verifiedTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.verification_email_sent), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        }
    }

    private void uploadImageToFirebaseStorage() {

        FirebaseUser user = mAuth.getCurrentUser();
        imageGoogle = user.getPhotoUrl();

        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        final StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
        final StorageReference imageRef = storageReferenceProfilePic.child("profileimages/" + System.currentTimeMillis() + ".jpg");
        if(uriProfileImage != null){
            progressBar.setVisibility(View.VISIBLE);
            imageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileImageURL = uri.toString();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void uploadImageGoogleToFirebaseStorage() {

        FirebaseUser user = mAuth.getCurrentUser();
        imageGoogle = user.getPhotoUrl();
        Uri uploadUri = Uri.parse(String.valueOf(imageGoogle));

        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        final StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
        final StorageReference imageRef = storageReferenceProfilePic.child("profileimagesgoogle/" + uploadUri.toString());//System.currentTimeMillis() + ".jpg");
        if(uploadUri != null){
            progressBar.setVisibility(View.VISIBLE);
            imageRef.putFile(uploadUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileImageURL = uri.toString();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void saveUserInformation() {

        final String name = nameET.getText().toString();
        final String phone = phoneET.getText().toString();


        if(name.isEmpty()){
            nameET.setError(getResources().getString(R.string.enter_your_name));
            nameET.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        String imageGoogle = String.valueOf(user.getPhotoUrl());
        Uri profileUri = null;

        if(profileImageURL == null) {
            if (user.getPhotoUrl() == null) {
                //When the user has not uploaded an image and when the profile does not contain a default photo
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_upload_an_image), Toast.LENGTH_LONG).show();
                return;
            }
            //get the default profile photo when user has not uploaded an image
            profileUri = user.getPhotoUrl();
        } else {
            //User the user uploaded image
            profileUri = Uri.parse(profileImageURL);
        }
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(profileUri)
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.musician_added), Toast.LENGTH_LONG).show();
                               // Toast.makeText(getApplicationContext(), getResources().getString(R.string.profile_updated), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            String id = databaseMusicians.push().getKey();
            Musician musician = new Musician(id, name, phone);
            databaseMusicians.child(id).setValue(musician);
            Toast.makeText(this, getResources().getString(R.string.musician_added), Toast.LENGTH_LONG).show();
       // }

       // uploadImageToFirebaseStorage();
        loginSharedPreferences(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());//, user.getPhotoUrl().toString());
        startActivity(new Intent(getApplicationContext(), MainActivity.class));



    }

    public void loginSharedPreferences(String userId, String userName, String userPicture){

        sharedPreferences = getApplicationContext().getSharedPreferences(PREF_STRING, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userId", userId);
        editor.putString("userName", userName);
        editor.putString("userPicture", userPicture);

        editor.commit();
    }


    private void addMusician(){
        String name = nameET.getText().toString().trim();
        String phone = phoneET.getText().toString().trim();

        if(!TextUtils.isEmpty(name)){
            String id = databaseMusicians.push().getKey();
            Musician musician = new Musician(id, name, phone);
            databaseMusicians.child(id).setValue(musician);
            Toast.makeText(this, getResources().getString(R.string.musician_added), Toast.LENGTH_LONG).show();


        } else {
            Toast.makeText(this, getResources().getString(R.string.enter_your_name), Toast.LENGTH_LONG).show();
        }
    }

    private void imageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_a_picture_for_your_profile)), CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                photoIV.setImageBitmap(bitmap);
                
                uploadImageToFirebaseStorage();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), LoginEmailPassActivity.class));
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginEmailPassActivity.class));
                break;
        }

        return true;
    }


}
