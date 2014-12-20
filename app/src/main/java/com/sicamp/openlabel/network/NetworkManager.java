package com.sicamp.openlabel.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import static java.util.Map.Entry;

/**
 * Created by 1002230 on 12/9/14.
 */
public class NetworkManager {

    private static NetworkManager networkManager;

    public static NetworkManager getInstance() {
        if (networkManager == null) {
            synchronized (NetworkManager.class) {
                if (networkManager == null) {
                    networkManager = new NetworkManager();
                }
            }
        }
        return networkManager;
    }

    public String POST(String addr, HashMap<String, String> param) {
        StringBuffer sb = null;
        String type = "application/x-www-form-urlencoded";
        HttpURLConnection urlCon = null;
        BufferedReader br = null;
        OutputStreamWriter wr = null;
        String line = null;
        try {
            sb = new StringBuffer();
            URL url = new URL(addr);
            urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setRequestProperty("Content-Type", type);
            urlCon.setRequestMethod("POST");
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.connect();
            wr = new OutputStreamWriter(urlCon.getOutputStream(), "utf8");
            wr.write(paramHashMaptoString(param));
            wr.flush();
            br = new BufferedReader(new InputStreamReader(
                    urlCon.getInputStream(), "utf8"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlCon != null) {
                urlCon.disconnect();
            }
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public String GET(String addr, HashMap<String, String> params) {
        StringBuffer sb = null;
        BufferedReader br = null;
        HttpURLConnection urlCon = null;
        String line = null;
        try {
            sb = new StringBuffer();
            URL url = new URL(addr + paramHashMaptoString(params));
            urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setRequestMethod("GET");
            urlCon.setDoInput(true);
            urlCon.connect();
            br = new BufferedReader(new InputStreamReader(
                    urlCon.getInputStream(), "utf-8"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlCon != null) {
                urlCon.disconnect();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private String paramHashMaptoString(HashMap<String, String> hasParams) {
        StringBuilder params = new StringBuilder();
        if (hasParams != null) {
            Iterator iterator = hasParams.entrySet().iterator();

            int index = 0;
            String separator = null;
            while (iterator.hasNext()) {
                if (index == 0) {
                    separator = "?";
                } else {
                    separator = "&";
                }
                Entry entry = (Entry) iterator.next();
                params.append(separator).append(entry.getKey()).append("=").append(entry.getValue());

                index++;
            }
        }
        return String.valueOf(params);
    }
}
