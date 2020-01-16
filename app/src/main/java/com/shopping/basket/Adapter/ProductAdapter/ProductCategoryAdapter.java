package com.shopping.basket.Adapter.ProductAdapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopping.basket.Model.CategoryModel.CategoryDatum;
import com.shopping.basket.R;

import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.MyViewHolder> {

    int row_index;
    private FragmentActivity mContext;
    private List<CategoryDatum> categoryList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title,price;
    ImageView imgCategory,imgFavourite;

    public MyViewHolder(View view) {
        super(view);
        title           = view.findViewById(R.id.txt_category);
    }
}

    public ProductCategoryAdapter(FragmentActivity mContext, List<CategoryDatum> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_category_item, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CategoryDatum itemMainModel = categoryList.get(position);

        holder.title.setText(itemMainModel.getName());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.title.setBackgroundResource(R.drawable.category_select);
//            holder.title.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            holder.title.setBackgroundResource(R.drawable.category_unselect);
//            holder.title.setTextColor(Color.parseColor("#000000"));
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
