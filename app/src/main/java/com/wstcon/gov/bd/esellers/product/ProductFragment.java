
package com.wstcon.gov.bd.esellers.product;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private TextView outputTV, outputTV2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product,container,false);
        outputTV=view.findViewById(R.id.outputTV);
        outputTV2=view.findViewById(R.id.outputTV2);

//        showData();

        return view;
    }

//    private void showData() {
//        Call<Products>call= RetrofitClient.getInstance().getApiInterface().getAllProducts();
//        call.enqueue(new Callback<Products>() {
//            @Override
//            public void onResponse(Call<Products> call, Response<Products> response) {
//                if (response.isSuccessful()){
//                    Products products=response.body();
//                    if (products!=null && products.getStatus()==1){
//                        Product product=products.getProducts().get(1);
//                        outputTV.setText(product.getProductName());
//                        Log.d("", "onResponse: "+products.getProducts().size());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Products> call, Throwable t) {
//
//            }
//        });
//
//
//        Call<Categories> categoriesCall=RetrofitClient.getInstance().getApiInterface().getAllCategories();
//        categoriesCall.enqueue(new Callback<Categories>() {
//            @Override
//            public void onResponse(Call<Categories> call, Response<Categories> response) {
//                Categories categories=response.body();
//                if (categories!=null && categories.getStatus()==1){
//                   Category category=categories.getCategories().get(1);
//                    outputTV2.setText(category.getCategoryName());
//                    Log.e("cat", "onResponse: "+categories.getCategories().size() );
//                    Log.d("", "onResponse: "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Categories> call, Throwable t) {
//
//            }
//        });
//    }
}
