package com.retailvend.sales;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.retailvend.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSalesOrderAdapter extends RecyclerView.Adapter<CreateSalesOrderAdapter.DataObjectHolder> {
//    private static MyClickListener myClickListener;
//    private ArrayList<CreatePurchaseModel> modelMains;
    Context context;
//    InvoiceProductAdapter invoiceProductAdapter;
//    InvoiceUnitAdapter invoiceUnitAdapter;
    String product = "";
    String unit = "";
    int spin_product_positon = 0;
    int spin_unit_pos = 0;
//    List<Productlist> productlists = new ArrayList<>();
//    List<InvoiceUnitDateModel> invoiceUnitDateModelList = new ArrayList<>();

    public CreateSalesOrderAdapter(Context context) {
        this.context = context;
//        this.modelMains = modelMains;
    }

//    public void setOnItemClickListener(MyClickListener myClickListener) {
//        CreateSalesOrderAdapter.myClickListener = myClickListener;
//    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_list, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

//        holder.listenerNos.updatePosition(position, holder);
//        holder.listenerPrice.updatePosition(position, holder);
//        holder.listenerkg.updatePosition(position, holder);
//
//        if (modelMains.get(position).getPrice() == 0) {
//            holder.edPrice.setText("");
//        } else {
//            holder.edPrice.setText(String.valueOf(modelMains.get(position).getPrice()));
//        }
//        if (modelMains.get(position).getNos() == 0) {
//            holder.edt_nos.setText("");
//        } else {
//            holder.edt_nos.setText(String.valueOf(modelMains.get(position).getNos()));
//        }
//        if (modelMains.get(position).getKg() == 0) {
//            holder.edt_kg.setText("");
//        } else {
//            holder.edt_kg.setText(String.valueOf(modelMains.get(position).getKg()));
//        }
//        if (modelMains.get(position).getAmt() == 0) {
//            holder.txt_total_amt.setText("Rs." + modelMains.get(position).getAmt());
//        } else {
//            holder.txt_total_amt.setText("Rs." + modelMains.get(position).getAmt());
//        }
//
//
//        if (modelMains.get(position).isBtnPlus()) {
//            holder.btnAdd.setText("+");
//            holder.btnAdd.setTextColor(Color.parseColor("#ffffff"));
//            holder.btnAdd.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.button_design_plus));
//        } else {
//            holder.btnAdd.setText("-");
//            holder.btnAdd.setTextColor(Color.parseColor("#ffffff"));
//            holder.btnAdd.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.button_design_delete));
//        }
//
//
////        if (modelMains.get(position).getSpin_product_posion() == spin_product_positon) {
////
//        spin_product_positon = modelMains.get(position).getSpin_product_posion();
//        spin_unit_pos = modelMains.get(position).getSpin_unit_pos();
////        } else {
////            spin_product_positon = 0;
////        }
//
////        getInvoiceProduct(holder.spin_invoice_product, spin_product_positon);
////        getInvoiceUnit(holder.spin_unit, spin_unit_pos);
//
//
//        holder.spin_invoice_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                spin_product_positon = holder.spin_invoice_product.getSelectedItemPosition();
//
//                Productlist productlist = productlists.get(i);
//                product = productlist.getId();
//
//                if (modelMains.get(position).getProduct_name().equals(product)) {
//                    return;
//                } else {
//                    modelMains.get(position).setProduct_name(product);
//                }
//                if (String.valueOf(modelMains.get(position).getSpin_product_posion()).equals(String.valueOf(spin_product_positon))) {
//                    return;
//                } else {
//                    modelMains.get(position).setSpin_product_posion(spin_product_positon);
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//        holder.spin_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                spin_unit_pos = holder.spin_unit.getSelectedItemPosition();
//
//                InvoiceUnitDateModel invoiceUnitDateModel = invoiceUnitDateModelList.get(i);
//                unit = invoiceUnitDateModel.getId();
//                System.out.println("unitunitunitunitunitunitunitunit " + unit);
//
//                if (modelMains.get(position).getUnit().equals(unit)) {
//                    return;
//                } else {
//                    modelMains.get(position).setUnit(unit);
//                }
//                if (String.valueOf(modelMains.get(position).getSpin_unit_pos()).equals(String.valueOf(spin_unit_pos))) {
//                    return;
//                } else {
//                    modelMains.get(position).setSpin_unit_pos(spin_unit_pos);
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


    }


//    public void addItem(CreatePurchaseModel modelMain, int index) {
//        modelMains.add(modelMain);
//        if (index != 0) {
//            modelMains.get(index - 1).setBtnPlus(false);
//        }
//        notifyDataSetChanged();
//    }
//
//    public void deleteItem(int index) {
//        if (modelMains.size() > index) {
//            modelMains.remove(index);
//            notifyDataSetChanged();
//            totalPrice();
//        }
//    }

//    public interface MyClickListener {
//        void onItemClick(int position, View v);
//    }

//    private class MyCustomEditTextListener implements TextWatcher {
//
//        private int position;
//        private int spin_pos;
//        private EditText editText;
//
//        private Spinner spinner;
//        DataObjectHolder holder;
//
//        public MyCustomEditTextListener(EditText ed) {
//            this.editText = ed;
//        }
//
//
//        public void updatePosition(int position, DataObjectHolder holder) {
//            this.position = position;
//            this.holder = holder;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            if (charSequence.length() == 0) {
//                return;
//            }
//
//            if (editText.getId() == R.id.txtPrice) {
//                if (String.valueOf(modelMains.get(position).getPrice()).equals(charSequence.toString())) {
//                    return;
//                } else {
//                    modelMains.get(position).setPrice(Integer.parseInt(charSequence.toString()));
//
//                }
//            } else if (editText.getId() == R.id.edt_nos) {
//                if (String.valueOf(modelMains.get(position).getNos()).equals(charSequence.toString())) {
//                    return;
//                } else {
//                    modelMains.get(position).setNos(Integer.parseInt(charSequence.toString()));
//                }
//            } else if (editText.getId() == R.id.edt_kg) {
//                if (String.valueOf(modelMains.get(position).getKg()).equals(charSequence.toString())) {
//                    return;
//                } else {
//                    modelMains.get(position).setKg(Integer.parseInt(charSequence.toString()));
//                }
//
//            }
//
//            int amt = 0;
//
//            if (unit.equals("1")) {
//                //Kg
//                amt = modelMains.get(position).getPrice() * modelMains.get(position).getKg();
//                modelMains.get(position).setAmt(amt);
//            } else if (unit.equals("2")) {
//                //Nos
//                amt = modelMains.get(position).getPrice() * modelMains.get(position).getNos();
//                modelMains.get(position).setAmt(amt);
//            }
//
//            holder.txt_total_amt.setText("Rs." + amt);
//            totalPrice();
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    }
//
//    private void totalPrice() {
//        int price = 0;
//        int nos = 0;
//        int kgs = 0;
//
//        for (int j = 0; j < modelMains.size(); j++) {
//            price += modelMains.get(j).getAmt();
//        }
//
//        for (int i = 0; i < modelMains.size(); i++) {
//            nos += modelMains.get(i).getNos();
//
//        }
//
//
//        for (int k = 0; k < modelMains.size(); k++) {
//            kgs += modelMains.get(k).getKg();
//        }
//
//
//        CreatePurchaseInvoiceActivity.txtTotalNos.setText(String.valueOf(nos));
//        CreatePurchaseInvoiceActivity.txtTotalkg.setText(String.valueOf(kgs));
//        CreatePurchaseInvoiceActivity.txtTotalPrice.setText("\u20B9" + price);
//
//
//    }


    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        EditText edt_nos, edPrice, edt_kg;
        TextView txt_total_amt, txt_dis_product;
        TextView btnAdd;
        LinearLayout linBtn;
        Spinner spin_invoice_product, spin_unit;
//        MyCustomEditTextListener listenerPrice, listenerNos, listenerkg;

        public DataObjectHolder(View itemView) {
            super(itemView);
            edt_nos =  itemView.findViewById(R.id.edt_nos);
            edPrice =  itemView.findViewById(R.id.txtPrice);
            edt_kg =  itemView.findViewById(R.id.edt_kg);
            txt_total_amt = itemView.findViewById(R.id.txt_total_amt);
            btnAdd =  itemView.findViewById(R.id.btnAdd);
            spin_invoice_product = itemView.findViewById(R.id.spin_invoice_product);
            spin_unit = itemView.findViewById(R.id.spin_unit);
            linBtn =  itemView.findViewById(R.id.linBtn);
            txt_dis_product = itemView.findViewById(R.id.txt_dis_product);
            linBtn.setOnClickListener(this);
            btnAdd.setOnClickListener(this);
//            this.listenerPrice = new MyCustomEditTextListener(this.edPrice);
//            this.listenerNos = new MyCustomEditTextListener(this.edt_nos);
//            this.listenerkg = new MyCustomEditTextListener(this.edt_kg);
//            this.edPrice.addTextChangedListener(listenerPrice);
//            this.edt_nos.addTextChangedListener(listenerNos);
//            this.edt_kg.addTextChangedListener(listenerkg);


        }

        @Override
        public void onClick(View v) {
//            myClickListener.onItemClick(getLayoutPosition(), v);

        }
    }

//    public void getInvoiceProduct(Spinner spinner, int spin_product_positon) {
//
//        Call<InvoiceProductModel> call = RetrofitClient
//                .getInstance().getApi().getInvoiceProduct("4");
//
//
//        call.enqueue(new Callback<InvoiceProductModel>() {
//            @Override
//            public void onResponse(Call<InvoiceProductModel> call, Response<InvoiceProductModel> response) {
//
//                try {
//
//                    Gson gson = new Gson();
//                    String json = gson.toJson(response.body());
//                    InvoiceProductModel invoiceProductModel = gson.fromJson(json, InvoiceProductModel.class);
////                LoginModule loginModule = response.body();
//
//
//                    if (invoiceProductModel.getCode() == 1) {
//
//                        productlists = invoiceProductModel.getProductlist();
//
//                        invoiceProductAdapter = new InvoiceProductAdapter(context, productlists);
//                        spinner.setAdapter(invoiceProductAdapter);
//                        spinner.setSelection(spin_product_positon);
//                        invoiceProductAdapter.notifyDataSetChanged();
//
//                    } else {
//
////                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                        CustomToast.getInstance(context).showSmallCustomToast(invoiceProductModel.getMessage());
////                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//                    Log.d("Exception", e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<InvoiceProductModel> call, Throwable t) {
//                Log.d("Failure ", t.getMessage());
//
////                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                CustomToast.getInstance(context).showSmallCustomToast("Something went wrong try again..");
//
//            }
//        });
//
//    }
//
//    public void getInvoiceUnit(Spinner spinner, int unit_pos) {
//
//        Call<InvoiceUnitModel> call = RetrofitClient
//                .getInstance().getApi().getInvoiceUnit("1");
//
//
//        call.enqueue(new Callback<InvoiceUnitModel>() {
//            @Override
//            public void onResponse(Call<InvoiceUnitModel> call, Response<InvoiceUnitModel> response) {
//
//                try {
//
//                    Gson gson = new Gson();
//                    String json = gson.toJson(response.body());
//                    InvoiceUnitModel invoiceUnitModel = gson.fromJson(json, InvoiceUnitModel.class);
////                LoginModule loginModule = response.body();
//
//
//                    if (invoiceUnitModel.getCode() == 1) {
//
//                        invoiceUnitDateModelList = invoiceUnitModel.getData();
//
//                        invoiceUnitAdapter = new InvoiceUnitAdapter(context, invoiceUnitDateModelList);
//                        spinner.setAdapter(invoiceUnitAdapter);
//                        spinner.setSelection(unit_pos);
//                        invoiceUnitAdapter.notifyDataSetChanged();
//
//                    } else {
//
////                        Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                        CustomToast.getInstance(context).showSmallCustomToast(invoiceUnitModel.getMessage());
////                    Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//                    Log.d("Exception", e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<InvoiceUnitModel> call, Throwable t) {
//                Log.d("Failure ", t.getMessage());
//
////                Toast.makeText(LoginActivity.this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
//                CustomToast.getInstance(context).showSmallCustomToast("Something went wrong try again..");
//
//            }
//        });
//
//    }
}
