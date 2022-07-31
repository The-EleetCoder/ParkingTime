package com.project.parkingtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class User_Settings extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String UserID;
    private EditText disp_username,disp_pass;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        update = (Button) findViewById(R.id.update_user_Btn);
        disp_username = (EditText) findViewById(R.id.upd_user_username);;
        disp_pass = (EditText) findViewById(R.id.upd_user_pass);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        UserID = user.getUid();
        reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Data profile =snapshot.getValue(User_Data.class);
                if(profile != null){
                    String username = profile.username;
                    String password = profile.password;
                    disp_username.setText(username);
                    disp_pass.setText(password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_Settings.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = disp_pass.getText().toString().trim();
                String username = disp_username.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    disp_username.setError("Username is required");
                    disp_username.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    disp_pass.setError("Password is required");
                    disp_pass.requestFocus();
                }
                else
                {
                    HashMap user = new HashMap();
                    user.put("username",username);
                    user.put("password",password);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(User_Settings.this, "Successfully Updated!", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(User_Settings.this, "Failed to Update! Try again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}