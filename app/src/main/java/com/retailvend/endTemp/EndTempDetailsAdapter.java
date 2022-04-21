package com.retailvend.endTemp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.endTempSales.EndTempData;
import com.retailvend.model.endTempSales.EndTempOutlet;
import com.retailvend.model.endTempSales.EndTempOutletAttendance;
import com.retailvend.model.endTempSales.EndTempOutletList;

import java.util.ArrayList;
import java.util.List;

public class EndTempDetailsAdapter extends RecyclerView.Adapter<EndTempDetailsAdapter.MyViewHolder> {

    Context mContext;
    List<EndTempOutlet> endTempOutletListList;

    public EndTempDetailsAdapter(Context mContext, List<EndTempOutlet> empTargetProductTargetList1) {
        this.mContext = mContext;
        this.endTempOutletListList = empTargetProductTargetList1;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_end_temp_details_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        EndTempOutlet endTempOutlet = endTempOutletListList.get(position);

        holder.company_name.setText(endTempOutlet.getCompanyName());
        holder.mobile_no.setText(endTempOutlet.getMobile());
        if(endTempOutlet.getAttendanceList().size()!=0){
            holder.order_no.setText(endTempOutlet.getAttendanceList().get(0).getOrderNo());
            holder.attendance_details.setText(endTempOutlet.getAttendanceList().get(0).getAttendanceType());
            holder.order_value.setText(endTempOutlet.getAttendanceList().get(0).getOrderTotal());
            holder.time.setText(endTempOutlet.getAttendanceList().get(0).getInTime()+" - "+endTempOutlet.getAttendanceList().get(0).getOutTime());
        }

//        holder.txt_add_to_cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                homeFragment.getProductWiseSellerDetailsList(productDataModel.getcItemCode());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return endTempOutletListList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView company_name, mobile_no,order_no,attendance_details,order_value,time;
        public CardView cardview;


        public MyViewHolder(View view) {
            super(view);

            company_name = view.findViewById(R.id.company_name);
            mobile_no = view.findViewById(R.id.mobile_no);
            cardview = view.findViewById(R.id.cardview);
            order_no = view.findViewById(R.id.order_no);
            attendance_details = view.findViewById(R.id.attendance_details);
            order_value = view.findViewById(R.id.order_value);
            time = view.findViewById(R.id.time);

        }
    }
}
