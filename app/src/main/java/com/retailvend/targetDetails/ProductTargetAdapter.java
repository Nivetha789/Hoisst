package com.retailvend.targetDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetProductTarget;

import java.util.List;


public class ProductTargetAdapter extends RecyclerView.Adapter<ProductTargetAdapter.MyViewHolder> {

    Context mContext;
    List<EmployeeTargetProductTarget> empTargetProductTargetList;

    public ProductTargetAdapter(Context mContext, List<EmployeeTargetProductTarget> empTargetProductTargetList1) {
        this.mContext = mContext;
        this.empTargetProductTargetList = empTargetProductTargetList1;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_prod_target_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        EmployeeTargetProductTarget productDataModel = empTargetProductTargetList.get(position);

        holder.prod_name.setText(productDataModel.getDescription());
        holder.target_val.setText(productDataModel.getPdtTargetVal());
        holder.achieve_val.setText(productDataModel.getPdtAchieveVal());
        holder.achieve_percent_val.setText(productDataModel.getPdtAchievePer().toString()+"%");


//        holder.txt_add_to_cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                homeFragment.getProductWiseSellerDetailsList(productDataModel.getcItemCode());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return empTargetProductTargetList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView prod_name, target_val, achieve_val, achieve_percent_val;
        public CardView cardview;


        public MyViewHolder(View view) {
            super(view);

            prod_name = view.findViewById(R.id.prod_name);
            target_val = view.findViewById(R.id.target_val);
            achieve_val = view.findViewById(R.id.achieve_val);
            achieve_percent_val = view.findViewById(R.id.achieve_percent_val);
            cardview = view.findViewById(R.id.cardview);

        }
    }
}