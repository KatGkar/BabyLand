package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainScreenDoctor extends AppCompatActivity {

    private Button addChildButton, showChildrenButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    Boolean userFound = false;
    private ArrayList<Baby> listKids;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_doctor);


        //getting views
        addChildButton = findViewById(R.id.addChild);
        showChildrenButton = findViewById(R.id.showChildlren);

        //setting database
        database = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        listKids = new ArrayList<>();

        //getting user info from database
        reference = database.getReference("doctor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        String UID = snapshots.getKey();
                        if (UID.equals(currentUser)) {
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


        addVaccines();
    }

    private void addVaccines(){
        reference = FirebaseDatabase.getInstance().getReference("vaccinations");
        Vaccination v1 = new Vaccination("διφθερίτιδαςΤετάνου-Κοκκύτη\n" +
                "(DiphtheriaTetanus-Pertussis)\n" +
                "[<7 ετών: DTaP\n" +
                " (παιδικής ηλικίας)\n" +
                " ≥7 ετών: Tdap/Td\n" +
                " (τύπου ενηλίκου)]", null, null, 0);
        reference.child("v1").setValue(v1);
        Vaccination v2 = new Vaccination("Πολιομυελίτιδας\n" +
                "(Poliomyelitis)\n" +
                "[IPV", null, null, 0);
        reference.child("v2").setValue(v1);
        Vaccination v3 = new Vaccination("Αιμόφιλου\n" +
                "ινφλουέντζας b\n" +
                "(Haemophilus\n" +
                "influenzae b)\n" +
                "[Hib]", null, null, 0);
        reference.child("v3").setValue(v1);
        Vaccination v4 = new Vaccination("Ηπατίτιδας Β\n" +
                "(Hepatitis B)\n" +
                "[HepB]", null, null, 0);
        reference.child("v4").setValue(v1);
        Vaccination v5 = new Vaccination("Πνευμονιόκοκκου,\n" +
                "συζευγμένο\n" +
                "(Pneumococcal,\n" +
                "conjugate)\n" +
                "[PCV]", null, null, 0);
        reference.child("v5").setValue(v1);
        Vaccination v6 = new Vaccination("", null, null, 0);
        reference.child("v6").setValue(v1);
        Vaccination v7 = new Vaccination("", null, null, 0);
        reference.child("v7").setValue(v1);
        Vaccination v8 = new Vaccination("", null, null, 0);
        reference.child("v8").setValue(v1);
        Vaccination v9 = new Vaccination("", null, null, 0);
        reference.child("v9").setValue(v1);
        Vaccination v10 = new Vaccination("", null, null, 0);
        reference.child("v10").setValue(v1);


    }


    private void load(){
        if( listKids !=null && !listKids.isEmpty()){
            showChildrenButton.setClickable(true);
        }else{
            showChildrenButton.setClickable(false);
            Toast.makeText(this, "There are no children to show!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addChild(View view){
        Intent intent = new Intent(MainScreenDoctor.this, addChildToDoctor.class);
        startActivity(intent);
    }

    private void showChildren(View view){
        Intent intent = new Intent(MainScreenDoctor.this, showChildren.class);
        startActivity(intent);
    }



}