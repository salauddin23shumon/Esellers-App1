package com.wstcon.gov.bd.esellers.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.payment.fragment.AddressFragment;
import com.wstcon.gov.bd.esellers.payment.fragment.DeliveryFragment;
import com.wstcon.gov.bd.esellers.payment.fragment.PaymentFragment;

public class PaymentActivity extends AppCompatActivity implements DeliveryFragment.DeliveryFrgmntAction,
        AddressFragment.AddressFrgmntAction {

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
    public void onDeliveryNxtClick() {
        fragment = new AddressFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onAddressNxtClick() {
        fragment = new PaymentFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onAddressBckClick() {
        fragment = new DeliveryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
//        getSupportFragmentManager().beginTransaction().remove(fragment).addToBackStack(null).commit();
    }
}
