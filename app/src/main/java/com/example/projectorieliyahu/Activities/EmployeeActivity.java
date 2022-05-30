package com.example.projectorieliyahu.Activities;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.projectorieliyahu.Adapters.ProductAdapter;
import com.example.projectorieliyahu.DataBase.*;
import com.example.projectorieliyahu.Model.*;
import com.example.projectorieliyahu.R;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    ListView eaLtvEmployee;
    ImageButton eaImagPlus;
    ArrayList<Employee> employeeList;
    ArrayAdapter<Employee> arrayAdapter;
    Employee e;


    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c;
    ContentValues contentValues;

    DBHelperRegister dbHelperRegister;
    SQLiteDatabase db1;

    ArrayList<String> list;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(),R.id.i1,false);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eaLtvEmployee=findViewById(R.id.eaLtvEmployee);
        eaImagPlus=findViewById(R.id.eaImagPlus);
        employeeList=new ArrayList<>();

        dbHelper=new DBHelper(this);
        db=dbHelper.getReadableDatabase();

        Intent intent=getIntent();


        c=db.query(DBHelper.Table_NameE,null,null,null,null,null,null);
        c.moveToFirst();
        SharedPreferences sharedPreferences=getSharedPreferences("managerEmail",Context.MODE_PRIVATE);
        String managerEmail=sharedPreferences.getString("ManagerEmail",null);
        while (!c.isAfterLast()){
            while (c.getString(3).equals(managerEmail)){
                int [] shifts=new int[5];
                e=new Employee("",0,shifts,"");
                e.setName(c.getString(0));
                e.setPayment(Double.valueOf(c.getString(1)));
                e.setShifts(GetShifts(c.getString(2)));
                e.setPhone(c.getString(4));
                employeeList.add(e);
                break;
            }
            c.moveToNext();
        }
        db.close();

        RefreshListView();
        eaLtvEmployee.setOnItemClickListener(this);
    }


    public int[] GetShifts(String strShifts){
        int[] shifts=new int[6];
        String [] temp;
        temp=strShifts.split(",");

        for (int i=0;i<shifts.length;i++){
            shifts[i]=Integer.parseInt(temp[i]);
        }

        return shifts;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        e=arrayAdapter.getItem(position);
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("פרטי עובד");
        alert.setMessage("שם עובד:"+e.getName().toString()+"\n"+"שכר עובד:"+e.getPayment()+"\n"+"מספר שעות שעבד החודש:"+String.valueOf(e.SumOfHours())+"\n"+"תשלום שבועי:"+String.valueOf(e.SumOfHours()*e.getPayment())+"\n"+"מספר טךפון:"+e.getPhone());
        alert.setCancelable(true);
        Intent intent=new Intent(this,EditEmployeeActivity.class);
        Intent intent2=new Intent(this,SendMessageActivity.class);
        alert.setPositiveButton("עריכת פרטי עובד", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intent.putExtra("ResultCode",1);
                intent.putExtra("Employee",(Serializable) e);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("שליחת הודעה", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent2.putExtra("PhoneNum",e.getPhone());
                intent2.putExtra("Ename",e.getName());
                startActivity(intent2);
            }
        });
        alert.show();
    }


    @Override
    public void onBackPressed() {
        return;
    }

    public void Plus(View view) {
        Intent intent=new Intent(this,EditEmployeeActivity.class);
        intent.putExtra("ResultCode",0);
        startActivity(intent);
    }



    public void RefreshListView(){
        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,employeeList);
        eaLtvEmployee.setAdapter(arrayAdapter);

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