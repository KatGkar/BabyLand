package com.example.babyland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class doctorParentChoose extends AppCompatActivity {

    private ImageView doctorButton, parentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_parent_choose);

        //getting views
        doctorButton = findViewById(R.id.doctorButton);
        parentButton = findViewById(R.id.parentsButton);

        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(doctorParentChoose.this, MainScreenDoctor.class);
                startActivity(intent);
            }
        });

        parentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(doctorParentChoose.this, MainScreenParents.class);
                startActivity(intent);
            }
        });

    }
}