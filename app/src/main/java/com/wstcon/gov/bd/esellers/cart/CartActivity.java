package com.wstcon.gov.bd.esellers.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.cartModel.Cart;
import com.wstcon.gov.bd.esellers.dashboard.HomeFragment;
import com.wstcon.gov.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.wstcon.gov.bd.esellers.interfaces.NavBackBtnPress;
import com.wstcon.gov.bd.esellers.mainApp.MainActivity;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.cartSet;
import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.grandTotalPlus;
import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.cart_count;



public class CartActivity extends AppCompatActivity implements CartListFragment.CartFrgmntAction,
        NavBackBtnPress, AddorRemoveCallbacks, PaymentFragment.PaymentFrgmntAction {

    private static final String TAG = "CartActivity";
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        fragment = new CartListFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            grandTotalPlus = 0;
            MainActivity.cart_count = (cartSet.size());
            finish();
            super.onBackPressed();
        } else {
            grandTotalPlus = 0;
            MainActivity.cart_count = (cartSet.size());
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onAddProduct(Cart cart) {

    }

    @Override
    public void onRemoveProduct(Cart cart) {
        if (cartSet.size() == 1) {
            cartSet.clear();
            invalidateOptionsMenu();
        } else {
            for (Iterator<Cart> iterator = cartSet.iterator(); iterator.hasNext(); ) {
                if (iterator.next().getProductId().equals(cart.getProductId()))
                    iterator.remove();
            }
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onPlaceOrderClick(double total) {
        grandTotalPlus = 0;
        MainActivity.cart_count = (cartSet.size());
        Bundle bundle=new Bundle();
        bundle.putDouble("total", total);
        Fragment fragment = new PaymentFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onNavBackBtnPress() {
        onBackPressed();
    }

    @Override
    public void onOrderSuccessfullyPlaced() {
        cart_count = 0;
        grandTotalPlus = 0;
        cartSet.clear();
        invalidateOptionsMenu();
        finish();
//        Fragment fragment=new HomeFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
