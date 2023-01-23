package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

public class createNewDoctor extends AppCompatActivity {

    private EditText nameEditText, surnameEditText, medicalIDEditText, phoneNumberEditText;
    private Button saveButton;
    private Boolean flagNext, flagUnique;
    private String currentUserUID, currentUserEmail;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_doctor);

        //getting views from xml file
        nameEditText = findViewById(R.id.doctorNameEditTextView);
        surnameEditText = findViewById(R.id.doctorSurnameEditTextView);
        medicalIDEditText = findViewById(R.id.doctorMedicalIDEditTextView);
        phoneNumberEditText = findViewById(R.id.doctorPhoneNumberEditTextView);
        saveButton = findViewById(R.id.saveDoctorInfoButton);
        constraintLayout = findViewById(R.id.constrainLayoutCreateNewDoctor);

        //UI
        constraintLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient));

        //getting user from database
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //setting database
        database = FirebaseDatabase.getInstance();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo(view);
            }
        });

    }

    //check textViews and if medical id is unique
    private void saveInfo(View view){
        flagNext = true;
        if (TextUtils.isEmpty(nameEditText.getText())) {
            nameEditText.setError("Please enter a name!");
            nameEditText.requestFocus();
            flagNext = false;
        }
        if (TextUtils.isEmpty(surnameEditText.getText())) {
            surnameEditText.setError("Please enter a surname!");
            surnameEditText.requestFocus();
            flagNext = false;
        }
        if (TextUtils.isEmpty(medicalIDEditText.getText()) || (medicalIDEditText.getText().length() != 11)) {
            medicalIDEditText.setError("Medical ID should have length 11 numbers!");
            medicalIDEditText.requestFocus();
            flagNext = false;
        }
        if (TextUtils.isEmpty(phoneNumberEditText.getText()) || (phoneNumberEditText.getText().length() != 10)) {
            phoneNumberEditText.setError("Phone number should have length 11 numbers!");
            phoneNumberEditText.requestFocus();
            flagNext = false;
        }

        //searching if medical Id is unique
        flagUnique=true;
        reference = database.getReference("doctor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        GenericTypeIndicator<Doctor> t = new GenericTypeIndicator<Doctor>() {};
                        if (medicalIDEditText.getText().toString().equals(snapshots.getValue(t).getMedicalID())) {
                            flagUnique = false;
                        }
                    }
                }
                check();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //check if restrictions are met
    private void check(){
        if(flagUnique && flagNext){
            //save into database
            Doctor doctor = new Doctor(nameEditText.getText().toString(), medicalIDEditText.getText().toString(),
                    phoneNumberEditText.getText().toString(), currentUserEmail, null, surnameEditText.getText().toString());
            reference = database.getReference("doctor");
            reference.child(currentUserUID).setValue(doctor);
            Toast.makeText(this, "Info saved successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(createNewDoctor.this, MainScreenDoctor.class);
            startActivity(intent);
            finish();
        }else if(!flagUnique){
            Toast.makeText(this, "Doctor exists already! Please try again", Toast.LENGTH_SHORT).show();
        }
    }



}