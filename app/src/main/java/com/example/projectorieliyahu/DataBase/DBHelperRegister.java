package com.example.projectorieliyahu.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperRegister extends SQLiteOpenHelper {

    public static final String DBfile="allTables.db";
    /*public static final String Table_Name="Managers";
    public static final String Manager_Name="name";
    public static final String Manager_Phone="phone";
    public static final String Manager_Email="email";
    public static final String Manager_Id="id";
    public static final String Manager_Pass="pass";
    public static final String Manager_NameBak="nameBak";

    public static final String Table_NameE="Employee";
    public static final String Employee_Name="name";
    public static final String Employee_Payment="payment";
    public static final String Employee_Shifts="shifts";*/

    public DBHelperRegister(@Nullable Context context) {
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
        db.execSQL(str);

        String str2= " CREATE TABLE " + Table_NameE;
        str2+= " ( "+ Employee_Name + " TEXT, ";
        str2+= Employee_Payment + " TEXT, ";
        str2+= Employee_Shifts + " TEXT );";
        db.execSQL(str2);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
