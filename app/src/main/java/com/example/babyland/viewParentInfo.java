package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewParentInfo extends AppCompatActivity {

    private TextView nameTextView, surnameTextView, amkaTextView, emailTextView, phoneNumberTextView, bloodTypeTextView,
            dateOfBirthTextView, amkaParentOneTextView, amkaParentTwoTextView;
    private RelativeLayout parentsRelativeLayout, parentInfoRelativeLayout;
    private String babyAmka;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Parent parent1, parent2;

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
        surnameTextView = findViewById(R.id.surnameParentInfoTextView);
        emailTextView = findViewById(R.id.emailParentInfoTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberParentInfoTextView);
        bloodTypeTextView = findViewById(R.id.bloodTypeParentInfoTextView);
        dateOfBirthTextView = findViewById(R.id.dateOfBirthParentInfoTextView);
        parentsRelativeLayout = findViewById(R.id.parentsRelativeLayout);
        parentInfoRelativeLayout = findViewById(R.id.parentInfoRelativeLayout);
        amkaParentOneTextView = findViewById(R.id.amkaParentOneParentInfoTextView);
        amkaParentTwoTextView = findViewById(R.id.amkaParentTwoParentInfoTextView);


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
    }

    //view parents info
    private void viewParentInformation(Parent parent){
        parentsRelativeLayout.setVisibility(View.INVISIBLE);
        parentInfoRelativeLayout.setVisibility(View.VISIBLE);
        nameTextView.setText(parent.getName());
        amkaTextView.setText(parent.getAmka());
        surnameTextView.setText(parent.getSurname());
        emailTextView.setText(parent.getEmail());
        phoneNumberTextView.setText(parent.getPhoneNumber());
        bloodTypeTextView.setText(parent.getBloodType());
        dateOfBirthTextView.setText(parent.getDateOfBirth());
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
                    amkaParentOneTextView.setText(parent1.getAmka());
                    if(!parent2.getAmka().equals("00000000000")){
                        amkaParentTwoTextView.setText(parent2.getAmka());
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
