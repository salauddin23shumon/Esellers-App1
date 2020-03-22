package com.wstcon.gov.bd.esellers.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.cartModel.Cart;
import com.wstcon.gov.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.wstcon.gov.bd.esellers.interfaces.NavBackBtnPress;
import com.wstcon.gov.bd.esellers.mainApp.MainActivity;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.grandTotalPlus;
import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.cart_count;
import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.globalCartList;


public class CartActivity extends AppCompatActivity implements CartListFragment.CartFrgmntAction,
        NavBackBtnPress, AddorRemoveCallbacks, PaymentFragment.PaymentFrgmntAction {

    private static final String TAG = "CartActivity";
    private Fragment fragment;
    public static List<Cart> tempArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        fragment = new CartListFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }

//        tempArrayList = new ArrayList<>();
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
    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            grandTotalPlus = 0;
            globalCartList.addAll(tempArrayList);
            MainActivity.cart_count = (tempArrayList.size());
            tempArrayList.clear();
            finish();
            super.onBackPressed();
            //additional code
        } else {
            grandTotalPlus = 0;
            globalCartList.addAll(tempArrayList);
            MainActivity.cart_count = (tempArrayList.size());
            getSupportFragmentManager().popBackStack();
        }

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
            Log.e(TAG, "onClick: 1st if clicked");
        }

        if (globalCartList.size() > 1) {
            for (Iterator<Cart> iterator = globalCartList.iterator(); iterator.hasNext(); ) {
                if (iterator.next().getProductId() == id)
                    iterator.remove();
            }

            Log.e(TAG, "onClick: 2nd " + globalCartList.size());

        }
    }

    @Override
    public void onPlaceOrderClick() {
        grandTotalPlus = 0;
        globalCartList.addAll(tempArrayList);
        MainActivity.cart_count = (tempArrayList.size());
        Fragment fragment = new PaymentFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onNavBackBtnPress() {
//        grandTotalPlus = 0;
//        globalCartList.addAll(tempArrayList);
//        MainActivity.cart_count = (tempArrayList.size());
//        finish();
        onBackPressed();
    }

    @Override
    public void onOrderSuccessfullyPlaced() {
        cart_count = 0;
        grandTotalPlus = 0;
        tempArrayList.clear();
        invalidateOptionsMenu();
        finish();
    }
}
