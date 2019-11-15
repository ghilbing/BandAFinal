package com.hilbing.bandafinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hilbing.bandafinal.activities.auth.LoginEmailPassActivity;
import com.squareup.picasso.Picasso;

import java.security.SecureRandom;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.account_IV)
    ImageView accountIV;
    @BindView(R.id.account_name_TV)
    TextView accountNameTV;
    @BindView(R.id.account_signOut_BT)
    Button accountSignOutBT;
    private int CAMERA_REQUEST_CODE = 1;
    private ProgressDialog progressDialog;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ButterKnife.bind(this);
        accountIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                if(intent.resolveActivity(getPackageManager()) != null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_a_picture_for_your_profile)), CAMERA_REQUEST_CODE);
                    }
                }
            }
        });

        accountSignOutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null){
                    mAuth.signOut();
                }
            }
        });

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    mStorage = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            accountNameTV.setText(dataSnapshot.child("name").getValue().toString());
                            String imageUrl = dataSnapshot.child("image").getValue().toString();
                            if (!imageUrl.equals("default") || TextUtils.isEmpty(imageUrl)){
                                Picasso.get().load(Uri.parse(dataSnapshot.child("image").getValue().toString())).into(accountIV);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    startActivity(new Intent(AccountActivity.this, LoginEmailPassActivity.class));
                    finish();
                }
            }
        };

    }

    public String getRandomString(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            if (mAuth.getCurrentUser() == null){
                return;
            }
            progressDialog.setMessage(getResources().getString(R.string.uploading_image));
            progressDialog.show();
            final Uri uri = data.getData();
            if (uri == null){
                progressDialog.dismiss();
                return;
            }
            if (mAuth.getCurrentUser() == null){
                return;
            }
            if (mStorage == null){
                mStorage = FirebaseStorage.getInstance().getReference();
            }
            if (mDatabase == null){
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
            }

            final StorageReference filepath = mStorage.child("Photos").child(getRandomString());
            final DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
            currentUserDB.child("image").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String image = dataSnapshot.getValue().toString();

                    if (!image.equals("default") && !image.isEmpty()){
                        Task<Void> task = FirebaseStorage.getInstance().getReferenceFromUrl(image).delete();
                        task.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(AccountActivity.this, getResources().getString(R.string.deleted_image_succesfully), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(AccountActivity.this, getResources().getString(R.string.deleted_image_failed), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    currentUserDB.child("image").removeEventListener(this);

                    filepath.putFile(uri).addOnSuccessListener(AccountActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map newImage = new HashMap();
                                    newImage.put("profileImageUrl", uri.toString());
                                    currentUserDB.updateChildren(newImage);
                                    Toast.makeText(AccountActivity.this, getResources().getString(R.string.finished), Toast.LENGTH_LONG).show();
                                    finish();
                                    return;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    finish();
                                    return;
                                }
                            });
                        }
                    });
                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
