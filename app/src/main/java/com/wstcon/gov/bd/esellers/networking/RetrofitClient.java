package com.wstcon.gov.bd.esellers.networking;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.wstcon.gov.bd.esellers.utility.Constant.BASE_URL;

public class RetrofitClient {
//    public static final String BASE_URL = "https://esellers.againwish.com/api/";
//    public static final String BASE_URL = "http://192.168.0.103/searching/";
    private static Retrofit retrofit;
    private static RetrofitClient retrofitClient;
//    private String token;


    private RetrofitClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
                .build();
    }

    private RetrofitClient(final String token){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "bearer"+token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();



        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public static synchronized RetrofitClient getInstance(String token) {
//        if (retrofitClient == null) {
//            retrofitClient = new RetrofitClient(token);
//        }
        return new RetrofitClient(token);
    }

    public ApiInterface getApiInterface() {
        return retrofit.create(ApiInterface.class);
    }


}
