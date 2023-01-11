package com.example.babyland;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.lang.reflect.Array;
import java.util.Arrays;

public class AddVaccination extends AppCompatActivity {

    TableLayout tableVac1;
    Button[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccination);


        buttons = new Button[5][4];


        tableVac1 = findViewById(R.id.tableLayoutVac1);


        for (int i = 0; i < 5; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            for(int j=0;j<4;j++) {
                buttons[i][j] = new Button(this);
                buttons[i][j].setText("hello" + i + " " + j);
                buttons[i][j].setClickable(true);
                row.addView(buttons[i][j]);
            }
            tableVac1.addView(row, i);
        }




    }
}