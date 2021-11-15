package com.retailvend.sales;

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
import com.retailvend.model.manageorder.OrderListDatum;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<OrderListDatum> orderListData;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public SalesAdapter(Activity context, List<OrderListDatum> itemsModelsl) {
        this.orderListData = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new SalesAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_list, parent, false));
            case VIEW_TYPE_LOADING:
                return new SalesAdapter.ProgressHolder(
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
            return position == orderListData.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return orderListData == null ? 0 : orderListData.size();
    }

    public void addItems(List<OrderListDatum> postItems) {
        orderListData.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        orderListData.add(new OrderListDatum());
        notifyItemInserted(orderListData.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = orderListData.size() - 1;
        OrderListDatum item = getItem(position);
        if (item != null) {
            orderListData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        orderListData.clear();
        notifyDataSetChanged();
    }

    OrderListDatum getItem(int position) {
        return orderListData.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.order_no)
        TextView order_no;
        @BindView(R.id.emp_name)
        TextView emp_name;
        @BindView(R.id.store_name)
        TextView store_name;
        @BindView(R.id.contact_name)
        TextView contact_name;
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
            OrderListDatum item = orderListData.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

//            order_status.setText(item.getOrderStatus());
            order_no.setText(item.getOrderNo());
            emp_name.setText(item.getEmpName());
            store_name.setText(item.getStoreName());
            contact_name.setText(item.getContactName());

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent salesDetailsIntent = new Intent(activity, SalesDetailsActivity.class);
                    salesDetailsIntent.putExtra("random_value",item.getRandomValue());
                    activity.startActivity(salesDetailsIntent);
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