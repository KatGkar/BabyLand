package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class showChildren extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String currentUser;
    private ArrayList<Baby> doctorsChildren;
    private RelativeLayout doctorsChildrenLayout, showChildRelativeLayout;
    private RecyclerView doctorsChildrenRecyclerView;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    private Button addDevelopmentsButton, viewParentsButton, showHistoricButton;
    private TextView nameTextView, amkaTextView, dateOfBirthTextView;
    private ImageView sexImageView;
    private int monthsD=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_children);

        doctorsChildrenLayout = findViewById(R.id.doctorsChildrenLayout);
        doctorsChildrenRecyclerView = findViewById(R.id.doctorsChildrenRecyclerView);
        showChildRelativeLayout = findViewById(R.id.showChildRelativeLayout);
        addDevelopmentsButton = findViewById(R.id.addDevelopmentsButton);
        viewParentsButton = findViewById(R.id.viewParentsButton);
        showHistoricButton = findViewById(R.id.showHistoricButton);
        amkaTextView = findViewById(R.id.amkaShowChildTextView);
        nameTextView = findViewById(R.id.nameShowCihildTextViw);
        dateOfBirthTextView = findViewById(R.id.dateOfBirthShowChildTextView);
        sexImageView = findViewById(R.id.sexShowChildImageView);

        //setting database
        database = FirebaseDatabase.getInstance();

        showChildRelativeLayout.setVisibility(View.INVISIBLE);
        doctorsChildrenLayout.setVisibility(View.VISIBLE);

        doctorsChildren = new ArrayList<>();

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //getting user info from database doctor
        reference = database.getReference("doctor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        String UID = snapshots.getKey();
                        if (UID.equals(currentUser)) {
                            GenericTypeIndicator<ArrayList<Baby>> t = new GenericTypeIndicator<ArrayList<Baby>>(){};
                            doctorsChildren = snapshots.child("kids").getValue(t);
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

    private void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(listener, doctorsChildren, "availableChildren");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        doctorsChildrenRecyclerView.setLayoutManager(layoutManager);
        doctorsChildrenRecyclerView.setItemAnimator(new DefaultItemAnimator());
        doctorsChildrenRecyclerView.setAdapter(adapter);
    }


    private void setOnClickListener() {
        listener = new recyclerAdapter.recyclerVewOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                //show Details
                doctorsChildrenLayout.setVisibility(View.INVISIBLE);
                showChildRelativeLayout.setVisibility(View.VISIBLE);
                nameTextView.setText(doctorsChildren.get(position).getName());
                amkaTextView.setText(doctorsChildren.get(position).getAmka());
                dateOfBirthTextView.setText(doctorsChildren.get(position).getDateOfBirth());
                String sex= doctorsChildren.get(position).getSex();
                if(sex.equals("BOY")){
                    Picasso.get().load(R.drawable.boy).into(sexImageView);
                }else{
                    Picasso.get().load(R.drawable.baby_girl).into(sexImageView);
                }
                addDevelopmentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addDevelopments(doctorsChildren.get(position).getAmka(), doctorsChildren.get(position).getDateOfBirth());
                    }
                });
                viewParentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewParents(doctorsChildren.get(position).getAmka());
                    }
                });
                showHistoricButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showHistoric(doctorsChildren.get(position).getAmka());
                    }
                });
            }
        };
    }

    @Override
    public void onBackPressed() {
        if(showChildRelativeLayout.getVisibility() == View.VISIBLE){
            showChildRelativeLayout.setVisibility(View.INVISIBLE);
            doctorsChildrenLayout.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }

    }

    private void addDevelopments(String babyAmka, String babyBirthDate){
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);
        int i = 0;
        String day = "", month = "", year = "";
        for (Character c : babyBirthDate.toCharArray()) {
            if (i < 2) {
                day = day + c;
            } else if (i > 2 && i < 5) {
                month = month + c;
            } else if (i > 5 && i < 11) {
                year = year + c;
            }
            i++;
        }
        int yearOfBirth = Integer.parseInt(year);
        int monthOfBirth = Integer.parseInt(month);
        int dayOfBirth = Integer.parseInt(day);
        if (yearOfBirth == yearNow && monthOfBirth == monthNow) {
            if (dayNow - dayOfBirth <= 14) {
                monthsD = 0;
            } else {
                monthsD = 1;
            }
        } else if (yearOfBirth == yearNow) {
            monthsD = monthNow - monthOfBirth;
        } else {
            int m = 12 - monthOfBirth;
            int m1 = yearNow - (yearOfBirth + 1);
            m1 = m1 * 12;
            monthsD = m + m1 + monthNow;
        }
        String age="";
        String ageType="";
        if (monthsD == 0) {
            ageType = "weeks";
            age = "1-2";
        } else if (monthsD <= 2) {
            ageType = "months";
            age = "2";
        } else if (monthsD == 3 || monthsD == 4) {
            ageType = "months";
            age = "4";
        } else if (monthsD == 5 || monthsD == 6) {
            ageType = "months";
            age = "6";
        } else if (monthsD >= 7 && monthsD <= 9) {
            ageType = "months";
            age = "9";
        } else if (monthsD >= 10 && monthsD <= 15) {
            ageType = "months";
            age = "12";
        }
        Intent intent = new Intent(showChildren.this, MonitoringDevelopment.class);
        intent.putExtra("babyAmka", babyAmka);
        intent.putExtra("ageType", ageType);
        intent.putExtra("age", age);
        startActivity(intent);
    }


    private void viewParents(String babyAmka){
        Intent intent = new Intent(showChildren.this, viewParentInfo.class);
        intent.putExtra("babyAmka", babyAmka);
        startActivity(intent);
    }

    private void showHistoric(String babyAmka){
        Intent intent = new Intent(showChildren.this, showHistoric.class);
        intent.putExtra("babyAmka", babyAmka);
        startActivity(intent);
    }











}