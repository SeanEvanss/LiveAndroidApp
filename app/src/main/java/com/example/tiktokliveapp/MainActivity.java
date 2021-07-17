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


    //Remeber that we can call FirebaseAuth anywhere because it's a singleton class (i.e there can only be one instance shared throughout the entire application)

    private static final String TAG = "debug";
    Boolean nightMode;
    private FirebaseUser currentUser;

    //Might wanna consider not having 2 sharedPrefences and just using the default since we have a settings fragment and handles that
    SharedPreferences userSharedPreferences;
    SharedPreferences.Editor userSharedPrefEditor;

    SharedPreferences defaultSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.i(TAG, "onCreate CALLED");
        userSharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        userSharedPrefEditor= userSharedPreferences.edit();

        Toolbar myToolBar= (androidx.appcompat.widget.Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Button go_live= findViewById(R.id.start_live);
        go_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG, "onClick: STARTING LIVE");
                recreate();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //We can place this in a function
        defaultSharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        TextView welcome_text =findViewById(R.id.welcome_user);
        welcome_text.setText("Welcome "+ defaultSharedPreferences.getString("editUserName","user"));

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        Log.i(TAG, "onCreate: MainActivity "+ currentUser);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume CALLED");
        //defaultSharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        TextView welcome_text =findViewById(R.id.welcome_user);
        welcome_text.setText("Welcome "+ defaultSharedPreferences.getString("editUserName","user"));

    }

    //The next 2 functions are for the menu bar ( the 3 dots near the top)

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


    //we override the default backbutton functionality by having an alerDialog prompt the user to confirm if they wish to log out.
    //We also called the FirebaseAuth signout function.
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
                        userSharedPrefEditor.putString("saved_username", "");
                        userSharedPrefEditor.putString("saved_password", "");
                        userSharedPrefEditor.apply();

                        FirebaseAuth.getInstance().signOut();
                        MainActivity.super.onBackPressed();
                    }
                }).show();

    }
}