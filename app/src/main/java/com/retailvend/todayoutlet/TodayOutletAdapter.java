package com.retailvend.todayoutlet;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.DashboardActivity;
import com.retailvend.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayOutletAdapter extends RecyclerView.Adapter<TodayOutletAdapter.MyViewHolder> {

    private Activity activity;


    TodayOutletAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.today_outlet_adapter, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        BuyEShareListData data = datas.get(position);
//        holder.cardView.setTag(data);
//        String value = "$ " + data.getEsPrice();
//        holder.cost.setText(value);
//        if (data.getEsName().equalsIgnoreCase("Gold")) {
//            holder.backgroundImg.setBackground(ContextCompat.getDrawable(activity, R.drawable.gold));
//        } else if (data.getEsName().equalsIgnoreCase("Silver")) {
//            holder.backgroundImg.setBackground(ContextCompat.getDrawable(activity, R.drawable.silver));
//        } else if (data.getEsName().equalsIgnoreCase("diamond")) {
//            holder.backgroundImg.setBackground(ContextCompat.getDrawable(activity, R.drawable.diamond));
//        } else if (data.getEsName().equalsIgnoreCase("Platinum")) {
//            holder.backgroundImg.setBackground(ContextCompat.getDrawable(activity, R.drawable.platinum));
//        } else if (data.getEsName().equalsIgnoreCase("Bronze")) {
//            holder.backgroundImg.setBackground(ContextCompat.getDrawable(activity, R.drawable.bronze));
//        }
//        if((datas.size()-1)==position){
//            holder.view.setVisibility(View.VISIBLE);
//        }
        holder.todayOutletCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, TodayOutletDetailsActivity.class);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

//    void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView todayOutletCard;


        MyViewHolder(View itemView) {
            super(itemView);
            todayOutletCard=itemView.findViewById(R.id.today_outlet);
        }
    }
}