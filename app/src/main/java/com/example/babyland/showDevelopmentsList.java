package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class showDevelopmentsList extends AppCompatActivity {

    private String babyAmka;
    private ArrayList<Development> developments;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView developmentsRecyclerView;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    private TextView ageText,weightText, lengthText, dateText;
    private RelativeLayout developmentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_developments_list);

        //getting extras
        Bundle extras = getIntent().getExtras();
        babyAmka = extras.getString("babyAmka");

        //getting views
        developmentsRecyclerView = findViewById(R.id.developmentsRecyclerView);
        developmentLayout = findViewById(R.id.developmentLayout);
        ageText = findViewById(R.id.ageTextViewShow);
        lengthText = findViewById(R.id.lengthTextViewShow);
        weightText = findViewById(R.id.weightTextViewShow);
        dateText = findViewById(R.id.dateTextViewShow);

        //setting lists
        developments = new ArrayList<>();

        //setting visibilities
        developmentLayout.setVisibility(View.INVISIBLE);
        developmentsRecyclerView.setVisibility(View.VISIBLE);

        //setting database
        firebaseDatabase = FirebaseDatabase.getInstance();

        //getting data from database
        getDevelopments();


    }


    private void getDevelopments(){
        databaseReference = firebaseDatabase.getReference("monitoringDevelopment");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        GenericTypeIndicator<Development> t = new GenericTypeIndicator<Development>() {};
                        if(snapshots.getValue(t).getAmka().equals(babyAmka)){
                            developments.add(snapshots.getValue(t));
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

    //loads data from list into recyclerView
    private void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(listener, developments, "developments");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        developmentsRecyclerView.setLayoutManager(layoutManager);
        developmentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        developmentsRecyclerView.setAdapter(adapter);
    }

    //click listener to show developments details
    private void setOnClickListener() {
        listener = new recyclerAdapter.recyclerVewOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                developmentsRecyclerView.setVisibility(View.INVISIBLE);
                developmentLayout.setVisibility(View.VISIBLE);
                Development dev = developments.get(position);
                ageText.setText(dev.getAge() + " " + dev.getAgeType());
                lengthText.setText(dev.getLength());
                dateText.setText(dev.getMeasurementDate());
                weightText.setText(dev.getWeight());
                
            }
        };
    }


    //showing messages to users
    public void showMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

}