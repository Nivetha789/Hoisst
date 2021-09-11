package com.retailvend.todayoutlet;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.outlets.ProductNameResData;
import com.retailvend.model.outlets.SalesAgentData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductNameAdapter extends RecyclerView.Adapter<ProductNameAdapter.MyViewHolder> implements Filterable {

    List<ProductNameResData> productNameResData;
    List<ProductNameResData> productNameResDataFull;
    Activity activity;

    public ProductNameAdapter(Activity context, List<ProductNameResData> itemsModelsl) {
        this.productNameResData = itemsModelsl;
        this.productNameResDataFull = new ArrayList<>();
        this.activity = context;
    }

    @NonNull
    @Override
    public ProductNameAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.product_name_adapter, viewGroup, false);
        return new ProductNameAdapter.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ProductNameResData item = productNameResData.get(position);
//        holder.productHsnCode.setText("HSN : " + item.getHsnCode());
        holder.productName.setText(item.getProductName());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 ((ProductNameActivity) activity).updateProdName(item.getProductName(),item.getProductId(),item.getGstVal(),item.getHsnCode());
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productNameResData.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            productNameResDataFull.clear();
            List<ProductNameResData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(productNameResDataFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ProductNameResData item : productNameResData) {
                    if (item.getProductName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productNameResData = (ArrayList<ProductNameResData>) results.values;
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView productName;
        private final CardView cardview;
//        private final TextView productHsnCode;

        public MyViewHolder(View view) {
            super(view);

            productName = view.findViewById(R.id.product_code_val);
            cardview = view.findViewById(R.id.product_code_card);
//            productHsnCode = view.findViewById(R.id.product_hsn_code);
        }
    }
}
