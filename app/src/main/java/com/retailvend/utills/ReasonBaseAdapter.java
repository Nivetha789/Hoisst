package com.retailvend.utills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.retailvend.R;
import com.retailvend.model.noreasonOutlet.NoReasonMessageDatum;

import java.util.List;

public class ReasonBaseAdapter extends BaseAdapter {
    Context context;

    List<NoReasonMessageDatum> noReasonMessageData;

    public ReasonBaseAdapter(Context context, List<NoReasonMessageDatum> noReasonMessageData1) {
        this.context = context;
        this.noReasonMessageData = noReasonMessageData1;
    }

    @Override
    public int getCount() {
        return noReasonMessageData.size();
    }

    @Override
    public Object getItem(int i) {
        return noReasonMessageData.get(i);
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

        NoReasonMessageDatum cityDataModel = noReasonMessageData.get(i);
        TextView txt_spin_type = (TextView) view.findViewById(R.id.txt_spin_state);

        txt_spin_type.setText(cityDataModel.getMessage());


        return view;
    }
}
