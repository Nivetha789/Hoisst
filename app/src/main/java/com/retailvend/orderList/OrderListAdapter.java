package com.retailvend.orderList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.manageorder.OrderListDatum;
import com.retailvend.todayoutlet.ProductNameActivity;
import com.retailvend.todayoutlet.ProductNameAdapter;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<OrderListDatum> orderListData;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public OrderListAdapter(Activity context, List<OrderListDatum> itemsModelsl) {
        this.orderListData = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new OrderListAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_adapter, parent, false));
            case VIEW_TYPE_LOADING:
                return new OrderListAdapter.ProgressHolder(
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
        @BindView(R.id.order_status)
        TextView order_status;
        @BindView(R.id.con_name)
        TextView con_name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.order_num)
        TextView order_num;
        @BindView(R.id.store_name)
        TextView store_name;
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
            OrderListDatum item = orderListData.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

//            order_status.setText(item.getOrderStatus());
            con_name.setText(item.getContactName());
            date.setText(item.getOrdered());
            order_num.setText(item.getOrderNo());
            store_name.setText(item.getStoreName());

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
