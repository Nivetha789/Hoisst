package com.retailvend.deliveryman.outlet;

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
import com.retailvend.model.delManModels.delCollection.todayOutletsModel.DeliveryTodayOutletsDatum;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.todayoutlet.TodayOutletAdapter;
import com.retailvend.todayoutlet.TodayOutletDetailsActivity;
import com.retailvend.utills.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayOutletDelManAdapter extends RecyclerView.Adapter<BaseViewHolder>  {

//    private Activity activity;
//    List<DeliveryTodayOutletsDatum> todayOutletsDatum;
//
//
//    TodayOutletDelManAdapter(Activity activity, List<DeliveryTodayOutletsDatum> assignOutletsDatum) {
//        this.activity = activity;
//        this.todayOutletsDatum=assignOutletsDatum;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(activity).inflate(R.layout.today_outlet_del_man_adapter, viewGroup, false);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        DeliveryTodayOutletsDatum data = todayOutletsDatum.get(position);
//        holder.todayOutletCard.setTag(data);
//        String companyName = data.getOutletName();
//        String billNo = data.getBillType();
//        String invoiceNo = data.getInvoiceNo();
//        String date = data.getOrdered();
////        String assignID = data.geta();
//        String randomVal = data.getRandomValue();
//        holder.compName.setText(companyName);
//
//        if(billNo.equals("1")){
//            holder.bill_no.setText("COD");
//        }else if(billNo.equals("2")){
//            holder.bill_no.setText("Credit");
//        }
//        holder.invoice.setText(invoiceNo);
//        holder.date.setText(date);
//
//        holder.todayOutletCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(activity, OutletInvoiceDetailsActivity.class);
////                i.putExtra("assign_id",)
//                i.putExtra("random_value", randomVal);
//                activity.startActivity(i);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return todayOutletsDatum.size();
//    }
//
////    void setOnClickListener(OnClickListener onClickListener) {
////        this.onClickListener = onClickListener;
////    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        ConstraintLayout todayOutletCard;
//        TextView compName;
//        TextView bill_no;
//        TextView invoice;
//        TextView date;
//
//        MyViewHolder(View itemView) {
//            super(itemView);
//            todayOutletCard=itemView.findViewById(R.id.today_outlet);
//            compName=itemView.findViewById(R.id.shop_title);
//            bill_no=itemView.findViewById(R.id.bill_no);
//            invoice=itemView.findViewById(R.id.invoice);
//            date=itemView.findViewById(R.id.date);
//        }
//    }
//}

        private List<DeliveryTodayOutletsDatum> todayOutletsDatum;
        Activity activity;
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        private static boolean isLoaderVisible = false;

    public TodayOutletDelManAdapter(Activity context, List<DeliveryTodayOutletsDatum> itemsModelsl) {
            this.todayOutletsDatum = itemsModelsl;
            this.activity = context;
        }

        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new ViewHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.today_outlet_del_man_adapter, parent, false));
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
                return position == todayOutletsDatum.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
            } else {
                return VIEW_TYPE_NORMAL;
            }
        }

        @Override
        public int getItemCount() {
            return todayOutletsDatum == null ? 0 : todayOutletsDatum.size();
        }

        public void addItems(List<DeliveryTodayOutletsDatum> postItems) {
            todayOutletsDatum.addAll(postItems);
            notifyDataSetChanged();


        }

        public void addLoading() {
            isLoaderVisible = true;
            todayOutletsDatum.add(new DeliveryTodayOutletsDatum());
            notifyItemInserted(todayOutletsDatum.size() - 1);
        }

        public void removeLoading() {
            isLoaderVisible = false;
            int position = todayOutletsDatum.size() - 1;
            DeliveryTodayOutletsDatum item = getItem(position);
            if (item != null) {
                todayOutletsDatum.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            todayOutletsDatum.clear();
            notifyDataSetChanged();
        }

    DeliveryTodayOutletsDatum getItem(int position) {
            return todayOutletsDatum.get(position);
        }

        public class ViewHolder extends BaseViewHolder {
            @BindView(R.id.company_name)
            TextView company_name;
            @BindView(R.id.store_name)
            TextView store_name;
            @BindView(R.id.invoice)
            TextView invoice;
            @BindView(R.id.date)
            TextView date;
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
                DeliveryTodayOutletsDatum item = todayOutletsDatum.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

                String companyName = item.getCompanyName();
                String storename = item.getStoreName();
                String invoice1 = item.getInvoiceNo();
                String createdate = item.getCreatedate();
                String randomVal = item.getRandomValue();

                company_name.setText(companyName);
                store_name.setText(storename);
                invoice.setText( invoice1);
                date.setText(createdate);

                cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(activity, OutletInvoiceDetailsActivity.class);
//                        i.putExtra("assign_id",)
                        i.putExtra("random_value", randomVal);
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