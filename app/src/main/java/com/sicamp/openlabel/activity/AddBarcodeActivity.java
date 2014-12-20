package com.sicamp.openlabel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sicamp.openlabel.R;
import com.sicamp.openlabel.controller.AddBarcodeController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static android.provider.MediaStore.Images.Media.CONTENT_TYPE;

public class AddBarcodeActivity extends Activity implements View.OnClickListener {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private Uri imageCaptureUri;
    private ImageView photoImageView;

    private Button btnAccept;

//    File file;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    private FileInputStream mFileInputStream = null;
    private URL connectUrl = null;
    private Uri selPhotoUri;

    private EditText editTitle, editTag, editReview;
    Intent intent;
    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barcode);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        init();
        initButton();
        initImageView();
        initEditView();
    }

    void init() {
        intent = getIntent();
        barcode = intent.getStringExtra("barcode");
    }


    void initEditView() {
        editReview = (EditText) findViewById(R.id.edit_review);
        editTag = (EditText) findViewById(R.id.edit_tag);
        editTitle = (EditText) findViewById(R.id.edit_title);
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
        intent.setType(CONTENT_TYPE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA:

                final Bundle extras = intent.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    photoImageView.setImageBitmap(photo);
                }
                break;

            case PICK_FROM_ALBUM:
                selPhotoUri = intent.getData();

            case PICK_FROM_CAMERA:
                Intent intent2 = new Intent("com.android.camera.action.CROP");
                intent2.setDataAndType(imageCaptureUri, "image/*");

                intent2.putExtra("outputX", 300);
                intent2.putExtra("outputY", 300);
                intent2.putExtra("aspectX", 1);
                intent2.putExtra("aspectY", 1);
                intent2.putExtra("sclae", true);
                intent2.putExtra("return-data", true);

                selPhotoUri = intent2.getData();

                startActivityForResult(intent2, CROP_FROM_CAMERA);
                break;

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accept:

                String urlString = "http://applepi.kr/sicamp/file_upload2.php";

                Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null, null);

                c.moveToNext();
                String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

                String s = DoFileUpload(urlString, absolutePath);
                String imgResult = null;
                String imgName = null;
                Log.d("KBS", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    imgResult = jsonObject.getString("result");
                    imgName = jsonObject.getString("imgName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //파일 업로드 시작!
                if (imgResult.equals("200")) {
                    //성공
                    AddBarcodeController addBarcodeController = new AddBarcodeController();
                    try {
                        String result = addBarcodeController.execute(barcode, editTitle.getText().toString(), imgName).get();
                        JSONObject jsonObject = new JSONObject(result);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //에러
                }
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
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, 0);
                break;
            default:

        }
    }

    public String DoFileUpload(String apiUrl, String absolutePath) {
        return HttpFileUpload(apiUrl, "", absolutePath);

    }

    public String HttpFileUpload(String urlString, String params, String fileName) {
        String s = "500";
        try {

            mFileInputStream = new FileInputStream(fileName);
            connectUrl = new URL(urlString);
            Log.d("Test", "mFileInputStream  is " + mFileInputStream);


            // open connection
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            for (int i = 0; i < 1; i++) {

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + "id" + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes("abcde");
                dos.writeBytes(lineEnd);

            }//for


            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            Log.d("Test", "image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test", "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            int ch;
            InputStream is = conn.getInputStream();
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            s = b.toString();
            Log.e("Test", "result = " + s);

            dos.close();

        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            Log.e("Test", "exception " + e.getMessage());
            // TODO: handle exception
        }

        return s;
    }

}
