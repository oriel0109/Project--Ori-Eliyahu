package com.example.projectorieliyahu.Activities;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.projectorieliyahu.R;
import com.google.android.material.navigation.NavigationView;

public class AboutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(),R.id.pre_dis,false);
        toggleVisibility(navigationView.getMenu(),R.id.i1,false);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                Intent intent1=getIntent();
                int i=intent1.getIntExtra("Home",0);
                if (i==1){
                    Intent intent=new Intent(this,HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.pre_About:
                break;
            case R.id.pre_dis:
                break;
        }
        return true;
    }
}