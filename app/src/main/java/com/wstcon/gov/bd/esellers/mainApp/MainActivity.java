package com.wstcon.gov.bd.esellers.mainApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.dashboard.HomeFragment;
import com.wstcon.gov.bd.esellers.dashboard.StartSplashFragment;
import com.wstcon.gov.bd.esellers.mainApp.dataModel.VerticalModel;
import com.wstcon.gov.bd.esellers.product.ProductFragment;
import com.wstcon.gov.bd.esellers.userAuth.AuthActivity;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProfileFragment.Logout, ProfileFragment.ProfileOpen, StartSplashFragment.SplashAction {

    private Fragment fragment;

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
}
