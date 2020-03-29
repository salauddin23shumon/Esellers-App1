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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shuhart.stepview.StepView;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.cart.cartModel.Address;
import com.wstcon.gov.bd.esellers.cart.cartModel.Cart;
import com.wstcon.gov.bd.esellers.cart.cartModel.CartRes;
import com.wstcon.gov.bd.esellers.cart.cartModel.Order;
import com.wstcon.gov.bd.esellers.interfaces.NavBackBtnPress;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.wstcon.gov.bd.esellers.mainApp.MainActivity.cartSet;


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
    private EditText addressET, cityET, zipET, countryET, contactET, receiverET;
    private String address, city, zip, country, contact, name;
    private double total;

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
        total=getArguments().getDouble("total");
        Log.d(TAG, "onAttach: "+total);
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


        addressET = view.findViewById(R.id.addressET);
        cityET = view.findViewById(R.id.cityET);
        zipET = view.findViewById(R.id.zipET);
        countryET = view.findViewById(R.id.countryET);
        contactET = view.findViewById(R.id.contactET);
        receiverET = view.findViewById(R.id.receiverET);


        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bckBtn.setVisibility(View.VISIBLE);

                Log.d(TAG, "onClick: "+currentStep);

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

                    }else {
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll1.setVisibility(View.VISIBLE);
                    }

                } else {


                    address=addressET.getText().toString();
                    city=cityET.getText().toString();
                    zip=zipET.getText().toString();
                    country=contactET.getText().toString();
                    contact=contactET.getText().toString();
                    name=receiverET.getText().toString();

                    if (address.equals("")||city.equals("")||zip.equals("")||country.equals("")||contact.equals("")||name.equals("")) {
                        Log.e(TAG, "onClick: empty");
                        stepView.go(1,true);
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);

                        addressET.setError("fill every fields ");
                        cityET.setError("fill every fields ");
                        zipET.setError("fill every fields ");
                        contactET.setError("fill every fields ");
                        receiverET.setError("fill every fields ");

//                        currentStep--;
//                        nxtBtn.setEnabled(false);
                    }else {
                        nxtBtn.setEnabled(true);
                        stepView.done(true);
                        Log.e(TAG, "onClick: finish");

                        Address addressObj=new Address();
                        addressObj.setAddress(address);
                        addressObj.setCity(city);
                        addressObj.setZip(Integer.parseInt(zip));
                        addressObj.setCountry(country);
                        addressObj.setPhone(contact);
                        addressObj.setReceiverName(name);

                        sendOrder(addressObj);
                    }
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

    private void sendOrder(Address address) {

        final List<Cart> tempArrayList=new ArrayList<>();
        tempArrayList.addAll(cartSet);

        Order order=new Order();
        order.setCustomerId(Integer.parseInt(customerId));
        order.setOrderTotal(total);
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
                    chngStatus(tempArrayList);
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

    public void chngStatus(List<Cart>carts){
        for (Cart c:carts)
            c.getProduct().setAddedToCart(false);
    }

}
