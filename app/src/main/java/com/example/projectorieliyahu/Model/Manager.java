package com.example.projectorieliyahu.Model;

import java.io.Serializable;

public class Manager implements Serializable {

    private String name;
    private String phone;
    private String email;
    private String id;
    private String pass;
    private String nameBak;

    public Manager(String name, String phone, String email, String id, String pass, String nameBak) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.id = id;
        this.pass = pass;
        this.nameBak = nameBak;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNameBak() {
        return nameBak;
    }

    public void setNameBak(String nameBak) {
        this.nameBak = nameBak;
    }


}
