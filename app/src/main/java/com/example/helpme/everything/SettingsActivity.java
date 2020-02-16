package com.example.helpme.everything;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.helpme.R;

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    Switch notification_switch;
    public static final String SHARED_PREFERENCES="sharedPrefs";
    public static final String notification_switch_pref = "switch_1";
    public static boolean switch_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notification_switch = findViewById(R.id.notification_switch);
        toolbar = findViewById(R.id.settings_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notification_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_preferences();
                if(notification_switch.isChecked())
                {
                    try {
                        //Toast.makeText(getApplicationContext(),"Starting btn",Toast.LENGTH_SHORT).show();
                        startService(new Intent(getApplicationContext(),MyService.class)); //Push Notification Service
                    }catch (Exception e){}
                }
                else
                {
                    try {
                        //Toast.makeText(getApplicationContext(),"Stopping btn",Toast.LENGTH_SHORT).show();
                        stopService(new Intent(getApplicationContext(),MyService.class));
                    }catch (Exception e){}
                }
            }
        });
        loadData();
        updateViews();
    }


    public void save_preferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(notification_switch_pref,notification_switch.isChecked());
        editor.apply();
    }
    public void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        switch_status = sharedPreferences.getBoolean(notification_switch_pref,true);
        updateViews();
    }

    public void updateViews()
    {
        notification_switch.setChecked(switch_status);
    }
}
