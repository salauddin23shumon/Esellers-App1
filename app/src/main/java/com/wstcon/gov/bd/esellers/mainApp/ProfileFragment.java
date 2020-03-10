package com.wstcon.gov.bd.esellers.mainApp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.mainApp.Users;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.LogoutResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView outputTV;
    private String token;
    private Button btnLogout;
    private SharedPreferences prefs;
    private Logout logout;
    private String TAG="ProfileFragment";
    private ProfileOpen profileOpen;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        logout= (Logout) context;
        profileOpen= (ProfileOpen) context;
        prefs = context.getSharedPreferences("UserToken", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        outputTV=view.findViewById(R.id.outputTV);
        btnLogout=view.findViewById(R.id.btn_logout);
        getUserData(token);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(token);
            }
        });

        outputTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileOpen.onProfile();
            }
        });

        return view;
    }

    private void logout(String token) {
        Call<LogoutResponse>call=RetrofitClient.getInstance(token).getApiInterface().userLogout();
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()){
                    LogoutResponse logoutResponse=response.body();
                    if (logoutResponse!=null && logoutResponse.getStatus()==1){
                        Toast.makeText(getContext(), logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        prefs.edit().clear().apply();
                        logout.onUserLogout();
                        Log.d(TAG, "onResponse: " + response.code());
                    }else {
                        Toast.makeText(getContext(), logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else
                    Log.d(TAG, "onResponse: " + response.code());
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserData(String token) {
        Log.d("profile", "getUserData: token length:"+token.length());
        Call<Users>call= RetrofitClient.getInstance(token).getApiInterface().getUserProfile();
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Users user = response.body();
                    outputTV.setText(user.getEmail());
                    Log.e(TAG, "onResponse: "+user.getEmail() );
                }
                Log.d("profile", "onResponse: "+response.code());
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.d("profile", "onFailure: "+t.getLocalizedMessage());
            }
        });
    }


    public interface Logout{
        void onUserLogout();
    }

    public interface ProfileOpen{
        void onProfile();
    }

}
