package com.sicamp.openlabel.controller;

import android.os.AsyncTask;

import com.sicamp.openlabel.network.NetworkManager;

import java.util.HashMap;

/**
 * Created by 1002230 on 12/20/14.
 */
public class BarcodeCheckController extends AsyncTask<String, String, String> {

    private NetworkManager networkManager;

    public BarcodeCheckController() {
    }

    @Override
    public void onPreExecute() {
        networkManager = NetworkManager.getInstance();
    }

    @Override
    public String doInBackground(String... params) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("barcode", params[0]);

        return networkManager.GET("http://applepi.kr/sicamp/barcode.php", hashMap);
    }

    @Override
    public void onPostExecute(String s) {

    }
}
