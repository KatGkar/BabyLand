package com.example.babyland;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class monitoringDevelopment extends AppCompatActivity {

    private TextView dateText;
    private EditText ageText, weightText, lengthText, headCircumferenceText, doctorText, sustenanceEditText;
    private Date date;
    private ArrayList<String> sustenance, examination, developmentalMonitoring;
    private Boolean hearing;
    private String observations, doctor, sustenanceText;
    private Button sustenanceButton, examinationButton, developmentalMonitoringButton, observationsButton;
    private Switch hearingSwitch;
    private RelativeLayout generalLayout, sustenanceLayout;
    private CalendarView calendarView;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_development);

        //finding views on layout
        dateText = findViewById(R.id.dateText);
        ageText = findViewById(R.id.ageText);
        weightText = findViewById(R.id.weightText);
        lengthText = findViewById(R.id.lengthText);
        headCircumferenceText = findViewById(R.id.headCircumferenceText);
        sustenanceButton = findViewById(R.id.sustenanceButton);
        examinationButton = findViewById(R.id.examinationButton);
        developmentalMonitoringButton = findViewById(R.id.developmentalMonitoringButton);
        doctorText = findViewById(R.id.doctorText);
        hearingSwitch = findViewById(R.id.hearingSwitch);
        observationsButton = findViewById(R.id.observationsButton);
        generalLayout = findViewById(R.id.generalLayout);
        calendarView = findViewById(R.id.calendar);
        sustenanceLayout = findViewById(R.id.sustenanceLayout);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        checkBox6 = findViewById(R.id.checkBox6);
        checkBox7 = findViewById(R.id.checkBox7);
        sustenanceEditText = findViewById(R.id.sustenanceText);


        //setting visibilities
        generalLayout.setVisibility(View.VISIBLE);
        calendarView.setVisibility(View.INVISIBLE);
        sustenanceLayout.setVisibility(View.INVISIBLE);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalLayout.setVisibility(View.INVISIBLE);
                calendarView.setVisibility(View.VISIBLE);
            }
        });

        //getting date of the calendar
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                String m="0";
                String d="0";
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    //date1 is selected date
                    Date date1 = sdf.parse(dayOfMonth + "/" + month + "/" + year);
                    int da = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    int mo = Calendar.getInstance().get(Calendar.MONTH);
                    int ye = Calendar.getInstance().get(Calendar.YEAR);
                    //date2 is date now
                    Date date2 = sdf.parse(da + "/"+ mo+ "/" + ye);
                    if ( date1.compareTo(date2) >0 ) {
                        //if date selected is after date now
                        mo++;
                        if(mo==13){
                            mo=12;
                        }
                        if(mo<=9){
                            m = "0"+ mo;
                        }else{
                            m = String.valueOf(mo);
                        }
                        if(da<=9){
                            d = "0"+da;
                        }else{
                            d = String.valueOf(da);
                        }
                        year = Calendar.getInstance().get(Calendar.YEAR);

                    }else {
                        //if date selected is now or if date selected is before date now
                        month++;
                        if(month == 13){
                            month = 12;
                        }
                        if(month<=9){
                            m = "0"+ month;
                        }else{
                            m = String.valueOf(month);
                        }
                        if(dayOfMonth<=9){
                            d = "0"+dayOfMonth;
                        }else{
                            d = String.valueOf(dayOfMonth);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("Watch out!!", "Something went wrong. Please try again later!");
                }
                dateText.setText(d + "/" + m + "/" + year);
                calendarView.setVisibility(View.INVISIBLE);
                generalLayout.setVisibility(View.VISIBLE);
            }
        });


        //sustenance button
        sustenanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sustenance();
            }
        });

    }

    //sustenance function
    public void sustenance(){
        generalLayout.setVisibility(View.INVISIBLE);
        sustenanceLayout.setVisibility(View.VISIBLE);
        checkBox1.setVisibility(View.VISIBLE);
        checkBox2.setVisibility(View.VISIBLE);
        checkBox3.setVisibility(View.VISIBLE);
        checkBox4.setVisibility(View.VISIBLE);
        checkBox5.setVisibility(View.VISIBLE);
        checkBox6.setVisibility(View.VISIBLE);
        checkBox7.setVisibility(View.VISIBLE);
        String age = " ";
        if(age.equals("1-2 weeks") || age.equals("2 months") || age.equals("4 months")) {
            checkBox1.setText("Exclusive breastfeeding");
            checkBox2.setText("Mixed sustenance");
            checkBox3.setText("Modified cow's milk");
            checkBox4.setText("Special sustenance");
            checkBox5.setText("Vitamin D");
            checkBox6.setVisibility(View.GONE);
            checkBox7.setVisibility(View.GONE);
        }else if(age.equals("6 months")){
            checkBox1.setText("Breastfeeding");
            checkBox2.setText("Ablactation");
            checkBox3.setText("Introduction of solid foods");
            checkBox4.setText("Modified cow's milk");
            checkBox5.setVisibility(View.GONE);
            checkBox6.setVisibility(View.GONE);
            checkBox7.setVisibility(View.GONE);
        }else if(age.equals("9 months")){
            checkBox1.setText("Breastfeeding");
            checkBox2.setText("Ablactation");
            checkBox3.setText("Modified cow's milk");
            checkBox4.setText("Special sustenance");
            checkBox5.setText("Food allergies");
            checkBox6.setText("Solid foods");
            checkBox7.setVisibility(View.GONE);
        }else {
            //12-15 months
            checkBox1.setText("Breastfeeding");
            checkBox2.setText("Ablactation");
            checkBox3.setText("Cow's milk");
            checkBox4.setText("Chewing");
            checkBox5.setText("Food allergies");
            checkBox6.setText("Introduction of unmashed foods");
            checkBox7.setText("Another milk");
        }
    }


    //showing messages to users
    public void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}