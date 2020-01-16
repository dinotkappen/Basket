package com.shopping.basket.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopping.basket.R;

public class TermsCondictionActivity extends AppCompatActivity {

    TextView txtTool;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.terms_conditions);

        txtTool = findViewById(R.id.txt_head);
        imgBack = findViewById(R.id.img_back_right);

        txtTool.setText(R.string.menu_terms);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
