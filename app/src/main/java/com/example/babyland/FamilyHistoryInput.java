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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FamilyHistoryInput extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected ArrayList<FamilyHistoryIlnesses> ilnesses ;
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
        ilnesses = new ArrayList<>();
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

        FamilyHistoryIlnesses f1 = new FamilyHistoryIlnesses("Διαταραχές ακοής", false, "");
        FamilyHistoryIlnesses f2 = new FamilyHistoryIlnesses("Διαταραχές όρασης", false, "");
        FamilyHistoryIlnesses f3 = new FamilyHistoryIlnesses("Αναπτυξιακή δυσπλασία των ισχύων", false, "");
        FamilyHistoryIlnesses f4 = new FamilyHistoryIlnesses("Αλλεργίες/ Βρογχικό άσθμα/ Έκζεμα", false, "");
        FamilyHistoryIlnesses f5 = new FamilyHistoryIlnesses("Συγγενής καρδιοπάθεια", false, "");
        FamilyHistoryIlnesses f6 = new FamilyHistoryIlnesses("Πρώιμη καρδιαγγειακή νόσος (<55 ετών σε άντρες και <65 ετών σε γυναίκες)", false, "");
        FamilyHistoryIlnesses f7 = new FamilyHistoryIlnesses("Ιστορικό αιφνιδίου θανάτου σε ηλικία <50 ετών", false, "");
        FamilyHistoryIlnesses f8 = new FamilyHistoryIlnesses("Δυσλιπιδαιμία", false, "");
        FamilyHistoryIlnesses f9 = new FamilyHistoryIlnesses("Αρτηριακή υπέρταση", false, "");
        FamilyHistoryIlnesses f10 = new FamilyHistoryIlnesses("Σακχαρώδης διαβήτης", false, "");
        FamilyHistoryIlnesses f11 = new FamilyHistoryIlnesses("Νεφρική νόσος", false, "");
        FamilyHistoryIlnesses f12 = new FamilyHistoryIlnesses("Αναιμία/ Διαταραχές πήξης", false, "");
        FamilyHistoryIlnesses f13 = new FamilyHistoryIlnesses("Σπασμοί", false, "");
        FamilyHistoryIlnesses f14 = new FamilyHistoryIlnesses("Ψυχικά νοσήματα", false, "");
        FamilyHistoryIlnesses f15 = new FamilyHistoryIlnesses("Αναπτυξιακές διαταραχές (μαθησιακές δυσκολίες/\n" +
                "νοητική υστέρηση/ αυτισμός/ ΔΕΠ-Υ κ.ά.)", false, "");
        FamilyHistoryIlnesses f16 = new FamilyHistoryIlnesses("Συγγενείς ανωμαλίες/ Γενετικά σύνδρομα", false, "");
        FamilyHistoryIlnesses f17 = new FamilyHistoryIlnesses("Νοσήματα που αφορούν ≥2 μέλη της οικογένειας", false, "");
        ilnesses.add(f1);
        ilnesses.add(f2);
        ilnesses.add(f3);
        ilnesses.add(f4);
        ilnesses.add(f5);
        ilnesses.add(f6);
        ilnesses.add(f7);
        ilnesses.add(f8);
        ilnesses.add(f9);
        ilnesses.add(f10);
        ilnesses.add(f11);
        ilnesses.add(f12);
        ilnesses.add(f13);
        ilnesses.add(f14);
        ilnesses.add(f15);
        ilnesses.add(f16);
        ilnesses.add(f17);


        setAdapter();
    }

    public void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

    //adapter to load services into the recyclerView
    private void setAdapter() {
        setOnClickListener();
        recyclerAdapter adapter = new recyclerAdapter(listener, ilnesses, "ilnessInput");
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
        Baby b = new Baby(name, birthDate, amka, birthPlace, bloodType,sex, "-", "-", "-", ilnesses);
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