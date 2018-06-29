package com.ajdi.yassin.instajournal.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageUtils {

    public static String getPathFromURI(Context context, Uri contentUri) {
//        String[] FILE = { MediaStore.Images.Media.DATA };
//
//
//        Cursor cursor = getContentResolver().query(URI,
//                FILE, null, null, null);
//
//        cursor.moveToFirst();
//
//        int columnIndex = cursor.getColumnIndex(FILE[0]);
//        ImageDecode = cursor.getString(columnIndex);
//        cursor.close();
//
//        imageViewLoad.setImageBitmap(BitmapFactory
//                .decodeFile(ImageDecode));

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}