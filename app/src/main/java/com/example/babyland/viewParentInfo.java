package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewParentInfo extends AppCompatActivity {

    private TextView nameTextView, amkaTextView, emailTextView, phoneNumberTextView, bloodTypeTextView,
            dateOfBirthTextView, amkaParentOneTextView, amkaParentTwoTextView, parentInfoTextView;
    private RelativeLayout parentsRelativeLayout, parentInfoRelativeLayout;
    private String babyAmka;
    protected Menu the_menu;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Parent parent1, parent2;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parent_info);

        //getting extras
        Bundle extras = getIntent().getExtras();
        babyAmka = extras.getString("babyAmka");

        //getting views from xml file
        nameTextView = findViewById(R.id.nameParentInfoTextView);
        amkaTextView = findViewById(R.id.amkaParentInfoTextView);
        emailTextView = findViewById(R.id.emailParentInfoTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberParentInfoTextView);
        bloodTypeTextView = findViewById(R.id.bloodTypeParentInfoTextView);
        dateOfBirthTextView = findViewById(R.id.dateOfBirthParentInfoTextView);
        parentsRelativeLayout = findViewById(R.id.parentsRelativeLayout);
        parentInfoRelativeLayout = findViewById(R.id.parentInfoRelativeLayout);
        amkaParentOneTextView = findViewById(R.id.amkaParentOneParentInfoTextView);
        amkaParentTwoTextView = findViewById(R.id.amkaParentTwoParentInfoTextView);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewViewParentInfo);
        parentInfoTextView = findViewById(R.id.parentInfoTextView);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        parentInfoTextView.setPaintFlags(parentInfoTextView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        //setting database
        database = FirebaseDatabase.getInstance();
        parent1 = new Parent();
        parent2 = new Parent();

        //setting visibilities
        parentsRelativeLayout.setVisibility(View.VISIBLE);
        parentInfoRelativeLayout.setVisibility(View.INVISIBLE);
        amkaParentTwoTextView.setVisibility(View.VISIBLE);
        amkaParentOneTextView.setVisibility(View.VISIBLE);

        //find parents
        reference = database.getReference("baby");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        if (snapshots.child("amka").getValue().equals(babyAmka)) {
                           parent1.setAmka((String) snapshots.child("parentOneAmka").getValue());
                           parent2.setAmka((String) snapshots.child("parentTwoAmka").getValue());
                        }
                    }
                    findParents();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        amkaParentOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewParentInformation(parent1);
            }
        });

        amkaParentTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewParentInformation(parent2);
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
                        Intent intent = new Intent(viewParentInfo.this, MainScreenDoctor.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_add:
                        addChild();
                        return true;
                    case R.id.navigation_account:
                        settingsButton();
                        return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuBuilder m = (MenuBuilder) menu;
        m.setOptionalIconsVisible(true);

        //get the menu
        the_menu = menu;

        //Called when a menu item with is collapsed.
        MenuItem.OnActionExpandListener actionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }
        };


        return true;

    }


    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {


        return super.onOptionsItemSelected(item);
    }


    //go to settings
    private void settingsButton(){
        Intent intent = new Intent(viewParentInfo.this, UserAccount.class);
        intent.putExtra("user", "doctor");
        startActivity(intent);
    }

    //view parents info
    private void viewParentInformation(Parent parent){
        parentsRelativeLayout.setVisibility(View.INVISIBLE);
        parentInfoRelativeLayout.setVisibility(View.VISIBLE);
        nameTextView.setText("Fullname: "+parent.getName());
        amkaTextView.setText("Amka: " +parent.getAmka());
        emailTextView.setText("Email: "+parent.getEmail());
        phoneNumberTextView.setText("Phone number: "+parent.getPhoneNumber());
        bloodTypeTextView.setText("Blood type: "+parent.getBloodType());
        dateOfBirthTextView.setText("Birth date: "+parent.getDateOfBirth());
    }

    //go to add child page
    private void addChild(){
        Intent intent = new Intent(viewParentInfo.this, AddChildToDoctor.class);
        startActivity(intent);
    }

    //finding parents from database
    private void findParents(){
        reference = database.getReference("parent");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots:snapshot.getChildren()){
                        if(snapshots.child("amka").getValue().equals(parent1.getAmka())){
                            GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>(){};
                             parent1 = snapshots.getValue(t);
                        }
                        if(snapshots.child("amka").getValue().equals(parent2.getAmka())){
                            GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>(){};
                            parent2 = snapshots.getValue(t);
                        }
                    }
                    amkaParentOneTextView.setText("Parent amka: " +parent1.getAmka());
                    if(!parent2.getAmka().equals("00000000000")){
                        amkaParentTwoTextView.setText("Co-parent amka: " + parent2.getAmka());
                    }else{
                        amkaParentTwoTextView.setVisibility(View.INVISIBLE);
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
        if(parentInfoRelativeLayout.getVisibility() == View.VISIBLE){
            parentInfoRelativeLayout.setVisibility(View.INVISIBLE);
            parentsRelativeLayout.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }

    }
}
