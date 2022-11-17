package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FamilyHistoryInput extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected ArrayList<FamilyHistoryIllnesses> illnesses ;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    Button nextButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    String name, sex, birthDate, amka, birthPlace, bloodType;
    int babyNumber;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_history_input);
        recyclerView = findViewById(R.id.recyclerViewFamilyHistory);
        illnesses = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        database = FirebaseDatabase.getInstance();

         sex = extras.getString("sex");
         name = extras.getString("name");
         birthDate = extras.getString("birthDate");
         amka = extras.getString("amka");
         birthPlace = extras.getString("birthPlace");
         bloodType = extras.getString("bloodType");

        //getting babies on database
        babyNumber = getBabyNumber();

        nextButton = findViewById(R.id.nextButton);

        FamilyHistoryIllnesses f1 = new FamilyHistoryIllnesses("Διαταραχές ακοής", false, "");
        FamilyHistoryIllnesses f2 = new FamilyHistoryIllnesses("Διαταραχές όρασης", false, "");
        FamilyHistoryIllnesses f3 = new FamilyHistoryIllnesses("Αναπτυξιακή δυσπλασία των ισχύων", false, "");
        FamilyHistoryIllnesses f4 = new FamilyHistoryIllnesses("Αλλεργίες/ Βρογχικό άσθμα/ Έκζεμα", false, "");
        FamilyHistoryIllnesses f5 = new FamilyHistoryIllnesses("Συγγενής καρδιοπάθεια", false, "");
        FamilyHistoryIllnesses f6 = new FamilyHistoryIllnesses("Πρώιμη καρδιαγγειακή νόσος (<55 ετών σε άντρες και <65 ετών σε γυναίκες)", false, "");
        FamilyHistoryIllnesses f7 = new FamilyHistoryIllnesses("Ιστορικό αιφνιδίου θανάτου σε ηλικία <50 ετών", false, "");
        FamilyHistoryIllnesses f8 = new FamilyHistoryIllnesses("Δυσλιπιδαιμία", false, "");
        FamilyHistoryIllnesses f9 = new FamilyHistoryIllnesses("Αρτηριακή υπέρταση", false, "");
        FamilyHistoryIllnesses f10 = new FamilyHistoryIllnesses("Σακχαρώδης διαβήτης", false, "");
        FamilyHistoryIllnesses f11 = new FamilyHistoryIllnesses("Νεφρική νόσος", false, "");
        FamilyHistoryIllnesses f12 = new FamilyHistoryIllnesses("Αναιμία/ Διαταραχές πήξης", false, "");
        FamilyHistoryIllnesses f13 = new FamilyHistoryIllnesses("Σπασμοί", false, "");
        FamilyHistoryIllnesses f14 = new FamilyHistoryIllnesses("Ψυχικά νοσήματα", false, "");
        FamilyHistoryIllnesses f15 = new FamilyHistoryIllnesses("Αναπτυξιακές διαταραχές (μαθησιακές δυσκολίες/\n" +
                "νοητική υστέρηση/ αυτισμός/ ΔΕΠ-Υ κ.ά.)", false, "");
        FamilyHistoryIllnesses f16 = new FamilyHistoryIllnesses("Συγγενείς ανωμαλίες/ Γενετικά σύνδρομα", false, "");
        FamilyHistoryIllnesses f17 = new FamilyHistoryIllnesses("Νοσήματα που αφορούν ≥2 μέλη της οικογένειας", false, "");
        illnesses.add(f1);
        illnesses.add(f2);
        illnesses.add(f3);
        illnesses.add(f4);
        illnesses.add(f5);
        illnesses.add(f6);
        illnesses.add(f7);
        illnesses.add(f8);
        illnesses.add(f9);
        illnesses.add(f10);
        illnesses.add(f11);
        illnesses.add(f12);
        illnesses.add(f13);
        illnesses.add(f14);
        illnesses.add(f15);
        illnesses.add(f16);
        illnesses.add(f17);


        setAdapter();
    }

    public void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

    //adapter to load services into the recyclerView
    private void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(listener, illnesses, "illnessInput");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    private void setOnClickListener() {
    }


    public void Click(View view){
        babyNumber = getBabyNumber();

      //  currentUser = FirebaseAuth.getInstance().getCurrentUser();
       // String user = currentUser.get
        Baby b = new Baby(name, birthDate, amka, birthPlace, bloodType,sex, "-", "-", "-", illnesses);
        reference = database.getReference("baby");
        reference.child("baby" + babyNumber).setValue(b);
    }

    //getting number of babies on database
    private int getBabyNumber() {
        reference = database.getReference("baby");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                babyNumber= (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return babyNumber+1;
    }





}