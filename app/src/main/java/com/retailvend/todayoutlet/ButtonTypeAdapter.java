package com.retailvend.todayoutlet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.model.outlets.AttendanceTypeDatum;
import com.retailvend.utills.CustomToast;

import java.util.List;

public class ButtonTypeAdapter extends RecyclerView.Adapter<ButtonTypeAdapter.MyViewHolder> {

    private Activity activity;
    private List<AttendanceTypeDatum> attendanceTypeDatumList;
    String store_Id="";
    String long_val="";
    String lat="";
    private int index = -1;

    ButtonTypeAdapter(Activity activity, List<AttendanceTypeDatum> assignOutletsDatum, String storeId,String lat1, String long_val1) {
        this.activity = activity;
        this.attendanceTypeDatumList=assignOutletsDatum;
        this.store_Id=storeId;
        this.lat=lat1;
        this.long_val=long_val1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.button_type_adapter, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AttendanceTypeDatum data = attendanceTypeDatumList.get(position);

        String typeId = data.getTypeId();
        String typeVal = data.getTypeVal();
        holder.typeValue.setText(typeVal);

        holder.typeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
//                System.out.println("tyyyyyyyyy"+typeVal);
                if(typeVal.equals("No Order")){
                    holder.typeValue.setBackgroundResource(R.drawable.background3);
                    holder.typeValue.setTextColor(Color.parseColor("#FFFFFF"));
                    ((TodayOutletDetailsActivity) activity).showReason(typeId,typeVal);
                } else if (typeVal.equals("Sales Order")) {
                    Intent intent = new Intent(activity, CreateOutletOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type_id", typeId);
                    bundle.putString("store_id", store_Id);
                    bundle.putString("type", typeVal);
                    bundle.putString("lat", lat);
                    bundle.putString("long", long_val);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                    activity.finish();
                }else if(typeVal.equals("Payment Collection")){
                    CustomToast.getInstance(activity).showSmallCustomToast("This feature will be enabled shortly");
                }
            }
        });
        if(typeVal.equals("Sales Order")){
            holder.typeValue.setBackgroundResource(R.drawable.background);
//            holder.typeValue.setBackgroundColor(Color.parseColor("#163764"));
        }else if(typeVal.equals("Payment Collection")){
            holder.typeValue.setBackgroundResource(R.drawable.background1);
//            holder.typeValue.setBackgroundColor(Color.parseColor("#37AC06"));
        }else{
            holder.typeValue.setBackgroundResource(R.drawable.background2);
//            holder.typeValue.setBackgroundColor(Color.parseColor("#63A1FF"));
        }
    }

    @Override
    public int getItemCount() {
        return attendanceTypeDatumList.size();
    }

//    void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView typeValue;

        MyViewHolder(View itemView) {
            super(itemView);
            typeValue=itemView.findViewById(R.id.checked);
        }
    }
}