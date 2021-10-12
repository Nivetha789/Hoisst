package com.retailvend.todayoutlet;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.outlets.AssignOutletsDatum;

import java.util.List;

public class TodayOutletAdapter extends RecyclerView.Adapter<TodayOutletAdapter.MyViewHolder> {

    private Activity activity;
    private List<AssignOutletsDatum> todayOutletsDatum;


    TodayOutletAdapter(Activity activity, List<AssignOutletsDatum> assignOutletsDatum) {
        this.activity = activity;
        this.todayOutletsDatum=assignOutletsDatum;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.today_outlet_adapter, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AssignOutletsDatum data = todayOutletsDatum.get(position);
        holder.todayOutletCard.setTag(data);
        String companyName = data.getCompanyName();
        String contactName = data.getContactName();
        String companyNumber = data.getMobile();
        holder.compName.setText(companyName);
        holder.contactName.setText(contactName);
        holder.contactNum.setText(companyNumber);
//
        holder.todayOutletCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, TodayOutletDetailsActivity.class);
                i.putExtra("todayOutlet", data);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todayOutletsDatum.size();
    }

//    void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout todayOutletCard;
        TextView compName;
        TextView contactName;
        TextView contactNum;

        MyViewHolder(View itemView) {
            super(itemView);
            todayOutletCard=itemView.findViewById(R.id.today_outlet);
            compName=itemView.findViewById(R.id.shop_title);
            contactName=itemView.findViewById(R.id.contact_name);
            contactNum=itemView.findViewById(R.id.contact_number);
        }
    }
}