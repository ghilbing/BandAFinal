package com.hilbing.bandafinal.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hilbing.bandafinal.R;
import com.hilbing.bandafinal.adapter.BandAdapter;
import com.hilbing.bandafinal.adapter.MusicianAdapter;
import com.hilbing.bandafinal.models.Band;
import com.hilbing.bandafinal.models.BandsMusicians;
import com.hilbing.bandafinal.models.InstrumentsMusicians;
import com.hilbing.bandafinal.models.Musician;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class BandFragment extends Fragment {

    @BindView(R.id.band_name_ET)
    EditText bandNameET;
    @BindView(R.id.create_band_BT)
    Button createBandBT;
    @BindView(R.id.bands_LV)
    ListView bandLV;
    @BindView(R.id.add_musician_BT)
    Button addMusicianBT;
    @BindView(R.id.musicians_added_LV)
    ListView musiciansAddedLV;



    List<Band> bandsList = new ArrayList<Band>();
    List<Musician> musiciansList = new ArrayList<>();

    DatabaseReference databaseBands;
    DatabaseReference databaseMusicians;
    DatabaseReference databaseBandsMusicians;


    private SharedPreferences sharedPref;
    private static String PREF_STRING = "pref_values";
    String idMusician = null;
    String idBand = null;
    String bandName = null;

    BandAdapter adapter;
    MusicianAdapter musicianAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_band, container, false);
        ButterKnife.bind(this, view);

        databaseBands = FirebaseDatabase.getInstance().getReference("bands");
        databaseBandsMusicians = FirebaseDatabase.getInstance().getReference("bandsMusicians");
        databaseMusicians = FirebaseDatabase.getInstance().getReference("musicians");


        sharedPref = this.getActivity().getSharedPreferences(PREF_STRING, 0);
        idMusician = sharedPref.getString("userId","");

        createBandBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBand();
            }
        });

        addMusicianBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });



        return view;
    }

    private void openDialog() {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();

            sharedPref = this.getActivity().getSharedPreferences(PREF_STRING, 0);
            idBand = sharedPref.getString("bandId","");
            bandName = sharedPref.getString("bandName", "");

            DatabaseReference bandsMusiciansSearch = FirebaseDatabase.getInstance().getReference("bandsMusicians");

            final View dialogView = inflater.inflate(R.layout.add_musician_dialog, null);
            dialogBuilder.setView(dialogView);

            List<Musician> musiciansListDialog = new ArrayList<>();

            final EditText musicianNameET = dialogView.findViewById(R.id.search_musician_ET);
            final ListView musiciansLV = dialogView.findViewById(R.id.musicians_dialog_LV);
            final Button addMusicianBT = dialogView.findViewById(R.id.add_m_dialog_BT);



            databaseMusicians.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    musiciansList.clear();
                    for(DataSnapshot musicianSnapshot :dataSnapshot.getChildren()){
                        Musician musician = musicianSnapshot.getValue(Musician.class);
                        musiciansListDialog.add(musician);
                    }

                    musicianAdapter = new MusicianAdapter(getContext(), musiciansListDialog);
                    musiciansLV.setTextFilterEnabled(true);
                    musiciansLV.setAdapter(musicianAdapter);

                    musiciansLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Musician musicSelected = musicianAdapter.getItem(i);
                            addMusicianBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Long tsLong = System.currentTimeMillis()/1000;
                                    String ts = tsLong.toString();
                                    String idMus = musicSelected.getmId();
                                    BandsMusicians bandsMusicians = new BandsMusicians(idBand, idMus, "player", ts);
                                    databaseBandsMusicians.child(idBand).child(idMus).setValue(bandsMusicians);
                                    Toast.makeText(getContext(), "Musician added clicked", Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    });



                    musicianNameET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            musicianAdapter.getFilter().filter(charSequence);
                            musicianAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        dialogBuilder.setTitle(getResources().getString(R.string.adding_musicians_to_band) + ": " + bandName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


    }

    private void addBand() {

        final String bandName = bandNameET.getText().toString().trim();

        final String idBand = databaseBands.push().getKey();


        Band band = new Band(idBand, bandName);
        databaseBands.child(idBand).setValue(band);

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        BandsMusicians bandsMusicians = new BandsMusicians(idBand, idMusician, "admin",ts);
        databaseBandsMusicians.child(idBand).child(idMusician).setValue(bandsMusicians);

        bandSharedPreferences(idBand, bandName);

        Toast.makeText(getContext(), "Band added", Toast.LENGTH_LONG).show();

    }



    @Override
    public void onStart() {
        super.onStart();

        databaseBands.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bandsList.clear();
                for(DataSnapshot bandsSnapshot :dataSnapshot.getChildren()){
                    Band band = bandsSnapshot.getValue(Band.class);
                    bandsList.add(band);

                    databaseBandsMusicians.child(idBand).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String id = dataSnapshot.getKey();
                            databaseMusicians.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    musiciansList.clear();
                                    for(DataSnapshot musicianSnapshot :dataSnapshot.getChildren()){
                                        Musician musician = musicianSnapshot.getValue(Musician.class);
                                        musiciansList.add(musician);
                                    }

                                    musicianAdapter = new MusicianAdapter(getContext(), musiciansList);
                                    musiciansAddedLV.setAdapter(musicianAdapter);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                adapter = new BandAdapter(getContext(), bandsList);
                bandLV.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




/*        databaseBandsMusicians.child(idBand).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                musiciansList.clear();
                for(DataSnapshot musicianSnapshot :dataSnapshot.getChildren()){
                    Musician musician = musicianSnapshot.getValue(Musician.class);
                    musiciansList.add(musician);
                }

                musicianAdapter = new MusicianAdapter(getContext(), musiciansList);
                musiciansAddedLV.setAdapter(musicianAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }

    public void bandSharedPreferences(String bandId, String bandName){

        sharedPref = getActivity().getApplicationContext().getSharedPreferences(PREF_STRING, 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("bandId", bandId);
        editor.putString("bandName", bandName);


        editor.commit();
    }

}