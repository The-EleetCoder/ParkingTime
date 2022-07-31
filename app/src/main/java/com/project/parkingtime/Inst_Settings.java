package com.project.parkingtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Inst_Settings extends AppCompatActivity {

    private FirebaseUser Inst_user;
    private DatabaseReference reference;
    private String InstID;
    private EditText disp_slot,disp_price,disp_pass;
    private Button update;
    private int cost,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_settings);
        update = (Button) findViewById(R.id.updateBtn);
        disp_slot = (EditText) findViewById(R.id.upd_inst_slot);
        disp_price = (EditText) findViewById(R.id.upd_inst_price);
        disp_pass = (EditText) findViewById(R.id.upd_inst_pass);
        Inst_user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Institution");
        InstID = Inst_user.getUid();
        reference.child(InstID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Inst_Data Inst_profile =snapshot.getValue(Inst_Data.class);
                if(Inst_profile != null){
                    int slot = Inst_profile.slots;
                    int price = Inst_profile.price;
                    String password = Inst_profile.password;
                    disp_slot.setText(slot+"");
                    disp_price.setText(price+"");
                    disp_pass.setText(password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Inst_Settings.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = disp_pass.getText().toString().trim();
                String money = disp_price.getText().toString().trim();
                String slots = disp_slot.getText().toString().trim();
                if (!"".equals(slots))
                   number= Integer.parseInt(slots);
                if (!"".equals(money))
                   cost = Integer.parseInt(money);
                if (TextUtils.isEmpty(slots)) {
                    disp_slot.setError("No. of slots can't be empty");
                    disp_slot.requestFocus();
                } else if (TextUtils.isEmpty(money)) {
                    disp_price.setError("Price can't be empty");
                    disp_price.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    disp_pass.setError("Password is required");
                    disp_pass.requestFocus();
                }
                else
                {
                    HashMap inst = new HashMap();
                    inst.put("slots",number);
                    inst.put("price",cost);
                    inst.put("password",password);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(inst).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Inst_Settings.this, "Successfully Updated!", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(Inst_Settings.this, "Failed to Update! Try again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}