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
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetBeatTarget;
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetProductTarget;

import java.util.List;


public class BeatTargetAdapter extends RecyclerView.Adapter<BeatTargetAdapter.MyViewHolder> {

    Context mContext;
    List<EmployeeTargetBeatTarget> empTargetBeatTargetList;

    public BeatTargetAdapter(Context mContext, List<EmployeeTargetBeatTarget> empTargetProductTargetList1) {
        this.mContext = mContext;
        this.empTargetBeatTargetList = empTargetProductTargetList1;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_beat_target_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        EmployeeTargetBeatTarget productDataModel = empTargetBeatTargetList.get(position);

        holder.zone_name.setText(productDataModel.getZoneName());
        holder.target_val.setText(productDataModel.getBeatTargetVal());
        holder.achieve_val.setText(productDataModel.getBeatAchieveVal());
        holder.achieve_percent_beat_val.setText(productDataModel.getBeatAchievePer().toString());


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
        public TextView zone_name, target_val, achieve_val, achieve_percent_beat_val;
        public CardView cardview;


        public MyViewHolder(View view) {
            super(view);

            zone_name = view.findViewById(R.id.zone_name);
            target_val = view.findViewById(R.id.target_val);
            achieve_val = view.findViewById(R.id.achieve_val);
            achieve_percent_beat_val = view.findViewById(R.id.achieve_percent_beat_val);
            cardview = view.findViewById(R.id.cardview);

        }
    }
}