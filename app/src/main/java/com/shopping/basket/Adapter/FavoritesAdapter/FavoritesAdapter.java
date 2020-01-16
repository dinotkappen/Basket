package com.shopping.basket.Adapter.FavoritesAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shopping.basket.Fragment.FavoritesFragment;
import com.shopping.basket.Fragment.ProductDetailsFragment;
import com.shopping.basket.Model.ProductListModel.ProductData;
import com.shopping.basket.Model.WishListModel.Wishlist;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.MyViewHolder> {

    private FragmentActivity mContext;
    private List<ProductData> categoryList;
    FavoritesFragment favoritesFragment;

public class MyViewHolder extends RecyclerView.ViewHolder {
    LinearLayout lenClick;
    public TextView title,price;
    ImageView imgCategory,imgFavourite;

    public MyViewHolder(View view) {
        super(view);

        title           = view.findViewById(R.id.txt_title);
        price           = view.findViewById(R.id.txt_price);
        imgCategory     = view.findViewById(R.id.img_product);
        imgFavourite    = view.findViewById(R.id.img_favourite);
        lenClick        = view.findViewById(R.id.len_click);

    }
}

    public FavoritesAdapter(FragmentActivity mContext, List<ProductData> categoryList, FavoritesFragment favoritesFragment) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.favoritesFragment=favoritesFragment;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorites_item, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductData itemMainModel = categoryList.get(position);

        holder.title.setText(itemMainModel.getName());
        holder.price.setText(itemMainModel.getPrice()+" QAR");



        Glide.with(mContext)
                .load(util.Imageurl+itemMainModel.getPhoto())
                .into(holder.imgCategory);
        holder.lenClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = ProductDetailsFragment.newInstance(itemMainModel.getId(),itemMainModel.getName());
                FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();
            }
        });

        holder.imgFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoritesFragment.addWhisList(itemMainModel.getId(),position);

            }
        });

//        holder.imgCategory.setImageDrawable(mContext.getResources().getDrawable(itemMainModel.drawble));
//        Glide.with(mContext).load(itemMainModel.getCover()).into(holder.imgCategory);



    }


    @Override
    public int getItemCount() {
        if (categoryList != null)
            return categoryList.size();
        else
            return 0;
    }
}
