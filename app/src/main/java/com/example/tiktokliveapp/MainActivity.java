package com.example.tiktokliveapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "debug";
    Boolean nightMode;
    private FirebaseUser currentUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        sharedPrefEditor= sharedPreferences.edit();

        Toolbar myToolBar= (androidx.appcompat.widget.Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView welcome_text =findViewById(R.id.welcome_user);
        welcome_text.setText("Welcome "+ getString(R.string.user_name));
        /*
        if(savedInstanceState== null){
            Bundle extra= getIntent().getExtras();
            if(extra==null){
                currentUser=null;
                Log.d(TAG, "onCreate: auth not recieved, major issue");
            }
            else{
                currentUser= (FirebaseUser) extra.getSerializable("authUser");
            }
        }
        else {
            currentUser= (FirebaseUser) savedInstanceState.getSerializable("authUser");
        }
        */

        Button go_live= findViewById(R.id.start_live);
        go_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: STARTING LIVE");
            }
        });

        //The first time the app opens we read the SharedPrefs and set the theme. Will turn this into a function.
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(this);
        nightMode= sharedPref.getBoolean("dark_mode", true);
        int DayNightMode= (nightMode)?AppCompatDelegate.MODE_NIGHT_YES:AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(DayNightMode);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm signout ?")
                .setMessage("Press confirmm to signout")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Reset the saved username and password since they're signing out
                        sharedPrefEditor.putString("saved_username", "");
                        sharedPrefEditor.putString("saved_password", "");
                        sharedPrefEditor.apply();

                        FirebaseAuth.getInstance().signOut();
                        MainActivity.super.onBackPressed();
                    }
                }).show();

    }
}