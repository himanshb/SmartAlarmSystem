package com.example.pc.smartalarmsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PC on 4/4/2018.
 */

public class LevelDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME="level_db";
    private static final int VERSION=1;
    public LevelDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table level(_id integer primary key autoincrement,level_name TEXT)");
        insert(db);
    }

    protected  void insert(SQLiteDatabase db){
       // SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("level_name","easy");
        db.insert("level",null,cv);
    }
    protected  boolean updateTable(String level){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("level_name",level);
        int check =db.update("level",cv,"_id=1",null);
        return check>0;
    }

    protected  String getLevel(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from level",null);
        cursor.moveToFirst();
        String level=cursor.getString(cursor.getColumnIndex("level_name"));
        cursor.close();
        return level;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
