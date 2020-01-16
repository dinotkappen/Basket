package com.shopping.basket.Adapter.SearchAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shopping.basket.Fragment.ProductDetailsFragment;
import com.shopping.basket.Model.CategoryModel.CategoryDatum;
import com.shopping.basket.Model.ProductListModel.ProductData;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;

import java.util.List;

import static com.shopping.basket.Fragment.Search.clearText;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private FragmentActivity mContext;
    private List<ProductData> categoryList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageView imgSearch;
    public MyViewHolder(View view) {
        super(view);
        title           = view.findViewById(R.id.txt_text);
        imgSearch       = view.findViewById(R.id.imgSearch);
    }
}

    public SearchAdapter(FragmentActivity mContext, List<ProductData> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
    String photo;
        final ProductData itemMainModel = categoryList.get(position);

        holder.title.setText(itemMainModel.getName());

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
                Fragment fragment = ProductDetailsFragment.newInstance(itemMainModel.getId(),itemMainModel.getName());
                FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();
            }
        });
        photo=itemMainModel.getPhoto();
        if (photo != null && !photo.isEmpty() && !photo.equals("null"))
        {
            Glide.with(mContext)
                    .load(util.Imageurl + photo)
                    .placeholder(R.drawable.user_color)
                    .into(holder.imgSearch);
        }


    }


    @Override
    public int getItemCount() {
        if (categoryList != null)
            return categoryList.size();
        else
            return 0;
    }
}
