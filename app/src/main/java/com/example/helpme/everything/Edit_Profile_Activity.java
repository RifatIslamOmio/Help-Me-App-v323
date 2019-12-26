package com.example.helpme.everything;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpme.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Edit_Profile_Activity extends AppCompatActivity {

    EditText fName,lName,address,phone;
    Button saveBtn;
    ImageView profilePic;
    DatabaseReference reference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile_);

        reference = FirebaseDatabase.getInstance().getReference("Profiles");

        fName = findViewById(R.id.FirstNameEditText);
        lName = findViewById(R.id.LastNameeditText);
        address = findViewById(R.id.AddresseditText);
        phone = findViewById(R.id.PhoneeditText);
        saveBtn = findViewById(R.id.Savebutton);
        profilePic = findViewById(R.id.profile_image_view);



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String userId = user.getUid();
                 String userEmail = user.getEmail();
                 String username = trimmer(userEmail);
                 String firstName = fName.getText().toString().trim();
                 String lastName = lName.getText().toString().trim();
                 String userAddress = address.getText().toString().trim();
                 String userPhone = phone.getText().toString().trim();

                if(TextUtils.isEmpty(firstName))
                {
                    fName.setError("Required Field!");
                    return;
                }
                if(TextUtils.isEmpty(lastName))
                {
                    lName.setError("Required Field!");
                    return;
                }
                if(TextUtils.isEmpty(userAddress))
                {
                    address.setError("Required Field!");
                    return;
                }
                if(TextUtils.isEmpty(userPhone))
                {
                    phone.setError("Required Field!");
                    return;
                }


                UserInfo userInfo = new UserInfo(userId,username,firstName,lastName,userEmail,userPhone,userAddress,"link:");

                reference.child(userId).setValue(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"User Information Saved!",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
                    }
                });
            }
        });



    }
    String trimmer(String str)
    {
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
