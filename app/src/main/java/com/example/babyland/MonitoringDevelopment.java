package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonitoringDevelopment extends AppCompatActivity {
    private TextView dateText;
    private EditText ageText, weightText, lengthText, headCircumferenceText, doctorText, sustenanceEditText;
    private Date date;
    private ArrayList<developmentalItems> developmentalMonitoring;
    private ArrayList<examinationItems> examination;
    private ArrayList<sustenanceItems> sustenance;
    private Boolean hearing;
    private String observations, doctor, sustenanceText;
    private Button sustenanceButton, examinationButton, developmentalMonitoringButton, observationsButton, backButtonExamination,
            backButtonDevelopmental, backButtonObservations, backButtonSustenance;
    private Switch hearingSwitch;
    private RelativeLayout generalLayout, sustenanceLayout, examinationLayout, developmentalLayout, observationsLayout;
    private CalendarView calendarView;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7;
    private RecyclerView recyclerViewExamination, recyclerViewDevelopmental;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_development);

        //finding views on layout
        dateText = findViewById(R.id.dateText);
        ageText = findViewById(R.id.ageText);
        weightText = findViewById(R.id.weightText);
        lengthText = findViewById(R.id.lengthText);
        headCircumferenceText = findViewById(R.id.headCircumferenceText);
        sustenanceButton = findViewById(R.id.sustenanceButton);
        examinationButton = findViewById(R.id.examinationButton);
        developmentalMonitoringButton = findViewById(R.id.developmentalMonitoringButton);
        doctorText = findViewById(R.id.doctorText);
        hearingSwitch = findViewById(R.id.hearingSwitch);
        observationsButton = findViewById(R.id.observationsButton);
        generalLayout = findViewById(R.id.generalLayout);
        calendarView = findViewById(R.id.calendar);
        sustenanceLayout = findViewById(R.id.sustenanceLayout);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        checkBox6 = findViewById(R.id.checkBox6);
        checkBox7 = findViewById(R.id.checkBox7);
        sustenanceEditText = findViewById(R.id.sustenanceText);
        recyclerViewExamination = findViewById(R.id.recyclerViewExamination);
        examinationLayout = findViewById(R.id.examinationLayout);
        backButtonExamination = findViewById(R.id.backButtonExamination);
        recyclerViewDevelopmental = findViewById(R.id.recyclerViewDevelopmental);
        developmentalLayout = findViewById(R.id.developmentalLayout);
        backButtonDevelopmental = findViewById(R.id.backButtonDevelopmental);
        backButtonObservations = findViewById(R.id.backButtonObservations);
        observationsLayout = findViewById(R.id.observationsLayout);
        backButtonSustenance = findViewById(R.id.backButtonSustenance);

        //setting database
        firebaseDatabase = FirebaseDatabase.getInstance();


        //setting lists
        examination = new ArrayList<>();
        sustenance = new ArrayList<>();
        developmentalMonitoring = new ArrayList<>();


        //setting visibilities
        generalLayout.setVisibility(View.VISIBLE);
        calendarView.setVisibility(View.INVISIBLE);
        sustenanceLayout.setVisibility(View.INVISIBLE);
        examinationLayout.setVisibility(View.INVISIBLE);
        developmentalLayout.setVisibility(View.INVISIBLE);


        //setting hint in date
        //getting current date
        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        String d = String.valueOf(dd);
        if(dd<=9){
            d = "0" + d;
        }
        if(mm<12){
            mm++;
        }
        String m = String.valueOf(mm);
        if(mm<=9){
            m = "0" + m;
        }
        dateText.setText(new StringBuilder()
                .append(d).append(" ").append("/").append(m).append("/")
                .append(yy));


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalLayout.setVisibility(View.INVISIBLE);
                calendarView.setVisibility(View.VISIBLE);
            }
        });

        //getting date of the calendar
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                String m="0";
                String d="0";
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    //date1 is selected date
                    Date date1 = sdf.parse(dayOfMonth + "/" + month + "/" + year);
                    int da = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    int mo = Calendar.getInstance().get(Calendar.MONTH);
                    int ye = Calendar.getInstance().get(Calendar.YEAR);
                    //date2 is date now
                    Date date2 = sdf.parse(da + "/"+ mo+ "/" + ye);
                    if ( date1.compareTo(date2) >0 ) {
                        //if date selected is after date now
                        mo++;
                        if(mo==13){
                            mo=12;
                        }
                        if(mo<=9){
                            m = "0"+ mo;
                        }else{
                            m = String.valueOf(mo);
                        }
                        if(da<=9){
                            d = "0"+da;
                        }else{
                            d = String.valueOf(da);
                        }
                        year = Calendar.getInstance().get(Calendar.YEAR);

                    }else {
                        //if date selected is now or if date selected is before date now
                        month++;
                        if(month == 13){
                            month = 12;
                        }
                        if(month<=9){
                            m = "0"+ month;
                        }else{
                            m = String.valueOf(month);
                        }
                        if(dayOfMonth<=9){
                            d = "0"+dayOfMonth;
                        }else{
                            d = String.valueOf(dayOfMonth);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("Watch out!!", "Something went wrong. Please try again later!");
                }
                dateText.setText(d + "/" + m + "/" + year);
                calendarView.setVisibility(View.INVISIBLE);
                generalLayout.setVisibility(View.VISIBLE);
            }
        });


        //sustenance button
        sustenanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalLayout.setVisibility(View.INVISIBLE);
                sustenanceLayout.setVisibility(View.VISIBLE);
                sustenance.clear();
                getData("sustenance");
            }
        });


        //examination button
        examinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalLayout.setVisibility(View.INVISIBLE);
                examinationLayout.setVisibility(View.VISIBLE);
                examination.clear();
                getData("examination");
            }
        });


        //Developmental monitoring button
        developmentalMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalLayout.setVisibility(View.INVISIBLE);
                developmentalLayout.setVisibility(View.VISIBLE);
                developmentalMonitoring.clear();
                getData("developmental");
            }
        });

        //observations button
        observationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalLayout.setVisibility(View.INVISIBLE);
                observationsLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    //back buttons
    public void backButton(View view){
        if(view == findViewById(R.id.backButtonObservations)){
            observationsLayout.setVisibility(View.INVISIBLE);
            generalLayout.setVisibility(View.VISIBLE);
        }else if(view == findViewById(R.id.backButtonExamination)){
            examinationLayout.setVisibility(View.INVISIBLE);
            generalLayout.setVisibility(View.VISIBLE);
        }else if(view == findViewById(R.id.backButtonSustenance)){
            sustenanceLayout.setVisibility(View.INVISIBLE);
            generalLayout.setVisibility(View.VISIBLE);
        }else if(view == findViewById(R.id.backButtonDevelopmental)){
            developmentalLayout.setVisibility(View.INVISIBLE);
            generalLayout.setVisibility(View.VISIBLE);
        }
    }



    //showing messages to users
    public void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }


    //loads data from list into recyclerView
    private void setAdapter(String id) {
        if(id.equals("examination")) {
            recyclerAdapter adapter = new recyclerAdapter(listener, examination, "examination");
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewExamination.setLayoutManager(layoutManager);
            recyclerViewExamination.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExamination.setAdapter(adapter);
            // recyclerView.addItemDecoration(new DividerItemDecoration(this,
            //       DividerItemDecoration.HORIZONTAL));
        }else if(id.equals("developmental")){
            recyclerAdapter adapter = new recyclerAdapter(listener, developmentalMonitoring, "developmental");
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewDevelopmental.setLayoutManager(layoutManager);
            recyclerViewDevelopmental.setItemAnimator((new DefaultItemAnimator()));
            recyclerViewExamination.setAdapter(adapter);
        }
    }


    //loading data in adapter from database
    private void getData(String id) {
        DatabaseReference rootRef= null;
        if(id.equals("examination")){
            rootRef = FirebaseDatabase.getInstance().getReference().child("examinationItems");
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot sn : snapshot.getChildren()){
                            examinationItems ex = sn.getValue(examinationItems.class);
                            examination.add(ex);
                        }
                    }
                    //calls adapter to load data into recyclerView
                    setAdapter("examination");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else if(id.equals("developmental")){
            rootRef = FirebaseDatabase.getInstance().getReference().child("developmentalItems");
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot sn : snapshot.getChildren()){
                            developmentalItems dev = sn.getValue(developmentalItems.class);
                            developmentalMonitoring.add(dev);
                        }
                    }
                    //calls adapter to load data into recyclerView
                    setAdapter("developmental");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else if(id.equals("sustenance")){
            rootRef = FirebaseDatabase.getInstance().getReference().child("sustenanceItems");
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot sn : snapshot.getChildren()){
                            sustenanceItems sus = sn.getValue(sustenanceItems.class);
                            sustenance.add(sus);
                        }
                    }
                    //calls adapter to load data into recyclerView
                    setAdapter("sustenance");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    }
}