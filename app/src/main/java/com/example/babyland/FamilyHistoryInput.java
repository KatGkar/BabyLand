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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FamilyHistoryInput extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected ArrayList<FamilyHistoryIllnesses> illnesses;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    Button nextButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    String name, sex, birthDate, amka, birthPlace, bloodType, parentOneAmka, parentTwoAmka;
    int babyNumber;
    String currentUser;
    private User user;
    private Baby b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_history_input);

        //getting views
        recyclerView = findViewById(R.id.recyclerViewFamilyHistory);
        nextButton = findViewById(R.id.nextButton);

        //setting list
        illnesses = new ArrayList<>();

        //getting extras
        Bundle extras = getIntent().getExtras();
        sex = extras.getString("sex");
        name = extras.getString("name");
        birthDate = extras.getString("birthDate");
        amka = extras.getString("amka");
        birthPlace = extras.getString("birthPlace");
        bloodType = extras.getString("bloodType");

        //setting database
        database = FirebaseDatabase.getInstance();

        //getting babies on database
        babyNumber = getBabyNumber();

        //getting parents amka
        getParentAmka();

        //setting list
        illnesses = new ArrayList<>();
        illnesses.clear();
        getIllnesses();

    }

    public void showMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

    //adapter to load services into the recyclerView
    private void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(listener, illnesses, "illnessInput");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    private void setOnClickListener() {
    }


    //next button
    public void Click(View view) {
        babyNumber = getBabyNumber();
        b = new Baby(name, birthDate, amka, birthPlace, bloodType, sex, parentOneAmka, parentTwoAmka, illnesses);
        reference = database.getReference("baby");
        reference.child(amka).setValue(b);
        updateParent();
    }


    private void updateParent(){
        reference = database.getReference("parent").child(currentUser);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    if (snapshot.getKey().equals(currentUser)) {
                        ArrayList<Baby> list;
                        try {
                            list = (ArrayList<Baby>) snapshot.child("kids").getValue();
                            list.add(b);
                        } catch (Exception e) {
                            list = new ArrayList<>();
                            list.add(b);
                        }
                        database.getReference("parent").child(currentUser).child("kids").setValue(list);
                        showMessage("Success", "Child added successfully!!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
        startActivity(intent);
    }


    //getting number of babies on database
    private int getBabyNumber() {
        reference = database.getReference("baby");
        reference.addValueEventListener(new ValueEventListener() {
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

    //getting parents amka
    private void getParentAmka() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //getting user amka from database
        reference = database.getReference("parent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        String UID = snapshots.getKey();
                        user = snapshots.getValue(User.class);
                        if (UID.equals(currentUser)) {
                            parentOneAmka = String.valueOf(snapshots.child("amka").getValue());
                            if (Boolean.valueOf(String.valueOf(snapshots.child("partner")))) {
                                parentTwoAmka = String.valueOf(snapshots.child("partnerAmka"));
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

    //loading data in adapter from database
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
                //calls adapter to load data into recyclerView
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}