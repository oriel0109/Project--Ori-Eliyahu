package com.example.projectorieliyahu.Activities;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projectorieliyahu.DataBase.DBHelperProduct;
import com.example.projectorieliyahu.Model.BitmapHelper;
import com.example.projectorieliyahu.DataBase.DBHelper;
import com.example.projectorieliyahu.Model.Product;
import com.example.projectorieliyahu.R;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

public class EditProductActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText epaEdtName;
    EditText epaEdtQuantity;
    ImageView epaImg;
    Button epaBtnFinish;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    String id;
    Product product;

    Uri imageUri;
    DBHelper dbHelper;
    SQLiteDatabase db;
    boolean isPhoto = false;
    Context context;
    String imageFileLocation = "nothing";
    private String imageFilename;

    Bitmap selectedImage;
    String picturePath = "";
    int resultCode = 0;

    String quantityOld = "";
    String imgOld = "";

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(),R.id.i1,false);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        epaEdtName = findViewById(R.id.epaEdtName);
        epaEdtQuantity = findViewById(R.id.epaEdtQuantity);
        epaImg = findViewById(R.id.epaImg);
        id = epaImg.getDrawable().toString();
        epaBtnFinish=findViewById(R.id.epaBtnFinish);

        Intent intent = getIntent();
        resultCode = intent.getIntExtra("ResultCode", 0);
        if (resultCode == 1) {
            Product product = (Product) intent.getSerializableExtra("Product");
            epaEdtName.setText(product.getName());
            epaEdtName.setEnabled(false);
            epaEdtQuantity.setText(String.valueOf(product.getQuantity()));
            Bitmap bitmap = BitmapHelper.DecodeBase64(product.getImg());
            epaImg.setImageBitmap(bitmap);
            quantityOld = String.valueOf(product.getQuantity());
            imgOld = product.getImg();
        }

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

    }

    public void Pic(View view) {

        if (checkAndRequestPermissions(this)) {
            chooseImage(this);
        }

        epaBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckError(view);
            }
        });


    }

    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    // function to check permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    // Handled permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(this);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
            epaImg.setImageURI(imageUri);
            isPhoto = true;
        }

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        selectedImage = (Bitmap) data.getExtras().get("data");
                        epaImg.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);
                                epaImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }



    private void CheckError(View v){

        String name=epaEdtName.getText().toString();
        String quantity=epaEdtQuantity.getText().toString();
        if (name.isEmpty()||name.length()<2)
            ShowError(epaEdtName,"שם לא תקין");
        else if (quantity.isEmpty())
            ShowError(epaEdtQuantity,"כמות לא תקינה");
        else if (epaImg.getDrawable().toString().matches(id))
            Toast.makeText(this, "תמונה לא תקינה", Toast.LENGTH_SHORT).show();
        else
            FinishProduct(v);
    }

    private void ShowError(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }

    public void FinishProduct(View view) {

          if (resultCode == 1) {
            String strName = epaEdtName.getText().toString();
            Double d = Double.valueOf(epaEdtQuantity.getText().toString());


            epaImg.buildDrawingCache();
            Bitmap bmap = epaImg.getDrawingCache();
            String byteArray = BitmapHelper.BitMaptoByte(bmap);

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.Product_Quantity, String.valueOf(d));
            contentValues.put(DBHelper.Product_Picture, byteArray);
            db.update(DBHelper.Table_NameP, contentValues, DBHelper.Product_Name + "=?", new String[]{strName});
            db.close();

            Intent intent = new Intent(this, ProductActivity.class);
            startActivity(intent);
        } else {

            String strName = epaEdtName.getText().toString();
            String strQuantity = epaEdtQuantity.getText().toString();
            Double d = Double.valueOf(strQuantity);


            epaImg.buildDrawingCache();
            Bitmap bmap = epaImg.getDrawingCache();
            String byteArray = BitmapHelper.BitMaptoByte(bmap);

              SQLiteDatabase dbRead=dbHelper.getReadableDatabase();
              Cursor cursor=dbRead.query(DBHelper.Table_NameP,null,null,null,null,null,null);
              cursor.moveToFirst();
              Boolean b=true;
              while (!cursor.isAfterLast()){
                  String name=cursor.getString(0);
                  if (name.equals(strName)){
                      ShowError(epaEdtName,"מוצר זה כבר קיים במערכת");
                      b=false;
                  }
                  cursor.moveToNext();
              }

             if (b) {
                 ContentValues contentValues = new ContentValues();
                 contentValues.put(DBHelper.Product_Name, strName);
                 contentValues.put(DBHelper.Product_Quantity, strQuantity);
                 contentValues.put(DBHelper.Product_Picture, byteArray);
                 SharedPreferences sharedPreferences = getSharedPreferences("managerEmail", Context.MODE_PRIVATE);
                 contentValues.put(DBHelper.Product_ManagerEmail, sharedPreferences.getString("ManagerEmail", null));
                 db.insert(DBHelper.Table_NameP, null, contentValues);
                 db.close();

                 Intent intent = new Intent(this, ProductActivity.class);
                 startActivity(intent);
             }


        }


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