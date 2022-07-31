package com.project.parkingtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Inst_Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser Inst_user;
    private DatabaseReference reference;
    private String InstID;
    DrawerLayout drawerLayout;
    NavigationView nav_view;
    private TextView slots_left,slots_booked;
    private Button add_book, cancel_book;
    private int c,slots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_dashboard);
        drawerLayout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        slots_left=(TextView) findViewById(R.id.inst_slots_left);
        slots_booked=(TextView) findViewById(R.id.inst_slots_booked);
        add_book = (Button) findViewById(R.id.add_booking);
        cancel_book = (Button) findViewById(R.id.cancel_booking);
        slots_left.startAnimation(AnimationUtils.loadAnimation(Inst_Dashboard.this,R.anim.text_anim));
        slots_booked.startAnimation(AnimationUtils.loadAnimation(Inst_Dashboard.this,R.anim.text_anim));
        setSupportActionBar(toolbar);

        Inst_user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Institution");
        InstID = Inst_user.getUid();
        reference.child(InstID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Inst_Data Inst_profile =snapshot.getValue(Inst_Data.class);
                if(Inst_profile != null){
                    String name = Inst_profile.name;
                    c= Inst_profile.count;
                    slots= Inst_profile.slots;
                    setTitle(name+"'s Dashboard");
                    slots_left.setText((slots-c)+"");
                    slots_booked.setText(c+"");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Inst_Dashboard.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        nav_view.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);

        nav_view.setCheckedItem(R.id.nav_Parking_Status);

        add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c<slots) {
                    HashMap inst = new HashMap();
                    c=c+1;
                    inst.put("count",c);
                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(inst).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Inst_Dashboard.this, "Successfully added booking!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Inst_Dashboard.this, "Failed to add booking! Try again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(Inst_Dashboard.this, "No of bookings is full! Can't add anymore", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        cancel_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c>0)
                {
                    HashMap inst1 = new HashMap();
                    c=c-1;
                    inst1.put("count",c);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(inst1).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Inst_Dashboard.this, "Removed a booking!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Inst_Dashboard.this, "Failed to remove booking! Try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Inst_Dashboard.this, "No of bookings is 0! Can't remove anymore", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_Parking_Status:

                break;
            case R.id.nav_Profile:
                Intent intent = new Intent (Inst_Dashboard.this,Inst_Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_Settings:
                Intent intent1 = new Intent (Inst_Dashboard.this,Inst_Settings.class);
                startActivity(intent1);
                break;
            case R.id.nav_SignOut:
                Intent intent2 = new Intent (Inst_Dashboard.this,MainActivity.class);
                startActivity(intent2);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}