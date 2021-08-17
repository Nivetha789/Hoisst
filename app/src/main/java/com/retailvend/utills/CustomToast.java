package com.retailvend.utills;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.retailvend.R;


public class CustomToast {


    private Context context;
    private static CustomToast instance;

    /**
     * @param context
     */
    private CustomToast(Context context) {
        this.context = context;
    }

    /**
     * @param context
     * @return
     */
    public synchronized static CustomToast getInstance(Context context) {
        if (instance == null) {
            instance = new CustomToast(context);
        }
        return instance;
    }

    /**
     * @param message
     */
    public void showLongMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param message
     */
    public void showSmallMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * The Toast displayed via this method will display it for short period of time
     *
     * @param message
     */
    public void showLongCustomToast(String message) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.ll_toast));
        TextView msgTv = (TextView) layout.findViewById(R.id.tv_msg);
        msgTv.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


    }

    /**
     * The toast displayed by this class will display it for long period of time
     *
     * @param message
     */
    public void showSmallCustomToast(String message) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.ll_toast));
        TextView msgTv = (TextView) layout.findViewById(R.id.tv_msg);
        msgTv.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}


