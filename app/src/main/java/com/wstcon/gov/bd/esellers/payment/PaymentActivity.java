package com.wstcon.gov.bd.esellers.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.dashboard.StartSplashFragment;
import com.wstcon.gov.bd.esellers.payment.fragment.AddressFragment;
import com.wstcon.gov.bd.esellers.payment.fragment.DeliveryFragment;

public class PaymentActivity extends AppCompatActivity implements DeliveryFragment.DeliveryFrgmntAction {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        fragment = new DeliveryFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }

    }

    @Override
    public void onNextClick() {
        fragment = new AddressFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
