package com.wstcon.gov.bd.esellers.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.adapter.CartAdapter;
import com.wstcon.gov.bd.esellers.cart.cartModel.Cart;
import com.wstcon.gov.bd.esellers.mainApp.MainActivity;
import com.wstcon.gov.bd.esellers.payment.PaymentActivity;

import java.util.ArrayList;
import java.util.List;

import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.globalCartList;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    public static TextView totalTV;
    private Button orderBtn;
    private CartAdapter adapter;
    public static double grandTotalPlus;
    // create a temp list and add cartitem list
    public static List<Cart> tempArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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
        Log.d("tempArrayList:"+tempArrayList.get(0).getProductName(), String.valueOf(tempArrayList.size()));
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
                startActivity(new Intent(CartActivity.this, PaymentActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        grandTotalPlus = 0;
        globalCartList.addAll(tempArrayList);
        MainActivity.cart_count = (tempArrayList.size());
        finish();
        super.onBackPressed();
    }
}
