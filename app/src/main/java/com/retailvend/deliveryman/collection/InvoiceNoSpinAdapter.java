package com.retailvend.deliveryman.collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.retailvend.R;
import com.retailvend.model.delManModels.delCollection.paymentCollection.InvoiceTypeDatum;
import com.retailvend.model.delManModels.delCollection.paymentCollection.PaymentTypeData;

import java.util.List;

public class InvoiceNoSpinAdapter extends BaseAdapter {
    Context context;

    List<InvoiceTypeDatum> invoiceTypeDataList;

    public InvoiceNoSpinAdapter(Context context, List<InvoiceTypeDatum> invoiceTypeDataList) {
        this.context = context;
        this.invoiceTypeDataList = invoiceTypeDataList;
    }

    @Override
    public int getCount() {
        return invoiceTypeDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return invoiceTypeDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.layout_invoice_no_adapter_spin, viewGroup, false);
        }

        InvoiceTypeDatum invoiceTypeDatum = invoiceTypeDataList.get(i);
        TextView txt_type = view.findViewById(R.id.txt_inv_no);
        txt_type.setText(invoiceTypeDatum.getBillNo());


        return view;
    }
}
