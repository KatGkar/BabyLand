package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
        familyHistoricRecyclerView = findViewById(R.id.familyHistoricRecyclerView);

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
    }

    //setting adapter for recyclerView
    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(listener, familyHistoric, "familyHistoric","none");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        familyHistoricRecyclerView.setLayoutManager(layoutManager);
        familyHistoricRecyclerView.setItemAnimator(new DefaultItemAnimator());
        familyHistoricRecyclerView.setAdapter(adapter);
    }
}