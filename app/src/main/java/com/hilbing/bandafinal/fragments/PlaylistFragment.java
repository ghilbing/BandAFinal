package com.hilbing.bandafinal.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hilbing.bandafinal.R;
import com.hilbing.bandafinal.connectors.MusicianService;
import com.hilbing.bandafinal.connectors.SongService;
import com.hilbing.bandafinal.models.Musician;
import com.hilbing.bandafinal.models.Song;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaylistFragment extends Fragment {

    @BindView(R.id.playlist_bandNameTV)
    TextView musicianIDTV;

    @BindView(R.id.addSongBT)
    Button addSongBT;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
      //  getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this, view);


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }
}
