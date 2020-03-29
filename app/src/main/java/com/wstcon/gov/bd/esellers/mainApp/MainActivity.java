package com.wstcon.gov.bd.esellers.mainApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.CartActivity;
import com.wstcon.gov.bd.esellers.cart.cartModel.Cart;
import com.wstcon.gov.bd.esellers.category.categoryModel.Category;
import com.wstcon.gov.bd.esellers.dashboard.StartSplashFragment;
import com.wstcon.gov.bd.esellers.database.DatabaseQuery;
import com.wstcon.gov.bd.esellers.interfaces.CategoryListener;
import com.wstcon.gov.bd.esellers.interfaces.SeeProductDetails;
import com.wstcon.gov.bd.esellers.interfaces.ShowHideIconListener;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;
import com.wstcon.gov.bd.esellers.order.OrderActivity;
import com.wstcon.gov.bd.esellers.product.ProductDetailsFragment;
import com.wstcon.gov.bd.esellers.product.productModel.Product;
import com.wstcon.gov.bd.esellers.userAuth.SessionManager;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.LogoutResponse;
import com.wstcon.gov.bd.esellers.userProfile.ProfileActivity;
import com.wstcon.gov.bd.esellers.dashboard.HomeFragment;
import com.wstcon.gov.bd.esellers.interfaces.AddorRemoveCallbacks;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.VerticalModel;
import com.wstcon.gov.bd.esellers.product.ProductFragment;
import com.wstcon.gov.bd.esellers.userAuth.AuthActivity;
import com.wstcon.gov.bd.esellers.utility.Converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wstcon.gov.bd.esellers.userAuth.SessionManager.EMAIL;
import static com.wstcon.gov.bd.esellers.userAuth.SessionManager.PHOTO;
import static com.wstcon.gov.bd.esellers.userAuth.SessionManager.PREF_NAME;
import static com.wstcon.gov.bd.esellers.userAuth.SessionManager.STATUS;
import static com.wstcon.gov.bd.esellers.userAuth.SessionManager.TOKEN;
import static com.wstcon.gov.bd.esellers.userAuth.SessionManager.USER_NAME;
import static com.wstcon.gov.bd.esellers.utility.Utils.getBitmapImage;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        StartSplashFragment.SplashAction, AddorRemoveCallbacks, SeeProductDetails, CategoryListener, ShowHideIconListener {

    private static final String TAG = "MainActivity ";
    private TextView nameTV, emailTV, header_authTV;
    private ImageView header_authIV;
    private LinearLayout userLoginLinearLayout;
    private CircleImageView profile_img;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Fragment fragment;
    private List<VerticalModel> vmList = new ArrayList<>();
    private List<Category>categoryList=new ArrayList<>();
    private SharedPreferences prefs;
    private SessionManager sessionManager;
    private DatabaseQuery databaseQuery;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    private Menu nav_profile_Menu, nav_order_Menu;

    public static Set<Cart> cartSet = new HashSet<>();
    public static int cart_count = 0;
    public static double grandTotalPlus = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        databaseQuery=new DatabaseQuery(this);
        categoryList.addAll(databaseQuery.getCategory());

        Log.e(TAG, "onCreate: called");
        prefs = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setViewForNavHeader();

        nav_profile_Menu = navigationView.getMenu();
        nav_profile_Menu.findItem(R.id.nav_profile).setVisible(false);

        nav_order_Menu = navigationView.getMenu();
        nav_order_Menu.findItem(R.id.nav_order).setVisible(false);

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
                if (!sessionManager.isLogin()) {
                    startActivity(new Intent(MainActivity.this, AuthActivity.class));
                    drawer.closeDrawer(GravityCompat.START);

                } else
                    doLogout(prefs.getString(TOKEN, ""));
            }
        });
        setDrawerView();

    }

    private void doLogout(String token) {
        Call<LogoutResponse> call = RetrofitClient.getInstance(token).getApiInterface().userLogout();
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    LogoutResponse logoutResponse = response.body();
                    if (logoutResponse != null && logoutResponse.getStatus() == 1) {
                        Toast.makeText(MainActivity.this, logoutResponse.getMessage(), Toast.LENGTH_LONG).show();
                        sessionManager.clearSession();
                        resetDrawerView();
                        drawer.closeDrawer(GravityCompat.START);
                        Log.d(TAG, "onResponse: " + response.code());
                    } else {
                        Toast.makeText(MainActivity.this, logoutResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else
                    Log.d(TAG, "onResponse: " + response.code());
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resetDrawerView() {
        profile_img.setImageResource(R.drawable.ic_user);
        nameTV.setText("Guest User");
        emailTV.setText("guest@mail.com");
        nav_profile_Menu.findItem(R.id.nav_profile).setVisible(false);
        nav_order_Menu.findItem(R.id.nav_order).setVisible(false);
        header_authTV.setText("Sign In");
        header_authIV.setImageResource(R.drawable.ic_login_white);
    }


    private void setDrawerView() {
        SharedPreferences preferences = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        prefs = preferences;
        if (sessionManager.isLogin()) {
            nav_profile_Menu.findItem(R.id.nav_profile).setVisible(true);
            nav_order_Menu.findItem(R.id.nav_order).setVisible(true);
            header_authTV.setText("Sign Out");
            header_authIV.setImageResource(R.drawable.ic_logout_white);
            emailTV.setText(preferences.getString(EMAIL, ""));
            if (preferences.getBoolean(STATUS, false)) {
                nameTV.setText(preferences.getString(USER_NAME, ""));
                profile_img.setImageBitmap(getBitmapImage(preferences.getString(PHOTO, "no photo")));
            }
        }
    }

    private void setViewForNavHeader() {
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        nameTV = header.findViewById(R.id.nav_usernameTV);
        emailTV = header.findViewById(R.id.nav_user_emailTV);
        profile_img = header.findViewById(R.id.nav_user_photoIV);
        userLoginLinearLayout = header.findViewById(R.id.nav_loginLL);
        header_authTV = header.findViewById(R.id.header_auth_statusTV);
        header_authIV = header.findViewById(R.id.header_auth_statusIV);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.cart3));
//        MenuItem menuItem2 = menu.findItem(R.id.notification_action);
//        menuItem2.setIcon(Converter.convertLayoutToImage(MainActivity.this, 0, R.drawable.ic_notifications_white_24dp));
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
    public void onSplashFinished(List<VerticalModel> vmList) {
        this.vmList = vmList;
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", (Serializable) vmList);
        fragment = new HomeFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onAddProduct(Cart cart) {
        if (cartSet.contains(cart)) {
            Toast.makeText(this, "already into cart", Toast.LENGTH_SHORT).show();
        } else {
            cart_count++;
            invalidateOptionsMenu();
            cartSet.add(cart);
            Log.e(TAG, "onAddProduct: " + cartSet.size() + " " + cart.getProductName());
            Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
        }
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
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: called");
        invalidateOptionsMenu();
        setDrawerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
        invalidateOptionsMenu();
        setDrawerView();
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
                onCatIconClick(categoryList.get(1));
                break;
            case R.id.nav_women:
                onCatIconClick(categoryList.get(2));
                break;
            case R.id.nav_kids:
                onCatIconClick(categoryList.get(3));
                break;
            case R.id.nav_cosmetics:
                onCatIconClick(categoryList.get(4));
                break;
            case R.id.nav_bag:
                onCatIconClick(categoryList.get(5));
                break;
            case R.id.nav_home_app:
                onCatIconClick(categoryList.get(6));
                break;
            case R.id.nav_mobile:
                onCatIconClick(categoryList.get(7));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
//            case R.id.nav_policy:
//                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.nav_about:
                Toast.makeText(this, "send", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_order:
                startActivity(new Intent(MainActivity.this, OrderActivity.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        navigationView.getMenu().getItem(0).setChecked(false);
        return false;
    }

    @Override
    public void onCatIconClick(Category category) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        fragment = new ProductFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }


    @Override
    public void onProductClick(Product model) {
//        startActivity(new Intent(MainActivity.this, ProductDetailsActivity.class).putExtra("product", model));
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", model);
        Fragment fragment = new ProductDetailsFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

    }

    @Override
    public void showHamburgerIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(true);
    }

    @Override
    public void showBackIcon() {
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
