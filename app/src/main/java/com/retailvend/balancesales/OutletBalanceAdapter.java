package com.retailvend.balancesales;

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
import com.retailvend.createOutlet.salesManCollection.SalesManCollectionActivity;
import com.retailvend.model.outletBalanceModule.OutletBalanceDatum;
import com.retailvend.utills.BaseViewHolder;
import com.retailvend.utills.SessionManagerSP;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OutletBalanceAdapter extends RecyclerView.Adapter<BaseViewHolder>  {

    private List<OutletBalanceDatum> outBalData;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;
    SessionManagerSP sessionManagerSP;

    public OutletBalanceAdapter(Activity context, List<OutletBalanceDatum> itemsModelsl) {
        this.outBalData = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                sessionManagerSP = new SessionManagerSP(activity);
                return new OutletBalanceAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.outlet_bal_adapter, parent, false));
            case VIEW_TYPE_LOADING:
                return new OutletBalanceAdapter.ProgressHolder(
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
            return position == outBalData.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return outBalData == null ? 0 : outBalData.size();
    }

    public void addItems(List<OutletBalanceDatum> postItems) {
        outBalData.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        outBalData.add(new OutletBalanceDatum());
        notifyItemInserted(outBalData.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = outBalData.size() - 1;
        OutletBalanceDatum item = getItem(position);
        if (item != null) {
            outBalData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        outBalData.clear();
        notifyDataSetChanged();
    }

    OutletBalanceDatum getItem(int position) {
        return outBalData.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_comp_name)
        TextView txt_comp_name;
        @BindView(R.id.txt_stre_name)
        TextView txt_stre_name;
        @BindView(R.id.txt_amount)
        TextView txt_amount;
        @BindView(R.id.txt_bill_no)
        TextView txt_bill_no;
        @BindView(R.id.txt_bal_amount)
        TextView txt_bal_amount;
        @BindView(R.id.out_bal_cardview)
        CardView cardview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            OutletBalanceDatum item = outBalData.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

            String compName = item.getCompanyName();
            String storeName = item.getStoreName();
            String amount = item.getAmount();
            String balAmount = item.getBalAmt();
            String billNo = item.getBillNo();
            String date = item.getDate();

            txt_comp_name.setText(compName);
            txt_stre_name.setText(storeName);
            txt_amount.setText("₹"+amount);
            txt_bill_no.setText(billNo);
            txt_bal_amount.setText("₹"+balAmount);
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent outletIntent = new Intent(activity, SalesManCollectionActivity.class);
                    outletIntent.putExtra("outlet_id", item.getOutletId());
                    activity.startActivity(outletIntent);
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

