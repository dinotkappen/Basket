package com.shopping.basket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Model.CheckoutPriceModel.CheckoutPriceModel;
import com.shopping.basket.Model.ForgotPasswordModel.ForgotPasswordModel;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Util.util.loadingAnimation;


public class FogotPassword extends AppCompatActivity {

    /**
     * Duration of wait
     **/

    Button btSend;
    API apiInterface;
    EditText editMail;
    TextView txtBack;
    private static AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.forgot_password);

            Hawk.init(getApplicationContext())
                    .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                    .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                    .setLogLevel(LogLevel.FULL)
                    .build();

        apiInterface = APIClient.getClient().create(API.class);
        avi         =  findViewById(R.id.avi);
        btSend      =  findViewById(R.id.bt_update);
        editMail    =  findViewById(R.id.txt_email);
        txtBack    =  findViewById(R.id.txt_back);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!util.isValidEmail(editMail.getText().toString())){
                    editMail.setError(getResources().getString(R.string.valid_email));
                }
                else {
                    forgotPassword();
                }

            }
        });

            /* New Handler to start the Menu-Activity
             * and close this Splash-Screen after some seconds.*/

    }

    public void forgotPassword(){
        loadingAnimation(avi,true);
        Call<ForgotPasswordModel> call = apiInterface.fogotPassword(editMail.getText().toString().trim());
        call.enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {

                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("response",jsonElement.toString());
                ForgotPasswordModel passwordststus = gson.fromJson(jsonElement, ForgotPasswordModel.class);

                if (passwordststus.getSuccess().getStatus()==200){
                    Toast.makeText(FogotPassword.this,(passwordststus.getSuccess().getMessage()),Toast.LENGTH_SHORT).show();
                    editMail.setText("");
                }
                else {
                    Toast.makeText(FogotPassword.this,(passwordststus.getSuccess().getMessage()),Toast.LENGTH_SHORT).show();
                }
                loadingAnimation(avi,false);
            }

            @Override
            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                loadingAnimation(avi,false);
            }
        });

    }
}
