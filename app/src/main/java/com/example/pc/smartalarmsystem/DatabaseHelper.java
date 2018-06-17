package com.example.pc.smartalarmsystem;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by PC on 4/4/2018.
 */

public class DatabaseHelper extends SQLiteAssetHelper {
    private static final String DB_NAME="question_db";
    private static final int VERSION=1;
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
}
