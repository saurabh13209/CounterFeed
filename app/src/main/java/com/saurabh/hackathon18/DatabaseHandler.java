package com.saurabh.hackathon18;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    final static int version = 1;

    final static String DatabaseName = "SqlDatabase";


    public DatabaseHandler(Context context) {
        super(context, DatabaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String sql = "create table feed (" +
                " SrNo text , " +
                "response text " +
                ")";


        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor getData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "select SrNo , response from feed ";
        Cursor messageList =  sqLiteDatabase.rawQuery(sql,null);
        return messageList;
    }


    public void AddData(String SrNo , String val){
        SQLiteDatabase sqLiteDatabase= getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SrNo" , SrNo);
        contentValues.put("response" , val);
        sqLiteDatabase.insert("feed" , null , contentValues);
        sqLiteDatabase.close();
    }
}
