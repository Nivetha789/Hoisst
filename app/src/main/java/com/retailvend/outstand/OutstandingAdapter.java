package com.retailvend.outstand;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.DashboardActivity;
import com.retailvend.R;
import com.retailvend.collection.CollectionAdapter;
import com.retailvend.model.outlets.AssignOutletsDatum;

import java.util.List;

public class OutstandingAdapter extends RecyclerView.Adapter<OutstandingAdapter.MyViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;
    private Activity activity;
    List<AssignOutletsDatum> todayOutletsDatum;

    public OutstandingAdapter(Activity activity, List<AssignOutletsDatum> todayOutletsDatum1) {
        this.activity = activity;
        this.todayOutletsDatum = todayOutletsDatum1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.outstanding_adapter, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AssignOutletsDatum data = todayOutletsDatum.get(position);
        holder.outstandingCardview.setTag(data);
//        String value = "$ " + data.getEsPrice();
        if(data.getAvailableLimit()!=null && !TextUtils.isEmpty(data.getAvailableLimit())){
            holder.txt_avai_no.setText("₹ " + data.getAvailableLimit());
        }else{
            holder.txt_avai_no.setText("₹ 0" );
        }
        holder.txt_name.setText(data.getCompanyName());
        holder.contact_name.setText(data.getContactName());
        holder.contact_number.setText(data.getMobile());
        holder.bal_amnt.setText("₹ 0");

        holder.outstandingCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outstandIntent = new Intent(activity, OutstandingDetailsActivity.class);
                outstandIntent.putExtra("sales_outstand", data);
                activity.startActivity(outstandIntent);
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

        CardView outstandingCardview;
        TextView txt_name, txt_avai_no, contact_name, contact_number,bal_amnt;

        MyViewHolder(View itemView) {
            super(itemView);
            outstandingCardview = itemView.findViewById(R.id.outstanding_cardview);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_avai_no = itemView.findViewById(R.id.txt_avai_no);
            contact_name = itemView.findViewById(R.id.contact_name);
            contact_number = itemView.findViewById(R.id.mob_no);
            bal_amnt = itemView.findViewById(R.id.bal_amnt);
        }
    }
}