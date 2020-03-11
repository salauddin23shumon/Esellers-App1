package com.wstcon.gov.bd.esellers.mainApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.dashboard.HomeFragment;
import com.wstcon.gov.bd.esellers.dashboard.StartSplashFragment;
import com.wstcon.gov.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.VerticalModel;
import com.wstcon.gov.bd.esellers.product.ProductFragment;
import com.wstcon.gov.bd.esellers.userAuth.AuthActivity;
import com.wstcon.gov.bd.esellers.utility.Converter;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProfileFragment.Logout,
        ProfileFragment.ProfileOpen, StartSplashFragment.SplashAction, AddorRemoveCallbacks {

    private Fragment fragment;
    private int cart_count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment =new StartSplashFragment();

        if (savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this,cart_count,R.drawable.ic_shopping_cart_white_24dp));
        MenuItem menuItem2 = menu.findItem(R.id.notification_action);
        menuItem2.setIcon(Converter.convertLayoutToImage(MainActivity.this,2,R.drawable.ic_notifications_white_24dp));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onUserLogout() {
        startActivity(new Intent(MainActivity.this, AuthActivity.class));
        finish();
    }

    @Override
    public void onProfile() {
        fragment =new ProductFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onSplashFinished(List<VerticalModel> vmList ) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("product", (Serializable) vmList);
        fragment=new HomeFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onAddProduct() {
        cart_count++;
        invalidateOptionsMenu();
        Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveProduct() {
        cart_count--;
        invalidateOptionsMenu();
        Toast.makeText(this, "removed", Toast.LENGTH_SHORT).show();
    }
}
