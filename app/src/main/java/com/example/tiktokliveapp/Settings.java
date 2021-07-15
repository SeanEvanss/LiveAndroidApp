package com.example.tiktokliveapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

public class Settings extends AppCompatActivity {

    private static final String TAG = "Settings";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //List of all the fragments we have
        SettingsFragment settings= new SettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, settings).commit();

    }


}
