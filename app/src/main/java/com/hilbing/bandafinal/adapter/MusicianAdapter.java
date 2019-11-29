package com.hilbing.bandafinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.util.CollectionUtils;
import com.hilbing.bandafinal.R;
import com.hilbing.bandafinal.models.InstrumentsMusicians;
import com.hilbing.bandafinal.models.Musician;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MusicianAdapter extends ArrayAdapter<Musician> {

    @BindView(R.id.musicianNameTV)
    TextView musicianNameTV;
    @BindView(R.id.musicianPhoneTV)
    TextView musicianPhoneTV;
   /* @BindView(R.id.musicianEmailTV)
    TextView musicianEmailTV;*/

    private Context context;
    private List<Musician> musiciansList;


    public MusicianAdapter(Context context, List<Musician> musiciansList) {
        super(context, R.layout.musician_layout, musiciansList);
        this.context = context;
        this.musiciansList = musiciansList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItem = inflater.inflate(R.layout.musician_layout, null, true);
        ButterKnife.bind(this, listItem);

        Musician musician = musiciansList.get(position);
        musicianNameTV.setText(musician.getmName());
        musicianPhoneTV.setText(musician.getmPhone());
      //  musicianEmailTV.setText(musician.getmEmail());

        return listItem;


    }

    @Override
    public int getCount() {
        return CollectionUtils.isEmpty(musiciansList) ? 0 : musiciansList.size();
    }

    public void updateList(List<Musician> list){
       musiciansList = list;
       notifyDataSetChanged();
    }


}
