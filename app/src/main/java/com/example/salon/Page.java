package com.example.salon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.salon.Adapter.MyViewPageAdapter;
import com.example.salon.Presenter.Page_presenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

public class Page extends AppCompatActivity {

//    StepView stepView;
    Page_presenter presenter= new Page_presenter(this,this);
    private Context context;
    public TextView text;
    public GoogleSignInClient mGoogleSignInClient;
    ViewPager viewPager;
    public  Button button,button_next,button_back;
//    public Page() {    }
// //   public Page(Context context){
//        this.context=context;
//    }

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_page);
        viewPager=findViewById(R.id.view_pager);
        text= findViewById(R.id.textView2);
        button=findViewById(R.id.button);
        button_next=findViewById(R.id.button_next);
        button_back=findViewById(R.id.button_back);
        setUpSignView();
        stColorButton();
        viewPager.setAdapter(new MyViewPageAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            if(position==0){
                button_back.setEnabled(false);
            }
            else {
                button_back.setEnabled(true);

            }
            stColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (user != null) {
            String name = user.getDisplayName();

            for (UserInfo userInfo : user.getProviderData()) {
                if (name == null && userInfo.getDisplayName() != null) {
                    name = userInfo.getDisplayName();
                    text.setText("Welcome "+name);
                }
                else {text.setText("Welcome "+name);}
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    mGoogleSignInClient = GoogleSignIn.getClient(Page.this ,gso);
                     signOut();
                     disconect();

                    presenter.sign_out();
                intent();
                }
            });
        }
    else {
            super.onCreate(savedInstanceState);


           text= findViewById(R.id.textView2);
           text.setText("wow xyi");

        }

    }

    private void stColorButton() {
        if(button_next.isEnabled()){
            button_next.setBackgroundResource(R.color.colorAccent);
        }
        else {
            button_next.setBackgroundResource(R.color.colorBottom);

        }
        if(button_back.isEnabled()){
            button_back.setBackgroundResource(R.color.colorAccent);
        }
        else {
            button_back.setBackgroundResource(R.color.colorBottom);

        }
    }

    private void setUpSignView() {
        StepView stepView=findViewById(R.id.step_view);

        List<String> stepList = new ArrayList<>();
        stepList.add("Genger");
        stepList.add("Type");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
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
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    public void intent(){
        startActivity(new Intent(Page.this, MainActivity.class));
        finish();

    }

}
