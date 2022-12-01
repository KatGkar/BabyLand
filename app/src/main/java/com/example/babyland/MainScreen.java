package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.BaseBundle;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainScreen extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    Boolean userFound = false;
    private ArrayList<Baby> listKids;
    String currentUser;
    private RelativeLayout noBabyLayout, mainScreenLayout;
    private ImageButton addBabyButton;
    private String[]  kids;
    private Spinner spinner;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        //getting views
        noBabyLayout = findViewById(R.id.noBabyLayout);
        addBabyButton = findViewById(R.id.addBabyButton);
        mainScreenLayout = findViewById(R.id.mainScreenLayout);
        spinner = findViewById(R.id.spinner);
        name = findViewById(R.id.textView);

        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        listKids=null;
        userFound = false;

        //getting ids from xml file
        noBabyLayout.setVisibility(View.INVISIBLE);
        mainScreenLayout.setVisibility(View.VISIBLE);

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
                            userFound = true;
                            listKids = (ArrayList<Baby>) snapshots.child("kids").getValue();
                        }
                    }
                }
                load();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addBabyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddBaby.class);
                startActivity(intent);
            }
        });


    }


    public void load(){
        if(listKids != null && !listKids.isEmpty()){
            //show panel with babies
            noBabyLayout.setVisibility(View.INVISIBLE);
            //noBabyLayout.setVisibility(View.VISIBLE);
            mainScreenLayout.setVisibility(View.VISIBLE);
            showKids();
        }else{
            if(userFound){
                //user found with none baby
                //show panel no babies
                mainScreenLayout.setVisibility(View.INVISIBLE);
                noBabyLayout.setVisibility(View.VISIBLE);
            }else{
                //user not found
                //create new user
                Intent intent = new Intent(getApplicationContext(), createNewUser.class);
                startActivity(intent);
            }

        }

    }

    public void showKids(){
       // ArrayAdapter<Baby> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listKids);
       // spinner.setAdapter(adapter);
    }


    public void button(View view){
        /* Baby b;
        if(!(spinner.getSelectedItem() == null))
        {
            b = (Baby) spinner.getSelectedItem();
            name.setText(String.format("Name: " + b.getName() + "\t Amka: " + b.getAmka()));
        }*/
    }







    //showing messages to users
    public void showMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}