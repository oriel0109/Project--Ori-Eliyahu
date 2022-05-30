package com.example.projectorieliyahu.Activities;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.projectorieliyahu.DataBase.DBHelper;
import com.example.projectorieliyahu.Model.*;
import com.example.projectorieliyahu.R;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView haTxtVtxt;
    SharedPreferences sharedPreferences;
    DBHelper dbHelper;
    SQLiteDatabase db;
    String name="";

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(),R.id.i1,false);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        haTxtVtxt=findViewById(R.id.haTxtVtxt);

        sharedPreferences=getSharedPreferences("details1", Context.MODE_PRIVATE);
        String email=sharedPreferences.getString("Email",null);
        String pass=sharedPreferences.getString("Pass",null);
        String spName=sharedPreferences.getString("Name",null);

        Intent intent=getIntent();

        if (spName!=null){
            name=spName;
        }

        else if (email!=null&&pass!=null){
            dbHelper=new DBHelper(this);
            db=dbHelper.getReadableDatabase();
            Cursor c;
            c=db.query(dbHelper.Table_NameR,null,null,null,null,null,null);
            c.moveToFirst();
            while (!c.isAfterLast()){
                String intentEmail=c.getString(2);
                if (intentEmail.matches(email)){
                    name=c.getString(0);
                    break;
                }
                c.moveToNext();
            }

        }
        else {
            name=intent.getStringExtra("Name");
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Name",name);
            editor.apply();
        }

        String lastName = "";
        String firstName= "";
        if(name.split("\\w+").length>1){

            lastName = name.substring(name.lastIndexOf(" ")+1);
            firstName = name.substring(0, name.lastIndexOf(' '));
        }
        else{
            firstName = name;
        }
        haTxtVtxt.setText("שלום "+firstName);
    }



    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    public void Employee(View view) {
        Intent intent=new Intent(this, EmployeeActivity.class);
        startActivity(intent);
    }

    public void Workers(View view) {
        Intent intent=new Intent(this,ProductActivity.class);
        startActivity(intent);
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
                break;
            case R.id.pre_About:
                Intent intent1=new Intent(this,AboutActivity.class);
                intent1.putExtra("Home",1);
                startActivity(intent1);
                break;
            case R.id.pre_dis:
                Intent intent2=new Intent(this,MainActivity.class);
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