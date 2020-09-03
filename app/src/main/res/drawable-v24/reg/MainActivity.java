package com.example.reg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.reg.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button buttonIn,buttonReg;
    FirebaseAuth auth; //authorization
    FirebaseDatabase db; // connect to db
    DatabaseReference users; // work with table
    RelativeLayout root ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonIn=(Button)findViewById(R.id.buttonIn);
        root=findViewById(R.id.root_element);
        buttonReg=(Button)findViewById(R.id.buttonReg);
        auth = FirebaseAuth.getInstance(); //start authentication in DB
        db= FirebaseDatabase.getInstance(); // connect to DB
        users=db.getReference("Users");
        if(auth.getCurrentUser() != null){
            finish();}
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
              show_regiter_window();
            }
        });
        buttonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_sign_in_window();
            }
        });


    }

    private void show_sign_in_window() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        //dialog.setTitle("Sign Up");
        //dialog.setMessage("Input data for registration");
        LayoutInflater inflater =LayoutInflater.from(this);
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
                    Snackbar.make(root,"Input coorect password ot email",Snackbar.LENGTH_LONG).show();
                    return;//if we have an error we instantly go away from function
                }
                if(pass.getText().toString().length()<5){
                    Snackbar.make(root,"Input coorect password ot email)",Snackbar.LENGTH_LONG).show();
                    return;//if we have an error we instantly go away from function
                }
                //auth of user
          auth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
              @Override
              public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(MainActivity.this, MapGirl.class));
                finish();
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Snackbar.make(root, "Failed to sign in",Snackbar.LENGTH_LONG);
              }
          });

            }

        });
        dialog.show();
    }

    private void show_regiter_window() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        //dialog.setTitle("Sign Up");
        //dialog.setMessage("Input data for registration");
        LayoutInflater inflater =LayoutInflater.from(this);
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
                        Snackbar.make(root,"Input your email",Snackbar.LENGTH_LONG).show();
                        return;//if we have an error we instantly go away from function
                    }
                    if(TextUtils.isEmpty(name.getText().toString())){
                        Snackbar.make(root,"Input your name",Snackbar.LENGTH_LONG).show();
                        return;//if we have an error we instantly go away from function
                    }
                    if(TextUtils.isEmpty(phone.getText().toString())){
                        Snackbar.make(root,"Input your phone",Snackbar.LENGTH_LONG).show();
                        return;//if we have an error we instantly go away from function
                    }
                    if(pass.getText().toString().length()<5){
                        Snackbar.make(root,"Input longer password(>5 symbols)",Snackbar.LENGTH_LONG).show();
                        return;//if we have an error we instantly go away from function
                    }
                  //registration of user

                 auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Snackbar.make(root,"Failed",Snackbar.LENGTH_LONG);
                    }
                    else {
                        User user =new User();
                        user.setName(name.getText().toString());
                        user.setEmail(email.getText().toString());
                        user.setPass(pass.getText().toString());
                        user.setPhone(phone.getText().toString());
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                 Snackbar.make(root,"User added",Snackbar.LENGTH_LONG);
                            }
                        });
                        startActivity(new Intent(MainActivity.this,MapGirl.class));

                    }
                     }
                 });

                }

            });
        dialog.show();
    }

}
