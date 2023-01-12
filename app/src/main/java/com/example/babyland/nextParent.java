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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class nextParent extends AppCompatActivity{

    private TextView userProfile;
    private EditText nameParentTwo, surnameParentTwo, amkaParentTwo, phoneNumberParentTwo, emailAddressParentTwo,
            dateOfBirthParentTwo;
    public String  nameParentOne, surnameParentOne, amkaParentOne, phoneNumberParentOne, emailAddressParentOne, dateOfBirthParentOne, bloodTypeParentOne;
    public String[] bloodType = {"A RhD positive (A+)", "A RhD negative (A-)", "B RhD positive (B+)",
            "B RhD negative (B-)", "O RhD positive (O+)", "O RhD negative (O-)", "AB RhD positive (AB+)", "AB RhD negative (AB-)"};
    String UID;
    CalendarView calendar;
    Button calendarButton, yesButton, noButton;
    RelativeLayout infoRelativeLayout, messageRelativeLayout;
    FirebaseDatabase database;
    DatabaseReference reference;
    int parentNumber;
    private Spinner bloodTypeParentTwo;
    private ArrayList<Baby> kids;
    Boolean flagUnique;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_parent);

        //setting database
        database = FirebaseDatabase.getInstance();
        //list kids
        kids= null;

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
        userProfile = findViewById(R.id.profileUser2);
        nameParentTwo = findViewById(R.id.nameParentTwo);
        surnameParentTwo = findViewById(R.id.surnameParentTwo);
        amkaParentTwo = findViewById(R.id.amkaParentTwo);
        phoneNumberParentTwo = findViewById(R.id.phoneNumberParentTwo);
        emailAddressParentTwo = findViewById(R.id.emailParetntTwo);
        calendar = findViewById(R.id.calendarView3);
        calendarButton = findViewById(R.id.calendarButtonOnParentTwo);
        infoRelativeLayout = findViewById(R.id.relativeLayoutParentTwo);
        dateOfBirthParentTwo = findViewById(R.id.birthDateParentTwo);
        bloodTypeParentTwo = findViewById(R.id.bloodTypeParentTwo);
        messageRelativeLayout = findViewById(R.id.messageRelativeLayout);
        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);

        //setting visibilities
        infoRelativeLayout.setVisibility(View.VISIBLE);
        messageRelativeLayout.setVisibility(View.INVISIBLE);
        calendar.setVisibility(View.INVISIBLE);

        //getting user UID from database
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //setting design
        userProfile.setPaintFlags(userProfile.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //setting blood types in list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodType);
        bloodTypeParentTwo.setAdapter(adapter);

        //setting hint in babyBirthDate
        //getting current date
        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        dateOfBirthParentTwo.setHint(new StringBuilder()
                .append(dd).append(" ").append("/").append(mm + 1).append("/")
                .append(yy));



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
                String m="1";
                String d="1";
                if(month<=9){
                    m = "0"+ month;
                }
                if(dayOfMonth<=9){
                    d = "0"+dayOfMonth;
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

                        year = (year<1970)?1970:(year>2010)?2010:year;
                        cal.set(Calendar.YEAR, year);

                        day = (day>cal.getActualMaximum(Calendar.DATE))?cal.getActualMaximum((Calendar.DATE)):day;
                        clean = String.format("%02d%02d%02d", day, month, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0,2), clean.substring(2,4), clean.substring(4,8));

                    sel = sel<0?0:sel;
                    current = clean;
                    dateOfBirthParentTwo.setText(current);
                    dateOfBirthParentTwo.setSelection(sel<current.length()? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }


    Parent u1;
    Parent u2;
    Boolean next;
    //button save and next
    public void next(View view) {
        next = true;
        if (TextUtils.isEmpty(nameParentTwo.getText())) {
            nameParentTwo.setError("Please enter a name!");
            next = false;
        }
        if (TextUtils.isEmpty(surnameParentTwo.getText())) {
            surnameParentTwo.setError("Please enter a surname!");
            next = false;
        }
        if (TextUtils.isEmpty(amkaParentTwo.getText())|| (amkaParentTwo.getText().length() != 11)) {
           amkaParentTwo.setError("Amka should have length 11 numbers!");
            next = false;
        }
        if (TextUtils.isEmpty(phoneNumberParentTwo.getText())|| (phoneNumberParentTwo.getText().length() != 10)) {
           phoneNumberParentTwo.setError("Phone number should have length 10 numbers!");
            next = false;
        }
        if (TextUtils.isEmpty(emailAddressParentTwo.getText()) || (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressParentTwo.getText()).matches())) {
           emailAddressParentTwo.setError("Please enter a valid email address!");
            next = false;
        }
        if(bloodTypeParentTwo.getSelectedItem().equals("Blood Type")){
            ((TextView)bloodTypeParentTwo.getSelectedView()).setError("Please choose a blood type!");
            next=false;
        }

        findIfUnique();
    }


    //button skip and next
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
                        reference.child(UID).setValue(p);
                        Toast.makeText(nextParent.this, "This is my Toast message!", Toast.LENGTH_LONG).show();
                        //showMessage("Success","User created successfully!!");
                        //go to app main screen
                        Intent intent = new Intent(getApplicationContext(), MainScreenParents.class);
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


    Parent parent;
    private void findIfUnique() {
        flagUnique = true;
        reference = database.getReference("parent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null){
                    for(DataSnapshot snapshots : snapshot.getChildren()) {
                        String amka = String.valueOf(snapshots.child("amka").getValue());
                        if(amkaParentTwo.getText().toString().equals(amka)){
                            flagUnique = false;
                            parent = new Parent(snapshots.getValue(Parent.class));
                        }
                    }
                    goToNext();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

   public void goToNext() {
       if (!flagUnique) {
           DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   switch (which) {
                       case DialogInterface.BUTTON_POSITIVE:
                           //Yes button clicked
                           //add parent to database
                           u1 = new Parent(nameParentOne, surnameParentOne, amkaParentOne, phoneNumberParentOne, emailAddressParentOne, dateOfBirthParentOne, bloodTypeParentOne, parent.getAmka(), true, kids);//, UID);
                           reference = FirebaseDatabase.getInstance().getReference("parent");
                           reference.child(UID).setValue(u1);
                           Intent in = new Intent(getApplicationContext(), MainScreenParents.class);
                           startActivity(in);
                           finish();

                       case DialogInterface.BUTTON_NEGATIVE:
                           //No button clicked
                           //Do nothing
                   }
               }
           };
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setMessage("Parent exists already continue with this parent??").setPositiveButton("Yes", dialogClickListener)
                   .setNegativeButton("No", dialogClickListener).show();
       } else {
           if (next && flagUnique) {
               //if there are empty textBoxes show message
               //add parents to database
               u1 = new Parent(nameParentOne, surnameParentOne, amkaParentOne, phoneNumberParentOne, emailAddressParentOne, dateOfBirthParentOne, bloodTypeParentOne, amkaParentTwo.getText().toString(), true, kids);//, UID);
               u2 = new Parent(nameParentTwo.getText().toString(), surnameParentTwo.getText().toString(), amkaParentTwo.getText().toString(), phoneNumberParentTwo.getText().toString(), emailAddressParentTwo.getText().toString(), dateOfBirthParentTwo.getText().toString(), bloodTypeParentTwo.getSelectedItem().toString(), amkaParentOne, true, kids);//, null);
               //getting number of parents on database
               reference = database.getReference("parent");
               reference.child(UID).setValue(u1);
               reference.child(amkaParentTwo.getText().toString()).setValue(u2);
               //go to app main screen
               Toast.makeText(this, "User created successfully!!", Toast.LENGTH_SHORT).show();
               //go to app main screen
               Intent intent = new Intent(getApplicationContext(), MainScreenParents.class);
               startActivity(intent);
           }
       }
   }




    //getting number of parents in database
    private int getParentNumber() {
        reference = database.getReference("parent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parentNumber= (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return parentNumber+1;
    }

}