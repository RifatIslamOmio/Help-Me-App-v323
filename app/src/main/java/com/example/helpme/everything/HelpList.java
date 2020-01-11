package com.example.helpme.everything;

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
import com.example.helpme.Models.Help;
import com.example.helpme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HelpList extends RecyclerView.Adapter<HelpList.MyViewHolder>  {


    public static Help profileData;
    public static int COMMENT_COUNT;
    long child_count;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context context;
    ArrayList<Help> helpList;
    DatabaseReference reference;
    public HelpList(Context c, ArrayList<Help> helpList)
    {
        context = c;
        this.helpList = helpList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        String[] parts = helpList.get(position).getDateandtime().split("/");
        String date_text = parts[0], time_text = parts[1];



        holder.date.setText(date_text);
        holder.votecounter.setText(helpList.get(position).getVoteCount()+"");
        holder.time.setText(time_text);
        holder.name.setText(helpList.get(position).getSeeker_name());
        holder.description.setText(helpList.get(position).getDescription());
        holder.location.setText(helpList.get(position).getCurrent_address());
        holder.comment.setText(helpList.get(position).getCommentCount()+"");




        //Location
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] latlang = helpList.get(position).getLatlong().split(" ");
                double latitude = Double.parseDouble(latlang[0]), longitude = Double.parseDouble(latlang[1]);

                //Log.d(Constants.RECEIVER_END_POST_ACTIVITY, "showMapClicked: latitude = "+latitude+" longitude = "+longitude);
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra(Constants.MAP_LATITUDE_KEY, latitude);
                intent.putExtra(Constants.MAP_LONGITUDE_KEY, longitude);
                context.startActivity(intent);
            }
        });


        //Post Comments

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileData = helpList.get(position);
                COMMENT_COUNT = helpList.get(position).getCommentCount();
                Intent intent = new Intent(context,CommentFeedActivity.class);
                context.startActivity(intent);
            }
        });



        //Load Imageview
        Picasso.with(context)
                .load(helpList.get(position).getPhoto_path())
                .into(holder.imageView);


        //Profile
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileData = helpList.get(position);
                Intent intent = new Intent(context,HelpSeekersProfile_activity.class);
                context.startActivity(intent);

            }
        });



        //Vote Icon
        List<String> voters = helpList.get(position).getVoters();
        if(voters.contains(user.getUid()))
        {
            holder.votecounter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_voted, 0, 0, 0);
        }
        else
        {
            holder.votecounter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_notvoted, 0, 0, 0);
        }



        //Vote Commit
        holder.votecounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("helps");

                List<String> voters = helpList.get(position).getVoters();

                if(voters.contains(user.getUid()))
                {
                    int voteCount = helpList.get(position).getVoteCount()-1;
                    reference.child(helpList.get(position).getHelpId()).child("voteCount").setValue(voteCount);
                    voters.remove(user.getUid());
                    reference.child(helpList.get(position).getHelpId()).child("voters").setValue(voters);
                    Toast.makeText(context,"Vote Removed!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int voteCount = helpList.get(position).getVoteCount()+1;
                    reference.child(helpList.get(position).getHelpId()).child("voteCount").setValue(voteCount);
                    voters.add(user.getUid());
                    reference.child(helpList.get(position).getHelpId()).child("voters").setValue(voters);
                    Toast.makeText(context,"Voted!",Toast.LENGTH_SHORT).show();
                }
            }
        });



        //Delete Post/Item
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(user.getUid().equals(helpList.get(position).getUser_id()))
                {
                    AlertDialog Dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setMessage("Do you want to remove this post?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("helps").child(helpList.get(position).getHelpId());
                                    reference.removeValue();
                                    dialog.dismiss();
                                    Toast.makeText(context,"Post Removed!",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,date,time,description,votecounter,location,comment;
        ImageView imageView;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.itemOfRecyclerView);
            name = itemView.findViewById(R.id.seekerNameText);
            date = itemView.findViewById(R.id.dateText);
            time = itemView.findViewById(R.id.timeText);
            description = itemView.findViewById(R.id.description_Text);
            imageView = itemView.findViewById(R.id.imageViewHelpFeed);
            votecounter = itemView.findViewById(R.id.counterText);
            location = itemView.findViewById(R.id.locationText);
            comment = itemView.findViewById(R.id.Comment_Counter_ItemView);
        }
    }

}
