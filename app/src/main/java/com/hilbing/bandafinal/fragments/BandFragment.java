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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hilbing.bandafinal.MainActivity;
import com.hilbing.bandafinal.R;
import com.hilbing.bandafinal.adapter.BandAdapter;
import com.hilbing.bandafinal.adapter.InstrumentAdapter;
import com.hilbing.bandafinal.adapter.MusicianAdapter;
import com.hilbing.bandafinal.models.Band;
import com.hilbing.bandafinal.models.BandsMusicians;
import com.hilbing.bandafinal.models.InstrumentsMusicians;
import com.hilbing.bandafinal.models.Musician;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BandFragment extends Fragment {

    @BindView(R.id.band_name_ET)
    EditText bandNameET;
    @BindView(R.id.create_band_BT)
    Button createBandBT;
    @BindView(R.id.bands_LV)
    ListView bandLV;



    List<Band> bandsList = new ArrayList<Band>();

    DatabaseReference databaseBands;
    DatabaseReference databaseBandsMusicians;

    private SharedPreferences sharedPref;
    private static String PREF_STRING = "pref_values";
    String idMusician = null;

    BandAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_band, container, false);
        ButterKnife.bind(this, view);

        databaseBands = FirebaseDatabase.getInstance().getReference("bands");
        databaseBandsMusicians = FirebaseDatabase.getInstance().getReference("bandsMusicians");


        sharedPref = this.getActivity().getSharedPreferences(PREF_STRING, 0);
        idMusician = sharedPref.getString("userId","");

        createBandBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBand();
            }
        });

        return view;
    }

    private void addBand() {

        String bandName = bandNameET.getText().toString().trim();

        final String id = databaseBands.push().getKey();


        Band band = new Band(id, bandName);
        databaseBands.child(id).setValue(band);

        final String id2 = databaseBandsMusicians.push().getKey();

        BandsMusicians bandsMusicians = new BandsMusicians(id2, id, idMusician, "admin");
        databaseBandsMusicians.child(id2).setValue(bandsMusicians);

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
                }

                adapter = new BandAdapter(getContext(), bandsList);
                bandLV.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}