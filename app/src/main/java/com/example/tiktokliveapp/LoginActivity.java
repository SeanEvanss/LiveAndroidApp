package com.example.tiktokliveapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.Instant;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String usernameEntry, passwordEntry;
    private Button loginButton, registerButton;
    private CheckBox stayLoggedInBox;
    EditText usernameField, passwordField;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        sharedPrefEditor= sharedPreferences.edit();

        mAuth = FirebaseAuth.getInstance();

        usernameEntry=null;
        passwordEntry= null;

        usernameField= findViewById(R.id.username_field);
        passwordField= findViewById(R.id.password_field);
        loginButton= findViewById(R.id.loginButton);
        registerButton= findViewById(R.id.registerButton);
        stayLoggedInBox= findViewById(R.id.stayLoggedInBox);

        //LoginButton
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEntry= usernameField.getText().toString();
                passwordEntry= passwordField.getText().toString();

                if(stayLoggedInBox.isChecked()){
                    sharedPrefEditor.putString("saved_username", usernameEntry);
                    sharedPrefEditor.putString("saved_password", passwordEntry);
                    sharedPrefEditor.apply(); //Remeber to call this or the pref wont be applied

                }
                loginAccount(usernameEntry, passwordEntry);
            }
        });
        //RegisterButton
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEntry= usernameField.getText().toString();
                passwordEntry= passwordField.getText().toString();

                if(stayLoggedInBox.isChecked()){
                    sharedPrefEditor.putString("saved_username", usernameEntry);
                    sharedPrefEditor.putString("saved_password", passwordEntry);
                    sharedPrefEditor.apply(); //Remeber to call this or the pref wont be applied

                }

                registerNewAccount(usernameEntry, passwordEntry);

            }
        });
    }

    private void registerNewAccount(String username, String password){
        mAuth.createUserWithEmailAndPassword(username,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: created new user account successfully");
                    FirebaseUser user= mAuth.getCurrentUser();
                    //add new intent
                    startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("authUser",user));
                }
                else{
                    Toast.makeText(LoginActivity.this, "Account not created, please try again", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void loginAccount(String username, String password){
       mAuth.signInWithEmailAndPassword(username, password)
       .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   FirebaseUser user= mAuth.getCurrentUser();
                   //add new intent
                   startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("authUser",user));
               }
               else{
                   Toast.makeText(LoginActivity.this, "Incorrect email or password, please try again", Toast.LENGTH_LONG).show();

               }
           }
       });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();

        usernameEntry= sharedPreferences.getString("username","");
        passwordEntry= sharedPreferences.getString("password","");

        if(currentUser!= null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("authUser",currentUser));
        }
        else if(!usernameEntry.equals("")){
            loginAccount(usernameEntry, passwordEntry);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAuth.getCurrentUser()!= null){
            FirebaseAuth.getInstance().signOut();
        }
    }
}
