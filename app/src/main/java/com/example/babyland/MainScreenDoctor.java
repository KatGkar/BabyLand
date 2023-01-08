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