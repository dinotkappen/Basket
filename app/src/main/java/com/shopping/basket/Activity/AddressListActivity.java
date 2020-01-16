package com.shopping.basket.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.shopping.basket.Adapter.AddressListAdapter.AddressListAdapter;
import com.shopping.basket.Model.AddressListModel.AddressListData;
import com.shopping.basket.Model.AddressListModel.AddressListModel;
import com.shopping.basket.Model.CityListModel.CityData;
import com.shopping.basket.Model.CityListModel.CityListModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Util.util.loadingAnimation;

public class AddressListActivity extends AppCompatActivity {

    TextView txtTool;
    static String userTocken;
    LoginData profile_dt;
    static RecyclerView recyList;
    static API apiInterface;
    CardView cardAddaddress;
    ImageView imgBack;
    static ImageView img_loder;
    static LinearLayout lenNoAddress;
    static Activity context;
    static AddressListAdapter adadpter;
    private static AVLoadingIndicatorView avi;
    static List<CityData> listCity = new ArrayList<>();
    static List<AddressListData> listAddress = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
        setContentView(R.layout.address_list);
        context =AddressListActivity.this;
        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        if (Hawk.get("logStatus","0").equals("1")) {

            profile_dt = Hawk.get("user_details");
            userTocken = profile_dt.getToken();
        }
        txtTool         = findViewById(R.id.txt_head);
        imgBack         = findViewById(R.id.img_back_right);
        avi             =  findViewById(R.id.avi);
        cardAddaddress  =  findViewById(R.id.card_add_address);
        recyList        =  findViewById(R.id.recy_address);
        lenNoAddress    =  findViewById(R.id.len_no_address);
        img_loder       = findViewById(R.id.img_loder);

        Glide.with(this)
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);

        txtTool.setText(R.string.address);

        recyList.setLayoutManager(new GridLayoutManager(this, 1));
        adadpter = new AddressListAdapter(this, listAddress,listCity);
        recyList.setAdapter(adadpter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cardAddaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent address = new Intent(AddressListActivity.this, AddressNewActivity.class);
                startActivity(address);
            }
        });


    }

    public void citys(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<CityListModel> call = apiInterface.getCityList();
        call.enqueue(new Callback<CityListModel>() {
            @Override
            public void onResponse(Call<CityListModel> call, Response<CityListModel> response) {

                CityListModel status = new CityListModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("City_list",jsonElement.toString());
                status = gson.fromJson(jsonElement, CityListModel.class);
                if (status.getSuccess().getStatus()==200){
                    listCity = status.getSuccess().getData();
                }
                else {
                    Toast.makeText(getApplicationContext(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT);
                }
                listAddress();
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CityListModel> call, Throwable t) {
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }
        });
    }

    public static void listAddress(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<AddressListModel> call = apiInterface.addressList(userTocken);
        call.enqueue(new Callback<AddressListModel>() {
            @Override
            public void onResponse(Call<AddressListModel> call, Response<AddressListModel> response) {

                AddressListModel status = new AddressListModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("addList",jsonElement.toString());
                status = gson.fromJson(jsonElement, AddressListModel.class);
                if (status.getSuccess().getStatus()==200){
                    listAddress.clear();
                    listAddress = status.getSuccess().getData();
                    adadpter = new AddressListAdapter(context, listAddress,listCity);
                    recyList.setAdapter(adadpter);
                    adadpter.notifyDataSetChanged();
                    if (listAddress.size()==0){
                        lenNoAddress.setVisibility(View.VISIBLE);
                    }
                    else {
                        lenNoAddress.setVisibility(View.GONE);
                    }

                }
                else {
                    lenNoAddress.setVisibility(View.VISIBLE);
                    Toast.makeText(context,status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<AddressListModel> call, Throwable t) {
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        citys();

    }
    public static void back(){

        Intent data = new Intent();
        context.setResult(RESULT_OK, data);
        context.finish();

    }
    public static void loading(boolean status){

        if (status){
            img_loder.setVisibility(View.VISIBLE);
        }
        else {
            img_loder.setVisibility(View.GONE);
        }

    }


}
