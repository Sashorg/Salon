package com.example.salon.Presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.salon.Models.AuthenticationController;
import com.example.salon.Page;
import com.example.salon.View.PhoneAndName;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

public class PhoneNumPresenter {
    PhoneAndName view;
    AuthenticationController controller;
    //save the context recievied via constructor in a local variable
    private Context context;

    public PhoneNumPresenter(PhoneAndName view, Context contex) {
        this.view=view;
        this.context = contex;
        this.controller = new AuthenticationController();
    }
    public void makeToast(String message) {
        Toast.makeText(view, message, Toast.LENGTH_SHORT).show();
    }

    public void sign_out(){
      

        controller.logout();

    }

    public void intent() {
        view.startActivity(new Intent(view.getApplicationContext(), Page.class));
        view.finish();
    }
}
