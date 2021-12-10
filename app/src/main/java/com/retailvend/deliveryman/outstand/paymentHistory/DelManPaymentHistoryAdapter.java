package com.retailvend.deliveryman.outstand.paymentHistory;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        @BindView(R.id.credit_by)
        TextView credit_by;
        @BindView(R.id.debit_to)
        TextView debit_to;
        @BindView(R.id.amount_main)
        TextView amount_main;
        @BindView(R.id.amount_debit)
        TextView amount_debit;
        @BindView(R.id.date_payment)
        TextView date_payment;
        @BindView(R.id.date_pay)
        TextView date_pay;
        @BindView(R.id.payment_cardview_dm)
        CardView cardview;
        @BindView(R.id.payment_cardview_debit)
        CardView payment_cardview_debit;
        @BindView(R.id.credit_arrow)
        ImageView credit_arrow;
        @BindView(R.id.debit_arrow)
        ImageView debit_arrow;
        @BindView(R.id.payment_details)
        TextView payment_details;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            PaymentHistoryDatum item = paymentHistorydata.get(position);

            if(item.getPaymentType().equals("Credit")){
                cardview.setVisibility(View.VISIBLE);
                payment_cardview_debit.setVisibility(View.GONE);
                credit_by.setText("Received From "+item.getDistributorName());
                amount_main.setText("₹ "+item.getAmount().toString());
                date_payment.setText(item.getDate());
            }else if(item.getPaymentType().equals("Debit")) {
                cardview.setVisibility(View.GONE);
                payment_cardview_debit.setVisibility(View.VISIBLE);
                debit_to.setText("Payment To "+item.getDistributorName());
                amount_debit.setText("₹ "+item.getAmount().toString());
                date_pay.setText(item.getDate());
            }

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
