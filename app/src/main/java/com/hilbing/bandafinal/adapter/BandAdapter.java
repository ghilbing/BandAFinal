package com.hilbing.bandafinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hilbing.bandafinal.R;
import com.hilbing.bandafinal.models.Band;
import com.hilbing.bandafinal.models.Musician;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BandAdapter extends ArrayAdapter<Band> {

    @BindView(R.id.bandNameTV)
    TextView bandNameTV;

    private Context context;
    private List<Band> bandsList;


    public BandAdapter(Context context, List<Band> bandsList) {
        super(context, R.layout.band_layout, bandsList);
        this.context = context;
        this.bandsList = bandsList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItem = inflater.inflate(R.layout.band_layout, null, true);
        ButterKnife.bind(this, listItem);

        Band band = bandsList.get(position);
        bandNameTV.setText(band.getmBandName());

        return listItem;


    }

}
