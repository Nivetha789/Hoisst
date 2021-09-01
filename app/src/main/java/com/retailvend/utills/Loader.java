package com.retailvend.utills;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;

import com.retailvend.R;

import java.util.Objects;

public class Loader {


    public static void showLoad(CustomProgress customProgress,Activity activity,boolean load){
        try {
            if(!customProgress.isShowing()) {
                customProgress.setCanceledOnTouchOutside(false);
                customProgress.setCancelable(false);
                Objects.requireNonNull(customProgress.getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                Objects.requireNonNull(customProgress.getWindow()).setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity, R.color.transparent)));
            }
            if(load){
                customProgress.show();
            }else {
                customProgress.dismiss();
            }
        }catch (Exception e){
            Log.d("LoaderError",e.toString());
        }
    }

}