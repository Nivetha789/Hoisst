package com.retailvend.deliveryman.outlet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsProductDetail;
import com.retailvend.model.sales.SalesProductDetail;

import java.util.List;

public class OutletInvoiceDetailsAdapter extends RecyclerView.Adapter<OutletInvoiceDetailsAdapter.MyViewHolder> {

    List<TodayOutletDetailsProductDetail> productDetails;
    private Activity activity;


    public OutletInvoiceDetailsAdapter(Activity activity1, List<TodayOutletDetailsProductDetail> productDetails1) {
        this.activity = activity1;
        this.productDetails = productDetails1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.outlet_invoice_details_adapter, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TodayOutletDetailsProductDetail productDetail = productDetails.get(position);
        holder.txt_invoice_details_sno.setText(String.valueOf(position+1));
        holder.txt_invoice_details_pname.setText(productDetail.getProductName());
        holder.hsncode.setText(productDetail.getHsnCode());
        double a = Double.parseDouble(productDetail.getPrice().trim());
        int b = Integer.parseInt(productDetail.getOrderQty().trim());
        double totalPrice=a*b;
        holder.txt_invoice_details_qty.setText(productDetail.getOrderQty());
        holder.txt_invoice_details_rate.setText("\u20B9 "+productDetail.getPrice());
        holder.txt_invoice_details_nos.setText(productDetail.getUnitName());
        holder.txt_invoice_details_total_price.setText("\u20B9 " +totalPrice);
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_invoice_details_sno, txt_invoice_details_pname, hsncode,
                txt_invoice_details_qty,txt_invoice_details_rate,txt_invoice_details_nos,txt_invoice_details_total_price;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_invoice_details_sno = itemView.findViewById(R.id.txt_invoice_details_sno);
            txt_invoice_details_pname = itemView.findViewById(R.id.txt_invoice_details_pname);
            hsncode = itemView.findViewById(R.id.hsncode);
            txt_invoice_details_qty = itemView.findViewById(R.id.txt_invoice_details_qty);
            txt_invoice_details_rate = itemView.findViewById(R.id.txt_invoice_details_rate);
            txt_invoice_details_nos = itemView.findViewById(R.id.txt_invoice_details_nos);
            txt_invoice_details_total_price = itemView.findViewById(R.id.txt_invoice_details_total_price);
        }

    }
}