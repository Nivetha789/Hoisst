package com.retailvend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.retailvend.utills.SessionManagerSP;
import com.retailvend.utills.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity {

    ImageView left_arrow;
    ImageView logout;
    SessionManagerSP sessionManagerSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        left_arrow=findViewById(R.id.back);
        logout=findViewById(R.id.logout);

        sessionManagerSP = new SessionManagerSP(ProfileActivity.this);
        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagerSP.setPhonelogin("0");
                SharedPrefManager.getInstance(getApplicationContext()).clear();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}