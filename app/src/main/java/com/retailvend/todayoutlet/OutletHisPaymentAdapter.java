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
import com.retailvend.model.outlets.outletHistory.OutletHisPaymentData;
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetBeatTarget;
import com.retailvend.targetDetails.BeatTargetAdapter;

import java.util.List;

public class OutletHisPaymentAdapter extends RecyclerView.Adapter<OutletHisPaymentAdapter.MyViewHolder> {

    Context mContext;
    List<OutletHisPaymentData> outletHisPaymentDataList;

    public OutletHisPaymentAdapter(Context mContext, List<OutletHisPaymentData> paymentDataList) {
        this.mContext = mContext;
        this.outletHisPaymentDataList = paymentDataList;
    }


    @Override
    public OutletHisPaymentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_outlet_his_payment_adapter, parent, false);

        return new OutletHisPaymentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OutletHisPaymentAdapter.MyViewHolder holder, int position) {

        OutletHisPaymentData paymentData = outletHisPaymentDataList.get(position);

        holder.dist_name.setText(paymentData.getDistributorName());
        holder.bill_no.setText(paymentData.getBillNo());
        holder.amount.setText(paymentData.getAmount());
        holder.amountType.setText(paymentData.getAmtType());
        holder.discount.setText(paymentData.getDiscount());
        holder.date.setText(paymentData.getDate());
        holder.collection_type.setText(paymentData.getCollectionType());


    }

    @Override
    public int getItemCount() {
        return outletHisPaymentDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dist_name, bill_no, amount,amountType,discount,date,time,collection_type;
        public CardView cardview;


        public MyViewHolder(View view) {
            super(view);

            dist_name = view.findViewById(R.id.dist_name);
            bill_no = view.findViewById(R.id.bill_no);
            amount = view.findViewById(R.id.amount);
            amountType = view.findViewById(R.id.amountType);
            discount = view.findViewById(R.id.discount);
            collection_type = view.findViewById(R.id.collection_type);
            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            cardview = view.findViewById(R.id.cardview);

        }
    }
}
