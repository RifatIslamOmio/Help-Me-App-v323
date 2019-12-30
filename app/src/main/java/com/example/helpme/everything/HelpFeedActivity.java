package com.example.helpme.everything;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class HelpFeedActivity extends AppCompatActivity {

    HelpList helpList;  //adapter
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Help> list;
    private Parcelable recyclerViewState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpfeed);




        reference = FirebaseDatabase.getInstance().getReference().child("helps");
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Help help = dataSnapshot1.getValue(Help.class);
                    list.add(help);
                }
                Collections.reverse(list);
                helpList = new HelpList(HelpFeedActivity.this,list);
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                recyclerView.setAdapter(helpList);
                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Wrong!....", Toast.LENGTH_SHORT).show();
            }

        });



    }
}
