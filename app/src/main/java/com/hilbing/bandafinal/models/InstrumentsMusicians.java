package com.hilbing.bandafinal.models;

public class InstrumentsMusicians {

    String mId;
    String mInstrument;
    String mExperience;

    public InstrumentsMusicians(){}

    public InstrumentsMusicians(String mId, String mInstrument, String mExperience) {
        this.mId = mId;

        this.mInstrument = mInstrument;
        this.mExperience = mExperience;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmInstrument() {
        return mInstrument;
    }

    public void setmInstrument(String mInstrument) {
        this.mInstrument = mInstrument;
    }

    public String getmExperience() {
        return mExperience;
    }

    public void setmExperience(String mExperience) {
        this.mExperience = mExperience;
    }
}
