package com.shopping.basket.Adapter.HomeAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shopping.basket.Model.CategoryModel.CategoryDatum;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

private Context mContext;
private List<CategoryDatum> categoryList;
public static final String MY_PREFS_NAME = "MyPrefsFile";
String url = "http://basket.qa/assets/images/categories/";
public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    ImageView imgCategory;

    public MyViewHolder(View view) {
        super(view);
        title           = view.findViewById(R.id.title);
        imgCategory     = view.findViewById(R.id.img_product);



    }
}

    public HomeAdapter(Context mContext, List<CategoryDatum> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CategoryDatum itemMainModel = categoryList.get(position);

        holder.title.setText(itemMainModel.getName());
//        holder.imgCategory.setImageDrawable(mContext.getResources().getDrawable(itemMainModel.drawble));
        Glide.with(mContext).load(util.Imageurl+itemMainModel.getPhoto()).into(holder.imgCategory);
//        Log.e("imgae_url",""+itemMainModel.getCover());



    }


    @Override
    public int getItemCount() {
        if (categoryList != null)
            return categoryList.size();
        else
            return 0;
    }
}
