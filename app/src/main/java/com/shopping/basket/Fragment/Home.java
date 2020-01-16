package com.shopping.basket.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Adapter.HomeAdapter.HomeAdapter;
import com.shopping.basket.Adapter.HomeAdapter.HomeNewAdapter;
import com.shopping.basket.Adapter.HomeAdapter.HomeTopAdapter;
import com.shopping.basket.Adapter.HomeAdapter.ViewPagerAdapter_banner;
import com.shopping.basket.Adapter.ProductAdapter.ProductListAdapter;
import com.shopping.basket.Application;
import com.shopping.basket.CardView.CardFragmentPagerAdapter;
import com.shopping.basket.CardView.CardItem;
import com.shopping.basket.CardView.CardPagerAdapter;
import com.shopping.basket.CardView.ShadowTransformer;
import com.shopping.basket.CustomView.RecyclerItemClickListener;
import com.shopping.basket.Model.BannerModel.Banner;
import com.shopping.basket.Model.BannerModel.BannerList;
import com.shopping.basket.Model.BannerModel.Success;
import com.shopping.basket.Model.CategoryModel.CategoryDatum;
import com.shopping.basket.Model.CategoryModel.CategorySuccess;
import com.shopping.basket.Model.CategoryModel.HomeCategoryModel;
import com.shopping.basket.Model.LatestArrivalModel.LatestProductModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.ProductListModel.ProductData;
import com.shopping.basket.Model.ProductListModel.ProductListModel;
import com.shopping.basket.Model.ProductListModel.ProductListSuccess;
import com.shopping.basket.R;
import com.shopping.basket.Util.util;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.MainActivity.backhide;
import static com.shopping.basket.Activity.MainActivity.bottom_clck;
import static com.shopping.basket.Activity.MainActivity.getCart;
import static com.shopping.basket.Activity.MainActivity.rightBackHide;
import static com.shopping.basket.Activity.MainActivity.titlHide;
import static com.shopping.basket.Util.util.loadingAnimation;
import static com.shopping.basket.Util.util.loadingFbAniamtion;

public class Home extends MainContanier implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener{

    int mainCatId;
    API apiInterface;
    String userToken;
    ViewPager mViewPager;
    CardView search_click;
    RecyclerView recyCategory;
    RecyclerView recyNewProduct;
    HomeAdapter adapterCategory;
    CardPagerAdapter mCardAdapter;
    HomeNewAdapter adapterNewProduct;
    boolean mShowingFragments = false;
    static FragmentActivity mContext;
    static SwipeRefreshLayout refresh;
    static RecyclerView recyTopProduct;
    ImageView latest_pro;
    ImageView imgAdds;
    static ImageView img_loder;
    private static int NUM_PAGES_BANNER = 0;
    static HomeTopAdapter adapterTopProduct;
    private static int currentPage_Banner = 0;
    ShadowTransformer mCardShadowTransformer;
    ArrayList<CardItem> images = new ArrayList();
    CardFragmentPagerAdapter mFragmentCardAdapter;
    private static AVLoadingIndicatorView avi;
    List<BannerList> list_iamg = new ArrayList<>();
    private ShimmerFrameLayout mShimmerViewContainer;
    ShadowTransformer mFragmentCardShadowTransformer;
    List<CategoryDatum> cate_name = new ArrayList<>();
    static List<ProductData> Product_list = new ArrayList<>();
    ProductListSuccess listProductModel = new ProductListSuccess();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.home, container, false);

        mViewPager              = rootView.findViewById(R.id.pager_banner);
        recyCategory            = rootView.findViewById(R.id.recycler_category);
        recyTopProduct          = rootView.findViewById(R.id.recycler_top);
        recyNewProduct          = rootView.findViewById(R.id.recycler_new);
        mShimmerViewContainer   = rootView.findViewById(R.id.shimmer_view_container);
        avi                     =  rootView.findViewById(R.id.avi);
        search_click            =  rootView.findViewById(R.id.card_search);
        latest_pro              =  rootView.findViewById(R.id.img_latest);
        refresh                 = rootView.findViewById(R.id.swipe);
        imgAdds                 = rootView.findViewById(R.id.img_add);
        img_loder               = rootView.findViewById(R.id.img_loder);

        Glide.with(getActivity())
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);
        rightBackHide(false);
        titlHide("null");

        search_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment =  new Search();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();
            }
        });

        mContext = getActivity();
        loadingFbAniamtion(mShimmerViewContainer,false);
        img_loder.setVisibility(View.VISIBLE);
        if (Hawk.get("logStatus","0").equals("1")) {
            getCart();
            LoginData userDt;
            userDt = Hawk.get("user_details");
            userToken = userDt.getToken();
        }
        mCardAdapter = new CardPagerAdapter(getActivity());
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                dpToPixels(2, getActivity()));
//
//        for (int i = 0; i <4 ; i++) {
////            images.add(new CardItem("http://click.whytecreations.in/public/uploads/dashboard/jaYBDQCtHBDrxMBY8GkIPIcm6EHzfTW8tYqW6e6O.jpeg"));
//            mCardAdapter.addCardItem(new CardItem("http://click.whytecreations.in/public/uploads/dashboard/jaYBDQCtHBDrxMBY8GkIPIcm6EHzfTW8tYqW6e6O.jpeg"));
//        }



        recyCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapterCategory = new HomeAdapter(getActivity(), cate_name);
        recyCategory.setAdapter(adapterCategory);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapterTopProduct = new HomeTopAdapter(getActivity(), Product_list);
        recyTopProduct.setLayoutManager(layoutManager);
        recyTopProduct.setAdapter(adapterTopProduct);
//
//        LinearLayoutManager layoutManager1
//                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        adapterNewProduct = new HomeNewAdapter(getActivity(), listCategory);
//        recyNewProduct.setLayoutManager(layoutManager1);
//        recyNewProduct.setAdapter(adapterNewProduct);

        latest_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainCatId = cate_name.get(0).getId();
                Fragment fragment =  ProductListFragment.newInstance(mainCatId,cate_name.get(0).getName(),1);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();
            }
        });

        recyCategory.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyCategory, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.e("category_id",""+cate_name.get(position).getId());
                mainCatId = cate_name.get(position).getId();
                Fragment fragment =  ProductListFragment.newInstance(mainCatId,cate_name.get(position).getName(),0);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_contaner, fragment);
                transaction.addToBackStack(null).commit();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mShimmerViewContainer.startShimmerAnimation();
                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.VISIBLE);
                getBanner();
                getCategory();

            }
        });
        getBanner();
        getCategory();

//        test();

        return rootView;

    }

    public void getLatestArival(){
        img_loder.setVisibility(View.VISIBLE);
        Call<LatestProductModel> call = apiInterface.getLatestArival();
        call.enqueue(new Callback<LatestProductModel>() {
            @Override
            public void onResponse(Call<LatestProductModel> call, Response<LatestProductModel> response) {

                LatestProductModel status = new LatestProductModel();// response.body();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("latest_banner",jsonElement.toString());
                status = gson.fromJson(jsonElement, LatestProductModel.class);
                if(status.getSuccess().getStatus()==200){
                    Glide.with(mContext).load(util.Imageurl+status.getSuccess().getData().get(0).getPhoto()).into(latest_pro);
                }
                else {
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
                refresh.setRefreshing(false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LatestProductModel> call, Throwable t) {
                img_loder.setVisibility(View.GONE);
                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
                refresh.setRefreshing(false);
            }
        });

    }

    public void getBanner(){
        Call<Banner> call = apiInterface.getBanner();
        call.enqueue(new Callback<Banner>() {
            @Override
            public void onResponse(Call<Banner> call, Response<Banner> response) {

                Banner status = new Banner();// response.body();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("banner",jsonElement.toString());
                status = gson.fromJson(jsonElement, Banner.class);

                Success success = status.getSuccess();
                list_iamg.clear();
                list_iamg = success.getData();
//                Log.e("size_bannar",""+list_iamg.size());
                for (int i = 0; i <list_iamg.size() ; i++) {
                    mCardAdapter.addCardItem(new CardItem(util.Imageurl+list_iamg.get(i).getPhoto()));
                }
                Glide.with(mContext).load(util.Imageurl+list_iamg.get(0).getPhoto()).into(imgAdds);
                banner_load();
//                setBanner();
            }

            @Override
            public void onFailure(Call<Banner> call, Throwable t) {

            }
        });
    }

    public void setBanner(){
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(4);
    }

    public void banner_load(){
        ViewPagerAdapter_banner adapter = new ViewPagerAdapter_banner(getActivity(), list_iamg);
        mViewPager.setAdapter(adapter);

        NUM_PAGES_BANNER = list_iamg.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage_Banner == NUM_PAGES_BANNER) {
                    currentPage_Banner = 0;
                }
                mViewPager.setCurrentItem(currentPage_Banner++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 6000, 6000);

        // Pager listener over indicator


//        indicator.setViewPager(viewpager);
//        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                currentPage_Banner = position;
//
//            }
//
//            @Override
//            public void onPageScrolled(int pos, float arg1, int arg2) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int pos) {
//
//            }
//        });
    }

    public void getCategory(){

        Call<HomeCategoryModel> call = apiInterface.getCategory(0);
        call.enqueue(new Callback<HomeCategoryModel>() {
            @Override
            public void onResponse(Call<HomeCategoryModel> call, Response<HomeCategoryModel> response) {
                HomeCategoryModel status ;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("category",jsonElement.toString());
                status = gson.fromJson(jsonElement, HomeCategoryModel.class);

                CategorySuccess Category = status.getSuccess();

                cate_name = Category.getData();

                adapterCategory = new HomeAdapter(getActivity(), cate_name);
                recyCategory.setAdapter(adapterCategory);
                getBestSeller();
                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<HomeCategoryModel> call, Throwable t) {
                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
                refresh.setRefreshing(false);
                Log.e("error",t.getMessage());

            }
        });
    }


    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mCardShadowTransformer.enableScaling(b);
        mFragmentCardShadowTransformer.enableScaling(b);
    }

    @Override
    public void onClick(View view) {
        if (!mShowingFragments) {

            mViewPager.setAdapter(mFragmentCardAdapter);
            mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        } else {

            mViewPager.setAdapter(mCardAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
        }

        mShowingFragments = !mShowingFragments;
    }

    public void getBestSeller(){
        loadingFbAniamtion(mShimmerViewContainer,false);
        img_loder.setVisibility(View.VISIBLE);
        Call<ProductListModel> call;
        if (Hawk.get("logStatus","0").equals("1")) {

            call = apiInterface.getBestsellerLogin("best",userToken);
        }
        else
            call = apiInterface.getBestseller("best");
        call.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response) {
                try {
                    ProductListModel status ;
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    Log.e("category_pkb_best",jsonElement.toString());
                    status = gson.fromJson(jsonElement, ProductListModel.class);
                    listProductModel = status.getSuccess();

                    Product_list = listProductModel.getData();
                    adapterTopProduct = new HomeTopAdapter(getActivity(), Product_list);
                    recyTopProduct.setAdapter(adapterTopProduct);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                img_loder.setVisibility(View.GONE);
                getLatestArival();
            }

            @Override
            public void onFailure(Call<ProductListModel> call, Throwable t) {
                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);
                Log.e("r",t.getMessage());

            }
        });
    }

    public static void wishlistUpdate(ProductData itemMainModel,int position){

        Product_list.set(position,itemMainModel);

        adapterTopProduct = new HomeTopAdapter(mContext, Product_list);
        recyTopProduct.setAdapter(adapterTopProduct);

    }

    public static void Loading(boolean ststaus){

        if (ststaus)
            img_loder.setVisibility(View.VISIBLE);
        else
            img_loder.setVisibility(View.GONE);

    }

}
