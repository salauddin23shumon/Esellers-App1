package com.wstcon.gov.bd.esellers.userAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.wstcon.gov.bd.esellers.mainApp.MainActivity;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.Token;

public class AuthActivity extends AppCompatActivity implements SignUpFragment.SignUpComplete, SignUpFragment.BackToSignIn, SignInFragment.LoginComplete, SignInFragment.BackToSignUp {

    private Fragment fragment;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        preferences = getSharedPreferences("UserToken", MODE_PRIVATE);
        boolean status = preferences.getBoolean("status", false);
        if (!status){
            fragment = new SignInFragment();
            commitTransaction(fragment);
        }else {
            startActivity(new Intent(AuthActivity.this, MainActivity.class));
            finish();
        }

    }

    private void commitTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onSignUpComplete(Token token) {
        Log.e("auth", "onSignUpComplete: "+token.getOriginal().getAccessToken() );
        writeToken(token.getOriginal().getAccessToken());
        startActivity(new Intent(AuthActivity.this, MainActivity.class).putExtra("token", token));
        finish();
    }

    @Override
    public void onBackToSignIn() {
        fragment=new SignInFragment();
        commitTransaction(fragment);
    }

    @Override
    public void onLoginComplete(Token token) {
        Log.e("auth", "onLoginComplete: "+token.getOriginal().getAccessToken() );
        writeToken(token.getOriginal().getAccessToken());
        startActivity(new Intent(AuthActivity.this, MainActivity.class).putExtra("token", token));
        finish();
    }

    public void writeToken(String token){
        editor = getSharedPreferences("UserToken", MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.putBoolean("status", true);
        editor.apply();
    }

    @Override
    public void onBackToSignUp() {
        fragment=new SignUpFragment();
        commitTransaction(fragment);
    }
}