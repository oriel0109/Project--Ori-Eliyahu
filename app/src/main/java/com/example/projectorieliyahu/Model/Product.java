package com.example.projectorieliyahu.Model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;

public class Product implements Serializable {

    private String name;
    private double quantity;
    private String img;

    public Product(String name, double quantity, String img) {
        this.name = name;
        this.quantity = quantity;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
