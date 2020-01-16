package com.shopping.basket.Adapter.CartAdapter;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Fragment.CartFragment;
import com.shopping.basket.Model.CartModel.CartItemsModel;
import com.shopping.basket.Model.CartModel.CartListModel.CartList;
import com.shopping.basket.Model.CartUpdateModel.CartUpdateModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Fragment.CartFragment.cartItemDelete;
import static com.shopping.basket.Fragment.CartFragment.loadingFbAniamtion;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    LoginData profile_dt;
    API apiInterface;
    String userTocken;
    private FragmentActivity mContext;
    private List<CartList> listPhone;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgproduct;
        LinearLayout btProducClick;
        FrameLayout framPluse, framMinus;
        public TextView title, prize, qty;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_title);
            imgproduct = view.findViewById(R.id.img_product);
            prize = view.findViewById(R.id.txt_price);
            framMinus = view.findViewById(R.id.fram_minus);
            framPluse = view.findViewById(R.id.fram_plus);
            qty = view.findViewById(R.id.txt_qty);


        }
    }

    public CartAdapter(FragmentActivity mContext, List<CartList> listPhone) {
        this.mContext = mContext;
        this.listPhone = listPhone;
        this.apiInterface = APIClient.getClient().create(API.class);
        this.profile_dt = Hawk.get("user_details");
        this.userTocken = profile_dt.getToken();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);


        return new MyViewHolder(itemView);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final CartList itemMainModel = listPhone.get(position);
//        }
        holder.title.setText(itemMainModel.getProductName());
        holder.prize.setText("QAR " + itemMainModel.getUnitPrice());
        holder.qty.setText(String.valueOf(itemMainModel.getQuantity()));

        Glide.with(mContext).load(util.Imageurl + itemMainModel.getProductPhoto()).into(holder.imgproduct);

        holder.framMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(itemMainModel.getQuantity()) > 0) {

                    if(Integer.parseInt(itemMainModel.getQuantity()) == 1)
                    {
                        cartItemDelete(position);
                    }
                    else
                    {
                        updateCart(itemMainModel.getCartId(), Integer.parseInt(itemMainModel.getQuantity()) - 1, holder);
                    }
                }


            }
        });

        holder.framPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("cart_id",String.valueOf(itemMainModel.id));
                updateCart(itemMainModel.getCartId(), Integer.parseInt(itemMainModel.getQuantity()) + 1, holder);

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

    public void updateCart(int cart_id, final int qty, final MyViewHolder holder) {
        loadingFbAniamtion(true);
        Call<CartUpdateModel> call = apiInterface.cartUpdate(cart_id, qty, userTocken);
        call.enqueue(new Callback<CartUpdateModel>() {
            @Override
            public void onResponse(Call<CartUpdateModel> call, Response<CartUpdateModel> response) {

                CartUpdateModel updateStatus;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
//                Log.e("cardUpdate",jsonElement.toString());
                updateStatus = gson.fromJson(jsonElement, CartUpdateModel.class);

                if (updateStatus.getSuccess().getStatus() == 200) {
                    CartFragment.getCart();
                }
                loadingFbAniamtion(false);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CartUpdateModel> call, Throwable t) {
                loadingFbAniamtion(false);
            }
        });

    }
}
