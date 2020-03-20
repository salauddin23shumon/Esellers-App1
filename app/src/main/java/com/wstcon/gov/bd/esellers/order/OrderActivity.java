package com.wstcon.gov.bd.esellers.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.interfaces.BackBtnPress;
import com.wstcon.gov.bd.esellers.order.adapter.OrderAdapter;
import com.wstcon.gov.bd.esellers.order.orderModel.OrderDetails;


public class OrderActivity extends AppCompatActivity implements BackBtnPress, OrderAdapter.OrderDetailsClick,
        OrderDetailsFragment.BackToOrderList {

    private static final String TAG = "OrderActivity ";
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        fragment=new OrderListFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackPressed() {
//        finish();
//        super.onBackPressed();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//        Log.d(TAG, "onBackPressed: "+count);
//        if (count == 0) {
//            Log.d(TAG, "onBackPressed: if called");
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
//
//            //additional code
//        } else {
//            Log.d(TAG, "onBackPressed: else called");
//
//            super.onBackPressed();
////                        getSupportFragmentManager().popBackStack();
////            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
////            finish();
//        }
    }

    @Override
    public void onBackBtnPress() {
        finish();
    }

    @Override
    public void onDetailsClick(OrderDetails details) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("details",details);
        Fragment fragment=new OrderDetailsFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        Log.d(TAG, "onDetailsClick: clicked");
    }

    @Override
    public void onBackToListClick() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
