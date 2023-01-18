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

import java.sql.Array;
import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {
    LineChart lineChart;
    private ArrayList<Development> devs;
    private ArrayList<Entry> dataValues;
    private String babyAmka;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Double> calculations, L, M, S;
    Double[][] ZtoPercentile;


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


        L.add(0.3809);
        L.add(0.1714);
        L.add(0.0962);
        L.add(0.0402);
        L.add(-0.0050);
        L.add(-0.0430);
        L.add(-0.0756);
        L.add(-0.1039);
        L.add(-0.1288);
        L.add(-0.1507);
        L.add(-0.1700);
        L.add(-0.1872);
        L.add(-0.2024);
        L.add(-0.2158);
        L.add(-0.2278);
        L.add(-0.2384);
        L.add(-0.2478);
        L.add(-0.2562);
        L.add(-0.2637);
        L.add(-0.2703);
        L.add(-0.2762);
        L.add(-0.2815);
        L.add(-0.2862);
        L.add(-0.2903);
        L.add(-0.2941);



        M.add(3.2322);
        M.add(4.1873);
        M.add(5.1282);
        M.add(5.8458);
        M.add(6.4237);
        M.add(6.8985);
        M.add(7.2970);
        M.add(7.6422);
        M.add(7.9487);
        M.add(8.2254);
        M.add(8.4800);
        M.add(8.7192);
        M.add(8.9481);
        M.add(9.1699);
        M.add(9.3870);
        M.add(9.6008);
        M.add(9.8124);
        M.add(10.0226);
        M.add(10.2315);
        M.add(10.4393);
        M.add(10.6464);
        M.add(10.8534);
        M.add(11.0608);
        M.add(11.2688);
        M.add(11.4775);


        S.add(0.14171);
        S.add(0.13724);
        S.add(0.13000);
        S.add(0.12619);
        S.add(0.12402);
        S.add(0.12274);
        S.add(0.12204);
        S.add(0.12178);
        S.add(0.12181);
        S.add(0.12199);
        S.add(0.12223);
        S.add(0.12247);
        S.add(0.12268);
        S.add(0.12283);
        S.add(0.12294);
        S.add(0.12299);
        S.add(0.12303);
        S.add(0.12306);
        S.add(0.12309);
        S.add(0.12315);
        S.add(0.12323);
        S.add(0.12335);
        S.add(0.12350);
        S.add(0.12369);
        S.add(0.12390);


        double weight = 2.36;
        /*for(int i=0;i<26;i++){
            Double cal = (((weight/M.get(i))^ L.get(i))-1)/(L.get(i)*S.get(i));
            calculations.add(cal);
        }*/


        ZtoPercentile[0][0] = -3.0;
        ZtoPercentile[1][0] = -2.9;
        ZtoPercentile[2][0] = -2.8;
        ZtoPercentile[3][0] = -2.7;
        ZtoPercentile[4][0] = -2.6;
        ZtoPercentile[5][0] = -2.5;
        ZtoPercentile[6][0] = -2.4;
        ZtoPercentile[7][0] = -2.3;
        ZtoPercentile[8][0] = -2.2;
        ZtoPercentile[8][0] = -2.1;
        ZtoPercentile[10][0] = -2.0;
        ZtoPercentile[11][0] = -1.9;
        ZtoPercentile[12][0] = -1.8;
        ZtoPercentile[13][0] = -1.7;
        ZtoPercentile[14][0] = -1.6;
        ZtoPercentile[15][0] = -1.5;
        ZtoPercentile[16][0] = -1.4;
        ZtoPercentile[17][0] = -1.3;
        ZtoPercentile[18][0] = -1.2;
        ZtoPercentile[19][0] = -1.1;
        ZtoPercentile[20][0] = -1.0;
        ZtoPercentile[21][0] = -0.9;
        ZtoPercentile[22][0] = -0.8;
        ZtoPercentile[23][0] = -0.7;
        ZtoPercentile[24][0] = -0.6;
        ZtoPercentile[25][0] = -0.5;
        ZtoPercentile[26][0] = -0.4;
        ZtoPercentile[27][0] = -0.3;
        ZtoPercentile[28][0] = -0.2;
        ZtoPercentile[29][0] = -0.1;
        ZtoPercentile[30][0] = 0.0;
        ZtoPercentile[31][0] = 0.1;
        ZtoPercentile[32][0] = 0.2;
        ZtoPercentile[33][0] = 0.3;
        ZtoPercentile[34][0] = 0.4;
        ZtoPercentile[35][0] = 0.5;
        ZtoPercentile[36][0] = 0.6;
        ZtoPercentile[37][0] = 0.7;
        ZtoPercentile[38][0] = 0.8;
        ZtoPercentile[39][0] = 0.9;
        ZtoPercentile[40][0] = 1.0;
        ZtoPercentile[41][0] = 1.1;
        ZtoPercentile[42][0] = 1.2;
        ZtoPercentile[43][0] = 1.3;
        ZtoPercentile[44][0] = 1.4;
        ZtoPercentile[45][0] = 1.5;
        ZtoPercentile[46][0] = 1.6;
        ZtoPercentile[47][0] = 1.7;
        ZtoPercentile[48][0] = 1.8;
        ZtoPercentile[49][0] = 1.9;
        ZtoPercentile[50][0] = 2.0;
        ZtoPercentile[51][0] = 2.1;
        ZtoPercentile[52][0] = 2.2;
        ZtoPercentile[53][0] = 2.3;
        ZtoPercentile[54][0] = 2.4;
        ZtoPercentile[55][0] = 2.5;
        ZtoPercentile[56][0] = 2.6;
        ZtoPercentile[57][0] = 2.7;
        ZtoPercentile[58][0] = 2.8;
        ZtoPercentile[59][0] = 2.9;
        ZtoPercentile[60][0] = 3.0;


        ZtoPercentile[0][1] = 0.13;
        ZtoPercentile[1][1] = 0.19;
        ZtoPercentile[2][1] = 0.26;
        ZtoPercentile[3][1] = 0.35;
        ZtoPercentile[4][1] = 0.47;
        ZtoPercentile[5][1] = 0.62;
        ZtoPercentile[6][1] = 0.82;
        ZtoPercentile[7][1] = 1.07;
        ZtoPercentile[8][1] = 1.39;
        ZtoPercentile[9][1] = 1.79;
        ZtoPercentile[10][1] = 2.28;
        ZtoPercentile[11][1] = 2.87;
        ZtoPercentile[12][1] = 3.59;
        ZtoPercentile[13][1] = 4.46;
        ZtoPercentile[14][1] = 5.48;
        ZtoPercentile[15][1] = 6.68;
        ZtoPercentile[16][1] = 8.08;
        ZtoPercentile[17][1] = 9.68;
        ZtoPercentile[18][1] = 11.51;
        ZtoPercentile[19][1] = 13.57;
        ZtoPercentile[20][1] = 15.87;
        ZtoPercentile[21][1] = 18.41;
        ZtoPercentile[22][1] = 21.19;
        ZtoPercentile[23][1] = 24.20;
        ZtoPercentile[24][1] = 27.43;
        ZtoPercentile[25][1] = 30.85;
        ZtoPercentile[26][1] = 34.46;
        ZtoPercentile[27][1] = 38.21;
        ZtoPercentile[28][1] = 42.07;
        ZtoPercentile[29][1] = 46.02;
        ZtoPercentile[30][1] = 50.00;
        ZtoPercentile[31][1] = 53.98;
        ZtoPercentile[32][1] = 57.93;
        ZtoPercentile[33][1] = 61.79;
        ZtoPercentile[34][1] = 65.54;
        ZtoPercentile[35][1] = 69.15;
        ZtoPercentile[36][1] = 72.58;
        ZtoPercentile[37][1] = 75.80;
        ZtoPercentile[38][1] = 78.81;
        ZtoPercentile[39][1] = 81.59;
        ZtoPercentile[40][1] = 84.13;
        ZtoPercentile[41][1] = 86.43;
        ZtoPercentile[42][1] = 88.49;
        ZtoPercentile[43][1] = 90.32;
        ZtoPercentile[44][1] = 91.92;
        ZtoPercentile[45][1] = 93.32;
        ZtoPercentile[46][1] = 94.52;
        ZtoPercentile[47][1] = 95.54;
        ZtoPercentile[48][1] = 96.41;
        ZtoPercentile[49][1] = 97.13;
        ZtoPercentile[50][1] = 97.73;
        ZtoPercentile[51][1] = 98.21;
        ZtoPercentile[52][1] = 98.61;
        ZtoPercentile[53][1] = 98.93;
        ZtoPercentile[54][1] = 99.18;
        ZtoPercentile[55][1] = 99.38;
        ZtoPercentile[56][1] = 99.53;
        ZtoPercentile[57][1] = 99.65;
        ZtoPercentile[58][1] = 99.74;
        ZtoPercentile[59][1] = 99.81;
        ZtoPercentile[60][1] = 99.87;




























        //getting baby developments
/*        reference =database.getReference("monitoringDevelopment");
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
*/

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