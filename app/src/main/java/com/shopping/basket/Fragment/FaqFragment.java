package com.shopping.basket.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Adapter.FaqAdapter.CustomExpandableListAdapter;
import com.shopping.basket.Adapter.OrderHistoryAdapter.OrderHistoryAdapter;
import com.shopping.basket.Application;
import com.shopping.basket.Model.CategoryModel.HomeCategoryModel;
import com.shopping.basket.Model.FaqModel.FaqDatum;
import com.shopping.basket.Model.FaqModel.FaqModel;
import com.shopping.basket.Model.OrderHistoryModel.OrderHistoryModel;
import com.shopping.basket.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.MainActivity.backSideManu;
import static com.shopping.basket.Activity.MainActivity.rightBackHide;
import static com.shopping.basket.Activity.MainActivity.titlHide;
import static com.shopping.basket.Util.util.loadingFbAniamtion;

public class FaqFragment extends MainContanier {

    API apiInterface;
    List<String> expandableListTitle;
    ExpandableListView expandableListView;
    List<FaqDatum> listdata = new ArrayList<>();
    ExpandableListAdapter expandableListAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
    static HashMap<String, List<String>> expandableListDetail1 = new HashMap<String, List<String>>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.faq, container, false);

      //  rightBackHide(true);
        backSideManu();
        titlHide(getResources().getString(R.string.menu_faq));
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        mShimmerViewContainer   = rootView.findViewById(R.id.shimmer_view_container);



        getFaq();

        return rootView;

    }



    public void getFaq(){
        loadingFbAniamtion(mShimmerViewContainer,true);
        Call<FaqModel> call = apiInterface.getFaq();
        call.enqueue(new Callback<FaqModel>() {
            @Override
            public void onResponse(Call<FaqModel> call, Response<FaqModel> response) {
                FaqModel status ;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("category",jsonElement.toString());
                status = gson.fromJson(jsonElement, FaqModel.class);

                if (status.getSuccess().getStatus()==200){
                    listdata = status.getSuccess().getData();
                    for (int i = 0; i <listdata.size() ; i++) {
                        List<String> basketball = new ArrayList<String>();
                        basketball.add(android.text.Html.fromHtml(listdata.get(i).getDetails()).toString());
                        expandableListDetail1.put(listdata.get(i).getTitle(), basketball);

                    }
                    expandableListDetail = expandableListDetail1;
                    expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                    expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
                    expandableListView.setAdapter(expandableListAdapter);

//                    Toast.makeText(getActivity(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
                loadingFbAniamtion(mShimmerViewContainer,false);
            }

            @Override
            public void onFailure(Call<FaqModel> call, Throwable t) {
                loadingFbAniamtion(mShimmerViewContainer,false);
            }
        });

    }

}
