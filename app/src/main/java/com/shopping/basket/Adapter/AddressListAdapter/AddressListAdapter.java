package com.shopping.basket.Adapter.AddressListAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Activity.AddressEditActivity;
import com.shopping.basket.Activity.AddressListActivity;
import com.shopping.basket.Model.AddressListModel.AddressListData;
import com.shopping.basket.Model.CartUpdateModel.CartUpdateModel;
import com.shopping.basket.Model.CityListModel.CityData;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.R;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.AddressListActivity.loading;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {

    API apiInterface;
    String userTocken,city;
    LoginData profile_dt;
    private Context mContext;
    List<CityData> listCity;
    private List<AddressListData> listPhone;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtAddress,imgEdit,imgDelet;

        public MyViewHolder(View view) {
            super(view);
            txtAddress           = view.findViewById(R.id.txt_address);
            imgEdit      = view.findViewById(R.id.img_edit);
            imgDelet      = view.findViewById(R.id.img_delet);



        }
    }

    public AddressListAdapter(Context mContext, List<AddressListData> listPhone,List<CityData> listCity) {
        this.mContext = mContext;
        this.listPhone = listPhone;
        this.listCity = listCity;
        this.apiInterface = APIClient.getClient().create(API.class);
        this.profile_dt =  Hawk.get("user_details");
        this.userTocken = profile_dt.getToken();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_list_item, parent, false);



        return new MyViewHolder(itemView);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final AddressListData itemMainModel = listPhone.get(position);
//        }

        for (int i = 0; i <listCity.size() ; i++) {
            if (listCity.get(i).getId()==itemMainModel.getShippingCity()){
                city = listCity.get(i).getCity();
            }
        }
        holder.txtAddress.setText(itemMainModel.getShippingName()+"\n"+itemMainModel.getShippingEmail()+"\n"+itemMainModel.getShippingPhone()+"\n"+itemMainModel.getShippingAddress()+"\n"+city);


        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("edit_address", itemMainModel);
                Intent addressEdit = new Intent(mContext, AddressEditActivity.class);
                mContext.startActivity(addressEdit);

            }
        });
        holder.txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("latest_address", itemMainModel);
                AddressListActivity.back();

            }
        });
        holder.imgDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCart(itemMainModel.getId());
            }
        });

    }


    @Override
    public int getItemCount() {
        if (listPhone != null)
            return listPhone.size();
        else
            return 0;
    }

    public void updateCart(int cart_id){
        loading(true);
        Call<CartUpdateModel> call = apiInterface.deletAddress(cart_id,userTocken);
        call.enqueue(new Callback<CartUpdateModel>() {
            @Override
            public void onResponse(Call<CartUpdateModel> call, Response<CartUpdateModel> response) {

                CartUpdateModel updateStatus;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("cardUpdate",jsonElement.toString());
                updateStatus = gson.fromJson(jsonElement, CartUpdateModel.class);

                if (updateStatus.getSuccess().getStatus()==200){
                    AddressListActivity.listAddress();
                }
                loading(false);
            }

            @Override
            public void onFailure(Call<CartUpdateModel> call, Throwable t) {
                loading(false);
            }
        });

    }

}
