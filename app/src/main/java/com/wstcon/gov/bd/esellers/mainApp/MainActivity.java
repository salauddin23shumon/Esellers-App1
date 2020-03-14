package com.wstcon.gov.bd.esellers.mainApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.CartActivity;
import com.wstcon.gov.bd.esellers.cart.adapter.CartAdapter;
import com.wstcon.gov.bd.esellers.cart.cartModel.Cart;
import com.wstcon.gov.bd.esellers.dashboard.HomeFragment;
import com.wstcon.gov.bd.esellers.dashboard.StartSplashFragment;
import com.wstcon.gov.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.VerticalModel;
import com.wstcon.gov.bd.esellers.product.ProductFragment;
import com.wstcon.gov.bd.esellers.userAuth.AuthActivity;
import com.wstcon.gov.bd.esellers.utility.Converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProfileFragment.Logout,
        ProfileFragment.ProfileOpen, StartSplashFragment.SplashAction, AddorRemoveCallbacks{

    private static final String TAG = "MainActivity ";
    private Fragment fragment;
    private Cart shoppingCart;

    public static List<Cart> globalCartList = new ArrayList<>();
    public static int cart_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(TAG, "onCreate: called" );

        fragment = new StartSplashFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.ic_shopping_cart_white_24dp));
        MenuItem menuItem2 = menu.findItem(R.id.notification_action);
        menuItem2.setIcon(Converter.convertLayoutToImage(MainActivity.this, 0, R.drawable.ic_notifications_white_24dp));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart_action:
                if (cart_count < 1) {
                    Toast.makeText(this, "there is no item in cart", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, CartActivity.class));
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onUserLogout() {
        startActivity(new Intent(MainActivity.this, AuthActivity.class));
        finish();
    }

    @Override
    public void onProfile() {
        fragment = new ProductFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onSplashFinished(List<VerticalModel> vmList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", (Serializable) vmList);
        fragment = new HomeFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onAddProduct(Cart cart) {
        cart_count++;
        invalidateOptionsMenu();
        globalCartList.add(cart);
        Log.e(TAG, "onAddProduct: " + globalCartList.size()+" "+cart.getProductName() );
        Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onRemoveProduct(int id) {
//        cart_count--;
//        invalidateOptionsMenu();
//        Toast.makeText(this, "removed", Toast.LENGTH_SHORT).show();
//
//        if (globalCartList.size() == 1) {
//            globalCartList.clear();
//            Log.e(TAG, "onClick: 1st if clicked" );
//        }
//
//        if (globalCartList.size() > 0) {
//            for(Iterator<Cart> iterator = globalCartList.iterator(); iterator.hasNext(); ) {
//                if(iterator.next().getProductId() == id)
//                    iterator.remove();
//            }
//
//            Log.e(TAG, "onClick: 2nd "+globalCartList.size() );
//
//        }
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        invalidateOptionsMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }
}
