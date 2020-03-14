package com.wstcon.gov.bd.esellers.payment.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wstcon.gov.bd.esellers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {


    public AddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.checkout_address, container, false);
        return view;
    }

}
