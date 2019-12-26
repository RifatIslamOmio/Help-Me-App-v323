package com.example.helpme.everything;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpme.R;
import com.google.firebase.auth.FirebaseAuth;


public class MenuActivity extends AppCompatActivity {

    Button logout;
    Button seek_help;
    Button help_feed;
    Button profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        logout = findViewById(R.id.logout_btn);
        seek_help = findViewById(R.id.btn_seek_help);
        help_feed = findViewById(R.id.btn_help_feed);
        profile = findViewById(R.id.btn_profile);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        seek_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start your Help Seek Activity/ Post Activity!!!
            }
        });


        help_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HelpFeedActivity.class));
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
            }
        });


    }
}
