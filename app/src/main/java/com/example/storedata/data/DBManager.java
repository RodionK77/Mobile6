package com.example.storedata.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private Context context;
    private DBHelper myDbHelper;
    private SQLiteDatabase db;

    public DBManager(Context context){
        this.context = context;
        myDbHelper = new DBHelper(context);
    }
    public void openDB(){
        db = myDbHelper.getWritableDatabase();
    }
    public void insertDB(String key, String value){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY, key);
        cv.put(DBHelper.VALUE, value);
        db.insert(DBHelper.TABLE_NAME, "", cv);
    }
    @SuppressLint("Range")
    public String getDB(){
        String str = "";
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        while(cursor.moveToNext()){
            str+=cursor.getString(cursor.getColumnIndex(DBHelper.KEY)) + ": " + cursor.getString(cursor.getColumnIndex(DBHelper.VALUE)) + "\n";
        }
        cursor.close();
        return str;
    }
    public void deleteDB(){
        db.delete(DBHelper.TABLE_NAME, null, null);
    }

    public void closeDB(){
        myDbHelper.close();
    }
}
