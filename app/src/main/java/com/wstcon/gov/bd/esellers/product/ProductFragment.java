
package com.wstcon.gov.bd.esellers.product;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.database.DatabaseQuery;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.HorizontalModel;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.RecyclerViewItem;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.SliderImage;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.VerticalModel;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;
import com.wstcon.gov.bd.esellers.product.adapter.ProductAdapter;
import com.wstcon.gov.bd.esellers.product.productModel.Product;
import com.wstcon.gov.bd.esellers.product.productModel.ProductResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private static final String TAG = "ProductFragment ";
    private TextView outputTV, outputTV2;
    private RecyclerView recyclerView;
    private Context context;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private ArrayList<RecyclerViewItem> items;
    private ArrayList<VerticalModel> vmList = new ArrayList<>();
    private int cid;

    public ProductFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cid = bundle.getInt("catId");
        }
        Log.e(TAG, "onAttach: " + cid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerView = view.findViewById(R.id.productRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        items = new ArrayList<>();
        productAdapter = new ProductAdapter(items, context);
        getData(cid);
        recyclerView.setAdapter(productAdapter);
//        outputTV=view.findViewById(R.id.outputTV);
//        outputTV2=view.findViewById(R.id.outputTV2);

//        showData();

        return view;
    }

    private void getData(int cid) {
        final ArrayList<HorizontalModel> horizontalModels = new ArrayList<>();
        Call<ProductResponse> call = RetrofitClient.getInstance().getApiInterface().getProductsByCat(cid);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        productList = response.body().getProducts();
                        Log.e(TAG, "onResponse: " + productList.size());
                        for (Product p : productList) {
                            horizontalModels.add(new HorizontalModel(p));
                        }

                        vmList.add(new VerticalModel(horizontalModels));

                        Log.e(TAG, "onResponse: " + vmList.size()+" "+horizontalModels.size());
                        productAdapter.updateList(vmList);
                    }
                } else
                    Log.e(TAG, "onResponse else: " + response.code());
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
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
//        Call<CategoryResponse> categoriesCall=RetrofitClient.getInstance().getApiInterface().getAllCategories();
//        categoriesCall.enqueue(new Callback<CategoryResponse>() {
//            @Override
//            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
//                CategoryResponse categories=response.body();
//                if (categories!=null && categories.getStatus()==1){
//                   Category category=categories.getCategories().get(1);
//                    outputTV2.setText(category.getCategoryName());
//                    Log.e("cat", "onResponse: "+categories.getCategories().size() );
//                    Log.d("", "onResponse: "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CategoryResponse> call, Throwable t) {
//
//            }
//        });
//    }
}
