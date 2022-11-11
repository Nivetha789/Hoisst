package com.retailvend.todayoutlet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.outlets.AttendanceTypeDatum;
import com.retailvend.utills.CustomToast;
import com.retailvend.utills.SessionManagerSP;

import java.util.List;

public class ButtonTypeAdapter extends RecyclerView.Adapter<ButtonTypeAdapter.MyViewHolder> {

    private Activity activity;
    private List<AttendanceTypeDatum> attendanceTypeDatumList;
    String store_Id = "";
    String otp_value = "";
    private int index = -1;

    SessionManagerSP sessionManagerSP;

    ButtonTypeAdapter(Activity activity, List<AttendanceTypeDatum> assignOutletsDatum, String storeId,String otpValue) {
        this.activity = activity;
        this.attendanceTypeDatumList = assignOutletsDatum;
        this.store_Id = storeId;
        this.otp_value = otpValue;
        this.sessionManagerSP = new SessionManagerSP(activity);
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

        String typeId =sessionManagerSP.getAttendanceId();;
        String typeVal = data.getTypeVal();
        holder.typeValue.setText(typeVal);

//        if(typeVal.equals("Sales Order")){
//            holder.typeValue.setBackgroundResource(R.drawable.lin_storke);
//            holder.typeValue.setTextColor(Color.parseColor("#000000"));
//        }else if(typeVal.equals("Payment Collection")){
//            holder.typeValue.setBackgroundResource(R.drawable.lin_storke);
//            holder.typeValue.setTextColor(Color.parseColor("#000000"));
//        }else{
//            holder.typeValue.setBackgroundResource(R.drawable.lin_storke);
//            holder.typeValue.setTextColor(Color.parseColor("#000000"));
//        }

        holder.typeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();

//                System.out.println("tyyyyyyyyy"+typeVal);
//                if (typeVal.equals("No Order")) {
//                    holder.typeValue.setBackgroundResource(R.drawable.background);
//                    holder.typeValue.setTextColor(Color.parseColor("#FFFFFF"));
//                    ((TodayOutletDetailsActivity) activity).showReason(typeId, typeVal);
//                } else if (typeVal.equals("Sales Order")) {
//                    holder.typeValue.setBackgroundResource(R.drawable.background);
//                    holder.typeValue.setTextColor(Color.parseColor("#FFFFFF"));
//                    sessionManagerSP.setSalesName("");
//                    sessionManagerSP.setSalesNameId("");
//                    Intent intent = new Intent(activity, CreateOutletOrderActivity.class);
//                    intent.putExtra("type_id", typeId);
//                    intent.putExtra("store_id", store_Id);
//                    intent.putExtra("type", typeVal);
//                    intent.putExtra("lat", lat);
//                    intent.putExtra("long", long_val);
//                    activity.startActivity(intent);
//                }
////                else if (typeVal.equals("Payment Collection")) {
////                    holder.typeValue.setBackgroundResource(R.drawable.background);
////                    holder.typeValue.setTextColor(Color.parseColor("#FFFFFF"));
////                    CustomToast.getInstance(activity).showSmallCustomToast("This feature will be enabled shortly");
////                }
//                else {
//                    holder.typeValue.setBackgroundResource(R.drawable.lin_storke);
//                    holder.typeValue.setTextColor(Color.parseColor("#000000"));
//                }
            }
        });

        if (index == position) {
            holder.typeValue.setBackgroundResource(R.drawable.background);
            holder.typeValue.setTextColor(Color.parseColor("#FFFFFF"));

            if (typeVal.equals("No Order")) {
                ((TodayOutletDetailsActivity) activity).showReason(typeId, typeVal);
            } else if (typeVal.equals("Sales Order")) {
                sessionManagerSP.setSalesName("");
                sessionManagerSP.setSalesNameId("");
                Intent intent = new Intent(activity, CreateOutletOrderActivity.class);
                intent.putExtra("store_id", store_Id);
                intent.putExtra("type", typeVal);
                intent.putExtra("otp_value", otp_value);
                activity.startActivity(intent);
                ((TodayOutletDetailsActivity) activity).hideReason(typeId, typeVal);
            }
        } else {
            holder.typeValue.setBackgroundResource(R.drawable.lin_storke);
            holder.typeValue.setTextColor(Color.parseColor("#000000"));
            ((TodayOutletDetailsActivity) activity).hideReason(typeId, typeVal);
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
            typeValue = itemView.findViewById(R.id.checked);
        }
    }
}