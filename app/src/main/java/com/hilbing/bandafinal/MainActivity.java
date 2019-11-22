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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
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



    private SharedPreferences sharedPref;
    private static String PREF_STRING = "pref_value";

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

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }


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

        HeaderViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
