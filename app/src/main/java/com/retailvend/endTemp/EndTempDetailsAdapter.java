package com.retailvend.endTemp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.endTempSales.EndTempOutletList;

import java.util.List;

public class EndTempDetailsAdapter extends RecyclerView.Adapter<EndTempDetailsAdapter.MyViewHolder> {

    Context mContext;
    List<EndTempOutletList> endTempOutletListList;

    public EndTempDetailsAdapter(Context mContext, List<EndTempOutletList> empTargetProductTargetList1) {
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

        EndTempOutletList productDataModel = endTempOutletListList.get(position);

        holder.company_name.setText(productDataModel.getCompanyName());
        holder.mobile_no.setText(productDataModel.getMobile());


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
        public TextView company_name, mobile_no;
        public CardView cardview;


        public MyViewHolder(View view) {
            super(view);

            company_name = view.findViewById(R.id.company_name);
            mobile_no = view.findViewById(R.id.mobile_no);
            cardview = view.findViewById(R.id.cardview);

        }
    }
}
