package com.wstcon.gov.bd.esellers.networking;

import com.wstcon.gov.bd.esellers.mainApp.Users;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.Slider;
import com.wstcon.gov.bd.esellers.product.productModel.Product;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.AuthResponse;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.LogoutResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("auth/user_register")
    Call<AuthResponse> createUser(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/user_login")
    Call<AuthResponse> userSignin(@Field("email") String email, @Field("password") String password);


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("auth/user_profile")
    Call<Users> getUserProfile();

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("auth/logout")
    Call<LogoutResponse> userLogout();

//
//    @GET("product/show_product")
//    Call<Products> getAllProducts();
//
//
//    @GET("category/show_category")
//    Call<Categories> getAllCategories();



    //////////////////////////*****///////////////////////


    @GET("getslider.php")
    Call<List<Slider>> getSlider();

    @GET("getproductsview.php")
    Call<List<Product>> getProducts();


}
