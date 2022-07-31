package com.project.parkingtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Profile extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String UserID;
    private TextView heading,user_name,user_city,user_state,user_email,user_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        heading=(TextView) findViewById(R.id.header_user_profile);
        user_name=(TextView) findViewById(R.id.disp_user_name);
        user_city=(TextView) findViewById(R.id.disp_user_city);
        user_state=(TextView) findViewById(R.id.disp_user_state);
        user_email=(TextView) findViewById(R.id.disp_user_email);
        user_phone=(TextView) findViewById(R.id.disp_user_phone);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        UserID = user.getUid();
        reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Data user_profile =snapshot.getValue(User_Data.class);
                if(user_profile != null){
                    String name = user_profile.name;
                    String username=user_profile.username;
                    String city = user_profile.city;
                    String state = user_profile.state;
                    String email = user_profile.email;
                    String phone = user_profile.phone;
                    heading.setText(username+" Profile");
                    user_name.setText("Name: "+name);
                    user_city.setText("City: "+city);
                    user_state.setText("State: "+state);
                    user_email.setText(email);
                    user_phone.setText(phone);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_Profile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}