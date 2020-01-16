package com.shopping.basket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.shopping.basket.R;


public class SplashActivity extends AppCompatActivity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 500;
    int logedIn = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.splach);

            Hawk.init(getApplicationContext())
                    .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                    .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                    .setLogLevel(LogLevel.FULL)
                    .build();

            /* New Handler to start the Menu-Activity
             * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//
//                        // Start your app main activity
                    Hawk.put("page","home");
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtra("from","SplashActivity");
                    startActivity(i);
                    finish();
//
                }
            }, SPLASH_DISPLAY_LENGTH);
        } catch (Exception ex) {
            String ms = ex.getMessage().toString();
            String y = ms;
        }
    }
}
