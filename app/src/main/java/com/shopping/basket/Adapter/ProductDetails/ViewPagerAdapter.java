package com.shopping.basket.Adapter.ProductDetails;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shopping.basket.Fragment.FullImageView;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    LayoutInflater inflater;
    int position = 3;
    List<String>  banner_list;
    FragmentActivity activity;
    ArrayList images = new ArrayList();


    public ViewPagerAdapter(FragmentActivity activity, List<String> banner_list) {
        this.activity = activity;
        this.banner_list = banner_list;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return banner_list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imgBanner;

        View imageLayout = inflater.inflate(R.layout.product_details_item, container, false);

        assert imageLayout != null;

        imgBanner = imageLayout.findViewById(R.id.img_banner);
        Glide.with(activity)
                .load(util.Imageurl+banner_list.get(position))
                .into(imgBanner);

//        imgBanner.setImageDrawable(activity.getResources().getDrawable(banner_list.get(position).drawble));
//        imgBanner.setAdjustViewBounds(true);
        images.add(util.Imageurl+banner_list.get(position));
        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.e("count",""+images.size());
                FullImageView fragment = FullImageView.newInstance(banner_list);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();
            }
        });
        container.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((View) object);
    }
}
