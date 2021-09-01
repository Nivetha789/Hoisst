package com.retailvend.todayoutlet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.productModel.AddProductModel;

import java.util.ArrayList;
import java.util.List;


public class CreateOutletAdapter extends RecyclerView.Adapter<CreateOutletAdapter.MyViewHolder> {

    Context context;
    List<AddProductModel> addProductList = new ArrayList<>();
    int index=0;

    public CreateOutletAdapter(Context context, List<AddProductModel> addProductLists) {
        this.context = context;
        this.addProductList = addProductLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_main_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  int position) {
        index=position;
        AddProductModel data = addProductList.get(position);
        holder.txt_name.setText(data.getName());
        holder.unit.setText(data.getUnit());
        holder.txt_amt.setText(data.getPrice());
        holder.txt_qty.setText(data.getQty());

//        holder.delBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((CreateOutletOrderActivity) context).updateAddProductAdapter("remove",index);
//            }
//        });

//        holder.delBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                notifyDataSetChanged();
//                ((CreateOutletOrderActivity) context).updateAddProductAdapter("remove",position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return addProductList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txt_name, unit, txt_amt, txt_qty;
        LinearLayout delBtn;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            txt_name = view.findViewById(R.id.txt_name);
            unit = view.findViewById(R.id.unit);
            txt_amt = view.findViewById(R.id.txt_amt);
            txt_qty = view.findViewById(R.id.txt_qty);
            delBtn = view.findViewById(R.id.delBtn);
        }
    }
}