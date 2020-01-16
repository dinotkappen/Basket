package com.shopping.basket.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Application;
import com.shopping.basket.Model.CityListModel.CityData;
import com.shopping.basket.Model.CityListModel.CityListModel;
import com.shopping.basket.Model.RegisterModel.SignUpModel;
import com.shopping.basket.Model.RegisterModel.Success;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Util.util.loadingAnimation;

public class RegisterActivity extends AppCompatActivity {

    int cityId;
    Button btSubmit;
    API apiInterface;
    Spinner spinCity;
    String selectedLang;
    TextView txtLogin,txtCity;
    ImageView imgShowPassword;
    Success success = new Success();
    int flagLogin=0,passwordVisible=0;
    private static AVLoadingIndicatorView avi;
    List<CityData> listCity = new ArrayList<>();
    EditText editNmae,editEmail,editPhone,editPassword,editConfirmPassword,editBuilding,editZone,editStreeet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.register);

        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();

        txtLogin            = findViewById(R.id.sign_in);
        editNmae            = findViewById(R.id.edit_name);
        btSubmit            = findViewById(R.id.bt_regster);
        editEmail           = findViewById(R.id.edit_email);
        editPhone           = findViewById(R.id.edit_phone);
        editPassword        = findViewById(R.id.edit_password);
        editConfirmPassword = findViewById(R.id.edit_current_pass);
        imgShowPassword     = findViewById(R.id.show_password);
        avi                 =  findViewById(R.id.avi);
        spinCity            = findViewById(R.id.spin_city);
        editBuilding        = findViewById(R.id.edit_build);
        editZone            = findViewById(R.id.edit_zone);
        editStreeet         = findViewById(R.id.edit_street);
        txtCity             = findViewById(R.id.edit_city);

        selectedLang=Hawk.get("selectedLang","en");
//        if(selectedLang.equals("en"))
//        {
//            editNmae.setGravity(Gravity.LEFT);
//            editEmail.setGravity(Gravity.LEFT);
//            editPhone.setGravity(Gravity.LEFT);
//            editPassword.setGravity(Gravity.LEFT);
//        }
//        else
//        {
//            editNmae.setGravity(Gravity.RIGHT);
//            editEmail.setGravity(Gravity.RIGHT);
//            editPhone.setGravity(Gravity.RIGHT);
//            editPassword.setGravity(Gravity.RIGHT);
//        }


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

        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passwordVisible==0) {
                    editPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                    editPassword.setTransformationMethod(null);
                    passwordVisible=1;
                }
                else {
                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                    editPassword.setTransformationMethod(new PasswordTransformationMethod());
                    passwordVisible=0;
                }

            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editNmae.getText().toString().equals("")){
                    editNmae.setError(getString(R.string.vaild_name));
                    flagLogin = 1;
                }
                else if (!util.isValidEmail(editEmail.getText().toString())){
                    editEmail.setError(getString(R.string.valid_email));
                    flagLogin = 1;
                }else if (editPhone.getText().toString().equals("")){
                    editPhone.setError(getString(R.string.enter_phone));
                    flagLogin = 1;
                }
                else if (editPhone.length()!=8){
                    editPhone.setError(getString(R.string.invalid_phone));
//                    Toast.makeText(getApplicationContext(),getString(R.string.invalid_phone),Toast.LENGTH_SHORT).show();
                    flagLogin = 1;
                }else if (editPassword.getText().toString().equals("")){
                    editPassword.setError(getString(R.string.password_empty));
                    flagLogin = 1;
                }
                else if (editPassword.length()<8){
                    editPassword.setError(getString(R.string.eight_character));
//                    Toast.makeText(getApplicationContext(),getString(R.string.eight_character),Toast.LENGTH_SHORT).show();
                    flagLogin = 1;
                }
                else if (spinCity.getSelectedItem().equals("City")){
                    Toast.makeText(Application.getContext1(),getResources().getString(R.string.select_city),Toast.LENGTH_SHORT).show();
                }
                else if (editBuilding.getText().toString().equals("")){
                    editBuilding.setError(getResources().getString(R.string.valid_building));
                }
                else if (editZone.getText().toString().equals("")){
                    editZone.setError(getResources().getString(R.string.valid_zone));
                }
                else if (editStreeet.getText().toString().equals("")){
                    editStreeet.setError(getResources().getString(R.string.valid_city));
                }
                else {
                    register();
                }

//                if (flagLogin==0){
//                    register();
//                    flagLogin=0;
//                }
//                else {
//                    flagLogin=0;
//                }

            }
        });




        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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

                    ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),R.layout.spinner_text,city);
                    aa.setDropDownViewResource(R.layout.spinner_text);
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

    public void register(){
        loadingAnimation(avi,true);
        Call<SignUpModel> call = apiInterface.SignUp(editNmae.getText().toString().trim(),editEmail.getText().toString().trim(),editPassword.getText().toString().trim(),editPhone.getText().toString().trim(),cityId,editBuilding.getText().toString(),editZone.getText().toString(),editStreeet.getText().toString(),selectedLang);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {

                SignUpModel status =new SignUpModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                status = gson.fromJson(jsonElement, SignUpModel.class);
                Log.e("user_dt",jsonElement.toString());
                success = status.getSuccess();
                if (success.getStatus()==200){
                    Toast.makeText(Application.getContext1(),success.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingAnimation(avi,false);
                    finish();
                }
                else {
                    Toast.makeText(Application.getContext1(),success.getMessage(),Toast.LENGTH_SHORT).show();
                }

                loadingAnimation(avi,false);
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {

                loadingAnimation(avi,false);
                Log.e("error",t.getMessage());

            }
        });
    }
}
