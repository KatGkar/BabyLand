package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class AddChildToDoctor extends AppCompatActivity {
    private EditText searchAmkaEditText, verificationEditText;
    private TextView nameAddChildTextView, amkaAddChildTextView, ageAddChildTextView, verificationTextView;
    private RecyclerView availableChildrenRecyclerView;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<Baby> availableChildren, doctorsChildren = null;
    private ImageView sexAddChildImageView, searchButton;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    private RelativeLayout viewChildInfoLayout, availableChildrenLayout, verificationRelativeLayout;
    private Button addChildButton, verificationButton;
    private String currentUserUID;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_to_doctor);

        //getting views from xml
        searchAmkaEditText = findViewById(R.id.searchAmkaEditText);
        availableChildrenRecyclerView = findViewById(R.id.availableChildrenRecyclerView);
        viewChildInfoLayout = findViewById(R.id.viewChildInfoLayout);
        availableChildrenLayout = findViewById(R.id.availableChildrenLayout);
        nameAddChildTextView = findViewById(R.id.nameAddChildTextView);
        amkaAddChildTextView = findViewById(R.id.amkaAddChildTextView);
        ageAddChildTextView = findViewById(R.id.ageAddChildTextView);
        addChildButton = findViewById(R.id.addChildButton);
        sexAddChildImageView = findViewById(R.id.sexAddChildImage);
        searchButton = findViewById(R.id.searchButton);
        verificationButton = findViewById(R.id.verificationButton);
        verificationEditText = findViewById(R.id.verificationEditTextView);
        verificationRelativeLayout = findViewById(R.id.verificationRelativeLayout);
        verificationTextView = findViewById(R.id.verificationTextView);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewAddChildToDoctor);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_add);
        verificationTextView.setText("Please type one parent amka to verify the addition");

        //setting visibilities
        viewChildInfoLayout.setVisibility(View.INVISIBLE);
        availableChildrenLayout.setVisibility(View.VISIBLE);
        verificationRelativeLayout.setVisibility(View.INVISIBLE);

        //setting database
        database = FirebaseDatabase.getInstance();


        //setting arrays
        availableChildren = new ArrayList<>();
        doctorsChildren = new ArrayList<>();

        //getting current user
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //getting children on doctor list from database
        getDoctorsChildren();

        //getting available children from database
        getAvailableChildren();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchButton(view);
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
                        Intent intent = new Intent(AddChildToDoctor.this, MainScreenDoctor.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_add:
                        return true;
                    case R.id.navigation_account:
                        settingsButton();
                        return true;
                }
                return false;
            }
        });

        verificationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString();
                if (!text.matches("^[0-9]+$")) {
                    verificationEditText.setError("Only numbers please!!");
                }
            }
        });

        searchAmkaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString();
                if (!text.matches("^[0-9]+$")) {
                    verificationEditText.setError("Only numbers please!!");
                }
            }
        });

    }


    //on resume page
    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_add);
        super.onResume();
    }

    //go to setting menu
    private void settingsButton(){
        Intent intent = new Intent(AddChildToDoctor.this, UserAccount.class);
        intent.putExtra("user", "doctor");
        startActivity(intent);
    }


    //setting adapter for recyclerView
    private void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(listener, availableChildren, "availableChildren","none");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        availableChildrenRecyclerView.setLayoutManager(layoutManager);
        availableChildrenRecyclerView.setItemAnimator(new DefaultItemAnimator());
        availableChildrenRecyclerView.setAdapter(adapter);
    }

    //click listener to show developments details
    private void setOnClickListener() {
        listener = new recyclerAdapter.recyclerVewOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                //show Details
                availableChildrenLayout.setVisibility(View.INVISIBLE);
                viewChildInfoLayout.setVisibility(View.VISIBLE);
                Baby baby = availableChildren.get(position);
                nameAddChildTextView.setText("Name: " +baby.getName());
                amkaAddChildTextView.setText("Amka: " +baby.getAmka());
                if(baby.getSex().equals("BOY")){
                    Picasso.get().load(R.drawable._02_baby_boy_1).into(sexAddChildImageView);
                }else{
                    Picasso.get().load(R.drawable.baby_girl1).into(sexAddChildImageView);
                }
                int monthsD;
                Calendar cal = Calendar.getInstance();
                int yearNow = cal.get(Calendar.YEAR);
                int monthNow = cal.get(Calendar.MONTH) + 1;
                int dayNow = cal.get(Calendar.DAY_OF_MONTH);
                int i = 0;
                String day = "", month = "", year = "";
                for (Character c : baby.getDateOfBirth().toCharArray()) {
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
                ageAddChildTextView.setText("Age: " + age +" " + ageType);

                addChildButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Baby b = availableChildren.get(position);
                        Boolean unique=true;
                        if(!doctorsChildren.isEmpty()) {
                            for (int i = 0; i < doctorsChildren.size(); i++) {
                                if (b.getAmka().equals(doctorsChildren.get(i).getAmka())) {
                                    unique = false;
                                }
                            }
                        }
                        if(unique) {
                            viewChildInfoLayout.setVisibility(View.INVISIBLE);
                            availableChildrenLayout.setVisibility(View.INVISIBLE);
                            verificationRelativeLayout.setVisibility(View.VISIBLE);
                            verificationButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String amka = verificationEditText.getText().toString();
                                    String amka1 = availableChildren.get(position).getParentOneAmka();
                                    String amka2 = availableChildren.get(position).getParentTwoAmka();
                                    if(amka.equals(amka1) || amka.equals(amka2)) {
                                        doctorsChildren.add(availableChildren.get(position));
                                        updateDatabase();
                                        onBackPressed();
                                    }else{
                                        Toast.makeText(AddChildToDoctor.this, "Wrong amka. Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(AddChildToDoctor.this, "Child already added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        };
    }

    //getting children from doctor list
    private void getDoctorsChildren(){
        reference = database.getReference("doctor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots:snapshot.getChildren()){
                        GenericTypeIndicator<ArrayList<Baby>> t = new GenericTypeIndicator<ArrayList<Baby>>() {};
                        if(snapshots.getKey().equals(currentUserUID)){
                            doctorsChildren = snapshots.child("kids").getValue(t);
                        }
                    }
                    try{
                        if(doctorsChildren.isEmpty()){
                            doctorsChildren = null;
                        }
                    }catch (Exception e){
                        doctorsChildren = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //getting all available children from database
    private void getAvailableChildren(){
        availableChildren.clear();
        reference = database.getReference("baby");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        GenericTypeIndicator<Baby> t = new GenericTypeIndicator<Baby>(){};
                        availableChildren.add(snapshots.getValue(t));
                    }
                    setAdapter();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //update doctor list on database
    private void updateDatabase(){
        reference = database.getReference("doctor").child(currentUserUID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    if (snapshot.getKey().equals(currentUserUID)) {
                        database.getReference("doctor").child(currentUserUID).child("kids").setValue(doctorsChildren);
                        Toast.makeText(AddChildToDoctor.this, "Child added successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //fixing on back press button
    @Override
    public void onBackPressed() {
        if(viewChildInfoLayout.getVisibility() == View.VISIBLE){
            viewChildInfoLayout.setVisibility(View.INVISIBLE);
            availableChildrenLayout.setClickable(true);
            availableChildrenLayout.setVisibility(View.VISIBLE);
        }else if(verificationRelativeLayout.getVisibility() == View.VISIBLE){
            verificationRelativeLayout.setVisibility(View.INVISIBLE);
            viewChildInfoLayout.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }

    }

    //search button
    private void searchButton(View view){
        String amka1 = searchAmkaEditText.getText().toString();
        if(!TextUtils.isEmpty(searchAmkaEditText.getText())) {
            //if textview is not empty
            availableChildren.clear();
            reference = database.getReference("baby");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null) {
                        for (DataSnapshot snapshots : snapshot.getChildren()) {
                           if (snapshots.child("amka").getValue().equals(amka1)) {
                                GenericTypeIndicator<Baby> t = new GenericTypeIndicator<Baby>() {};
                                availableChildren.add(snapshots.getValue(t));
                            }
                        }
                        setAdapter();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Toast.makeText(this, "There is no child with this amka", Toast.LENGTH_SHORT).show();
        }

    }


}
