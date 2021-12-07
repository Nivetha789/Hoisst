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
        @BindView(R.id.employee_name)
        TextView name;
        @BindView(R.id.cur_bal)
        TextView cur_bal;
        @BindView(R.id.outlet_name)
        TextView outlet_name;
        @BindView(R.id.payment_cardview_dm)
        CardView cardview;
        @BindView(R.id.pay_type)
        TextView pay_type;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.dist_name)
        TextView dist_name;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            PaymentHistoryDatum item = paymentHistorydata.get(position);

            name.setText(item.getEmployeeName());
            cur_bal.setText("₹ "+item.getCurBal().toString());
            outlet_name.setText(item.getOutletName());
            pay_type.setText(item.getAmountType());
            date.setText(item.getDate());
            dist_name.setText("Distributor: "+item.getDistributorName());

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
