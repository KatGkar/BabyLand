package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorParentChoose extends AppCompatActivity {

    private ImageView doctorButton, parentButton, loadingImageView;
    private TextView chooseTypeTextView;
    private String currentUserUID;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_parent_choose);

        //getting views from xml file
        doctorButton = findViewById(R.id.doctorButton);
        parentButton = findViewById(R.id.parentsButton);
        constraintLayout = findViewById(R.id.constrainLayoutDoctorParentChoose);
        loadingImageView = findViewById(R.id.loadingImageView);
        chooseTypeTextView = findViewById(R.id.chooseTypeTextView);

        //UI
        constraintLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient));
        chooseTypeTextView.setText("Please choose user type...");

        //setting visibilities
        doctorButton.setVisibility(View.INVISIBLE);
        parentButton.setVisibility(View.INVISIBLE);
        chooseTypeTextView.setVisibility(View.INVISIBLE);
        loadingImageView.setVisibility(View.VISIBLE);

        //setting database
        database = FirebaseDatabase.getInstance();

        //getting user UID
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //checking if user is a doctor
        reference = database.getReference("doctor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots:snapshot.getChildren()){
                        if(snapshots.getKey().equals(currentUserUID)){
                            Intent intent = new Intent(DoctorParentChoose.this, MainScreenDoctor.class);
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //checking if user is a parent
        reference = database.getReference("parent");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots:snapshot.getChildren()){
                        if(snapshots.getKey().equals(currentUserUID)){
                            Intent intent = new Intent(DoctorParentChoose.this, MainScreenParents.class);
                            startActivity(intent);
                        }
                    }
                    showLayout();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //if user chooses to be a doctor
        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorParentChoose.this, createNewDoctor.class);
                startActivity(intent);
            }
        });

        //if user chooses to be a parent
        parentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorParentChoose.this, createNewParent.class);
                startActivity(intent);
            }
        });
    }


    //if it is first time log in for user
    private void showLayout(){
        loadingImageView.setVisibility(View.INVISIBLE);
        chooseTypeTextView.setVisibility(View.VISIBLE);
        doctorButton.setVisibility(View.VISIBLE);
        parentButton.setVisibility(View.VISIBLE);
    }

}
