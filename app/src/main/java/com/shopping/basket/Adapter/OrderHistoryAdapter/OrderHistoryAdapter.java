package com.shopping.basket.Adapter.OrderHistoryAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.shopping.basket.Activity.OrderSummeryDetails;
import com.shopping.basket.Model.OrderHistoryModel.OrderHistoryData;
import com.shopping.basket.Model.OrderHistoryModel.OrderHistoryModel;
import com.shopping.basket.R;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<OrderHistoryData> categoryList;

public class MyViewHolder extends RecyclerView.ViewHolder {

    LinearLayout lenClick;
    public TextView txtOrderId,price,txtDate,txtStatus;

    public MyViewHolder(View view) {
        super(view);
        txtOrderId  = view.findViewById(R.id.order_id);
        price       = view.findViewById(R.id.txt_price);
        txtDate     = view.findViewById(R.id.txt_date);
        txtStatus   = view.findViewById(R.id.txt_status);
        lenClick    = view.findViewById(R.id.len_click);


    }
}

    public OrderHistoryAdapter(Context mContext, List<OrderHistoryData> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_summery_item, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OrderHistoryData itemMainModel = categoryList.get(position);

        holder.txtOrderId.setText(""+itemMainModel.getOrderNumber());
        holder.price.setText(""+itemMainModel.getTotalAmount()+" QAR");
        holder.txtStatus.setText(""+itemMainModel.getStatus());
        holder.txtDate.setText(""+itemMainModel.getDate());

        holder.lenClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Hawk.put("order_id",itemMainModel.getOrderNumber());
                Intent summery = new Intent(mContext, OrderSummeryDetails.class);
                mContext.startActivity(summery);

            }
        });
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
