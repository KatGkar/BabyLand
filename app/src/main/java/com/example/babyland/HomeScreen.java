package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {
    LineChart lineChart;
    private ArrayList<Development> devs;
    private ArrayList<Entry> dataValues;
    private String babyAmka;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        lineChart = findViewById(R.id.line12);

        Bundle extras = getIntent().getExtras();
        babyAmka = extras.getString("babyAmka");


        //setting database
        database = FirebaseDatabase.getInstance();

        devs = new ArrayList<>();


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
                    completeLineChart();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void completeLineChart(){
        dataValues = new ArrayList<>();
        for(int i=0;i<devs.size();i++){
            dataValues.add(new Entry(i+1, Integer.valueOf(devs.get(i).getLength())));
        }

        LineDataSet lineDataSet = new LineDataSet(dataValues, "Data Set 1");

        LineData data = new LineData(lineDataSet);
        lineChart.setData(data);
        lineChart.invalidate();

    }

}