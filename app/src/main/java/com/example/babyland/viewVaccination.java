package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class viewVaccination extends AppCompatActivity {

    private ArrayList<Vaccination> vaccines;
    private RecyclerView vaccinationRecyclerView;
    private String babyAmka, currentUserUID, doctorName, m, d, userType,amkaParent1, amkaParent2;
    private FirebaseDatabase database;
    private BottomNavigationView bottomNavigationView;
    private DatabaseReference reference, reference2, reference3, reference4;
    private Button addOtherVaccineButton, saveVaccineButton;
    private RecyclerAdapter.recyclerVewOnClickListener listener;
    private RelativeLayout addOtherVaccineRelativeLayout, vaccinesRelativeLayout;
    private TextView doctorNameTextView, dateVaccinatedTextView;
    private TextInputEditText vaccineNameEditText;
    private int mm, yy, dd, vaccineNumber;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vaccination);

        //getting extras
        Bundle extras = getIntent().getExtras();
        babyAmka = extras.getString("babyAmka");
        userType = extras.getString("userType");

        //getting views from xml
        bottomNavigationView = findViewById(R.id.bottomNavigationViewViewVaccination);
        vaccinationRecyclerView = findViewById(R.id.vaccinationRecyclerView);
        addOtherVaccineButton = findViewById(R.id.addOtherVaccineButton);
        saveVaccineButton = findViewById(R.id.saveVaccineButton);
        addOtherVaccineRelativeLayout = findViewById(R.id.addOtherVaccineRelativeLayout);
        doctorNameTextView = findViewById(R.id.doctorNameTextView);
        dateVaccinatedTextView = findViewById(R.id.dateVaccinatedTextView);
        vaccineNameEditText = findViewById(R.id.vaccineNameTextInput);
        vaccinesRelativeLayout = findViewById(R.id.vaccRelativeLayout);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        //setting database
        database = FirebaseDatabase.getInstance();

        //setting list
        vaccines = new ArrayList<>();

        //setting visibilities
        vaccinesRelativeLayout.setVisibility(View.VISIBLE);
        addOtherVaccineRelativeLayout.setVisibility(View.INVISIBLE);
        if (userType.equals("parent")) {
            addOtherVaccineButton.setVisibility(View.INVISIBLE);
        } else {
            addOtherVaccineButton.setVisibility(View.VISIBLE);
        }

        //getting user UID
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //find vaccination list of baby
        reference = database.getReference("baby");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        if (snapshots.child("amka").getValue().equals(babyAmka)) {
                            GenericTypeIndicator<ArrayList<Vaccination>> t = new GenericTypeIndicator<ArrayList<Vaccination>>() {
                            };
                            vaccines = snapshots.child("vaccinations").getValue(t);
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
        reference = database.getReference("doctor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        String UID = snapshots.getKey();
                        if (UID.equals(currentUserUID)) {
                            doctorName = "Dr. " + snapshots.child("surname").getValue() + " " + snapshots.child("name").getValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //calculate today's date
        Calendar cal = Calendar.getInstance();
        yy = cal.get(Calendar.YEAR);
        mm = cal.get(Calendar.MONTH);
        dd = cal.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        d = String.valueOf(dd);
        if (dd <= 9) {
            d = "0" + d;
        }
        if (mm < 12) {
            mm++;
        }
        m = String.valueOf(mm);
        if (mm <= 9) {
            m = "0" + m;
        }

        addOtherVaccineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOtherVaccine(view);
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
                            Intent intent = new Intent(viewVaccination.this, addChildToDoctor.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(viewVaccination.this, addBaby.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.navigation_account:
                        if(userType.equals("doctor")){
                            Intent intent1 = new Intent(viewVaccination.this, userAccount.class);
                            intent1.putExtra("user", "doctor");
                            startActivity(intent1);
                        }else{
                            Intent intent1 = new Intent(viewVaccination.this, userAccount.class);
                            intent1.putExtra("user", "parent");
                            startActivity(intent1);

                        }
                        return true;
                }
                return false;
            }
        });
    }

    //on page resume
    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }


    //add other vaccine button
    private void addOtherVaccine(View view){
        vaccinesRelativeLayout.setVisibility(View.INVISIBLE);
        addOtherVaccineButton.setVisibility(View.INVISIBLE);
        addOtherVaccineRelativeLayout.setVisibility(View.VISIBLE);
        doctorNameTextView.setText(doctorName);
        dateVaccinatedTextView.setText(d + "/"+ m+"/"+yy);
        saveVaccineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(vaccineNameEditText.getText())){
                    Toast.makeText(viewVaccination.this, "Vaccine name is empty!!", Toast.LENGTH_SHORT).show();
                }else{
                    //get unique vaccine number from database
                    reference2 = database.getReference("baby").child(babyAmka).child("vaccinations");
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            vaccineNumber = (int) snapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(viewVaccination.this, "Vaccination saved successfully!", Toast.LENGTH_SHORT).show();
                    Vaccination vac = new Vaccination(vaccineNameEditText.getText().toString(), d + "/"+ m+"/"+yy, doctorName, vaccineNumber+1);
                    reference2.child(String.valueOf(vaccineNumber+1)).setValue(vac);
                    vaccines.add(vac);
                    addOtherVaccineButton.setVisibility(View.VISIBLE);
                    addOtherVaccineRelativeLayout.setVisibility(View.INVISIBLE);
                    vaccinesRelativeLayout.setVisibility(View.VISIBLE);
                    setAdapter();
                }
            }
        });
    }

    //setting adapter for recyclerView
    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(listener, vaccines, "viewVaccination", userType);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        vaccinationRecyclerView.setLayoutManager(layoutManager);
        vaccinationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        vaccinationRecyclerView.setAdapter(adapter);
        adapter.addVaccination(new RecyclerAdapter.addVaccination() {
            @Override
            public void addVaccine(int position) {
                vaccines.get(position).setDate(d + "/"+ m+"/"+yy);
                vaccines.get(position).setDoctorName(doctorName);
                updateDatabase(vaccines.get(position).getUniqueID());
                setAdapter();
            }
        });
    }


    private void updateDatabase(int id){
        //updating child's database
        reference2 = FirebaseDatabase.getInstance().getReference("baby").child(babyAmka).child("vaccinations");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots:snapshot.getChildren()){
                        GenericTypeIndicator<Vaccination> t = new GenericTypeIndicator<Vaccination>() {};
                        int uniqueID = snapshots.getValue(t).getUniqueID();
                        if(uniqueID == id){
                            reference2.child(snapshots.getKey()).child("doctorName").setValue(doctorName);
                            reference2.child(snapshots.getKey()).child("date").setValue(d + "/"+ m+"/"+yy);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //updating doctor's database
        reference3 = FirebaseDatabase.getInstance().getReference("doctor").child(currentUserUID).child("kids");
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots:snapshot.getChildren()) {
                        GenericTypeIndicator<ArrayList<Baby>> t = new GenericTypeIndicator<ArrayList<Baby>>() {};
                        ArrayList<Baby> listBaby = snapshot.getValue(t);
                        for (int i = 0; i < listBaby.size(); i++) {
                            //find kid
                            if (listBaby.get(i).getAmka().equals(babyAmka)) {
                                ArrayList<Vaccination> vacc = listBaby.get(i).getVaccinations();
                                for (int j = 0; j < vacc.size(); j++) {
                                    if (vacc.get(j).getUniqueID() == id) {
                                        vacc.get(j).setDoctorName(doctorName);
                                        vacc.get(j).setDate(d + "/" + m + "/" + yy);
                                        listBaby.get(i).setVaccinations(vacc);
                                        reference3.setValue(listBaby);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //find parent amka
        reference = FirebaseDatabase.getInstance().getReference("baby").child(babyAmka);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    amkaParent1 = (String) snapshot.child("parentOneAmka").getValue();
                    amkaParent2 = (String) snapshot.child("parentTwoAmka").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //updating parent's database
        reference4 = FirebaseDatabase.getInstance().getReference("parent");
        reference4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots:snapshot.getChildren()){
                        GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {};
                        //parent one
                        if(snapshots.getValue(t).getAmka().equals(amkaParent1)){
                            ArrayList<Baby> listKids = snapshots.getValue(t).getKids();
                            //search baby
                            for(int k=0;k<listKids.size();k++){
                                if(listKids.get(k).getAmka().equals(babyAmka)){
                                    ArrayList<Vaccination> vaccinationArrayList = listKids.get(k).getVaccinations();
                                    //search vaccine
                                    for(int h=0;h<vaccinationArrayList.size();h++){
                                        if(vaccinationArrayList.get(h).getUniqueID() == id){
                                            vaccinationArrayList.get(h).setDoctorName(doctorName);
                                            vaccinationArrayList.get(h).setDate(d + "/"+ m+"/"+yy);
                                            listKids.get(k).setVaccinations(vaccinationArrayList);
                                            reference4.child(snapshots.getKey()).child("kids").setValue(listKids);
                                        }
                                    }
                                }
                            }
                        }
                        //parent two
                        if(snapshots.getValue(t).getAmka().equals(amkaParent2)){
                            ArrayList<Baby> listKids2 = snapshots.getValue(t).getKids();
                            //search baby
                            for(int w=0;w<listKids2.size();w++){
                                if(listKids2.get(w).getAmka().equals(babyAmka)){
                                    ArrayList<Vaccination> vaccinations = listKids2.get(w).getVaccinations();
                                    //search vaccine
                                    for(int g=0;g<=vaccinations.size();g++){
                                        if(vaccinations.get(g).getUniqueID() == id){
                                            vaccinations.get(g).setDoctorName(doctorName);
                                            vaccinations.get(g).setDate(d + "/"+ m+"/"+yy);
                                            listKids2.get(w).setVaccinations(vaccinations);
                                            reference4.child(snapshots.getKey()).child("kids").setValue(listKids2);
                                        }
                                    }
                                }
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
        if(addOtherVaccineRelativeLayout.getVisibility() == View.VISIBLE){
            addOtherVaccineRelativeLayout.setVisibility(View.INVISIBLE);
            addOtherVaccineButton.setVisibility(View.VISIBLE);
            vaccinesRelativeLayout.setVisibility(View.VISIBLE);
        }else{
           if(userType.equals("parent")){
               Intent intent = new Intent(viewVaccination.this, mainScreenParents.class);
               startActivity(intent);
           }else {
               Intent intent = new Intent(viewVaccination.this, mainScreenDoctor.class);
               startActivity(intent);
           }
        }
    }
}