package com.example.max.valute;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.max.valute.Valute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 12.05.2015.
 */
public class Db {

    private static final String LOG_TAG = "my_tag";
    DbHelper dbHelper;
    Context context;
    Cursor cursor;
    SQLiteDatabase db;
    List<Valute> mValuteList;
    // переменные для query
    String[] columns = null;
    String selection = null;
    String[] selectionArgs = null;
    String groupBy = null;
    String having = null;
    String orderBy = null;

    public Db(Context context){
        this.context = context;
        dbHelper = new DbHelper(context);
    }
    //Вывод количества записей в таблице
    public int getItemCount(){
        db = dbHelper.getReadableDatabase();
        cursor = db.query(DbHelper.TABLE_NAME, columns,selection,selectionArgs,groupBy,having,orderBy);
        int cnt = cursor.getColumnCount();
        cursor.close();
        return cnt;
    }
    public void insertRow(String date, String valute, String value){
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", date);
        cv.put("valute", valute);
        cv.put("value", value);
        db.insert(DbHelper.TABLE_NAME, null, cv);
    }
    public String getValue(String date, String valute){
        String value;
        columns = new String[]{DbHelper.VALUE};
        selection = DbHelper.DATE + " = ? AND " + DbHelper.VALUTE + " = ?";
        selectionArgs = new String[]{date, valute};
        db = dbHelper.getReadableDatabase();
        cursor = db.query(DbHelper.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
        //cursor = db.rawQuery("SELECT "+ DbHelper.VALUE + " FROM " + DbHelper.TABLE_NAME + " WHERE " + selection)
        cursor.moveToFirst();
        if (cursor == null) {
                Log.d(LOG_TAG, "Cursor is null");
                value = "Error";
            } else {
                int valueInd = cursor.getColumnIndex(DbHelper.VALUE);
                value = cursor.getString(valueInd);
                cursor.close();
            }
        cursor.close();
        return value;
    }
    public void close() {
        dbHelper.close();
        db.close();
    }
}
