package com.sicamp.openlabel.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sicamp.openlabel.HotDto;
import com.sicamp.openlabel.R;

import java.util.ArrayList;

public class HotListAdapter extends ArrayAdapter<HotDto> {
    private Context context = null;
    private ArrayList<HotDto> hotDtos = new ArrayList<>();

    public HotListAdapter(Context context, int viewResourceId, ArrayList<HotDto> hotDtos) {
        super(context, viewResourceId, hotDtos);
        this.context = context;
        this.hotDtos = hotDtos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_mainlist, null);

        }
        HotDto hotDto = hotDtos.get(position);
        TextView tvL, tvR, tvRecommendLeft, tvaVoidLeft, tvRecommendRight, tvaVoidRight;
        ProgressBar progressBarLeft, progressBarRight;

        if (hotDto != null) {
            tvL = (TextView) v.findViewById(R.id.text_left);
            tvR = (TextView) v.findViewById(R.id.text_right);
            tvRecommendLeft = (TextView) v.findViewById(R.id.text_progress_recommend_left);
            tvaVoidLeft = (TextView) v.findViewById(R.id.text_progress_aVoid_left);
            tvRecommendRight = (TextView) v.findViewById(R.id.text_progress_recommend_right);
            tvaVoidRight = (TextView) v.findViewById(R.id.text_progress_aVoid_right);

            progressBarLeft = (ProgressBar) v.findViewById(R.id.progressBar_left);
            progressBarRight = (ProgressBar) v.findViewById(R.id.progressBar_right);

            int reL =hotDto.getRecommendLeft();
            int avL =hotDto.getaVoidLeft();
            int reR =hotDto.getRecommendRight();
            int avR =hotDto.getaVoidRight();

            tvL.setText(hotDto.getTextLeft());
            tvR.setText(hotDto.getTextRight());
            tvRecommendLeft.setText(String.valueOf(reL));
            tvaVoidLeft.setText(String.valueOf(avL));
            tvRecommendRight.setText(String.valueOf(reR));
            tvaVoidRight.setText(String.valueOf(avR));

            progressBarLeft.setProgress((reL*100/(reL+avL)));
            progressBarRight.setProgress((reR*100/(reR+avR)));
        }
        return v;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return hotDtos.size();
    }

    @Override
    public HotDto getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
}