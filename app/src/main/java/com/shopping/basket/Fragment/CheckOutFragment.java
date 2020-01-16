package com.shopping.basket.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Activity.MainActivity;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.R;

import java.io.IOException;
import java.util.List;

public class CheckOutFragment extends MainContanier implements OnMapReadyCallback {

    String paymentMod = "Cash On Delivery";
    ImageView imgBack;
    String userTocken;
    API apiInterface;
    LoginData profile_dt;
    private GoogleMap mMap;
    TextView txtAddaddressNew;
    private TextView txt_address;
    SupportMapFragment mapFragment;
    FrameLayout cardFram,CashFram;
    ImageView imgCardSelect,imgCashSelect;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.check_out_fragment, container, false);

        if (Hawk.get("logStatus","0").equals("1")) {

            profile_dt = Hawk.get("user_details");
            userTocken = profile_dt.getToken();
        }
        Hawk.put("address_page","checkout");
        txt_address      = rootView.findViewById(R.id.txt_address);
        imgCardSelect    = rootView.findViewById(R.id.img_card_select);
        imgCashSelect    = rootView.findViewById(R.id.img_cash_select);
        cardFram         = rootView.findViewById(R.id.fram_card);
        CashFram         = rootView.findViewById(R.id.fram_cash);
      //  txtAddaddressNew = rootView.findViewById(R.id.txt_add_new);

        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MainActivity.rightBackHide(false);

        configureCameraIdle();
        cardFram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPayment(1);
            }


        });
        CashFram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPayment(2);
            }


        });

        txtAddaddressNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        return rootView;

    }
    public void SelectPayment(int type) {
        if (type==1){
            imgCardSelect.setVisibility(View.VISIBLE);
            imgCardSelect.setVisibility(View.GONE);
            paymentMod = "Card";
        }
        else {
            imgCardSelect.setVisibility(View.GONE);
            imgCardSelect.setVisibility(View.VISIBLE);
            paymentMod = "Cash On Delivery";
        }
    }


    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(getActivity());

                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        try {
                            if (!locality.isEmpty() && !country.isEmpty())
                                txt_address.setText(profile_dt.getName()+"\n"+locality + "\n" + country+"\n"+profile_dt.getPhone()+"\n"+profile_dt.getEmail());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);

    }

}
