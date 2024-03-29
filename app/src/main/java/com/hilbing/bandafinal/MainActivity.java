package com.hilbing.bandafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hilbing.bandafinal.activities.auth.FacebookLoginActivity;
import com.hilbing.bandafinal.activities.auth.GoogleLoginActivity;
import com.hilbing.bandafinal.activities.auth.LoginEmailPassActivity;
import com.hilbing.bandafinal.activities.auth.RegisterActivity;
import com.hilbing.bandafinal.fragments.BandFragment;
import com.hilbing.bandafinal.fragments.ConcertFragment;
import com.hilbing.bandafinal.fragments.InstrumentFragment;
import com.hilbing.bandafinal.fragments.PlaylistFragment;
import com.hilbing.bandafinal.fragments.ProfileFragment;
import com.hilbing.bandafinal.fragments.RehearsalFragment;
import com.hilbing.bandafinal.fragments.SongFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    HeaderViewHolder mHeaderViewHolder;

    ProfileFragment profileFragment;



    private SharedPreferences sharedPref;
    private static String PREF_STRING = "pref_values";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        sharedPref = getApplicationContext().getSharedPreferences(PREF_STRING, 0);

        View header = navigationView.getHeaderView(0);
        mHeaderViewHolder = new HeaderViewHolder(header);

        mHeaderViewHolder.userIdTV.setText(sharedPref.getString("userId", ""));
        mHeaderViewHolder.userNameTV.setText(sharedPref.getString("userName", ""));
        mHeaderViewHolder.bandIdTV.setText(sharedPref.getString("bandId",""));
        String userPicture = sharedPref.getString("userPicture", "");
        if(!TextUtils.isEmpty(userPicture)) {
            Picasso.get().load(sharedPref.getString("userPicture", "")).into(mHeaderViewHolder.userImageIV);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                        break;
                    case R.id.nav_instruments:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InstrumentFragment()).commit();
                        break;
                    case R.id.nav_band:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BandFragment()).commit();
                        break;
                    case R.id.nav_song:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SongFragment()).commit();
                        break;
                    case R.id.nav_playlists:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlaylistFragment()).commit();
                        break;
                    case R.id.nav_rehearsals:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RehearsalFragment()).commit();
                        break;
                    case R.id.nav_concerts:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConcertFragment()).commit();
                        break;
                    case R.id.nav_share:
                        Toast.makeText(getApplicationContext(), "Share option menu", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_settings:
                        Toast.makeText(getApplicationContext(), "Settings option menu", Toast.LENGTH_LONG).show();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

   /*     if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }
        if(savedInstanceState != null){
            profileFragment = (ProfileFragment) getSupportFragmentManager().getFragment(savedInstanceState, "ProfileFragment");
        }*/


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        getSupportFragmentManager().putFragment(outState, "ProfileFragment", profileFragment);
    }

    public void getDataFromPreferences(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREF_STRING, 0);
        SharedPreferences.Editor editor = preferences.edit();

        final String userName = preferences.getString("userName", "");
        final String userId = preferences.getString("userId", "");


        editor.commit();

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    protected static class HeaderViewHolder{
        @BindView(R.id.nav_header_name_TV)
        TextView userNameTV;

        @BindView(R.id.nav_header_id_TV)
        TextView userIdTV;

        @BindView(R.id.nav_header_image_IV)
        ImageView userImageIV;

        @BindView(R.id.nav_header_idBand_TV)
        TextView bandIdTV;

        HeaderViewHolder(View view){
            ButterKnife.bind(this, view);
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
