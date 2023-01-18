package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FamilyHistoryInput extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected ArrayList<FamilyHistoryIllnesses> illnesses;
    private ArrayList<Vaccination> vaccines;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    private Button saveButton;
    private FirebaseDatabase database;
    private DatabaseReference reference1, reference2;
    private String name, sex, amka, birthPlace, bloodType, parentOneAmka, parentTwoAmka, birthDate, currentUserUID;
    private int babyNumber;
    private Parent user;
    private Baby b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_history_input);

        //getting extras
        Bundle extras = getIntent().getExtras();
        sex = extras.getString("sex");
        name = extras.getString("name");
        birthDate = extras.getString("birthDate");
        amka = extras.getString("amka");
        birthPlace = extras.getString("birthPlace");
        bloodType = extras.getString("bloodType");

        //getting views from xml file
        recyclerView = findViewById(R.id.recyclerViewFamilyHistory);
        saveButton = findViewById(R.id.nextButton);

        //setting lists
        illnesses = new ArrayList<>();
        vaccines = new ArrayList<>();
        illnesses.clear();
        vaccines.clear();

        //setting database
        database = FirebaseDatabase.getInstance();

        //getting babies on database
        babyNumber = getBabyNumber();

        //getting parents amka
        getParentAmka();

        //getting illness from database
        getIllnesses();

        //getting vaccines from database
        getVaccines();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBaby(view);
            }
        });
    }


    //setting adapter for recyclerView
    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(listener, illnesses, "illnessInput", "none");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    //getting vaccines from database
    private void getVaccines(){
        reference2 = database.getReference("vaccinations");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots:snapshot.getChildren()){
                        GenericTypeIndicator<Vaccination> t = new GenericTypeIndicator<Vaccination>() {};
                        vaccines.add(snapshots.getValue(t));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //save baby to database
    public void saveBaby(View view) {
        babyNumber = getBabyNumber();
        b = new Baby(name, birthDate, amka, birthPlace, bloodType, sex, parentOneAmka, parentTwoAmka, illnesses, vaccines);
        reference1 = database.getReference("baby");
        reference1.child(amka).setValue(b);
        updateParent();
    }

    //update parents on database
    private void updateParent(){
        reference1 = database.getReference("parent").child(currentUserUID);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {};
                    if (snapshot.getKey().equals(currentUserUID)) {
                        ArrayList<Baby> list;
                        try {
                            list = (ArrayList<Baby>) snapshot.child("kids").getValue();
                            list.add(b);
                        } catch (Exception e) {
                            list = new ArrayList<>();
                            list.add(b);
                        }
                        database.getReference("parent").child(currentUserUID).child("kids").setValue(list);
                        if(snapshot.getValue(t).getPartner()){
                            //if there is parent 2
                            String amka1 = snapshot.getValue(t).getPartnersAmka();
                            reference2 = database.getReference("parent");
                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot!=null){
                                        for (DataSnapshot snapshots : snapshot.getChildren()){
                                            GenericTypeIndicator<Parent> g = new GenericTypeIndicator<Parent>() {};
                                            if(snapshots.getValue(g).getAmka().equals(amka1)){
                                                ArrayList<Baby> partList;
                                                try {
                                                    partList = (ArrayList<Baby>) snapshots.child("kids").getValue();
                                                    partList.add(b);
                                                } catch (Exception e) {
                                                    partList = new ArrayList<>();
                                                    partList.add(b);
                                                }
                                                database.getReference("parent").child(snapshots.getKey()).child("kids").setValue(partList);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        Toast.makeText(FamilyHistoryInput.this, "Child added successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainScreenParents.class);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    //getting number of babies on database
    private int getBabyNumber() {
        reference1 = database.getReference("baby");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                babyNumber = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return babyNumber + 1;
    }

    //getting parents amka from database
    private void getParentAmka() {
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //getting user amka from database
        reference1 = database.getReference("parent");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {};
                        user = snapshots.getValue(t);
                        if (snapshots.getKey().equals(currentUserUID)) {
                            parentOneAmka = String.valueOf(snapshots.child("amka").getValue());
                            if (snapshots.getValue(t).getPartner()) {
                                parentTwoAmka = snapshots.getValue(t).getPartnersAmka();
                            } else {
                                parentTwoAmka = "00000000000";
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

    //getting illnesses from database
    private void getIllnesses() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("illnesses");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot sn : snapshot.getChildren()) {
                        FamilyHistoryIllnesses ill = sn.getValue(FamilyHistoryIllnesses.class);
                        illnesses.add(ill);
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}