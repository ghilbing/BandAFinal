package com.hilbing.bandafinal.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.hilbing.bandafinal.models.Instruments;

import java.util.List;

public class InstrumentsList extends ArrayAdapter<Instruments> {


    public InstrumentsList(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
