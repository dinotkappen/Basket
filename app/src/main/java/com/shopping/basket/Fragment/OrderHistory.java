package com.shopping.basket.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.shopping.basket.Activity.MainActivity;
import com.shopping.basket.Adapter.FavoritesAdapter.FavoritesAdapter;
import com.shopping.basket.Adapter.OrderHistoryAdapter.OrderHistoryAdapter;
import com.shopping.basket.Application;
import com.shopping.basket.Model.CartModel.CartListModel.CartListModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.OrderHistoryModel.OrderHistoryData;
import com.shopping.basket.Model.OrderHistoryModel.OrderHistoryModel;
import com.shopping.basket.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.MainActivity.backSideManu;
import static com.shopping.basket.Activity.MainActivity.rightBackHide;
import static com.shopping.basket.Activity.MainActivity.titlHide;
import static com.shopping.basket.Util.util.loadingFbAniamtion;

public class OrderHistory extends MainContanier {

    String userTocken;
    ImageView img_loder;
    LoginData profile_dt;
    LinearLayout lenEmpty;
    static API apiInterface;
    RecyclerView recyHistory;
    OrderHistoryAdapter adapterHistory;
    private static ShimmerFrameLayout mShimmerViewContainer;
    List<OrderHistoryData> listCategory = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.order_summery, container, false);
        if (Hawk.get("logStatus","0").equals("1")) {

            profile_dt = Hawk.get("user_details");
            userTocken = profile_dt.getToken();
            Log.v("userTocken",userTocken);
        }
        //rightBackHide(true);
        backSideManu();
        recyHistory    = rootView.findViewById(R.id.recy_history);
        mShimmerViewContainer   = rootView.findViewById(R.id.shimmer_view_container);
        lenEmpty   = rootView.findViewById(R.id.len_empty);
        img_loder               = rootView.findViewById(R.id.img_loder);
        titlHide(getResources().getString(R.string.menu_history));

        Glide.with(getActivity())
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);


//        listCategory.add(new OrderHistoryModel(R.drawable.product1,"Furniture"));
//        listCategory.add(new OrderHistoryModel(R.drawable.product2,"Kitchen"));
//        listCategory.add(new OrderHistoryModel(R.drawable.product3,"Outdoor"));
//        listCategory.add(new OrderHistoryModel(R.drawable.product4,"Pets"));
//        listCategory.add(new OrderHistoryModel(R.drawable.product5,"Kids"));
//        listCategory.add(new OrderHistoryModel(R.drawable.product6,"Bath"));

        recyHistory.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapterHistory = new OrderHistoryAdapter(getActivity(), listCategory);
        recyHistory.setAdapter(adapterHistory);
        orderHistory();
        return rootView;

    }

    public void orderHistory(){
//        loadingFbAniamtion(mShimmerViewContainer,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<OrderHistoryModel> call = apiInterface.getOrderHistory(userTocken);
        call.enqueue(new Callback<OrderHistoryModel>() {
            @Override
            public void onResponse(Call<OrderHistoryModel> call, Response<OrderHistoryModel> response) {
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("response",jsonElement.toString());
                OrderHistoryModel listHistory = gson.fromJson(jsonElement, OrderHistoryModel.class);
                if (listHistory.getSuccess().getStatus()==200){

                    listCategory = listHistory.getSuccess().getData();
                    adapterHistory = new OrderHistoryAdapter(getActivity(), listCategory);
                    recyHistory.setAdapter(adapterHistory);
                    if (listCategory.size()==0){
                        lenEmpty.setVisibility(View.VISIBLE);
                    }else {
                        lenEmpty.setVisibility(View.GONE);
                    }

                }
                else {
                    Toast.makeText(Application.getContext1(),listHistory.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
//                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<OrderHistoryModel> call, Throwable t) {
//                loadingFbAniamtion(mShimmerViewContainer,true);
                img_loder.setVisibility(View.GONE);
            }
        });
    }


}
