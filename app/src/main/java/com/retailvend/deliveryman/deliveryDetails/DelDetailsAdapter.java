package com.retailvend.deliveryman.deliveryDetails;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.delManModels.delCollection.delManDeliDetails.DelManDelDetailsDatum;
import com.retailvend.utills.BaseViewHolder;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DelDetailsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<DelManDelDetailsDatum> orderListData;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public DelDetailsAdapter(Activity context, List<DelManDelDetailsDatum> itemsModelsl) {
        this.orderListData = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_del_details_list, parent, false));
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
            return position == orderListData.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return orderListData == null ? 0 : orderListData.size();
    }

    public void addItems(List<DelManDelDetailsDatum> postItems) {
        orderListData.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        orderListData.add(new DelManDelDetailsDatum());
        notifyItemInserted(orderListData.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = orderListData.size() - 1;
        DelManDelDetailsDatum item = getItem(position);
        if (item != null) {
            orderListData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        orderListData.clear();
        notifyDataSetChanged();
    }

    DelManDelDetailsDatum getItem(int position) {
        return orderListData.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.store_name)
        TextView store_name;
        @BindView(R.id.invoice_no)
        TextView invoice_no;
        @BindView(R.id.dueDays)
        TextView dueDays;
        @BindView(R.id.discount)
        TextView discount;
        @BindView(R.id.del_details_cardview)
        CardView cardview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            DelManDelDetailsDatum item = orderListData.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

//            order_status.setText(item.getOrderStatus());
            invoice_no.setText(item.getInvoiceNo());
            dueDays.setText(item.getDueDays());
            store_name.setText(item.getStoreName());

//            cardview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent salesDetailsIntent = new Intent(activity, SalesDetailsActivity.class);
//                    salesDetailsIntent.putExtra("random_value",item.getRandomValue());
//                    activity.startActivity(salesDetailsIntent);
//                }
//            });
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