package com.example.salon.Presenter;

import android.content.Context;

import com.example.salon.Models.AuthenticationController;
import com.example.salon.Page;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class Page_presenter {

ViewPager viewPager;
    Page view;


    AuthenticationController controller;
    //save the context recievied via constructor in a local variable
    private Context context;


    public Page_presenter(Page view, Context contex) {
        this.view = view;
        this.context = contex;
        this.controller = new AuthenticationController();
    }

    public void sign_out(){

         LoginManager.getInstance().logOut();
        controller.logout();

    }
}