package com.retailvend.collection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.collectionmodel.CollectionDetailsListDatum;
import com.retailvend.model.manageorder.OrderListDatum;
import com.retailvend.orderList.OrderListAdapter;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionDetailsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<CollectionDetailsListDatum> collectionDetailsListData;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public CollectionDetailsAdapter(Activity context, List<CollectionDetailsListDatum> itemsModelsl) {
        this.collectionDetailsListData = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_details_adapter, parent, false));
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
            return position == collectionDetailsListData.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return collectionDetailsListData == null ? 0 : collectionDetailsListData.size();
    }

    public void addItems(List<CollectionDetailsListDatum> postItems) {
        collectionDetailsListData.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        collectionDetailsListData.add(new CollectionDetailsListDatum());
        notifyItemInserted(collectionDetailsListData.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = collectionDetailsListData.size() - 1;
        CollectionDetailsListDatum item = getItem(position);
        if (item != null) {
            collectionDetailsListData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        collectionDetailsListData.clear();
        notifyDataSetChanged();
    }

    CollectionDetailsListDatum getItem(int position) {
        return collectionDetailsListData.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.dist_name)
        TextView distName;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.amount_type)
        TextView amount_type;
        @BindView(R.id.cardview)
        CardView cardview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            CollectionDetailsListDatum item = collectionDetailsListData.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

//            order_status.setText(item.getOrderStatus());
            distName.setText(item.getDistributorName());
            date.setText(item.getPaymentDate());
            amount.setText(item.getAmount());
            amount_type.setText(item.getAmountType());

//            cardview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((ProductNameActivity) activity).updateProdName(item.getProductName(), item.getProductId(), item.getGstVal(), item.getHsnCode());
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
