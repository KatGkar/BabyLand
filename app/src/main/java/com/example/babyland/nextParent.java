package com.example.babyland;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class nextParent extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextInputEditText nameParentTwo, surnameParentTwo, amkaParentTwo, phoneNumberParentTwo, emailAddressParentTwo,
            dateOfBirthParentTwo;
    private String  nameParentOne, surnameParentOne, amkaParentOne, phoneNumberParentOne, emailAddressParentOne, dateOfBirthParentOne,
            bloodTypeParentOne, currentUserUID;
    private String[] bloodType = {"Blood Type", "A RhD positive (A+)", "A RhD negative (A-)", "B RhD positive (B+)",
            "B RhD negative (B-)", "O RhD positive (O+)", "O RhD negative (O-)", "AB RhD positive (AB+)", "AB RhD negative (AB-)"};
    private int[] bloodImages = {R.drawable.blood_type, R.drawable.a_plus, R.drawable.a_minus,
            R.drawable.b_minus, R.drawable.b_plus, R.drawable.o_plus, R.drawable.o_minus,
            R.drawable.ab_minus, R.drawable.ab_minus};
    private CalendarView calendar;
    private Button saveButton, skipButton;
    private ImageButton calendarButton;
    private RelativeLayout infoRelativeLayout;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Spinner bloodTypeParentTwoSpinner;
    private ArrayList<Baby> kids;
    private Boolean flagUnique, flagNext;
    private Parent p1, p2, parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_parent);

        //getting extras
        Bundle extras = getIntent().getExtras();
        nameParentOne = extras.getString("name");
        surnameParentOne = extras.getString("surname");
        amkaParentOne = extras.getString("amka");
        phoneNumberParentOne = extras.getString("phoneNumber");
        emailAddressParentOne = extras.getString("email");
        dateOfBirthParentOne = extras.getString("birthDate");
        bloodTypeParentOne = extras.getString("bloodType");

        //getting ids from layout
        nameParentTwo = findViewById(R.id.nameParentTwoTextInput);
        surnameParentTwo = findViewById(R.id.surnameParentTwoTextInput);
        amkaParentTwo = findViewById(R.id.amkaParentTwoTextInput);
        phoneNumberParentTwo = findViewById(R.id.phoneNumberParentTwoTextInput);
        emailAddressParentTwo = findViewById(R.id.emailParentTwoTextInput);
        calendar = findViewById(R.id.calendarView3);
        calendarButton = findViewById(R.id.calendarButtonOnParentTwo);
        infoRelativeLayout = findViewById(R.id.relativeLayoutParentTwo);
        dateOfBirthParentTwo = findViewById(R.id.birthDateParentTwoTextInput);
        bloodTypeParentTwoSpinner = findViewById(R.id.bloodTypeParentTwo);
        saveButton = findViewById(R.id.saveButton);
        skipButton = findViewById(R.id.skipButton);

        //setting visibilities
        infoRelativeLayout.setVisibility(View.VISIBLE);
        calendar.setVisibility(View.INVISIBLE);

        //setting database
        database = FirebaseDatabase.getInstance();

        //list kids
        kids= null;

        //getting user UID from database
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //setting blood types in list
        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),bloodImages,bloodType);
        bloodTypeParentTwoSpinner.setAdapter(customAdapter);
        bloodTypeParentTwoSpinner.setOnItemSelectedListener(this);

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
                dateOfBirthParentTwo.setText(d + "/" + m + "/" + year);
                calendar.setVisibility(View.INVISIBLE);
                infoRelativeLayout.setVisibility(View.VISIBLE);
            }
        });

        //setting listener to format textView babyBirthDate
        dateOfBirthParentTwo.addTextChangedListener(new TextWatcher() {
            private String current="";
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
                            Toast.makeText(nextParent.this, "Birth year should be between 1970 and 2010", Toast.LENGTH_SHORT).show();
                        }else if(year>2010){
                            year = 2010;
                            Toast.makeText(nextParent.this, "Birth year should be between 1970 and 2010", Toast.LENGTH_SHORT).show();
                        }
                        cal.set(Calendar.YEAR, year);
                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum((Calendar.DATE)) : day;
                        clean = String.format("%02d%02d%02d", day, month, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2), clean.substring(2, 4), clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dateOfBirthParentTwo.setText(current);
                    dateOfBirthParentTwo.setSelection(sel<current.length()? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next(view);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skip(view);
            }
        });

        //checking textviews input type
        nameParentTwo.addTextChangedListener(new TextWatcher() {
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
                    flagNext = false;
                    nameParentTwo.setError("Type only letters please!!");
                }
            }
        });
        surnameParentTwo.addTextChangedListener(new TextWatcher() {
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
                    flagNext = false;
                    surnameParentTwo.setError("Type only letters please!!");
                }
            }
        });
        phoneNumberParentTwo.addTextChangedListener(new TextWatcher() {
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
                    flagNext = false;
                    phoneNumberParentTwo.setError("Type only numbers please!!");
                }
            }
        });
        amkaParentTwo.addTextChangedListener(new TextWatcher() {
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
                    flagNext = false;
                    amkaParentTwo.setError("Type only numbers please!!");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        bloodTypeParentTwoSpinner.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //check textViews and amka number
    public void next(View view) {
        flagNext = true;
        if (TextUtils.isEmpty(nameParentTwo.getText())) {
            nameParentTwo.setError("Please enter a name!");
            nameParentTwo.requestFocus();
            flagNext = false;
        }
        if (TextUtils.isEmpty(surnameParentTwo.getText())) {
            surnameParentTwo.setError("Please enter a surname!");
            surnameParentTwo.requestFocus();
            flagNext = false;
        }
        if (TextUtils.isEmpty(amkaParentTwo.getText())|| (amkaParentTwo.getText().length() != 11)) {
           amkaParentTwo.setError("Amka number should have length 11 numbers!");
           amkaParentTwo.requestFocus();
           flagNext = false;
        }
        if (TextUtils.isEmpty(phoneNumberParentTwo.getText())|| (phoneNumberParentTwo.getText().length() != 10)) {
           phoneNumberParentTwo.setError("Phone number should have length 10 numbers!");
           phoneNumberParentTwo.requestFocus();
            flagNext = false;
        }
        if (TextUtils.isEmpty(emailAddressParentTwo.getText()) || (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressParentTwo.getText()).matches())) {
           emailAddressParentTwo.setError("Please enter a valid email address!");
           emailAddressParentTwo.requestFocus();
            flagNext = false;
        }
        if(bloodTypeParentTwoSpinner.getSelectedItem().equals("Blood Type")){
            Toast.makeText(this, "Please choose a blood type!!", Toast.LENGTH_SHORT).show();
            bloodTypeParentTwoSpinner.requestFocus();
            flagNext=false;
        }
        if(!dateOfBirthParentTwo.getText().toString().matches("^[0-9]+(\\/[0-9]+)*$")) {
            flagNext=false;
            dateOfBirthParentTwo.requestFocus();
            Toast.makeText(this, "Please fill in correct birth date!", Toast.LENGTH_SHORT).show();
        }

        //check if amka number is unique
        flagUnique = true;
        reference = database.getReference("parent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null){
                    for(DataSnapshot snapshots : snapshot.getChildren()) {
                        GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {};
                        String amka = String.valueOf(snapshots.child("amka").getValue());
                        if(amkaParentTwo.getText().toString().equals(amka)){
                            flagUnique = false;
                            parent = new Parent(snapshots.getValue(t));
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

    //skip button
    public void skip(View view){
        DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        //add parent to database
                        Parent p = new Parent(nameParentOne, surnameParentOne, amkaParentOne, phoneNumberParentOne,emailAddressParentOne,dateOfBirthParentOne, bloodTypeParentOne, "00000000000",false, kids);//, UID);
                        reference = database.getReference("parent");
                        reference.child(currentUserUID).setValue(p);
                        //showMessage("Success","User created successfully!!");
                        //go to app main screen
                        Intent intent = new Intent(getApplicationContext(), mainScreenParents.class);
                        startActivity(intent);
                        finish();

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        //Do nothing
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to continue without partner? If no please fill in the blanks.").setPositiveButton("Yes", dialogClickListener2)
                .setNegativeButton("No", dialogClickListener2).show();
    }

    //check restrictions
    private void check() {
       if (!flagUnique) {
           //if amka number is unique
           DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   switch (which) {
                       case DialogInterface.BUTTON_POSITIVE:
                           //Yes button clicked
                           //add parent to database
                           p1 = new Parent(nameParentOne, surnameParentOne, amkaParentOne, phoneNumberParentOne, emailAddressParentOne, dateOfBirthParentOne, bloodTypeParentOne, parent.getAmka(), true, kids);
                           reference = FirebaseDatabase.getInstance().getReference("parent");
                           reference.child(currentUserUID).setValue(p1);
                           Intent in = new Intent(getApplicationContext(), mainScreenParents.class);
                           startActivity(in);
                           finish();

                       case DialogInterface.BUTTON_NEGATIVE:
                           //No button clicked
                           //Do nothing
                   }
               }
           };
           try {
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               builder.setMessage("Parent exists already. Do you want to continue with this parent??").setPositiveButton("Yes", dialogClickListener)
                       .setNegativeButton("No", dialogClickListener).show();
           }catch (Exception e){
               System.out.println(e.getLocalizedMessage());
           }
       } else {
           if (flagNext && flagUnique) {
               //if there are empty textBoxes show message
               //add parents to database
               p1 = new Parent(nameParentOne, surnameParentOne, amkaParentOne, phoneNumberParentOne, emailAddressParentOne, dateOfBirthParentOne, bloodTypeParentOne, amkaParentTwo.getText().toString(), true, kids);//, UID);
               p2 = new Parent(nameParentTwo.getText().toString(), surnameParentTwo.getText().toString(), amkaParentTwo.getText().toString(), phoneNumberParentTwo.getText().toString(), emailAddressParentTwo.getText().toString(), dateOfBirthParentTwo.getText().toString(), bloodTypeParentTwoSpinner.getSelectedItem().toString(), amkaParentOne, true, kids);//, null);
               //getting number of parents on database
               reference = database.getReference("parent");
               reference.child(currentUserUID).setValue(p1);
               reference.child(amkaParentTwo.getText().toString()).setValue(p2);
               Toast.makeText(this, "Info saved successfully!!", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getApplicationContext(), mainScreenParents.class);
               startActivity(intent);
           }
       }
   }

}