package com.shopping.basket.Adapter.SearchAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopping.basket.Model.CategoryModel.CategoryDatum;
import com.shopping.basket.R;

import java.util.List;

public class HomeBottomProductAdapter extends RecyclerView.Adapter<HomeBottomProductAdapter.MyViewHolder> {

    private Context mContext;
    private List<CategoryDatum> categoryList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title,price;
    ImageView imgCategory,imgFavourite;

    public MyViewHolder(View view) {
        super(view);
        title           = view.findViewById(R.id.txt_title);
        price           = view.findViewById(R.id.txt_price);
        imgCategory     = view.findViewById(R.id.img_product);
        imgFavourite    = view.findViewById(R.id.img_favourite);



    }
}

    public HomeBottomProductAdapter(Context mContext, List<CategoryDatum> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_bottom_item, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CategoryDatum itemMainModel = categoryList.get(position);

//        holder.title.setText(itemMainModel.title);
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
