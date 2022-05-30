package com.example.projectorieliyahu.Activities;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.widget.ListView;

import com.example.projectorieliyahu.Adapters.ProductAdapter;
import com.example.projectorieliyahu.DataBase.DBHelper;
import com.example.projectorieliyahu.DataBase.DBHelperProduct;
import com.example.projectorieliyahu.Model.Product;
import com.example.projectorieliyahu.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, NavigationView.OnNavigationItemSelectedListener {

    ListView paLtvProduct;
    ArrayList<Product> productList;
    ProductAdapter productAdapter;

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c;
    Product p;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(),R.id.i1,false);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        paLtvProduct=findViewById(R.id.paLtvProduct);
        productList=new ArrayList<>();
        dbHelper=new DBHelper(this);
        db=dbHelper.getReadableDatabase();


        c=db.query(DBHelper.Table_NameP,null,null,null,null,null,null);
        c.moveToFirst();
        SharedPreferences sharedPreferences=getSharedPreferences("managerEmail",Context.MODE_PRIVATE);
        String managerEmail=sharedPreferences.getString("ManagerEmail",null);
        while (!c.isAfterLast()){
            while (c.getString(2).equals(managerEmail)){
                p=new Product("",0,"");
                p.setName(c.getString(0));
                p.setQuantity(Double.valueOf(c.getString(1)));
                p.setImg((c.getString(3)));
                productList.add(p);
                break;
            }
            c.moveToNext();
        }
        db.close();
        RefreshListView();
        paLtvProduct.setOnItemLongClickListener(this);

    }

    public void Plus(View view) {
        Intent intent=new Intent(this,EditProductActivity.class);
        startActivity(intent);
    }

    public void show(View view) {
        Intent intent=getIntent();
        Product product=(Product) intent.getSerializableExtra("Product");
        productList.add(product);
        RefreshListView();

    }

    public void RefreshListView(){
        productAdapter=new ProductAdapter(this,R.layout.product_layout,productList);
        paLtvProduct.setAdapter(productAdapter);

    }

    @Override
    public void onBackPressed() {
        return;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Product product=productAdapter.getItem(position);
        dbHelper=new DBHelper(this);
        db=dbHelper.getReadableDatabase();
        Intent intent=new Intent(this,EditProductActivity.class);
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("שינוי מוצר");
        alert.setMessage("שם המוצר: "+product.getName());
        alert.setCancelable(true);
        alert.setPositiveButton("עריכת מוצר", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent.putExtra("ResultCode",1);
                intent.putExtra("Product",product);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("מחיקת מוצר", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.delete(DBHelper.Table_NameP,"name=?",new String[]{product.getName()});
                db.close();
                productList.remove(position);
                RefreshListView();
            }
        });
        alert.show();
        return false;
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