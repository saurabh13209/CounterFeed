package com.saurabh.hackathon18;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import java.util.ArrayList;


public class ShareHolder {
    Context context;
    SharedPreferences sharedPreferences;
    DatabaseHandler databaseHandler;

    ShareHolder(Context context){
        databaseHandler = new DatabaseHandler(context);
        this.context =context;
        sharedPreferences = context.getSharedPreferences("DataBase", Context.MODE_PRIVATE);
    }


    void setId(String Id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Id",Id);
        editor.commit();
    }


    void setAccount(String Name , String Email , String Mob){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name",Name);
        editor.putString("Email",Email);
        editor.putString("Phone",Mob);
        editor.commit();
    }

    void AddValue(String SrNo , String Val){
        databaseHandler.AddData(SrNo ,Val);
    }

    String getCheckButton(String id){
        return sharedPreferences.getString(id,"");
    }

    void SetButton(String id , String val){
        SharedPreferences .Editor editor = sharedPreferences.edit();
        editor.putString(id , val);
        editor.commit();
    }


    String getId(){
        return sharedPreferences.getString("Id","");
    }

    String getName(){
        return sharedPreferences.getString("Name","");
    }

    ArrayList<ArrayList> getDataResponse(){
        ArrayList<ArrayList> arrayLists = new ArrayList<>();
        Cursor cursor = databaseHandler.getData();
        while (cursor.moveToNext()){
            ArrayList temp  =new ArrayList();
            temp.add(cursor.getString(0));
            temp.add(cursor.getString(1));
            arrayLists.add(temp);
        }

        return arrayLists;
    }

}
