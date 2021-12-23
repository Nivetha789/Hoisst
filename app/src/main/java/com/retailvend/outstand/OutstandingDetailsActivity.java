package com.retailvend.outstand;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.retailvend.R;
import com.retailvend.model.outlets.AssignOutletsDatum;

public class OutstandingDetailsActivity extends AppCompatActivity {

    TextView shop_name,contact_name,address,mail,shop_number,avai_amount,bal_amount;
    AssignOutletsDatum assignOutletsDatum;
    String shopName,contactName,addressTxt,mailTxt,shopNum,avaiAmount,balanceAmount="";
    ImageView left_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding_details);
        shop_name=findViewById(R.id.shop_name);
        contact_name=findViewById(R.id.contact_name);
        address=findViewById(R.id.address);
        mail=findViewById(R.id.mail);
        shop_number=findViewById(R.id.shop_number);
        avai_amount=findViewById(R.id.avai_amount);
        bal_amount=findViewById(R.id.bal_amount);
        left_arrow=findViewById(R.id.left_arrow);

        if (Build.VERSION.SDK_INT >= 19) {

            Window window = getWindow();

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.setStatusBarColor(this.getResources().getColor(R.color.white));
//            window.setNavigationBarColor(this.getResources().getColor(R.color.black_overlay));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        assignOutletsDatum = (AssignOutletsDatum) getIntent().getSerializableExtra("sales_outstand");
        if(assignOutletsDatum!=null){
            shopName=assignOutletsDatum.getCompanyName();
            contactName=assignOutletsDatum.getContactName();
            addressTxt=assignOutletsDatum.getAddress();
            mailTxt=assignOutletsDatum.getEmail();
            shopNum=assignOutletsDatum.getMobile();
            avaiAmount=assignOutletsDatum.getAvailableLimit();
            balanceAmount=assignOutletsDatum.getCreditLimit();
            shop_name.setText(shopName);
            contact_name.setText(contactName);
            address.setText(addressTxt);
            mail.setText(mailTxt);
            shop_number.setText(shopNum);
            avai_amount.setText(avaiAmount);
            bal_amount.setText(balanceAmount);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}