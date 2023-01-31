package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.data.model.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewCharts extends AppCompatActivity {

    private LineChart lineChart;
    private ArrayList<Development> devs;
    private ArrayList<Entry> dataValues, dataValues2, dataValues3, dataValues4, dataValues5, dataValuesChild;
    private ArrayList<ILineDataSet> dataSets;
    private String babyAmka, babySex, userType;
    private BottomNavigationView bottomNavigationView;
    private FirebaseDatabase database;
    private DatabaseReference reference, reference2, reference3;
    private RelativeLayout buttonsRelativeLayout;
    private ImageButton lengthButton,  weightButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_charts);

        //getting extras
        Bundle extras = getIntent().getExtras();
        babyAmka = extras.getString("babyAmka");
        userType = extras.getString("userType");

        //getting views from xml file
        lineChart = findViewById(R.id.chart);
        lengthButton = findViewById(R.id.lengthButton);
        weightButton = findViewById(R.id.weightButton);
        buttonsRelativeLayout = findViewById(R.id.buttonsRelativeLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewViewCharts);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        //setting visibilities
        lineChart.setVisibility(View.INVISIBLE);
        buttonsRelativeLayout.setVisibility(View.VISIBLE);

        //setting database
        database = FirebaseDatabase.getInstance();

        //setting arraylists
        dataValues = new ArrayList<>();
        dataValues2 =new ArrayList<>();
        dataValues3 =new ArrayList<>();
        dataValues4 =new ArrayList<>();
        dataValues5 =new ArrayList<>();
        dataValuesChild = new ArrayList<>();
        devs = new ArrayList<>();
        dataSets = new ArrayList<>();

        //find baby sex
        findBabySex();

        //getting baby developments
       reference =database.getReference("monitoringDevelopment");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        GenericTypeIndicator<Development> t = new GenericTypeIndicator<Development>() {};
                        if(snapshots.getValue(t).getAmka().equals(babyAmka)){
                            devs.add(snapshots.getValue(t));
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //length button
        lengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataValues.clear();
                dataValues2.clear();
                dataValues3.clear();
                dataValues4.clear();
                dataValues5.clear();
                showStatistics("length");
            }
        });

        //weight button
        weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataValues.clear();
                dataValues2.clear();
                dataValues3.clear();
                dataValues4.clear();
                dataValues5.clear();
                showStatistics("weight");
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
                        if(userType.equals("doctor")){
                            Intent intent = new Intent(viewCharts.this, AddChildToDoctor.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), AddBaby.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.navigation_account:
                        if(userType.equals("doctor")){
                            Intent intent = new Intent(viewCharts.this, UserAccount.class);
                            intent.putExtra("user", "doctor");
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(viewCharts.this, UserAccount.class);
                            intent.putExtra("user", "parent");
                            startActivity(intent);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    //showing line chart
    private void completeLineChart(String type){
        buttonsRelativeLayout.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.VISIBLE);
        //setting babies statistics to array list
        if(type.equals("length")){
            for(int i=0;i<devs.size();i++){
                dataValuesChild.add(new Entry(Float.valueOf(devs.get(i).getAge()), Float.valueOf(devs.get(i).getLength())));
            }
        }else{
            for(int i=0;i<devs.size();i++){
                dataValuesChild.add(new Entry(Float.valueOf(devs.get(i).getAge()), Float.valueOf(devs.get(i).getWeight())));
            }
        }

        //creating line datasets
        LineDataSet lineChild = new LineDataSet(dataValuesChild,"Child");
        LineDataSet lineDataSet = new LineDataSet(dataValues, "3rd Per");
        LineDataSet lineDataSet2 = new LineDataSet(dataValues2, "10th Per");
        LineDataSet lineDataSet3 = new LineDataSet(dataValues3, "50th Per");
        LineDataSet lineDataSet4 = new LineDataSet(dataValues4, "90th Per");
        LineDataSet lineDataSet5 = new LineDataSet(dataValues5, "97th Per");

        //customizing line datasets
        lineChild.setColor(Color.RED);//line child
        lineDataSet.setColor(Color.BLACK);
        lineDataSet2.setColor(Color.BLACK);
        lineDataSet3.setColor(Color.BLACK);
        lineDataSet4.setColor(Color.BLACK);
        lineDataSet5.setColor(Color.BLACK);
        lineChild.setCircleColor(Color.RED);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet2.setCircleColor(Color.BLACK);
        lineDataSet3.setCircleColor(Color.BLACK);
        lineDataSet4.setCircleColor(Color.BLACK);
        lineDataSet5.setCircleColor(Color.BLACK);
        lineChild.setDrawCircleHole(false);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet2.setDrawCircleHole(false);
        lineDataSet3.setDrawCircleHole(false);
        lineDataSet4.setDrawCircleHole(false);
        lineDataSet5.setDrawCircleHole(false);

        dataSets.add(lineDataSet);
        dataSets.add(lineDataSet2);
        dataSets.add(lineDataSet3);
        dataSets.add(lineDataSet4);
        dataSets.add(lineDataSet5);
        dataSets.add(lineChild);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();

    }

    //getting statistics from database based on WHO data
    private void showStatistics(String type){
        Description description = new Description();
        description.setTextColor(Color.BLACK);
        description.setTextSize(22);
        lineChart.setDescription(description);
        if(type.equals("length")) {
            reference3 = database.getReference("chartData").child(babySex).child("lengthForAge");
            description.setText("Length chart");
        }else{
            reference3 = database.getReference("chartData").child(babySex).child("weightForAge");
            description.setText("Weight chart");
        }
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    GenericTypeIndicator<ArrayList<Entry>> t = new GenericTypeIndicator<ArrayList<Entry>>() {};
                    dataValues = snapshot.child("3").getValue(t);
                    dataValues2 = snapshot.child("10").getValue(t);
                    dataValues3 = snapshot.child("50").getValue(t);
                    dataValues4 = snapshot.child("90").getValue(t);
                    dataValues5 = snapshot.child("97").getValue(t);
                }
                completeLineChart(type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //finding child sex
    private void findBabySex(){
        reference2 = database.getReference("baby").child(babyAmka);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    babySex = snapshot.child("sex").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //back button pressed
    @Override
    public void onBackPressed() {
        if(buttonsRelativeLayout.getVisibility() == View.INVISIBLE){
            lineChart.setVisibility(View.INVISIBLE);
            buttonsRelativeLayout.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }
    }

    //on rsume page
    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        super.onResume();
    }
}