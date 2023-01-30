package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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
    private TextView ageText,weightText, lengthText, dateText, headCircumferenceText, doctorTextView, observationsTextView,
                noDevelopmentTextView, textViewDevs;
    private BottomNavigationView bottomNavigationView;
    private ScrollView scrollViewDevs;
    private Switch hearingSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_developments_list);

        //getting extras
        Bundle extras = getIntent().getExtras();
        babyAmka = extras.getString("babyAmka");

        //getting views from xml file
        bottomNavigationView = findViewById(R.id.bottomNavigationViewShowDevelopments);
        developmentsRecyclerView = findViewById(R.id.developmentsRecyclerView);
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
        textViewDevs = findViewById(R.id.textViewDevelopments);
        scrollViewDevs = findViewById(R.id.scrollViewShowDevs);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        //setting lists
        developments = new ArrayList<>();

        //setting visibilities
        doctorTextView.setVisibility(View.INVISIBLE);
        developmentsRecyclerView.setVisibility(View.VISIBLE);
        textViewDevs.setVisibility(View.VISIBLE);
        scrollViewDevs.setVisibility(View.INVISIBLE);
        noDevelopmentTextView.setVisibility(View.INVISIBLE);

        //setting database
        database = FirebaseDatabase.getInstance();

        //getting data from database
        getDevelopments();

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
                        addChild();
                        return true;
                    case R.id.navigation_account:
                        settingsButton();
                        return true;
                }
                return false;
            }
        });

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
                        textViewDevs.setVisibility(View.INVISIBLE);
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
        if(scrollViewDevs.getVisibility() == View.VISIBLE){
            doctorTextView.setVisibility(View.INVISIBLE);
            scrollViewDevs.setVisibility(View.INVISIBLE);
            developmentsRecyclerView.setVisibility(View.VISIBLE);
            textViewDevs.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }

    //go to settings
    private void settingsButton(){
        Intent intent = new Intent(showDevelopmentsList.this, UserAccount.class);
        intent.putExtra("user", "doctor");
        startActivity(intent);
    }

    //on page resume
    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    //go to add child page
    private void addChild(){
        Intent intent = new Intent(showDevelopmentsList.this, AddChildToDoctor.class);
        startActivity(intent);
    }

    //click listener to show developments details
    private void setOnClickListener() {
        listener = new recyclerAdapter.recyclerVewOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                developmentsRecyclerView.setVisibility(View.INVISIBLE);
                textViewDevs.setVisibility(View.INVISIBLE);
                doctorTextView.setVisibility(View.VISIBLE);
                scrollViewDevs.setVisibility(View.VISIBLE);
                Development dev = developments.get(position);
                ageText.setText("Age: " + dev.getAge() + " " + dev.getAgeType());
                lengthText.setText("Length: " + dev.getLength() + "cm");
                dateText.setText("Measurement Date: " +dev.getMeasurementDate());
                weightText.setText("Weight: " + dev.getWeight()+ "cm");
                headCircumferenceText.setText("Head Circumference: " + dev.getHeadCircumference()+ "cm");
                hearingSwitch.setChecked(dev.getHearing());
                doctorTextView.setText(dev.getDoctor());
                doctorTextView.setPaintFlags(doctorTextView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                if(dev.getObservations() == null || dev.getObservations().equals("") || TextUtils.isEmpty(dev.getObservations())){
                    observationsTextView.setText("No observations!");
                }else{
                    observationsTextView.setText(dev.getObservations());
                }
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