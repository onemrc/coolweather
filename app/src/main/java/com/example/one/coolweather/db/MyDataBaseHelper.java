package com.example.one.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


/**
 * Created by one on 2018/11/29.
 * 描述：数据库建表
 */

public class MyDataBaseHelper extends SQLiteOpenHelper{
    private static final String CREATE_PROVINCE="create table province ("
            +"id integer primary key autoincrement ,"
            +"provinceName text,"
            +"provinceCode integer)";

    private static final String CREATE_CITY="create table city ("
            +"id integer primary key autoincrement ,"
            +"cityName text,"
            +"cityCode integer"
            +"provinceId integer)";

    private static final String CREATE_COUNTY="create table county ("
            +"id integer primary key autoincrement ,"
            +"countyName text,"
            +"weatherId text"
            +"cityId integer)";

    private Context context1;

    public MyDataBaseHelper( Context context, String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        context1 = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);

        Toast.makeText(context1,"create success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
