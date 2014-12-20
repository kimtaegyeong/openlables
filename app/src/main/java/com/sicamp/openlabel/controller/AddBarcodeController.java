package com.sicamp.openlabel.controller;

import android.os.AsyncTask;

import com.sicamp.openlabel.network.NetworkManager;

import java.util.HashMap;

/**
 * Created by 1002230 on 12/20/14.
 */
public class AddBarcodeController extends AsyncTask<String, String, String> {

    private NetworkManager networkManager;

    public AddBarcodeController() {
    }

    @Override
    public void onPreExecute() {
        networkManager = NetworkManager.getInstance();
    }

    @Override
    public String doInBackground(String... params) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("title", params[0]);
        hashMap.put("tag", params[1]);
        hashMap.put("review", params[2]);
        hashMap.put("barcode", params[3]);

        return networkManager.POST("http://applepi.kr/sicamp/", hashMap);
    }

    @Override
    public void onPostExecute(String s) {

    }
}
