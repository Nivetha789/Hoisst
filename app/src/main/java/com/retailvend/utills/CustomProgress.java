package com.retailvend.utills;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;


import androidx.core.content.ContextCompat;

import com.retailvend.R;

import java.util.Objects;

public class CustomProgress {

    private static Dialog dialog;

    public static void showProgress(Activity activity){
        try {
            dialog=new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity, R.color.transparent)));
            dialog.setContentView(R.layout.loader);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }catch (Exception e){
            Log.d("ProgressDialog",e.toString());
        }
    }

    public static void hideProgress(Activity activity){
        try {
            dialog.hide();
        }catch (Exception e){
            Log.d("ProgressDialog",e.toString());
        }
    }
}
