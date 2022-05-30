package com.example.projectorieliyahu.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectorieliyahu.Model.BitmapHelper;
import com.example.projectorieliyahu.Model.Product;
import com.example.projectorieliyahu.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {


    private List<Product> productList;
    TextView productName;
    TextView productQuantity;
    ImageView productImg;

    public ProductAdapter(@NonNull Context context, int resource, List<Product> productList) {
        super(context, resource,productList);
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null){
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.product_layout,parent,false);
        }

        productName=convertView.findViewById(R.id.plTxtName);
        productQuantity=convertView.findViewById(R.id.plTxtProduct);
        productImg=convertView.findViewById(R.id.plImg);

        productName.setText(productList.get(position).getName());
        productQuantity.setText(String.valueOf(productList.get(position).getQuantity()));
        productImg.setImageBitmap(BitmapHelper.DecodeBase64(productList.get(position).getImg()));

        return convertView;
    }
}


















