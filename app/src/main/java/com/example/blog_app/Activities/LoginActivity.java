package com.example.blog_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blog_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText userMail, userPassword;
    private Button loginBtn;
    private ProgressBar loginProgress;
    private ImageView loginPhoto;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userMail = findViewById(R.id.loginMail);
        userPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        loginProgress = findViewById(R.id.prBar);
        mAuth = FirebaseAuth.getInstance();
        loginPhoto = findViewById(R.id.loginUserPhoto);


        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
            }
        });

        user = mAuth.getCurrentUser();
        if(user != null){
            updateUI();
        }

        loginProgress.setVisibility(View.INVISIBLE);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgress.setVisibility(View.VISIBLE);
                loginBtn.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if(mail.isEmpty() || password.isEmpty()){
                    loginProgress.setVisibility(View.INVISIBLE);
                    loginBtn.setVisibility(View.VISIBLE);
                    showMessage("Please Verify All field");
                }else{
                    signIn(mail, password);
                }
            }
        });


    }

    private void signIn(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loginProgress.setVisibility(View.VISIBLE);
                            loginBtn.setVisibility(View.INVISIBLE);
                            updateUI();
                        }else{
                            loginProgress.setVisibility(View.INVISIBLE);
                            loginBtn.setVisibility(View.VISIBLE);
                            showMessage(task.getException().getMessage());
                        }
                    }
                });


    }


    private void showMessage(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();

    }

    private void updateUI() {
        Intent intent = new Intent(LoginActivity.this, Home.class);
        startActivity(intent);
        finish();
    }
}
