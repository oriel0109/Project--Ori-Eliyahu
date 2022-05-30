package com.example.projectorieliyahu.Activities;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectorieliyahu.BroadcastReceiver.NetworkChangerReciever;
import com.example.projectorieliyahu.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class SendMessageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    List<String> messages;
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;
    TextView smaTxtName;

    String message="";
    String phone="";

    BroadcastReceiver broadcastReceiver;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(),R.id.i1,false);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        broadcastReceiver=new NetworkChangerReciever();
        RegisterNetworkBroadcastReceiver();

        smaTxtName=findViewById(R.id.smaTxtName);
        Intent intent=getIntent();
        smaTxtName.setText(intent.getStringExtra("Ename"));
        phone=intent.getStringExtra("PhoneNum");

        messages=new ArrayList<>();
        messages.add("נכנסה לך משכורת");
        messages.add("יש להגיש משמרות");
        messages.add("עודכנו לך משמרות");
        messages.add("חזור אליי בהקדם האפשרי");
        messages.add("משמרת טובה, כל הכבוד");
        spinner=findViewById(R.id.smaSpinner);
        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice,messages);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

    }

    protected void RegisterNetworkBroadcastReceiver(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }
    }

    protected void UnregisterNetwork(){
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UnregisterNetwork();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         message=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void SendSms(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
        == PackageManager.PERMISSION_GRANTED){
            sendMessage();
        }
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},100);
        }

    }

    private void sendMessage(){
        if (!phone.equals("")&&!message.equals("")){
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,message,null,null);
            Toast.makeText(this, "ההודעה נשלחה", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "בחר הודעה", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100&&grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            sendMessage();
        }
        else
            Toast.makeText(this, "הרשאה נדחתה", Toast.LENGTH_SHORT).show();
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