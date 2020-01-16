package com.shopping.basket.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shopping.basket.Adapter.ImageFullScreen.ImageViewPagerAdapter;
import com.shopping.basket.R;

import java.util.ArrayList;
import java.util.List;

public class FullImageView extends MainContanier{
    private ViewPager viewPager;
    private ImageViewPagerAdapter imageViewPagerAdapter;
    static ArrayList image_array = new ArrayList();
    static List<String> banner_list = new ArrayList<>();

    public static FullImageView newInstance(List<String> pageUrl) {
        FullImageView fragment = new FullImageView();
        banner_list = pageUrl;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.ful_image, container, false);

//        image_array.add()

        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);

//        imageViewPagerAdapter = new ImageViewPagerAdapter(drawables);
        viewPager = rootView.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);
        imageViewPagerAdapter= new ImageViewPagerAdapter(banner_list);
        imageViewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(imageViewPagerAdapter);

        return rootView;

    }
}
