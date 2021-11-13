package com.retailvend.utills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.retailvend.R;
import com.retailvend.model.outlets.ProductTypeDatum;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    Context context;

    List<ProductTypeDatum> productNameResData;

    public ProductAdapter(Context context, List<ProductTypeDatum> cityDataModelList) {
        this.context = context;
        this.productNameResData = cityDataModelList;
    }

    @Override
    public int getCount() {
        return productNameResData.size();
    }

    @Override
    public Object getItem(int i) {
        return productNameResData.get(i);
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

        ProductTypeDatum cityDataModel = productNameResData.get(i);
        TextView txt_spin_type = (TextView) view.findViewById(R.id.txt_spin_state);

        txt_spin_type.setText(cityDataModel.getDescription());


        return view;
    }
}
