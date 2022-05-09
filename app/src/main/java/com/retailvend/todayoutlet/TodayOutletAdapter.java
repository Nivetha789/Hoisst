package com.retailvend.todayoutlet;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;
import com.retailvend.model.outlets.AssignOutletsDatum;
import com.retailvend.model.outlets.ProductNameResData;
import com.retailvend.model.outlets.SalesAgentData;
import com.retailvend.utills.BaseViewHolder;
import com.retailvend.utills.SessionManagerSP;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayOutletAdapter extends RecyclerView.Adapter<BaseViewHolder>  {

    private List<AssignOutletsDatum> todayOutletsDatum;
    Activity activity;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;
    SessionManagerSP sessionManagerSP;

    public TodayOutletAdapter(Activity context, List<AssignOutletsDatum> itemsModelsl) {
        this.todayOutletsDatum = itemsModelsl;
        this.activity = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                sessionManagerSP = new SessionManagerSP(activity);
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.today_outlet_adapter, parent, false));
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

    public void addItems(List<AssignOutletsDatum> postItems) {
        todayOutletsDatum.addAll(postItems);
        notifyDataSetChanged();


    }

    public void addLoading() {
        isLoaderVisible = true;
        todayOutletsDatum.add(new AssignOutletsDatum());
        notifyItemInserted(todayOutletsDatum.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = todayOutletsDatum.size() - 1;
        AssignOutletsDatum item = getItem(position);
        if (item != null) {
            todayOutletsDatum.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        todayOutletsDatum.clear();
        notifyDataSetChanged();
    }

    AssignOutletsDatum getItem(int position) {
        return todayOutletsDatum.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.shop_title)
        TextView shopName;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.contact_number)
        TextView contactNum;
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
            AssignOutletsDatum item = todayOutletsDatum.get(position);

//            System.out.println("tesgsg "+salesAgentDataList.get(position));

            String companyName = item.getCompanyName();
            String address1 = item.getAddress();
            String companyNumber = item.getMobile();

            System.out.println("tesgsg "+companyName);
            shopName.setText(companyName);
            address.setText(address1);
            contactNum.setText(companyNumber);
            sessionManagerSP.setOutletLat(item.getLatitude());
            sessionManagerSP.setOutletLong(item.getLongitude());


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