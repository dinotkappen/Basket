package com.shopping.basket.Adapter.ProductAdapter;

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
import com.shopping.basket.Fragment.ProductDetailsFragment;
import com.shopping.basket.Fragment.ProductListFragment;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.RelatedProduct.Relate_proUct;
import com.shopping.basket.Model.WishListModel.WishListAddModel;
import com.shopping.basket.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RlatedProductListAdapter extends RecyclerView.Adapter<RlatedProductListAdapter.MyViewHolder> {

    LoginData profile_dt;
    API apiInterface;
    String userTocken;
    private FragmentActivity mContext;
    private List<Relate_proUct> categoryList;
    ArrayList list_id = new ArrayList();

public class MyViewHolder extends RecyclerView.ViewHolder {
    LinearLayout lenOutOfStock,lenClick,len_whish_clcik;
    public TextView title,price;
    ImageView imgCategory,imgFavourite;

    public MyViewHolder(View view) {
        super(view);

        title           = view.findViewById(R.id.txt_title);
        price           = view.findViewById(R.id.txt_price);
        imgCategory     = view.findViewById(R.id.img_product);
        imgFavourite    = view.findViewById(R.id.img_favourite);
        lenOutOfStock   = view.findViewById(R.id.len_out_of_stock);
        lenClick        = view.findViewById(R.id.len_click);
        len_whish_clcik = view.findViewById(R.id.len_fav);

    }
}

    public RlatedProductListAdapter(FragmentActivity mContext, List<Relate_proUct> categoryList) {
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
                .inflate(R.layout.product_list_item, parent, false);



        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Relate_proUct itemMainModel = categoryList.get(position);

        holder.title.setText(itemMainModel.getName());
        holder.price.setText(itemMainModel.getPrice()+" Qar");
//        if (position==0)
//        holder.lenOutOfStock.setVisibility(View.VISIBLE);
//        else
            holder.lenOutOfStock.setVisibility(View.GONE);

//        holder.imgCategory.setImageDrawable(mContext.getResources().getDrawable(itemMainModel.drawble));
        Glide.with(mContext).load(itemMainModel.getCover()).into(holder.imgCategory);
//        Log.e("image_url",Config.url+itemMainModel.getPhoto());
        Hawk.get("wish_list",null);
        list_id = Hawk.get("wish_list",null);
        if (Hawk.get("logStatus","0").equals("1")) {
            if (list_id!=null) {
                if (list_id.contains(itemMainModel.getI())) {
                    holder.imgFavourite.setBackgroundResource(R.drawable.favorite_fill);
                } else {
                    holder.imgFavourite.setBackgroundResource(R.drawable.favorite_black);
                }
            }
            else {
                holder.imgFavourite.setBackgroundResource(R.drawable.favorite_black);
            }
        }
        else {
            holder.imgFavourite.setBackgroundResource(R.drawable.favorite_black);
        }

        holder.len_whish_clcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Hawk.get("logStatus","0").equals("1")) {
//                    addWhisList(itemMainModel.getI(),holder);
                }
                else {
                    Intent login = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(login);
                }



            }
        });

        holder.lenClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int visibleFlage=0;
//                if (position==0){
//                    visibleFlage=0;
//                }
//                else {
//                    visibleFlage = 1;
//                }

                Fragment fragment = ProductDetailsFragment.newInstance(itemMainModel.getI(),itemMainModel.getName());
                FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();

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

//    private void addWhisList(int product_id, final MyViewHolder holder1) {
//        final WishListAddModel addStatus = new WishListAddModel();
//        Call<WishListAddModel> call = apiInterface.addWishList(userTocken,product_id);
//
//        call.enqueue(new Callback<WishListAddModel>() {
//            @Override
//            public void onResponse(Call<WishListAddModel> call, Response<WishListAddModel> response) {
//
//                WishListAddModel addStatus; //;;= response.body();
////                SmartPhoneModel status = new SmartPhoneModel();
////                Gson gson = new Gson();
////                JsonElement jsonElement = gson.toJsonTree(response.body());
////                Log.e("error_",jsonElement.toString());
////                addStatus = gson.fromJson(jsonElement, WishListAddModel.class);
////
////                if (addStatus.getSuccess()==1){
////                    Toast.makeText(mContext,addStatus.getMag(),Toast.LENGTH_SHORT).show();
////                    ProductListFragment.getwishList();
////
//////                    holder1.imgWishlist.setBackgroundResource(R.drawable.wish_list_select);
////                }
////                else {
////                    Toast.makeText(mContext,addStatus.getMag(),Toast.LENGTH_SHORT).show();
////                }
//                notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<WishListAddModel> call, Throwable t) {
//                Toast.makeText(mContext,addStatus.getMag(),Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}
