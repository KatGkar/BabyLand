package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBaby extends AppCompatActivity {
    TextView babyName, babyAmka, babyBirthPlace;
    EditText babyBirthDate;
    RadioButton babyBoy, babyGirl;
    public String[] bloodType = {"A RhD positive (A+)","A RhD negative (A-)", "B RhD positive (B+)",
            "B RhD negative (B-)", "O RhD positive (O+)", "O RhD negative (O-)", "AB RhD positive (AB+)", "AB RhD negative (AB-)"};
    private AutoCompleteTextView babyBloodType;
    RadioGroup radioGroup;
    CalendarView calendar;
    Button calendarButton;
    RelativeLayout infoRelativeLayout;
    FirebaseDatabase database;
    DatabaseReference reference;
    Boolean flagUnique;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_baby);
        //adding new baby
        //finding views on xml file
        babyName = findViewById(R.id.babyName);
        babyAmka = findViewById(R.id.babyAmka);
        babyBirthPlace = findViewById(R.id.babyBirthPlace);
        babyBoy = findViewById(R.id.babyBoy);
        babyGirl = findViewById(R.id.babyGirl);
        babyBirthDate = findViewById(R.id.babyBirthDate);
        babyBloodType = findViewById(R.id.babyBloodType);
        calendar = findViewById(R.id.calendarView2);
        calendarButton = findViewById(R.id.calendarButton);
        infoRelativeLayout = findViewById(R.id.infoRelativeLayout);
        radioGroup = findViewById(R.id.radioGroup);
        //setting database
        database = FirebaseDatabase.getInstance();



        //setting hint in babyBirthDate
        //getting current date
        Calendar  cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        babyBirthDate.setHint(new StringBuilder()
                .append(dd).append(" ").append("/").append(mm + 1).append("/")
                .append(yy));

        //setting visibilities
        calendar.setVisibility(View.INVISIBLE);
        infoRelativeLayout.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);

        //setting blood types in list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, bloodType);
        babyBloodType.setThreshold(1);//will start working from first character
        babyBloodType.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        //onclick listener for calendar opening
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setVisibility(View.VISIBLE);
                infoRelativeLayout.setVisibility(View.INVISIBLE);
                radioGroup.setVisibility(View.INVISIBLE);
            }
        });

        //getting date of the calendar
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                String m="1";
                String d="1";
                if(month<=9){
                    m = "0"+ month;
                }
                if(dayOfMonth<=9){
                    d = "0"+dayOfMonth;
                }
                babyBirthDate.setText(d + "/" + m + "/" + year);
                calendar.setVisibility(View.INVISIBLE);
                infoRelativeLayout.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.VISIBLE);
            }
        });


        //setting listener to format textView babyBirthDate
        babyBirthDate.addTextChangedListener(new TextWatcher() {
            private String current="";
            private String ddmmyyyy = "ddmmyyyy";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for(int i=2; i<=cl && i<6; i+=2){
                        sel++;
                    }
                    if(clean.equals(cleanC)){
                        sel--;
                    }
                    if(clean.length()<8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        int day = Integer.parseInt(clean.substring(0,2));
                        int month = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(month>12){
                            month = 12;
                        }
                        cal.set(Calendar.MONTH, month-1);

                        year = (year<2010)?2010:(year>2023)?2022:year;
                        cal.set(Calendar.YEAR, year);

                        day = (day>cal.getActualMaximum(Calendar.DATE))?cal.getActualMaximum((Calendar.DATE)):day;
                        clean = String.format("%02d%02d%02d", day, month, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0,2), clean.substring(2,4), clean.substring(4,8));

                    sel = sel<0?0:sel;
                    current = clean;
                    babyBirthDate.setText(current);
                    babyBirthDate.setSelection(sel<current.length()? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //check restrictions and add new baby or show error messages
    public void addNewBaby(View view){
        String sex;
        Boolean flagAmka= false;
        if(babyGirl.isChecked()) {
            sex= "GIRL";
        }else{
            sex ="BOY";
        }
        if(babyAmka.getText().length() == 11){
            flagAmka = true;
        }
        flagUnique = findIfUnique();
        if(flagAmka){ //&& flagUnique) {
            Intent myIntent = new Intent(this, FamilyHistoryInput.class);
            myIntent.putExtra("sex",sex) ;
            myIntent.putExtra("name", babyName.getText().toString());
            myIntent.putExtra("birthDate",babyBirthDate.getText().toString());
            myIntent.putExtra("amka",babyAmka.getText().toString());
            myIntent.putExtra("birthPlace",babyBirthPlace.getText().toString());
            myIntent.putExtra("bloodType",babyBloodType.getText().toString());
            this.startActivity(myIntent);
        }else{
            showMessage("Error", "Watch out!");
        }

    }

    Boolean flag;
    private Boolean findIfUnique() {
        flag = false;
        reference = database.getReference("baby");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null){
                  for(DataSnapshot snapshots : snapshot.getChildren()) {
                      String amka = String.valueOf(snapshots.child("amka").getValue());
                      if(babyAmka.toString().equals(amka)){
                        flag = true;
                      }
                  }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return flag;
    }



    public void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

}

