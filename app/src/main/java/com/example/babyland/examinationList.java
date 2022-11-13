package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class examinationList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CardView cardView;
    private recyclerAdapter.recyclerVewOnClickListener listener;
    private ArrayList<String> examination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_list);
       // cardView = findViewById(R.id.cardView);
        recyclerView = findViewById(R.id.recyclerViewExamination);
        examination = new ArrayList<>();
        examination.add("derma");
        examination.add("miti");
        examination.add("autia");
        setAdapter();
    }

    /*//loading data in adapter from database
    private void setQuestions() {
        // empty the examination types in list
        examination.clear();
        //create variable
        DatabaseReference rootRef= null;
        rootRef = FirebaseDatabase.getInstance().getReference().child("questions");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot sn : snapshot.getChildren()){
                        String s =sn.getValue(String.class);
                        examination.add(s);
                    }
                }
                //calls adapter to load data into recyclerView
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    //loads data from list into recyclerView
    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(listener, examination, "examination");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
       // recyclerView.addItemDecoration(new DividerItemDecoration(this,
         //       DividerItemDecoration.HORIZONTAL));
    }
}