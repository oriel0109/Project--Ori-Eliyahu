package com.example.projectorieliyahu.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBfile="allTablesWork.db";
    public static final String Table_NameE="Employee";
    public static final String Employee_Name="name";
    public static final String Employee_Payment="payment";
    public static final String Employee_Shifts="shifts";
    public static final String Employee_Phone="phone";
    public static final String Employee_ManagerEmail="managerEmail";

    public static final String Table_NameR="Managers";
    public static final String Manager_Name="name";
    public static final String Manager_Phone="phone";
    public static final String Manager_Email="email";
    public static final String Manager_Id="id";
    public static final String Manager_Pass="pass";
    public static final String Manager_NameBak="nameBak";

    public static final String Table_NameP="Products";
    public static final String Product_Name="name";
    public static final String Product_Quantity="quantity";
    public static final String Product_Picture="picture";
    public static final String Product_ManagerEmail="managerEmail";

    public DBHelper(@Nullable Context context) {
        super(context, DBfile, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String str1= "CREATE TABLE " + Table_NameR;
        str1+= " ( "+ Manager_Name + " TEXT, ";
        str1+= Manager_Phone + " TEXT, ";
        str1+= Manager_Email + " TEXT, ";
        str1+= Manager_Id + " TEXT, ";
        str1+= Manager_Pass + " TEXT, ";
        str1+= Manager_NameBak + " TEXT );";


        String str2= " CREATE TABLE " + Table_NameE;
        str2+= " ( "+ Employee_Name + " TEXT, ";
        str2+= Employee_Payment + " TEXT, ";
        str2+= Employee_Shifts + " TEXT, ";
        str2+= Employee_ManagerEmail + " TEXT, ";
        str2+= Employee_Phone + " TEXT );";


        String str3= " CREATE TABLE " + Table_NameP;
        str3+= " ( "+ Product_Name + " TEXT, ";
        str3+= Product_Quantity + " TEXT, ";
        str3+= Product_ManagerEmail + " TEXT, ";
        str3+= Product_Picture + " TEXT );";

        db.execSQL(str1);
        db.execSQL(str2);
        db.execSQL(str3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
