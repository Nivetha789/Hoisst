package com.retailvend;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.*;

import java.util.ArrayList;
import java.util.List;

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
    }

    @Override
    public int getItemCount() {
        return 6;
    }

//    void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MyViewHolder(View itemView) {
            super(itemView);
        }

    }
}