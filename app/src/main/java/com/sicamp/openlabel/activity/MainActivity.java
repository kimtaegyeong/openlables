package com.sicamp.openlabel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sicamp.openlabel.HotDto;
import com.sicamp.openlabel.R;
import com.sicamp.openlabel.adapter.HotListAdapter;
import com.sicamp.openlabel.controller.BarcodeCheckController;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.view.View.OnClickListener;


public class MainActivity extends Activity implements OnClickListener {


    private Button btnSearch, btnCamera;
    private EditText editSearch;
    private ListView listview;
    private ArrayList<HotDto> hotDtos;
    private HotDto hotDto = null;
    private HotListAdapter hotAdapter;

    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButton();
        initEditText();
        initListView();
    }

    void initButton() {
        btnCamera = (Button) findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(this);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
    }

    void initListView() {
        hotDtos = new ArrayList<>();
        hotAdapter = new HotListAdapter(MainActivity.this, R.layout.item_mainlist, hotDtos);


        hotDto = new HotDto("left1", "right1", "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQoMzU7CyBpNPy5dy4jcFIi9pHfDT98Ml_un0wxB0nsTJ1jnVD_cA", "imgR1", 80, 20, 60, 40);
        hotAdapter.add(hotDto);
        hotDto = new HotDto("left2", "right2", "imgL2", "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQoMzU7CyBpNPy5dy4jcFIi9pHfDT98Ml_un0wxB0nsTJ1jnVD_cA", 80, 20, 20, 80);
        hotAdapter.add(hotDto);
        hotDto = new HotDto("left3", "right3", "imgL3", "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQoMzU7CyBpNPy5dy4jcFIi9pHfDT98Ml_un0wxB0nsTJ1jnVD_cA", 70, 30, 60, 40);
        hotAdapter.add(hotDto);

        listview = (ListView) findViewById(R.id.list);

        listview.setAdapter(hotAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    void initEditText() {
        editSearch = (EditText) findViewById(R.id.edit_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_search:
                startActivity(new Intent(MainActivity.this, AddBarcodeActivity.class));
                break;

            default:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");

                barcode = contents;
                BarcodeCheckController barcodeCheckController = new BarcodeCheckController();
                try {
                    String s = barcodeCheckController.execute(barcode).get();
                    Log.d("kkk = ", s);
                    if (s.equals("500")) {
                        Intent intent = new Intent(MainActivity.this, AddBarcodeActivity.class);
                        intent.putExtra("barcode", barcode);
                        startActivity(intent);
                    } else {
                        //바코드가 잇을대
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }
}
