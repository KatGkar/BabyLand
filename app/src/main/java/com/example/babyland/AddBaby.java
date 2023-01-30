package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddBaby extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText babyBirthDate, babyNameTextView, babyAmkaTextView, babyBirthPlaceTextView;
    private RadioButton babyBoy, babyGirl;
    private String[] bloodType = {"Blood Type", "A RhD positive (A+)","A RhD negative (A-)", "B RhD positive (B+)",
            "B RhD negative (B-)", "O RhD positive (O+)", "O RhD negative (O-)", "AB RhD positive (AB+)", "AB RhD negative (AB-)"};
    private int[] bloodImages = {R.drawable.blood_type_black, R.drawable.a_plus, R.drawable.a_minus,
            R.drawable.b_minus, R.drawable.b_plus, R.drawable.o_plus, R.drawable.o_minus,
            R.drawable.ab_minus, R.drawable.ab_minus};
    private Spinner babyBloodTypeSpinner;
    private RadioGroup radioGroup;
    private CalendarView calendar;
    private Button calendarButton;
    private RelativeLayout infoRelativeLayout;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Boolean flagUnique, flagNext;
    private String sex;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_baby);

        //finding views on xml file
        babyNameTextView = findViewById(R.id.babyNameText);
        babyAmkaTextView = findViewById(R.id.babyAmkaText);
        babyBirthPlaceTextView = findViewById(R.id.babyBirthPlaceText);
        babyBoy = findViewById(R.id.babyBoy);
        babyGirl = findViewById(R.id.babyGirl);
        babyBirthDate = findViewById(R.id.babyBirthDate);
        babyBloodTypeSpinner = findViewById(R.id.babyBloodTypeSpinner);
        calendar = findViewById(R.id.calendarView2);
        calendarButton = findViewById(R.id.calendarButton);
        infoRelativeLayout = findViewById(R.id.infoRelativeLayout);
        radioGroup = findViewById(R.id.radioGroup);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewAddBaby);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_add);

        //setting database
        database = FirebaseDatabase.getInstance();

        //setting default sex
        babyGirl.setChecked(true);

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
        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),bloodImages,bloodType);
        babyBloodTypeSpinner.setAdapter(customAdapter);
        babyBloodTypeSpinner.setOnItemSelectedListener(this);

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

                        year = (year<2019)?2019:(year>2023)?2022:year;
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
                        Intent intent = new Intent(AddBaby.this, MainScreenParents.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_add:
                        return true;
                    case R.id.navigation_account:
                        settingsButton();
                        return true;
                }
                return false;
            }
        });

        //textViews for correct input type
        babyNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString();
                if (!text.matches("^[a-zA-Zα-ωΑ-ΩίόάέύώήΈΆΊΌΎΉΏ ]+$")) {
                    flagNext=false;
                    babyNameTextView.setError("Only letters please!!");
                }
            }
        });
        babyBirthPlaceTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString();
                if (!text.matches("^[a-zA-Zα-ωΑ-ΩίόάέύώήΈΆΊΌΎΉΏ ]+$")) {
                    flagNext=false;
                    babyBirthPlaceTextView.setError("Only letters please!!");
                }
            }
        });
        babyAmkaTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString();
                if (!text.matches("^[0-9]+$")) {
                    flagNext=false;
                    babyAmkaTextView.setError("Only numbers please!!");
                }
            }
        });

    }

    //on resume page
    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.home);
        super.onResume();
    }

    //on back pressed
    @Override
    public void onBackPressed() {
        if(calendar.getVisibility() == View.VISIBLE){
            calendar.setVisibility(View.INVISIBLE);
            infoRelativeLayout.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //go to settings page
    private void settingsButton(){
        Intent intent = new Intent(AddBaby.this, UserAccount.class);
        intent.putExtra("user", "parent");
        startActivity(intent);
    }
    //check restrictions and add new baby or show error messages
    public void addNewBaby(View view){
        flagNext=true;
        if(babyGirl.isChecked()) {
            sex= "GIRL";
        }else if(babyBoy.isChecked()){
            sex ="BOY";
        }
        if(TextUtils.isEmpty(babyNameTextView.getText())) {
            babyNameTextView.setError("Enter a name please!");
            babyNameTextView.requestFocus();
            flagNext=false;
        }
        if(!(babyAmkaTextView.getText().length() == 11) || (TextUtils.isEmpty(babyAmkaTextView.getText()))) {
            babyAmkaTextView.setError("Amka length must be 11 numbers!");
            babyAmkaTextView.requestFocus();
            flagNext=false;
        }
        if(TextUtils.isEmpty(babyBirthPlaceTextView.getText())){
            babyBirthPlaceTextView.setError("Please enter a birth place!");
            babyBirthPlaceTextView.requestFocus();
            flagNext=false;
        }
        if(babyBloodTypeSpinner.getSelectedItem().equals("Blood Type")){
            Toast.makeText(this, "Please a choose a blood type!", Toast.LENGTH_SHORT).show();
            babyBloodTypeSpinner.requestFocus();
            flagNext=false;
        }

        //find if amka number is unique
        flagUnique = true;
        reference = database.getReference("baby");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        String UID = snapshots.getKey();
                        if (UID.equals(babyAmkaTextView.getText().toString())) {
                            flagUnique = false;
                        }
                    }
                    check();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //check if restrictions are met
    private void check(){
        if(flagNext && flagUnique) {
            Intent myIntent = new Intent(this, FamilyHistoryInput.class);
            myIntent.putExtra("sex", sex);
            myIntent.putExtra("name", babyNameTextView.getText().toString());
            myIntent.putExtra("birthDate", babyBirthDate.getText().toString());
            myIntent.putExtra("amka", babyAmkaTextView.getText().toString());
            myIntent.putExtra("birthPlace", babyBirthPlaceTextView.getText().toString());
            myIntent.putExtra("bloodType", babyBloodTypeSpinner.getSelectedItem().toString());
            this.startActivity(myIntent);
            finish();
        }else if(!flagUnique) {
            babyAmkaTextView.setError("Amka should be unique");
        }
    }

}

