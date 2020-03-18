package com.wstcon.gov.bd.esellers.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.adapter.CartAdapter;
import com.wstcon.gov.bd.esellers.cart.cartModel.Address;
import com.wstcon.gov.bd.esellers.cart.cartModel.Cart;
import com.wstcon.gov.bd.esellers.cart.cartModel.CartRes;
import com.wstcon.gov.bd.esellers.cart.cartModel.Order;
import com.wstcon.gov.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.wstcon.gov.bd.esellers.mainApp.MainActivity;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.cart_count;
import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.globalCartList;

public class CartActivity extends AppCompatActivity implements AddorRemoveCallbacks {

    private static final String TAG = "CartActivity";
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    public static TextView totalTV;
    private Button orderBtn;
    private CartAdapter adapter;
    public static double grandTotalPlus;
    // create a temp list and add cartitem list
    public static List<Cart> tempArrayList;
    private SharedPreferences prefs;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        prefs = this.getSharedPreferences("Session", MODE_PRIVATE);
        id = prefs.getString("ID", "No ID defined");

        recyclerView = findViewById(R.id.cartRV);
        totalTV = findViewById(R.id.totalTV);
        orderBtn = findViewById(R.id.placeOrderBtn);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cart");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // these lines of code for show the same  cart for future refrence
                grandTotalPlus = 0;
                globalCartList.addAll(tempArrayList);
                MainActivity.cart_count = (tempArrayList.size());
                finish();
            }
        });

        tempArrayList = new ArrayList<>();
        adapter = new CartAdapter(tempArrayList,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


//        MainActivity.cart_count = 0;

        for (int i = 0; i < globalCartList.size(); i++) {
            for (int j = i + 1; j < globalCartList.size(); j++) {
                if (globalCartList.get(i).getProductId().equals(globalCartList.get(j).getProductId())) {
                    globalCartList.get(i).setProductQuantity(globalCartList.get(j).getProductQuantity());
                    globalCartList.get(i).setTotalCash(globalCartList.get(j).getTotalCash());
                    globalCartList.remove(j);
                    j--;
                    Log.d(TAG, String.valueOf(globalCartList.size()));
                }
            }
        }
        tempArrayList.addAll(globalCartList);
//        Log.d("tempArrayList:"+tempArrayList.get(0).getProductName(), String.valueOf(tempArrayList.size()));
        adapter.updateList(tempArrayList);
        globalCartList.clear();

        Log.d("sizecart_22", String.valueOf(globalCartList.size()));
        // this code is for get total cash
        for (int i = 0; i < tempArrayList.size(); i++) {
            grandTotalPlus = grandTotalPlus + tempArrayList.get(i).getTotalCash();
        }
        totalTV.setText("TK. " + String.valueOf(grandTotalPlus));
        recyclerView.setAdapter(adapter);


        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });
    }

    private void sendOrder() {

        Log.e(TAG, "sendOrder: list "+tempArrayList.size() );

        Address address=new Address();
        address.setAddress("banglamotor");
        address.setCity("dhaka");
        address.setZip(1000);
        address.setCountry("bd");
        address.setPhone("1212331232");
        address.setReceiverName("sk");


        Order order=new Order();
        order.setCustomerId(Integer.parseInt(id));
        order.setOrderTotal(grandTotalPlus);
        order.setPaymentType("COD");
        order.setHasDifferentShipping(true);
        order.setCart(tempArrayList);
        order.setAddress(address);


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(String.valueOf(order));
        String prettyJsonString = gson.toJson(je);
        System.out.println(prettyJsonString);

        Call<CartRes>call= RetrofitClient.getInstance().getApiInterface().sendOrder(order);
        call.enqueue(new Callback<CartRes>() {
            @Override
            public void onResponse(Call<CartRes> call, Response<CartRes> response) {
                if (response.isSuccessful()){
                    Log.e(TAG, "onResponse: "+response.body().getStatus() );
                    Toast.makeText(CartActivity.this, "u have placed order successfully", Toast.LENGTH_SHORT).show();
                    clearData();
                }else
                    Log.e(TAG, "onResponse: else "+response.code() );
            }

            @Override
            public void onFailure(Call<CartRes> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
            }
        });
    }

    public void clearData(){
        cart_count=0;
        grandTotalPlus=0;
        tempArrayList.clear();
        invalidateOptionsMenu();
        finish();
    }

    @Override
    public void onBackPressed() {
        grandTotalPlus = 0;
        globalCartList.addAll(tempArrayList);
        MainActivity.cart_count = (tempArrayList.size());
        finish();
        super.onBackPressed();
    }

    @Override
    public void onAddProduct(Cart cart) {

    }

    @Override
    public void onRemoveProduct(int id) {
        cart_count--;
        invalidateOptionsMenu();
        Toast.makeText(this, "removed", Toast.LENGTH_SHORT).show();

        if (globalCartList.size() == 1) {
            globalCartList.clear();
            Log.e(TAG, "onClick: 1st if clicked" );
        }

        if (globalCartList.size() > 0) {
            for(Iterator<Cart> iterator = globalCartList.iterator(); iterator.hasNext(); ) {
                if(iterator.next().getProductId() == id)
                    iterator.remove();
            }

            Log.e(TAG, "onClick: 2nd "+globalCartList.size() );

        }
    }
}
