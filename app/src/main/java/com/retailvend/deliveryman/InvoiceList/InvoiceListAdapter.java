package com.retailvend.deliveryman.InvoiceList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.invoiceListModel.InvoiceListDatum;
import com.retailvend.model.manageorder.OrderListDatum;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<InvoiceListDatum> invoiceListDatum;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public InvoiceListAdapter(Activity context, List<InvoiceListDatum> itemsModelsl) {
        this.invoiceListDatum = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_list_adapter, parent, false));
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
            return position == invoiceListDatum.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return invoiceListDatum == null ? 0 : invoiceListDatum.size();
    }

    public void addItems(List<InvoiceListDatum> postItems) {
        invoiceListDatum.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        invoiceListDatum.add(new InvoiceListDatum());
        notifyItemInserted(invoiceListDatum.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = invoiceListDatum.size() - 1;
        InvoiceListDatum item = getItem(position);
        if (item != null) {
            invoiceListDatum.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        invoiceListDatum.clear();
        notifyDataSetChanged();
    }

    InvoiceListDatum getItem(int position) {
        return invoiceListDatum.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.com_name)
        TextView com_name;
        @BindView(R.id.invoice_num)
        TextView invoice_num;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.address)
        TextView address;
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
            InvoiceListDatum item = invoiceListDatum.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

//            order_status.setText(item.getOrderStatus());
            com_name.setText(item.getCompanyName());
            invoice_num.setText(item.getInvoiceNo());
            date.setText(item.getCreatedate());
            address.setText(item.getAddress());

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
