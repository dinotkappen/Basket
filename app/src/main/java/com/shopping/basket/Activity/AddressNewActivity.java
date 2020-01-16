package com.shopping.basket.Activity;

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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Application;
import com.shopping.basket.Model.CartModel.CartListModel.CartListModel;
import com.shopping.basket.Model.CityListModel.CityData;
import com.shopping.basket.Model.CityListModel.CityListModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.RegisterModel.SignUpModel;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Util.util.loadingAnimation;

public class AddressNewActivity extends AppCompatActivity {

    int cityId;
    TextView txtCity;
    Spinner spinCity;
    TextView txtTool;
    ImageView imgBack;
    String userTocken;
    Button btAddaddress;
    LoginData profile_dt;
    static API apiInterface;
    List<CityData> listCity = new ArrayList<>();
    private static AVLoadingIndicatorView avi;
    EditText editName,editEmail,editPhone,editCity,editAddress,editBuilding,editZone,editStreeet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
        setContentView(R.layout.add_address);
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
        spinCity        = findViewById(R.id.spin_city);
        txtCity         = findViewById(R.id.edit_city);
        editAddress = findViewById(R.id.edit_address);
        avi                 =  findViewById(R.id.avi);
        editBuilding        = findViewById(R.id.edit_build);
        editZone            = findViewById(R.id.edit_zone);
        editStreeet         = findViewById(R.id.edit_street);

        txtTool.setText(R.string.add_new_address);

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
                else if (editPhone.length()!=8){
                    editPhone.setError(getString(R.string.invalid_phone));
//                    Toast.makeText(getActivity(),getString(R.string.invalid_phone),Toast.LENGTH_SHORT).show();
                }
                else if (editAddress.getText().toString().equals("")){
                    editAddress.setError(getString(R.string.field_requirend));
                }
                else if (spinCity.getSelectedItem().equals("City")){
                    Toast.makeText(Application.getContext1(),getResources().getString(R.string.select_city),Toast.LENGTH_SHORT).show();
                }else if (editBuilding.getText().toString().equals("")){
                    editBuilding.setError(getResources().getString(R.string.valid_building));
                }
                else if (editZone.getText().toString().equals("")){
                    editZone.setError(getResources().getString(R.string.valid_zone));
                }
                else if (editStreeet.getText().toString().equals("")){
                    editStreeet.setError(getResources().getString(R.string.valid_city));
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
               if (position!=0)
                txtCity.setText(listCity.get(position).getCity());
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
        loadingAnimation(avi,true);
        Call<CityListModel> call = apiInterface.getCityList();
        call.enqueue(new Callback<CityListModel>() {
            @Override
            public void onResponse(Call<CityListModel> call, Response<CityListModel> response) {

                CityListModel status = new CityListModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("City_list",jsonElement.toString());
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
                loadingAnimation(avi,false);
            }

            @Override
            public void onFailure(Call<CityListModel> call, Throwable t) {
                loadingAnimation(avi,false);
            }
        });
    }

    public void addAddress(){
        loadingAnimation(avi,true);
        Call<CartListModel> call = apiInterface.addAddress(editName.getText().toString(),editPhone.getText().toString(),editEmail.getText().toString().trim(),editAddress.getText().toString(),cityId,editBuilding.getText().toString(),editZone.getText().toString(),editStreeet.getText().toString(),userTocken);
        call.enqueue(new Callback<CartListModel>() {
            @Override
            public void onResponse(Call<CartListModel> call, Response<CartListModel> response) {

                CartListModel status = new CartListModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("address",jsonElement.toString());
                status = gson.fromJson(jsonElement, CartListModel.class);
                if (status.getSuccess().getStatus()==200){

                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editEmail.setText("");
                    editPhone.setText("");
                    editAddress.setText("");
                    editBuilding.setText("");
                    editZone.setText("");
                    editStreeet.setText("");
                    txtCity.setText("");
                    txtCity.setHint(R.string.city);


                }
                loadingAnimation(avi,false);

            }

            @Override
            public void onFailure(Call<CartListModel> call, Throwable t) {
                loadingAnimation(avi,false);
            }
        });
    }
}
