package com.example.salon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.salon.Presenter.PopUpIn;
import com.example.salon.View.PageOne;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.Arrays;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
DatabaseReference ref;
    Button signIn;
Button signUp;
private CallbackManager callbackManager;
LoginButton loginButton;
private FirebaseAuth mAuth;
private SignInButton mGoogleBtn;
private GoogleSignInClient mGoogleSignInClient;
private final static int  RC_SIGN_IN=1;
String uid;
PopUpIn presenter_in= new PopUpIn(this,this);


    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

       mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mGoogleBtn= findViewById(R.id.sign_google_button);
        mAuth = FirebaseAuth.getInstance();
       final FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            user=FirebaseAuth.getInstance().getCurrentUser();
            uid=user.getUid();

           ref= FirebaseDatabase.getInstance().getReference();
            ref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object fb =dataSnapshot.child("server").child("saving-data").child("fireblog").child("Users").child(uid).getValue();
                Object regular =dataSnapshot.child("Users").child(uid).getValue();
                if ((fb==null)&&(regular==null)){
                    presenter_in.updateUIFB(currentUser);}
                    else{   presenter_in.updateUI(currentUser);}

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


           }

             else {
            loginButton = findViewById(R.id.login_button);

            callbackManager = CallbackManager.Factory.create();
            loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
             loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    presenter_in.makeToast("Yes");
                    presenter_in.handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    presenter_in.makeToast("Cancel");
                }

                @Override
                public void onError(FacebookException exception) {
                    presenter_in.makeToast("Error");
                }
            });

            //super.onCreate(savedInstanceState);

            signIn = findViewById(R.id.buttonIn);
            signUp = findViewById(R.id.buttonReg);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter_in.show_sign_in_window();

                }
            });
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    presenter_in.show_sign_up_window();
                }
            });

            mGoogleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                //presenter_in.firebaseAuthWithGoogle(account);
                presenter_in.makeToast("also hereee");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
         //       Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        presenter_in.makeToast("googleeee");
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //                       Log.d(TAG, "signInWithCredential:success");
                           final FirebaseUser user = mAuth.getCurrentUser();
                            String uid=user.getUid();
                            ref= FirebaseDatabase.getInstance().getReference().child("server").child("saving-data").child("fireblog").child("Users").child(uid);
                            ref.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Object phone =dataSnapshot.getValue();
                                    if (phone==null){
                                        presenter_in.updateUIFB(user);}
                                    else{   presenter_in.updateUI(user);}

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            presenter_in.makeToast("lol");
                            // If sign in fails, display a message to the user.
                            ////                     Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //         Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            presenter_in.updateUI(null);
                        }

                    }
                });
    }
}