package com.retailvend.todayoutlet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.outlets.outletHistory.OutletHisAttendanceData;
import com.retailvend.model.outlets.outletHistory.OutletHisInvoiceData;

import java.util.List;

public class OutletHisInvoiceAdapter extends RecyclerView.Adapter<OutletHisInvoiceAdapter.MyViewHolder> {

    Context mContext;
    List<OutletHisInvoiceData> outletHisAttendanceDataList;

    public OutletHisInvoiceAdapter(Context mContext, List<OutletHisInvoiceData> outletHisAttendanceDataList) {
        this.mContext = mContext;
        this.outletHisAttendanceDataList = outletHisAttendanceDataList;
    }


    @Override
    public OutletHisInvoiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_outlet_his_invoice_adapter, parent, false);

        return new OutletHisInvoiceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OutletHisInvoiceAdapter.MyViewHolder holder, int position) {

        OutletHisInvoiceData attendanceData = outletHisAttendanceDataList.get(position);

        holder.invoice_no.setText(attendanceData.getInvoiceNo());
        holder.dist_name.setText(attendanceData.getDistributorName());
        holder.del_employee.setText(attendanceData.getDeliveryEmployee());
        holder.inv_date.setText(attendanceData.getInvoiceDate());
        holder.del_date.setText(attendanceData.getDeliveryDate());

    }

    @Override
    public int getItemCount() {
        return outletHisAttendanceDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView invoice_no, dist_name, del_employee, inv_date,del_date;
        public CardView cardview;


        public MyViewHolder(View view) {
            super(view);

            invoice_no = view.findViewById(R.id.invoice_no);
            dist_name = view.findViewById(R.id.dist_name);
            del_employee = view.findViewById(R.id.del_employee);
            inv_date = view.findViewById(R.id.inv_date);
            del_date = view.findViewById(R.id.del_date);
            cardview = view.findViewById(R.id.cardview);

        }
    }
}
