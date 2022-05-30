package com.example.projectorieliyahu.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperProduct extends SQLiteOpenHelper {

    public static final String DBfile="allTables.db";
    /*public static final String Table_NameP="Products";
    public static final String Product_Name="name";
    public static final String Product_Quantity="quantity";
    public static final String Product_Picture="picture";*/

    public DBHelperProduct(@Nullable Context context) {
        super(context, DBfile, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String str= " CREATE TABLE " + Table_NameP;
        str+= " ( "+ Product_Name + " TEXT, ";
        str+= Product_Quantity + " TEXT, ";
        str+= Product_Picture + " TEXT );";
        db.execSQL(str);*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
