package com.wstcon.gov.bd.esellers.dashboard;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.category.categoryModel.Category;
import com.wstcon.gov.bd.esellers.category.categoryModel.CategoryResponse;
import com.wstcon.gov.bd.esellers.database.DatabaseQuery;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.HorizontalModel;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.SliderImage;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.SliderResponse;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.VerticalModel;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;
import com.wstcon.gov.bd.esellers.product.productModel.Product;
import com.wstcon.gov.bd.esellers.product.productModel.ProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wstcon.gov.bd.esellers.utility.Constant.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartSplashFragment extends Fragment {

    private String TAG = "StartSplashFragment";
    private SplashAction action;
    private ArrayList<VerticalModel> vmList = new ArrayList<>();
    private DatabaseQuery query;
    private int sliderSize = 0;
    private int catSize = 0;
    private Context context;


    public StartSplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        query = new DatabaseQuery(context);
        action = (SplashAction) context;


        if (query.doesDatabaseExist()) {
            Log.e(TAG, "onCreateView: exist");
            addData();

        } else {
            Log.e(TAG, "onCreateView: not exist");
            getCategory();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_start_splash, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
    }

    private void fetchSlider() {

        Log.d(TAG, "fetchSlider: cld");

        Call<SliderResponse> call = RetrofitClient.getInstance().getApiInterface().getSliders();
        call.enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {

                List<SliderImage> sliderImages = response.body().getSliderImages();
                Log.e(TAG, "onResponse: " + sliderImages.size());
                sliderSize = sliderImages.size();
                for (SliderImage s : sliderImages) {
                    getSliderPhoto(s);
                }

//                if (query.getSliderCount() < sliderSize) {
//                    query.deleteSlider();
//                    for (SliderImage s : sliderImages) {
//                        getSliderPhoto(s);
//                    }
//                } else {
//                    addData();
//                }
            }

            @Override
            public void onFailure(Call<SliderResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }

    private void getSliderPhoto(final SliderImage slider) {

        Log.d(TAG, "getSliderPhoto: cld");

        Glide.with(context).asBitmap().load(BASE_URL + slider.getSliderImage()).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                slider.setBitmap(resource);
//                Log.e(TAG, "onResourceReady: " + slider.getSliderImage());
                query.insertSlider(slider);


                if (query.getSliderCount() == sliderSize && query.getCatIconCount()==catSize) {
                    addData();
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

    }

    private void getCatIcon(final Category category) {

        Log.d(TAG, "getCatIcon: cld");

        Glide.with(context).asBitmap().load(BASE_URL + category.getCategoryIcon()).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                category.setBitmap(resource);
//                Log.e(TAG, "onResourceReady: " + category.getCategoryIcon());
                query.insertCategory(category);
//                if (query.getCatIconCount() == catSize) {
//                    fetchSlider();
//                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    private void addData() {

        Log.d(TAG, "addData: cld");

        final ArrayList<HorizontalModel> horizontalModels1 = new ArrayList<>();
        final ArrayList<HorizontalModel> horizontalModels2 = new ArrayList<>();
        final ArrayList<HorizontalModel> horizontalModels3 = new ArrayList<>();
        final ArrayList<HorizontalModel> horizontalModels4 = new ArrayList<>();

        Call<ProductResponse> call = RetrofitClient.getInstance().getApiInterface().getAllProducts();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                List<Product> products = response.body().getProducts();
                Log.e(TAG, "onResponse: products size " + products.size());
                for (Product p : products) {
                    if (p.getProductStatusName().equals("Featured"))
                        horizontalModels1.add(new HorizontalModel(p));
                    else if (p.getProductStatusName().equals("Offer"))
                        horizontalModels2.add(new HorizontalModel(p));
                    else if (p.getProductStatusName().equals("Latest"))
                        horizontalModels3.add(new HorizontalModel(p));
                    else if (p.getProductStatusName().equals("Popular"))
                        horizontalModels4.add(new HorizontalModel(p));
                }

                vmList.add(new VerticalModel(horizontalModels1));
                vmList.add(new VerticalModel(horizontalModels2));
                vmList.add(new VerticalModel(horizontalModels3));
                vmList.add(new VerticalModel(horizontalModels4));

                action.onSplashFinished(vmList);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }


    private void getCategory() {

        Log.d(TAG, "getCategory: cld");


        Call<CategoryResponse> call = RetrofitClient.getInstance().getApiInterface().getAllCategories();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                List<Category> categoryList = response.body().getCategories();
                catSize = categoryList.size();
                Log.e(TAG, "getCategory: " + categoryList.size());
                fetchSlider();
                for (Category c : categoryList) {
                    getCatIcon(c);
                }

//                if (query.getCatIconCount() < categoryList.size()) {
//                    query.deleteCat();
//                    for (Category c : categoryList) {
//                        getCatIcon(c);
//                    }
//                } else
//                    addData();

            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );

            }
        });
    }

    public interface SplashAction {
        void onSplashFinished(List<VerticalModel> vmList);
    }

}
