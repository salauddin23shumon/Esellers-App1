/*
 * Copyright (c) 2019. $1$1$1. All Rights Reserved.
 *  <p>
 *  Save to the extent permitted by law, you may not use, copy, modify, distribute or create derivative works of this material  or any part of it without the prior written consent of $1$1$1.
 *  <p>
 *  The above Copyright notice  and the permission notice shall be included in all copies or substantial portions of the Software.
 */

package com.wstcon.gov.bd.esellers.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static byte[] getImageBOA(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap getImageBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
