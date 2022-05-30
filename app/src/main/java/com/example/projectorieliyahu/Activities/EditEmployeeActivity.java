package com.example.projectorieliyahu.Activities;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projectorieliyahu.DataBase.*;
import com.example.projectorieliyahu.Model.*;
import com.example.projectorieliyahu.R;
import com.google.android.material.navigation.NavigationView;

public class EditEmployeeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText eeaEdtName;
    EditText eeaEdtShifts;
    EditText eeaEdtPayment;
    EditText eeaEdtPhone;
    Button eeaBtnDelete;
    Button eeaBtnFinish;

    Employee employee;
    int resultCode;
    DBHelper dbHelper;
    SQLiteDatabase db;
    ContentValues c;

    String strPayOld = "";
    String strShifOld = "";

    String[] shifts = new String[6];

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(),R.id.i1,false);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eeaEdtName = findViewById(R.id.eeaEdtName);
        eeaEdtShifts = findViewById(R.id.eeaEdtShifts);
        eeaEdtPayment = findViewById(R.id.eeaEdtPayment);
        eeaEdtPhone = findViewById(R.id.eeaEdtPhone);
        eeaBtnDelete=findViewById(R.id.eeaBtnDelete);
        eeaBtnFinish=findViewById(R.id.eeaBtnFinish);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        Intent intent=getIntent();

        resultCode = intent.getIntExtra("ResultCode", 0);
        if (resultCode == 1) {
            eeaEdtName.setEnabled(false);
            employee = (Employee) intent.getSerializableExtra("Employee");
            eeaEdtName.setText(employee.getName());
            eeaEdtShifts.setText(employee.ShowShifts());
            eeaEdtPayment.setText(String.valueOf(employee.getPayment()));
            eeaEdtPhone.setText(employee.getPhone());
            eeaEdtPhone.setEnabled(false);
            eeaBtnDelete.setVisibility(View.VISIBLE);
        }

        strPayOld = eeaEdtPayment.getText().toString();
        strShifOld = eeaEdtShifts.getText().toString();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setTitle("הנחיות למילוי שדה זה");
        alert.setMessage("יש למלא משמרות לשישה ימים בשבוע באמצעות מספרים המייצגים את מספר שעות העבודה ביום ו- ' , ' על מנת להפריד בין ימות השבוע."+"\n"+"לדוגמה: 1,9,6,7,5,3");

        eeaEdtShifts.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {



                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (eeaEdtShifts.getRight() - eeaEdtShifts.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        alert.show();

                        return true;
                    }
                }
                return false;
            }
        });

        eeaBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckError(view);
            }
        });
    }

    private void CheckError(View v){

        String name=eeaEdtName.getText().toString();
        String shifts=eeaEdtShifts.getText().toString();
        String payment=eeaEdtPayment.getText().toString();
        String phone=eeaEdtPhone.getText().toString();
        if (name.isEmpty()||name.length()<2)
            ShowError(eeaEdtName,"שם לא תקין");
        else if (shifts.isEmpty()||!shifts.contains(",")||!CheckShifts(shifts))
            ShowError(eeaEdtShifts,"משמרות לא תקינות"+"\n"+"יש לרשום משמרות לפי ההוראות");
        else if (payment.isEmpty())
            ShowError(eeaEdtPayment,"תשלם לא תקין");
        else if (phone.isEmpty()||phone.length()<10)
            ShowError(eeaEdtPhone,"מספר טלפון לא תקין");
        else
            FinishEdit(v);
    }

    private boolean CheckShifts(String shifts){
        if (shifts.length()<11)
            return false;
        int count=0;
        for (int i=1;i<=shifts.length();i++){
            if (Character.toString(shifts.charAt(shifts.length()-i)).equals(","))
                count++;
        }
        if (count==5)
            return true;
        return false;
    }


    private void ShowError(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }



    public void FinishEdit(View view) {
        Intent intent = new Intent(this, EmployeeActivity.class);
        c = new ContentValues();
        String strName = eeaEdtName.getText().toString();
        String strPayment = eeaEdtPayment.getText().toString();
        String strShifts = eeaEdtShifts.getText().toString();
        String strPhone = eeaEdtPhone.getText().toString();

        if (resultCode == 1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.Employee_Payment, eeaEdtPayment.getText().toString());
            contentValues.put(DBHelper.Employee_Shifts, eeaEdtShifts.getText().toString());
            db.update(DBHelper.Table_NameE, contentValues, DBHelper.Employee_Phone + "=?", new String[]{strPhone});
            db.close();
            startActivity(intent);
            finish();
            return;
        }

        SQLiteDatabase dbRead=dbHelper.getReadableDatabase();
        Cursor cursor=dbRead.query(DBHelper.Table_NameE,null,null,null,null,null,null);
        cursor.moveToFirst();
        Boolean b=true;
        while (!cursor.isAfterLast()){
            String phone=cursor.getString(4);
            if (phone.equals(strPhone)){
                ShowError(eeaEdtPhone,"עובד זה רשום במערכת");
                b=false;
            }
            cursor.moveToNext();
        }

        if (b) {
            c.put(DBHelper.Employee_Name, strName);
            c.put(DBHelper.Employee_Payment, strPayment);
            c.put(DBHelper.Employee_Shifts, strShifts);
            c.put(DBHelper.Employee_Phone, strPhone);
            SharedPreferences sharedPreferences = getSharedPreferences("managerEmail", Context.MODE_PRIVATE);
            c.put(DBHelper.Employee_ManagerEmail, sharedPreferences.getString("ManagerEmail", null));



            if (resultCode == 0) {
                db.insert(DBHelper.Table_NameE, null, c);
                db.close();
                startActivity(intent);
            }
        }
    }

    public void DeleteEmployee(View view) {
        Intent intent=new Intent(this,EmployeeActivity.class);
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("האם להסיר את העובד מהרשימה?");
        alert.setMessage("שם עובד: "+employee.getName());
        alert.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db=dbHelper.getWritableDatabase();
                db.delete(DBHelper.Table_NameE,DBHelper.Employee_Phone + "=?",new String[]{employee.getPhone()});
                db.close();
                startActivity(intent);
            }
        });
        alert.setNegativeButton("לא", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void toggleVisibility(Menu menu, @IdRes int id, boolean visible){
        menu.findItem(id).setVisible(visible);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.pre_Home:
                Intent intent=new Intent(this,HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.pre_About:
                Intent intent1=new Intent(this,AboutActivity.class);
                intent1.putExtra("Home",1);
                startActivity(intent1);
                break;
            case R.id.pre_dis:
                Intent intent2=new Intent(this,MainActivity.class);
                SharedPreferences sharedPreferences=getSharedPreferences("details1", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("Email",null);
                editor.putString("Pass",null);
                editor.putString("Name",null);
                editor.apply();
                startActivity(intent2);
                break;
        }

        return true;
    }


}