package com.hilbing.bandafinal.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hilbing.bandafinal.R;
import com.hilbing.bandafinal.adapter.InstrumentAdapter;
import com.hilbing.bandafinal.adapter.SongAdapter;
import com.hilbing.bandafinal.models.InstrumentsMusicians;
import com.hilbing.bandafinal.models.Song;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongFragment extends Fragment {
    @BindView(R.id.song_song_name_ET)
    EditText songNameET;
    @BindView(R.id.song_artist_band_ET)
    EditText songArtistBandET;
    @BindView(R.id.song_duration_ET)
    EditText songDurationET;
    @BindView(R.id.song_youtube_link_ET)
    EditText songYoutubeLinkET;
    @BindView(R.id.song_add_song_BT)
    Button addSongBT;
    @BindView(R.id.songs_LV)
    ListView songsLV;

    List<Song> songsList = new ArrayList<>();

    DatabaseReference databaseSongs;

    private SharedPreferences sharedPref;
    private static String PREF_STRING = "pref_values";
    String idBand = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        ButterKnife.bind(this, view);

        databaseSongs = FirebaseDatabase.getInstance().getReference("songs");

        sharedPref = this.getActivity().getSharedPreferences(PREF_STRING, 0);
        idBand = sharedPref.getString("bandId","");

        songYoutubeLinkET.setText(R.string.no_data_available);

        addSongBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSong();
            }
        });

        songsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = songsList.get(i);
                showUpdateDialog(song.getmId(), song.getmName(), song.getmArtist(), song.getmDuration(), song.getmUrlYoutube());
                return false;
            }
        });



        return view;

    }

    private void addSong() {

        String name = songNameET.getText().toString();
        String artist = songArtistBandET.getText().toString();
        String duration = songDurationET.getText().toString();
        String youtubeLink = songYoutubeLinkET.getText().toString();

        String id = databaseSongs.push().getKey();

            Song song = new Song(id, name, artist, duration, youtubeLink);
            databaseSongs.child(idBand).child(id).setValue(song);
            Toast.makeText(getContext(), getResources().getString(R.string.song_added), Toast.LENGTH_LONG).show();

    }

    private void showUpdateDialog(final String id, String name, String artist, String duration, String youtubeLink){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_song, null);
        dialogBuilder.setView(dialogView);

        final EditText nameET = dialogView.findViewById(R.id.newSongNameET);
        final EditText artistET = dialogView.findViewById(R.id.newArtistNameET);
        final EditText durationET = dialogView.findViewById(R.id.newDurationET);
        final EditText youtubeET = dialogView.findViewById(R.id.newYoutubeLinkET);
        final Button updateBT = dialogView.findViewById(R.id.update_BT);
        final Button deleteBT = dialogView.findViewById(R.id.delete_BT);

        nameET.setText(name);
        artistET.setText(artist);
        durationET.setText(duration);
        youtubeET.setText(youtubeLink);

        dialogBuilder.setTitle(getResources().getString(R.string.updating_song));
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        updateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = nameET.getText().toString();
                String newArtist = artistET.getText().toString();
                String newDuration = durationET.getText().toString();
                String newYoutube = youtubeET.getText().toString();

                    updateSong(id, newName, newArtist, newDuration, newYoutube);

                alertDialog.dismiss();

            }
        });

        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSong(id);
                alertDialog.dismiss();
            }
        });

    }

    private void deleteSong(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("songs").child(idBand).child(id);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), getResources().getString(R.string.song_deleted), Toast.LENGTH_LONG).show();
            }
        });



    }


    private boolean updateSong(String id, String name, String artist, String duration, String youtube){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("songs").child(idBand).child(id);
        Song song = new Song(id, name, artist, duration, youtube);
        databaseReference.setValue(song);
        Toast.makeText(getContext(), getResources().getString(R.string.song_added_successfully), Toast.LENGTH_LONG).show();
        return true;

    }


    @Override
    public void onStart() {
        super.onStart();
        databaseSongs.child(idBand).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                songsList.clear();

                for(DataSnapshot songSnapshot : dataSnapshot.getChildren()){
                    Song song = songSnapshot.getValue(Song.class);
                    songsList.add(song);
                }

                SongAdapter adapter = new SongAdapter(getContext(), songsList);
                songsLV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
