package com.shopping.basket.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.R;

public class ConfirmMail extends AppCompatActivity {

    API apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    apiInterface =APIClient.getClient().create(API .class);

    requestWindowFeature(Window.FEATURE_NO_TITLE);

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
             WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.confirm_email);



}
}
