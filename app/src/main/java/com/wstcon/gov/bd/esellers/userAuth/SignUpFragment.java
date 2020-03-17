package com.wstcon.gov.bd.esellers.userAuth;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.AuthResponse;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment ";
    private EditText  emailET,  passwordET, confirmET;
    private String  email, password;
    private ProgressBar progressBar;
    private Button regBtn;
    private TextView logonTV;
    private SignUpComplete signupComplete;
    private BackToSignIn backToSignin;
    private Context context;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        signupComplete = (SignUpComplete) context;
        backToSignin = (BackToSignIn) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        emailET = view.findViewById(R.id.emailET);
        confirmET = view.findViewById(R.id.confirmPassET);
        passwordET = view.findViewById(R.id.passwordET);
        regBtn = view.findViewById(R.id.signupBtn);
        logonTV = view.findViewById(R.id.loginTV);
        progressBar = view.findViewById(R.id.progress_signup);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                doSignup(email, password);
            }
        });

        logonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToSignin.onBackToSignIn();
            }
        });

        return view;
    }

    private void doSignup(String email, String password) {

        Call<AuthResponse> call = RetrofitClient.getInstance().getApiInterface().createUser(email, password);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null && authResponse.getStatus() == 1) {
                        Toast.makeText(context, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        signupComplete.onSignUpComplete(authResponse.getToken());
                        Log.d(TAG, "onResponse1: " + response.code());
                    } else {
                        Toast.makeText(context, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Log.d(TAG, "onResponse2: " + response.code());//this is for server side error, wrong path, wrong file name or missing
            }
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface SignUpComplete {
        void onSignUpComplete(Token token);
    }

    public interface BackToSignIn {
        void onBackToSignIn();
    }

}
