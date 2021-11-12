//package com.retailvend.payment;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.retailvend.R;
//
//import java.util.List;
//
//public class AddPaymentTypeAdapter extends BaseAdapter {
//    Context context;
//
//    List<AddPaymentType> addPaymentTypeList;
//
//    public AddPaymentTypeAdapter(Context context, List<AddPaymentType> addPaymentTypeList) {
//        this.context = context;
//        this.addPaymentTypeList = addPaymentTypeList;
//    }
//
//    @Override
//    public int getCount() {
//        return addPaymentTypeList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return addPaymentTypeList.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (view == null) {
//            view = inflater.inflate(R.layout.layout_add_payment_type, viewGroup, false);
//        }
//
//        AddPaymentType addPaymentType = addPaymentTypeList.get(i);
//        TextView txt_type = view.findViewById(R.id.txt_type);
//
////        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/CALIBRIB.TTF");
////        txt_spin_vehicle_type.setTypeface(tf);
//
//        txt_type.setText(addPaymentType.getType());
//
//
//        return view;
//    }
//}
