package com.hilbing.bandafinal.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hilbing.bandafinal.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import butterknife.ButterKnife;

public class PlaylistFragment extends Fragment {

    final String appPackageName = "com.spotify.music";
    final String referrer = "adjust_campaign=PACKAGE_NAME&adjust_tracker=ndjczk&utm_source=adjust_preinstall";



    private static final int REQUEST_CODE = 1337;

    private static final String CLIENT_ID = "8a3367eb003649048f31515144e208b5";
    private static final String REDIRECT_URI = "http://localhost:8888/callback/";
    private SpotifyAppRemote mSpotifyAppRemote;

    AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
       // ButterKnife.bind(this, view);

/*        PackageManager pm = getActivity().getPackageManager();
        boolean isSpotifyInstalled;
        try{
            pm.getPackageInfo("com.spotify.music", 0);
            isSpotifyInstalled = true;
        }catch (PackageManager.NameNotFoundException e){
            isSpotifyInstalled = false;
        }

        try {
            Uri uri = Uri.parse("market://details")
                    .buildUpon()
                    .appendQueryParameter("id", appPackageName)
                    .appendQueryParameter("referrer", referrer)
                    .build();
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (android.content.ActivityNotFoundException ignored) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                    .buildUpon()
                    .appendQueryParameter("id", appPackageName)
                    .appendQueryParameter("referrer", referrer)
                    .build();
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("spotify:album:0sNOF9WDwhWunNAHPD3Baj"));
        intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://") + getContext().getPackageName());
        startActivity(intent);*/

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(getActivity(), REQUEST_CODE, request);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getActivity(), connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    private void connected(){

        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
    }

    @Override
    public void onStop() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
            switch (response.getType()){
                case TOKEN:
                    break;
                case ERROR:
                    break;
                default:
            }
        }
    }
}
