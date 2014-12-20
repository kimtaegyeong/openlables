package com.sicamp.openlabel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sicamp.openlabel.HotDto;
import com.sicamp.openlabel.R;
import com.sicamp.openlabel.adapter.HotListAdapter;

import java.util.ArrayList;

import static android.view.View.OnClickListener;


public class MainActivity extends Activity implements OnClickListener {


    private Button btnSearch, btnCamera;
    private EditText editSearch;
    private ListView listview;
    private ArrayList<HotDto> hotDtos;
    private HotDto hotDto = null;
    private HotListAdapter hotAdapter;

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


        hotDto = new HotDto("left1", "right1", "imgL1", "imgR1", 80, 20, 60, 40);
        hotAdapter.add(hotDto);
        hotDto = new HotDto("left2", "right2", "imgL2", "imgR2", 80, 20, 20, 80);
        hotAdapter.add(hotDto);
        hotDto = new HotDto("left3", "right3", "imgL3", "imgR3", 70, 30, 60, 40);
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


            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }
}
