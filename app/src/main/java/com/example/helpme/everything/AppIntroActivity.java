package com.example.helpme.everything;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.helpme.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class AppIntroActivity extends AppIntro {


    //Titles and descriptions can be modified from here
    private String title1 = "Welcome!";
    private String title2 = "Seek Help!";
    private String title3 = "Response!";
    private String desc1 = "Signup and join the community to stand against the social violence around you!";
    private String desc2 = "Scared to stand against it alone?" +
                        " Seek help from people around you and defeat the brutality together!";
    private String desc3 = "Get notified when someone around you seeks for help to prevent the violence!";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance(title1,desc1, R.drawable.help,
                ContextCompat.getColor(getApplicationContext(), R.color.first)));

        addSlide(AppIntroFragment.newInstance(title2,desc2, R.drawable.seek,
                ContextCompat.getColor(getApplicationContext(), R.color.second)));

        addSlide(AppIntroFragment.newInstance(title3,desc3, R.drawable.response,
                ContextCompat.getColor(getApplicationContext(), R.color.third)));

    }



    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
