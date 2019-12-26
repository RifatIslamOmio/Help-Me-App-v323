package com.example.helpme.everything;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.helpme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HelpSeekersProfile_activity extends AppCompatActivity {

    Toolbar toolbar;
    TextView emailTVHS,usernameTVHS,fullnameTVHS,addressTVHS,phoneTVHS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_seekers_profile);

        String userId = HelpList.profileData.getUser_id();


        usernameTVHS = findViewById(R.id.profileUsernameHS);
        emailTVHS = findViewById(R.id.profileEmailHS);
        fullnameTVHS = findViewById(R.id.profileFullNameHS);
        addressTVHS = findViewById(R.id.profileAddressHS);
        phoneTVHS = findViewById(R.id.profilePhoneHS);
        toolbar = findViewById(R.id.ToolbarSeekerProfile);


        Query query = FirebaseDatabase.getInstance().getReference("Profiles")
                .orderByChild("userId")
                .equalTo(userId);
        query.addListenerForSingleValueEvent(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
            {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {

                    UserInfo userInfo = snapshot.getValue(UserInfo.class);

                    String fullName = fullnameTVHS.getText()+userInfo.getFirstName()+" "+userInfo.getLastName();
                    fullnameTVHS.setText(fullName);

                    String email = emailTVHS.getText()+userInfo.getEmail();
                    emailTVHS.setText(email);

                    String userName = usernameTVHS.getText()+userInfo.getUserName();
                    usernameTVHS.setText(userName);

                    String phone = phoneTVHS.getText()+userInfo.getPhone();
                    phoneTVHS.setText(phone);

                    String address = addressTVHS.getText()+userInfo.getAddress();
                    addressTVHS.setText(address);

                    setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle(userInfo.getUserName()+"'s Profile");

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
