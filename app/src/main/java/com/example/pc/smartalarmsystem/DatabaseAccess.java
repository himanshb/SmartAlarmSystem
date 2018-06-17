package com.example.pc.smartalarmsystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 4/4/2018.
 */

public class DatabaseAccess {
    private DatabaseHelper helper;
    private SQLiteDatabase myDatabase;

    public DatabaseAccess(Context context) {
        this.helper=new DatabaseHelper(context);
    }
    //open Database
    public void open(){
        myDatabase=helper.getWritableDatabase();
    }

    //open Database
    public void close(){
        if (myDatabase!=null){
            myDatabase.close();
        }
    }

    public List<Question> getQuestions(String level){
        Cursor cursor=myDatabase.rawQuery("select * from question_table where level=?",new String[]{level});
        List<Question> questions=new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.moveToNext()){
            String ques=cursor.getString(cursor.getColumnIndex("ques"));
            String op1=cursor.getString(cursor.getColumnIndex("op_1"));
            String op2=cursor.getString(cursor.getColumnIndex("op_2"));
            String op3=cursor.getString(cursor.getColumnIndex("op_3"));
            String op4=cursor.getString(cursor.getColumnIndex("op_4"));
            List<String> options=new ArrayList<>(4);
            options.add(op1);
            options.add(op2);
            options.add(op3);
            options.add(op4);
            Question question=new Question(ques,options);
            questions.add(question);
        }
        cursor.close();
        return questions;
    }
    public boolean checkAns(String ques,String ans){
        Cursor cursor=myDatabase.rawQuery("select q_id from question_table where ques=? and op_1=?",new String[]{ques,ans});
        if (cursor!=null&&cursor.moveToFirst()&&cursor.getCount()>0){
            cursor.close();
            return  true;
        }
        return false;
    }
}
