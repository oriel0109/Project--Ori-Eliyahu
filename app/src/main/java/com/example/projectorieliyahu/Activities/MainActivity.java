package com.example.projectorieliyahu.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.projectorieliyahu.DataBase.DBHelper;
import com.example.projectorieliyahu.DataBase.DBHelperEmployee;
import com.example.projectorieliyahu.DataBase.DBHelperRegister;
import com.example.projectorieliyahu.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DBHelper dbHelper;
    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    SQLiteDatabase dbRead;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(),R.id.pre_dis,false);
        toggleVisibility(navigationView.getMenu(),R.id.i1,false);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        sharedPreferences=getSharedPreferences("details1", Context.MODE_PRIVATE);
        String email=sharedPreferences.getString("Email",null);
        String pass=sharedPreferences.getString("Pass",null);
        dbHelper=new DBHelper(this);
        if (email!=null&&pass!=null){
            Intent intent=new Intent(this,HomeActivity.class);
            dbRead=dbHelper.getReadableDatabase();
            Cursor c;
            c=dbRead.query(dbHelper.Table_NameR,null,null,null,null,null,null);
            c.moveToFirst();
            while (!c.isAfterLast()){
                String intentEmail=c.getString(2);
                if (intentEmail.matches(email)){
                    String intentName=c.getString(0);
                    intent.putExtra("Name",intentName);
                    startActivity(intent);
                    return;
                }
                c.moveToNext();
            }
        }


       db=dbHelper.getWritableDatabase();




    }


    public void Login(View view) {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }



    public void Register(View view) {
        Intent intent=new Intent(this,RegisterActivity.class);
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
                Intent intent=new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.pre_dis:
                break;

        }

        return true;
    }
}