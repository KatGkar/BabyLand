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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView developmentsRecyclerView, developmentalMonitoringRecyclerView, examinationRecyclerView, sustenanceRecyclerView;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    private TextView ageText,weightText, lengthText, dateText, headCircumferenceText, doctorTextView, observationsTextView, noDevelopmentTextView;
    private RelativeLayout developmentLayout;
    private Switch hearingSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_developments_list);

        //getting extras
        Bundle extras = getIntent().getExtras();
        babyAmka = extras.getString("babyAmka");

        //getting views from xml file
        developmentsRecyclerView = findViewById(R.id.developmentsRecyclerView);
        developmentLayout = findViewById(R.id.developmentLayout);
        ageText = findViewById(R.id.ageTextViewShow);
        lengthText = findViewById(R.id.lengthTextViewShow);
        weightText = findViewById(R.id.weightTextViewShow);
        dateText = findViewById(R.id.dateTextViewShow);
        headCircumferenceText = findViewById(R.id.headCircumferenceTextViewShow);
        developmentalMonitoringRecyclerView = findViewById(R.id.developmentalMonitoringRecyclerView);
        examinationRecyclerView = findViewById(R.id.examinationRecyclerView);
        hearingSwitch = findViewById(R.id.hearingShowDevelopmentsSwitch);
        doctorTextView = findViewById(R.id.doctorShowDevelopmentsTextView);
        sustenanceRecyclerView = findViewById(R.id.sustenanceRecyclerView);
        observationsTextView = findViewById(R.id.observationsShowDevelopmentsTextView);
        noDevelopmentTextView = findViewById(R.id.noDevelopmentTextView);

        //setting lists
        developments = new ArrayList<>();

        //setting visibilities
        developmentLayout.setVisibility(View.INVISIBLE);
        developmentsRecyclerView.setVisibility(View.VISIBLE);
        noDevelopmentTextView.setVisibility(View.INVISIBLE);

        //setting database
        database = FirebaseDatabase.getInstance();

        //getting data from database
        getDevelopments();


    }

    //getting developments form database
    private void getDevelopments(){
        reference = database.getReference("monitoringDevelopment");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        GenericTypeIndicator<Development> t = new GenericTypeIndicator<Development>() {};
                        if(snapshots.getValue(t).getAmka().equals(babyAmka)){
                            developments.add(snapshots.getValue(t));
                        }
                    }
                    if(developments.isEmpty()){
                        noDevelopmentTextView.setVisibility(View.VISIBLE);
                        noDevelopmentTextView.setText("No development for choosen child has been found!");
                    }else{
                        setAdapter();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //loading data into recyclerView
    private void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(listener, developments, "developments","none");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        developmentsRecyclerView.setLayoutManager(layoutManager);
        developmentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        developmentsRecyclerView.setAdapter(adapter);
    }

    //on back button pressed
    @Override
    public void onBackPressed() {
        if(developmentLayout.getVisibility() == View.VISIBLE){
            developmentLayout.setVisibility(View.INVISIBLE);
            developmentsRecyclerView.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }

    //click listener to show developments details
    private void setOnClickListener() {
        listener = new recyclerAdapter.recyclerVewOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                developmentsRecyclerView.setVisibility(View.INVISIBLE);
                developmentLayout.setVisibility(View.VISIBLE);
                Development dev = developments.get(position);
                ageText.setText("Age: " + dev.getAge() + " " + dev.getAgeType());
                lengthText.setText("Length: " + dev.getLength());
                dateText.setText("Measurement Date: " +dev.getMeasurementDate());
                weightText.setText("Weight: " + dev.getWeight());
                headCircumferenceText.setText("Head Circumference: " + dev.getHeadCircumference());
                hearingSwitch.setChecked(dev.getHearing());
                doctorTextView.setText(dev.getDoctor());
                observationsTextView.setText(dev.getObservations());
                //developmental monitoring info
                recyclerAdapter adapter = new recyclerAdapter(listener, dev.getDevelopmentalMonitoring(), "developmentalMonitoring","none");
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                developmentalMonitoringRecyclerView.setLayoutManager(layoutManager);
                developmentalMonitoringRecyclerView.setItemAnimator(new DefaultItemAnimator());
                developmentalMonitoringRecyclerView.setAdapter(adapter);
                //examination info
                adapter = new recyclerAdapter(listener, dev.getExamination(), "exam","none");
                layoutManager = new LinearLayoutManager(getApplicationContext());
                examinationRecyclerView.setLayoutManager(layoutManager);
                examinationRecyclerView.setItemAnimator(new DefaultItemAnimator());
                examinationRecyclerView.setAdapter(adapter);
                //sustenance info
                adapter = new recyclerAdapter(listener, dev.getSustenance(), "sust","none");
                layoutManager = new LinearLayoutManager(getApplicationContext());
                sustenanceRecyclerView.setLayoutManager(layoutManager);
                sustenanceRecyclerView.setItemAnimator(new DefaultItemAnimator());
                sustenanceRecyclerView.setAdapter(adapter);
            }
        };
    }


}