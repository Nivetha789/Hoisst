package com.retailvend.createOutlet.salesManCollection;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.collection.CollectionDetailsActivity;
import com.retailvend.deliveryman.collection.PaymentDetailsActivity;
import com.retailvend.model.createOutSales.collectionModel.CollectionResDatum;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesCollectionAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<CollectionResDatum> todayOutletsDatum;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public SalesCollectionAdapter(Activity context, List<CollectionResDatum> itemsModelsl) {
        this.todayOutletsDatum = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_collection_adapter, parent, false));
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

    public void addItems(List<CollectionResDatum> postItems) {
        todayOutletsDatum.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        todayOutletsDatum.add(new CollectionResDatum());
        notifyItemInserted(todayOutletsDatum.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = todayOutletsDatum.size() - 1;
        CollectionResDatum item = getItem(position);
        if (item != null) {
            todayOutletsDatum.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        todayOutletsDatum.clear();
        notifyDataSetChanged();
    }

    CollectionResDatum getItem(int position) {
        return todayOutletsDatum.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_name)
        TextView storeName;
        @BindView(R.id.txt_invoice)
        TextView txt_invoice;
        @BindView(R.id.txt_bal_amt)
        TextView txt_bal_amt;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.arrow)
        ImageView arrow;
        @BindView(R.id.collection_cardview)
        CardView cardview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            CollectionResDatum item = todayOutletsDatum.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

            String storeName1 = item.getCompanyName();
            String invoice = item.getBillNo();
            String bal_amt = item.getBalAmt();
            String date = item.getDate();

            storeName.setText(storeName1);
            txt_invoice.setText(invoice);
            txt_bal_amt.setText(bal_amt);
            txt_date.setText(date);

            if(bal_amt.equals("0")){
                arrow.setVisibility(View.GONE);
            }else{
                arrow.setVisibility(View.VISIBLE);
            }

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(bal_amt.equals("0")){

                    }else {
                        Intent paymentIntent = new Intent(activity, PaymentDetailsActivity.class);
                        paymentIntent.putExtra("assign_id", item.getAssignId());
                        paymentIntent.putExtra("name", item.getCompanyName());
                        paymentIntent.putExtra("outletId", item.getOutletId());
                        paymentIntent.putExtra("balamnt", item.getBalAmt());
                        paymentIntent.putExtra("paymentId", item.getPaymentId());
                        activity.startActivity(paymentIntent);
                    }
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
