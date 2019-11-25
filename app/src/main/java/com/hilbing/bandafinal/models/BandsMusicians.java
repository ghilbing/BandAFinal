package com.hilbing.bandafinal.models;

public class BandsMusicians {

    String mId;
    String mIdBand;
    String mIdMusician;
    String mMusicianRole;

    public BandsMusicians(){}

    public BandsMusicians(String mId, String mIdBand, String mIdMusician, String mMusicianRole) {
        this.mId = mId;
        this.mIdBand = mIdBand;
        this.mIdMusician = mIdMusician;
        this.mMusicianRole = mMusicianRole;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
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
}
