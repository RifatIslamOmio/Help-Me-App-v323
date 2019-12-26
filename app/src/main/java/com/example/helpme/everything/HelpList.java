package com.example.helpme.everything;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HelpList extends RecyclerView.Adapter<HelpList.MyViewHolder>  {


    public static Help profileData;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context context;
    ArrayList<Help> helpList;
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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        String[] parts = helpList.get(position).getDateandtime().split("/");
        String date_text = parts[0], time_text = parts[1];



        holder.date.setText(date_text);
        holder.votecounter.setText(helpList.get(position).getVoteCount()+"");
        holder.time.setText(time_text);
        holder.name.setText(helpList.get(position).getSeeker_name());
        holder.description.setText(helpList.get(position).getDescription());


        holder.location.setText("location");
        //location and photolink
        Picasso.with(context)
                .load(helpList.get(position).getPhoto_path())
                .into(holder.imageView);



        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileData = helpList.get(position);
                Intent intent = new Intent(context,HelpSeekersProfile_activity.class);
                context.startActivity(intent);

            }
        });


        List<String> voters = helpList.get(position).getVoters();
        if(voters.contains(user.getUid()))
        {
            holder.votecounter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_voted, 0, 0, 0);
        }
        else
        {
            holder.votecounter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_notvoted, 0, 0, 0);
        }


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

    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,date,time,description,votecounter,location;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.seekerNameText);
            date = itemView.findViewById(R.id.dateText);
            time = itemView.findViewById(R.id.timeText);
            description = itemView.findViewById(R.id.description_Text);
            imageView = itemView.findViewById(R.id.imageViewHelpFeed);
            votecounter = itemView.findViewById(R.id.counterText);
            location = itemView.findViewById(R.id.locationText);

        }
    }

}
