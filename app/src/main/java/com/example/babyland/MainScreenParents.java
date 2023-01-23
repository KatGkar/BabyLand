package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainScreenParents extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Boolean userFound = false;
    private ArrayList<Baby> listKids;
    private String currentUserUID;
    private RelativeLayout noBabyLayout, mainScreenLayout, statisticLayout, doctorInfoRelativeLayout;
    private ImageButton addBabyButton;
    private Spinner chooseChildSpinner;
    private Button showDevelopmentButton, deleteChildButton, chartButton, settingsButton, viewDoctorInfoButton, addNewBabyButton,
                    viewVaccinesButton;
    private TextView ageTextView, statisticTextView, doctorFullNameTextView, doctorEmailTextView, doctorPhoneNumberTextView,
                    doctorMedicalIDTextView;
    private int monthsD, developments=0;
    private Doctor doctor=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_parents);

        //getting views from xml file
        noBabyLayout = findViewById(R.id.noBabyLayout);
        addBabyButton = findViewById(R.id.addBabyButton);
        mainScreenLayout = findViewById(R.id.mainScreenLayout);
        chooseChildSpinner = findViewById(R.id.chooseBabySpinner);
        statisticLayout = findViewById(R.id.statisticLayout);
        showDevelopmentButton = findViewById(R.id.showDevelopmentButton);
        ageTextView = findViewById(R.id.ageMainScreenTextView);
        statisticTextView = findViewById(R.id.statisticTextView);
        deleteChildButton = findViewById(R.id.deleteChildButton);
        chartButton = findViewById(R.id.chartButton);
        settingsButton = findViewById(R.id.settingsParentButton);
        viewDoctorInfoButton = findViewById(R.id.viewDoctorInfoButton);
        doctorInfoRelativeLayout = findViewById(R.id.doctorInfoRelativeView);
        doctorEmailTextView = findViewById(R.id.doctorEmailTextView);
        doctorFullNameTextView = findViewById(R.id.doctorFullNameTextView);
        doctorPhoneNumberTextView = findViewById(R.id.doctorPhoneNumberTextView);
        doctorMedicalIDTextView = findViewById(R.id.doctorMedicalIIDTextView);
        addNewBabyButton = findViewById(R.id.addNewBabyButton);
        viewVaccinesButton = findViewById(R.id.viewVaccinesButton);

        //setting list
        listKids= new ArrayList<>();

        userFound = false;


        //setting visibilities
        noBabyLayout.setVisibility(View.INVISIBLE);
        mainScreenLayout.setVisibility(View.VISIBLE);
        doctorInfoRelativeLayout.setVisibility(View.INVISIBLE);

        //setting database
        database = FirebaseDatabase.getInstance();

        //getting user UID
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //getting user info from database parent
        reference = database.getReference("parent");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        String UID = snapshots.getKey();
                        if (UID.equals(currentUserUID)) {
                            userFound = true;
                            GenericTypeIndicator<ArrayList<Baby>> t = new GenericTypeIndicator<ArrayList<Baby>>(){};
                            listKids = snapshots.child("kids").getValue(t);
                        }
                    }
                }
               load();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //add child button
        addBabyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddBaby.class);
                startActivity(intent);
            }
        });

        //delete child button
        deleteChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), deleteChildActivity.class);
                startActivity(intent);
            }
        });

        //setting menu button
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsButton(view);
            }
        });


        //on selected child change
        chooseChildSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                findSelectedBabyAge();
                findSelectedBabyStatistics();
                findDoctor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //chart button
        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreenParents.this, viewCharts.class);
                intent.putExtra("babyAmka", chooseChildSpinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        //view doctor info button
        viewDoctorInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDoctor(view);
            }
        });

        //add new baby button
        addNewBabyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddBaby.class);
                startActivity(intent);
            }
        });

        //view vaccines button
        viewVaccinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreenParents.this, viewVaccination.class);
                intent.putExtra("babyAmka", chooseChildSpinner.getSelectedItem().toString());
                intent.putExtra("userType", "parent");
                startActivity(intent);
            }
        });
    }

    //view doctor info
    private void viewDoctor(View view){
        if(doctor!=null) {
            mainScreenLayout.setVisibility(View.INVISIBLE);
            doctorInfoRelativeLayout.setVisibility(View.VISIBLE);
            doctorMedicalIDTextView.setText(doctor.getMedicalID());
            doctorEmailTextView.setText(doctor.getEmail());
            doctorFullNameTextView.setText(doctor.getName() + " " + doctor.getSurname());
            doctorPhoneNumberTextView.setText(doctor.getPhoneNumber());
        }else{
            Toast.makeText(this, "No doctor yet!!", Toast.LENGTH_SHORT).show();
        }
    }

    //find doctor in charge for child
    private void findDoctor(){
        reference = database.getReference("doctor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots:snapshot.getChildren()){
                        GenericTypeIndicator<Doctor> t = new GenericTypeIndicator<Doctor>() {};
                        for(int i=0;i<snapshots.getValue(t).getKids().size();i++){
                            if(snapshots.getValue(t).getKids().get(i).getAmka().equals(chooseChildSpinner.getSelectedItem().toString())){
                                doctor = snapshots.getValue(t);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //on back button pressed
    @Override
    public void onBackPressed() {
        if(doctorInfoRelativeLayout.getVisibility() == View.VISIBLE){
            doctorInfoRelativeLayout.setVisibility(View.INVISIBLE);
            mainScreenLayout.setVisibility(View.VISIBLE);
        }else{
            Intent intent = new Intent(MainScreenParents.this, LoginRegister.class);
            startActivity(intent);

        }
    }

    //on resume
    @Override
    protected void onResume() {
        super.onResume();
        reference = database.getReference("parent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        String UID = snapshots.getKey();
                        if (UID.equals(currentUserUID)) {
                            userFound = true;
                            GenericTypeIndicator<ArrayList<Baby>> t = new GenericTypeIndicator<ArrayList<Baby>>(){};
                            listKids = snapshots.child("kids").getValue(t);
                        }
                    }
                }
                load();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //check if user has children
    private void load(){
        if(listKids != null && !listKids.isEmpty()){
            //show panel with babies
            ArrayAdapter<Baby> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listKids);
            chooseChildSpinner.setAdapter(adapter);
            findSelectedBabyAge();
            findSelectedBabyStatistics();
        }else {
            mainScreenLayout.setVisibility(View.INVISIBLE);
            noBabyLayout.setVisibility(View.VISIBLE);
        }
    }

    //find children statistics
    private void findSelectedBabyStatistics(){
        String babyAmka = chooseChildSpinner.getSelectedItem().toString();
        reference =database.getReference("monitoringDevelopment");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    developments=0;
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        GenericTypeIndicator<Development> t = new GenericTypeIndicator<Development>() {};
                        if(snapshots.getValue(t).getAmka().equals(babyAmka)){
                            developments++;
                        }
                    }
                }
                statisticTextView.setText(String.valueOf(developments));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //calculate child age
    private void findSelectedBabyAge(){
        String babyAmka = chooseChildSpinner.getSelectedItem().toString();
        String date = " ";
        for (int i = 0; i < listKids.size(); i++) {
            if (listKids.get(i).getAmka() == babyAmka) {
                date = listKids.get(i).getDateOfBirth();
            }
        }
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);
        int i = 0;
        String day = "", month = "", year = "";
        for (Character c : date.toCharArray()) {
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
        String age;
        String ageType;
        if (monthsD == 0) {
            ageType = "weeks";
            age = "1-2";
        } else {
            ageType = "months";
            age = String.valueOf(monthsD);
        }
        ageTextView.setText(age +" " + ageType);
        noBabyLayout.setVisibility(View.INVISIBLE);
        mainScreenLayout.setVisibility(View.VISIBLE);

    }


    //go to show development page
    public void showDevelopment(View view){
        Intent intent = new Intent(this, showDevelopmentsList.class);
        intent.putExtra("babyAmka", chooseChildSpinner.getSelectedItem().toString());
        startActivity(intent);
    }

    //go to settings page
    private void settingsButton(View view){
        Intent intent = new Intent(MainScreenParents.this, UserAccount.class);
        intent.putExtra("user", "parent");
        startActivity(intent);
    }
}