package com.example.projectorieliyahu.DataBase;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperEmployee extends SQLiteOpenHelper {

    public static final String DBfile="allTablesWork.db";
    /*public static final String Table_NameE="Employee";
    public static final String Employee_Name="name";
    public static final String Employee_Payment="payment";
    public static final String Employee_Shifts="shifts";

   public static final String Table_Name="Managers";
    public static final String Manager_Name="name";
    public static final String Manager_Phone="phone";
    public static final String Manager_Email="email";
    public static final String Manager_Id="id";
    public static final String Manager_Pass="pass";
    public static final String Manager_NameBak="nameBak";*/



    public DBHelperEmployee(@Nullable Context context) {
        super(context, DBfile, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*String str= "CREATE TABLE " + Table_Name;
        str+= " ( "+ Manager_Name + " TEXT, ";
        str+= Manager_Phone + " TEXT, ";
        str+= Manager_Email + " TEXT, ";
        str+= Manager_Id + " TEXT, ";
        str+= Manager_Pass + " TEXT, ";
        str+= Manager_NameBak + " TEXT );";
        db.execSQL(str);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
