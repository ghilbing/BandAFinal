package com.hilbing.bandafinal.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

        instrumentsExperienceLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                InstrumentsMusicians instrumentsMusicians = instrumentList.get(i);
                showUpdateDialog(instrumentsMusicians.getmId(), instrumentsMusicians.getmInstrument(), instrumentsMusicians.getmExperience());
                return false;
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

    private void showUpdateDialog(final String id, String instrument, String experience){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_instrument, null);
        dialogBuilder.setView(dialogView);

        final Spinner newInstrumentSP = dialogView.findViewById(R.id.new_instrument_SP);
        final Spinner newExperienceSP = dialogView.findViewById(R.id.new_experience_SP);
        final Button updateBT = dialogView.findViewById(R.id.update_BT);
        final Button deleteBT = dialogView.findViewById(R.id.delete_BT);

        newInstrumentSP.setSelection(getIndexSpinner(newInstrumentSP, instrument));
        newExperienceSP.setSelection(getIndexSpinner(newExperienceSP, experience));



        dialogBuilder.setTitle(getResources().getString(R.string.updating_instruments) + ": " + instrument);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        updateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newInstrument = newInstrumentSP.getSelectedItem().toString();
                String newExperience = newExperienceSP.getSelectedItem().toString();

                updateInstrument(id, newInstrument, newExperience);

                alertDialog.dismiss();


            }
        });

        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteInstrument(id);
                alertDialog.dismiss();
            }
        });

    }

    private void deleteInstrument(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("instruments_musicians").child(idMusician).child(id);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), getResources().getString(R.string.instrument_deleted), Toast.LENGTH_LONG).show();
            }
        });



    }

    private int getIndexSpinner(Spinner spinner, String string){
        for (int i = 0; i < spinner.getCount() ; i++) {
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)){
                return i;
            }
        }

        return 0;
    }

    private boolean updateInstrument(String id, String instrument, String experience){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("instruments_musicians").child(idMusician).child(id);
        InstrumentsMusicians instrumentsMusicians = new InstrumentsMusicians(id, instrument, experience);
        databaseReference.setValue(instrumentsMusicians);
        Toast.makeText(getContext(), getResources().getString(R.string.instrument_updated_successfully), Toast.LENGTH_LONG).show();
        return true;

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
