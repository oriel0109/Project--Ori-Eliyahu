package com.example.projectorieliyahu.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.projectorieliyahu.DataBase.*;
import com.example.projectorieliyahu.R;
import com.google.android.material.navigation.NavigationView;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText laEdtEmail;
    EditText laEdtPass;
    TextView laTxtRegister;
    Button laBtnLogin;

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c;
    CheckBox laRbRem;

    SharedPreferences sharedPreferences;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(),R.id.pre_dis,false);
        toggleVisibility(navigationView.getMenu(),R.id.i1,false);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        laEdtEmail=findViewById(R.id.laEdtEmail);
        laEdtPass=findViewById(R.id.laEdtPass);
        laTxtRegister=findViewById(R.id.laTxtRegister);
        laBtnLogin=findViewById(R.id.laBtnLogin);

        String text="אין לך משתמש? הרשם/י";
        SpannableString ss=new SpannableString(text);
        Intent intent=new Intent(this,RegisterActivity.class);
        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(intent);
            }
        };
        ss.setSpan(clickableSpan,14,20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        laTxtRegister.setText(ss);
        laTxtRegister.setMovementMethod(LinkMovementMethod.getInstance());

        dbHelper=new DBHelper(this);
        db=dbHelper.getReadableDatabase();

        laRbRem=findViewById(R.id.laRbRem);
        laBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckError(view);
            }
        });

    }


    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private void CheckError(View v){

        String email=laEdtEmail.getText().toString();
        String pass=laEdtPass.getText().toString();
        if (!isValidEmailId(email))
            ShowError(laEdtEmail,"אימייל לא תקין");
        else if (pass.isEmpty()||pass.length()<6)
            ShowError(laEdtPass,"סיסמה לא תקינה");
        else
            LoginIn(v);
    }

    private void ShowError(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }

    public void LoginIn(View view) {
        String strEmail="";
        String strPass="";

        Intent intent=new Intent(this,HomeActivity.class);


            strEmail=laEdtEmail.getText().toString();
            strPass=laEdtPass.getText().toString();
            c=db.query(dbHelper.Table_NameR,null,null,null,null,null,null);
            c.moveToFirst();
            while (!c.isAfterLast()){
                String str1=c.getString(c.getColumnIndexOrThrow(DBHelper.Manager_Email));
                String str2=c.getString(c.getColumnIndexOrThrow(DBHelper.Manager_Pass));
                if (str1.matches(strEmail)&&str2.matches(strPass)){
                    intent.putExtra("Name",c.getString(c.getColumnIndexOrThrow(DBHelper.Manager_Name)));
                    if (laRbRem.isChecked()){
                        sharedPreferences=getSharedPreferences("details1", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("Email",strEmail);
                        editor.putString("Pass",c.getString(4));
                        editor.apply();
                    }
                    db.close();
                    SharedPreferences sharedPreferences1=getSharedPreferences("managerEmail",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1=sharedPreferences1.edit();
                    String managerEmail=c.getString(c.getColumnIndexOrThrow(DBHelper.Manager_Email));
                    editor1.putString("ManagerEmail",managerEmail);
                    editor1.apply();
                    startActivity(intent);
                    return;
                }
                c.moveToNext();

            }
            Toast.makeText(this, "אימייל או סיסמה אינם נכונים", Toast.LENGTH_SHORT).show();
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
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.pre_About:
                Intent intent1=new Intent(this,AboutActivity.class);
                startActivity(intent1);
                break;
            case R.id.pre_dis:
                break;
        }

        return true;
    }
}