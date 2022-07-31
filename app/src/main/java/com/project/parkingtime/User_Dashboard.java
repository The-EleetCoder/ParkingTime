package com.project.parkingtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class User_Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser user;
    private DatabaseReference reference,inst_reference;
    private String UserID;
    DrawerLayout drawerLayout;
    NavigationView nav_view;
    ArrayList<Inst_Data> list;
    RecyclerView recyclerView;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        drawerLayout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        inst_reference = FirebaseDatabase.getInstance().getReference("Institution");
        UserID = user.getUid();
        reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Data profile =snapshot.getValue(User_Data.class);
                if(profile != null){
                    String name = profile.username;
                    setTitle(name+"'s Dashboard");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_Dashboard.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        nav_view.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);

        nav_view.setCheckedItem(R.id.nav_user_home);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(inst_reference!=null)
        {
            inst_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        list=new ArrayList<>();
                       for(DataSnapshot ds :dataSnapshot.getChildren())
                       {
                            list.add(ds.getValue(Inst_Data.class));
                       }
                       AdapterClass adapterClass = new AdapterClass(list);
                       recyclerView.setAdapter(adapterClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(User_Dashboard.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        if(searchView!=null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }
    private void search(String str)
    {
        ArrayList<Inst_Data> myList = new ArrayList<>();
        for (Inst_Data object : list)
        {
            if(object.name.toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        AdapterClass adapterClass = new AdapterClass(myList);
        recyclerView.setAdapter(adapterClass);
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
            case R.id.nav_user_home:

                break;
            case R.id.nav_user_Profile:
                Intent intent = new Intent (User_Dashboard.this,User_Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_user_Settings:
                Intent intent1 = new Intent (User_Dashboard.this,User_Settings.class);
                startActivity(intent1);
                break;
            case R.id.nav_user_SignOut:
                Intent intent2 = new Intent (User_Dashboard.this,MainActivity.class);
                startActivity(intent2);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}