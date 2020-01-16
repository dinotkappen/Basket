package com.shopping.basket.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Activity.AddressListActivity;
import com.shopping.basket.Activity.AddressNewActivity;
import com.shopping.basket.Activity.CahngeLanguage;
import com.shopping.basket.Activity.MainActivity;
import com.shopping.basket.Activity.ResetPassword;
import com.shopping.basket.R;

import static com.shopping.basket.Activity.MainActivity.bottom_clck_profile;
import static com.shopping.basket.Activity.MainActivity.titlHide;

public class SettingFragment extends MainContanier {

    TextView txtHead;
    ImageView backBt;
    API apiInterface;
    LinearLayout lenRsetPassword,lenChangeLanguage,lenProfile,lenAddress,lenPasswordCahnge,lenInfo,lenSignout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.setting_activity, container, false);

        MainActivity.double_icon();
        titlHide(getResources().getString(R.string.action_settings));
        backBt              = rootView.findViewById(R.id.img_back_right);
        txtHead             = rootView.findViewById(R.id.txt_head);
        lenRsetPassword     = rootView.findViewById(R.id.len_password);
        lenChangeLanguage   = rootView.findViewById(R.id.len_language);
        lenProfile          = rootView.findViewById(R.id.len_info);
        lenAddress          = rootView.findViewById(R.id.len_address);
        lenPasswordCahnge   = rootView.findViewById(R.id.len_password);
        lenInfo             = rootView.findViewById(R.id.len_info);
        lenSignout          = rootView.findViewById(R.id.len_sign_out);


//      backBt.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              finish();
//          }
//      });

        lenAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("address_page","list");
                Intent address = new Intent(getActivity(), AddressListActivity.class);
                startActivity(address);


            }
        });

      lenRsetPassword.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent restPass = new Intent(getContext(),ResetPassword.class);
              startActivity(restPass);
          }
      });
        lenChangeLanguage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent restPass = new Intent(getContext(),CahngeLanguage.class);
              startActivity(restPass);
          }
      });
        lenPasswordCahnge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changePassword = new Intent(getContext(),ResetPassword.class);
                startActivity(changePassword);
            }
        });
        lenInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_clck_profile();
                Fragment fragment = new ProfileFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();
            }
        });
        lenSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final Dialog log_out = new Dialog(getActivity());
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
                            Intent login = new Intent(getActivity(), MainActivity.class);
                            startActivity(login);

                        }
                    });
                    log_out.show();
            }
        });


        return rootView;
    }
}
