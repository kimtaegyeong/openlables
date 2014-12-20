package com.sicamp.openlabel.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageLoad extends AsyncTask<String, Integer, Bitmap> {


    ImageView imageView;
    String image_url;

    public ImageLoad(ImageView imageView) {

        this.imageView = imageView;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }

    protected Bitmap doInBackground(String... strData) {

        image_url = strData[0];

        Bitmap image = ImageCache.getImage(image_url);

        if (image == null) {

            image = ImageDownLoad(image_url);

            ImageCache.setImage(image_url, image);

        }

        return image;

    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Bitmap bm) {

        imageView.setImageBitmap(bm);

    }

    @Override
    protected void onCancelled() {

        // TODO Auto-generated method stub

        super.onCancelled();

    }

    public Bitmap ImageDownLoad(String image_url) {

        try {

            URL url = new URL(image_url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.connect();

            BufferedInputStream bis = new BufferedInputStream(
                    conn.getInputStream());

            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();

            return bm;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
            // TODO: handle exception

        }

    }
}
