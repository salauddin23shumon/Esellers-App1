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
public class DeliveryFragment extends Fragment {

    private Button nxtBtn;
    private DeliveryFrgmntAction action;

    public DeliveryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        action= (DeliveryFrgmntAction) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.checkout_delivery_option, container, false);
        nxtBtn=view.findViewById(R.id.nextBtn);

        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.onNextClick();
            }
        });
        return view;
    }

    public interface DeliveryFrgmntAction{
        void onNextClick();
    }

}
