package com.project.parkingtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout inst_btn,user_btn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inst_btn=(RelativeLayout) findViewById(R.id.inst);
        user_btn=(RelativeLayout) findViewById(R.id.user);
        inst_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInst_Reg();
            }
        });
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUser_Login();
            }
        });
    }
    public void openInst_Reg(){
        Intent intent = new Intent(this, Inst_Reg.class);
        startActivity(intent);
    }
    public void openUser_Login(){
        Intent intent = new Intent(this,User_Login.class);
        startActivity(intent);

    }
}