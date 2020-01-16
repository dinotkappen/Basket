package com.shopping.basket.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.shopping.basket.R;


public class NoInternet extends Fragment {
    View rootView;
    Button ReTry;
    ProgressBar loading;
    ImageView img_refesh;
    LinearLayout lean_internet;
    public NoInternet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);



        return rootView;
    }


}
