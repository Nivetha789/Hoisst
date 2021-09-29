package com.retailvend.todayoutlet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.outlets.SalesAgentData;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesAgentAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<SalesAgentData> salesAgentDataList;
    Activity activity;
    String sales_name = "";
    String sales_id = "";
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public SalesAgentAdapter(Activity context, List<SalesAgentData> itemsModel) {
        this.salesAgentDataList = itemsModel;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new SalesAgentAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_agent_adapter, parent, false));
            case VIEW_TYPE_LOADING:
                return new SalesAgentAdapter.ProgressHolder(
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
            return position == salesAgentDataList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return salesAgentDataList == null ? 0 : salesAgentDataList.size();
    }

    public void addItems(List<SalesAgentData> postItems) {
        salesAgentDataList.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        salesAgentDataList.add(new SalesAgentData());
        notifyItemInserted(salesAgentDataList.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = salesAgentDataList.size() - 1;
        SalesAgentData item = getItem(position);
        if (item != null) {
            salesAgentDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        salesAgentDataList.clear();
        notifyDataSetChanged();
    }

    SalesAgentData getItem(int position) {
        return salesAgentDataList.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.sales_agent_val)
        TextView salesAgentName;
        @BindView(R.id.sales_agent_card)
        CardView cardview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            SalesAgentData item = salesAgentDataList.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

            salesAgentName.setText(item.getCompanyName());

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SalesAgentNameActivity) activity).updateSalesNameAdapter(item.getCompanyName(), item.getAgentsId());
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

