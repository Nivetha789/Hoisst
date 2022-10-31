package com.retailvend.todayoutlet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.productModel.AddProductModel;

import java.util.List;


public class CreateOutletAdapter extends RecyclerView.Adapter<CreateOutletAdapter.MyViewHolder> {

    Context context;
    List<AddProductModel> addProductLists;
    int index = 0;

    public CreateOutletAdapter(Context mContext, List<AddProductModel> addProductLists) {
        this.context = mContext;
        this.addProductLists = addProductLists;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_main_list, parent, false);
        return new CreateOutletAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        AddProductModel data = addProductLists.get(position);
        holder.txt_name.setText(data.getProduct_name());
        holder.unit.setText(data.getUnit());
        holder.txt_amt.setText(String.valueOf("₹." + data.getPrice()));
        holder.txt_qty.setText("Qty: " + data.getQty());

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateOutletOrderActivity) context).updateAddProductAdapter("remove", index);
            }
        });

    }

    @Override
    public int getItemCount() {
        return addProductLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView txt_name, unit, txt_amt, txt_qty;
        public LinearLayout delBtn;


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