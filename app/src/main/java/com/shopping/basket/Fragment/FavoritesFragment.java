package com.shopping.basket.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Adapter.FavoritesAdapter.FavoritesAdapter;
import com.shopping.basket.Adapter.ProductAdapter.ProductListAdapter;
import com.shopping.basket.Application;
import com.shopping.basket.CustomView.SwipeToDeleteCallback;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.ProductListModel.ProductData;
import com.shopping.basket.Model.ProductListModel.ProductListModel;
import com.shopping.basket.Model.ProductListModel.ProductListSuccess;
import com.shopping.basket.Model.WishListModel.AddWishList.WishListAddModel;
import com.shopping.basket.Model.WishListModel.WishListModel;
import com.shopping.basket.Model.WishListModel.Wishlist;
import com.shopping.basket.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.MainActivity.backSideManu;
import static com.shopping.basket.Activity.MainActivity.backhide;
import static com.shopping.basket.Activity.MainActivity.rightBackHide;
import static com.shopping.basket.Activity.MainActivity.titlHide;
import static com.shopping.basket.Util.util.loadingFbAniamtion;

public class FavoritesFragment extends MainContanier {

    API apiInterface;
    ImageView img_loder;
    LinearLayout lenEmpty;
    RecyclerView recyCategory;
    String userToken,selectedLang;
    FavoritesAdapter adapterFavorite;
    List<ProductData> Product_list = new ArrayList<>();
    private ShimmerFrameLayout mShimmerViewContainer;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.favorites, container, false);
        LoginData userDt ;
        userDt = Hawk.get("user_details");
        userToken = userDt.getToken();
        selectedLang= Hawk.get("selectedLang","en");

//        backhide();
        backSideManu();
        titlHide(getResources().getString(R.string.menu_fav));

        recyCategory    = rootView.findViewById(R.id.recy_favorites);
        mShimmerViewContainer   = rootView.findViewById(R.id.shimmer_view_container);
        lenEmpty                = rootView.findViewById(R.id.len_wish_list);
        img_loder               = rootView.findViewById(R.id.img_loder);

        Glide.with(getActivity())
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);
       // rightBackHide(false);
        recyCategory.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapterFavorite = new FavoritesAdapter(getActivity(), Product_list,FavoritesFragment.this);
        recyCategory.setAdapter(adapterFavorite);


//                loadingFbAniamtion(mShimmerViewContainer,true);
        getProductList();
        enableSwipeToDeleteAndUndo();

        return rootView;

    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                loadingFbAniamtion(mShimmerViewContainer,true);
                addWhisList(Product_list.get(position).getId(),position);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyCategory);
    }
    public void addWhisList(int id, final int position) {
        loadingFbAniamtion(mShimmerViewContainer,true);
        Call<WishListAddModel> call = apiInterface.addWishList(id,userToken);

        call.enqueue(new Callback<com.shopping.basket.Model.WishListModel.AddWishList.WishListAddModel>() {
            @Override
            public void onResponse(Call<WishListAddModel> call, Response<com.shopping.basket.Model.WishListModel.AddWishList.WishListAddModel> response) {

                WishListAddModel addStatus; //;;= response.body();
                WishListAddModel status = new WishListAddModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("error_",jsonElement.toString());
                addStatus = gson.fromJson(jsonElement, com.shopping.basket.Model.WishListModel.AddWishList.WishListAddModel.class);

                if (addStatus.getSuccess().getStatus()==200){
                    Toast.makeText(Application.getContext1(),addStatus.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
//                    ProductListFragment.getwishList();
                    Product_list.remove(position);
                    adapterFavorite = new FavoritesAdapter(getActivity(), Product_list,FavoritesFragment.this);
                    recyCategory.setAdapter(adapterFavorite);
                    adapterFavorite.notifyDataSetChanged();
                    if (Product_list.size()==0){
                        lenEmpty.setVisibility(View.VISIBLE);
                    }
                    else {
                        lenEmpty.setVisibility(View.GONE);
                    }

//                    holder1.imgWishlist.setBackgroundResource(R.drawable.wish_list_select);
                }
                else {
                    Toast.makeText(Application.getContext1(),addStatus.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
//                notifyDataSetChanged();
                loadingFbAniamtion(mShimmerViewContainer,false);
            }

            @Override
            public void onFailure(Call<WishListAddModel> call, Throwable t) {
//                Toast.makeText(mContext,addStatus.getMag(),Toast.LENGTH_SHORT).show();
                loadingFbAniamtion(mShimmerViewContainer,false);
            }
        });

    }


//    private void getwishList() {
//        Call<WishListModel> call = apiInterface.getWishList(userToken,selectedLang);
//        call.enqueue(new Callback<WishListModel>() {
//            @Override
//            public void onResponse(Call<WishListModel> call, Response<WishListModel> response) {
//
//                WishListModel wishList ;
//                Gson gson = new Gson();
//                JsonElement jsonElement = gson.toJsonTree(response.body());
//                Log.e("wishList",jsonElement.toString());
//                wishList = gson.fromJson(jsonElement, WishListModel.class);
//                listWishList.clear();
//                listWishList = wishList.getWishlist();
//                ArrayList ids = new ArrayList();
//                for (int i = 0; i <listWishList.size() ; i++) {
//
//                    ids.add(listWishList.get(i).getId());
//
//                }
//                Hawk.put("wish_list",ids);
//                Log.e("listWishList",String.valueOf(listWishList.size()));
//
//                adapterFavorite = new FavoritesAdapter(getActivity(), listWishList);
//                recyCategory.setAdapter(adapterFavorite);
//                adapterFavorite.notifyDataSetChanged();
//                loadingFbAniamtion(mShimmerViewContainer,false);
//
//                if (listWishList.size()==0){
//                    lenEmpty.setVisibility(View.VISIBLE);
//                }
//                else {
//                    lenEmpty.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WishListModel> call, Throwable t) {
//                loadingFbAniamtion(mShimmerViewContainer,false);
//            }
//        });
//
//    }

    public void getProductList(){
//        loadingFbAniamtion(mShimmerViewContainer,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<ProductListModel> call = apiInterface.getWishList(userToken);
        call.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response) {
                ProductListModel status ;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("category_pkb",jsonElement.toString());
                status = gson.fromJson(jsonElement, ProductListModel.class);

                ProductListSuccess Category = status.getSuccess();

                Product_list = Category.getData();

                adapterFavorite = new FavoritesAdapter(getActivity(), Product_list,FavoritesFragment.this);
                recyCategory.setAdapter(adapterFavorite);
                adapterFavorite.notifyDataSetChanged();

                if (Product_list.size()==0){
                    lenEmpty.setVisibility(View.VISIBLE);
                }
                else {
                    lenEmpty.setVisibility(View.GONE);
                }
//                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductListModel> call, Throwable t) {
                img_loder.setVisibility(View.GONE);
                Log.e("error",t.getMessage());

            }
        });
    }

//    private void addWhisList(int product_id) {
//
//        Call<WishListAddModel> call = apiInterface.addWishList(userToken,product_id);
//
//        call.enqueue(new Callback<WishListAddModel>() {
//            @Override
//            public void onResponse(Call<WishListAddModel> call, Response<WishListAddModel> response) {
//
//                WishListAddModel addStatus = response.body();
//
//                if (addStatus.getSuccess()==1){
////                    Toast.makeText(getContext(),getResources().getString(R.string.success),Toast.LENGTH_SHORT).show();
////                    getwishList();
//                }
//                else {
//                    Toast.makeText(getContext(),addStatus.getMag(),Toast.LENGTH_SHORT).show();
//                }
//
//                loadingFbAniamtion(mShimmerViewContainer,false);
//            }
//
//            @Override
//            public void onFailure(Call<WishListAddModel> call, Throwable t) {
//                Toast.makeText(getContext(),getResources().getString(R.string.try_lagain),Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

}
