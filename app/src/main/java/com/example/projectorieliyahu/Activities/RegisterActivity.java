package com.example.projectorieliyahu.Activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.projectorieliyahu.DataBase.*;
import com.example.projectorieliyahu.Model.*;
import com.example.projectorieliyahu.R;
import com.google.android.material.navigation.NavigationView;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText raEdtName;
    EditText raEdtPhone;
    EditText raEdtEmail;
    EditText raEdtId;
    EditText raEdtPass;
    EditText raEdtPassCon;
    EditText raEdtNameBak;
    TextView raTxtLogin;
    Button raBtnRegister;

    Manager manager;
    DBHelper dbHelper;
    SQLiteDatabase db;
    ContentValues contentValues;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(),R.id.pre_dis,false);
        toggleVisibility(navigationView.getMenu(),R.id.i1,false);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        raEdtName=findViewById(R.id.raEdtName);
        raEdtPhone=findViewById(R.id.raEdtPhone);
        raEdtEmail=findViewById(R.id.raEdtEmail);
        raEdtId=findViewById(R.id.raEdtId);
        raEdtPass=findViewById(R.id.raEdtPass);
        raEdtPassCon=findViewById(R.id.raEdtPassCon);
        raEdtNameBak=findViewById(R.id.raEdtNameBak);
        raTxtLogin=findViewById(R.id.raTxtLogin);
        raBtnRegister=findViewById(R.id.raBtnRegister);

        String text="כבר יש לך חשבון? התחבר/י";
        SpannableString ss=new SpannableString(text);
        Intent intentL=new Intent(this,LoginActivity.class);
        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(intentL);
            }
        };
        ss.setSpan(clickableSpan,16,24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        raTxtLogin.setText(ss);
        raTxtLogin.setMovementMethod(LinkMovementMethod.getInstance());

        dbHelper=new DBHelper(this);
        db=dbHelper.getWritableDatabase();

        raBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckError(v);
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

        String email=raEdtEmail.getText().toString();
        String phone=raEdtPhone.getText().toString();
        String name=raEdtName.getText().toString();
        String id=raEdtId.getText().toString();
        String pass=raEdtPass.getText().toString();
        String passCon=raEdtPassCon.getText().toString();
        String bak=raEdtNameBak.getText().toString();

         if (name.isEmpty()||name.length()<2)
            ShowError(raEdtName,"שם לא תקין");
       else if (phone.isEmpty()||phone.length()<10)
            ShowError(raEdtPhone,"מספר טלפון לא תקין");
        else if (!isValidEmailId(email))
            ShowError(raEdtEmail,"אימייל לא תקין");
        else if (id.isEmpty()||id.length()<9)
            ShowError(raEdtId,"תעודת זהות לא תקינה");
        else if (pass.isEmpty()||pass.length()<6)
            ShowError(raEdtPass,"סיסמה לא תקינה");
        else if (passCon.isEmpty()||passCon.length()<6)
            ShowError(raEdtPassCon,"סיסמה לא תקינה");
        else if (!pass.equals(passCon))
            ShowError(raEdtPassCon,"סיסמה לא תואמת");
        else if (bak.isEmpty())
            ShowError(raEdtNameBak,"שם מאפייה לא תקין");
        else
            RegisterSave(v);
    }

    private void ShowError(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }

    public void RegisterSave(View view) {
        String strName="";
        String strPhone="";
        String strEmail="";
        String strId="";
        String strPass="";
        String strPassCon="";
        String strNameBak="";




            strName=raEdtName.getText().toString();
            strPhone=raEdtPhone.getText().toString();
            strEmail=raEdtEmail.getText().toString();
            strId=raEdtId.getText().toString();
            strPass=raEdtPass.getText().toString();
            strPassCon=raEdtPassCon.getText().toString();
            strNameBak=raEdtNameBak.getText().toString();


                SQLiteDatabase dbRead=dbHelper.getReadableDatabase();
                Cursor cursor=dbRead.query(DBHelper.Table_NameR,null,null,null,null,null,null);
                cursor.moveToFirst();
                Boolean b=true;
                while (!cursor.isAfterLast()){
                    String email=cursor.getString(2);
                    String phone=cursor.getString(1);
                    String id=cursor.getString(3);
                    if (email.equals(strEmail)) {
                        ShowError(raEdtEmail, "אימייל זה קיים במערכת");
                        b=false;
                    }
                   else if (phone.equals(strPhone)) {
                        ShowError(raEdtPhone, "מספר טלפון זה זה קיים במערכת");
                        b=false;
                    }
                  else if (id.equals(strId)) {
                        ShowError(raEdtId, "תעודת זהות זאת קיימת במערכת");
                        b=false;
                    }
                  cursor.moveToNext();
                }
                if (b) {
                    manager = new Manager(strName, strPhone, strEmail, strId, strPass, strNameBak);
                    Intent intent = new Intent(this, LoginActivity.class);
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("האם לשמור את הנתונים?");
                    alert.setMessage("שם מלא:" + strName + "\n" + "מספר טלפון:" + strPhone + "\n" + "אימייל:" + strEmail + "\n" + "תעודת זהות:" + strId + "\n" + "סיסמה:" + strPass + "\n" + "שם מאפייה:" + strNameBak);
                    alert.setCancelable(false);
                    alert.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            contentValues = new ContentValues();
                            contentValues.put(DBHelper.Manager_Name, manager.getName());
                            contentValues.put(DBHelper.Manager_Phone, manager.getPhone());
                            contentValues.put(DBHelper.Manager_Email, manager.getEmail());
                            contentValues.put(DBHelper.Manager_Id, manager.getId());
                            contentValues.put(DBHelper.Manager_Pass, manager.getPass());
                            contentValues.put(DBHelper.Manager_NameBak, manager.getNameBak());
                            db.insert(dbHelper.Table_NameR, null, contentValues);
                            db.close();

                            startActivity(intent);
                        }
                    });
                    alert.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();
                }

    }

    public void MoveToLogin(View view) {
        Intent intent=new Intent(this,LoginActivity.class);
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