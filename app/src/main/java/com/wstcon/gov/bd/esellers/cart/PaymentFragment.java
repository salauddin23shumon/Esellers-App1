package com.wstcon.gov.bd.esellers.cart;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shuhart.stepview.StepView;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.cartModel.Address;
import com.wstcon.gov.bd.esellers.cart.cartModel.CartRes;
import com.wstcon.gov.bd.esellers.cart.cartModel.Order;
import com.wstcon.gov.bd.esellers.interfaces.NavBackBtnPress;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.wstcon.gov.bd.esellers.cart.CartActivity.tempArrayList;
import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.grandTotalPlus;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    private static final String TAG = "PaymentFragment ";
    private int currentStep = 0;
    private LinearLayout ll1, ll2, ll3;
    private Button nxtBtn, bckBtn;
    private SharedPreferences prefs;
    private String customerId;
    private Context context;
    private PaymentFrgmntAction action;
    private Toolbar toolbar;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        action= (PaymentFrgmntAction) context;
        prefs = context.getSharedPreferences("Session", MODE_PRIVATE);
        customerId = prefs.getString("ID", "No ID defined");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_payment, container, false);

        final StepView stepView = view.findViewById(R.id.step_view);
        ll1 = view.findViewById(R.id.midLL1);
        ll2 = view.findViewById(R.id.midLL2);
        ll3 = view.findViewById(R.id.midLL3);
        nxtBtn = view.findViewById(R.id.next);
        bckBtn = view.findViewById(R.id.back);
        toolbar = view.findViewById(R.id.toolbar);


        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bckBtn.setVisibility(View.VISIBLE);

                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                    if (currentStep==1){
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                    }else if (currentStep==2){
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.VISIBLE);
                    }
                    else {
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll1.setVisibility(View.VISIBLE);
                    }

                } else {
                    stepView.done(true);
                    Log.e(TAG, "onClick: finish" );
                    sendOrder();
                }
            }
        });
        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep > 0) {
                    currentStep--;
                    if (currentStep==1){
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                    }else if (currentStep==2){
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.VISIBLE);
                    }
                    else {
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll1.setVisibility(View.VISIBLE);
                        bckBtn.setVisibility(View.GONE);
                    }
                }
                stepView.done(false);
                stepView.go(currentStep, true);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Payment");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavBackBtnPress) context).onNavBackBtnPress();
            }
        });
    }

    private void sendOrder() {

        Address address=new Address();
        address.setAddress("banglamotor");
        address.setCity("dhaka");
        address.setZip(1000);
        address.setCountry("bd");
        address.setPhone("1212331232");
        address.setReceiverName("sk");


        Order order=new Order();
        order.setCustomerId(Integer.parseInt(customerId));
        order.setOrderTotal(grandTotalPlus);
        order.setPaymentType("COD");
        order.setHasDifferentShipping(true);
        order.setCart(tempArrayList);
        order.setAddress(address);


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(String.valueOf(order));
        String prettyJsonString = gson.toJson(je);
        System.out.println(prettyJsonString);

        Call<CartRes> call= RetrofitClient.getInstance().getApiInterface().sendOrder(order);
        call.enqueue(new Callback<CartRes>() {
            @Override
            public void onResponse(Call<CartRes> call, Response<CartRes> response) {
                if (response.isSuccessful()){
                    Log.e(TAG, "onResponse: "+response.body().getStatus() );
                    Toast.makeText(context, "u have placed order successfully", Toast.LENGTH_SHORT).show();
                    action.onOrderSuccessfullyPlaced();
                }else
                    Log.e(TAG, "onResponse: else "+response.code() );
            }

            @Override
            public void onFailure(Call<CartRes> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
            }
        });
    }

    public interface PaymentFrgmntAction{
        void onOrderSuccessfullyPlaced();
    }

}
