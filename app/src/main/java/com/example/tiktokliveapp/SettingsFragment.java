package com.example.tiktokliveapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static String TAG= "debug";
    private SharedPreferences prefs;
    private FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();


    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.i(TAG, "onSharedPreferenceChanged: there was a pref change");
            switch(key){
                case("dark_mode"):
                    Boolean darkMode= sharedPreferences.getBoolean("dark_mode", true);
                    Log.i(TAG, "onSharedPreferenceChanged: day night changed");
                    int DayNightMode= (darkMode)?AppCompatDelegate.MODE_NIGHT_YES:AppCompatDelegate.MODE_NIGHT_NO;
                    AppCompatDelegate.setDefaultNightMode(DayNightMode);
                    break;

                 case("editUserName"):
                    //Case to update the user's given name

                    UserProfileChangeRequest profileUpdateRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName( sharedPreferences.getString("editUserName", "user"))
                            .build();
                    currentUser.updateProfile(profileUpdateRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Profile successfully updated", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Profile NOT updated, please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


            }
        }
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Log.i(TAG, "onCreatePreferences: OnCreate Called");

        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(listener);

    }

    @Override
    public void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        prefs.unregisterOnSharedPreferenceChangeListener(listener);

    }
}