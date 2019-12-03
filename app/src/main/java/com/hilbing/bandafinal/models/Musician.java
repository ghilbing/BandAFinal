package com.hilbing.bandafinal.models;

public class Musician {

    String mId;
    String mName;
    String mPhone;
    String mEmail;
    String mUrlProfilePicture;

    public Musician(){

    }

    public Musician(String mId, String mName, String mPhone, String mEmail, String mUrlProfilePicture) {
        this.mId = mId;
        this.mName = mName;
        this.mPhone = mPhone;
        this.mEmail = mEmail;
        this.mUrlProfilePicture = mUrlProfilePicture;
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

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmUrlProfilePicture() {
        return mUrlProfilePicture;
    }

    public void setmUrlProfilePicture(String mUrlProfilePicture) {
        this.mUrlProfilePicture = mUrlProfilePicture;
    }
}
