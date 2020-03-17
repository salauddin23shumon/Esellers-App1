package com.wstcon.gov.bd.esellers.networking;

import com.wstcon.gov.bd.esellers.cart.cartModel.CartRes;
import com.wstcon.gov.bd.esellers.cart.cartModel.Order;
import com.wstcon.gov.bd.esellers.category.categoryModel.CategoryResponse;
import com.wstcon.gov.bd.esellers.userProfile.userModel.ProfileUpdateRes;
import com.wstcon.gov.bd.esellers.userProfile.userModel.Users;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.SliderResponse;
import com.wstcon.gov.bd.esellers.product.productModel.ProductResponse;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.AuthResponse;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.LogoutResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("api/auth/user_register")
    Call<AuthResponse> createUser(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("api/auth/user_login")
    Call<AuthResponse> userSignin(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("api/auth/profile_update/{uid}")
    Call<ProfileUpdateRes> updateProfile(@Field("name") String name, @Field("email") String email,
                                         @Field("mobile") String mobile, @Field("user_profile_photo") String photo,
                                         @Field("address") String address, @Path("uid")  int id);    //token required


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("api/auth/user_profile")
    Call<Users> getUserProfile();

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("auth/logout")
    Call<LogoutResponse> userLogout();


    @GET("api/product/show_product")
    Call<ProductResponse> getAllProducts();


    @GET("api/product/category_product/{cid}")
    Call<ProductResponse> getProductsByCat(@Path("cid") int id);


    @GET("api/category/show_category")
    Call<CategoryResponse> getAllCategories();


    @GET("api/slider/show")
    Call<SliderResponse> getSliders();


    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/checkout/checkout")
    Call<CartRes> sendOrder(@Body Order order);


//
//    @GET("product/show_product")
//    Call<Products> getAllProducts();
//
//
//    @GET("category/show_category")
//    Call<CategoryResponse> getAllCategories();



    //////////////////////////*****///////////////////////


//    @GET("getslider.php")
//    Call<List<Slider>> getSlider();
//
//    @GET("getproductsview.php")
//    Call<List<Product>> getProducts();


}
