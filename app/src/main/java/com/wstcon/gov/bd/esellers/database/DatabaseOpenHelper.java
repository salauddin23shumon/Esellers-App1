/*
 * Copyright (c) 2019. $1$1$1. All Rights Reserved.
 *  <p>
 *  Save to the extent permitted by law, you may not use, copy, modify, distribute or create derivative works of this material  or any part of it without the prior written consent of $1$1$1.
 *  <p>
 *  The above Copyright notice  and the permission notice shall be included in all copies or substantial portions of the Software.
 */

package com.wstcon.gov.bd.esellers.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AppCache.db";
    public static final int DATABASE_VERSION = 1;

    //Slider table
    public static final String SLIDER_TBL = "Slider";
    public static final String SLIDER_ID = "id";
    public static final String SLIDER_URL = "url";
    public static final String SLIDER_STATUS = "status";
    public static final String SLIDER_CREATED_AT = "created_at";
    public static final String SLIDER_UPDATED_AT = "updated_at";
    public static final String SLIDER_PHOTO = "photo";


    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + SLIDER_TBL + " ( " +
            SLIDER_ID + " TEXT, " +
            SLIDER_URL + " TEXT, " +
            SLIDER_STATUS + " TEXT, " +
            SLIDER_CREATED_AT + " TEXT, " +
            SLIDER_UPDATED_AT + " TEXT, " +
            SLIDER_PHOTO + " BLOB );";

    public static final String READ_SLIDER_TBL = "SELECT * FROM " + SLIDER_TBL;

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + SLIDER_TBL;

    public static final String DELETE_TABLE = "DELETE FROM " + SLIDER_TBL;


    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        }catch (SQLException ex){
            Log.e("DatabaseOpenHelper", "onCreate: "+ex.getMessage() );
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
