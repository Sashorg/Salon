package com.example.salon.Presenter;

import com.example.salon.Models.User;
import com.example.salon.View.PerLoginView;

public class LoginPresenter implements PerLoginPresenter {
    PerLoginView  loginView;
    public LoginPresenter(PerLoginView loginView){
        this.loginView=loginView;
    }
    @Override
    public void onLogin(String email, String pass) {



    }
}
