package com.shopping.basket.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Application;
import com.shopping.basket.Model.AddressListModel.AddressListData;
import com.shopping.basket.Model.DeliveryTimeModel.DeliveryTimeData;
import com.shopping.basket.Model.DeliveryTimeModel.DeliveryTimeModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    API apiInterface;
    TextView txtTool;
    String userTocken;
    Button btConfirm;
    Spinner spinTime;
    LoginData profile_dt;
    private GoogleMap mMap;
    ImageView imgBack, img_loder;
    int REQUEST_CODE_SHIPPING = 101;
    FrameLayout cardFram, CashFram;
    SupportMapFragment mapFragment;
    String paymentMod = "Cash On Delivery";
    ImageView imgCardSelect, imgCashSelect;
    private TextView txt_address;
    LinearLayout linearAddress;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    AddressListData addressdata = new AddressListData();
    List<DeliveryTimeData> listTime = new ArrayList<>();
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.check_out_fragment);
        apiInterface = APIClient.getClient().create(API.class);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(
//                R.layout.check_out_fragment, container, false);

        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();

        if (Hawk.get("logStatus", "0").equals("1")) {

            profile_dt = Hawk.get("user_details");
            userTocken = profile_dt.getToken();
        }

        txt_address = findViewById(R.id.txt_address);
        imgCardSelect = findViewById(R.id.img_card_select);
        imgCashSelect = findViewById(R.id.img_cash_select);
        cardFram = findViewById(R.id.fram_card);
        CashFram = findViewById(R.id.fram_cash);
        btConfirm = findViewById(R.id.bt_confirm);
        imgBack = findViewById(R.id.img_back_right);
//        txtAddaddress = findViewById(R.id.txt_add_new);
//        txtEditAddress = findViewById(R.id.txt_change);
        linearAddress=findViewById(R.id.linearAddress);
        spinTime = findViewById(R.id.spin_time);
        img_loder = findViewById(R.id.img_loder);

        Glide.with(this)
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MainActivity.rightBackHide(false);

        configureCameraIdle();
//        cardFram.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SelectPayment(1);
//            }
//
//
//        });
        SelectPayment(2);
        CashFram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPayment(2);
            }


        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = txt_address.getText().toString();
                if (address != null && !address.isEmpty() && !address.equals("null")) {


                    Hawk.put("Address", address);
                    Hawk.put("payment", paymentMod);
                    if (spinTime.getSelectedItem().equals("Select Time of Delivery")) {
                        Toast.makeText(Application.getContext1(), "Please Select Devivery Time", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent place_order = new Intent(getApplicationContext(), PlaceOrderActivity.class);
                        startActivity(place_order);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please select a address",Toast.LENGTH_SHORT).show();
                }

            }
        });

        linearAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("address_page", "chcke_out");
                Intent i = new Intent("Basket.address.select");
                startActivityForResult(i, REQUEST_CODE_SHIPPING);
            }
        });

//        txtAddaddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Hawk.put("address_page", "chcke_out");
//                Intent i = new Intent("Basket.address.select");
//                startActivityForResult(i, REQUEST_CODE_SHIPPING);
//            }
//        });
//        txtEditAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Hawk.put("address_page", "chcke_out");
//                Intent i = new Intent("Basket.address.select");
//                startActivityForResult(i, REQUEST_CODE_SHIPPING);
//            }
//        });
        deliverTime();
        spinTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("city",listCity.get(position).getCity());
//                if (flageFirstLoading!=0) {
//                txtCity.setText(listCity.get(position).getCity());
//                cityId = listCity.get(position).getId();
//                }
//                flageFirstLoading=1;
                Hawk.put("deliveryTime", listTime.get(position).getTime());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        return rootView;

    }

    public void SelectPayment(int type) {
        if (type == 1) {
            imgCardSelect.setVisibility(View.VISIBLE);
            imgCashSelect.setVisibility(View.GONE);
            paymentMod = "Card On Delivery";
        } else {
            imgCardSelect.setVisibility(View.GONE);
            imgCashSelect.setVisibility(View.VISIBLE);
            paymentMod = "Cash On Delivery";
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
//        createTempFile();
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SHIPPING) {

                addressdata = Hawk.get("latest_address");
                Hawk.put("Address", addressdata);
                txt_address.setText(addressdata.getShippingName() + "\n" + addressdata.getShippingAddress() + "\n" + addressdata.getShippingPhone());
//                txtBillingAddress.setText(addressBilling.getAlias() + "\n" + addressBilling.getAddress1() + "\n" + addressBilling.getPhone());
//                vieBilling.setBackgroundResource(R.drawable.category_fill_round);

            }

        }
    }


    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(CheckOutActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        String city = addressList.get(0).getLocality();
//                        try {
//                            if (!locality.isEmpty() && !country.isEmpty())
////                                txt_address.setText(profile_dt.getName()+"\n"+locality + "\n" + city+"\n"+country+"\n"+profile_dt.getPhone()+"\n"+profile_dt.getEmail());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        Toast.makeText(getApplicationContext(),locality + "  " + country,Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setOnCameraIdleListener(onCameraIdleListener);

        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        enableMyLocationIfPermitted();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {

            mMap.setMyLocationEnabled(true);
            View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 30, 30);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((mMap.getMyLocation().),mMap.getMyLocation().getLongitude()), 16));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(47.6739881, -122.121512);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(redmond));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((47.6739881),47.6739881), 10.0f));
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void deliverTime() {
        img_loder.setVisibility(View.VISIBLE);
        Call<DeliveryTimeModel> call = apiInterface.getTime();
        call.enqueue(new Callback<DeliveryTimeModel>() {
            @Override
            public void onResponse(Call<DeliveryTimeModel> call, Response<DeliveryTimeModel> response) {
                DeliveryTimeModel status = new DeliveryTimeModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                status = gson.fromJson(jsonElement, DeliveryTimeModel.class);
                if (status.getSuccess().getStatus() == 200) {
                    listTime = status.getSuccess().getData();
                    DeliveryTimeData time = new DeliveryTimeData();

                    time.setId(0);
                    time.setTime("Select Time of Delivery");
                    listTime.add(0, time);
                    ArrayList timeList = new ArrayList();

                    for (int i = 0; i < listTime.size(); i++) {

                        timeList.add(listTime.get(i).getTime());
//                        zip_code.add(listCity.get(i).getZoneCode());

                    }

                    ArrayAdapter aa = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, timeList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinTime.setAdapter(aa);
                    img_loder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<DeliveryTimeModel> call, Throwable t) {
                img_loder.setVisibility(View.GONE);
            }
        });


    }
}
