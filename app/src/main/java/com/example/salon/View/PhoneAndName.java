package com.example.salon.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.salon.MainActivity;
import com.example.salon.Models.User;
import com.example.salon.Presenter.PhoneNumPresenter;
import com.example.salon.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PhoneAndName extends AppCompatActivity {
    public GoogleSignInClient mGoogleSignInClient;
    public PhoneAndName (){


    }

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog");
    PhoneNumPresenter presenter= new PhoneNumPresenter(this,this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_and_name);
     final  DatabaseReference usersRef = ref.child("Users");
      Button  sgn_btn=findViewById(R.id.button_sign_in);

      Button  button=(Button) findViewById(R.id.button2);
      final String ml=user.getEmail();
     final   EditText name =(EditText) findViewById(R.id.editText2);
      final  EditText phone =(EditText)findViewById(R.id.editText);
        final String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    TextView txt=findViewById(R.id.textView3);
    txt.setText(uid);
  sgn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef.child(uid).push().setValue(new User(ml,name.getText().toString(),phone.getText().toString() ));
               presenter.intent();
            }
        });

 button.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build();

         mGoogleSignInClient = GoogleSignIn.getClient(PhoneAndName.this ,gso);
         //FirebaseAuth.getInstance().signOut();
         signOut();
         disconect();
         presenter.sign_out();
         startActivity(new Intent(PhoneAndName.this, MainActivity.class));

     }
 });
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    private void disconect() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        //updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }

}
