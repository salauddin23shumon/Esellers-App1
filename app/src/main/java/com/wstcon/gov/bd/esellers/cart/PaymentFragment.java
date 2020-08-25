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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
public class PaymentFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

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
    private RadioButton r1, r2, r3, r4, r5;
    private double total;
    private List<Cart> tempArrayList = new ArrayList<>();

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        action = (PaymentFrgmntAction) context;
        prefs = context.getSharedPreferences("Session", MODE_PRIVATE);
        customerId = prefs.getString("ID", "No ID defined");
        total = getArguments().getDouble("total");
        Log.d(TAG, "onAttach: " + total+" uid :"+customerId);

        tempArrayList.addAll(cartSet);
        Log.d(TAG, "sendOrder: "+tempArrayList.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        final StepView stepView = view.findViewById(R.id.step_view);
        ll1 = view.findViewById(R.id.midLL1);
        ll2 = view.findViewById(R.id.midLL2);
        ll3 = view.findViewById(R.id.midLL3);
        nxtBtn = view.findViewById(R.id.next);
        bckBtn = view.findViewById(R.id.back);
        toolbar = view.findViewById(R.id.toolbar);

        r1 = view.findViewById(R.id.rdbtn1);
        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (buttonView.getId() == R.id.rdbtn1) {
                        r2.setChecked(false);
                    }
                }
            }
        });
        r2 = view.findViewById(R.id.rdbtn2);
        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (buttonView.getId() == R.id.rdbtn2) {
                        r1.setChecked(false);
                    }
                }
            }
        });

        r3 = view.findViewById(R.id.rdbtn3);
        r3.setOnCheckedChangeListener(this);
        r4 = view.findViewById(R.id.rdbtn4);
        r4.setOnCheckedChangeListener(this);
        r5 = view.findViewById(R.id.rdbtn5);
        r5.setOnCheckedChangeListener(this);


        addressET = view.findViewById(R.id.addressET);
        cityET = view.findViewById(R.id.cityET);
        zipET = view.findViewById(R.id.zipET);
        countryET = view.findViewById(R.id.countryET);
        contactET = view.findViewById(R.id.contactET);
        receiverET = view.findViewById(R.id.receiverET);

        countryET.setEnabled(false);

        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bckBtn.setVisibility(View.VISIBLE);

                Log.d(TAG, "onClick: " + currentStep);

                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                    if (currentStep == 1) {
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);

                    } else if (currentStep == 2) {
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.VISIBLE);

                    } else {
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll1.setVisibility(View.VISIBLE);
                    }

                } else {


                    address = addressET.getText().toString();
                    city = cityET.getText().toString();
                    zip = zipET.getText().toString();
                    country = countryET.getText().toString();
                    contact = contactET.getText().toString();
                    name = receiverET.getText().toString();


                    if (address.isEmpty()) {
                        addressET.setError("Please enter address");
                        stepView.go(1, true);
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                    } else if (contact.isEmpty()) {
                        contactET.setError("Please enter valid mobile number");
                        stepView.go(1, true);
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                    } else if (contact.length() < 11) {
                        contactET.setError("mobile number must be 11 characters");
                        stepView.go(1, true);
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                    } else if (zip.isEmpty()) {
                        zipET.setError("Please enter zip code");
                        stepView.go(1, true);
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                    } else if (name.isEmpty()) {
                        receiverET.setError("Please enter name");
                        stepView.go(1, true);
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                    } else if (city.isEmpty()) {
                        cityET.setError("Please enter city");
                        stepView.go(1, true);
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                    } else {

//                        nxtBtn.setEnabled(true);
                        stepView.done(true);
                        Log.e(TAG, "onClick: finish");

                        Address addressObj = new Address();
                        addressObj.setAddress(address);
                        addressObj.setCity(city);
                        addressObj.setZip(Integer.parseInt(zip));
                        addressObj.setCountry(country);
                        addressObj.setPhone(contact);
                        addressObj.setReceiverName(name);


                        Order order = new Order();
                        order.setCustomerId(Integer.parseInt(customerId));
                        order.setOrderTotal(total);
                        order.setPaymentType("COD");
                        order.setHasDifferentShipping(true);
                        order.setCart(tempArrayList);
                        order.setAddress(addressObj);

                        sendOrder(order);
                    }


//                    if (address.equals("")||city.equals("")||zip.equals("")||country.equals("")||contact.equals("")||name.equals("")) {
//                        Log.e(TAG, "onClick: empty");
//                        stepView.go(1,true);
//                        ll1.setVisibility(View.GONE);
//                        ll3.setVisibility(View.GONE);
//                        ll2.setVisibility(View.VISIBLE);
//
//                        addressET.setError("fill every fields ");
//                        cityET.setError("fill every fields ");
//                        zipET.setError("fill every fields ");
//                        contactET.setError("fill every fields ");
//                        receiverET.setError("fill every fields ");
//
////                        currentStep--;
////                        nxtBtn.setEnabled(false);
//                    }else {
//                        nxtBtn.setEnabled(true);
//                        stepView.done(true);
//                        Log.e(TAG, "onClick: finish");
//
//                        Address addressObj=new Address();
//                        addressObj.setAddress(address);
//                        addressObj.setCity(city);
//                        addressObj.setZip(Integer.parseInt(zip));
//                        addressObj.setCountry(country);
//                        addressObj.setPhone(contact);
//                        addressObj.setReceiverName(name);
//
//                        sendOrder(addressObj);
//                    }
                }
            }
        });
        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep > 0) {
                    currentStep--;
                    if (currentStep == 1) {
                        ll1.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                    } else if (currentStep == 2) {
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.VISIBLE);
                    } else {
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

    private void sendOrder(Order newOrder) {


//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonParser jp = new JsonParser();
//        JsonElement je = jp.parse(String.valueOf(order));
//        String prettyJsonString = gson.toJson(je);
//        System.out.println(prettyJsonString);

        Call<CartRes> call = RetrofitClient.getInstance().getApiInterface().sendOrder(newOrder);
        call.enqueue(new Callback<CartRes>() {
            @Override
            public void onResponse(Call<CartRes> call, Response<CartRes> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.body().getStatus());
                    Toast.makeText(context, "u have placed order successfully", Toast.LENGTH_SHORT).show();
//                    action.onOrderSuccessfullyPlaced();
//                    for (Cart c:tempArrayList){
//                        c.getProduct().setAddedToCart(false);
//                        Log.d(TAG, "onResponse: "+c.getProductId());
//                    }
                    chngStatus(tempArrayList);
                } else
                    Log.e(TAG, "onResponse: else " + response.code());
            }

            @Override
            public void onFailure(Call<CartRes> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.rdbtn3) {
                r4.setChecked(false);
                r5.setChecked(false);
            }
            if (buttonView.getId() == R.id.rdbtn4) {
                r3.setChecked(false);
                r5.setChecked(false);
            }
            if (buttonView.getId() == R.id.rdbtn5) {
                r3.setChecked(false);
                r4.setChecked(false);
            }
        }
    }

    public interface PaymentFrgmntAction {
        void onOrderSuccessfullyPlaced();
    }

    public void chngStatus(List<Cart> carts) {
//        Log.d(TAG, "chngStatus: "+carts.size());
        if (carts != null) {
            for (Cart c : carts)
                c.getProduct().setAddedToCart(false);
            action.onOrderSuccessfullyPlaced();
        } else {
            Log.e(TAG, "chngStatus: temparray is null");
        }
    }

}
