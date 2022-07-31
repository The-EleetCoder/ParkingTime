package com.project.parkingtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class User_Reg extends AppCompatActivity implements View.OnClickListener{
    private Button user_reg_submit;
    private FirebaseAuth mAuth;
    private EditText user_name,user_city,user_state,user_email,user_phone,user_username,user_password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);
        mAuth = FirebaseAuth.getInstance();


        user_name = (EditText) findViewById(R.id.user_reg_name);
        user_city = (EditText) findViewById(R.id.user_reg_city);
        user_state = (EditText) findViewById(R.id.user_reg_state);
        user_email = (EditText) findViewById(R.id.user_reg_email);
        user_phone = (EditText) findViewById(R.id.user_reg_phone);
        user_username = (EditText) findViewById(R.id.user_reg_username);
        user_password = (EditText) findViewById(R.id.user_reg_pass);
        progressBar=(ProgressBar) findViewById(R.id.progressBar5);
        user_reg_submit = (Button) findViewById(R.id.button3);
        user_reg_submit.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button3:
                button3();
                break;
        }
    }

    private void button3() {
        String name = user_name.getText().toString().trim();
        String city = user_city.getText().toString().trim();
        String state = user_state.getText().toString().trim();
        String email = user_email.getText().toString().trim();
        String phone = user_phone.getText().toString().trim();
        String username = user_username.getText().toString().trim();
        String password = user_password.getText().toString().trim();


        if(name.isEmpty()){

            user_name.setError("Full name is required!");
            user_name.requestFocus();
            return;
        }

        else if(city.isEmpty()){
            user_city.setError("City is required");
            user_city.requestFocus();
            return;
        }

        else if(state.isEmpty()){
            user_state.setError("State is required!");
            user_state.requestFocus();
            return;
        }

        else if(email.isEmpty()){
            user_email.setError("Email is required");
            user_email.requestFocus();
            return;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            user_email.setError("Please provide valid email!");
            user_email.setText("");
            user_email.requestFocus();
            return;
        }

        else if(phone.isEmpty()){
            user_phone.setError("Please enter Phone No.");
            user_phone.requestFocus();
            return;
        }

        else if (username.isEmpty()){
            user_username.setError("Required!");
            user_username.requestFocus();
            return;
        }

        else if(password.isEmpty()){
            user_password.setError("Required!");
            user_password.requestFocus();
            return;
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User_Data user = new User_Data(name, city, state, email, phone, username, password);

                                FirebaseDatabase.getInstance().getReference("User")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(User_Reg.this, "You are Successfully Registered!", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            user_name.setText("");
                                            user_city.setText("");
                                            user_state.setText("");
                                            user_email.setText("");
                                            user_phone.setText("");
                                            user_username.setText("");
                                            user_password.setText("");
                                            openUser_Login();
                                        } else {
                                            Toast.makeText(User_Reg.this, "Failed To Register! Try Again!", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            user_email.setText("");
                                            user_phone.setText("");
                                            user_username.setText("");
                                            user_password.setText("");

                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(User_Reg.this, "Failed To Register! Try Again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                user_email.setText("");
                                user_phone.setText("");
                                user_username.setText("");
                                user_password.setText("");
                            }
                        }
                    });
        }

    }
    public void openUser_Login(){
        Intent intent= new Intent(this, User_Login.class);
        startActivity(intent);
    }
}