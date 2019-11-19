package com.hilbing.bandafinal.models;

public class Musician {

    String mId;
    String mName;
    String mPhone;


    public Musician(){

    }

    public Musician(String mId, String mName, String mPhone) {
        this.mId = mId;
        this.mName = mName;
        this.mPhone = mPhone;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }
}
