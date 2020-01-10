package com.example.helpme.everything;

import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpme.Activities.MapsActivity;
import com.example.helpme.Extras.Constants;
import com.example.helpme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentList extends RecyclerView.Adapter<CommentList.MyViewHolder> {


    Context context;
    ArrayList<Comment> list;
    public static String COMMENT_USER_ID=null;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public CommentList(Context c, ArrayList<Comment> cList)
    {
        this.list = cList;
        this.context = c;
    }

    @NonNull
    @Override
    public CommentList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_view,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommentList.MyViewHolder holder, final int position) {
        holder.votecounter.setText(list.get(position).getUpvoteCount()+"");
        holder.name.setText(list.get(position).getcUserName());
        holder.commentText.setText(list.get(position).getCommentText());
        holder.date.setText(list.get(position).getDate());
        holder.time.setText(list.get(position).getTime());


        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                COMMENT_USER_ID = list.get(position).getcUserId();
                Intent intent = new Intent(context,HelpSeekersProfile_activity.class);
                context.startActivity(intent);

            }
        });


        holder.votecounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("helps")
                        .child(HelpList.profileData.getHelpId())
                        .child("comments")
                        .child(list.get(position).getComment_id());

                List<String> voters = list.get(position).getCommentVoters();

                if(voters.contains(user.getUid()))
                {
                    int voteCount = list.get(position).getUpvoteCount()-1;
                    reference.child("upvoteCount").setValue(voteCount);
                    voters.remove(user.getUid());
                    reference.child("commentVoters").setValue(voters);
                    Toast.makeText(context,"Vote Removed!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int voteCount = list.get(position).getUpvoteCount()+1;
                    reference.child("upvoteCount").setValue(voteCount);
                    voters.add(user.getUid());
                    reference.child("commentVoters").setValue(voters);
                    Toast.makeText(context,"Voted!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Vote Icon
        List<String> voters = list.get(position).getCommentVoters();
        if(voters.contains(user.getUid()))
        {
            holder.votecounter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_voted, 0, 0, 0);
        }
        else
        {
            holder.votecounter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_notvoted, 0, 0, 0);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView name,date,time,commentText,votecounter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.CommentorName);
            date = itemView.findViewById(R.id.CommentDate);
            time = itemView.findViewById(R.id.CommentTime);
            commentText = itemView.findViewById(R.id.Comment_Text);
            votecounter = itemView.findViewById(R.id.CommentVotecounter);

        }
    }
}
