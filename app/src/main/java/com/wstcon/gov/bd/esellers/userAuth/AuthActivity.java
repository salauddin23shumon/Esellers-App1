package com.wstcon.gov.bd.esellers.userAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wstcon.gov.bd.esellers.mainApp.MainActivity;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.userProfile.userModel.Users;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity implements SignUpFragment.SignUpComplete,
        SignUpFragment.BackToSignIn, SignInFragment.SignInFrgmntAction {

    private static final String TAG = "AuthActivity ";
    private Fragment fragment;
    private SessionManager sessionManager;
//    private SharedPreferences preferences;
//    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        sessionManager = new SessionManager(this);

        fragment = new SignInFragment();
        if (savedInstanceState == null)
            commitTransaction(fragment);

//        preferences = getSharedPreferences("UserToken", MODE_PRIVATE);
//        boolean status = preferences.getBoolean("status", false);
//        if (!status){
//            fragment = new SignInFragment();
//            commitTransaction(fragment);
//        }else {
//            startActivity(new Intent(AuthActivity.this, MainActivity.class));
//            finish();
//        }

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    private void commitTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onSignUpComplete(Token token) {
        Log.e(TAG, "onSignUpComplete: " + token.getOriginal().getAccessToken());
//        writeToken(token.getOriginal().getAccessToken());
        startActivity(new Intent(AuthActivity.this, MainActivity.class).putExtra("token", token));
        finish();
    }

    @Override
    public void onBackToSignIn() {
        fragment = new SignInFragment();
        commitTransaction(fragment);
    }

    @Override
    public void onLoginComplete(Token token) {
        Log.e(TAG, "onLoginComplete: " + token.getOriginal().getAccessToken());
        getUserData(token.getOriginal().getAccessToken());
//        writeToken(token.getOriginal().getAccessToken());
//        startActivity(new Intent(AuthActivity.this, MainActivity.class).putExtra("token", token));
        finish();
    }

//    public void writeToken(String token){
//        editor = getSharedPreferences("UserToken", MODE_PRIVATE).edit();
//        editor.putString("token", token);
//        editor.putBoolean("status", true);
//        editor.apply();
//    }

    @Override
    public void onBackToSignUp() {
        fragment = new SignUpFragment();
        commitTransaction(fragment);
    }

    @Override
    public void onBackPress() {
//        startActivity(new Intent(AuthActivity.this, MainActivity.class));
        finish();
    }

    private void getUserData(final String token) {
        Log.d(TAG, "getUserData: token length:" + token.length());
        Call<Users> call = RetrofitClient.getInstance(token).getApiInterface().getUserProfile();
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Users user = response.body();
                    user.setToken(token);
                    if (user.getUserProfilePhoto()==null) {
                        user.setProfileComplete(false);
                    }else {
                        user.setProfileComplete(true);
                    }
                    Log.e(TAG, "onResponse: " + user.getEmail());
                    sessionManager.createSession(user);
                } else
                    Log.d(TAG, "onResponse: " + response.code());
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}