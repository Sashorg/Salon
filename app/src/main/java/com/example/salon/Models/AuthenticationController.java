package com.example.salon.Models;

import android.content.Intent;
import android.widget.RelativeLayout;

import com.example.salon.MainActivity;
import com.example.salon.Presenter.LoginPresenter;
import com.example.salon.Presenter.PopUpIn;
import com.example.salon.R;
import com.example.salon.View.PageOne;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import androidx.annotation.NonNull;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import static androidx.core.content.ContextCompat.startActivity;

public class AuthenticationController {


    public void loginUser(final PopUpIn presenter, final String email, final String password){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                presenter.setActivityPage();}
                else {
                    presenter.makeToast("Wrong Username or Password");
                }
            }


        });

}
 public void registeUser(final PopUpIn presenter,final String name,final String email, final String pass, final String phone){


    //  presenter.makeToast("okay, start registration");
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass).addOnCompleteListener( new OnCompleteListener<AuthResult>() {

         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful()){

//                 presenter.makeToast("Good");
                 User user =new User(email, pass, name, phone);
                 user.setName(name);
                 user.setEmail(email);
                 user.setPass(pass);
                 user.setPhone(phone);
                 FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
  //                       presenter.makeToast("User added");
                         // Snackbar.make(root,"User added",Snackbar.LENGTH_LONG);
                     }
                 });
                 presenter.setActivityPage();
               //  Snackbar.make(root,"Failed",Snackbar.LENGTH_LONG);
             }
             else {
                 presenter.makeToast("This email is taken");
                 }


         }
         });
     }

    public void logout() {

        FirebaseAuth.getInstance().signOut();
    }
}
