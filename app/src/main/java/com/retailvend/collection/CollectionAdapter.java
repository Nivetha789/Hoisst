package com.retailvend.collection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.retailvend.R;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static boolean isLoaderVisible = false;
    //    private List<Saleslist> saleslists;
    private Activity activity;


//    public SalesAdapter(Context context, List<Saleslist> postItems) {
//        this.saleslists = postItems;
//        this.context = context;
//    }

    public CollectionAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.collection_adapter, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        BuyEShareListData data = datas.get(position);
//        holder.cardView.setTag(data);
//        String value = "$ " + data.getEsPrice();
//        holder.cost.setText(value);
//        if (data.getEsName().equalsIgnoreCase("Gold")) {
//            holder.backgroundImg.setBackground(ContextCompat.getDrawable(activity, R.drawable.gold));
//        } else if (data.getEsName().equalsIgnoreCase("Silver")) {
//            holder.backgroundImg.setBackground(ContextCompat.getDrawable(activity, R.drawable.silver));
//        } else if (data.getEsName().equalsIgnoreCase("diamond")) {
//            holder.backgroundImg.setBackground(ContextCompat.getDrawable(activity, R.drawable.diamond));
//        } else if (data.getEsName().equalsIgnoreCase("Platinum")) {
//            holder.backgroundImg.setBackground(ContextCompat.getDrawable(activity, R.drawable.platinum));
//        } else if (data.getEsName().equalsIgnoreCase("Bronze")) {
//            holder.backgroundImg.setBackground(ContextCompat.getDrawable(activity, R.drawable.bronze));
//        }
//        if((datas.size()-1)==position){
//            holder.view.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

//    void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MyViewHolder(View itemView) {
            super(itemView);
        }

    }
}
