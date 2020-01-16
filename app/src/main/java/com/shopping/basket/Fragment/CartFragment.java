package com.shopping.basket.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Activity.CheckOutActivity;
import com.shopping.basket.Adapter.CartAdapter.CartAdapter;
import com.shopping.basket.Application;
import com.shopping.basket.CustomView.SwipeToDeleteCallback;
import com.shopping.basket.Model.CartModel.CartDeletModel;
import com.shopping.basket.Model.CartModel.CartItem;
import com.shopping.basket.Model.CartModel.CartItemsModel;
import com.shopping.basket.Model.CartModel.CartListModel.CartList;
import com.shopping.basket.Model.CartModel.CartListModel.CartListModel;
import com.shopping.basket.Model.CartModel.CartListModel.CartListSuccess;
import com.shopping.basket.Model.CartModel.CartModel;
import com.shopping.basket.Model.CartUpdateModel.CartUpdateModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.MainActivity.backSideManu;
import static com.shopping.basket.Activity.MainActivity.backSideManuCart;
import static com.shopping.basket.Activity.MainActivity.cartSizeUpdate;
import static com.shopping.basket.Activity.MainActivity.rightBackHide;
import static com.shopping.basket.Activity.MainActivity.titlHide;

public class CartFragment extends MainContanier {

    Button btcheckOut;
    static ImageView img_loder;
    LoginData profile_dt;
    static API apiInterface;
    static String userTocken;
    static String selectedLang;
    static CartAdapter adapterCart;
    static LinearLayout lenCartEmpty;
    static RecyclerView recyclerCart;
    ArrayList list_id = new ArrayList();
    public static FragmentActivity activity;
    static List<CartList> listCart = new ArrayList<>();
    private static ShimmerFrameLayout mShimmerViewContainer;
    public static CartListModel cartList = new CartListModel();
    static List<CartItemsModel> cartListAdd = new ArrayList<>();
    static TextView txtNetTotal,txtDiscount,txtMrp,txtShippingAmount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.cart, container, false);
        selectedLang= Hawk.get("selectedLang","en");
        if (Hawk.get("logStatus","0").equals("1")) {

            profile_dt = Hawk.get("user_details");
            userTocken = profile_dt.getToken();
        }
       // rightBackHide(true);
        titlHide(getResources().getString(R.string.your_BAg));
        backSideManuCart();

        recyclerCart            = rootView.findViewById(R.id.recy_favorites);
        btcheckOut              = rootView.findViewById(R.id.bt_update);
        txtNetTotal             = rootView.findViewById(R.id.txt_net_total1);
        txtMrp                  = rootView.findViewById(R.id.txt_net_total);
        lenCartEmpty            = rootView.findViewById(R.id.len_empty);
        mShimmerViewContainer   = rootView.findViewById(R.id.shimmer_view_container);
        txtDiscount             = rootView.findViewById(R.id.txt_discount);
        txtShippingAmount       = rootView.findViewById(R.id.txt_shipping);
        img_loder               = rootView.findViewById(R.id.img_loder);

        Glide.with(getActivity())
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);

        recyclerCart.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapterCart = new CartAdapter(getActivity(), listCart);
        recyclerCart.setAdapter(adapterCart);

        btcheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("cart_details",listCart);
                if (cartList.getSuccess().getData().getPriceDetails().getTotalMrp()>0) {
                    Intent checkout = new Intent(getActivity(), CheckOutActivity.class);
                    startActivity(checkout);
                }
                else {
                    Toast.makeText(Application.getContext1(),getString(R.string.cart_empty),Toast.LENGTH_SHORT).show();
                }
            }
        });
        enableSwipeToDeleteAndUndo();
        getCart();
        return rootView;

    }


    public static void getCart(){
        try {
            Call<CartListModel> call = apiInterface.getCart(userTocken);
            call.enqueue(new Callback<CartListModel>() {
                @Override
                public void onResponse(Call<CartListModel> call, Response<CartListModel> response) {
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    Log.e("response_cart", jsonElement.toString());
                    cartList = gson.fromJson(jsonElement, CartListModel.class);
                    if (cartList.getSuccess().getStatus() == 200) {
                        listCart = cartList.getSuccess().getData().getCartList();
                        adapterCart = new CartAdapter(activity, listCart);
                        recyclerCart.setAdapter(adapterCart);
                        adapterCart.notifyDataSetChanged();
    //                    txtSubTotal.setText(String.valueOf(cartList.getSubtotal()));
                        txtNetTotal.setText(String.valueOf(cartList.getSuccess().getData().getPriceDetails().getTotalMrp()) + " QAR");
                        txtDiscount.setText(String.valueOf(cartList.getSuccess().getData().getPriceDetails().getDiscount()) + " Qar");
                        txtMrp.setText(String.valueOf(cartList.getSuccess().getData().getPriceDetails().getTotal()) + " Qar");
                        txtShippingAmount.setText(String.valueOf(cartList.getSuccess().getData().getPriceDetails().getShipping()) + " Qar");
                        Hawk.put("total_payment", cartList.getSuccess().getData().getPriceDetails());
                        loadingFbAniamtion(false);
                        Hawk.put("cart_count", String.valueOf(listCart.size()));
                        cartSizeUpdate(listCart.size());
                        if (listCart.size() == 0) {
                            lenCartEmpty.setVisibility(View.VISIBLE);
                        } else {
                            lenCartEmpty.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CartListModel> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//

    private void enableSwipeToDeleteAndUndo() {
        loadingFbAniamtion(true);
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
//                final String item = adapterCart.getData().get(position);

//                mAdapter.removeItem(position);
//                adapterCart.rem
//                startAnimation();
                cartItemDelete(position);

//                Snackbar snackbar = Snackbar
//                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        mAdapter.restoreItem(item, position);
//                        recyclerView.scrollToPosition(position);
//                    }
//                });

//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerCart);
    }
    
    public static void cartItemDelete(int position){
        loadingFbAniamtion(true);
        Call<CartUpdateModel> call = apiInterface.cartDelete(listCart.get(position).getCartId(),userTocken);
        call.enqueue(new Callback<CartUpdateModel>() {
            @Override
            public void onResponse(Call<CartUpdateModel> call, Response<CartUpdateModel> response) {

                CartUpdateModel deletStatus;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                deletStatus = gson.fromJson(jsonElement, CartUpdateModel.class);

                if (deletStatus.getSuccess().getStatus()==200){
//                    cartUpdate();
                    Toast.makeText(Application.getContext1(),deletStatus.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                    getCart();
                }
                else {
                    Toast.makeText(Application.getContext1(),deletStatus.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }

//                loadingFbAniamtion(false);

            }

            @Override
            public void onFailure(Call<CartUpdateModel> call, Throwable t) {
                loadingFbAniamtion(false);

            }
        });
    }

    public static void loadingFbAniamtion( boolean stsaus){

        if (stsaus){
//            mShimmerViewContainer.setVisibility(View.VISIBLE);
//            mShimmerViewContainer.startShimmerAnimation();
            img_loder.setVisibility(View.VISIBLE);
        }
        else {
//            mShimmerViewContainer.setVisibility(View.GONE);
//            mShimmerViewContainer.stopShimmerAnimation();
            img_loder.setVisibility(View.GONE);
        }

    }

}
