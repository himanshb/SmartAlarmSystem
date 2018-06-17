package com.example.pc.smartalarmsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sparsh on 07-Apr-18.
 */

public class AlarmDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME="alarm_db";
    private static final int VERSION=1;
    public AlarmDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table alarm_table(_id integer primary key autoincrement,alarm_time TEXT unique,time_stamp TEXT UNIQUE)");
    }


    protected  boolean insertAlarm(String time,String timeStamp){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("alarm_time",time);
        cv.put("time_stamp",timeStamp);
        long check =db.insert("alarm_table",null,cv);
        return check>0;
    }

    protected List<String> getAlarms(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from alarm_table",null);
        cursor.moveToFirst();
        List<String> alarms=new ArrayList<>();
        while (cursor.moveToNext()){
            String alarm=cursor.getString(cursor.getColumnIndex("time_stamp"));
            //String timeStampedALa=timeConversion(alarm);
            alarms.add(alarm);
        }
        cursor.close();
        return alarms;
    }

    protected String getAlarm(String timeStamp){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from alarm_table where time_stamp=?",new String[]{timeStamp});
        cursor.moveToFirst();
        String alarm=cursor.getString(cursor.getColumnIndex("alarm_time"));
        cursor.close();
        return alarm;
    }

    protected  boolean deleteAlarm(String timeStamp){
        SQLiteDatabase db=this.getWritableDatabase();
        int check=db.delete("alarm_table","time_stamp=?",new String[]{timeStamp});
        return check>0;
    }


 /*   String timeConversion(String time){
        long timestampLong = Long.parseLong(time);
        Date d = new Date(timestampLong);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
        int second=c.get(Calendar.SECOND);
        return new String(hour +" : "+minute+" : "+second);
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
