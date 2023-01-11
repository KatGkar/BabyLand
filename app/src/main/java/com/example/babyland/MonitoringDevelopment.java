package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonitoringDevelopment extends AppCompatActivity {
    private TextView dateText, doctorText;
    private EditText weightText, lengthText, headCircumferenceText, observationText;
    private ArrayList<developmentalItems> developmentalMonitoring;
    private ArrayList<examinationItems> examination;
    private ArrayList<sustenanceItems> sustenance;
    private Button sustenanceButton, examinationButton, developmentalMonitoringButton, observationsButton;
    private Switch hearingSwitch;
    private RelativeLayout generalLayout, sustenanceLayout, examinationLayout, developmentalLayout, observationsLayout;
    private CalendarView calendarView;
    private RecyclerView recyclerViewExamination, recyclerViewDevelopmental, recyclerViewSustenance;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String babyAmka, ageType, age, currentUser;
    private int monitoringDevNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_development);

        Bundle extras = getIntent().getExtras();
        babyAmka = extras.getString("babyAmka");
        ageType = extras.getString("ageType");
        age= extras.getString("age");

        //finding views on layout
        dateText = findViewById(R.id.dateText);
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
        recyclerViewExamination = findViewById(R.id.recyclerViewExamination);
        examinationLayout = findViewById(R.id.examinationLayout);
        recyclerViewDevelopmental = findViewById(R.id.recyclerViewDevelopmental);
        developmentalLayout = findViewById(R.id.developmentalLayout);
        observationsLayout = findViewById(R.id.observationsLayout);
        observationText = findViewById(R.id.observationsText);
        recyclerViewSustenance = findViewById(R.id.recyclerViewSustenance);


        //setting database
        firebaseDatabase = FirebaseDatabase.getInstance();

        //getting doctor's info
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = firebaseDatabase.getReference("doctor");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        String UID = snapshots.getKey();
                        if (UID.equals(currentUser)) {
                            doctorText.setText("Dr. " + snapshots.child("surname").getValue() + " " + snapshots.child("name").getValue());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        //get devs on firebase
        monitoringDevNumber = getDevNumber();

        //setting hint in date
        //getting current date
        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        String d = String.valueOf(dd);
        if (dd <= 9) {
            d = "0" + d;
        }
        if (mm < 12) {
            mm++;
        }
        String m = String.valueOf(mm);
        if (mm <= 9) {
            m = "0" + m;
        }
        dateText.setText(new StringBuilder()
                .append(d).append("/").append(m).append("/")
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
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String m = "0";
                String d = "0";
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    //date1 is selected date
                    Date date1 = sdf.parse(dayOfMonth + "/" + month + "/" + year);
                    int da = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    int mo = Calendar.getInstance().get(Calendar.MONTH);
                    int ye = Calendar.getInstance().get(Calendar.YEAR);
                    //date2 is date now
                    Date date2 = sdf.parse(da + "/" + mo + "/" + ye);
                    if (date1.compareTo(date2) > 0) {
                        //if date selected is after date now
                        mo++;
                        if (mo == 13) {
                            mo = 12;
                        }
                        if (mo <= 9) {
                            m = "0" + mo;
                        } else {
                            m = String.valueOf(mo);
                        }
                        if (da <= 9) {
                            d = "0" + da;
                        } else {
                            d = String.valueOf(da);
                        }
                        year = Calendar.getInstance().get(Calendar.YEAR);

                    } else {
                        //if date selected is now or if date selected is before date now
                        month++;
                        if (month == 13) {
                            month = 12;
                        }
                        if (month <= 9) {
                            m = "0" + month;
                        } else {
                            m = String.valueOf(month);
                        }
                        if (dayOfMonth <= 9) {
                            d = "0" + dayOfMonth;
                        } else {
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


        examination.clear();
        sustenance.clear();
        developmentalMonitoring.clear();
        getData("examination");
        getData("sustenance");
        getData("developmental");

        //sustenance button
        sustenanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalLayout.setVisibility(View.INVISIBLE);
                sustenanceLayout.setVisibility(View.VISIBLE);
            }
        });

        //examination button
        examinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalLayout.setVisibility(View.INVISIBLE);
                examinationLayout.setVisibility(View.VISIBLE);
            }
        });

        //Developmental monitoring button
        developmentalMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalLayout.setVisibility(View.INVISIBLE);
                developmentalLayout.setVisibility(View.VISIBLE);
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



    //showing messages to users
    public void showMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }


    //loads data from list into recyclerView
    private void setAdapter(String id) {
        if (id.equals("examination")) {
            recyclerAdapter adapter = new recyclerAdapter(listener, examination, "examination");
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewExamination.setLayoutManager(layoutManager);
            recyclerViewExamination.setItemAnimator(new DefaultItemAnimator());
            recyclerViewExamination.setAdapter(adapter);
            adapter.radioButtonChange(new recyclerAdapter.radioButtonChange() {
                @Override
                public void rChange(int id, int position) {
                    examination.get(position).setDetails(id);
                }
            });
            // recyclerView.addItemDecoration(new DividerItemDecoration(this,
            //       DividerItemDecoration.HORIZONTAL));
        } else if (id.equals("developmental")) {
            recyclerAdapter adapter = new recyclerAdapter(listener, developmentalMonitoring, "developmental");
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewDevelopmental.setLayoutManager(layoutManager);
            recyclerViewDevelopmental.setItemAnimator(new DefaultItemAnimator());
            recyclerViewDevelopmental.setAdapter(adapter);
            adapter.textChange(new recyclerAdapter.textChange() {
                @Override
                public void textChanged(int position, String text) {
                    developmentalMonitoring.get(position).setDetails(text);
                }
            });
        }else if(id.equals("sustenance")){
            recyclerAdapter adapter = new recyclerAdapter(listener, sustenance, "sustenance");
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewSustenance.setLayoutManager(layoutManager);
            recyclerViewSustenance.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSustenance.setAdapter(adapter);
            adapter.sustenanceCheck(new recyclerAdapter.sustenanceCheck() {
                @Override
                public void sustenanceChecked(int position, Boolean value) {
                    sustenance.get(position).setChecked(value);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (sustenanceLayout.getVisibility() == View.VISIBLE) {
            sustenanceLayout.setVisibility(View.INVISIBLE);
            generalLayout.setVisibility(View.VISIBLE);
        } else if (examinationLayout.getVisibility() == View.VISIBLE) {
            examinationLayout.setVisibility(View.INVISIBLE);
            generalLayout.setVisibility(View.VISIBLE);
        } else if (developmentalLayout.getVisibility() == View.VISIBLE) {
            developmentalLayout.setVisibility(View.INVISIBLE);
            generalLayout.setVisibility(View.VISIBLE);
        } else if (observationsLayout.getVisibility() == View.VISIBLE) {
            observationsLayout.setVisibility(View.INVISIBLE);
            generalLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }


    //loading data in adapter from database
    private void getData(String id) {
        DatabaseReference rootRef = null;
        if (id.equals("examination")) {
            rootRef = FirebaseDatabase.getInstance().getReference().child("examinationItems");
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot sn : snapshot.getChildren()) {
                            GenericTypeIndicator<examinationItems> t = new GenericTypeIndicator<examinationItems>() {
                            };
                            if(Integer.valueOf(age) <=4){
                                examination.add(sn.getValue(t));
                            }else{
                                if(sn.getValue(t).getAgeGap().equals("all")){
                                    examination.add(sn.getValue(t));
                                }
                            }
                        }
                    }
                    //calls adapter to load data into recyclerView
                    setAdapter("examination");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else if (id.equals("developmental")) {
            rootRef = FirebaseDatabase.getInstance().getReference().child("developmentalItems");
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot sn : snapshot.getChildren()) {
                            GenericTypeIndicator<developmentalItems> t = new GenericTypeIndicator<developmentalItems>() {
                            };
                            if(sn.getValue(t).getAgeGap().equals(age)){
                                developmentalMonitoring.add(sn.getValue(t));
                            }
                        }
                    }
                    //calls adapter to load data into recyclerView
                    setAdapter("developmental");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else if (id.equals("sustenance")) {
            rootRef = FirebaseDatabase.getInstance().getReference().child("sustenanceItems");
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot sn : snapshot.getChildren()) {
                            GenericTypeIndicator<sustenanceItems> t = new GenericTypeIndicator<sustenanceItems>() {
                            };
                            sustenanceItems s = sn.getValue(t);
                            String prev ="";
                            if(age.equals("1")){
                                age="0";
                            }
                            for (char c: s.getAgeGap().toCharArray()) {
                                if(c=='-'){
                                    prev="";
                                }else{
                                    prev = prev+c;
                                }
                                if(prev.equals(age)){
                                    sustenance.add(sn.getValue(t));
                                }
                            }
                            if(age.equals("0")){
                                age="1";
                            }
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


    //save function
    public void saveDevelopment(View view) throws ParseException {
        Boolean error = false;
        int examinationError = 0, developmentalError = 0, sustenanceError=0;
        if (TextUtils.isEmpty(lengthText.getText())) {
            error = true;
        }
        if (TextUtils.isEmpty(weightText.getText())) {
            error = true;
        }
        if (TextUtils.isEmpty(headCircumferenceText.getText())) {
            error = true;
        }

        for (int i = 0; i < sustenance.size(); i++) {
            if (sustenance.get(i).getChecked().equals(false)) {
                sustenanceError++;
            }
        }
        if (sustenanceError == sustenance.size()) {
            error = true;
        }

        for (int i = 0; i < examination.size(); i++) {
            if (examination.get(i).getDetails() != 1 && examination.get(i).getDetails() != 2 && examination.get(i).getDetails() != 3) {
                examinationError++;
            }
        }
        if (examinationError == examination.size()) {
            error = true;
        }
        for (int i = 0; i < developmentalMonitoring.size(); i++) {
            if (developmentalMonitoring.get(i).getDetails().isEmpty()) {
                developmentalError++;
            }
        }
        if (developmentalError == developmentalMonitoring.size()) {
            error = true;
        }

        if (!error) {
            Development dev = new Development(babyAmka, weightText.getText().toString(), lengthText.getText().toString(),
                    headCircumferenceText.getText().toString(), dateText.getText().toString(), age,
                    ageType, sustenance, examination, developmentalMonitoring, hearingSwitch.isChecked(), observationText.getText().toString(),
                    doctorText.getText().toString());
            System.out.println(monitoringDevNumber);
            databaseReference = firebaseDatabase.getReference("monitoringDevelopment");
            databaseReference.child(String.valueOf(monitoringDevNumber +1)).setValue(dev);
           // Intent intent = new Intent(MonitoringDevelopment.this, MainScreenParents.class);
            //startActivity(intent);
            onBackPressed();
        }
    }



    //getting number of babies on database
    private int getDevNumber() {
        databaseReference = firebaseDatabase.getReference("monitoringDevelopment");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                monitoringDevNumber = (int) snapshot.getChildrenCount();
                System.out.println(monitoringDevNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return (monitoringDevNumber + 1);
    }
}

