package com.retailvend.deliveryman.outstand.paymentHistory;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.deliveryman.outstand.DelManOutstandAdapter;
import com.retailvend.model.delManModels.delCollection.outstand.OutstandDatum;
import com.retailvend.model.delManModels.delCollection.paymentHistory.PaymentHistoryDatum;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DelManPaymentHistoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<PaymentHistoryDatum> paymentHistorydata;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public DelManPaymentHistoryAdapter(Activity context, List<PaymentHistoryDatum> itemsModelsl) {
        this.paymentHistorydata = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_adapter, parent, false));
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
            return position == paymentHistorydata.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return paymentHistorydata == null ? 0 : paymentHistorydata.size();
    }

    public void addItems(List<PaymentHistoryDatum> postItems) {
        paymentHistorydata.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        paymentHistorydata.add(new PaymentHistoryDatum());
        notifyItemInserted(paymentHistorydata.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = paymentHistorydata.size() - 1;
        PaymentHistoryDatum item = getItem(position);
        if (item != null) {
            paymentHistorydata.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        paymentHistorydata.clear();
        notifyDataSetChanged();
    }

    PaymentHistoryDatum getItem(int position) {
        return paymentHistorydata.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.invoice_date)
        TextView invoice_date;
        @BindView(R.id.invoice_amount)
        TextView invoice_amount;
        @BindView(R.id.outstand_cardview_dm)
        ConstraintLayout cardview;
        @BindView(R.id.name_frst_ltr)
        TextView name_txt;
        @BindView(R.id.history)
        ImageView history;
        @BindView(R.id.payment_history)
        TextView payment_history;
        @BindView(R.id.collection)
        ImageView collection;
        @BindView(R.id.payment_collection)
        TextView payment_collection;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            PaymentHistoryDatum item = paymentHistorydata.get(position);

            name.setText(item.getOutletName());
//            invoice_date.setText(item.getUpdateDate().toString());
            invoice_amount.setText("₹ "+item.getCurBal().toString());
            String nameSplit =item.getOutletName();
            name.setText(nameSplit);
            char firstLetter = nameSplit.charAt(0);
            System.out.println("firsrdsrs "+firstLetter);
            name_txt.setText(String.valueOf(firstLetter));

            payment_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            payment_collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

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
