package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class AddVaccination extends AppCompatActivity {

    private ArrayList<Vaccination> vaccines;
    private RecyclerView vaccinationRecyclerView;
    private String babyAmka, currentUser, doctorName;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private recyclerAdapter.recyclerVewOnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccination);

        //getting extras
        Bundle extras = getIntent().getExtras();
        babyAmka = extras.getString("babyAmka");

        //getting views from xml
        vaccinationRecyclerView = findViewById(R.id.vaccinationRecyclerView);

        //setting database
        database = FirebaseDatabase.getInstance();

        //find vaccinaiton list of baby
        reference = database.getReference("baby");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        if (snapshots.child("amka").getValue().equals(babyAmka)) {
                            GenericTypeIndicator<ArrayList<Vaccination>> t = new GenericTypeIndicator<ArrayList<Vaccination>>() {
                            };
                            vaccines = snapshots.child("vacc").getValue(t);
                        }
                    }
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //getting doctor's info
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = database.getReference("doctor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        String UID = snapshots.getKey();
                        if (UID.equals(currentUser)) {
                            doctorName = "Dr. " + snapshots.child("surname").getValue() + " " + snapshots.child("name").getValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(listener, vaccines, "addVaccination");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        vaccinationRecyclerView.setLayoutManager(layoutManager);
        vaccinationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        vaccinationRecyclerView.setAdapter(adapter);
        adapter.addVaccination(new recyclerAdapter.addVaccination() {
            @Override
            public void addVaccine(int position) {
                vaccines.get(position).setTimesVaccinated(vaccines.get(position).getTimesVaccinated() + 1);
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
                vaccines.get(position).setDate(d + "/"+ m+"/"+yy);
                vaccines.get(position).setDoctorName(doctorName);
            }
        });
    }

}