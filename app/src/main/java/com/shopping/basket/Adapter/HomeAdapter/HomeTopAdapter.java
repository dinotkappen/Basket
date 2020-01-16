package com.shopping.basket.Adapter.HomeAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Activity.LoginActivity;
import com.shopping.basket.Adapter.ProductAdapter.ProductListAdapter;
import com.shopping.basket.Application;
import com.shopping.basket.Fragment.Home;
import com.shopping.basket.Fragment.ProductDetailsFragment;
import com.shopping.basket.Fragment.ProductListFragment;
import com.shopping.basket.Model.CartModel.CardAddModel.AddCartModel;
import com.shopping.basket.Model.CategoryModel.CategoryDatum;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.ProductListModel.ProductData;
import com.shopping.basket.Model.WishListModel.AddWishList.WishListAddModel;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.MainActivity.getCart;
import static com.shopping.basket.Fragment.ProductListFragment.loading;

public class HomeTopAdapter extends RecyclerView.Adapter<HomeTopAdapter.MyViewHolder> {

    API apiInterface;
    String userTocken;
    LoginData profile_dt;
    private FragmentActivity mContext;
    private List<ProductData> categoryList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    LinearLayout len_whish_clcik,lenOutOfStock;
    public TextView title,price;
    ImageView imgCategory,imgFavourite,imgCartAdd;

    public MyViewHolder(View view) {
        super(view);
        title           = view.findViewById(R.id.txt_title);
        price           = view.findViewById(R.id.txt_price);
        imgCategory     = view.findViewById(R.id.img_product);
        imgFavourite    = view.findViewById(R.id.img_favourite);
        len_whish_clcik    = view.findViewById(R.id.len_fav);
        imgCartAdd    = view.findViewById(R.id.img_cart);
        lenOutOfStock    = view.findViewById(R.id.len_out_of_stock);

    }
}

    public HomeTopAdapter(FragmentActivity mContext, List<ProductData> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.apiInterface = APIClient.getClient().create(API.class);
        if (Hawk.get("logStatus","0").equals("1")) {
            this.profile_dt = Hawk.get("user_details");
            this.userTocken = profile_dt.getToken();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_top_product, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductData itemMainModel = categoryList.get(position);
        holder.title.setText(itemMainModel.getName());
        holder.price.setText(itemMainModel.getPrice()+" Qar");

        if (itemMainModel.getStock()==0)
            holder.lenOutOfStock.setVisibility(View.VISIBLE);
        else
            holder.lenOutOfStock.setVisibility(View.GONE);

        if (Hawk.get("logStatus","0").equals("1")) {
                if (itemMainModel.getWishlists()==1) {
                    holder.imgFavourite.setBackgroundResource(R.drawable.favorite_fill);
                } else {
                    holder.imgFavourite.setBackgroundResource(R.drawable.favorite_black);
                }

        }
        else {
            holder.imgFavourite.setBackgroundResource(R.drawable.favorite_black);
        }
        Glide.with(mContext).load(util.Imageurl+itemMainModel.getPhoto()).into(holder.imgCategory);

        holder.len_whish_clcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Hawk.get("logStatus","0").equals("1")) {
                    addWhisList(itemMainModel,position);
                }
                else {
                    Intent login = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(login);
                }
            }
        });

        holder.imgCartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Hawk.get("logStatus","0").equals("1")) {
                    if (itemMainModel.getStock()==0){
                        Toast.makeText(Application.getContext(),mContext.getResources().getString(R.string.out_of_stock),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        addToCart(itemMainModel.getId());
                    }

                }
                else {
                    Intent login = new Intent(mContext,LoginActivity.class);
                    mContext.startActivity(login);
                }
            }
        });
        holder.imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = ProductDetailsFragment.newInstance(itemMainModel.getId(),itemMainModel.getName());
                FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

            }
        });


    }

    public void addToCart(int product_id){
        Home.Loading(true);
        Call<AddCartModel> call = apiInterface.addToCart(product_id,1,userTocken);

        call.enqueue(new Callback<AddCartModel>() {
            @Override
            public void onResponse(Call<AddCartModel> call, Response<AddCartModel> response) {

                AddCartModel status;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("jsonElement",jsonElement.toString());
                status = gson.fromJson(jsonElement, AddCartModel.class);
                if (status.getSuccess().getStatus()==200){
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                    getCart();
                }
                else {
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
                Home.Loading(false);

            }

            @Override
            public void onFailure(Call<AddCartModel> call, Throwable t) {

                Home.Loading(false);

            }
        });
    }


    @Override
    public int getItemCount() {
        if (categoryList != null)
            return categoryList.size();
        else
            return 0;
    }

    private void addWhisList(final ProductData itemMainModel,  final int position) {
        Home.Loading(true);
        Call<WishListAddModel> call = apiInterface.addWishList(itemMainModel.getId(),userTocken);

        call.enqueue(new Callback<WishListAddModel>() {
            @Override
            public void onResponse(Call<WishListAddModel> call, Response<WishListAddModel> response) {

                WishListAddModel addStatus; //;;= response.body();
                WishListAddModel status = new WishListAddModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("error_",jsonElement.toString());
                addStatus = gson.fromJson(jsonElement, WishListAddModel.class);

                if (addStatus.getSuccess().getStatus()==200){
                    Toast.makeText(Application.getContext1(),addStatus.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                    if (itemMainModel.getWishlists()==1)
                        itemMainModel.setWishlists(0);
                    else
                        itemMainModel.setWishlists(1);
                    Home.wishlistUpdate(itemMainModel,position);
                    notifyDataSetChanged();
                }
                else {
                    Toast.makeText(Application.getContext1(),addStatus.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
                Home.Loading(false);

            }

            @Override
            public void onFailure(Call<WishListAddModel> call, Throwable t) {
//                Toast.makeText(mContext,addStatus.getMag(),Toast.LENGTH_SHORT).show();
                Home.Loading(false);
            }
        });

    }
}
