package com.shopping.basket.API;

import com.shopping.basket.Model.AddressListModel.AddressListModel;
import com.shopping.basket.Model.BannerModel.Banner;
import com.shopping.basket.Model.CartModel.CardAddModel.AddCartModel;
import com.shopping.basket.Model.CartModel.CartListModel.CartListModel;
import com.shopping.basket.Model.CartUpdateModel.CartUpdateModel;
import com.shopping.basket.Model.CategoryModel.HomeCategoryModel;
import com.shopping.basket.Model.CheckoutPriceModel.CheckoutPriceModel;
import com.shopping.basket.Model.CityListModel.CityListModel;
import com.shopping.basket.Model.ContactUsModel.ContactUsModel;
import com.shopping.basket.Model.DeliveryTimeModel.DeliveryTimeModel;
import com.shopping.basket.Model.FaqModel.FaqModel;
import com.shopping.basket.Model.ForgotPasswordModel.ForgotPasswordModel;
import com.shopping.basket.Model.LatestArrivalModel.LatestProductModel;
import com.shopping.basket.Model.LoginModel.LoginModel;
import com.shopping.basket.Model.OrderDetailsModel.OrderDetailsModel;
import com.shopping.basket.Model.OrderHistoryModel.OrderHistoryModel;
import com.shopping.basket.Model.PlaceOrderModel.PlaceOrderModel;
import com.shopping.basket.Model.ProductDetailsModel.ProductDetailsModel;
import com.shopping.basket.Model.ProductListModel.ProductListModel;
import com.shopping.basket.Model.ProfileListModel.ProfileLisModel;
import com.shopping.basket.Model.ProfileUpdateModel.ProfileUpdateModel;
import com.shopping.basket.Model.RegisterModel.SignUpModel;
import com.shopping.basket.Model.RelatedProduct.RelateProUctMoEl;
import com.shopping.basket.Model.ReviewAddModel.ReviewAddModel;
import com.shopping.basket.Model.ReviewListModel.ReviewListModel;
import com.shopping.basket.Model.WishListModel.AddWishList.WishListAddModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

//    @POST("api/test")
//    Call<CategoryModel> test();

    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> Login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("signUp")
    Call<SignUpModel> SignUp(@Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("phone") String phone,@Field("city") int city,@Field("building_no") String building_no, @Field("zone") String zone,@Field("street") String street, @Header("lang")String selectedLang);

    @FormUrlEncoded
    @POST("fbLogin")
    Call<LoginModel> FbLogin(@Field("fb_user_id") String fb_user_id, @Field("fb_token") String fb_token);

    @FormUrlEncoded
    @POST("gmailLogin")
    Call<LoginModel> GpLogin(@Field("gmail_user_id") String fb_user_id, @Field("gmail_token") String fb_token);

    @FormUrlEncoded
    @POST("getCategories")
    Call<HomeCategoryModel> getCategory(@Field("parentCategoryID") int id);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> getProductList(@Field("page") int pag_number,@Field("category_id") int id);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> getProductListprice_sort(@Field("page") int pag_number,@Field("category_id") int id,@Field("price_from") int price_from,@Field("price_to") int price_to);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> getProductList_prices_ortLogin(@Field("page") int pag_number,@Field("category_id") int id,@Field("price_from") int price_from,@Field("price_to") int price_to,@Header("token")String userToken);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> getProduct_sort(@Field("page") int pag_number,@Field("price_filter") String price_filter,@Field("category_id") int id);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> getProduct_sortLogin(@Field("page") int pag_number,@Field("price_filter")String price_filter,@Field("category_id") int id,@Header("token")String userToken);

    @FormUrlEncoded
    @POST("getHomeProducts")
    Call<ProductListModel> getBestseller(@Field("slug") String slug);

    @FormUrlEncoded
    @POST("getHomeProducts")
    Call<ProductListModel> getBestsellerLogin(@Field("slug") String slug,@Header("token")String userToken);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> getProductListLogin(@Field("page") int pag_number,@Field("category_id") int id,@Header("token")String userToken);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> getLatestLogin(@Field("page") int pag_number,@Field("slug") String slug,@Field("category_id") int id,@Header("token")String userToken);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> getLatest(@Field("page") int pag_number,@Field("slug") String slug,@Field("category_id") int id);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> getRelatedProductListLogin(@Field("page") int pag_number,@Field("category_id") int id,@Field("product_id") int product_id,@Header("token")String userToken);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> getRelatedProductList(@Field("page") int pag_number,@Field("category_id") int id,@Field("product_id") int product_id);


    @GET("getBasicInfo")
    Call<LoginModel> getProfile(@Header("token")String userToken);

    @GET("getBanners")
    Call<Banner> getBanner();

    @GET("/api/related-products/{productId}")
    Call<RelateProUctMoEl> getRelatedProduct(@Path("productId") int id, @Header("lang")String selectedLang);
//
    @FormUrlEncoded
    @POST("productDetails")
    Call<ProductDetailsModel> getProductDetails(@Field("product_id") int product_id);

    @FormUrlEncoded
    @POST("productDetails")
    Call<ProductDetailsModel> getProductDetailsLogin(@Field("product_id") int product_id,@Header("token") String userToken);

    @GET("favorites")
    Call<ProductListModel> getWishList(@Header("token")String userToken);

    @POST("getFaq")
    Call<FaqModel> getFaq();

    @FormUrlEncoded
    @POST("getFaq")
    Call<FaqModel> getPrivacy(@Field("slug") String slug);

    @FormUrlEncoded
    @POST("addFavorites")
    Call<WishListAddModel> addWishList(@Field("product_id") int product_id, @Header("token") String userToken);
//
    @GET("listCart")
    Call<CartListModel> getCart(@Header("token")String userToken);
//
    @FormUrlEncoded
    @POST("updateCartQuantity")
    Call<CartUpdateModel> cartUpdate(@Field("cart_id") int id, @Field("quantity") int quantity, @Header("token")String userToken);
//
    @FormUrlEncoded
    @POST("removeCart")
    Call<CartUpdateModel> cartDelete(@Field("cart_id") int id, @Header("token")String userToken);

    @FormUrlEncoded
    @POST("addCart")
    Call<AddCartModel> addToCart(@Field("product_id") int product, @Field("quantity") int quantity, @Header("token")String userToken);

    @FormUrlEncoded
    @POST("reviewsubmit")
    Call<ReviewAddModel> postReview(@Field("product_id") int product_id, @Field("review") String review, @Field("title") String title, @Field("rating") float rating, @Header("token") String userToken);

//    @FormUrlEncoded
//    @POST("updateBasicInfo")
//    Call<ProfileUpdateModel> updateProfile(@Field("name") String name, @Field("phone") String phone, @Header("token")String token);


    @Multipart
    @POST("updateBasicInfo")
    Call<LoginModel> updateProfile(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("city") RequestBody city,
            @Part("building_no") RequestBody building_no,
            @Part("zone") RequestBody zone,
            @Part("street") RequestBody street,
            @Part MultipartBody.Part Profile_image,
            @Header("token")String userToken);

    @FormUrlEncoded
    @POST("contact_us")
    Call<ContactUsModel> contactUs(@Field("name") String name, @Field("email") String email, @Field("message")String message);

    @FormUrlEncoded
    @POST("listReviews")
    Call<ReviewListModel> getReviwList(@Field("product_id") int product_id);

    @FormUrlEncoded
    @POST("productListing")
    Call<ProductListModel> search(@Field("page") int pag_number,@Field("keywords") String keywords);

    @FormUrlEncoded
    @POST("changePassword")
    Call<CartUpdateModel> changePassword(@Field("oldPassword") String oldPassword,@Field("newPassword") String newPassword,@Field("confirmPassword") String confirmPassword,@Header("token") String token);

    @FormUrlEncoded
    @POST("placeOrder")
    Call<PlaceOrderModel> placeOrder(@Field("shipping_name") String shipping_name, @Field("shipping_phone") String shipping_phone, @Field("shipping_email") String shipping_email, @Field("shipping_address") String shipping_address, @Field("payment_method") String payment_method, @Field("shipping_city") String shipping_city,@Field("delivery_time") String delivery_time,@Field("shipping_building_no") String shipping_building_no,@Field("shipping_zone") String shipping_zone,@Field("shipping_street") String shipping_street, @Header("token") String token);

    @FormUrlEncoded
    @POST("addShippingAddress")
    Call<CartListModel> addAddress(@Field("shipping_name") String shipping_name, @Field("shipping_phone") String shipping_phone, @Field("shipping_email") String shipping_email, @Field("shipping_address") String shipping_address, @Field("shipping_city_id") int shipping_city_id,@Field("building_no") String building_no,@Field("zone") String zone,@Field("street") String street, @Header("token") String token);

    @GET("listShippingAddress")
    Call<AddressListModel> addressList(@Header("token")String userToken);

    @FormUrlEncoded
    @POST("editShippingAddress")
    Call<CartListModel> editAddress(@Field("shipping_name") String shipping_name, @Field("shipping_phone") String shipping_phone, @Field("shipping_email") String shipping_email, @Field("shipping_address") String shipping_address,@Field("address_id") int address_id, @Field("shipping_city_id") int shipping_city_id,
                                    @Field("building_no")int building_no,@Field("zone")int zone, @Field("street")int street, @Header("token") String token);

    @GET("orderHistory")
    Call<OrderHistoryModel> getOrderHistory(@Header("token")String userToken);

    @FormUrlEncoded
    @POST("orderDetails")
    Call<OrderDetailsModel> getOrderDetilsa(@Field("order_number")String order_number,@Header("token")String userToken);

    @FormUrlEncoded
    @POST("deleteShippingAddress")
    Call<CartUpdateModel> deletAddress(@Field("address_id")int order_number,@Header("token")String userToken);

    @GET("getSubBanners")
    Call<LatestProductModel> getLatestArival();

    @GET("citysList")
    Call<CityListModel> getCityList();

    @GET("deliveryTime")
    Call<DeliveryTimeModel> getTime();


    @FormUrlEncoded
    @POST("updateBasicInfo")
    Call<LoginModel> socialLogin(@Field("name") String name, @Field("phone") String phone,@Field("email") String email,@Field("city") int city,@Field("building_no") String building_no,@Field("zone") String zone,@Field("street") String street,@Header("token") String token);

    @FormUrlEncoded
    @POST("getCheckOutAmount")
    Call<CheckoutPriceModel> getPrice(@Field("city_id") int city_id, @Header("token") String token);

    @FormUrlEncoded
    @POST("forgot")
    Call<ForgotPasswordModel> fogotPassword(@Field("email") String city_id);

}




