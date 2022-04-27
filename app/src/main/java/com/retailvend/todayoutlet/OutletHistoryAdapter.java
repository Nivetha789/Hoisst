package com.retailvend.todayoutlet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.outlets.outletHistory.OutletHisOrderData;
import com.retailvend.model.outlets.outletHistory.OutletHisPaymentData;
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetBeatTarget;

import java.util.List;

public class OutletHistoryAdapter extends RecyclerView.Adapter<OutletHistoryAdapter.MyViewHolder> {

    Context mContext;
    List<OutletHisOrderData> empTargetBeatTargetList;
    List<OutletHisPaymentData> outletHisPaymentData;

    public OutletHistoryAdapter(Context mContext, List<OutletHisOrderData> empTargetProductTargetList1) {
        this.mContext = mContext;
        this.empTargetBeatTargetList = empTargetProductTargetList1;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_outlet_history_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        OutletHisOrderData outletHisOrderData = empTargetBeatTargetList.get(position);

        holder.order_no.setText(outletHisOrderData.getOrderNo());
        holder.emp_name.setText(outletHisOrderData.getEmpName());
        holder.date.setText(outletHisOrderData.getOrdered());


//        holder.txt_add_to_cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                homeFragment.getProductWiseSellerDetailsList(productDataModel.getcItemCode());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return empTargetBeatTargetList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView order_no, emp_name,date;
        public CardView cardview;


        public MyViewHolder(View view) {
            super(view);

            order_no = view.findViewById(R.id.order_no);
            emp_name = view.findViewById(R.id.emp_name);
            date = view.findViewById(R.id.date);
            cardview = view.findViewById(R.id.cardview);

        }
    }
}
