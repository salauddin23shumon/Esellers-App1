package com.wstcon.gov.bd.esellers.dashboard;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.database.DatabaseQuery;
import com.wstcon.gov.bd.esellers.mainApp.adapter.MixedAdapter;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.HorizontalModel;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.RecyclerViewItem;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.Slider;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.VerticalModel;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;
import com.wstcon.gov.bd.esellers.product.productModel.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartSplashFragment extends Fragment {

    private String TAG ="StartSplashFragment";
    private SplashAction action;
    private ArrayList<VerticalModel> vmList=new ArrayList<>();
    private DatabaseQuery query;
    private int size=0;
    private Context context;



    public StartSplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        query=new DatabaseQuery(context);
        action= (SplashAction) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fetchSlider();

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
        Call<List<Slider>> call = RetrofitClient.getInstance().getApiInterface().getSlider();
        call.enqueue(new Callback<List<Slider>>() {
            @Override
            public void onResponse(Call<List<Slider>> call, Response<List<Slider>> response) {

                Log.e(TAG, "onResponse: " + response.body().size());
                size=response.body().size();
                if (query.getRowCount()<size) {
                    query.deleteAll();
                    for (Slider s : response.body()) {
                        getSliderPhoto(s);
                    }
                }else {
                    addData();
                }
            }

            @Override
            public void onFailure(Call<List<Slider>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }

    public void getSliderPhoto(final Slider slider){

        Glide.with(context).asBitmap().load(slider.getSliderImage()).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                slider.setBitmap(resource);
                Log.e(TAG, "onResourceReady: "+slider.getSliderImage() );
                query.insertSlider(slider);

                Log.e(TAG, "onResourceReady: "+query.getRowCount() );

                if (query.getRowCount()==size){
                    addData();
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

    }

    private void addData() {

        final ArrayList<HorizontalModel> horizontalModels1 = new ArrayList<>();
        final ArrayList<HorizontalModel> horizontalModels2 = new ArrayList<>();
        final ArrayList<HorizontalModel> horizontalModels3 = new ArrayList<>();
        final ArrayList<HorizontalModel> horizontalModels4 = new ArrayList<>();

        Call<List<Product>> call = RetrofitClient.getInstance().getApiInterface().getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> products = response.body();
                Log.e(TAG, "onResponse: " + products.size());
                for (Product p : products) {
                    if (p.getStatus().equals("Featured"))
                        horizontalModels1.add(new HorizontalModel(p));
                    else if (p.getStatus().equals("Offer"))
                        horizontalModels2.add(new HorizontalModel(p));
                    else if (p.getStatus().equals("Latest"))
                        horizontalModels3.add(new HorizontalModel(p));
                    else if (p.getStatus().equals("Popular"))
                        horizontalModels4.add(new HorizontalModel(p));
                }

                vmList.add(new VerticalModel(horizontalModels1));
                vmList.add(new VerticalModel(horizontalModels2));
                vmList.add(new VerticalModel(horizontalModels3));
                vmList.add(new VerticalModel(horizontalModels4));

                action.onSplashFinished( vmList);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }

    public interface SplashAction{
        void onSplashFinished(List<VerticalModel> vmList );
    }

}
