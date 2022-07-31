package com.project.parkingtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Inst_Login extends AppCompatActivity {
    private Button login;
    private EditText InstPassword, InstEmail;
    FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_login);
        InstPassword=(EditText) findViewById(R.id.logInstPassword);
        InstEmail=(EditText) findViewById(R.id.logInstEmail);
        login=(Button) findViewById(R.id.InstLogin);
        mAuth = FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.progressBar2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=InstEmail.getText().toString().trim();
                String password=InstPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    InstEmail.setError("E-mail is Required");
                    InstEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    InstEmail.setError("Enter valid E-mail Address");
                    InstEmail.setText("");
                    InstEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(password)) {
                    InstPassword.setError("Password is Required");
                    InstPassword.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Inst_Login.this, "Your institution has successfully logged in", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                InstEmail.setText("");
                                InstPassword.setText("");
                                openInst_Dashboard();
                            } else {
                                Toast.makeText(Inst_Login.this, "Failed to log in! Try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                InstEmail.setText("");
                                InstPassword.setText("");
                            }
                        }
                    });

                }

            }
        });
    }
    public void openInst_Dashboard()
    {
        Intent intent = new Intent(this,Inst_Dashboard.class);
        startActivity(intent);
    }
}