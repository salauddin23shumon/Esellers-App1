package com.wstcon.gov.bd.esellers.userAuth;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.wstcon.gov.bd.esellers.userProfile.userModel.Users;

import static com.wstcon.gov.bd.esellers.utility.Constant.BASE_URL;


public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final int MODE = Context.MODE_PRIVATE;
    private static final String PREF_NAME = "Session";
    private static final String LOGIN = "IS_LOGIN";
    private static final String TOKEN = "TOKEN";
    private static final String ID = "ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String EMAIL = "EMAIL";
    private static final String MOBILE = "MOBILE";
    private static final String ADDRESS = "ADDRESS";
    private static final String PHOTO_URL = "PHOTO_URL";
    private static final String PHOTO = "PHOTO";
    private static final String STATUS = "USER_STATUS";



    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(Users user) {
        editor.putBoolean(LOGIN, true);
        editor.putString(ID, String.valueOf(user.getId()));
        editor.putString(USER_NAME, user.getName());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(MOBILE, user.getMobile());
        editor.putString(ADDRESS, user.getAddress());
        editor.putString(PHOTO_URL, BASE_URL+user.getUserProfilePhoto());
        editor.putString(PHOTO, user.getImgString());
        editor.putString(TOKEN, user.getToken());
        editor.putBoolean(STATUS, user.isProfileComplete());
        editor.apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

//    public boolean checkLogin() {
//
//        if (!this.isLogin()) {
//            Intent i = new Intent(context, LoginActivity.class);
//
//            // Closing all the Activities from stack
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//            ((Activity) context).finish();
//            return true;
//        }
//        return false;
//    }

    public void logout(final Class<? extends Activity> ActivityToBeOpen) {
        editor.clear();
        sharedPreferences.getBoolean(LOGIN, false);
        editor.commit();
        Intent i = new Intent(context, ActivityToBeOpen);
        context.startActivity(i);
        ((Activity) context).finish();
    }


    /*user status
    * if 1 -> user profile is complete
    * else 0 -> incomplete user profile
    * */
}
