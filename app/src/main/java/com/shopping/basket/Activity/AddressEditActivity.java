package com.shopping.basket.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.shopping.basket.Application;
import com.shopping.basket.Model.AddressListModel.AddressListData;
import com.shopping.basket.Model.CartModel.CartListModel.CartListModel;
import com.shopping.basket.Model.CityListModel.CityData;
import com.shopping.basket.Model.CityListModel.CityListModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Util.util.loadingAnimation;

public class AddressEditActivity extends AppCompatActivity {

    int cityId;
    Spinner spinCity;
    String userTocken;
    Button btAddaddress;
    LoginData profile_dt;
    static API apiInterface;
    TextView txtTool,editCity;
    ImageView imgBack,img_loder;
    List<CityData> listCity = new ArrayList<>();
    AddressListData itemMainModel = new AddressListData();
    private static AVLoadingIndicatorView avi;
    EditText editName,editEmail,editPhone,editAddress,edit_build,edit_zone,edit_street;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
        setContentView(R.layout.add_address);
        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        if (Hawk.get("logStatus","0").equals("1")) {

            profile_dt = Hawk.get("user_details");
            userTocken = profile_dt.getToken();
        }
        txtTool = findViewById(R.id.txt_head);
        imgBack = findViewById(R.id.img_back_right);
        btAddaddress = findViewById(R.id.bt_add_address);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        editCity = findViewById(R.id.edit_city);
        editAddress = findViewById(R.id.edit_address);
        edit_build = findViewById(R.id.edit_build);
        edit_zone = findViewById(R.id.edit_zone);
        edit_street = findViewById(R.id.edit_street);
        spinCity        = findViewById(R.id.spin_city);
        avi                 =  findViewById(R.id.avi);
        img_loder       = findViewById(R.id.img_loder);

        Glide.with(this)
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);

        itemMainModel = Hawk.get("edit_address", itemMainModel);
        editName.setText(itemMainModel.getShippingName());
        editEmail.setText(itemMainModel.getShippingEmail());
        editPhone.setText(itemMainModel.getShippingPhone());
        editAddress.setText(itemMainModel.getShippingAddress());
        edit_build.setText(itemMainModel.getShippingBuildingNo());
        edit_zone.setText(itemMainModel.getShippingZone());
        edit_street.setText(itemMainModel.getShippingStreet());
        txtTool.setText(getString(R.string.edit_address));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btAddaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editName.getText().toString().equals("")){
                    editName.setError(getString(R.string.vaild_name));
                }
                else if (!util.isValidEmail(editEmail.getText().toString().trim())){
                    editEmail.setError(getString(R.string.valid_email));
                }
                else if (editPhone.getText().toString().equals("")){
                    editPhone.setError(getString(R.string.enter_phone));
                }
                else if (editAddress.getText().toString().equals("")){
                    editAddress.setError(getString(R.string.field_requirend));
                }
                else if (spinCity.getSelectedItem().equals("City")){
                    Toast.makeText(Application.getContext1(),"Select City",Toast.LENGTH_SHORT).show();
                }
                else {
                    addAddress();
                }

            }
        });
        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("city",listCity.get(position).getCity());
//                if (flageFirstLoading!=0) {
                editCity.setText(listCity.get(position).getCity());
                cityId = listCity.get(position).getId();
//                }
//                flageFirstLoading=1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        citys();



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
                status = gson.fromJson(jsonElement, CityListModel.class);
                listCity.clear();
                if (status.getSuccess().getStatus()==200){
                    listCity = status.getSuccess().getData();


                    CityData zone_name= new CityData();

                    zone_name.setCity("City");
                    zone_name.setId(0);
                    listCity.add(0,zone_name);
                    ArrayList city = new ArrayList();

                    for (int i = 0; i < listCity.size() ; i++) {

                        city.add(listCity.get(i).getCity());
//                        zip_code.add(listCity.get(i).getZoneCode());

                    }

                    ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,city);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinCity.setAdapter(aa);
                }
                else {
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT);
                }
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

    public void addAddress(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<CartListModel> call = apiInterface.editAddress(editName.getText().toString(),editPhone.getText().toString(),editEmail.getText().toString().trim(),editAddress.getText().toString(),itemMainModel.getId(),cityId,Integer.parseInt(edit_build.getText().toString()),Integer.parseInt(edit_zone.getText().toString()),Integer.parseInt(edit_street.getText().toString()),userTocken);
        call.enqueue(new Callback<CartListModel>() {
            @Override
            public void onResponse(Call<CartListModel> call, Response<CartListModel> response) {

                CartListModel status = new CartListModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.v("resultEditAdrz",jsonElement.toString());
                status = gson.fromJson(jsonElement, CartListModel.class);
                if (status.getSuccess().getStatus()==200){

                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editEmail.setText("");
                    editPhone.setText("");
//                    editCity.setText("");
                    editAddress.setText("");

                }
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<CartListModel> call, Throwable t) {
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }
        });
    }
}
