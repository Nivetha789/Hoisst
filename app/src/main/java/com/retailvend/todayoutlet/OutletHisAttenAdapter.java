package com.retailvend.todayoutlet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.outlets.outletHistory.OutletHisAttendanceData;
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetBeatTarget;
import com.retailvend.targetDetails.BeatTargetAdapter;

import java.util.List;

public class OutletHisAttenAdapter extends RecyclerView.Adapter<OutletHisAttenAdapter.MyViewHolder> {

    Context mContext;
    List<OutletHisAttendanceData> outletHisAttendanceDataList;

    public OutletHisAttenAdapter(Context mContext, List<OutletHisAttendanceData> empTargetProductTargetList1) {
        this.mContext = mContext;
        this.outletHisAttendanceDataList = empTargetProductTargetList1;
    }


    @Override
    public OutletHisAttenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_outlet_his_attendance_adapter, parent, false);

        return new OutletHisAttenAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OutletHisAttenAdapter.MyViewHolder holder, int position) {

        OutletHisAttendanceData attendanceData = outletHisAttendanceDataList.get(position);

        holder.employee_name.setText(attendanceData.getEmployeeName());
        holder.atten_type.setText(attendanceData.getAttendanceType());
        holder.date.setText(attendanceData.getAttendanceDate());
        holder.time.setText(attendanceData.getAttendanceTime());


    }

    @Override
    public int getItemCount() {
        return outletHisAttendanceDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView employee_name, atten_type, date, time;
        public CardView cardview;


        public MyViewHolder(View view) {
            super(view);

            employee_name = view.findViewById(R.id.employee_name);
            atten_type = view.findViewById(R.id.atten_type);
            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            cardview = view.findViewById(R.id.cardview);

        }
    }
}
