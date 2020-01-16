package com.shopping.basket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Adapter.PlaceOrderAdapter.PlaceOrderAdadpter;
import com.shopping.basket.Application;
import com.shopping.basket.Model.AddressListModel.AddressListData;
import com.shopping.basket.Model.CartModel.CartListModel.CartList;
import com.shopping.basket.Model.CartModel.CartListModel.PriceDetails;
import com.shopping.basket.Model.CheckoutPriceModel.CheckoutPriceModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.PlaceOrderModel.PlaceOrderModel;
import com.shopping.basket.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Util.util.loadingAnimation;

public class PlaceOrderActivity extends AppCompatActivity {

    API apiInterface;
    TextView txtTool;
    int successFage=0;
    LoginData profile_dt;
    LinearLayout lenSucces;
    RecyclerView placeOrder;
    Button btPlaceOrder;
    PlaceOrderAdadpter adapter;
    ImageView imgBack,img_loder;
    String userTocken,deliveryTime,strCity="Kochi";
    private static AVLoadingIndicatorView avi;
    PriceDetails priceDt = new PriceDetails();
    static List<CartList> listCart = new ArrayList<>();
    AddressListData addressdata = new AddressListData();
    TextView txtPhone,txtEmail,txtAddress,txtPaymentMode,txtMrp,txtDiscount,txtTotal,txtshipAmount;
String  cityID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.place_order);
        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        if (Hawk.get("logStatus","0").equals("1")) {

            profile_dt = Hawk.get("user_details");
            userTocken = profile_dt.getToken();
        }
        listCart = Hawk.get("cart_details",null);
        priceDt =  Hawk.get("total_payment",null);
        deliveryTime =  Hawk.get("deliveryTime","null");

        apiInterface = APIClient.getClient().create(API.class);

        txtTool         = findViewById(R.id.txt_head);
        imgBack         = findViewById(R.id.img_back_right);
        txtAddress      = findViewById(R.id.txt_address);
        txtPhone        = findViewById(R.id.txt_phone);
        txtEmail        = findViewById(R.id.txt_email);
        txtPaymentMode  = findViewById(R.id.txt_payment_mod);
        txtMrp          = findViewById(R.id.txt_mrp);
        txtDiscount     = findViewById(R.id.txt_discount);
        txtTotal        = findViewById(R.id.txt_total);
        placeOrder      = findViewById(R.id.recy_place_order);
        avi             =  findViewById(R.id.avi);
        lenSucces       =  findViewById(R.id.len_cuccess);
        btPlaceOrder    =  findViewById(R.id.bt_place_order);
        txtshipAmount   =  findViewById(R.id.txt_shipping_amount);
        img_loder               = findViewById(R.id.img_loder);

        Glide.with(this)
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);

        placeOrder.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new PlaceOrderAdadpter(this, listCart);
        placeOrder.setAdapter(adapter);
        addressdata = Hawk.get("latest_address",null);
        txtAddress.setText( Hawk.get("Address","null"));
        if (addressdata==null){
            txtPhone.setText( profile_dt.getPhone());
            txtEmail.setText(profile_dt.getEmail());
            strCity = Hawk.get("city","null");
        }
        else {
            txtPhone.setText(addressdata.getShippingPhone());
            txtEmail.setText(addressdata.getShippingEmail());
        }
        txtPaymentMode.setText(Hawk.get("payment","null"));
//        txtMrp.setText(priceDt.getTotalMrp()+" QAR");
//        txtDiscount.setText(priceDt.getDiscount()+" Qar");
////        txtDiscount.setText(priceDt.getDiscount()+" Qar");
//        txtTotal.setText(priceDt.getTotal()+" Qar");

        txtTool.setText(getString(R.string.order_summery));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (successFage==0)
                        finish();
                else {
                    Intent main = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(main);
                }

            }
        });
        btPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                placeOrder();

            }
        });

        getPriceDetails();


    }
    public void placeOrder(){

//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        Log.e("place_order",profile_dt.getName()+"\n"+profile_dt.getPhone()+"\n"+profile_dt.getEmail()+"\n"+txtAddress.getText().toString()+"\n"+txtPaymentMode.getText().toString()+"\n"+strCity+"\n"+deliveryTime+"\n"+userTocken);
        Call<PlaceOrderModel> call = apiInterface.placeOrder(profile_dt.getName().toString(),profile_dt.getPhone(),profile_dt.getEmail(),txtAddress.getText().toString(),txtPaymentMode.getText().toString(),strCity,deliveryTime,addressdata.getShippingBuildingNo(),addressdata.getShippingZone(),addressdata.getShippingStreet(),userTocken);
        call.enqueue(new Callback<PlaceOrderModel>() {
            @Override
            public void onResponse(Call<PlaceOrderModel> call, Response<PlaceOrderModel> response) {
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("response",jsonElement.toString());
                PlaceOrderModel cartList = gson.fromJson(jsonElement, PlaceOrderModel.class);
                if (cartList.getSuccess().getStatus()==200){
                    lenSucces.setVisibility(View.VISIBLE);
                    successFage = 1;
                    Hawk.get("cart_count","0");
                }
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PlaceOrderModel> call, Throwable t) {
                img_loder.setVisibility(View.GONE);
//                loadingAnimation(avi,false);
            }
        });
    }
    public void onBackPressed(){
        if (successFage==0)
            finish();
        else {
            Intent main = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(main);
        }
    }

    public void getPriceDetails(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        cityID=""+addressdata.getShippingCity();
        Log.v("cityID",""+cityID);
        if (cityID != null && !cityID.isEmpty() && !cityID.equals("null"))
        {
            Call<CheckoutPriceModel> call = apiInterface.getPrice(Integer.parseInt(cityID),userTocken);
            call.enqueue(new Callback<CheckoutPriceModel>() {
                @Override
                public void onResponse(Call<CheckoutPriceModel> call, Response<CheckoutPriceModel> response) {

                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    Log.e("response",jsonElement.toString());
                    CheckoutPriceModel pricList = gson.fromJson(jsonElement, CheckoutPriceModel.class);

                    if (pricList.getSuccess().getStatus()==200){

                        txtshipAmount.setText(pricList.getSuccess().getData().getPriceDetails().getShippingAmount()+" QAR");
                        txtDiscount.setText(pricList.getSuccess().getData().getPriceDetails().getDiscount()+" QAR");
                        txtMrp.setText(pricList.getSuccess().getData().getPriceDetails().getTotal()+" QAR");
                        txtTotal.setText(pricList.getSuccess().getData().getPriceDetails().getTotalMrp()+" QAR");

                    }else {
                        Toast.makeText(Application.getContext1(),pricList.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                    }
//                loadingAnimation(avi,false);
                    img_loder.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<CheckoutPriceModel> call, Throwable t) {
//                loadingAnimation(avi,false);
                    img_loder.setVisibility(View.GONE);
                }
            });

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please select a Address",Toast.LENGTH_SHORT).show();
        }


    }

}


