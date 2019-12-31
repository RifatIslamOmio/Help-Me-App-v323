package com.example.helpme.everything;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.helpme.Extras.Constants;
import com.example.helpme.Extras.Notifications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyService extends Service {
    DatabaseReference reference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    long childCount;
    long delChildCount=0;
    ArrayList<Help> list;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifications.createNotificationChannel(this);
        reference = FirebaseDatabase.getInstance().getReference().child("helps");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childCount = dataSnapshot.getChildrenCount();
                Log.d("DATA","first"+":: "+childCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(delChildCount>dataSnapshot.getChildrenCount())
                {
                    delChildCount-=1;
                    childCount-=1;
                }
                else
                {
                    delChildCount = dataSnapshot.getChildrenCount();
                }

                if(dataSnapshot.getChildrenCount()>childCount)
                {
                    childCount=dataSnapshot.getChildrenCount();
                    //Toast.makeText(getApplicationContext(),"Data Change!",Toast.LENGTH_SHORT).show();
                    Log.d("DATA","DATA Changed"+":: "+dataSnapshot.getChildrenCount());
                    //Generate Notification Here.......



                    list = new ArrayList<>();
                    Help help=null;
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                         help = dataSnapshot1.getValue(Help.class);
                        //list.add(help);
                    }
                    //Collections.reverse(list);
                    if(user.getUid()!=help.getUser_id())
                    {
                        Notifications.showNotification(getApplicationContext(),
                                new Intent(getApplicationContext(),
                                        HelpFeedActivity.class), Constants.DB_NOTIFICATION_ID,"Help!",
                                help.getSeeker_name()+" is asking for help!");

                    }


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
