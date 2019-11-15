package com.hilbing.bandafinal.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    public FirebaseInstanceIDService(){}

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        registerTokenServer(token);
    }

    private void registerTokenServer(String token) {

        //OkttpClient
    }
}
