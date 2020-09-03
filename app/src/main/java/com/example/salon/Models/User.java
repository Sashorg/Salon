package com.example.salon.Models;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class User implements PerUser{
 private String email,pass,name,phone;

    public User(String email, String pass, String name, String phone) {
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.phone = phone;
    }
    public User(String email,  String name, String phone) {
        this.email = email;

        this.name = name;
        this.phone = phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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
}
