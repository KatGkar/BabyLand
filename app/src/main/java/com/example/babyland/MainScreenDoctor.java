package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

public class MainScreenDoctor extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String currentUserUID;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<Baby> doctorsChildren;
    private RelativeLayout doctorsChildrenLayout, showChildRelativeLayout, deleteChildVerificationRelativeLayout, noChildRelativeLayout;
    private RecyclerView doctorsChildrenRecyclerView;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    private Button addDevelopmentsButton, viewParentsButton, showDevelopmentsButton, viewFamilyHistoricButton, vaccinationsButton,
            deleteChildButton, deleteChildVerificationButton, noChildButton, viewChartButton;
    private TextView nameTextView, amkaTextView, dateOfBirthTextView;
    private EditText deleteChildVerificationEditText;
    private ImageView sexImageView;
    private int monthsD=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_doctor);

        //getting views from xml file
        bottomNavigationView = findViewById(R.id.bottomNavigationViewAddBaby);
        doctorsChildrenLayout = findViewById(R.id.doctorsChildrenLayout);
        doctorsChildrenRecyclerView = findViewById(R.id.doctorsChildrenRecyclerView);
        showChildRelativeLayout = findViewById(R.id.showChildRelativeLayout);
        addDevelopmentsButton = findViewById(R.id.addDevelopmentsButton);
        viewParentsButton = findViewById(R.id.viewParentsButton);
        showDevelopmentsButton = findViewById(R.id.showDevelopmentsButton);
        amkaTextView = findViewById(R.id.amkaShowChildTextView);
        nameTextView = findViewById(R.id.nameShowCihildTextViw);
        dateOfBirthTextView = findViewById(R.id.dateOfBirthShowChildTextView);
        sexImageView = findViewById(R.id.sexShowChildImageView);
        viewFamilyHistoricButton = findViewById(R.id.viewFamilyHistoricButton);
        vaccinationsButton = findViewById(R.id.vaccinationButton);
        deleteChildButton = findViewById(R.id.deleteBabyButton);
        deleteChildVerificationButton = findViewById(R.id.deleteChildVerificationButton);
        deleteChildVerificationEditText = findViewById(R.id.deleteChildVerificationEditText);
        deleteChildVerificationRelativeLayout = findViewById(R.id.deleteChildRelativeLayout);
        noChildButton = findViewById(R.id.noChildButton);
        noChildRelativeLayout = findViewById(R.id.noChildRelativeLayout);
        viewChartButton = findViewById(R.id.viewChartButton);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        //setting database
        database = FirebaseDatabase.getInstance();

        //setting visibilities
        showChildRelativeLayout.setVisibility(View.INVISIBLE);
        doctorsChildrenLayout.setVisibility(View.VISIBLE);
        deleteChildVerificationRelativeLayout.setVisibility(View.INVISIBLE);
        noChildRelativeLayout.setVisibility(View.INVISIBLE);

        //getting user UID
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //setting list
        doctorsChildren = new ArrayList<>();

        //getting user children from database
        getChildren();

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
                        Intent intent = new Intent(MainScreenDoctor.this, AddChildToDoctor.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_account:
                        Intent intent1 = new Intent(MainScreenDoctor.this, UserAccount.class);
                        intent1.putExtra("user", "doctor");
                        startActivity(intent1);
                        return true;
                }
                return false;
            }
        });

        //add child
        noChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreenDoctor.this, AddChildToDoctor.class);
                startActivity(intent);
            }
        });

    }


    //on page resume
    @Override
    protected void onResume() {
        showChildRelativeLayout.setVisibility(View.INVISIBLE);
        doctorsChildrenLayout.setVisibility(View.VISIBLE);
        deleteChildVerificationRelativeLayout.setVisibility(View.INVISIBLE);
        noChildRelativeLayout.setVisibility(View.INVISIBLE);
        super.onResume();
        getChildren();
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    //on back button pressed
    @Override
    public void onBackPressed() {
        if(showChildRelativeLayout.getVisibility() == View.VISIBLE){
            showChildRelativeLayout.setVisibility(View.INVISIBLE);
            doctorsChildrenLayout.setVisibility(View.VISIBLE);
        }else if(deleteChildVerificationRelativeLayout.getVisibility() == View.VISIBLE){
            deleteChildVerificationRelativeLayout.setVisibility(View.INVISIBLE);
            showChildRelativeLayout.setVisibility(View.VISIBLE);
        }else{
            Intent intent = new Intent(MainScreenDoctor.this, LoginRegister.class);
            startActivity(intent);
        }

    }

    //getting user children from database
    private void getChildren(){
        reference = database.getReference("doctor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        String UID = snapshots.getKey();
                        if (UID.equals(currentUserUID)) {
                            GenericTypeIndicator<ArrayList<Baby>> t = new GenericTypeIndicator<ArrayList<Baby>>(){};
                            doctorsChildren = snapshots.child("kids").getValue(t);
                        }
                    }
                    try{
                        noChildRelativeLayout.setVisibility(View.INVISIBLE);
                        doctorsChildrenLayout.setVisibility(View.VISIBLE);
                        doctorsChildren.size();
                    }catch (Exception e){
                        doctorsChildren = new ArrayList<>();
                        doctorsChildrenLayout.setVisibility(View.INVISIBLE);
                        noChildRelativeLayout.setVisibility(View.VISIBLE);
                    }
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //setting adapter for recyclerView
    private void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(listener, doctorsChildren, "availableChildren","none");
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
                nameTextView.setText("Name: " + doctorsChildren.get(position).getName());
                amkaTextView.setText("Amka: " + doctorsChildren.get(position).getAmka());
                dateOfBirthTextView.setText("Birth date: " +doctorsChildren.get(position).getDateOfBirth());
                String sex= doctorsChildren.get(position).getSex();
                if(sex.equals("BOY")){
                    Picasso.get().load(R.drawable._02_baby_boy_1).into(sexImageView);
                }else{
                    Picasso.get().load(R.drawable.baby_girl1).into(sexImageView);
                }
                addDevelopmentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addDevelopments(doctorsChildren.get(position).getAmka(), doctorsChildren.get(position).getDateOfBirth());
                    }
                });
                viewChartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewChart(doctorsChildren.get(position).getAmka());
                    }
                });
                viewParentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewParents(doctorsChildren.get(position).getAmka());
                    }
                });
                showDevelopmentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDevelopments(doctorsChildren.get(position).getAmka());
                    }
                });
                viewFamilyHistoricButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewFamilyHistoric(doctorsChildren.get(position).getAmka());
                    }
                });

                vaccinationsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vaccinations(doctorsChildren.get(position).getAmka());
                    }
                });

                deleteChildButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteChild(position);
                    }
                });
            }
        };
    }

    //add development button
    private void addDevelopments(String babyAmka, String babyBirthDate){
        //calculate child age in month
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
        Intent intent = new Intent(MainScreenDoctor.this, MonitoringDevelopment.class);
        intent.putExtra("babyAmka", babyAmka);
        intent.putExtra("ageType", ageType);
        intent.putExtra("age", age);
        startActivity(intent);
    }

    //view parents button
    private void viewParents(String babyAmka){
        Intent intent = new Intent(MainScreenDoctor.this, viewParentInfo.class);
        intent.putExtra("babyAmka", babyAmka);
        startActivity(intent);
    }

    //show developments button
    private void showDevelopments(String babyAmka){
        Intent intent = new Intent(MainScreenDoctor.this, showDevelopmentsList.class);
        intent.putExtra("babyAmka", babyAmka);
        startActivity(intent);
    }

    //view family historic button
    private void viewFamilyHistoric(String babyAmka){
        Intent intent = new Intent(MainScreenDoctor.this, showHistoric.class);
        intent.putExtra("babyAmka", babyAmka);
        startActivity(intent);
    }

    //vaccinations button
    private void vaccinations(String babyAmka){
        Intent intent = new Intent(MainScreenDoctor.this, viewVaccination.class);
        intent.putExtra("babyAmka", babyAmka);
        intent.putExtra("userType", "doctor");
        startActivity(intent);
    }

    //delete child button
    private void deleteChild(int position){
        showChildRelativeLayout.setVisibility(View.INVISIBLE);
        deleteChildVerificationRelativeLayout.setVisibility(View.VISIBLE);
        deleteChildVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amka = deleteChildVerificationEditText.getText().toString();
                if(amka.equals(doctorsChildren.get(position).getAmka())){
                    doctorsChildren.remove(position);
                    reference = database.getReference("doctor");
                    reference.child(currentUserUID).child("kids").setValue(doctorsChildren);
                    Toast.makeText(MainScreenDoctor.this, "Child deleted successfully!", Toast.LENGTH_SHORT).show();
                    getChildren();
                    deleteChildVerificationRelativeLayout.setVisibility(View.INVISIBLE);
                    doctorsChildrenLayout.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(MainScreenDoctor.this, "Amka number is not correct. Please try again!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //view chart button
    private void viewChart(String babyAmka){
        Intent intent = new Intent(MainScreenDoctor.this, viewCharts.class);
        intent.putExtra("babyAmka", babyAmka);
        intent.putExtra("userType", "doctor");
        startActivity(intent);
    }




}