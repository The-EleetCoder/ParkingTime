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
import com.google.firebase.database.FirebaseDatabase;

public class Inst_Reg extends AppCompatActivity {

    FirebaseAuth mAuth;
    //  FirebaseUser mUser;
    private TextView Inst_login_btn;
    private Button register;
    private EditText InstName, InstCity, InstState, InstSlot, InstPassword, InstMoney, InstEmail;
    private ProgressBar progressBar;
    private int slot,price;
    public int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_reg);
        mAuth = FirebaseAuth.getInstance();
//      mUser= mAuth.getCurrentUser();
        InstName= (EditText) findViewById(R.id.inpInstName);
        InstCity=(EditText) findViewById(R.id.inpInstCity);
        InstState=(EditText) findViewById(R.id.inpInstState);
        InstSlot=(EditText) findViewById(R.id.inpInstSlot);
        InstPassword=(EditText) findViewById(R.id.inpInstPassword);
        InstMoney=(EditText) findViewById(R.id.inpInstMoney);
        InstEmail=(EditText) findViewById(R.id.inpInstEmail);
        Inst_login_btn=(TextView) findViewById(R.id.alreadyInstAcc);
        register=(Button) findViewById(R.id.inpInstLogin);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        //If clicks on 'the already have an account' then redirected to login page
        Inst_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInstlogin();
            }
        });

        //if clicks on register then validates the credentials and if they are correct then registers user in firebase and redirects to Dashboard(Inst_Dashboard)
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = InstName.getText().toString().trim();
                String email = InstEmail.getText().toString().trim();
                String city = InstCity.getText().toString().trim();
                String state = InstState.getText().toString().trim();
                String password = InstPassword.getText().toString().trim();
                String money = InstMoney.getText().toString().trim();
                String slots = InstSlot.getText().toString().trim();
                if (!"".equals(slots))
                    slot = Integer.parseInt(slots);
                if (!"".equals(money))
                    price = Integer.parseInt(money);

                if (TextUtils.isEmpty(name)) {
                    InstName.setError("Name is Required");
                    InstName.requestFocus();
                } else if (TextUtils.isEmpty(city)) {
                    InstCity.setError("City is Required");
                    InstCity.requestFocus();
                } else if (TextUtils.isEmpty(state)) {
                    InstState.setError("State is Required");
                    InstState.requestFocus();
                } else if (TextUtils.isEmpty(slots)) {
                    InstSlot.setError("Mention the number of slots");
                    InstSlot.requestFocus();
                } else if (TextUtils.isEmpty(money)) {
                    InstMoney.setError("Price is Required");
                    InstMoney.requestFocus();
                }
                else if (TextUtils.isEmpty(email)) {
                    InstEmail.setError("E-mail is Required");
                    InstEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    InstEmail.setError("Enter valid E-mail Address");
                    InstEmail.setText("");
                    InstEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    InstPassword.setError("Password is Required");
                    InstPassword.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Inst_Data inst_data = new Inst_Data(name, city, state, slot, price, email, password, count);

                                        FirebaseDatabase.getInstance().getReference("Institution")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(inst_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Inst_Reg.this, "Your institution has been successfully registered", Toast.LENGTH_LONG).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    InstName.setText("");
                                                    InstCity.setText("");
                                                    InstState.setText("");
                                                    InstSlot.setText("");
                                                    InstEmail.setText("");
                                                    InstPassword.setText("");
                                                    InstMoney.setText("");
                                                    openInstlogin();
//                                                    openInst_Dashboard();
                                                } else {
                                                    Toast.makeText(Inst_Reg.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    InstEmail.setText("");
                                                    InstPassword.setText("");
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(Inst_Reg.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
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
    public void openInstlogin(){
        Intent intent= new Intent(this, Inst_Login.class);
        startActivity(intent);
    }
//    public void openInst_Dashboard()
//    {
//        Intent intent = new Intent(this,Inst_Dashboard.class);
//        startActivity(intent);
//    }
}