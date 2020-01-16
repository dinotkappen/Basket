package com.shopping.basket.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
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
import com.shopping.basket.Model.DeviceRegistration.ModelDeviceRegister;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.LoginModel.LoginModel;
import com.shopping.basket.Model.LoginModel.LoginSuccess;
import com.shopping.basket.Model.RegisterModel.SignUpModel;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;
import com.shopping.basket.service.Config;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.MainActivity.updateHeader;
import static com.shopping.basket.Util.util.loadingAnimation;

public class LoginActivity extends AppCompatActivity {

     EditText phone;
    String userToken="";
    int cityId;
    Button btLogin;
    Button btGoogle;
    int flagLogin=0;
    API apiInterface;
    Spinner spinCity;
    Dialog missingValue;
    int RC_SIGN_IN = 100;
    String firebase_token;
    LoginButton loginButton;
    Task<InstanceIdResult> token;
    GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    public static String accessToken_str,fbID;
    String name_fb, email_fb,URL_FB,selectedLang;
    TextView txtSignUp,txtCity,txtForgoPass,txtSkip;
    List<CityData> listCity = new ArrayList<>();
    private static AVLoadingIndicatorView avi,avi1;
    EditText editEmail,editPassword,editBuilding,editZone,editStreeet;
    String strName="",strEmail="",strPhone="",strID="",strPassword,strToken;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ArrayList city = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = APIClient.getClient().create(API.class);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login);
        FirebaseApp.initializeApp(this);
//        hashFromSHA1("00:F3:11:A1:E7:F2:42:8F:04:37:E4:C4:D6:5B:3E:FD:78:00:50:3C");
        FacebookSdk.sdkInitialize(getApplication());
        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();
        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();

        LoginManager.getInstance().logOut();
        loginButton     =  findViewById(R.id.login_button);
        btLogin         =  findViewById(R.id.bt_login);
        editEmail       =  findViewById(R.id.edit_email);
        editPassword    =  findViewById(R.id.edit_password);
        avi             =  findViewById(R.id.avi);
        txtForgoPass    =  findViewById(R.id.txt_forgot_password);
        txtSkip         =  findViewById(R.id.txt_skip);

        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtForgoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fogot = new Intent(LoginActivity.this,FogotPassword.class);
                startActivity(fogot);
            }
        });

        selectedLang = Hawk.get("selectedLang", "en");
//        if (selectedLang.equals("en")) {
//            editEmail.setGravity(Gravity.LEFT);
//            editPassword.setGravity(Gravity.LEFT);
//
//        } else {
//            editEmail.setGravity(Gravity.RIGHT);
//            editPassword.setGravity(Gravity.RIGHT);
//        }
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!util.isValidEmail(editEmail.getText().toString())){
                    editEmail.setError(getResources().getString(R.string.valid_email));
                    flagLogin = 1;
                }
                else if (editPassword.getText().toString().equals("")){
                    editPassword.setError(getString(R.string.password_empty));
                    flagLogin = 1;
                }
                if (flagLogin==0){
                    loginUser();
                }
                else {
                    flagLogin=0;
                }

            }
        });
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        //*****************************************FirebaseId***************************************************
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.e("firebase_faild", "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        firebase_token = task.getResult().getToken();
//
//                        // Log and toast
////                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.e("token", firebase_token);
//                        Toast.makeText(LoginActivity.this, firebase_token, Toast.LENGTH_SHORT).show();
//                    }
//                });
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                // checking for type intent filter
//                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//
//                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
//
//                    displayFirebaseRegId();
//
//                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//
//                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//
//                    // txtMessage.setText(message);
//                }
//            }
//        };

        txtSignUp = findViewById(R.id.sign_up);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent register = new Intent(getApplication(),RegisterActivity.class);
                startActivity(register);

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

//        GoogleSignIn.silentSignIn()
//                .addOnCompleteListener(
//                        this,
//                        new OnCompleteListener<GoogleSignInAccount>() {
//                            @Override
//                            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
//                                handleSignInResult(task);
//                            }
//                        });
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btGoogle = findViewById(R.id.bt_google);

        btGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        try {
                            accessToken_str = loginResult.getAccessToken().getToken();
                            fbID = loginResult.getAccessToken().getUserId();
                            Log.v("fbID", fbID);



                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject jsonObject,
                                                                GraphResponse response) {

                                            // Getting FB User LoginData
                                            Bundle facebookData = getFacebookData(jsonObject);


                                        }
                                    });

                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,first_name,last_name,email,gender");
                            request.setParameters(parameters);
                            request.executeAsync();
                        } catch (Exception ex) {
                            String msg = ex.getMessage().toString();
                            String r = msg;
                        }
                    }


                    @Override
                    public void onCancel() {
//                        Log.d(TAG, "Login attempt cancelled.");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        e.printStackTrace();
//                        Log.d(TAG, "Login attempt failed.");
//                        deleteAccessToken();
                    }
                }
        );


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            Log.e("user_dt1", account.getEmail());
            Log.e("user_dt2", account.getDisplayName());
            Log.e("user_dt3", account.getIdToken());

            strName =  account.getDisplayName();
            strEmail =  account.getEmail();
            strID =  account.getId();

            strToken = account.getIdToken();
//            registerSocialFB("gplus");
            registerSocialFB("gmail");
//            missingInformation("google");


            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("user_dt_error", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }
    private void displayFirebaseRegId() {

       String id= Hawk.get("regId", "null");
        Log.e("fireBase",  id);

//        if (!TextUtils.isEmpty(regId))
////            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
        //txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");


//            Log.e("user_dt",accessToken_str);
            strToken = accessToken_str;
            strPassword = accessToken_str;
//            Log.e("user_dt",object.toString());
            bundle.putString("idFacebook", id);
            if (object.has("first_name")) {
                bundle.putString("first_name", object.getString("first_name"));
                strName = object.getString("first_name");
            }

//            Log.e("user_dt",object.getString("first_name"));

            if (object.has("last_name")) {
                bundle.putString("last_name", object.getString("last_name"));
//                Log.e("user_dt", object.getString("last_name"));
                strName = strName+" "+object.getString("last_name");
            }

//            if (object.has("gender"))
//                bundle.putString("gender", object.getString("gender"));

            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
                strEmail = object.getString("email");
            }
//            if (object.has("phone")){
//                strPhone = object.getString("phone");
//
//            }
            else
            {
//                Log.v("firebasseRegID",firebasseRegID);
                name_fb = object.getString("first_name");
//                Intent in=new Intent(LoginActivity.this,FbEmailActivity.class);
//                in.putExtra("valueMissing","email");
//                in.putExtra("name",name_fb);
//                in.putExtra("fb_id",fbID);
//                in.putExtra("token",accessToken_str);
//                in.putExtra("firebasseRegID",firebasseRegID);
//                startActivity(in);
            }
            try {
                name_fb = object.getString("first_name");
//                email_fb = object.getString("email");
                strID =  fbID;

//                registerSocialFB("fb");
                registerSocialFB("fb");
//                missingInformation("facebook");
//                if (email_fb != null && !email_fb.isEmpty() && !email_fb.equals("null"))
//                {
//
//                    FB_API();
//                }
//                else
//                {
//                    Intent in=new Intent(LogInActivity.this,FbEmailActivity.class);
//                    in.putExtra("name",name_fb);
//                    in.putExtra("fb_id",fbID);
//                    in.putExtra("token",accessToken_str);
//                    in.putExtra("firebasseRegID",firebasseRegID);
//                    startActivity(in);
//
//
//                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
//            prefUtil.saveFacebookUserInfo(object.getString("first_name"),
//                    object.getString("last_name"), object.getString("email"),
//                    "gender", profile_pic.toString());

        } catch (Exception e) {
//            Log.d(TAG, "BUNDLE Exception : " + e.toString());
        }

        return bundle;
    }


    public void hashFromSHA1(String sha1) {
        String[] arr = sha1.split(":");
        byte[] byteArr = new  byte[arr.length];

        for (int i = 0; i< arr.length; i++) {
            byteArr[i] = Integer.decode("0x" + arr[i]).byteValue();
        }

        Log.e("hash_:", Base64.encodeToString(byteArr, Base64.NO_WRAP));
    }


    public void loginUser(){
        loadingAnimation(avi,true);
        Call<LoginModel> call = apiInterface.Login(editEmail.getText().toString().trim(),editPassword.getText().toString().trim());
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                LoginModel status = new LoginModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                status = gson.fromJson(jsonElement, LoginModel.class);
                Log.e("user_dt",jsonElement.toString());
                LoginSuccess loginStatus= status.getSuccess();
//                String user = status.getUser();
                if (loginStatus.getStatus()==200) {
                    Hawk.put("user_details", loginStatus.getData());
                    Hawk.put("logStatus", "1");
//                    registerDeveice(profile_dt.getLoginToken());
                    Hawk.put("page","home");
                    updateHeader();
                    finish();
                } else {
                    Toast.makeText(Application.getContext1(), loginStatus.getMessage(), Toast.LENGTH_SHORT).show();
                }

                loadingAnimation(avi,false);
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

            }
        });
    }

//    public void registerDeveice(String deviceToken){
//        Call<ModelDeviceRegister> call = apiInterface.deviceRegister(firebase_token,"Android","3",deviceToken);
//        call.enqueue(new Callback<ModelDeviceRegister>() {
//            @Override
//            public void onResponse(Call<ModelDeviceRegister> call, Response<ModelDeviceRegister> response) {
//
//                ModelDeviceRegister devicgerster  = new ModelDeviceRegister();
//
//                Gson gson = new Gson();
//                JsonElement jsonElement = gson.toJsonTree(response.body());
//                devicgerster = gson.fromJson(jsonElement, ModelDeviceRegister.class);
//                Log.e("pkb_dt",jsonElement.toString());
////                Log.e("response",devicgerster.getMsg());
//                if (devicgerster.getSuccess()==1){
//                    Intent home = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(home);
//                }
//                loadingAnimation(avi,false);
//
//            }
//
//            @Override
//            public void onFailure(Call<ModelDeviceRegister> call, Throwable t) {
//                loadingAnimation(avi,false);
//            }
//        });
//    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    public void missingInformation(){
        missingValue = new Dialog(LoginActivity.this);
        missingValue.requestWindowFeature(Window.FEATURE_NO_TITLE);
        missingValue.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        missingValue.setCancelable(true);
        missingValue.setContentView(R.layout.confirm_email);


        final TextView name  = missingValue.findViewById(R.id.edit_name);
        final EditText email = missingValue.findViewById(R.id.edit_email);
         phone = missingValue.findViewById(R.id.edit_phone);
        avi1                 = missingValue.findViewById(R.id.avi);
        spinCity             = missingValue.findViewById(R.id.spin_city);
        editBuilding         = missingValue.findViewById(R.id.edit_build);
        editZone             = missingValue.findViewById(R.id.edit_zone);
        editStreeet          = missingValue.findViewById(R.id.edit_street);
        txtCity              = missingValue.findViewById(R.id.edit_city);

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

        Button submit = missingValue.findViewById(R.id.bt_regster);
        name.setText(strName);
        email.setText(strEmail);
        phone.setText(strPhone);
        if (!email.getText().toString().equals("")){
            email.setFocusable(false);
        }if (!phone.getText().toString().equals("")){
            phone.setFocusable(false);
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")){
                    name.setError(getString(R.string.vaild_name));
                    flagLogin = 1;
                }
                else if (!util.isValidEmail(email.getText().toString())){
                    email.setError(getString(R.string.valid_email));
                    flagLogin = 1;
                }
                else if (phone.getText().toString().equals("")){
                    phone.setError(getString(R.string.enter_phone));
                    flagLogin = 1;
                } else if (phone.length()!=8){
                    phone.setError(getString(R.string.invalid_phone));
//                    Toast.makeText(getApplicationContext(),getString(R.string.invalid_phone),Toast.LENGTH_SHORT).show();
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
//                    if (type.equals("facebook")){
//                        registerSocialFB("fb");
//                    }
                    socialLogin();
                }
//                if (flagLogin==0){
////                    registerSocial(type);
//
//                    if (type.equals("facebook")){
//                        registerSocialFB("fb");
//                    }
//
//                    flagLogin=0;
//                }
//                else {
//                    flagLogin=0;
//                }
            }
        });

        citys();

        missingValue.show();
    }



    public void citys(){
        loadingAnimation(avi1,true);
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
                     city .clear();

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
                loadingAnimation(avi1,false);
            }

            @Override
            public void onFailure(Call<CityListModel> call, Throwable t) {
                loadingAnimation(avi1,false);
            }
        });
    }



    public void registerSocialFB(String type){
        loadingAnimation(avi,true);
        Call<LoginModel> call = null;
        Log.e("user_dt_pkb",strID+"\n"+strToken+"\n"+strName+"\n"+strEmail+"\n"+strPhone);
        if (type.equals("fb"))
         call = apiInterface.FbLogin(strID.trim(),strToken);
        else{
            call = apiInterface.GpLogin(strID.trim(),strToken);
        }
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                LoginModel status;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("user_dt",jsonElement.toString());
                status = gson.fromJson(jsonElement, LoginModel.class);

                if (status.getSuccess().getStatus()==200){

                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_LONG).show();

//                    if (status.getSuccess().getData().getName().equals("")){
//                        if (!status.getSuccess().getData().getToken().equals(""))
//                            userToken = status.getSuccess().getData().getToken();
//                        missingInformation();
//                        Hawk.put("user_details", status.getSuccess().getData());
//                        Hawk.put("logStatus", "1");
//                        Hawk.put("page","home");
//                        updateHeader();
//                        finish();
//                    }
//                    else {
                        Hawk.put("user_details", status.getSuccess().getData());
                        Hawk.put("logStatus", "1");
                        Hawk.put("page","home");
                        updateHeader();
                        finish();
//                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(home);
//                    }


                }
                else{
                   Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_LONG).show();
                }
                loadingAnimation(avi,false);
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                loadingAnimation(avi,false);

            }
        });
    }

    public void socialLogin(){
        loadingAnimation(avi1,true);
        Call<LoginModel> call = null;

        Log.e("user_dt",strName+"\n"+strPhone+"\n"+strEmail+"\n"+cityId+"\n"+editBuilding.getText().toString()+"\n"+editZone.getText().toString()+"\n"+editStreeet.getText().toString()+"\n"+userToken);
            call = apiInterface.socialLogin(strName,phone.getText().toString(),strEmail,cityId,editBuilding.getText().toString(),editZone.getText().toString(),editStreeet.getText().toString(),userToken);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                LoginModel status;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("user_dt",jsonElement.toString());
                status = gson.fromJson(jsonElement, LoginModel.class);

                if (status.getSuccess().getStatus()==200){
                    LoginData  user_dt = status.getSuccess().getData();
                    user_dt.setToken(userToken);
                    Hawk.put("user_details", user_dt);
                    Hawk.put("logStatus", "1");
                    Hawk.put("page","home");
                    Intent home = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(home);
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_LONG).show();

                    if (status.getSuccess().getData().getCity().equals("")){
                        missingInformation();
                    }

                }
                else{
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_LONG).show();
                }
                loadingAnimation(avi1,false);
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                loadingAnimation(avi1,false);

            }
        });
    }


}
