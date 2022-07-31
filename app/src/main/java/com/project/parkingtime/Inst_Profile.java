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

public class Inst_Profile extends AppCompatActivity {

    private FirebaseUser Inst_user;
    private DatabaseReference reference;
    private String InstID;
    private TextView heading,inst_name,inst_city,inst_state,inst_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_profile);
        heading=(TextView) findViewById(R.id.header_inst_profile);
        inst_name=(TextView) findViewById(R.id.disp_inst_name);
        inst_city=(TextView) findViewById(R.id.disp_inst_city);
        inst_state=(TextView) findViewById(R.id.disp_inst_state);
        inst_email=(TextView) findViewById(R.id.disp_inst_email);
        Inst_user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Institution");
        InstID = Inst_user.getUid();
        reference.child(InstID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Inst_Data Inst_profile =snapshot.getValue(Inst_Data.class);
                if(Inst_profile != null){
                    String name = Inst_profile.name;
                    String city = Inst_profile.city;
                    String state = Inst_profile.state;
                    String email = Inst_profile.email;
                    heading.setText(name+" Profile");
                    inst_name.setText("Name: "+name);
                    inst_city.setText("City: "+city);
                    inst_state.setText("State: "+state);
                    inst_email.setText(email);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Inst_Profile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}