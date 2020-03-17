package com.wstcon.gov.bd.esellers.mainApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.CartActivity;
import com.wstcon.gov.bd.esellers.cart.cartModel.Cart;
import com.wstcon.gov.bd.esellers.interfaces.CategoryListener;
import com.wstcon.gov.bd.esellers.interfaces.SeeProductDetails;
import com.wstcon.gov.bd.esellers.mainApp.adapter.MixedAdapter;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.HorizontalModel;
import com.wstcon.gov.bd.esellers.product.ProductDetailsActivity;
import com.wstcon.gov.bd.esellers.product.ProductDetailsFragment;
import com.wstcon.gov.bd.esellers.userProfile.ProfileFragment;
import com.wstcon.gov.bd.esellers.userProfile.userModel.Users;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.Logout,
        ProfileFragment.ProfileOpen, StartSplashFragment.SplashAction, AddorRemoveCallbacks, SeeProductDetails, CategoryListener {

    private static final String TAG = "MainActivity ";
    private TextView nameTV, emailTV;
    private LinearLayout userLoginLinearLayout;
    private CircleImageView profile_img;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Fragment fragment;
    private List<VerticalModel> vmList=new ArrayList<>();
    private Users user;
    private Cart shoppingCart;

    public static List<Cart> globalCartList = new ArrayList<>();
    public static int cart_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(TAG, "onCreate: called");

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setViewForNavHeader();
        chkProfile();

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fragment = new StartSplashFragment();

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }

        userLoginLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
            }
        });

        setDrawerView(user);

    }

    private boolean chkProfile() {
        return false;

    }

    private void setDrawerView(Users users) {

    }

    private void setViewForNavHeader() {
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        nameTV = header.findViewById(R.id.nav_usernameTV);
        emailTV = header.findViewById(R.id.nav_user_emailTV);
        profile_img = header.findViewById(R.id.nav_user_photoIV);
        userLoginLinearLayout = header.findViewById(R.id.nav_loginLL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.cart3));
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
        this.vmList=vmList;
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
        Log.e(TAG, "onAddProduct: " + globalCartList.size() + " " + cart.getProductName());
        Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", (Serializable) vmList);
                fragment = new HomeFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                break;
            case R.id.nav_men:
                onCatIconClick(1);
                break;
            case R.id.nav_women:
                onCatIconClick(2);
                break;
            case R.id.nav_kids:
                onCatIconClick(3);
                break;
            case R.id.nav_cosmetics:
                onCatIconClick(4);
                break;
            case R.id.nav_bag:
                onCatIconClick(5);
                break;
            case R.id.nav_home_app:
                onCatIconClick(6);
                break;
            case R.id.nav_mobile:
                onCatIconClick(7);
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_policy:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about:
                Toast.makeText(this, "send", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.nav_profile:
//                Toast.makeText(this, "send", Toast.LENGTH_SHORT).show();
//                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        navigationView.getMenu().getItem(0).setChecked(false);
        return false;
    }

    @Override
    public void onCatIconClick(int cid) {
        Bundle bundle=new Bundle();
        bundle.putInt("catId",cid);
        fragment=new ProductFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }


    @Override
    public void onProductClick(HorizontalModel model) {
//        Bundle bundle=new Bundle();
//        bundle.putSerializable("product",model);
//        fragment=new ProductDetailsFragment();
//        fragment.setArguments(bundle);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        startActivity(new Intent(MainActivity.this, ProductDetailsActivity.class).putExtra("product", model));
    }
}
