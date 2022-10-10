package com.retailvend.createOutlet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.retailvend.R;
import com.retailvend.model.createOutletModule.CreateOutletBeatListDatum;

import java.util.List;

public class BeatAdapter extends BaseAdapter {
    Context context;

    List<CreateOutletBeatListDatum> createOutletBeatListData;

    public BeatAdapter(Context context, List<CreateOutletBeatListDatum> noReasonMessageData1) {
        this.context = context;
        this.createOutletBeatListData = noReasonMessageData1;
    }

    @Override
    public int getCount() {
        return createOutletBeatListData.size();
    }

    @Override
    public Object getItem(int i) {
        return createOutletBeatListData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.spin_product_item_layout, viewGroup, false);
        }

        CreateOutletBeatListDatum cityDataModel = createOutletBeatListData.get(i);
        TextView txt_spin_type = (TextView) view.findViewById(R.id.txt_spin_state);

        txt_spin_type.setText(cityDataModel.getZoneName());

        return view;
    }
}