package com.hilbing.bandafinal.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hilbing.bandafinal.R;
import com.hilbing.bandafinal.adapter.InstrumentAdapter;
import com.hilbing.bandafinal.models.Instruments;
import com.hilbing.bandafinal.models.InstrumentsMusicians;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstrumentFragment extends Fragment {

    @BindView(R.id.instrument_SP)
    Spinner instrumentsSP;
    @BindView(R.id.experience_SP)
    Spinner experienceSP;
    @BindView(R.id.add_instrument_BT)
    Button addInstrumentBT;
    @BindView(R.id.instruments_experience_LV)
    ListView instrumentsExperienceLV;

    List<InstrumentsMusicians> instrumentList = new ArrayList<>();

    DatabaseReference databaseInstrumentsMusicians;

    private SharedPreferences sharedPref;
    private static String PREF_STRING = "pref_values";
    String idMusician = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instrument, container, false);
        ButterKnife.bind(this, view);

        databaseInstrumentsMusicians = FirebaseDatabase.getInstance().getReference("instruments_musicians");

        sharedPref = this.getActivity().getSharedPreferences(PREF_STRING, 0);
        idMusician = sharedPref.getString("userId","");

        addInstrumentBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInstrumentMusician();
            }
        });



        return view;

    }

    private void addInstrumentMusician() {

        String instrument = instrumentsSP.getSelectedItem().toString();
        String experience = experienceSP.getSelectedItem().toString();

        String id = databaseInstrumentsMusicians.push().getKey();


        InstrumentsMusicians instrumentsMusicians = new InstrumentsMusicians(id, instrument, experience);
        databaseInstrumentsMusicians.child(idMusician).child(id).setValue(instrumentsMusicians);

        Toast.makeText(getContext(), "Instrument added", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStart() {
        super.onStart();
        databaseInstrumentsMusicians.child(idMusician).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                instrumentList.clear();

                for(DataSnapshot instrumentSnapshot : dataSnapshot.getChildren()){
                    InstrumentsMusicians instrumentsMusicians = instrumentSnapshot.getValue(InstrumentsMusicians.class);
                    instrumentList.add(instrumentsMusicians);
                }

                InstrumentAdapter adapter = new InstrumentAdapter(getContext(), instrumentList);
                instrumentsExperienceLV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
