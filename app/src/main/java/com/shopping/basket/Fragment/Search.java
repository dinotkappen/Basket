package com.shopping.basket.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Adapter.CartAdapter.CartAdapter;
import com.shopping.basket.Adapter.SearchAdapter.HomeBottomProductAdapter;
import com.shopping.basket.Adapter.SearchAdapter.SearchAdapter;
import com.shopping.basket.Model.CategoryModel.CategoryDatum;
import com.shopping.basket.Model.ProductListModel.ProductData;
import com.shopping.basket.Model.ProductListModel.ProductListModel;
import com.shopping.basket.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.MainActivity.backhide;
import static com.shopping.basket.Activity.MainActivity.bottom_clck;
import static com.shopping.basket.Activity.MainActivity.rightBackHide;
import static com.shopping.basket.Activity.MainActivity.titlHide;

public class Search extends MainContanier {

    RecyclerView recyBottomProduct;
    SearchAdapter adapdterSearch;
    static EditText editSearch;
    List<ProductData> listSearch = new ArrayList<>();
    Call<ProductListModel> call =null;
    static API apiInterface;
    ProgressBar progressBar;
    ImageView imgClose;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.search, container, false);
        backhide();
        titlHide(getResources().getString(R.string.search_here_products));
        bottom_clck();
        recyBottomProduct = rootView.findViewById(R.id.recycler_search);
        progressBar = rootView.findViewById(R.id.progressBar);
        editSearch = rootView.findViewById(R.id.edit_search);
        imgClose = rootView.findViewById(R.id.img_close);

        imgClose.setVisibility(View.GONE);
        recyBottomProduct.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapdterSearch = new SearchAdapter(getActivity(), listSearch);
        recyBottomProduct.setAdapter(adapdterSearch);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearch.setText("");
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (call!=null)
                    call.cancel();
                String search = s.toString();
                if (!s.toString().equals("")) {
                    search(search);
                    imgClose.setVisibility(View.VISIBLE);
                }
                else {
                    imgClose.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    listSearch.clear();
                    adapdterSearch.notifyDataSetChanged();
                }
            }
        });


        return rootView;

    }
    public void search(String search){
        progressBar.setVisibility(View.VISIBLE);
        call = apiInterface.search(1,search);
        call.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response) {
                ProductListModel seachList ;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("seachList",jsonElement.toString());
                seachList = gson.fromJson(jsonElement, ProductListModel.class);
                listSearch.clear();
                listSearch = seachList.getSuccess().getData();
                Log.e("listSearch",""+listSearch.size());
                adapdterSearch = new SearchAdapter(getActivity(), listSearch);
                recyBottomProduct.setAdapter(adapdterSearch);
//                adapdterSearch.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductListModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    public static void clearText(){
        editSearch.setText("");
    }

}
