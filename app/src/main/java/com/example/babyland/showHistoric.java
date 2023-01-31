package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class showHistoric extends AppCompatActivity {

    private RecyclerView familyHistoricRecyclerView;
    private String babyAmka;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<FamilyHistoryIllnesses> familyHistoric;
    private recyclerAdapter.recyclerVewOnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_historic);

        //getting extras
        Bundle extras = getIntent().getExtras();
        babyAmka = extras.getString("babyAmka");

        //getting view from xml file
        bottomNavigationView = findViewById(R.id.bottomNavigationViewShowHistoric);
        familyHistoricRecyclerView = findViewById(R.id.familyHistoricRecyclerView);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        //setting database
        database = FirebaseDatabase.getInstance();

        //find historic from database
        reference = database.getReference("baby");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        if (snapshots.child("amka").getValue().equals(babyAmka)) {
                            GenericTypeIndicator<ArrayList<FamilyHistoryIllnesses>> t = new GenericTypeIndicator<ArrayList<FamilyHistoryIllnesses>>() {
                            };
                            familyHistoric = snapshots.child("iln").getValue(t);
                        }
                    }
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //setting listener for navigation bar
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });


        //on item click
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        return true;
                    case R.id.navigation_add:
                        Intent intent = new Intent(showHistoric.this, AddChildToDoctor.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_account:
                        Intent intent1 = new Intent(showHistoric.this, UserAccount.class);
                        intent1.putExtra("user", "doctor");
                        startActivity(intent1);
                        return true;
                }
                return false;
            }
        });

    }

    //setting adapter for recyclerView
    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(listener, familyHistoric, "familyHistoric","none");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        familyHistoricRecyclerView.setLayoutManager(layoutManager);
        familyHistoricRecyclerView.setItemAnimator(new DefaultItemAnimator());
        familyHistoricRecyclerView.setAdapter(adapter);
    }

    //on page resume
    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

}