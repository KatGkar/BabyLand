package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class deleteChild extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference, reference1;
    private String currentUser;
    private RecyclerView deleteChildRecyclerView;
    private ArrayList<Baby> childList;
    private RecyclerAdapter.recyclerVewOnClickListener listener;
    private AlertDialog.Builder builder;
    private TextView childInfoTextView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_child);

        //finding views in xml file
        deleteChildRecyclerView = findViewById(R.id.deleteChildRecyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewDeleteChild);
        childInfoTextView = findViewById(R.id.childInfoTextView);

        //setting visibilities
        deleteChildRecyclerView.setVisibility(View.VISIBLE);
        childInfoTextView.setVisibility(View.VISIBLE);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        childInfoTextView.setPaintFlags(childInfoTextView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        //builder to show message
        builder = new AlertDialog.Builder(this);

        //setting database
        database = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //getting user info from database parent
        reference = database.getReference("parent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        String UID = snapshots.getKey();
                        if (UID.equals(currentUser)) {
                            GenericTypeIndicator<ArrayList<Baby>> t = new GenericTypeIndicator<ArrayList<Baby>>(){};
                            childList = snapshots.child("kids").getValue(t);
                        }
                    }
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        return true;
                    case R.id.navigation_add:
                        Intent intent = new Intent(getApplicationContext(), addBaby.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_account:
                        Intent intent1 = new Intent(deleteChild.this, userAccount.class);
                        intent1.putExtra("user", "parent");
                        startActivity(intent1);
                        return true;
                }
                return false;
            }
        });

    }
    //on resume page
    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        deleteChildRecyclerView.setVisibility(View.VISIBLE);
        childInfoTextView.setVisibility(View.VISIBLE);
        super.onResume();
    }


    //setting adapter for recyclerView
    private void setAdapter(){
        setOnClickListener();
        RecyclerAdapter adapter = new RecyclerAdapter(listener, childList, "deleteChild","none");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        deleteChildRecyclerView.setLayoutManager(layoutManager);
        deleteChildRecyclerView.setItemAnimator(new DefaultItemAnimator());
        deleteChildRecyclerView.setAdapter(adapter);
    }

    //click listener to delete child
    private void setOnClickListener() {
        listener = new RecyclerAdapter.recyclerVewOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                builder.setMessage("Are you sure? Deletion will be permanent!").setTitle("Delete child")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //deleting child from baby list
                                reference = database.getReference("baby");
                                reference.child(childList.get(position).getAmka()).removeValue();
                                //deleting child from parent list
                                reference = database.getReference("parent");
                                removeDevelopments(childList.get(position).getAmka());
                                removeFromDoctor(childList.get(position).getAmka());
                                childList.remove(position);
                                reference = FirebaseDatabase.getInstance().getReference("parent");
                                reference.child(currentUser).child("kids").setValue(childList);
                                setAdapter();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
    }

    //function to delete baby developments if baby gets deleted
    private void removeDevelopments(String amka){
        reference = FirebaseDatabase.getInstance().getReference("monitoringDevelopment");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {
                if(snapshots!=null){
                    for(DataSnapshot dataSnapshot:snapshots.getChildren()){
                        GenericTypeIndicator<Development> t = new GenericTypeIndicator<Development>(){};
                        Development dev = dataSnapshot.getValue(t);
                        if(dev.getAmka().equals(amka)){
                            reference = dataSnapshot.getRef();
                            reference.removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //remove child from doctor list
    private void removeFromDoctor(String babyAmka){
        reference1 = database.getReference("doctor");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        GenericTypeIndicator<Doctor> t = new GenericTypeIndicator<Doctor>() {};
                        try{
                            int size = snapshots.getValue(t).getKids().size();
                            for(int i=0;i<size;i++){
                                if(snapshots.getValue(t).getKids().get(i).getAmka().equals(babyAmka)){
                                    ArrayList<Baby> k = snapshots.getValue(t).getKids();
                                    k.remove(i);
                                    Doctor d = snapshots.getValue(t);
                                    Doctor d1 = new Doctor(d.getName(), d.getMedicalID(), d.getPhoneNumber(), d.getEmail(), k, d.getSurname());
                                    reference1.child(snapshots.getKey()).removeValue();
                                    reference1.child(snapshots.getKey()).setValue(d1);
                                }
                            }
                        }catch (Exception e){
                            //do nothing
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
