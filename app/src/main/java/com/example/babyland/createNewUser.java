package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class createNewUser extends AppCompatActivity {

    private TextView userProfile;
    private String UID;
    private EditText nameParentOne, surnameParentOne, amkaParentOne, phoneNumberParentOne, emailAddressParentOne, dateOfBirthParentOne;
    public String[] bloodType = {"Blood Type", "A RhD positive (A+)","A RhD negative (A-)", "B RhD positive (B+)",
            "B RhD negative (B-)", "O RhD positive (O+)", "O RhD negative (O-)", "AB RhD positive (AB+)", "AB RhD negative (AB-)"};

    private Spinner blood;
    CalendarView calendar;
    Button calendarButton;
    RelativeLayout infoRelativeLayout;
    FirebaseDatabase database;
    DatabaseReference reference;
    Boolean flagUnique;
    ImageView bab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);

        //finding views
        userProfile = findViewById(R.id.profileUser);
        nameParentOne = findViewById(R.id.nameParentOne);
        surnameParentOne = findViewById(R.id.surnameParentOne);
        amkaParentOne = findViewById(R.id.amkaParentOne);
        phoneNumberParentOne = findViewById(R.id.phoneNumberParentOne);
        emailAddressParentOne = findViewById(R.id.emailParetntOne);
        calendar = findViewById(R.id.calendarView);
        calendarButton = findViewById(R.id.calendarButtonOnParentOne);
        infoRelativeLayout = findViewById(R.id.relativeLayoutParentOne);
        dateOfBirthParentOne = findViewById(R.id.birthDateParentOne);
        blood = findViewById(R.id.bloodTypeParentOne);
        bab = findViewById(R.id.imageView2);


        bab.setImageResource(R.drawable.baby_girl);
        //setting database
        database = FirebaseDatabase.getInstance();

        //getting user UID from database
        //UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //setting desing
        userProfile.setPaintFlags(userProfile.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);



        //setting blood types in list
        Spinner dropdown = findViewById(R.id.bloodTypeParentOne);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodType);
        dropdown.setAdapter(adapter);

        //setting hint in babyBirthDate
        //getting current date
        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        String d = String.valueOf(dd);
        if(dd<=9){
            d = "0" + d;
        }
        if(mm<12){
            mm++;
        }
        String m = String.valueOf(mm);
        if(mm<=9){
            m = "0" + m;
        }
        dateOfBirthParentOne.setHint(new StringBuilder()
                .append(d).append(" ").append("/").append(m).append("/")
                .append(yy));

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
                dateOfBirthParentOne.setText(d + "/" + m + "/" + year);
                calendar.setVisibility(View.INVISIBLE);
                infoRelativeLayout.setVisibility(View.VISIBLE);
            }
        });

        //setting listener to format textView babyBirthDate
        dateOfBirthParentOne.addTextChangedListener(new TextWatcher() {
            private String current="";
            private String ddmmyyyy = "ddmmyyyy";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    String d="0";
                    String m="0";
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

                       /* if(month>12){
                            month = 12;
                        }
                        cal.set(Calendar.MONTH, month);

                        year = (year<1970)?1970:(year>2023)?2022:year;
                        cal.set(Calendar.YEAR, year);
*/
                        day = (day>cal.getActualMaximum(Calendar.DATE))?cal.getActualMaximum((Calendar.DATE)):day;
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                            //date1 is selected date
                            Date date1 = sdf.parse(day + "/" + month + "/" + year);
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
                                day = Integer.parseInt(d);
                                month = Integer.parseInt(m);
                                year = Calendar.getInstance().get(Calendar.YEAR);

                            }else {
                                //if date selected is now or if date selected is before date now
                                month++;
                                if(month==13){
                                    month=12;
                                }
                                if(month<=9){
                                    m = "0"+ month;
                                }else{
                                    m = String.valueOf(month);
                                }
                                if(day<=9){
                                    d = "0"+day;
                                }else{
                                    d = String.valueOf(day);
                                }
                                day = Integer.parseInt(d);
                                month = Integer.parseInt(m);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage("Watch out!!", "Something went wrong. Please try again later!");
                        }



                        clean = String.format("%02d%02d%02d", day, month, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0,2), clean.substring(2,4), clean.substring(4,8));

                    sel = sel<0?0:sel;
                    current = clean;
                    dateOfBirthParentOne.setText(current);
                    dateOfBirthParentOne.setSelection(sel<current.length()? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    //showing messages to users
    public void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

    Boolean next = true;

    public void nextParent(View view){
        next = true;
        if(TextUtils.isEmpty(nameParentOne.getText())){
            showMessage("Warning!", "Field Name is empty!!");
            next=false;
        }
        if(TextUtils.isEmpty(surnameParentOne.getText())){
            showMessage("Warning!", "Field Surname is empty!!");
            next=false;
        }
        if(TextUtils.isEmpty(amkaParentOne.getText())){
            showMessage("Warning!", "Field Amka is empty!!");
            next=false;
        }else if (amkaParentOne.getText().length() != 11){
            showMessage("Warning!", "Field Amka is not correct!!");
            next=false;
        }
        if(TextUtils.isEmpty(phoneNumberParentOne.getText())){
            showMessage("Warning!", "Field Phone Number is empty!!");
            next=false;
        }else if(phoneNumberParentOne.getText().length() != 10){
            showMessage("Warning!", "Field Phone Number is not correct!!");
            next=false;
        }
        if(TextUtils.isEmpty(emailAddressParentOne.getText())){
            showMessage("Warning!", "Field Email is empty!!");
            next=false;
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressParentOne.getText()).matches()){
            showMessage("Warning!", "Field Email does not have correct format!!");
            next=false;
        }
        if(blood.getSelectedItem().equals("Blood Type")){
            showMessage("Warning!", "Field Blood Type does not have an answer!!");
            next=false;
        }



        flagUnique = findIfUnique();

        if(next && flagUnique){
            //next screen
            Intent myIntent = new Intent(this, nextParent.class);
            myIntent.putExtra("name",nameParentOne.getText().toString()) ;
            myIntent.putExtra("surname", surnameParentOne.getText().toString());
            myIntent.putExtra("amka", amkaParentOne.getText().toString());
            myIntent.putExtra("phoneNumber", phoneNumberParentOne.getText().toString());
            myIntent.putExtra("email", emailAddressParentOne.getText().toString());
            myIntent.putExtra("birthDate", dateOfBirthParentOne.getText().toString());
            myIntent.putExtra("bloodType", blood.getSelectedItem().toString());
            myIntent.putExtra("UID", UID);
            this.startActivity(myIntent);
        }

    }

    Boolean flag;
    private Boolean findIfUnique() {
        flag = true;
        reference = database.getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null){
                    for(DataSnapshot snapshots : snapshot.getChildren()) {
                        String amka = String.valueOf(snapshots.child("amka").getValue());
                        if(amkaParentOne.toString().equals(amka)){
                            flag = false;
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

}