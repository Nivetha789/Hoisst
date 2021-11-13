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
import com.retailvend.model.outlets.ProductNameResData;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductNameAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<ProductNameResData> productNameResData;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;

    public ProductNameAdapter(Activity context, List<ProductNameResData> itemsModelsl) {
        this.productNameResData = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ProductNameAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.product_name_adapter, parent, false));
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
            return position == productNameResData.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return productNameResData == null ? 0 : productNameResData.size();
    }

    public void addItems(List<ProductNameResData> postItems) {
        productNameResData.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        productNameResData.add(new ProductNameResData());
        notifyItemInserted(productNameResData.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = productNameResData.size() - 1;
        ProductNameResData item = getItem(position);
        if (item != null) {
            productNameResData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        productNameResData.clear();
        notifyDataSetChanged();
    }

    ProductNameResData getItem(int position) {
        return productNameResData.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.product_code_val)
        TextView productVal;
        @BindView(R.id.product_code_card)
        CardView cardview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            ProductNameResData item = productNameResData.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

            productVal.setText(item.getProductName());

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ProductNameActivity) activity).updateProdName(item.getProductName(), item.getProductId(), item.getGstVal(), item.getHsnCode());
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

   /* @NonNull
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
}*/
