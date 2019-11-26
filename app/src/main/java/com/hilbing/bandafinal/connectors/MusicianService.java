package com.hilbing.bandafinal.connectors;

import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.connection.ListenHashProvider;
import com.google.gson.Gson;
import com.hilbing.bandafinal.models.Musician;

import java.util.HashMap;
import java.util.Map;

public class MusicianService {

    private static final String ENDPOINT = "https://api.spotify.com/v1/me";
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Musician musician;


    public MusicianService(RequestQueue queue, SharedPreferences sharedPreferences){
        this.queue = queue;
        this.sharedPreferences = sharedPreferences;
    }

    public Musician getMusician(){
        return musician;
    }

    public void get(final VolleyCallBack callBack){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, null, response -> {
            Gson gson = new Gson();
            musician = gson.fromJson(response.toString(), Musician.class);
            callBack.onSuccess();
        }, error -> get(() -> {

        })) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;

            }
        };

        queue.add(jsonObjectRequest);
    }

}
