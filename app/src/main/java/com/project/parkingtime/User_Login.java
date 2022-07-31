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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class User_Login extends AppCompatActivity {

    private Button login;
    private EditText UserPassword, UserEmail;
    FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        UserEmail = (EditText) findViewById(R.id.user_email_login);
        UserPassword = (EditText) findViewById(R.id.userpass_login);
        mAuth = FirebaseAuth.getInstance();
        login = (Button) findViewById(R.id.User_login_submit);
        progressBar=(ProgressBar) findViewById(R.id.progressBar3);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = UserEmail.getText().toString().trim();
                String password = UserPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    UserEmail.setError("E-mail is Required");
                    UserEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    UserEmail.setError("Enter valid E-mail Address");
                    UserEmail.setText("");
                    UserEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(password)) {
                    UserPassword.setError("Password is Required");
                    UserPassword.requestFocus();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(User_Login.this, "You have successfully logged in", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                UserEmail.setText("");
                                UserPassword.setText("");
                                openUser_Dashboard();
                            } else {
                                Toast.makeText(User_Login.this, "Failed to log in! Try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                UserEmail.setText("");
                                UserPassword.setText("");
                            }
                        }
                    });
                }
            }
        });

        textView2 = (TextView) findViewById(R.id.login_to_register);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSign_up_page();
            }
        });

    }
    public void openSign_up_page(){
        Intent intent =new Intent(this, User_Reg.class);
        startActivity(intent);
    }
    public void openUser_Dashboard()
    {
        Intent intent = new Intent(this,User_Dashboard.class);
        startActivity(intent);
    }
}