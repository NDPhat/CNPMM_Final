package com.example.myapplication;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public  class ConvertToString {
    public static String ConvertToString(Bitmap data)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        data.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return  encoded;

    }
}
