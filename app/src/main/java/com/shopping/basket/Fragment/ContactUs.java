package com.shopping.basket.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Application;
import com.shopping.basket.Model.CartUpdateModel.CartUpdateModel;
import com.shopping.basket.Model.ContactUsModel.ContactUsModel;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.MainActivity.backSideManu;
import static com.shopping.basket.Activity.MainActivity.rightBackHide;
import static com.shopping.basket.Activity.MainActivity.titlHide;
import static com.shopping.basket.Util.util.loadingAnimation;

public class ContactUs extends MainContanier {

    Button submit;
    ImageView img_loder;
    int flagValidation = 0;
    static API apiInterface;
    EditText editNmae,editEmail,editMessage;
    private static AVLoadingIndicatorView avi;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.contact_us, container, false);

        editNmae    = rootView.findViewById(R.id.edit_name);
        editEmail   = rootView.findViewById(R.id.edit_email);
        editMessage = rootView.findViewById(R.id.edit_message);
        submit      = rootView.findViewById(R.id.semd_bt);
        avi         =  rootView.findViewById(R.id.avi);
        img_loder   = rootView.findViewById(R.id.img_loder);

        titlHide(getResources().getString(R.string.menu_contact));
        backSideManu();

        Glide.with(getActivity())
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNmae.getText().toString().equals("")){
                    editNmae.setError(getString(R.string.vaild_name));
                    flagValidation = 1;
                }
               else if (!util.isValidEmail(editEmail.getText().toString().trim())){
                    editEmail.setError(getString(R.string.valid_email));
                    flagValidation = 1;
                }
               else if (editMessage.getText().toString().equals("")){

                    editMessage.setError(getString(R.string.enter_message));

                }
               else {
                    ContactUs();
                }
            }
        });

     //   rightBackHide(true);
        img_loder.setVisibility(View.GONE);

        return rootView;

    }




    public void ContactUs(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<ContactUsModel> call = apiInterface.contactUs(editNmae.getText().toString(),editEmail.getText().toString().trim(),editMessage.getText().toString());
        call.enqueue(new Callback<ContactUsModel>() {
            @Override
            public void onResponse(Call<ContactUsModel> call, Response<ContactUsModel> response) {

                ContactUsModel updateStatus;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("cardUpdate",jsonElement.toString());
                updateStatus = gson.fromJson(jsonElement, ContactUsModel.class);

                if (updateStatus.getSuccess().getStatus()==200){
                    Toast.makeText(Application.getContext1(),updateStatus.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                    editNmae.setText("");
                    editEmail.setText("");
                    editMessage.setText("");
                }
                else {
                    Toast.makeText(Application.getContext1(),updateStatus.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ContactUsModel> call, Throwable t) {
//                Log.e("BagResponse", call.toString());
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }
        });
    }



}
