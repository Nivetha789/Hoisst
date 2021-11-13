package com.retailvend.sales;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.sales.SalesProductDetail;

import java.util.List;

public class SalesDetailsAdapter extends RecyclerView.Adapter<SalesDetailsAdapter.MyViewHolder> {

    List<SalesProductDetail> productDetails;
    private Activity activity;


    public SalesDetailsAdapter(Activity activity1, List<SalesProductDetail> productDetails1) {
        this.activity = activity1;
        this.productDetails = productDetails1;
    }

    @NonNull
    @Override
    public SalesDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.product_table, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesDetailsAdapter.MyViewHolder holder, int position) {
        SalesProductDetail salesInvoiceList = productDetails.get(position);
        holder.txt_invoice_details_sno.setText(salesInvoiceList.getAutoId());
        holder.txt_invoice_details_pname.setText(salesInvoiceList.getProductName());
        holder.txt_invoice_details_price.setText("\u20B9 " +salesInvoiceList.getPrice());
        holder.txt_invoice_details_nos.setText(salesInvoiceList.getOrderQty());
        holder.txt_invoice_details_kg.setText(salesInvoiceList.getUnitVal());
        holder.txt_invoice_details_total_price.setText("\u20B9 " +salesInvoiceList.getPrice());
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_invoice_details_sno, txt_invoice_details_pname, txt_invoice_details_price,
                txt_invoice_details_nos, txt_invoice_details_kg, txt_invoice_details_total_price;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_invoice_details_sno = itemView.findViewById(R.id.txt_invoice_details_sno);
            txt_invoice_details_pname = itemView.findViewById(R.id.txt_invoice_details_pname);
            txt_invoice_details_price = itemView.findViewById(R.id.txt_invoice_details_price);
            txt_invoice_details_nos = itemView.findViewById(R.id.txt_invoice_details_nos);
            txt_invoice_details_kg = itemView.findViewById(R.id.txt_invoice_details_kg);
            txt_invoice_details_total_price = itemView.findViewById(R.id.txt_invoice_details_total_price);
        }

    }
}