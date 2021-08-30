package com.retailvend.utills;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.retailvend.R;

import butterknife.ButterKnife;

public class CustomProgress extends Dialog{

    public CustomProgress(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loader);
        ButterKnife.bind(this);
    }
}