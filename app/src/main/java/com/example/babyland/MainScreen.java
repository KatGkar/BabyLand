package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    String userAmka;
    Boolean found=false;
    private ArrayList<Baby> kids;
    String currentuser;
    private RelativeLayout noBaby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        kids=null;

        //getting ids from xml file
        noBaby = findViewById(R.id.noBaby);
        noBaby.setVisibility(View.INVISIBLE);

        //setting database
        database = FirebaseDatabase.getInstance();
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //getting user info from database
        reference = database.getReference("parent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        String UID = String.valueOf(snapshots.child("UID").getValue());
                        if (UID.equals(currentuser)) {
                            kids = (ArrayList<Baby>) snapshots.child("kids").getValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(kids != null && !kids.isEmpty()){
            //show panel to add baby
            noBaby.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, AddBaby.class);
            startActivity(intent);
        }else{
            //show panel with babies
            noBaby.setVisibility(View.INVISIBLE);
        }
    }
}