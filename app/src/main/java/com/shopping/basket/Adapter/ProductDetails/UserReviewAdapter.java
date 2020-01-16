package com.shopping.basket.Adapter.ProductDetails;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopping.basket.Model.ReviewListModel.Cartlist;
import com.shopping.basket.R;
import java.util.List;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.MyViewHolder> {

    private FragmentActivity mContext;
    private List<Cartlist> listPhone;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView review,rating,txt_name,txt_title;

        public MyViewHolder(View view) {
            super(view);

            review           = view.findViewById(R.id.review);
            rating           = view.findViewById(R.id.txt_rating);
            txt_name         = view.findViewById(R.id.txt_name);
            txt_title        = view.findViewById(R.id.txt_title);

        }
    }

    public UserReviewAdapter(FragmentActivity mContext, List<Cartlist> listPhone) {
        this.mContext = mContext;
        this.listPhone = listPhone;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Cartlist listData = listPhone.get(position);

        holder.txt_name.setText(listData.getUserName());
        holder.txt_title.setText(listData.getTitle());
        holder.review.setText(listData.getReview());
        holder.rating.setText(""+listData.getRating());
    }


    @Override
    public int getItemCount() {
        if (listPhone != null)
            return listPhone.size();
        else
            return 0;
    }

}
