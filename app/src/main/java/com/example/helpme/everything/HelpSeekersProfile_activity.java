package com.example.helpme.everything;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.helpme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HelpSeekersProfile_activity extends AppCompatActivity {

    Toolbar toolbar;
    TextView emailTVHS,usernameTVHS,fullnameTVHS,addressTVHS,phoneTVHS;
    CircleImageView circleImageView;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    String photopath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_seekers_profile);


        String userId;
        if(CommentList.COMMENT_USER_ID!=null)
        {
            userId = CommentList.COMMENT_USER_ID;
            CommentList.COMMENT_USER_ID=null;
        }
        else
        {
            userId = HelpList.profileData.getUser_id();
        }


        usernameTVHS = findViewById(R.id.profileUsernameHS);
        emailTVHS = findViewById(R.id.profileEmailHS);
        fullnameTVHS = findViewById(R.id.profileFullNameHS);
        addressTVHS = findViewById(R.id.profileAddressHS);
        phoneTVHS = findViewById(R.id.profilePhoneHS);
        toolbar = findViewById(R.id.ToolbarSeekerProfile);
        circleImageView = findViewById(R.id.profileUserpicHS);
        progressBar = findViewById(R.id.progressBar_helpSeekerprofileView);
        linearLayout = findViewById(R.id.helpSeekerProfileView_linear_layout);

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

        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

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

                    getSupportActionBar().setTitle(userInfo.getUserName()+"'s Profile");

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


                    if(userInfo.getPhoto_link().compareTo("link:")!=0)
                    {
                        photopath = userInfo.getPhoto_link();
                        Picasso.with(getApplicationContext())
                                .load(userInfo.getPhoto_link())
                                .into(circleImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressBar.setVisibility(View.GONE);
                                        linearLayout.setVisibility(View.VISIBLE);
                                    }
                                    @Override
                                    public void onError() { }
                                });

                        circleImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(),FullScreenImage.class);
                                intent.putExtra("link",photopath);
                                startActivity(intent);
                            }
                        });

                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),"Failed to load profile",Toast.LENGTH_SHORT).show();

        }
    };

}
