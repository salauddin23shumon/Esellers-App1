package com.wstcon.gov.bd.esellers.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;
import com.wstcon.gov.bd.esellers.order.adapter.OrderAdapter;
import com.wstcon.gov.bd.esellers.order.orderModel.CustomerOrder;
import com.wstcon.gov.bd.esellers.order.orderModel.OrderHistoryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private static final String TAG = "OrderActivity ";
    private RecyclerView orderRV;
    private String cid;
    private List<CustomerOrder> orderList;
    private OrderAdapter adapter;
    private Toolbar toolbar;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        toolbar = findViewById(R.id.toolbar);

        prefs = this.getSharedPreferences("Session", MODE_PRIVATE);
        cid = prefs.getString("ID", "No ID defined");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Order History");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orderList=new ArrayList<>();
        adapter=new OrderAdapter(orderList, this);

        orderRV=findViewById(R.id.orderRV);
        orderRV.setHasFixedSize(true);
        orderRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        orderRV.setAdapter(adapter);

        getOrderHistory(cid);
    }

    private void getOrderHistory(String cid) {
        Call<OrderHistoryResponse> call= RetrofitClient.getInstance().getApiInterface().getOrder(Integer.parseInt(cid));
        call.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        orderList=response.body().getCustomerOrders();
                        adapter.updateList(orderList);
                        Log.e(TAG, "onResponse: "+orderList.size() );
                    }else
                        Toast.makeText(OrderActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }else
                    Log.e(TAG, "onResponse: "+response.code() );
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
