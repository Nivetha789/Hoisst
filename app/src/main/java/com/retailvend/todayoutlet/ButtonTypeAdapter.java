package com.retailvend.todayoutlet;

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

import java.util.List;

public class ButtonTypeAdapter extends RecyclerView.Adapter<ButtonTypeAdapter.MyViewHolder> {

    private Activity activity;
    private List<AttendanceTypeDatum> attendanceTypeDatumList;
    String store_Id="";

    ButtonTypeAdapter(Activity activity, List<AttendanceTypeDatum> assignOutletsDatum, String storeId) {
        this.activity = activity;
        this.attendanceTypeDatumList=assignOutletsDatum;
        this.store_Id=storeId;
    }

    @NonNull
    @Override
    public ButtonTypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.button_type_adapter, viewGroup, false);
        return new ButtonTypeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonTypeAdapter.MyViewHolder holder, int position) {
        AttendanceTypeDatum data = attendanceTypeDatumList.get(position);
        String typeId = data.getTypeId();
        String typeVal = data.getTypeVal();
        holder.typeValue.setText(typeVal);
        if(typeVal.equals("Sales Order")){
            holder.typeValue.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.background));
//            holder.typeValue.setBackgroundColor(Color.parseColor("#163764"));
        }else if(typeVal.equals("Payment Collection")){
            holder.typeValue.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.background1));
//            holder.typeValue.setBackgroundColor(Color.parseColor("#37AC06"));
        }else{
            holder.typeValue.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.background2));
//            holder.typeValue.setBackgroundColor(Color.parseColor("#63A1FF"));
        }


//        holder.todayOutletCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(activity, TodayOutletDetailsActivity.class);
//                i.putExtra("todayOutlet", data);
//                activity.startActivity(i);
//            }
//        });

        holder.typeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CreateOutletOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type_id",typeId);
                bundle.putString("store_id",store_Id);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
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