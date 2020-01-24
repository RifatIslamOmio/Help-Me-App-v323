package com.example.helpme.everything;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.helpme.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class CommentFeedActivity extends AppCompatActivity {

    Toolbar toolbar;
    String help_id = HelpList.profileData.getHelpId() ;
    RecyclerView recyclerViewComment;
    EditText commentText;
    Button sendBtn;
    FirebaseUser user;
    DatabaseReference reference,referenceforRecyclerView;
    ArrayList<Comment> list;
    CommentList commentList;
    private Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_feed);

        commentText = findViewById(R.id.commentSendText);
        sendBtn = findViewById(R.id.commentSendBtn);
        reference = FirebaseDatabase.getInstance().getReference("helps");
        user = FirebaseAuth.getInstance().getCurrentUser();
        toolbar = findViewById(R.id.comment_feed_toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(HelpList.profileData.getSeeker_name()+"'s Post Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment_text = commentText.getText().toString().trim();
                if(!comment_text.equals(""))
                {
                    String uId= user.getUid();
                    String uName = trimmer(user.getEmail());
                    String date = getDatenTime()[0];
                    String time = getDatenTime()[1];
                    String commentId = reference.push().getKey();
                    String help_post_id = HelpList.profileData.getHelpId();

                    Comment comment = new Comment(commentId,uId,uName,help_post_id,date,time,comment_text);
                    reference.child(help_post_id).child("comments").child(commentId).setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            commentText.setText("");
                            //Hiding Keyboard
                            try { InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception e) {}

                            recyclerViewComment.smoothScrollToPosition(HelpList.COMMENT_COUNT+1);
                            Toast.makeText(getApplicationContext(),"Comment Added",Toast.LENGTH_SHORT).show();
                        }

                    });

                    reference.child(help_post_id).child("commentCount").setValue(list.size()+1);
                }
            }
        });



        recyclerViewComment = findViewById(R.id.CommentrecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerViewComment.setLayoutManager(manager);
        recyclerViewComment.setHasFixedSize(true);

        referenceforRecyclerView = FirebaseDatabase.getInstance().getReference("helps").child(help_id).child("comments");
        referenceforRecyclerView.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                HelpList.COMMENT_COUNT = (int) dataSnapshot.getChildrenCount();

                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    Comment comment = data.getValue(Comment.class);
                    list.add(comment);
                }

                commentList = new CommentList(CommentFeedActivity.this,list);
                recyclerViewState = recyclerViewComment.getLayoutManager().onSaveInstanceState();
                recyclerViewComment.setAdapter(commentList);
                recyclerViewComment.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {Toast.makeText(getApplicationContext(),"Failed To Load",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewComment.smoothScrollToPosition(HelpList.COMMENT_COUNT);
    }

    public String[] getDatenTime()
    {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy/hh-mm-ss");
        String dateandtime = sdf.format(date);
        String[] parts = dateandtime.split("/");
        String date_text = parts[0], time_text = parts[1];
        return parts;
    }

    String trimmer(String str) {
        String temp="";
        for(int i =0;i<str.length();i++)
        {
            if(str.charAt(i)!='@')
            {
                temp = temp+str.charAt(i);
            }
            else
            {
                break;
            }
        }
        return temp.toUpperCase();
    }
}
