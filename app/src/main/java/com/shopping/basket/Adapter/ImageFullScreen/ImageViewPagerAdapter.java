package com.shopping.basket.Adapter.ImageFullScreen;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;

import java.util.ArrayList;
import java.util.List;

public class ImageViewPagerAdapter extends PagerAdapter {

//    private ArrayList drawables;
    List<String> drawables = new ArrayList<>();

    public ImageViewPagerAdapter(List<String> drawables) {
        this.drawables = drawables;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Context context = container.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.page_image, null);
        container.addView(view);

        ImageView imageView = view.findViewById(R.id.image);
//        imageView.setImageDrawable(drawables.get(position).toString());
        Glide.with(context)
                .load(util.Imageurl+drawables.get(position).toString())
                .into(imageView);
        imageView.setAdjustViewBounds(true);

        ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(context);
        imageView.setOnTouchListener(imageMatrixTouchHandler);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;

        ImageView imageView = view.findViewById(R.id.image);
        imageView.setImageResource(0);

        container.removeView(view);
    }

    @Override
    public int getCount() {
        return drawables.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition (Object object) {
        return POSITION_NONE;
    }
}