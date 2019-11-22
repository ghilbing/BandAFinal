package com.hilbing.bandafinal.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hilbing.bandafinal.R;
import com.hilbing.bandafinal.models.InstrumentsMusicians;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstrumentAdapter extends ArrayAdapter<InstrumentsMusicians> {

    @BindView(R.id.instrumentNameTV)
    TextView instrumentNameTV;
    @BindView(R.id.experienceTV)
    TextView experienceTV;

    private Context context;
    private List<InstrumentsMusicians> instrumentsMusiciansList;


    public InstrumentAdapter(Context context, List<InstrumentsMusicians> instrumentsMusiciansList) {
        super(context, R.layout.instrument_layout, instrumentsMusiciansList);
        this.context = context;
        this.instrumentsMusiciansList = instrumentsMusiciansList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItem = inflater.inflate(R.layout.instrument_layout, null, true);
        ButterKnife.bind(this, listItem);

        InstrumentsMusicians instrumentsMusicians = instrumentsMusiciansList.get(position);
        instrumentNameTV.setText(instrumentsMusicians.getmInstrument());
        experienceTV.setText(instrumentsMusicians.getmExperience());

        return listItem;


    }
}
