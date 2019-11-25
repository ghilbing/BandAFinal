package com.hilbing.bandafinal.models;

public class Band {
    String mId;
    String mBandName;


    public Band() {
    }

    public Band(String mId, String mBandName) {
        this.mId = mId;
        this.mBandName = mBandName;

    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmBandName() {
        return mBandName;
    }

    public void setmBandName(String mBandName) {
        this.mBandName = mBandName;
    }
}
