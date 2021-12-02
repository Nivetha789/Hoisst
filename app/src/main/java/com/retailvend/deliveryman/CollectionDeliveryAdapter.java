package com.retailvend.deliveryman;

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
import com.retailvend.model.delManModels.delCollection.DeliveryCollectionListData;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionDeliveryAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<DeliveryCollectionListData> deliveryCollectionListData;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public CollectionDeliveryAdapter(Activity context, List<DeliveryCollectionListData> itemsModelsl) {
        this.deliveryCollectionListData = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new CollectionDeliveryAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_delivery_adapter, parent, false));
            case VIEW_TYPE_LOADING:
                return new CollectionDeliveryAdapter.ProgressHolder(
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
            return position == deliveryCollectionListData.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return deliveryCollectionListData == null ? 0 : deliveryCollectionListData.size();
    }

    public void addItems(List<DeliveryCollectionListData> postItems) {
        deliveryCollectionListData.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        deliveryCollectionListData.add(new DeliveryCollectionListData());
        notifyItemInserted(deliveryCollectionListData.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = deliveryCollectionListData.size() - 1;
        DeliveryCollectionListData item = getItem(position);
        if (item != null) {
            deliveryCollectionListData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        deliveryCollectionListData.clear();
        notifyDataSetChanged();
    }

    DeliveryCollectionListData getItem(int position) {
        return deliveryCollectionListData.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_preBal_no)
        TextView txt_preBal_no;
        @BindView(R.id.txt_cur_amt)
        TextView txt_cur_amt;
        @BindView(R.id.sales_cardview)
        CardView cardview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            DeliveryCollectionListData item = deliveryCollectionListData.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

            txt_name.setText(item.getOutletName());
            txt_preBal_no.setText("₹ " + item.getPreBal());
            txt_cur_amt.setText("₹ " + item.getCurBal());

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent paymentIntent = new Intent(activity, PaymentDetailsActivity.class);
                    paymentIntent.putExtra("assign_id", item.getAssignId());
                    paymentIntent.putExtra("name", item.getOutletName());
                    paymentIntent.putExtra("balamnt", item.getCurBal()+item.getPreBal());
                    paymentIntent.putExtra("outletId", item.getOutletId());
                    activity.startActivity(paymentIntent);
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

