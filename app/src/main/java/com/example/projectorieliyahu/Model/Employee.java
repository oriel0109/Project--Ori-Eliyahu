package com.example.projectorieliyahu.Model;

import java.io.Serializable;
import java.util.Arrays;

public class Employee implements Serializable {

    private String name;
    private String phone;
    private double payment;
    private int []shifts;

    public Employee(String name, double payment, int[] shifts,String phone) {
        this.name = name;
        this.payment = payment;
        this.shifts = shifts;
        this.phone=phone;
    }


    public double SumOfHours(){
        double sum=0;
        for (int i=0;i<shifts.length;i++){
            sum+=shifts[i];
        }
        return sum;
    }

    public String ShowShifts(){
        String str=String.valueOf(this.shifts[0]);
        for (int i=1;i<6;i++){
            str+=","+String.valueOf(this.shifts[i]);
        }
        return str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public int[] getShifts() {
        return shifts;
    }

    public void setShifts(int[] shifts) {
        this.shifts = shifts;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return name+" ";


    }
}
