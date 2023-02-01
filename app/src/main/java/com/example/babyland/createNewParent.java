package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class createNewParent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText nameParentOne, surnameParentOne, amkaParentOne, phoneNumberParentOne, dateOfBirthParentOne;
    private String[] bloodType = {"Blood Type", "A RhD positive (A+)", "A RhD negative (A-)", "B RhD positive (B+)",
            "B RhD negative (B-)", "O RhD positive (O+)", "O RhD negative (O-)", "AB RhD positive (AB+)", "AB RhD negative (AB-)"};
    private int[] bloodImages = {R.drawable.blood_type, R.drawable.a_plus, R.drawable.a_minus,
            R.drawable.b_minus, R.drawable.b_plus, R.drawable.o_plus, R.drawable.o_minus,
            R.drawable.ab_minus, R.drawable.ab_minus};
    private Spinner bloodSpinner;
    private CalendarView calendar;
    private Button calendarButton, nextParentButton;
    private RelativeLayout infoRelativeLayout;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private int flagUnique;
    private Boolean flagNext;
    private String emailParentOne, currentUserUID;
    private  Parent parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_parent);

        //finding views from xml file
        nameParentOne = findViewById(R.id.nameParentOne);
        surnameParentOne = findViewById(R.id.surnameParentOne);
        amkaParentOne = findViewById(R.id.amkaParentOne);
        phoneNumberParentOne = findViewById(R.id.phoneNumberParentOne);
        calendar = findViewById(R.id.calendarView);
        calendarButton = findViewById(R.id.calendarButtonOnParentOne);
        infoRelativeLayout = findViewById(R.id.relativeLayoutParentOne);
        dateOfBirthParentOne = findViewById(R.id.birthDateParentOne);
        bloodSpinner = findViewById(R.id.bloodTypeParentOne);
        nextParentButton = findViewById(R.id.nextParentButton);

        //setting database
        database = FirebaseDatabase.getInstance();

        //getting user information
        emailParentOne = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //setting blood types in list
        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),bloodImages,bloodType);
        bloodSpinner.setAdapter(customAdapter);
        bloodSpinner.setOnItemSelectedListener(this);

        //getting current date
        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH) + 1;
        int dd = cal.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        String d = String.valueOf(dd);
        if (dd <= 9) {
            d = "0" + d;
        }
        String m = String.valueOf(mm);
        if (mm <= 9) {
            m = "0" + m;
        }
        dateOfBirthParentOne.setHint(new StringBuilder()
                .append(d).append(" ").append("/").append(m).append("/").append(yy));

        //setting visibilities
        calendar.setVisibility(View.INVISIBLE);
        infoRelativeLayout.setVisibility(View.VISIBLE);

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
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String m, d;
                month = month + 1;
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
                dateOfBirthParentOne.setText(d + "/" + m + "/" + year);
                calendar.setVisibility(View.INVISIBLE);
                infoRelativeLayout.setVisibility(View.VISIBLE);
            }
        });

        //setting listener to format textView babyBirthDate
        dateOfBirthParentOne.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "ddmmyyyy";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) {
                        sel--;
                    }
                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int month = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if(month>12){
                            month = 12;
                        }
                        cal.set(Calendar.MONTH, month);

                        //Birth year 1970-2010
                        if(year<1970){
                            year=1970;
                            Toast.makeText(createNewParent.this, "Birth year should be between 1970 and 2010", Toast.LENGTH_SHORT).show();
                        }else if(year>2010){
                            year = 2010;
                            Toast.makeText(createNewParent.this, "Birth year should be between 1970 and 2010", Toast.LENGTH_SHORT).show();
                        }
                        cal.set(Calendar.YEAR, year);
                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum((Calendar.DATE)) : day;
                        clean = String.format("%02d%02d%02d", day, month, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2), clean.substring(2, 4), clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dateOfBirthParentOne.setText(current);
                    dateOfBirthParentOne.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nextParentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextParent(view);
            }
        });

        //checking texviews correct input type
        nameParentOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString();
                if (!text.matches("^[a-zA-Zα-ωΑ-ΩίόάέύώήΈΆΊΌΎΉΏ]+$")) {
                    flagNext=false;
                    nameParentOne.setError("Type only letters please!!");
                }
            }
        });
        surnameParentOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString();
                if (!text.matches("^[a-zA-Zα-ωΑ-ΩίόάέύώήΈΆΊΌΎΉΏ]+$")) {
                    flagNext=false;
                    surnameParentOne.setError("Type only letters please!!");
                }
            }
        });
        amkaParentOne.addTextChangedListener(new TextWatcher() {
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
                    amkaParentOne.setError("Type only numbers please!!");
                }
            }
        });
    }


    //on back button pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(createNewParent.this, loginRegister.class);
        startActivity(intent);
    }


    //check textViews and amka number
    public void nextParent(View view) {
        flagNext = true;
        if (TextUtils.isEmpty(nameParentOne.getText())) {
            nameParentOne.setError("Please enter a name!");
            nameParentOne.requestFocus();
            flagNext = false;
        }
        if (TextUtils.isEmpty(surnameParentOne.getText())) {
            surnameParentOne.setError("Please enter a surname!");
            surnameParentOne.requestFocus();
            flagNext = false;
        }
        if (TextUtils.isEmpty(amkaParentOne.getText()) || (amkaParentOne.getText().length() != 11)) {
            amkaParentOne.setError("Amka number should have length 11 numbers!");
            amkaParentOne.requestFocus();
            flagNext = false;
        }
        if (TextUtils.isEmpty(phoneNumberParentOne.getText()) || (phoneNumberParentOne.getText().length() != 10)) {
            phoneNumberParentOne.setError("Phone number should have length 10 numbers!");
            phoneNumberParentOne.requestFocus();
            flagNext = false;
        }
        if (bloodSpinner.getSelectedItem().equals("Blood Type")) {
            Toast.makeText(this, "Please a choose a blood type!", Toast.LENGTH_SHORT).show();
            bloodSpinner.requestFocus();
            flagNext = false;
        }
        if(!dateOfBirthParentOne.getText().toString().matches("^[0-9]+(\\/[0-9]+)*$" )) {
            flagNext=false;
            dateOfBirthParentOne.requestFocus();
            Toast.makeText(this, "Please fill in correct birth date!", Toast.LENGTH_SHORT).show();
        }

        //check if amka number is unique
        //flagUnique = 1 --> amka number is unique
        //flagUnique = 2 --> amka number is being user by another parent (parent!=user)
        //flagUnique = 3 --> amka number is being used by another user
        flagUnique = 1;
        reference = database.getReference("parent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {};
                        if (amkaParentOne.getText().toString().equals(snapshots.getValue(t).getAmka())) {
                            if (snapshots.getKey().length() == 11) {
                                flagUnique = 3;
                                parent = snapshots.getValue(t);
                            } else {
                                flagUnique = 2;
                            }
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
    private void check() {
        if (flagNext && flagUnique == 1) {
            //there is no other user with this amka number
            Intent myIntent = new Intent(this, nextParent.class);
            myIntent.putExtra("name", nameParentOne.getText().toString());
            myIntent.putExtra("surname", surnameParentOne.getText().toString());
            myIntent.putExtra("amka", amkaParentOne.getText().toString());
            myIntent.putExtra("phoneNumber", phoneNumberParentOne.getText().toString());
            myIntent.putExtra("email", emailParentOne);
            myIntent.putExtra("birthDate", dateOfBirthParentOne.getText().toString());
            myIntent.putExtra("bloodType", bloodSpinner.getSelectedItem().toString());
            this.startActivity(myIntent);
        } else if (flagUnique == 2) {
            //there is another user with this amka
            amkaParentOne.setError("Amka number should be unique! Please try again");
            amkaParentOne.requestFocus();
        } else if(flagUnique == 3){
            //there is a parent with this amka number but he is not a user yet
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            //add parent to database
                            if(parent.getPartner()){
                                reference = database.getReference("parent");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot != null) {
                                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                                String amka = String.valueOf(snapshots.child("amka").getValue());
                                                GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {};
                                                if (amkaParentOne.getText().toString().equals(amka)) {
                                                    reference.child(amka).removeValue();
                                                    reference.child(currentUserUID).setValue(parent);
                                                    Toast.makeText(createNewParent.this, "Info saved successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            Intent intent = new Intent(createNewParent.this, mainScreenParents.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else{
                                Intent intent = new Intent(createNewParent.this, nextParent.class);
                                intent.putExtra("name", parent.getName());
                                intent.putExtra("surname", parent.getSurname());
                                intent.putExtra("amka", parent.getAmka());
                                intent.putExtra("phoneNumber", parent.getPhoneNumber());
                                intent.putExtra("email", parent.getEmail());
                                intent.putExtra("birthDate", parent.getDateOfBirth());
                                intent.putExtra("bloodType", parent.getBloodType());
                                startActivity(intent);
                            }

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            //Do nothing
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Parent exists already. Do you continue with this parent??").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}