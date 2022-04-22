package com.retailvend.deliveryman.InvoiceList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.delManModels.delCollection.invoiceDetails.InvoiceProductDetail;
import com.retailvend.model.sales.SalesProductDetail;

import java.util.List;

public class InvoiceDetailsAdapter extends RecyclerView.Adapter<InvoiceDetailsAdapter.MyViewHolder> {

    List<InvoiceProductDetail> productDetails;
    private Activity activity;


    public InvoiceDetailsAdapter(Activity activity1, List<InvoiceProductDetail> productDetails1) {
        this.activity = activity1;
        this.productDetails = productDetails1;
    }

    @NonNull
    @Override
    public InvoiceDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.product_table, viewGroup, false);
        return new InvoiceDetailsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceDetailsAdapter.MyViewHolder holder, int position) {
        InvoiceProductDetail invoiceList = productDetails.get(position);
        holder.txt_invoice_details_sno.setText(String.valueOf(position+1));
        holder.txt_invoice_details_pname.setText(invoiceList.productName);
        double a = Double.parseDouble(invoiceList.price.trim());
        int b = Integer.parseInt(invoiceList.orderQty.trim());
        double totalPrice=a*b;
        holder.txt_invoice_details_price.setText("\u20B9 " +invoiceList.price);
        holder.txt_invoice_details_nos.setText(invoiceList.orderQty);
        holder.txt_invoice_details_kg.setText(invoiceList.unitVal);
        holder.txt_invoice_details_total_price.setText("\u20B9 " +totalPrice);
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
