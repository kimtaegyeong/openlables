package com.sicamp.openlabel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicamp.openlabel.R;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class AddBarcodeActivity extends Activity implements View.OnClickListener {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private Uri imageCaptureUri;
    private ImageView photoImageView;

    private Button btnAccept;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barcode);

        initButton();
        initImageView();

    }

    void initButton() {
        btnAccept = (Button) findViewById(R.id.btn_accept);
        btnAccept.setOnClickListener(this);
    }

    void initImageView() {
        photoImageView = (ImageView) findViewById(R.id.image_add);
        photoImageView.setOnClickListener(this);

    }

    void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        imageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);

        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    void takePhotoFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);


    }

    public static byte[] convertTextFileToByteArray(File file) {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }
        return bFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA:
                final Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    photoImageView.setImageBitmap(photo);
                }

                file = new File(imageCaptureUri.getPath());
                Log.d("file", file.toString());
                break;

            case PICK_FROM_ALBUM:
                imageCaptureUri = data.getData();
            case PICK_FROM_CAMERA:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imageCaptureUri, "image/*");

                intent.putExtra("outputX", 300);
                intent.putExtra("outputY", 300);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("sclae", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);
                break;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accept:

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams param = new RequestParams();
                try {
                    param.put("fileToUpload", file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                client.post("http://applepi.kr/sicamp/file_upload.php", param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("KKKK", "SUCCESS : " +statusCode + " / " + responseBody.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

                break;
            case R.id.image_add:
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBarcodeActivity.this);
                builder.setPositiveButton("앨범", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        takePhotoFromAlbum();
                    }
                });
                builder.setNegativeButton("카메라", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        takePhotoFromCamera();
                    }
                });
                builder.create().show();

                break;
            default:

        }
    }

    class FuckingAsync extends AsyncTask<String, Integer, HttpResponse> {

        private ArrayList<NameValuePair> list;
        HttpResponse res;

        FuckingAsync() {
            list = new ArrayList<>();
        }
        @Override
        protected HttpResponse doInBackground(String... strings) {
            try {
                HttpPost httpPost = new HttpPost("http://applepi.kr/sicamp/file_upload.php");
                String s = Base64.encodeToString(convertTextFileToByteArray(file), Base64.DEFAULT);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                builder.setBoundary("KKK");
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.setCharset(Charset.forName("UTF-8"));

                builder.addBinaryBody("fileToUpload", file);
                HttpEntity entity = builder.build();

                httpPost.setEntity(entity);
                HttpClient client = new DefaultHttpClient();
                res = client.execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HttpResponse httpResponse) {
            try {
                Log.d("kkkk", EntityUtils.toString(res.getEntity()));
            } catch (IOException e) {

            }
        }
    }
}
