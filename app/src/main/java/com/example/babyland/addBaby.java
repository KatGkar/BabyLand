package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class addBaby extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextInputEditText babyBirthDate, babyNameTextView, babyAmkaTextView, babyBirthPlaceTextView;
    private RadioButton babyBoy, babyGirl;
    private String[] bloodType = {"Blood Type", "A RhD positive (A+)","A RhD negative (A-)", "B RhD positive (B+)",
            "B RhD negative (B-)", "O RhD positive (O+)", "O RhD negative (O-)", "AB RhD positive (AB+)", "AB RhD negative (AB-)"};
    private int[] bloodImages = {R.drawable.blood_type, R.drawable.a_plus, R.drawable.a_minus,
            R.drawable.b_minus, R.drawable.b_plus, R.drawable.o_plus, R.drawable.o_minus,
            R.drawable.ab_minus, R.drawable.ab_minus};
    private Spinner babyBloodTypeSpinner;
    private CalendarView calendar;
    private ImageButton calendarButton;
    private RelativeLayout infoRelativeLayout;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Boolean flagUnique, flagNext;
    private String sex, d, m;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_baby);

        //finding views on xml file
        babyNameTextView = findViewById(R.id.babyNameTextInput);
        babyAmkaTextView = findViewById(R.id.babyAmkaTextInput);
        babyBirthPlaceTextView = findViewById(R.id.babyBirthPlaceTextInput);
        babyBoy = findViewById(R.id.babyBoy);
        babyGirl = findViewById(R.id.babyGirl);
        babyBirthDate = findViewById(R.id.babyBirthDateTextInput);
        babyBloodTypeSpinner = findViewById(R.id.babyBloodTypeSpinner);
        calendar = findViewById(R.id.calendarView2);
        calendarButton = findViewById(R.id.calendarButton);
        infoRelativeLayout = findViewById(R.id.infoRelativeLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewAddBaby);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_add);

        //setting database
        database = FirebaseDatabase.getInstance();

        //setting default sex
        babyGirl.setChecked(true);


        //setting visibilities
        calendar.setVisibility(View.INVISIBLE);
        infoRelativeLayout.setVisibility(View.VISIBLE);

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
            }
        });

        //getting date of the calendar
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                String m = "0";
                String d = "0";
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    //date1 is selected date
                    month = month+1;
                    Date date1 = sdf.parse(dayOfMonth + "/" + month + "/" + year);
                    int da = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    int mo = Calendar.getInstance().get(Calendar.MONTH) + 1;
                    int ye = Calendar.getInstance().get(Calendar.YEAR);
                    //date2 is date now
                    Date date2 = sdf.parse(da + "/" + mo + "/" + ye);
                    if (date1.compareTo(date2) > 0) {
                        //if dateSelected is after dateNow
                        if (mo <= 9) {
                            m = "0" + mo;
                        } else {
                            m = String.valueOf(mo);
                        }
                        if (da <= 9) {
                            d = "0" + da;
                        } else {
                            d = String.valueOf(da);
                        }
                    } else {
                        //if date selected is now or if date selected is before date now
                        if (month <= 9) {
                            m = "0" + month;
                        } else {
                            m = String.valueOf(month);
                        }
                        if (dayOfMonth <= 9) {
                            d = "0" + dayOfMonth;
                        } else {
                            d = String.valueOf(dayOfMonth);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                babyBirthDate.setText(d + "/" + m + "/" + String.valueOf(year));
                calendar.setVisibility(View.INVISIBLE);
                infoRelativeLayout.setVisibility(View.VISIBLE);
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

                        if(year<2019){
                            year=2019;
                            Toast.makeText(addBaby.this, "Birth year should be between 2019 and 2023", Toast.LENGTH_SHORT).show();
                        }else if(year>2023){
                            year = 2023;
                            Toast.makeText(addBaby.this, "Birth year should be between 2019 and 2023", Toast.LENGTH_SHORT).show();
                        }
                        year = (year<2019)?2019:(year>2023)?2023:year;
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
                        Intent intent = new Intent(addBaby.this, mainScreenParents.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_add:
                        return true;
                    case R.id.navigation_account:
                        Intent intent1 = new Intent(addBaby.this, userAccount.class);
                        intent1.putExtra("user", "parent");
                        startActivity(intent1);
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
                    babyNameTextView.setError("Type only letters please!!");
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
                    babyBirthPlaceTextView.setError("Type only letters please!!");
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
                    babyAmkaTextView.setError("Type only numbers please!!");
                }
            }
        });

    }

    //on resume page
    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_add);
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
        if(!babyBirthDate.getText().toString().matches("^[0-9]+(\\/[0-9]+)*$")) {
            flagNext=false;
            babyBirthDate.requestFocus();
            Toast.makeText(this, "Please fill in correct birth date!", Toast.LENGTH_SHORT).show();
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
            Intent myIntent = new Intent(this, familyHistoryInput.class);
            myIntent.putExtra("sex", sex);
            myIntent.putExtra("name", babyNameTextView.getText().toString());
            myIntent.putExtra("birthDate", babyBirthDate.getText().toString());
            myIntent.putExtra("amka", babyAmkaTextView.getText().toString());
            myIntent.putExtra("birthPlace", babyBirthPlaceTextView.getText().toString());
            myIntent.putExtra("bloodType", babyBloodTypeSpinner.getSelectedItem().toString());
            this.startActivity(myIntent);
            finish();
        }else if(!flagUnique) {
            babyAmkaTextView.setError("Amka should be unique! Please try again");
        }
    }

}

