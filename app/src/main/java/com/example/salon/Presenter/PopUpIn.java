package com.example.salon.Presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.salon.MainActivity;
import com.example.salon.Models.AuthenticationController;
import com.example.salon.Page;
import com.example.salon.R;
import com.example.salon.View.PageOne;
import com.example.salon.View.PhoneAndName;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class PopUpIn {
//    FirebaseAuth auth; //authorization
//    FirebaseDatabase db; // connect to db
//    DatabaseReference users; // work with table
    RelativeLayout root ;

    MainActivity view;


    AuthenticationController controller;
//save the context recievied via constructor in a local variable
    private Context context;
    private FirebaseAuth mAuth;
    DatabaseReference ref;
    public PopUpIn(MainActivity view,Context contex){
    this.view=view;
    this.context=contex;
    this.controller=new AuthenticationController();
}



//    public PopUpIn(Context context){
//        this.context=context;
//    }
public void setActivityPage(){
    //view.finish();
    makeToast("I am here");
    intent();
}
    public void show_sign_in_window() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        //dialog.setTitle("Sign Up");
        //dialog.setMessage("Input data for registration");
        LayoutInflater inflater =LayoutInflater.from(context);
        View sing_in_window= inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(sing_in_window);

        final MaterialEditText email= sing_in_window.findViewById(R.id.emailField);
        final MaterialEditText pass= sing_in_window.findViewById(R.id.passField);


        dialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog_interface, int which) {
                dialog_interface.dismiss();
            }
        });

        dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog_interface, int which) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    makeToast("Input email");
                    //Snackbar.make(email.findViewById(R.id.emailField),"Input coorect password ot email",Snackbar.LENGTH_LONG).show();
                    return;//if we have an error we instantly go away from function
                }
                if(pass.getText().toString().length()<5){
                    makeToast("Input longer password");

                    //Snackbar.make(root,"Input coorect password ot email)",Snackbar.LENGTH_LONG).show();
                    return;//if we have an error we instantly go away from function
                }
                //auth of user
                controller.loginUser(PopUpIn.this,email.getText().toString(),pass.getText().toString());

            }

        });
        dialog.show();
    }

  public void  show_sign_up_window(){

      AlertDialog.Builder dialog=new AlertDialog.Builder(context);
      //dialog.setTitle("Sign Up");
      //dialog.setMessage("Input data for registration");
      LayoutInflater inflater =LayoutInflater.from(context);
      View registe_window= inflater.inflate(R.layout.register, null);
      dialog.setView(registe_window);

      final MaterialEditText email= registe_window.findViewById(R.id.emailField);
      final MaterialEditText pass= registe_window.findViewById(R.id.passField);
      final MaterialEditText phone= registe_window.findViewById(R.id.phoneField);
      final MaterialEditText name= registe_window.findViewById(R.id.nameField);


      dialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog_interface, int which) {
              dialog_interface.dismiss();
          }
      });

      dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog_interface, int which) {
              if(TextUtils.isEmpty(email.getText().toString())){
                  makeToast("Input email");
                  return;//if we have an error we instantly go away from function
              }
              if(TextUtils.isEmpty(name.getText().toString())){
                  makeToast("Input name");
                  //  Snackbar.make(root,"Input your name",Snackbar.LENGTH_LONG).show();
                  return;//if we have an error we instantly go away from function
              }
              if(TextUtils.isEmpty(phone.getText().toString())){
                  makeToast("Input phone");
                  //  Snackbar.make(root,"Input your phone",Snackbar.LENGTH_LONG).show();
                  return;//if we have an error we instantly go away from function
              }
              if(pass.getText().toString().length()<5){
                  makeToast("Input longer password");
                  // Snackbar.make(root,"Input longer password(>5 symbols)",Snackbar.LENGTH_LONG).show();
                  return;//if we have an error we instantly go away from function
              }
              //registration of user
                controller.registeUser(PopUpIn.this,name.getText().toString(),email.getText().toString(),pass.getText().toString(),phone.getText().toString());
              // makeToast("Okay, presenter");


            //  System.out.println(pass);

          }

      });
      dialog.show();

  }
    public void intent(){
        view.startActivity(new Intent(view.getApplicationContext(), Page.class));
        view.finish();

    }
    public void intent2(){
        view.startActivity(new Intent(view.getApplicationContext(), PhoneAndName.class));
        view.finish();

    }
    public void updateUIFB(FirebaseUser currentUser) {

        if (currentUser != null) {
            intent2();

        }
        else
        {makeToast("lol");}
    }
    public void updateUI(FirebaseUser currentUser) {

        if (currentUser != null) {
            intent();

        }
        else
        {makeToast("lol");}
    }
    public void handleFacebookAccessToken(AccessToken token) {
        // Log.d(TAG, "handleFacebookAccessToken:" + token);
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            String uid=user.getUid();
                            ref= FirebaseDatabase.getInstance().getReference().child("server").child("saving-data").child("fireblog").child("Users").child(uid);
                            ref.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Object phone =dataSnapshot.getValue();
                                    if (phone==null){
                                        updateUIFB(user);}
                                    else{   updateUI(user);}

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void makeToast(String message) {
        Toast.makeText(view, message, Toast.LENGTH_LONG).show();
    }
//    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//       // Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//        makeToast("googleeee");
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//     //                       Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUIFB(user);
//                        } else {
//                                makeToast("lol");
//                            // If sign in fails, display a message to the user.
//       ////                     Log.w(TAG, "signInWithCredential:failure", task.getException());
//                   //         Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }

                        // ...
                    }

//    public void onCreate(){
//        auth = FirebaseAuth.getInstance(); //start authentication in DB
//        db= FirebaseDatabase.getInstance(); // connect to DB
//        users=db.getReference("Users");
//
//    }


