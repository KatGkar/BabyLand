package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    Boolean found=false, userFound;
    private ArrayList<Baby> kids;
    String currentuser;
    private RelativeLayout noBaby;
    private ImageButton addBabyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        //getting views
        noBaby = findViewById(R.id.noBaby);
        addBabyButton = findViewById(R.id.addBabyButton);

        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        kids=null;
        userFound = false;

        //getting ids from xml file
        noBaby.setVisibility(View.INVISIBLE);


        //setting database
        database = FirebaseDatabase.getInstance();
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //getting user info from database parent
        reference = database.getReference("parent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        String UID = String.valueOf(snapshots.child("UID").getValue());
                        if (UID.equals(currentuser)) {
                            userFound = true;
                            kids = (ArrayList<Baby>) snapshots.child("kids").getValue();
                        }else{
                          userFound = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if(kids != null && !kids.isEmpty()){
            //show panel with babies
            noBaby.setVisibility(View.INVISIBLE);
        }else{
            if(userFound){
                //user found with none baby
                //show panel no babies
                noBaby.setVisibility(View.VISIBLE);
                //  Intent intent = new Intent(this, AddBaby.class);
                // startActivity(intent);
            }else{
                //user not found
                //create new user
                Intent intent = new Intent(getApplicationContext(), createNewUser.class);
                intent.putExtra("UID", currentuser);
                startActivity(intent);
            }

        }



    }

    //showing messages to users
    public void showMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}