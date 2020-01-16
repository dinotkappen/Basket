package com.shopping.basket.Fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Activity.MainActivity;
import com.shopping.basket.Adapter.ProductAdapter.ProductCategoryAdapter;
import com.shopping.basket.Adapter.ProductAdapter.ProductListAdapter;
import com.shopping.basket.Application;
import com.shopping.basket.CustomView.RecyclerItemClickListener;
import com.shopping.basket.Interface.OnBottomReachedListener;
import com.shopping.basket.Model.CategoryModel.CategoryDatum;
import com.shopping.basket.Model.CategoryModel.CategorySuccess;
import com.shopping.basket.Model.CategoryModel.HomeCategoryModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.ProductListModel.ProductData;
import com.shopping.basket.Model.ProductListModel.ProductListModel;
import com.shopping.basket.Model.ProductListModel.ProductListSuccess;
import com.shopping.basket.Model.SubCategoryModel.SubCategoryData;
import com.shopping.basket.Model.WishListModel.WishListModel;
import com.shopping.basket.Model.WishListModel.Wishlist;
import com.shopping.basket.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Util.util.loadingAnimation;
import static com.shopping.basket.Util.util.loadingFbAniamtion;

public class ProductListFragment extends MainContanier {

    int arrayCount=0;
    int loader = 0;
     int from ,to;
    static ImageView img_loder;
    String lastPageUrl;
    int lasPosition = 0;
    String next_page_url;
    TextView titleCategory;
    int page_loaderflag = 0;
    LinearLayout lenNoItem;
    static API apiInterface;
    static int categoryId,pageType;
    static FragmentActivity cotext;
    static RecyclerView recyCategory;
    static RecyclerView recyProduct;
    static SwipeRefreshLayout refresh;
    private LinearLayout bottom_sheet;
    FloatingActionButton filter,sorting,menu_filter_clear;
    int productId,subCategoryId,pagnumber=1;
    private BottomSheetBehavior sheetBehavior;
     ProductListAdapter adapterProduct;
    private static AVLoadingIndicatorView avi;
    static ProductCategoryAdapter adapterCategory;
    private ShimmerFrameLayout mShimmerViewContainer;
    List<ProductData> listProduct = new ArrayList<>();
    List<SubCategoryData> listCategory = new ArrayList<>();
    static List<Wishlist> listWishList = new ArrayList<>();
    static String categoryNmae,selectedLang,userToken="null";
    static List<ProductData> Product_list = new ArrayList<>();
    static  List<ProductListModel> allList = new ArrayList<>();
    ProductListSuccess listProductModel = new ProductListSuccess();
    private static List<CategoryDatum> smartPhoneList = new ArrayList<>();


    public static ProductListFragment newInstance(int mainCat_id,String category,int type) {
        ProductListFragment fragment = new ProductListFragment();
        categoryId = mainCat_id;
        categoryNmae = category;
        pageType = type;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
        cotext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.product_list, container, false);
        selectedLang= Hawk.get("selectedLang","en");
        if (Hawk.get("logStatus","0").equals("1")) {
            LoginData userDt;
            userDt = Hawk.get("user_details");
            userToken = userDt.getToken();
//            getwishList();
        }
//        Log.e("id_s",""+categoryId);
        MainActivity.double_icon();
        Product_list.clear();
        recyCategory            = rootView.findViewById(R.id.recy_category);
        recyProduct             = rootView.findViewById(R.id.recy_product);
        mShimmerViewContainer   = rootView.findViewById(R.id.shimmer_view_container);
        lenNoItem               = rootView.findViewById(R.id.len_no_itme);
        refresh                 = rootView.findViewById(R.id.swipe);
        avi                     = rootView.findViewById(R.id.avi);
        filter                  = rootView.findViewById(R.id.menu_filter);
        sorting                 = rootView.findViewById(R.id.menu_sot);
        menu_filter_clear       = rootView.findViewById(R.id.menu_filter_clear);
        titleCategory            = rootView.findViewById(R.id.txt_category);
        img_loder               = rootView.findViewById(R.id.img_loder);

        Glide.with(getActivity())
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);

//        sheetBehavior           = BottomSheetBehavior.from(bottom_sheet);


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allList.clear();
                Product_list.clear();
                View view = getLayoutInflater ().inflate (R.layout.filter_sheet, null);
                TextView price_text = view.findViewById( R.id.txt_price);
                TextView discount_text = view.findViewById( R.id.txt_discount);
                TextView txtClear = view.findViewById( R.id.txtClear);

                final Dialog mBottomSheetDialog = new Dialog (getActivity(), R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView (view);
                mBottomSheetDialog.setCancelable (true);
                mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
                mBottomSheetDialog.show ();

                price_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pagnumber = 1;
                        page_loaderflag=3;
                        arrayCount = 0;
                        priceRangeSelect();
                        mBottomSheetDialog.dismiss();

                    }
                });
                discount_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pagnumber = 1;
                        page_loaderflag=4;
                        arrayCount = 0;
                        getProductList_sort("discount");
                        mBottomSheetDialog.dismiss();


                    }
                });

                txtClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        allList.clear();
                        Product_list.clear();

                        lasPosition = listProduct.size();
//                loadingFbAniamtion(mShimmerViewContainer,true);
                        page_loaderflag=0;
                        loader=0;
                        pagnumber = 1;
                        mBottomSheetDialog.dismiss();
                        getProductList();

                    }
                });

            }
        });
        menu_filter_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allList.clear();
                Product_list.clear();

                lasPosition = listProduct.size();
//                loadingFbAniamtion(mShimmerViewContainer,true);
                page_loaderflag=0;
                loader=0;
                pagnumber = 1;
                getProductList();

            }
        });
        sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allList.clear();
                Product_list.clear();
                View view = getLayoutInflater ().inflate (R.layout.sorting_sheet, null);
                TextView new_text = view.findViewById( R.id.txt_new);
                TextView price_h_t_l = view.findViewById( R.id.txt_price_h_t_l);
                TextView price_l_t_h = view.findViewById( R.id.txt_price_l_t_h);
                TextView txtClear = view.findViewById( R.id.txtClear);
                final Dialog mBottomSheetDialog = new Dialog (getActivity(), R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView (view);
                mBottomSheetDialog.setCancelable (true);
                mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
                mBottomSheetDialog.show ();

                new_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Product_list.clear();
                        pagnumber = 1;
                        page_loaderflag=1;
                        arrayCount = 0;
                        getProductList_sort("latest");
                        mBottomSheetDialog.dismiss();
                    }
                });

                price_h_t_l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Product_list.clear();
                        pagnumber=1;
                        page_loaderflag=2;
                        arrayCount = 0;
                        getProductListADsort("D_A");
                        mBottomSheetDialog.dismiss();
                    }
                });
                price_l_t_h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        getProductList_sort("latest");
//                        getProductListPricesort(1,100000,"l");
                        Product_list.clear();
                        pagnumber = 1;
                        page_loaderflag=5;
                        arrayCount = 0;
                        getProductListADsort("A_D");

                        mBottomSheetDialog.dismiss();
                    }
                });
                txtClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        allList.clear();
                        Product_list.clear();

                        lasPosition = listProduct.size();
//                loadingFbAniamtion(mShimmerViewContainer,true);
                        page_loaderflag=0;
                        loader=0;
                        pagnumber = 1;
                        mBottomSheetDialog.dismiss();
                        getProductList();

                    }
                });
            }
        });

//        mShimmerViewContainer.startShimmerAnimation();
        loadingFbAniamtion(mShimmerViewContainer,false);
        img_loder.setVisibility(View.VISIBLE);


        recyProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        adapterProduct = new ProductListAdapter(getActivity(), listProduct);
//        recyProduct.setAdapter(adapterProduct);

        adapterProduct = new ProductListAdapter(getActivity(), Product_list);
        recyProduct.setAdapter(adapterProduct);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyProduct.getContext(),
//                GridLayoutManager.getOrientation());
//        recyProduct.addItemDecoration(dividerItemDecoration);
        adapterProduct.notifyDataSetChanged();

        //****************************************CategoryRecyclerView****************************************
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyCategory.setLayoutManager(layoutManager);
        adapterCategory = new ProductCategoryAdapter(getActivity(), smartPhoneList);
        recyCategory.setAdapter(adapterCategory);
        if (pageType==0) {
            getCategory();
            recyCategory.setVisibility(View.VISIBLE);
        }
        else {
            recyCategory.setVisibility(View.GONE);
            pageType=0;
            Hawk.put("subCategoryId",0);
            getProductList();
            loader=0;
        }
//
//        adapterProduct.setOnBottomReachedListener(new OnBottomReachedListener() {
//            @Override
//            public void onBottomReached(int position) {
//                //your code goes here
//
////
//            }
//        });

        recyProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Toast.makeText(getContext(), "Lst", Toast.LENGTH_SHORT).show();
//                    if (next_page_url != null && !next_page_url.isEmpty() && !next_page_url.equals("null")) {
                    if (Product_list.size()>=arrayCount) {
                        arrayCount = arrayCount+10;
                        pagnumber = pagnumber + 1;

                        loader = 1;

                        if (page_loaderflag == 0) {
                            pageType = 0;
                            getProductList();
                        } else if (page_loaderflag == 1) {
                            getProductList_sort("latest");
                        } else if (page_loaderflag == 2) {
                            getProductListADsort("D_A");
                        } else if (page_loaderflag == 3) {
                            getProductListPricesort(from,to);
                        } else if (page_loaderflag == 4) {
                            getProductList_sort("discount");
                        } else if (page_loaderflag == 5) {
                            getProductListADsort("A_D");
                        }
                    }
//                    }

                }
            }
        });


        recyCategory.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyCategory, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                listPhones.clear();
//                categoryId = smartPhoneList.get(position).getId().toString();
//                loadingFbAniamtion(mShimmerViewContainer,true);
//                getProduct();
                loader = 1;
                Product_list.clear();
                subCategoryId = smartPhoneList.get(position).getId();
                Product_list.clear();
                page_loaderflag=0;
                pageType=0;
                arrayCount = 10;
                pagnumber = 1;
                titleCategory.setText(smartPhoneList.get(position).getName());
                getProductList();
                loader=1;
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        loadingFbAniamtion(mShimmerViewContainer,false);
        img_loder.setVisibility(View.VISIBLE);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mShimmerViewContainer.startShimmerAnimation();
                lasPosition = listProduct.size();
//                loadingFbAniamtion(mShimmerViewContainer,true);
                Product_list.clear();
                page_loaderflag=0;
                loader=0;
                pagnumber = 1;
                getProductList();


            }
        });
//        getProduct();



        return rootView;

    }

    public void getProductList(){
        Hawk.put("subCategoryId",subCategoryId);
//        if ( loader==1){
//            loadingAnimation(avi,true);
//        }
//        else
//        loadingFbAniamtion(mShimmerViewContainer,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<ProductListModel> call;
        if (pageType==0) {
            if (Hawk.get("logStatus", "0").equals("1")) {

                call = apiInterface.getProductListLogin(pagnumber, subCategoryId, userToken);
            } else {
                call = apiInterface.getProductList(pagnumber, subCategoryId);
            }
        }
        else {
            if (Hawk.get("logStatus", "0").equals("1")) {

                call = apiInterface.getLatestLogin(pagnumber, "Latest",subCategoryId, userToken);
            } else {
                call = apiInterface.getLatest(pagnumber, "Latest",subCategoryId);
            }
        }
        call.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response) {
                try {
                    allList.clear();
                    ProductListModel status ;
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(response.body());
                    Log.e("category_pkb",jsonElement.toString());
                    status = gson.fromJson(jsonElement, ProductListModel.class);
                    allList.add(status);
                    listProductModel = status.getSuccess();

                    Product_list.addAll(listProductModel.getData());


//                adapterProduct = new ProductListAdapter(getActivity(), Product_list);
//                recyProduct.setAdapter(adapterProduct);


//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
                            adapterProduct.notifyDataSetChanged();
//                        }
//                    });


//                if (lasPosition == 0) {
//                    recyProduct.scrollToPosition(0);
//                } else {
//                    recyProduct.scrollToPosition(lasPosition - 2);
//                }
//                    loadingFbAniamtion(mShimmerViewContainer,false);
                    refresh.setRefreshing(false);
//                    loadingFbAniamtion(mShimmerViewContainer,false);
//                    loadingAnimation(avi,false);
                    img_loder.setVisibility(View.GONE);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductListModel> call, Throwable t) {
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
                Log.e("error",t.getMessage());

            }
        });
    }

    public void getProductListPricesort(int from , int to){
        Hawk.put("subCategoryId",subCategoryId);
        Log.e("price_details",""+pagnumber+"\n"+subCategoryId+"\n"+from+"\n"+to);
//        loadingFbAniamtion(mShimmerViewContainer,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<ProductListModel> call;
        if (Hawk.get("logStatus", "0").equals("1")) {

            call = apiInterface.getProductList_prices_ortLogin(pagnumber, subCategoryId,from,to, userToken);
        } else {
            call = apiInterface.getProductListprice_sort(pagnumber, subCategoryId,from,to);
        }
        call.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response) {

                ProductListModel status ;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("category_pkb",jsonElement.toString());
                status = gson.fromJson(jsonElement, ProductListModel.class);
                allList.add(status);
                listProductModel = status.getSuccess();

                Product_list.addAll(listProductModel.getData());

//                adapterProduct = new ProductListAdapter(getActivity(), Product_list);
//                recyProduct.setAdapter(adapterProduct);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        adapterProduct.notifyDataSetChanged();
                    }
                });
//                loadingFbAniamtion(mShimmerViewContainer,false);
                refresh.setRefreshing(false);
//                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductListModel> call, Throwable t) {
                img_loder.setVisibility(View.GONE);
                Log.e("error",t.getMessage());

            }
        });
    }

    public void getProductListADsort(String type){
        Hawk.put("subCategoryId",subCategoryId);
        Log.e("price_details",""+pagnumber+"\n"+subCategoryId+"\n"+from+"\n"+to);
//        loadingFbAniamtion(mShimmerViewContainer,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<ProductListModel> call;
        if (Hawk.get("logStatus", "0").equals("1")) {

            call = apiInterface.getProduct_sortLogin(pagnumber, type,subCategoryId, userToken);
        } else {
            call = apiInterface.getProduct_sort(pagnumber,type,subCategoryId);
        }
        call.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response) {
                allList.clear();
                ProductListModel status ;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("category_pkb",jsonElement.toString());
                status = gson.fromJson(jsonElement, ProductListModel.class);
                allList.add(status);
                listProductModel = status.getSuccess();

                Product_list.addAll(listProductModel.getData());



//                adapterProduct = new ProductListAdapter(getActivity(), Product_list);
//                recyProduct.setAdapter(adapterProduct);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        adapterProduct.notifyDataSetChanged();
                    }
                });
//                loadingFbAniamtion(mShimmerViewContainer,false);
                refresh.setRefreshing(false);
//                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductListModel> call, Throwable t) {
                img_loder.setVisibility(View.GONE);
                Log.e("error",t.getMessage());

            }
        });
    }

    public void getProductList_sort(String slug){
        Hawk.put("subCategoryId",subCategoryId);
//        loadingFbAniamtion(mShimmerViewContainer,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<ProductListModel> call;
//        if (pageType==0) {
//            if (Hawk.get("logStatus", "0").equals("1")) {
//
//                call = apiInterface.getProductListLogin(pagnumber, subCategoryId, userToken);
//            } else {
//                call = apiInterface.getProductList(pagnumber, subCategoryId);
//            }
//        }
//        else {
            if (Hawk.get("logStatus", "0").equals("1")) {

                call = apiInterface.getLatestLogin(pagnumber, slug,categoryId, userToken);
            } else {
                call = apiInterface.getLatest(pagnumber, slug,categoryId);
            }
//        }
        call.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response) {
                allList.clear();
                Product_list.clear();
                ProductListModel status ;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("category_pkb",jsonElement.toString());
                status = gson.fromJson(jsonElement, ProductListModel.class);
                allList.add(status);
                listProductModel = status.getSuccess();

                Product_list.addAll(listProductModel.getData());

//                adapterProduct = new ProductListAdapter(getActivity(), Product_list);
//                recyProduct.setAdapter(adapterProduct);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        adapterProduct.notifyDataSetChanged();
                    }
                });
                refresh.setRefreshing(false);
//                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductListModel> call, Throwable t) {

                Log.e("error",t.getMessage());

            }
        });
    }

    public void getCategory(){

        Call<HomeCategoryModel> call = apiInterface.getCategory(categoryId);
        call.enqueue(new Callback<HomeCategoryModel>() {
            @Override
            public void onResponse(Call<HomeCategoryModel> call, Response<HomeCategoryModel> response) {
                HomeCategoryModel status ;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("category",jsonElement.toString());
                status = gson.fromJson(jsonElement, HomeCategoryModel.class);

                CategorySuccess Category = status.getSuccess();

                smartPhoneList = Category.getData();

                adapterCategory = new ProductCategoryAdapter(getActivity(), smartPhoneList);
                recyCategory.setAdapter(adapterCategory);
                subCategoryId = smartPhoneList.get(0).getId();
                Log.e("category_id",""+subCategoryId);
                titleCategory.setText(smartPhoneList.get(0).getName());
                getProductList();
                loader=1;
//                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<HomeCategoryModel> call, Throwable t) {

                Log.e("error",t.getMessage());
//                loadingFbAniamtion(mShimmerViewContainer,false);
                img_loder.setVisibility(View.GONE);
            }
        });
    }

    public static void wishlistUpdate(ProductData itemMainModel,int position){

        Product_list.set(position,itemMainModel);

//        adapterProduct = new ProductListAdapter(cotext, Product_list);
//        recyProduct.setAdapter(adapterProduct);

    }

    public static void loading(boolean status){
        if (status)
        img_loder.setVisibility(View.VISIBLE);
        else
            img_loder.setVisibility(View.GONE);
    }

    public void priceRangeSelect() {

        final Dialog price_range = new Dialog(getActivity());
        price_range.requestWindowFeature(Window.FEATURE_NO_TITLE);
        price_range.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        price_range.setCancelable(true);
        price_range.setContentView(R.layout.price_range_sort);

        TextView txtOk = price_range.findViewById(R.id.txt_ok);
        TextView txtCancel = price_range.findViewById(R.id.txt_cancel);
        RangeSeekBar seekBar = price_range.findViewById(R.id.rangeSeekbar);

//        final RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(getActivity());
        seekBar.setNotifyWhileDragging(true);

        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                //Now you have the minValue and maxValue of your RangeSeekbar
//                Toast.makeText(getApplicationContext(), minValue + "-" + maxValue, Toast.LENGTH_LONG).show();
                from = minValue;
                to = maxValue;
            }
        });


        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                to=seekBar.getAbsoluteMaxValue();
//                from=seekBar.getSelectedMinValue();
                arrayCount = 0;
                pagnumber = 1;
                loader = 1;
                getProductListPricesort(from,to);
                price_range.cancel();
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price_range.cancel();
            }
        });

        price_range.show();

    }



}
