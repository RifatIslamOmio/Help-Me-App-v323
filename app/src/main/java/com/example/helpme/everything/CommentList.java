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

    @Override
    public void onBindViewHolder(@NonNull CommentList.MyViewHolder holder, int position) {




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
