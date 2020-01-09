package com.example.helpme.everything;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helpme.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class CommentFeedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText commentText;
    Button sendBtn;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_feed);

        recyclerView = findViewById(R.id.CommentrecyclerView);
        commentText = findViewById(R.id.commentSendText);
        sendBtn = findViewById(R.id.commentSendBtn);
        reference = FirebaseDatabase.getInstance().getReference("helps");
        user = FirebaseAuth.getInstance().getCurrentUser();

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
                            Toast.makeText(getApplicationContext(),"Posted",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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
