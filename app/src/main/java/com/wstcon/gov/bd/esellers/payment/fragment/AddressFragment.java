package com.wstcon.gov.bd.esellers.payment.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wstcon.gov.bd.esellers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {

    private Button nxtBtn, backBtn;
    private AddressFrgmntAction action;

    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        action= (AddressFrgmntAction) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.checkout_address, container, false);
        nxtBtn=view.findViewById(R.id.nextBtn);
        backBtn=view.findViewById(R.id.backBtn);

        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.onAddressNxtClick();
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.onAddressBckClick();
            }
        });
        return view;
    }


    public interface AddressFrgmntAction{
        void onAddressNxtClick();
        void onAddressBckClick();
    }

}
