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

import com.retailvend.R;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OutstandingAdapter extends RecyclerView.Adapter<BaseViewHolder>  {

    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;
    List<AssignOutletsDatum> todayOutletsDatum;

    public OutstandingAdapter(Activity activity, List<AssignOutletsDatum> todayOutletsDatum1) {
        this.activity = activity;
        this.todayOutletsDatum = todayOutletsDatum1;
    }

//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(activity).inflate(R.layout.outstanding_adapter, viewGroup, false);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        AssignOutletsDatum data = todayOutletsDatum.get(position);
//        holder.outstandingCardview.setTag(data);
////        String value = "$ " + data.getEsPrice();
//        if(data.getAvailableLimit()!=null && !TextUtils.isEmpty(data.getAvailableLimit())){
//            holder.txt_avai_no.setText("₹ " + data.getAvailableLimit());
//        }else{
//            holder.txt_avai_no.setText("₹ 0" );
//        }
//        holder.txt_name.setText(data.getCompanyName());
//        holder.contact_name.setText(data.getContactName());
//        holder.contact_number.setText(data.getMobile());
//        holder.bal_amnt.setText("₹ 0");
//
//        holder.outstandingCardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent outstandIntent = new Intent(activity, OutstandingDetailsActivity.class);
//                outstandIntent.putExtra("sales_outstand", data);
//                activity.startActivity(outstandIntent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return todayOutletsDatum.size();
//    }
//
////    void setOnClickListener(OnClickListener onClickListener) {
////        this.onClickListener = onClickListener;
////    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//
//        CardView outstandingCardview;
//        TextView txt_name, txt_avai_no, contact_name, contact_number,bal_amnt;
//
//        MyViewHolder(View itemView) {
//            super(itemView);
//            outstandingCardview = itemView.findViewById(R.id.outstanding_cardview);
//            txt_name = itemView.findViewById(R.id.txt_name);
//            txt_avai_no = itemView.findViewById(R.id.txt_avai_no);
//            contact_name = itemView.findViewById(R.id.contact_name);
//            contact_number = itemView.findViewById(R.id.mob_no);
//            bal_amnt = itemView.findViewById(R.id.bal_amnt);
//        }
//    }
//}

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.outstanding_adapter, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == todayOutletsDatum.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return todayOutletsDatum == null ? 0 : todayOutletsDatum.size();
    }

    public void addItems(List<AssignOutletsDatum> postItems) {
        todayOutletsDatum.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        todayOutletsDatum.add(new AssignOutletsDatum());
        notifyItemInserted(todayOutletsDatum.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = todayOutletsDatum.size() - 1;
        AssignOutletsDatum item = getItem(position);
        if (item != null) {
            todayOutletsDatum.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        todayOutletsDatum.clear();
        notifyDataSetChanged();
    }

    AssignOutletsDatum getItem(int position) {
        return todayOutletsDatum.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_avai_no)
        TextView txt_avai_no;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.mob_no)
        TextView contact_number;
        @BindView(R.id.bal_amnt)
        TextView bal_amnt;
        @BindView(R.id.outstanding_cardview)
        CardView cardview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            AssignOutletsDatum item = todayOutletsDatum.get(position);

            cardview.setTag(item);

        if(item.getAvailableLimit()!=null && !TextUtils.isEmpty(item.getAvailableLimit())){
            txt_avai_no.setText("₹ " + item.getAvailableLimit());
        }else{
            txt_avai_no.setText("₹ 0" );
        }
        txt_name.setText(item.getCompanyName());
        address.setText(item.getAddress());
        contact_number.setText(item.getMobile());
        bal_amnt.setText("₹ 0");

            cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outstandIntent = new Intent(activity, OutstandingDetailsActivity.class);
                outstandIntent.putExtra("sales_outstand", item);
                activity.startActivity(outstandIntent);
            }
        });
        }
    }

    public static class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
        }
    }
}