package com.hilbing.bandafinal.models;

public class BandsMusicians {


    String mIdBand;
    String mIdMusician;
    String mMusicianRole;
    String mSince;

    public BandsMusicians(){}

    public BandsMusicians(String mIdBand, String mIdMusician, String mMusicianRole, String mSince) {
        this.mIdBand = mIdBand;
        this.mIdMusician = mIdMusician;
        this.mMusicianRole = mMusicianRole;
        this.mSince = mSince;
    }

    public String getmIdBand() {
        return mIdBand;
    }

    public void setmIdBand(String mIdBand) {
        this.mIdBand = mIdBand;
    }

    public String getmIdMusician() {
        return mIdMusician;
    }

    public void setmIdMusician(String mIdMusician) {
        this.mIdMusician = mIdMusician;
    }

    public String getmMusicianRole() {
        return mMusicianRole;
    }

    public void setmMusicianRole(String mMusicianRole) {
        this.mMusicianRole = mMusicianRole;
    }

    public String getmSince() {
        return mSince;
    }

    public void setmSince(String mSince) {
        this.mSince = mSince;
    }
}
