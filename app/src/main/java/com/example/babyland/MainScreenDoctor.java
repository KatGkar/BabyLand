package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainScreenDoctor extends AppCompatActivity {

    private Button addChildButton, showChildrenButton, settingsButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Boolean userFound = false;
    private ArrayList<Baby> listKids;
    private String currentUserUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_doctor);

        //getting views from xml file
        addChildButton = findViewById(R.id.addChild);
        showChildrenButton = findViewById(R.id.showChildlren);
        settingsButton = findViewById(R.id.settingsDoctorButton);

        //setting database
        database = FirebaseDatabase.getInstance();

        //getting user UID
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //setting list
        listKids = new ArrayList<>();

        //getting user children from database
        getChildren();

        //button to add baby
        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChild(view);
            }
        });

        //button to show children
        showChildrenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChildren(view);
            }
        });

        //button to setting menu
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsButton(view);
            }
        });
    }

    //getting children from database
    private void getChildren(){
        reference = database.getReference("doctor");
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
                    load();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //go to settings
    private void settingsButton(View view){
        Intent intent = new Intent(MainScreenDoctor.this, UserAccount.class);
        intent.putExtra("user", "doctor");
        startActivity(intent);
    }


    //on page resume
    @Override
    protected void onResume() {
        super.onResume();
        getChildren();
    }

    //on back button pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainScreenDoctor.this, LoginRegister.class);
        startActivity(intent);
    }

    //check if doctor has taken on any children
    private void load(){
        if( listKids !=null && !listKids.isEmpty()){
            showChildrenButton.setClickable(true);
        }else{
            showChildrenButton.setClickable(false);
        }
    }

    //go to add child page
    private void addChild(View view){
        Intent intent = new Intent(MainScreenDoctor.this, addChildToDoctor.class);
        startActivity(intent);
    }

    //go to show children page
    private void showChildren(View view){
        Intent intent = new Intent(MainScreenDoctor.this, showChildren.class);
        startActivity(intent);
    }



}