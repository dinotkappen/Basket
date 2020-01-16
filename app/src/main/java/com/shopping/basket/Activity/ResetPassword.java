package com.shopping.basket.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Application;
import com.shopping.basket.Model.CartModel.CartListModel.CartListModel;
import com.shopping.basket.Model.CartUpdateModel.CartUpdateModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.R;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Util.util.loadingAnimation;

public class ResetPassword extends AppCompatActivity {

    String userTocken;
    TextView txtTool;
    Button btResetPassword;
    static API apiInterface;
    LoginData profile_dt;
    ImageView imgBack,img_loder;
    private static AVLoadingIndicatorView avi;
    EditText editCurrentPass,editNewPass,editConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_password);

        apiInterface = APIClient.getClient().create(API.class);
        if (Hawk.get("logStatus","0").equals("1")) {

            profile_dt = Hawk.get("user_details");
            userTocken = profile_dt.getToken();
        }
        txtTool         = findViewById(R.id.txt_head);
        imgBack         = findViewById(R.id.img_back_right);
        avi             = findViewById(R.id.avi);
        btResetPassword = findViewById(R.id.bt_regster);
        editCurrentPass = findViewById(R.id.edit_current_pass);
        editNewPass     = findViewById(R.id.edit_new_pass);
        editConfirmPass = findViewById(R.id.edit_confirm);
        img_loder       = findViewById(R.id.img_loder);

        Glide.with(this)
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);

        txtTool.setText(R.string.change_password);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editCurrentPass.getText().toString().equals("")){
                    editCurrentPass.setError(getString(R.string.field_requirend));
                }
                else if (editNewPass.getText().toString().equals("")){
                    editNewPass.setError(getString(R.string.field_requirend));
                }
                else if (!editNewPass.getText().toString().equals(editConfirmPass.getText().toString())){
                    editConfirmPass.setError(getString(R.string.pass_not_match));
                }
                else {
                    resrPassword();
                }

            }
        });


    }
    public void resrPassword(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<CartUpdateModel> call = apiInterface.changePassword(editCurrentPass.getText().toString(),editNewPass.getText().toString(),editConfirmPass.getText().toString(),userTocken);
        call.enqueue(new Callback<CartUpdateModel>() {
            @Override
            public void onResponse(Call<CartUpdateModel> call, Response<CartUpdateModel> response) {
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("response",jsonElement.toString());
                CartUpdateModel status = gson.fromJson(jsonElement, CartUpdateModel.class);
                if (status.getSuccess().getStatus()==200){
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CartUpdateModel> call, Throwable t) {
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }
        });
    }
}
