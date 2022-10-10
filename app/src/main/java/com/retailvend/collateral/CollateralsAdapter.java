package com.retailvend.collateral;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.collateralsmodule.CollateralsListDatum;
import com.retailvend.sales.SalesDetailsActivity;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollateralsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<CollateralsListDatum> orderListData;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public CollateralsAdapter(Activity context, List<CollateralsListDatum> itemsModelsl) {
        this.orderListData = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new CollateralsAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.collaterals_adapter, parent, false));
            case VIEW_TYPE_LOADING:
                return new CollateralsAdapter.ProgressHolder(
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

    public void addItems(List<CollateralsListDatum> postItems) {
        orderListData.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        orderListData.add(new CollateralsListDatum());
        notifyItemInserted(orderListData.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = orderListData.size() - 1;
        CollateralsListDatum item = getItem(position);
        if (item != null) {
            orderListData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        orderListData.clear();
        notifyDataSetChanged();
    }

    CollateralsListDatum getItem(int position) {
        return orderListData.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.collateral_name)
        TextView collateral_name;
        @BindView(R.id.cardview)
        CardView cardview;
        @BindView(R.id.date)
        TextView date;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            CollateralsListDatum item = orderListData.get(position);

            collateral_name.setText(item.getName());
            date.setText(item.getCreatedDate());

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent collateralsDetailsIntent = new Intent(activity, CollateralsDetailsActivity.class);
                    collateralsDetailsIntent.putExtra("random_value", item.getRandomVal());
                    activity.startActivity(collateralsDetailsIntent);
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
