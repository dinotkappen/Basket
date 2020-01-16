package com.shopping.basket.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Activity.LoginActivity;
import com.shopping.basket.Activity.MainActivity;
import com.shopping.basket.Adapter.ProductAdapter.ProductListAdapter;
import com.shopping.basket.Adapter.ProductDetails.RelatedProductAdapter;
import com.shopping.basket.Adapter.ProductDetails.UserReviewAdapter;
import com.shopping.basket.Adapter.ProductDetails.ViewPagerAdapter;
import com.shopping.basket.Application;
import com.shopping.basket.Model.CartModel.CardAddModel.AddCartModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.ProductDetailsModel.ProductDetailsModel;
import com.shopping.basket.Model.ProductListModel.ProductData;
import com.shopping.basket.Model.ProductListModel.ProductListModel;
import com.shopping.basket.Model.RelatedProduct.Relate_proUct;
import com.shopping.basket.Model.ReviewAddModel.ReviewAddModel;
import com.shopping.basket.Model.ReviewListModel.Cartlist;
import com.shopping.basket.Model.ReviewListModel.ReviewListModel;
import com.shopping.basket.Model.WishListModel.AddWishList.WishListAddModel;
import com.shopping.basket.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.basket.Activity.MainActivity.getCart;
import static com.shopping.basket.Activity.MainActivity.rightBackHide;
import static com.shopping.basket.Activity.MainActivity.titlHide;
import static com.shopping.basket.Util.util.loadingAnimation;

public class ProductDetailsFragment  extends MainContanier{

    String userName;
    ViewPager viewpager;
    LoginData profile_dt;
    TextView txtReviewClick;
    int wishListStatus = 0;
    LinearLayout lenReview;
    static String strTitle;
    static int product_id;
    RatingBar ratingReview;
    static API apiInterface;
    public static int banner;
    static int flageOutStock;
    ViewPagerAdapter adapter;
    LinearLayout lenOutStock;
    CircleIndicator indicator;
    EditText edittitle,editReview;
    String userTocken,selectedLang;
    List<Relate_proUct> listPhones;
    ProductDetailsModel status = null;
    UserReviewAdapter adapter_review;
    int currentPage_Banner,pagnumber=1;
    RelatedProductAdapter adapterProduct;
    ImageView img_favorite;
    ImageView imgshare;
    static ImageView img_loder;
    FrameLayout framMinus,framPluse,framOldPrice;
    RecyclerView recyProductDetails,recyReview;
    int strQty=1,addrevieFlage=0,subCategoryId;
    private static AVLoadingIndicatorView avi;
    List<String> arrayImages = new ArrayList<>();
    List<Cartlist> listReview = new ArrayList<>();
    static List<ProductData> Product_list = new ArrayList<>();
    TextView txt1,txt2,txt3,txt4,txt5,qty;
    TextView txtTitle,txtPrice,txt_description,txt_related_product,txt_review,txtAddtoCard,txt_avarg_rating,txtPricePrevious;
public static FragmentActivity fragmentActivity;
    public static ProductDetailsFragment newInstance(int id ,String name) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        product_id = id;
        strTitle = name;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.product_details, container, false);
        subCategoryId = Hawk.get("subCategoryId",0);
        viewpager               = rootView.findViewById(R.id.pager_banner);
        indicator               = rootView.findViewById(R.id.indicator);
        recyProductDetails      = rootView.findViewById(R.id.recy_product);
        txtTitle                = rootView.findViewById(R.id.txt_tile);
        txtPrice                = rootView.findViewById(R.id.txt_price);
        lenOutStock             = rootView.findViewById(R.id.len_out_of_stock);
        txt_description         = rootView.findViewById(R.id.txt_description);
        img_favorite            = rootView.findViewById(R.id.img_favorite);
        lenReview               = rootView.findViewById(R.id.len_review);
        txt_related_product     = rootView.findViewById(R.id.txt_related_product);
        txt_review              = rootView.findViewById(R.id.txt_review);
        recyReview              = rootView.findViewById(R.id.recycler_rating);
        txtAddtoCard            = rootView.findViewById(R.id.txt_add_cart);
        avi                     =  rootView.findViewById(R.id.avi);
        txtReviewClick          =  rootView.findViewById(R.id.txt_add_review);
        txt_avarg_rating        =  rootView.findViewById(R.id.txt_rating);
        imgshare                =  rootView.findViewById(R.id.img_share);
        txtPricePrevious        =  rootView.findViewById(R.id.txt_price_previous);
        framOldPrice            =  rootView.findViewById(R.id.fram_orld_price);
        img_loder               = rootView.findViewById(R.id.img_loder);
        fragmentActivity=getActivity();
        Glide.with(getActivity())
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);
        rightBackHide(false);
        titlHide("null");

        framMinus       = rootView.findViewById(R.id.fram_minus);
        framPluse       = rootView.findViewById(R.id.fram_plus);
        qty             = rootView.findViewById(R.id.txt_qty);

        framMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                if (Integer.parseInt(qty.getText().toString())>1) {
                    qty.setText(""+(Integer.parseInt(qty.getText().toString())-1));
                }
                }catch (Exception ex)
                {
                    Log.v("framMinusError",ex.getMessage());
                }
            }
        });

        framPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (status.getSuccess().getData().getProductData().getstock() > Integer.parseInt(qty.getText().toString()))
                        qty.setText("" + (Integer.parseInt(qty.getText().toString()) + 1));
                    else {
                        Toast.makeText(Application.getContext(), "Only " + String.valueOf(Integer.parseInt(qty.getText().toString())) + " Product Available", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex)
                {
                    Log.v("framPluseError",ex.getMessage());
                }
            }
        });

        MainActivity.double_icon();
        txt1        =  rootView.findViewById(R.id.txt_1);
        txt2        =  rootView.findViewById(R.id.txt_2);
        txt3        =  rootView.findViewById(R.id.txt_3);
        txt4        =  rootView.findViewById(R.id.txt_4);
        txt5        =  rootView.findViewById(R.id.txt_5);
        Product_list.clear();

        recyReview.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter_review = new UserReviewAdapter(getActivity(), listReview);
        recyReview.setAdapter(adapter_review);

        recyProductDetails.setVisibility(View.VISIBLE);
        lenReview.setVisibility(View.GONE);
        if (Hawk.get("logStatus","0").equals("1")) {

            profile_dt = Hawk.get("user_details");
            userTocken = profile_dt.getToken();
            userName = profile_dt.getName().toString();
        }
        selectedLang= Hawk.get("selectedLang","en");
//

        txtReviewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Hawk.get("logStatus","0").equals("1")) {
                    addReview();
                }
                else {
                    Intent loign = new Intent(getContext(),LoginActivity.class);
                    startActivity(loign);
                }
            }
        });
        txt_related_product.setVisibility(View.VISIBLE);
        txt_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_related_product.setBackgroundResource(R.drawable.round_product_detal);
                txt_related_product.setTextColor(Color.parseColor("#000000"));
                txt_review.setTextColor(Color.parseColor("#ffffff"));
                txt_review.setBackgroundResource(R.drawable.round_product_detal_fill);
                recyProductDetails.setVisibility(View.GONE);
                lenReview.setVisibility(View.VISIBLE);
                getReview();
            }
        });

        imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = txtTitle.getText() + "\n" + txtPrice.getText().toString()+ "\n" +"View product here : "+"https://basket.qa" + "\n"+product_id+"\n"+subCategoryId;
                //  String shareBody = "https://www.themarket.qa";
                Log.v("shareBody", shareBody);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Welcome to The Basket.");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        txt_related_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_related_product.setBackgroundResource(R.drawable.round_product_detal_fill);
                txt_related_product.setTextColor(Color.parseColor("#ffffff"));
                txt_review.setTextColor(Color.parseColor("#000000"));
                txt_review.setBackgroundResource(R.drawable.round_product_detal);
                recyProductDetails.setVisibility(View.VISIBLE);
                lenReview.setVisibility(View.GONE);

            }
        });
        img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Hawk.get("logStatus","0").equals("1")) {
                    addWhisList();
                }
                else {
                    Intent login = new Intent(getActivity(),LoginActivity.class);
                    startActivity(login);
                }

            }
        });
        recyProductDetails.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapterProduct = new RelatedProductAdapter(getActivity(), Product_list);
        recyProductDetails.setAdapter(adapterProduct);

        txtTitle.setText(strTitle);

//        lisBanner.add(new ProducDetailsModel(banner));
//        banner_load();
        getProductDetails();

        txtAddtoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (status.getProUct().getQuantity()<1) {
//                    Toast.makeText(getContext(),getResources().getString(R.string.not_here),Toast.LENGTH_SHORT).show();
//                }
//                else {
                    if (Hawk.get("logStatus","0").equals("1")) {
                        profile_dt = Hawk.get("user_details");
                        userTocken = profile_dt.getToken();
                        userName = profile_dt.getName().toString();
                        strQty = Integer.parseInt(qty.getText().toString());
                        if (status.getSuccess().getData().getProductData().getstock()==0){
                           Toast.makeText(getContext(),getResources().getString(R.string.out_of_stock),Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (strQty!=0)
                            addToCart();
                            else {
                                Toast.makeText(getContext(),getResources().getString(R.string.out_of_stock),Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    else {
                        Intent login = new Intent(getContext(), LoginActivity.class);
                        startActivity(login);
                    }
//                }
            }
        });
        return rootView;

    }
    public void banner_load(){
        try {
            adapter = new ViewPagerAdapter(getActivity(), arrayImages);
            viewpager.setAdapter(adapter);
            indicator.setViewPager(viewpager);
//        mShimmerViewContainer.stopShimmerAnimation();
//        mShimmerViewContainer.setVisibility(View.GONE);
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage_Banner = position;

                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        img_loder.setVisibility(View.GONE);
        fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        mShimmerViewContainer.stopShimmerAnimation();
//        mShimmerViewContainer.setVisibility(View.GONE);
    }

    private void getProductDetails() {
        Log.e("product_id",String.valueOf(product_id));
        img_loder.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

//        mShimmerViewContainer.startShimmerAnimation();
        Call<ProductDetailsModel> call = null;
        if (Hawk.get("logStatus","0").equals("1")) {
            call = apiInterface.getProductDetailsLogin(product_id,userTocken);//67
        }
        else {
             call = apiInterface.getProductDetails(product_id);//67
        }
        call.enqueue(new Callback<ProductDetailsModel>() {
            @Override
            public void onResponse(Call<ProductDetailsModel> call, Response<ProductDetailsModel> response) {
//

                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("details",jsonElement.toString());
                status = gson.fromJson(jsonElement, ProductDetailsModel.class);
                txtTitle.setText(status.getSuccess().getData().getProductData().getName());
                txtPrice.setText(status.getSuccess().getData().getProductData().getPrice()+" QAR");
//                txt_avarg_rating.setText(status.getSuccess().getData().getProductData().get);
                txt_description.setText(android.text.Html.fromHtml(status.getSuccess().getData().getProductData().getDetails()));
                wishListStatus = status.getSuccess().getData().getProductData().getWishlists();
                if (Hawk.get("logStatus","0").equals("1")) {
                    if (status.getSuccess().getData().getProductData().getWishlists()==1) {
                        img_favorite.setBackgroundResource(R.drawable.favorite_round_select);
                    } else {
                        img_favorite.setBackgroundResource(R.drawable.favorite_round);
                    }
                }
                else {
                    img_favorite.setBackgroundResource(R.drawable.favorite_round);
                }
                if (status.getSuccess().getData().getProductData().getstock()==0){
                    lenOutStock.setVisibility(View.VISIBLE);
                    qty.setText("0");
                }
                else {
                    qty.setText("1");
                    lenOutStock.setVisibility(View.GONE);
                }
                if (status.getSuccess().getData().getProductData().getPreviousPrice()!=0){
                    framOldPrice.setVisibility(View.VISIBLE);
                    txtPricePrevious.setText(status.getSuccess().getData().getProductData().getPreviousPrice()+" QAR");
                }
                else {
                    framOldPrice.setVisibility(View.GONE);
                }

//                productImage = status.getImaE();
                arrayImages = status.getSuccess().getData().getPhotoGallery();
                arrayImages.add(0, status.getSuccess().getData().getProductData().getPhoto());

                banner_load();
                getProductList();


            }

            @Override
            public void onFailure(Call<ProductDetailsModel> call, Throwable t) {

            }
        });


    }
    private void addWhisList() {
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Call<WishListAddModel> call = apiInterface.addWishList(product_id,userTocken);

        call.enqueue(new Callback<WishListAddModel>() {
            @Override
            public void onResponse(Call<WishListAddModel> call, Response<WishListAddModel> response) {

                WishListAddModel addStatus; //;;= response.body();
                WishListAddModel status = new WishListAddModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("error_",jsonElement.toString());
                addStatus = gson.fromJson(jsonElement, WishListAddModel.class);

                if (addStatus.getSuccess().getStatus()==200){
                    Toast.makeText(Application.getContext1(),addStatus.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();

                    if (wishListStatus==0){
                            img_favorite.setBackgroundResource(R.drawable.favorite_round_select);
                        wishListStatus = 1;
                    }
                    else {
                        wishListStatus = 0;
                        img_favorite.setBackgroundResource(R.drawable.favorite_round);
                    }
                }
                else {
                    Toast.makeText(Application.getContext1(),addStatus.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }

//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
                fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onFailure(Call<WishListAddModel> call, Throwable t) {
//                Toast.makeText(mContext,addStatus.getMag(),Toast.LENGTH_SHORT).show();
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
                fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        });

    }



    public void getReview(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Call<ReviewListModel> call = apiInterface.getReviwList(product_id);
        call.enqueue(new Callback<ReviewListModel>() {
            @Override
            public void onResponse(Call<ReviewListModel> call, Response<ReviewListModel> response) {
                ReviewListModel listReviewSuccess;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("jsonElement",jsonElement.toString());
                listReviewSuccess = gson.fromJson(jsonElement, ReviewListModel.class);
                if (listReviewSuccess.getSuccess().getStatus()==200){
                    listReview = listReviewSuccess.getSuccess().getData().getCartlist();
                }
                txt5.setText(""+listReviewSuccess.getSuccess().getData().getRatingCount().get5());
                txt4.setText(""+listReviewSuccess.getSuccess().getData().getRatingCount().get4());
                txt3.setText(""+listReviewSuccess.getSuccess().getData().getRatingCount().get3());
                txt2.setText(""+listReviewSuccess.getSuccess().getData().getRatingCount().get2());
                txt1.setText(""+listReviewSuccess.getSuccess().getData().getRatingCount().get1());
                adapter_review = new UserReviewAdapter(getActivity(), listReview);
                recyReview.setAdapter(adapter_review);
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
                fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onFailure(Call<ReviewListModel> call, Throwable t) {
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
                fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

    }

    public void addToCart(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Call<AddCartModel> call = apiInterface.addToCart(product_id,strQty,userTocken);

        call.enqueue(new Callback<AddCartModel>() {
            @Override
            public void onResponse(Call<AddCartModel> call, Response<AddCartModel> response) {

                AddCartModel status;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("jsonElement",jsonElement.toString());
                status = gson.fromJson(jsonElement, AddCartModel.class);
                if (status.getSuccess().getStatus()==200){
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                    getCart();
                }
                else {
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
                fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }

            @Override
            public void onFailure(Call<AddCartModel> call, Throwable t) {

//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
                fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        });
    }
    
    public void addReview(){
       
            final Dialog Review = new Dialog(getActivity());
            Review.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Review.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Review.setCancelable(true);
            Review.setContentView(R.layout.add_rating);

            TextView txtNmaeRevie   = Review.findViewById(R.id.txt_name);
             ratingReview  = Review.findViewById(R.id.rating_bar);
             edittitle      =  Review.findViewById(R.id.edit_title);
             editReview     = Review.findViewById(R.id.edit_message);
            TextView addReview      = Review.findViewById(R.id.txt_send);
            TextView cancelReview   = Review.findViewById(R.id.txt_cancel);

            txtNmaeRevie.setText(userName);


        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittitle.getText().toString().equals("")) {
                    edittitle.setError(getString(R.string.field_requirend));
                    addrevieFlage = 1;
                }
                else if (editReview.getText().toString().equals("")) {
                    editReview.setError(getString(R.string.field_requirend));
                    addrevieFlage = 1;
                }
                if (addrevieFlage==0){
                    postReview();
                    Review.dismiss();
                }
                else {
                    addrevieFlage=0;
                }
            }
        });
        cancelReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review.dismiss();
            }
        });



        Review.show();

    }

    public void postReview(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Call<ReviewAddModel> call = apiInterface.postReview(product_id,editReview.getText().toString(),edittitle.getText().toString(),ratingReview.getRating(),userTocken);
        call.enqueue(new Callback<ReviewAddModel>() {
            @Override
            public void onResponse(Call<ReviewAddModel> call, Response<ReviewAddModel> response) {

                ReviewAddModel status;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                status = gson.fromJson(jsonElement, ReviewAddModel.class);

                if (status.getSuccess().getStatus()==200){
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
                fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onFailure(Call<ReviewAddModel> call, Throwable t) {
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
                fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

    }
    public void getProductList(){
//        loadingFbAniamtion(mShimmerViewContainer,true);
        Call<ProductListModel> call;
        if (Hawk.get("logStatus","0").equals("1")) {

            call = apiInterface.getRelatedProductListLogin(pagnumber,subCategoryId,product_id,userTocken);
        }
        else
            call = apiInterface.getRelatedProductList(pagnumber,subCategoryId,product_id);
        call.enqueue(new Callback<ProductListModel>() {
            @Override
            public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response) {
//                allList.clear();
                ProductListModel status ;
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("category_pkb_related",jsonElement.toString());
                status = gson.fromJson(jsonElement, ProductListModel.class);
//                allList.add(status);
//                listProductModel = status.getSuccess();

                Product_list = status.getSuccess().getData();

                adapterProduct = new RelatedProductAdapter(getActivity(), Product_list);
                recyProductDetails.setAdapter(adapterProduct);
//                loadingFbAniamtion(mShimmerViewContainer,false);
//                refresh.setRefreshing(false);
//                loadingFbAniamtion(mShimmerViewContainer,false);
            }

            @Override
            public void onFailure(Call<ProductListModel> call, Throwable t) {

                Log.e("error",t.getMessage());

            }
        });
    }
    public static void loaderAnimation(boolean status){
        if (status) {
            img_loder.setVisibility(View.VISIBLE);
            fragmentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else {
            img_loder.setVisibility(View.GONE);
            fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
    }




}
