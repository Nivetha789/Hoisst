package com.retailvend.loyalty;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.loyalty.LoyaltyDatum;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.todayoutlet.TodayOutletAdapter;
import com.retailvend.todayoutlet.TodayOutletDetailsActivity;
import com.retailvend.utills.BaseViewHolder;
import com.retailvend.utills.SessionManagerSP;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoyaltyAdapter extends RecyclerView.Adapter<BaseViewHolder>  {

    private List<LoyaltyDatum> loyaltyData;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;
    SessionManagerSP sessionManagerSP;

    public LoyaltyAdapter(Activity context, List<LoyaltyDatum> itemsModelsl) {
        this.loyaltyData = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                sessionManagerSP = new SessionManagerSP(activity);
                return new LoyaltyAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.loyalty_adapter, parent, false));
            case VIEW_TYPE_LOADING:
                return new LoyaltyAdapter.ProgressHolder(
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
            return position == loyaltyData.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return loyaltyData == null ? 0 : loyaltyData.size();
    }

    public void addItems(List<LoyaltyDatum> postItems) {
        loyaltyData.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        loyaltyData.add(new LoyaltyDatum());
        notifyItemInserted(loyaltyData.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = loyaltyData.size() - 1;
        LoyaltyDatum item = getItem(position);
        if (item != null) {
            loyaltyData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        loyaltyData.clear();
        notifyDataSetChanged();
    }

    LoyaltyDatum getItem(int position) {
        return loyaltyData.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.inv_count)
        TextView inv_count;
        @BindView(R.id.dis_value)
        TextView dis_value;
        @BindView(R.id.date_loyalty)
        TextView date_loyalty;
        @BindView(R.id.today_outlet)
        ConstraintLayout cardview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            LoyaltyDatum item = loyaltyData.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

            String invCount = item.getInvCount();
            String disValue = item.getDisValue();
            String date = item.getDate();

            inv_count.setText(" "+invCount);
            dis_value.setText(" "+disValue);
            date_loyalty.setText(date);

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, TodayOutletDetailsActivity.class);
                    i.putExtra("todayOutlet", item);
                    activity.startActivity(i);
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
