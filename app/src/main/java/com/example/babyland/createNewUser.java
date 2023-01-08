package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private EditText nameParentOne, surnameParentOne, amkaParentOne, phoneNumberParentOne, dateOfBirthParentOne;
    public String[] bloodType = {"Blood Type", "A RhD positive (A+)", "A RhD negative (A-)", "B RhD positive (B+)",
            "B RhD negative (B-)", "O RhD positive (O+)", "O RhD negative (O-)", "AB RhD positive (AB+)", "AB RhD negative (AB-)"};

    private Spinner blood;
    CalendarView calendar;
    Button calendarButton;
    RelativeLayout infoRelativeLayout;
    FirebaseDatabase database;
    DatabaseReference reference;
    int flagUnique;
    ImageView bab;
    private String emailParentOne;


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
        calendar = findViewById(R.id.calendarView);
        calendarButton = findViewById(R.id.calendarButtonOnParentOne);
        infoRelativeLayout = findViewById(R.id.relativeLayoutParentOne);
        dateOfBirthParentOne = findViewById(R.id.birthDateParentOne);
        blood = findViewById(R.id.bloodTypeParentOne);
        bab = findViewById(R.id.imageView2);

        bab.setImageResource(R.drawable.baby_girl);

        //setting database
        database = FirebaseDatabase.getInstance();
        emailParentOne = FirebaseAuth.getInstance().getCurrentUser().getEmail();


        //setting design
        userProfile.setPaintFlags(userProfile.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //setting blood types in list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodType);
        blood.setAdapter(adapter);

        //setting hint in babyBirthDate
        //getting current date
        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        String d = String.valueOf(dd);
        if (dd <= 9) {
            d = "0" + d;
        }
        if (mm < 12) {
            mm++;
        }
        String m = String.valueOf(mm);
        if (mm <= 9) {
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
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String m = "0";
                String d = "0";
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    //date1 is selected date
                    Date date1 = sdf.parse(dayOfMonth + "/" + month + "/" + year);
                    int da = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    int mo = Calendar.getInstance().get(Calendar.MONTH);
                    int ye = Calendar.getInstance().get(Calendar.YEAR);
                    //date2 is date now
                    Date date2 = sdf.parse(da + "/" + mo + "/" + ye);
                    if (date1.compareTo(date2) > 0) {
                        //if date selected is after date now
                        mo++;
                        if (mo == 13) {
                            mo = 12;
                        }
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
                        year = Calendar.getInstance().get(Calendar.YEAR);

                    } else {
                        //if date selected is now or if date selected is before date now
                        // month++;
                       /* if(month == 13){
                            month = 12;
                        }*/
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
                    showMessage("Watch out!!", "Something went wrong. Please try again later!");
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
                    String d = "0";
                    String m = "0";
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

                       /* if(month>12){
                            month = 12;
                        }
                        cal.set(Calendar.MONTH, month);

                        year = (year<1970)?1970:(year>2023)?2022:year;
                        cal.set(Calendar.YEAR, year);
*/
                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum((Calendar.DATE)) : day;
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                            //date1 is selected date
                            Date date1 = sdf.parse(day + "/" + month + "/" + year);
                            int da = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                            int mo = Calendar.getInstance().get(Calendar.MONTH);
                            int ye = Calendar.getInstance().get(Calendar.YEAR);
                            //date2 is date now
                            Date date2 = sdf.parse(da + "/" + mo + "/" + ye);
                            if (date1.compareTo(date2) > 0) {
                                //if date selected is after date now
                                mo++;
                                if (mo == 13) {
                                    mo = 12;
                                }
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
                                day = Integer.parseInt(d);
                                month = Integer.parseInt(m);
                                year = Calendar.getInstance().get(Calendar.YEAR);

                            } else {
                                //if date selected is now or if date selected is before date now
                                //month++;
                                if (month == 13) {
                                    month = 12;
                                }
                                if (month <= 9) {
                                    m = "0" + month;
                                } else {
                                    m = String.valueOf(month);
                                }
                                if (day <= 9) {
                                    d = "0" + day;
                                } else {
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

    }


    //on back button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(createNewUser.this, LoginRegister.class);
        startActivity(intent);
    }

    //showing messages to users
    public void showMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

    Boolean next;

    public void nextParent(View view) {
        next = true;
        if (TextUtils.isEmpty(nameParentOne.getText())) {
            nameParentOne.setError("Please enter a name!");
            next = false;
        }
        if (TextUtils.isEmpty(surnameParentOne.getText())) {
            surnameParentOne.setError("Please enter a surname!");
            next = false;
        }
        if (TextUtils.isEmpty(amkaParentOne.getText()) || (amkaParentOne.getText().length() != 11)) {
            amkaParentOne.setError("Amka should have length 11 numbers!");
            next = false;
        }
        if (TextUtils.isEmpty(phoneNumberParentOne.getText()) || (phoneNumberParentOne.getText().length() != 10)) {
            phoneNumberParentOne.setError("Phone number should have length 11 numbers!");
            next = false;
        }
        if (blood.getSelectedItem().equals("Blood Type")) {
            ((TextView) blood.getSelectedView()).setError("Please choose a blood type!");
            next = false;
        }
        findIfUnique();

    }

    private void findIfUnique() {
        flagUnique = 1;
        reference = database.getReference("parent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        String amka = String.valueOf(snapshots.child("amka").getValue());
                        if (amkaParentOne.getText().toString().equals(amka)) {
                            if (snapshots.getKey().length() == 11) {
                                flagUnique = 3;
                            } else {
                                flagUnique = 2;
                            }
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

    private void goToNext() {
        if (next && flagUnique == 1) {
            //next screen
            Intent myIntent = new Intent(this, nextParent.class);
            myIntent.putExtra("name", nameParentOne.getText().toString());
            myIntent.putExtra("surname", surnameParentOne.getText().toString());
            myIntent.putExtra("amka", amkaParentOne.getText().toString());
            myIntent.putExtra("phoneNumber", phoneNumberParentOne.getText().toString());
            myIntent.putExtra("email", emailParentOne);
            myIntent.putExtra("birthDate", dateOfBirthParentOne.getText().toString());
            myIntent.putExtra("bloodType", blood.getSelectedItem().toString());
            this.startActivity(myIntent);
        } else if (flagUnique == 2) {
            amkaParentOne.setError("Amka should be unique");
            amkaParentOne.requestFocus();
        } else {
            reference = database.getReference("parent");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null) {
                        for (DataSnapshot snapshots : snapshot.getChildren()) {
                            String amka = String.valueOf(snapshots.child("amka").getValue());
                            if (amkaParentOne.getText().toString().equals(amka)) {

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}