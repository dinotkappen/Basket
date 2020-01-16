package com.shopping.basket.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Adapter.CartAdapter.CartAdapter;
import com.shopping.basket.BuildConfig;
import com.shopping.basket.Fragment.CartFragment;
import com.shopping.basket.Fragment.ContactUs;
import com.shopping.basket.Fragment.FaqFragment;
import com.shopping.basket.Fragment.FavoritesFragment;
import com.shopping.basket.Fragment.FullImageView;
import com.shopping.basket.Fragment.Home;
import com.shopping.basket.Fragment.NoInternet;
import com.shopping.basket.Fragment.OrderHistory;
import com.shopping.basket.Fragment.PrivacyFragment;
import com.shopping.basket.Fragment.ProductDetailsFragment;
import com.shopping.basket.Fragment.ProductListFragment;
import com.shopping.basket.Fragment.ProfileFragment;
import com.shopping.basket.Fragment.Search;
import com.shopping.basket.Fragment.SettingFragment;
import com.shopping.basket.Model.CartModel.CartListModel.CartList;
import com.shopping.basket.Model.CartModel.CartListModel.CartListModel;
import com.shopping.basket.Model.CartModel.CartListModel.Data;
import com.shopping.basket.Model.CartModel.CartModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.R;
import com.shopping.basket.Util.NetworkChangeReceiver;
import com.shopping.basket.Util.util;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Fragment.ProfileFragment.callLoadProfile;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BottomNavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    static API apiInterface;
    static ImageView profile;
    static Runnable MainRunnable;
    NavigationView navigationView;
    static MainActivity mainActivity;
    static String userTocken,selectedLang;
    private NetworkChangeReceiver receiver;
    static TextView user_name,email,txtCartCound;
    public static BottomNavigationView navigation;
    public static FrameLayout framCart,framCartCount;
    public static ImageView imgSlidMenu,imgBackRight;
    LinearLayout lenLogin,linearBlankClick,linearBlankClick2,linearBottomMenu;
    static Runnable home_fragment;
    Runnable search_fragment;
    Runnable favorite_fragment;
    Runnable profile_fragment;
    Runnable cart_fragment;
    Runnable contact_fragment;
    Runnable history_fragment;
    Runnable faq_fragment;
    Runnable privacy_fragment;
    Runnable setting_fragment;

    static Runnable fragmentNointernet;
    static List<CartList> listCart = new ArrayList<>();
    static TextView txtHeadTitle,txt_log;
    static ImageView img_log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mainActivity = MainActivity.this;
        apiInterface = APIClient.getClient().create(API.class);
        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        selectedLang = Hawk.get("selectedLang", "en");

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        drawer          = findViewById(R.id.drawer_layout);
        lenLogin        = findViewById(R.id.len_login);
        imgSlidMenu     = findViewById(R.id.img_menu);
        imgBackRight    = findViewById(R.id.img_back_right);
        framCart        = findViewById(R.id.fram_cart);
        framCartCount   = findViewById(R.id.fram_cart_count);
        txtCartCound    = findViewById(R.id.txt_cart_count);
        txtHeadTitle    = findViewById(R.id.txt_title);
        img_log         = findViewById(R.id.img_log);
        txt_log         = findViewById(R.id.txt_log);

        navigation = findViewById(R.id.navigation_bottum);
        navigationView = findViewById(R.id.nav_view);
        user_name =  findViewById(R.id.txt_name);
        email =findViewById(R.id.txt_email);
        profile =  findViewById(R.id.img_profile);
        linearBlankClick= findViewById(R.id.linearBlankClick);
        linearBlankClick2= findViewById(R.id.linearBlankClick2);
        linearBottomMenu= findViewById(R.id.linearBottomMenu);

        navigation.setOnNavigationItemSelectedListener(this);

        updateHeader();
        imgBackRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        framCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Hawk.get("logStatus","0").equals("1")) {

                    MainRunnable = cart_fragment;
                    fragment_open();
                }
                else {
                    Intent login = new Intent(getApplication(), LoginActivity.class);
                    startActivity(login);
                }

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Hawk.get("logStatus","0").equals("1")) {
                    navigation.getMenu().getItem(3).setChecked(true);
                    MainRunnable = profile_fragment;
                    fragment_open();
                }
                else {
                    Intent login = new Intent(getApplicationContext() , LoginActivity.class);
                    startActivity(login);
                }
                toggle();

            }
        });

        imgSlidMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggle();

            }
        });

        txt_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Hawk.get("logStatus","0").equals("1")) {
                    Intent login = new Intent(getApplication(), LoginActivity.class);
                    startActivity(login);
                }
                else {
                    Log_out();
                }

            }
        });

        linearBlankClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linearBlankClick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linearBottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        home_fragment = new Runnable() {
            public void run() {
                Fragment fragment = new Home();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        };
        search_fragment = new Runnable() {
            public void run() {
                Fragment fragment = new Search();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        };
        favorite_fragment = new Runnable() {
            public void run() {
                Fragment fragment = new FavoritesFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        };
        profile_fragment = new Runnable() {
            public void run() {
                Fragment fragment = new ProfileFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        };
        cart_fragment = new Runnable() {
            public void run() {

                Fragment fragment = new CartFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        };
        contact_fragment = new Runnable() {
            public void run() {

                Fragment fragment = new ContactUs();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        };
        history_fragment = new Runnable() {
            public void run() {

                Fragment fragment = new OrderHistory();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        };
        faq_fragment = new Runnable() {
            public void run() {

                Fragment fragment = new FaqFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        };
        privacy_fragment = new Runnable() {
            public void run() {

                Fragment fragment = new PrivacyFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        };
        setting_fragment = new Runnable() {
            public void run() {

                Fragment fragment = new SettingFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        };
        fragmentNointernet = new Runnable() {
            public void run() {
                Fragment fragment = new NoInternet();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment).commitAllowingStateLoss();

            }
        };


        if (Hawk.get("page","profile").equals("profile")){
            MainRunnable = profile_fragment;
            fragment_open();
        }
        else {
            MainRunnable = home_fragment;
            fragment_open();
        }
    }

    public static void updateHeader() {

        if (Hawk.get("logStatus","0").equals("1")){
            LoginData userDt = new LoginData();
            userDt = Hawk.get("user_details");
            if (userDt.getName()!=null) {
                user_name.setText(userDt.getName().toString());
                email.setText(userDt.getEmail());
                Log.e("user_token", userDt.getToken());
                if (userDt.getPhoto() != null)
                    Glide.with(mainActivity)
                            .load(util.Imageurl + userDt.getPhoto())
                            .placeholder(R.drawable.user_color)
                            .into(profile);
            }
            txt_log.setText("Log Out");
        }
        else {
            user_name.setText("");
            email.setText("");
            txt_log.setText(R.string.login);
        }

    }

    public static void fragment_open(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainRunnable.run();

            }
        }, 350);
    }

    @Override
    public void onBackPressed() {
        rightBackHide(false);
        Fragment fragment_back = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_contaner);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragment_back instanceof CartFragment){
                getSupportFragmentManager().popBackStack();
                rightBackHide(false);
            }
            else if (fragment_back instanceof ProductListFragment){

                getSupportFragmentManager().popBackStack();
            }
            else if (fragment_back instanceof ProductDetailsFragment){

                getSupportFragmentManager().popBackStack();
            }
            else if (fragment_back instanceof ContactUs){

                getSupportFragmentManager().popBackStack();
            }
            else if (fragment_back instanceof OrderHistory){

                getSupportFragmentManager().popBackStack();
            }
            else if (fragment_back instanceof FavoritesFragment){

                getSupportFragmentManager().popBackStack();
            }
            else if (fragment_back instanceof PrivacyFragment){

                getSupportFragmentManager().popBackStack();
            }
            else if (fragment_back instanceof FaqFragment){

                getSupportFragmentManager().popBackStack();
            } else if (fragment_back instanceof SettingFragment){

                getSupportFragmentManager().popBackStack();
            }else if (fragment_back instanceof Search){

                getSupportFragmentManager().popBackStack();
            }else if (fragment_back instanceof ProfileFragment){
                callLoadProfile.cancel();

                getSupportFragmentManager().popBackStack();
            }else if (fragment_back instanceof FullImageView){

                getSupportFragmentManager().popBackStack();
            }
            else {
                moveTaskToBack(true);
            }
        }
    }

    public static void rightBackHide(boolean status){

        if (status){

            if (Hawk.get("cart_count","0").equals("0")){
                framCart.setVisibility(View.GONE);
                imgBackRight.setVisibility(View.GONE);
            }
            else {
                framCart.setVisibility(View.GONE);
                imgBackRight.setVisibility(View.VISIBLE);
            }


        }
        else {
            if (Hawk.get("logStatus","0").equals("1")) {
                if (Hawk.get("cart_count","0").equals("0")){
                    framCart.setVisibility(View.GONE);
                }
                else
                    framCart.setVisibility(View.VISIBLE);

                imgBackRight.setVisibility(View.GONE);
            }
            else {
                framCart.setVisibility(View.GONE);
                imgBackRight.setVisibility(View.GONE);
            }
        }

    }

    public static void titlHide(String title){

        if (title.equals("null")){
            txtHeadTitle.setVisibility(View.GONE);
            img_log.setVisibility(View.VISIBLE);
        }
        else {
            txtHeadTitle.setText(title);
            txtHeadTitle.setVisibility(View.VISIBLE);
            img_log.setVisibility(View.INVISIBLE);
        }

    }
    public static void double_icon(){
        if (Hawk.get("cart_count","0").equals("0")){
            framCart.setVisibility(View.GONE);
            imgBackRight.setVisibility(View.VISIBLE);
        }
        else {
            if (Hawk.get("cart_count","0").equals("0")){
                framCart.setVisibility(View.GONE);
            }
            else
                framCart.setVisibility(View.VISIBLE);

            imgBackRight.setVisibility(View.VISIBLE);
        }
    }

    public static void backhide(){
//        if (status){
            if (Hawk.get("cart_count","0").equals("0")){
                framCart.setVisibility(View.GONE);
                imgBackRight.setVisibility(View.GONE);
            }
            else {
                framCart.setVisibility(View.VISIBLE);
                imgBackRight.setVisibility(View.GONE);
            }



//        }
//        else {
//            framCart.setVisibility(View.VISIBLE);
//            imgBackRight.setVisibility(View.VISIBLE);
//        }
    }
    public static void backSideManu(){
        imgBackRight.setVisibility(View.VISIBLE);
        framCart.setVisibility(View.GONE);

    }
    public static void backSideManuCart(){
        imgBackRight.setVisibility(View.VISIBLE);
        framCart.setVisibility(View.VISIBLE);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment_back = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_contaner);
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navigationView.getMenu().getItem(0).setChecked(false);
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(2).setChecked(false);
            navigationView.getMenu().getItem(3).setChecked(false);
            navigationView.getMenu().getItem(4).setChecked(false);

            navigation.getMenu().getItem(0).setChecked(true);
            if (fragment_back instanceof Home) {

            }
            else {
                MainRunnable = home_fragment;
                fragment_open();
            }
        }
        else if(id==R.id.nav_cart)
        {

            if (Hawk.get("logStatus","0").equals("1")) {
                if(MainRunnable!=cart_fragment) {
                    MainRunnable = cart_fragment;
                    fragment_open();
                }
            }
            else {
                Intent login = new Intent(getApplicationContext() , LoginActivity.class);
                startActivity(login);
            }
        }
        else if (id == R.id.nav_history) {
            navigation.getMenu().setGroupCheckable(0, false, true);
            if (Hawk.get("logStatus","0").equals("1")) {
                if(MainRunnable!=history_fragment) {
                    MainRunnable = history_fragment;
                }
                fragment_open();
            }
            else {
                Intent login = new Intent(getApplicationContext() , LoginActivity.class);
                startActivity(login);
            }

        }
        else if (id == R.id.nav_fav) {
            navigation.getMenu().getItem(2).setChecked(true);
            if (Hawk.get("logStatus","0").equals("1")) {
                if (fragment_back instanceof FavoritesFragment) {

                }
                else {
                    MainRunnable = favorite_fragment;
                    fragment_open();
                }
            }
            else {
                Intent login = new Intent(getApplicationContext() , LoginActivity.class);
                startActivity(login);
            }


        } else if (id == R.id.nav_setting) {
            navigation.getMenu().setGroupCheckable(0, false, true);

            if (Hawk.get("logStatus","0").equals("1")) {
                if(MainRunnable!=setting_fragment) {
                    MainRunnable = setting_fragment;
                    fragment_open();
                }
            }
            else {
                Intent login = new Intent(getApplicationContext() , LoginActivity.class);
                startActivity(login);
            }
//            Intent setting = new Intent(getApplicationContext(),SettingActivity.class);
//            startActivity(setting);

        } else if (id == R.id.nav_faq) {
            if(MainRunnable!=faq_fragment) {
                MainRunnable = faq_fragment;
                fragment_open();
            }

        } else if (id == R.id.nav_contact) {
            if(MainRunnable!=contact_fragment) {
                MainRunnable = contact_fragment;
                fragment_open();
            }

        }else if (id == R.id.nav_share) {

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Basket");
                String shareMessage= "";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }

        }else if (id == R.id.nav_terms) {

            MainRunnable = privacy_fragment;
            fragment_open();

        }
        else if (id == R.id.navi_bottom_home) {
            if (fragment_back instanceof Home) {

            }
            else {
                MainRunnable = home_fragment;
                fragment_open();
            }
        }
        else if (id == R.id.navi_bottom_search) {
            if (fragment_back instanceof Search) {

            }
            else {
                MainRunnable = search_fragment;
                fragment_open();
            }
        }
        else if (id == R.id.navi_bottom_favorite) {
            if (Hawk.get("logStatus","0").equals("1")) {
                if (fragment_back instanceof FavoritesFragment) {

                }
                else {
                    MainRunnable = favorite_fragment;
                    fragment_open();
                }
            }
            else {
                Intent login = new Intent(getApplicationContext() , LoginActivity.class);
                startActivity(login);
            }

        }
        else if (id == R.id.navi_bottom_profile) {
            if (Hawk.get("logStatus","0").equals("1")) {
                if (fragment_back instanceof ProfileFragment) {

                }
                else {
                    MainRunnable = profile_fragment;
                    fragment_open();
                }
            }
            else {
                Intent login = new Intent(getApplicationContext() , LoginActivity.class);
                startActivity(login);
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toggle() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public static void bottom_clck(){
        navigation.getMenu().getItem(1).setChecked(true);
    }
    public static void bottom_clck_profile(){
        navigation.getMenu().getItem(3).setChecked(true);
    }
    public static void bottom_clckProfile(){
        navigation.getMenu().getItem(3).setChecked(true);
    }

//    public static void getCart() {
//        LoginData profile_dt ;
//        if (Hawk.get("logStatus","0").equals("1")) {
//            profile_dt = Hawk.get("user_details");
//            userTocken = profile_dt.getToken();
//
//
//            Call<CartModel> call = apiInterface.getCart(userTocken);
//            call.enqueue(new Callback<CartModel>() {
//                @Override
//                public void onResponse(Call<CartModel> call, Response<CartModel> response) {
//
//                    CartModel cartList;
//                    Gson gson = new Gson();
//                    JsonElement jsonElement = gson.toJsonTree(response.body());
//                    Log.e("cart_dt", jsonElement.toString());
//                    cartList = gson.fromJson(jsonElement, CartModel.class);
//
//                    if (cartList.getStatus().equals("200")) {
//
//                        if (cartList.getCartItems().size() != 0) {
//                            framCartCount.setVisibility(View.VISIBLE);
//                            txtCartCound.setText(String.valueOf(cartList.getCartItems().size()));
//                        } else {
//                            framCartCount.setVisibility(View.GONE);
//                        }
//
//                    }
//                    else{
//                        Toast.makeText(mainActivity,mainActivity.getString(R.string.sign_out_and),Toast.LENGTH_SHORT).show();
//
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<CartModel> call, Throwable t) {
//
//                }
//            });
//        }
//
//    }


    public static void getCart(){

        try {
            if (Hawk.get("logStatus","0").equals("1")) {
                LoginData userDt = new LoginData();
                userDt = Hawk.get("user_details");
                userTocken = userDt.getToken();
                Log.e("token_cart",userTocken);
                Call<CartListModel> call = apiInterface.getCart(userTocken);
                call.enqueue(new Callback<CartListModel>() {
                    @Override
                    public void onResponse(Call<CartListModel> call, Response<CartListModel> response) {
                        Gson gson = new Gson();
                        JsonElement jsonElement = gson.toJsonTree(response.body());
                        Log.e("response_cart", jsonElement.toString());
                        CartListModel cartList = gson.fromJson(jsonElement, CartListModel.class);
                        if (cartList.getSuccess().getStatus() == 200) {
                             listCart = cartList.getSuccess().getData().getCartList();

                            if (listCart.size() != 0) {
                                framCartCount.setVisibility(View.VISIBLE);
                                framCart.setVisibility(View.VISIBLE);
                                txtCartCound.setText(String.valueOf(listCart.size()));
                                Hawk.put("cart_count",String.valueOf(listCart.size()));
                            } else {
                                framCart.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<CartListModel> call, Throwable t) {

                    }
                });
            }
            else {
                framCart.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Log_out() {
        final Dialog log_out = new Dialog(MainActivity.this);
        log_out.requestWindowFeature(Window.FEATURE_NO_TITLE);
        log_out.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        log_out.setCancelable(true);
        log_out.setContentView(R.layout.log_out);

        TextView txtYes= log_out.findViewById(R.id.txt_yes);
        TextView txtNo= log_out.findViewById(R.id.txt_no);

        txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                log_out.dismiss();

            }
        });
        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("page","home");
                Hawk.put("logStatus","0");
                Hawk.put("user_details", null);
                Hawk.put("cart_count","0");
                Intent login = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(login);

            }
        });
        log_out.show();
    }

    public static void no_internet(boolean status){
        Fragment fragment_back = mainActivity.getSupportFragmentManager()
                .findFragmentById(R.id.fragment_contaner);
        if (!status  ){
//            if (fragment_back instanceof NoInternet) {
//                navigation.getMenu().getItem(0).setCheckable(true);
//
//                navigationView.setCheckedItem(R.id.nav_shop);
//                loadFragment(new Home_Fragment());
//            }
//
            MainRunnable = fragmentNointernet;
            fragment_open();




        }
        else {
            if (fragment_back instanceof NoInternet) {
                navigation.getMenu().getItem(0).setCheckable(true);

                MainRunnable = home_fragment;
                fragment_open();
            }

        }

//
    }

    public static void cartSizeUpdate(int size){
        if (size>0) {
            framCart.setVisibility(View.VISIBLE);
            txtCartCound.setText(""+size);
        }
        else {
            framCart.setVisibility(View.GONE);
            txtCartCound.setText("");
        }
    }

}
