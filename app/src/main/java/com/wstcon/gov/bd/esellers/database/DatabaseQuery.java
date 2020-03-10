/*
 * Copyright (c) 2019. $1$1$1. All Rights Reserved.
 *  <p>
 *  Save to the extent permitted by law, you may not use, copy, modify, distribute or create derivative works of this material  or any part of it without the prior written consent of $1$1$1.
 *  <p>
 *  The above Copyright notice  and the permission notice shall be included in all copies or substantial portions of the Software.
 */

package com.wstcon.gov.bd.esellers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wstcon.gov.bd.esellers.mainApp.dataModel.Slider;
import com.wstcon.gov.bd.esellers.utility.Utils;

import java.util.ArrayList;
import java.util.List;


public class DatabaseQuery {
    private DatabaseOpenHelper helper;
    private SQLiteDatabase db;

    public DatabaseQuery(Context context) {
        helper = new DatabaseOpenHelper(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public void insertSlider(Slider slider) {

        this.open();
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.SLIDER_ID, slider.getId());
        values.put(DatabaseOpenHelper.SLIDER_URL, slider.getSliderImage());
        values.put(DatabaseOpenHelper.SLIDER_STATUS, slider.getPublicationStatus());
        values.put(DatabaseOpenHelper.SLIDER_CREATED_AT, slider.getCreatedAt());
        values.put(DatabaseOpenHelper.SLIDER_UPDATED_AT, slider.getUpdatedAt());
        values.put(DatabaseOpenHelper.SLIDER_PHOTO, Utils.getImageBOA(slider.getBitmap()));
        try {
            db.insert(DatabaseOpenHelper.SLIDER_TBL, null, values);
        } catch (Exception e) {
            Log.e("DatabaseQuery", "insertData: " + e.getMessage());
        }

        this.close();
    }

    public List<Slider> getSlider() {
        this.open();
        List<Slider> sliders = new ArrayList<>();
        Cursor cursor = db.rawQuery(DatabaseOpenHelper.READ_SLIDER_TBL, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Slider slider = new Slider();
                slider.setId(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_ID)));
                slider.setSliderImage(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_URL)));
                slider.setPublicationStatus(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_STATUS)));
                slider.setCreatedAt(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_CREATED_AT)));
                slider.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_UPDATED_AT)));
                slider.setBitmap(Utils.getImageBitmap(cursor.getBlob(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_PHOTO))));
                sliders.add(slider);
            } while (cursor.moveToNext());
            cursor.close();
        }
        this.close();
        return sliders;
    }


    public long getRowCount() {
        this.open();
        long count = DatabaseUtils.queryNumEntries(db, DatabaseOpenHelper.SLIDER_TBL);
        this.close();
        return count;
    }

    public void deleteAll(){
        this.open();
        db.execSQL(DatabaseOpenHelper.DELETE_TABLE);
        this.close();
    }
}
