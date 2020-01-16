package com.shopping.basket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.shopping.basket.R;

public class SettingActivity extends AppCompatActivity {

    TextView txtHead;
    ImageView backBt;
    LinearLayout lenRsetPassword,lenChangeLanguage,lenInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.setting_activity);

        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();

        backBt              = findViewById(R.id.img_back_right);
        txtHead             = findViewById(R.id.txt_head);
        lenRsetPassword     = findViewById(R.id.len_password);
        lenChangeLanguage   = findViewById(R.id.len_language);
        lenInfo             = findViewById(R.id.len_info);

      txtHead.setText(R.string.menu_setting);

      backBt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
      });

      lenRsetPassword.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent restPass = new Intent(getApplication(),ResetPassword.class);
              startActivity(restPass);
          }
      });
        lenInfo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent restPass = new Intent(getApplication(),CahngeLanguage.class);
              startActivity(restPass);
          }
      });
        lenChangeLanguage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Hawk.put("page","profile");
              Intent restPass = new Intent(getApplication(),MainActivity.class);
              startActivity(restPass);
          }
      });


    }
}
