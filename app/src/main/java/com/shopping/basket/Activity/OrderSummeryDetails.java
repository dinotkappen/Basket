package com.shopping.basket.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Adapter.OrderDetilsAdapter.OrderDetilsAdadpter;
import com.shopping.basket.Adapter.PlaceOrderAdapter.PlaceOrderAdadpter;
import com.shopping.basket.Application;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.OrderDetailsModel.OrderDetailsModel;
import com.shopping.basket.Model.OrderDetailsModel.ProductsDatum;
import com.shopping.basket.Model.OrderHistoryModel.OrderHistoryModel;
import com.shopping.basket.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Util.util.loadingFbAniamtion;

public class OrderSummeryDetails extends AppCompatActivity {

    API apiInterface;
    LoginData profile_dt;
    RecyclerView recyDetails;
    ImageView imgBack,img_loder;
    OrderDetilsAdadpter addapter;
    String orderNumber,userTocken;
    private static ShimmerFrameLayout mShimmerViewContainer;
    List<ProductsDatum> listProduct = new ArrayList<>();
    TextView txtTool,txtDate,txtNumber,txtPhone,txtEmail,txtAddress,txtPaymentMode,txtMrp,txtTotal;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_summery_details);
        apiInterface = APIClient.getClient().create(API.class);
        if (Hawk.get("logStatus","0").equals("1")) {

            profile_dt = Hawk.get("user_details");
            userTocken = profile_dt.getToken();
        }

        orderNumber = Hawk.get("order_id");
        Log.v("orderNumber",orderNumber);

        txtTool         = findViewById(R.id.txt_head);
        imgBack         = findViewById(R.id.img_back_right);
        recyDetails     = findViewById(R.id.recy_place_order);
        txtDate         = findViewById(R.id.txt_date);
        txtNumber       = findViewById(R.id.txt_number);
        txtAddress      = findViewById(R.id.txt_address);
        txtPhone        = findViewById(R.id.txt_phone);
        txtEmail        = findViewById(R.id.txt_email);
        txtPaymentMode  = findViewById(R.id.txt_payment_mod);
        txtMrp          = findViewById(R.id.txt_mrp);
        txtTotal        = findViewById(R.id.txt_total);
        img_loder       = findViewById(R.id.img_loder);

        Glide.with(this)
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);
        mShimmerViewContainer   = findViewById(R.id.shimmer_view_container);

        recyDetails.setLayoutManager(new GridLayoutManager(this, 1));
        addapter = new OrderDetilsAdadpter(this, listProduct);
        recyDetails.setAdapter(addapter);

        txtTool.setText(getString(R.string.order_summery));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        getOrderDetials();


    }

    public void getOrderDetials(){
//        loadingFbAniamtion(mShimmerViewContainer,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<OrderDetailsModel> call = apiInterface.getOrderDetilsa(orderNumber,userTocken);

        call.enqueue(new Callback<OrderDetailsModel>() {
            @Override
            public void onResponse(Call<OrderDetailsModel> call, Response<OrderDetailsModel> response) {
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("response",jsonElement.toString());
                OrderDetailsModel listHistory = gson.fromJson(jsonElement, OrderDetailsModel.class);

                if (listHistory.getSuccess().getStatus()==200){

                    txtDate.setText("Placed On : "+listHistory.getSuccess().getData().getDate());
                    txtNumber.setText("Order No  : "+listHistory.getSuccess().getData().getTitle());
                    txtAddress.setText(listHistory.getSuccess().getData().getBillingAddress().getAddress());
                    txtPhone.setText(listHistory.getSuccess().getData().getBillingAddress().getPhone());
                    txtEmail.setText(listHistory.getSuccess().getData().getBillingAddress().getEmail());
                    txtPaymentMode.setText(listHistory.getSuccess().getData().getPaymentInformation().getPaymentMethod());
                    txtTotal.setText(listHistory.getSuccess().getData().getPaymentInformation().getPrice());
                    listProduct = listHistory.getSuccess().getData().getProductsData();
                    addapter = new OrderDetilsAdadpter(OrderSummeryDetails.this, listProduct);
                    recyDetails.setAdapter(addapter);


                }
                else {
                    Toast.makeText(Application.getContext1(),listHistory.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
//                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<OrderDetailsModel> call, Throwable t) {
//                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
            }
        });

    }


}
