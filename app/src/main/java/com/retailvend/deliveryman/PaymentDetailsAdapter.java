package com.retailvend.deliveryman;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.retailvend.R;
import com.retailvend.model.delManModels.delCollection.paymentCollection.PaymentCollectionDatum;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentDetailsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private final List<PaymentCollectionDatum> paymentDetailsDataModelList;
    Activity context;
    String assignId;

    public PaymentDetailsAdapter(Activity context, List<PaymentCollectionDatum> postItems, String assignIdd) {
        this.paymentDetailsDataModelList = postItems;
        this.context = context;
        this.assignId = assignIdd;

    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cust_pay_details, parent, false));
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
            return position == paymentDetailsDataModelList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return paymentDetailsDataModelList == null ? 0 : paymentDetailsDataModelList.size();
    }

    public void addItems(List<PaymentCollectionDatum> postItems) {
        paymentDetailsDataModelList.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        paymentDetailsDataModelList.add(new PaymentCollectionDatum());
        notifyItemInserted(paymentDetailsDataModelList.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = paymentDetailsDataModelList.size() - 1;
        PaymentCollectionDatum item = getItem(position);
        if (item != null) {
            paymentDetailsDataModelList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        paymentDetailsDataModelList.clear();
        notifyDataSetChanged();
    }

    PaymentCollectionDatum getItem(int position) {
        return paymentDetailsDataModelList.get(position);
    }


    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_pay_type)
        TextView txt_pay_type;
        @BindView(R.id.txt_pay_date)
        TextView txt_pay_date;
        @BindView(R.id.txt_pay_amt)
        TextView txt_pay_amt;
        @BindView(R.id.txt_pay_describ)
        TextView txt_pay_describ;
        @BindView(R.id.img_delete)
        ImageView img_delete;
        @BindView(R.id.card)
        CardView card;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            PaymentCollectionDatum item = paymentDetailsDataModelList.get(position);
            txt_pay_type.setText(item.getAmountType());
            txt_pay_date.setText(item.getDate());
            if (item.getAmount().length() > 0) {
                txt_pay_amt.setText("Rs." + item.getAmount());
            } else {
                txt_pay_amt.setText("Rs.0");
            }
            txt_pay_describ.setText("Discount : "+item.getDiscount());

//            card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                        ((PaymentDetailsActivity) context).sendOutletId(item.getOutletId());
//                }
//            });

            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        ((PaymentDetailsActivity) context).deletePaymentList("5",item.getPaymentId(),assignId);
                }
            });
        }
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
        }
    }
}