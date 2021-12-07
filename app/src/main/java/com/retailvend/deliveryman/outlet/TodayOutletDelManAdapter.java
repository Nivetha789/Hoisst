package com.retailvend.deliveryman.outlet;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.delManModels.delCollection.todayOutletsModel.DeliveryTodayOutletsDatum;

import java.util.List;

public class TodayOutletDelManAdapter extends RecyclerView.Adapter<TodayOutletDelManAdapter.MyViewHolder> {

    private Activity activity;
    List<DeliveryTodayOutletsDatum> todayOutletsDatum;


    TodayOutletDelManAdapter(Activity activity, List<DeliveryTodayOutletsDatum> assignOutletsDatum) {
        this.activity = activity;
        this.todayOutletsDatum=assignOutletsDatum;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.today_outlet_del_man_adapter, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DeliveryTodayOutletsDatum data = todayOutletsDatum.get(position);
        holder.todayOutletCard.setTag(data);
        String companyName = data.getStoreName();
        String billNo = data.getBillType();
        String invoiceNo = data.getInvoiceNo();
        String date = data.getOrdered();
//        String assignID = data.geta();
        String randomVal = data.getRandomValue();
        holder.compName.setText(companyName);

        if(billNo.equals("1")){
            holder.bill_no.setText("COD");
        }else if(billNo.equals("2")){
            holder.bill_no.setText("Credit");
        }
        holder.invoice.setText(invoiceNo);
        holder.date.setText(date);

        holder.todayOutletCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, OutletInvoiceDetailsActivity.class);
//                i.putExtra("assign_id",)
                i.putExtra("random_value", randomVal);
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
        TextView bill_no;
        TextView invoice;
        TextView date;

        MyViewHolder(View itemView) {
            super(itemView);
            todayOutletCard=itemView.findViewById(R.id.today_outlet);
            compName=itemView.findViewById(R.id.shop_title);
            bill_no=itemView.findViewById(R.id.bill_no);
            invoice=itemView.findViewById(R.id.invoice);
            date=itemView.findViewById(R.id.date);
        }
    }
}