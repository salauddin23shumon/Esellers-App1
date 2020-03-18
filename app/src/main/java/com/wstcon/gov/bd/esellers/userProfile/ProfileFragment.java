package com.wstcon.gov.bd.esellers.userProfile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wstcon.gov.bd.esellers.R;
import com.wstcon.gov.bd.esellers.userProfile.userModel.ProfileUpdateRes;
import com.wstcon.gov.bd.esellers.networking.RetrofitClient;
import com.wstcon.gov.bd.esellers.userAuth.userAuthModels.LogoutResponse;
import com.wstcon.gov.bd.esellers.userProfile.userModel.Users;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.wstcon.gov.bd.esellers.utility.Utils.getStringImage;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final int PHOTO_REQUEST_CODE = 1;
    private String id;
    private EditText nameET, emailET, contactET, addressET;
    private TextView outputTV;
    private String token, name, email, contact, stringPhoto, address;
    private Button btnLogout, choseBtn, editBtn;
    private CircleImageView profileImg;
    private Context context;
    private SharedPreferences prefs;
    private Logout logout;
    private String TAG = "ProfileFragment ";
    private ProfileOpen profileOpen;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        logout = (Logout) context;
        profileOpen = (ProfileOpen) context;
        prefs = context.getSharedPreferences("Session", MODE_PRIVATE);
        token = prefs.getString("TOKEN", "No TOKEN defined");
        id = prefs.getString("ID", "No ID defined");
//        token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9lc2VsbGVycy5hZ2Fpbndpc2guY29tXC9hcGlcL2F1dGhcL3VzZXJfbG9naW4iLCJpYXQiOjE1ODQzNzIxMDIsImV4cCI6MTYxNTkwODEwMiwibmJmIjoxNTg0MzcyMTAyLCJqdGkiOiJXWGZLaXVwOHpUbERDYmJHIiwic3ViIjoxMywicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.Vdl5pfgFfs2HKyRoBpfkv2g1yjCQbZ6-1sDsG-vyHFk";
//        id="13";

        Log.e(TAG, "onAttach: "+token+" \nid: "+id );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        outputTV = view.findViewById(R.id.outputTV);
        nameET = view.findViewById(R.id.nameET);
        emailET = view.findViewById(R.id.emailET);
        contactET = view.findViewById(R.id.contactET);
        addressET = view.findViewById(R.id.addressET);
        choseBtn = view.findViewById(R.id.choseBtn);
        editBtn = view.findViewById(R.id.editBtn);
        profileImg = view.findViewById(R.id.profileIV);

        emailET.setText(prefs.getString("EMAIL", "No name defined"));
//        btnLogout=view.findViewById(R.id.btn_logout);
//        getUserData(token);

//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout(token);
//            }
//        });

//        outputTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                profileOpen.onProfile();
//            }
//        });

        choseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                chooseImageFile(context, PHOTO_REQUEST_CODE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Photo"), PHOTO_REQUEST_CODE);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                contact = contactET.getText().toString();
                address = addressET.getText().toString();
                updateProfile(name, email, contact, stringPhoto, address);
            }
        });

        return view;
    }

    private void updateProfile(String name, String email, String contact, String stringPhoto, String address) {
//        Log.e(TAG, "updateProfile: "+name+" "+email+" "+contact+" "+stringPhoto.length()+" "+address );
        Call<ProfileUpdateRes>call=RetrofitClient.getInstance(token).getApiInterface().updateProfile(name,email,contact,stringPhoto,address,Integer.parseInt(id));
        call.enqueue(new Callback<ProfileUpdateRes>() {
            @Override
            public void onResponse(Call<ProfileUpdateRes> call, Response<ProfileUpdateRes> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==1){
                        Users users=response.body().getUser();
                        Log.e(TAG, "onResponse1: "+users.getAddress() );
                    }
                }else
                    Log.e(TAG, "onResponse2: "+response.code() );
            }

            @Override
            public void onFailure(Call<ProfileUpdateRes> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d(TAG, "onActivityResult: called");
            Uri filePath = data.getData();
            RequestOptions myOptions = new RequestOptions()
                    .centerCrop() // or centerCrop
                    .override(195, 195);

            Glide
                    .with(context)
                    .asBitmap()
                    .apply(myOptions)
                    .load(filePath)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            stringPhoto = (getStringImage(resource));
                            profileImg.setImageBitmap(resource);
                            Log.d(TAG, "onResourceReady: called");
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    private void logout(String token) {
        Call<LogoutResponse> call = RetrofitClient.getInstance(token).getApiInterface().userLogout();
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    LogoutResponse logoutResponse = response.body();
                    if (logoutResponse != null && logoutResponse.getStatus() == 1) {
                        Toast.makeText(getContext(), logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        prefs.edit().clear().apply();
                        logout.onUserLogout();
                        Log.d(TAG, "onResponse: " + response.code());
                    } else {
                        Toast.makeText(getContext(), logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Log.d(TAG, "onResponse: " + response.code());
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void getUserData(String token) {
//        Log.d("profile", "getUserData: token length:"+token.length());
//        Call<Users>call= RetrofitClient.getInstance(token).getApiInterface().getUserProfile();
//        call.enqueue(new Callback<Users>() {
//            @Override
//            public void onResponse(Call<Users> call, Response<Users> response) {
//                if (response.isSuccessful()) {
//                    Users user = response.body();
//                    outputTV.setText(user.getEmail());
//                    Log.e(TAG, "onResponse: "+user.getEmail() );
//                }
//                Log.d("profile", "onResponse: "+response.code());
//            }
//
//            @Override
//            public void onFailure(Call<Users> call, Throwable t) {
//                Log.d("profile", "onFailure: "+t.getLocalizedMessage());
//            }
//        });
//    }


    public interface Logout {
        void onUserLogout();
    }

    public interface ProfileOpen {
        void onProfile();
    }

}
