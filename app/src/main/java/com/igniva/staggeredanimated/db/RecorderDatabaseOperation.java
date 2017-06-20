package com.igniva.staggeredanimated.db;

import android.content.ContentValues;

/**
 * Created by igniva-android-21 on 20/12/16.
 */

public class RecorderDatabaseOperation {


    public static ContentValues createContentValues(String key1,String key2,int value1,int value2){

        ContentValues contentValues  = new ContentValues();
        contentValues.put(key1,value1);
        contentValues.put(key2,value2);
        return  contentValues;

    }
    public static ContentValues createContentValues(String key1,String key2,String key3,String key4,String key5,int value1,String value2,String value3,String value4,String value5){

        ContentValues contentValues  = new ContentValues();
        contentValues.put(key1,value1);
        contentValues.put(key2,value2);
        contentValues.put(key3,value3);
        contentValues.put(key4,value4);
        contentValues.put(key5,value5);


        return  contentValues;

    }
}
