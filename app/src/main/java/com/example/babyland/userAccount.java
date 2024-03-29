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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class userAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextView userFullnameTextView, userEmailTextView, userPhoneNumberTextView, userKidsTextView, userBirthDateTextView,
                    userBloodTypeTextView, userAmkaTextView, coParentFullNameTextView,
                    coParentEmailTextView, coParentPhoneNumberTextView, coParentAmkaTextView, coParentBirthDateTextView,
                    coParentBloodTypeTextView;
    private TextInputEditText coParentAmkaEditText, coParentEmailEditText, coParentPhoneNumberEditText, coParentBirthDateEditText,
            newPasswordEditText, newPasswordVerificationEditText, coParentNameEditText, coParentSurnameEditText, emailEditText,
            nameEditText, surnameEditText, amkaEditText, phoneNumberEditText, birthDateEditText;
    private String[] bloodType = { "A RhD positive (A+)", "A RhD negative (A-)", "B RhD positive (B+)",
            "B RhD negative (B-)", "O RhD positive (O+)", "O RhD negative (O-)", "AB RhD positive (AB+)", "AB RhD negative (AB-)"};
    private int[] bloodImages = {R.drawable.a_plus, R.drawable.a_minus,
            R.drawable.b_minus, R.drawable.b_plus, R.drawable.o_plus, R.drawable.o_minus,
            R.drawable.ab_minus, R.drawable.ab_minus};
    private BottomNavigationView bottomNavigationView;
    private String currentUserUID, userType,userUID = "", provider=null, cal = "none";
    private Switch userPartnerSwitch;
    private TextInputLayout userBirthTextInputLayout;
    private FirebaseDatabase database;
    private DatabaseReference reference1, reference2, reference3;
    private Parent parent = null, coParent=null;
    private Doctor doctor = null;
    private ImageButton calendarCoParentButton, calendarButton;
    private Button updateUserInfoButton, updateButton, deleteButton, changeEmailButton, showEmailButton,
            showPasswordButton, changePasswordButton, coParentButton, coParentAddButton, coParentDeleteButton,
            coParentSaveButton;
    private RelativeLayout viewUserInfoRelativeLayout, updateUserInfoRelativeLayout, changeEmailRelativeLayout,
                            changePasswordRelativeLayout, coParentRelativeLayout, coParentAddRelativeLayout,
                            coParentExistsRelativeLayout, noCoParentRelativeLayout;
    private CalendarView calendarView;
    private Spinner bloodTypeSpinner, coParentBloodTypeSpinner;
    private int position=0;
    private Boolean next, flagUnique, flagOnce=false, flagOnce1=false, flagNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        //getting extras
        Bundle extras = getIntent().getExtras();
        userType = extras.getString("user");

        //getting views from xml file
        userFullnameTextView = findViewById(R.id.userFullnameTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userPhoneNumberTextView = findViewById(R.id.userPhoneNumberTextView);
        userKidsTextView = findViewById(R.id.userKidsTextView);
        userBirthDateTextView = findViewById(R.id.userBirthDateTextView);
        userBloodTypeTextView = findViewById(R.id.userBloodTypeTextView);
        userAmkaTextView = findViewById(R.id.userAmkaTextView);
        userPartnerSwitch = findViewById(R.id.userPartnerSwitch);
        updateUserInfoButton = findViewById(R.id.updateUserInfoButton);
        viewUserInfoRelativeLayout = findViewById(R.id.viewUserInfoRelativeLayout);
        updateUserInfoRelativeLayout = findViewById(R.id.updateUserInfoRelativeLayout);
        calendarView = findViewById(R.id.updateUserInfoCalendarView);
        nameEditText = findViewById(R.id.nameUpdateUserTextInput);
        surnameEditText = findViewById(R.id.surNameUpdateUserTextInput);
        amkaEditText = findViewById(R.id.amkaUpdateUserTextInput);
        phoneNumberEditText = findViewById(R.id.phoneNumberUpdateUserTextInput);
        birthDateEditText = findViewById(R.id.userBirthDateUpdateTextInput);
        calendarButton = findViewById(R.id.calendarButton);
        bloodTypeSpinner = findViewById(R.id.bloodTypeUpdateUserInfoSpinner);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteUserButton);
        showEmailButton = findViewById(R.id.showEmailButton);
        changeEmailButton = findViewById(R.id.changeEmailButton);
        changeEmailRelativeLayout = findViewById(R.id.changeEmailRelativeLayout);
        emailEditText = findViewById(R.id.emailInputText);
        changePasswordButton = findViewById(R.id.passwordChangeButton);
        showPasswordButton = findViewById(R.id.showPasswordButton);
        changePasswordRelativeLayout = findViewById(R.id.passwordChangeRelativeLayout);
        newPasswordEditText = findViewById(R.id.newPasswordTextInput);
        newPasswordVerificationEditText = findViewById(R.id.passwordValidationInputText);
        coParentButton = findViewById(R.id.coParentButton);
        coParentRelativeLayout = findViewById(R.id.coParentRelativeLayout);
        coParentAmkaTextView = findViewById(R.id.coParentAmkaTextView);
        coParentFullNameTextView = findViewById(R.id.coParentFullNameTextView);
        coParentEmailTextView = findViewById(R.id.coParentEmailTextView);
        coParentPhoneNumberTextView = findViewById(R.id.coParentPhoneNumberTextView);
        coParentBirthDateTextView = findViewById(R.id.coParentBirthDateTextView);
        coParentBloodTypeTextView = findViewById(R.id.coParentBloodTypeTextView);
        coParentAddButton = findViewById(R.id.addCoParentButton);
        coParentDeleteButton = findViewById(R.id.deleteCoParentButton);
        coParentNameEditText = findViewById(R.id.coParentNameTextInput);
        coParentSurnameEditText = findViewById(R.id.coParentSurNameTextInput);
        coParentEmailEditText = findViewById(R.id.coParentEmailTextInput);
        coParentPhoneNumberEditText = findViewById(R.id.coParentPhoneNumberTextInput);
        coParentAmkaEditText = findViewById(R.id.coParentAmkaTextInput);
        coParentBirthDateEditText = findViewById(R.id.coParentBirthDateTextInput);
        coParentBloodTypeSpinner = findViewById(R.id.coParentBloodSpinner);
        coParentAddRelativeLayout = findViewById(R.id.coParentAddRelativeLayout);
        coParentSaveButton = findViewById(R.id.coParentSaveButton);
        calendarCoParentButton = findViewById(R.id.calendarCoParentButton);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewUserAccount);
        coParentExistsRelativeLayout = findViewById(R.id.coParentExistsRelativeLayout);
        noCoParentRelativeLayout = findViewById(R.id.noCoParentRelativeLayout);
        userBirthTextInputLayout = findViewById(R.id.userBirthDateUpdateTextInputLayout);

        //UI
        bottomNavigationView.setSelectedItemId(R.id.navigation_account);

        //getting current user UID
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //setting database
        database = FirebaseDatabase.getInstance();

        //setting visibilities
        userPartnerSwitch.setVisibility(View.VISIBLE);
        userPartnerSwitch.setClickable(false);
        viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
        updateUserInfoRelativeLayout.setVisibility(View.INVISIBLE);
        calendarView.setVisibility(View.INVISIBLE);
        changeEmailRelativeLayout.setVisibility(View.INVISIBLE);
        changePasswordRelativeLayout.setVisibility(View.INVISIBLE);
        coParentRelativeLayout.setVisibility(View.INVISIBLE);
        coParentDeleteButton.setClickable(false);
        coParentAddButton.setClickable(false);
        coParentAddRelativeLayout.setVisibility(View.INVISIBLE);

        //getting data
        getUser();
        viewProvider();

        //setting blood types in lists
        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),bloodImages,bloodType);
        bloodTypeSpinner.setAdapter(customAdapter);
        bloodTypeSpinner.setOnItemSelectedListener(this);

        CustomAdapter customAdapter1=new CustomAdapter(getApplicationContext(),bloodImages,bloodType);
        coParentBloodTypeSpinner.setAdapter(customAdapter1);
        coParentBloodTypeSpinner.setOnItemSelectedListener(this);

        //update user button
        updateUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
            }
        });

        //onclick listener to open calendar for update user info
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setVisibility(View.VISIBLE);
                updateUserInfoRelativeLayout.setVisibility(View.INVISIBLE);
                cal = "user update";
            }
        });



        //getting date of the calendar
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
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
                calendarView.setVisibility(View.INVISIBLE);
                if(cal.equals("user update")){
                    birthDateEditText.setText(d + "/" + m + "/" + year);
                    updateUserInfoRelativeLayout.setVisibility(View.VISIBLE);
                }else if(cal.equals("coParent update")){
                    coParentBirthDateEditText.setText(d + "/" + m + "/" + year);
                    coParentAddRelativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //setting listener to format textView babyBirthDate
        birthDateEditText.addTextChangedListener(new TextWatcher() {
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
                            Toast.makeText(getApplicationContext(), "Birth year should be between 1970 and 2010", Toast.LENGTH_SHORT).show();
                        }else if(year>2010){
                            year = 2010;
                            Toast.makeText(getApplicationContext(), "Birth year should be between 1970 and 2010", Toast.LENGTH_SHORT).show();
                        }
                        cal.set(Calendar.YEAR, year);
                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum((Calendar.DATE)) : day;
                        clean = String.format("%02d%02d%02d", day, month, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2), clean.substring(2, 4), clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    birthDateEditText.setText(current);
                    birthDateEditText.setSelection(sel < current.length() ? sel : current.length());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userType.equals("parent")){
                    updateParent();
                }else{
                    updateDoctor();
                }
            }
        });

        //delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(view);
            }
        });

        //show email button
        showEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEmail(view);
            }
        });

        //show password button
        showPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPassword(view);
            }
        });

        //co parent button
        coParentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coParent();
            }
        });

        //co parent add button
        coParentAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coParentAdd(view);
            }
        });

        //co parent delete button
        coParentDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coParentDelete(view);
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
                        if(userType.equals("doctor")){
                            Intent intent = new Intent(getApplicationContext(), mainScreenDoctor.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), mainScreenParents.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.navigation_add:
                        if(userType.equals("doctor")){
                            Intent intent = new Intent(getApplicationContext(), addChildToDoctor.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), addBaby.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.navigation_account:
                        return true;
                }
                return false;
            }
        });

        //checking textviews input type
        nameEditText.addTextChangedListener(new TextWatcher() {
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
                    next = false;
                    nameEditText.setError("Type only letters please!!");
                }
            }
        });
        surnameEditText.addTextChangedListener(new TextWatcher() {
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
                    next = false;
                    surnameEditText.setError("Type only letters please!!");
                }
            }
        });
        amkaEditText.addTextChangedListener(new TextWatcher() {
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
                    next = false;
                    amkaEditText.setError("Type only numbers please!!");
                }
            }
        });
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
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
                    next = false;
                    phoneNumberEditText.setError("Type only numbers please!!");
                }
            }
        });
        coParentNameEditText.addTextChangedListener(new TextWatcher() {
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
                    coParentNameEditText.setError("Type only letters please!!");
                }
            }
        });
        coParentSurnameEditText.addTextChangedListener(new TextWatcher() {
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
                    coParentSurnameEditText.setError("Type only letters please!!");
                }
            }
        });
        coParentAmkaEditText.addTextChangedListener(new TextWatcher() {
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
                    coParentAmkaEditText.setError("Type only numbers please!!");
                }
            }
        });
        coParentPhoneNumberEditText.addTextChangedListener(new TextWatcher() {
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
                    coParentPhoneNumberEditText.setError("Type only numbers please!!");
                }
            }
        });
    }

    //co parent delete button
    public void coParentDelete(View view){
        if(parent.getPartner()){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            //delete co parent
                            reference2 = database.getReference("parent").child(currentUserUID);
                            reference2.child("partner").setValue(false);
                            reference2.child("partnersAmka").setValue("00000000000");
                            parent.setPartner(false);
                            parent.setPartnersAmka("00000000000");
                            coParent();

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            //Do nothing
                    }
                }
            };
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete co-parent? Deletion will be permanent").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }catch (Exception e){
                System.out.println(e.getLocalizedMessage());
            }

        }else{
            Toast.makeText(this, "There is no parent to delete!", Toast.LENGTH_SHORT).show();
        }
    }

    //co parent add button
    public void coParentAdd(View view){
        if(parent.getPartner()){
            //if there is parent number 2
            Toast.makeText(this, "You cant add another parent!", Toast.LENGTH_SHORT).show();
        }else{
            //if there is no other parent
            coParentRelativeLayout.setVisibility(View.INVISIBLE);
            coParentAddRelativeLayout.setVisibility(View.VISIBLE);
            calendarView.setVisibility(View.INVISIBLE);

            //onclick listener for calendar opening
            calendarCoParentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendarView.setVisibility(View.VISIBLE);
                    coParentAddRelativeLayout.setVisibility(View.INVISIBLE);
                }
            });

            //setting listener to format textView babyBirthDate
            coParentBirthDateEditText.addTextChangedListener(new TextWatcher() {
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
                                Toast.makeText(getApplicationContext(), "Birth year should be between 1970 and 2010", Toast.LENGTH_SHORT).show();
                            }else if(year>2010){
                                year = 2010;
                                Toast.makeText(getApplicationContext(), "Birth year should be between 1970 and 2010", Toast.LENGTH_SHORT).show();
                            }
                            cal.set(Calendar.YEAR, year);
                            day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum((Calendar.DATE)) : day;
                            clean = String.format("%02d%02d%02d", day, month, year);
                        }

                        clean = String.format("%s/%s/%s", clean.substring(0, 2), clean.substring(2, 4), clean.substring(4, 8));

                        sel = sel < 0 ? 0 : sel;
                        current = clean;
                        coParentBirthDateEditText.setText(current);
                        coParentBirthDateEditText.setSelection(sel<current.length()? sel : current.length());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            coParentSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    coParentSave(view);
                }
            });
        }
    }

    //co parent save button
    public void coParentSave(View view) {
        flagNext = true;
        if (TextUtils.isEmpty(coParentNameEditText.getText())) {
            coParentNameEditText.setError("Please enter a name!");
            flagNext = false;
        }
        if (TextUtils.isEmpty(coParentSurnameEditText.getText())) {
            coParentSurnameEditText.setError("Please enter a surname!");
            flagNext = false;
        }
        if (TextUtils.isEmpty(coParentAmkaEditText.getText()) || (coParentAmkaEditText.getText().length() != 11)) {
            coParentAmkaEditText.setError("Amka number should have length 11 numbers!");
            flagNext = false;
        }
        if (TextUtils.isEmpty(coParentPhoneNumberEditText.getText()) || (coParentPhoneNumberEditText.getText().length() != 10)) {
            coParentPhoneNumberEditText.setError("Phone number should have length 10 numbers!");
            flagNext = false;
        }
        if (TextUtils.isEmpty(coParentEmailEditText.getText()) || (!android.util.Patterns.EMAIL_ADDRESS.matcher(coParentEmailEditText.getText()).matches())) {
            coParentEmailEditText.setError("Please enter a valid email address!");
            flagNext = false;
        }
        if (coParentBloodTypeSpinner.getSelectedItem().equals("Blood Type")) {
            Toast.makeText(this, "Please choose a blood type!", Toast.LENGTH_SHORT).show();
            flagNext = false;
        }
        if(!coParentBirthDateEditText.getText().toString().matches("^[0-9]+(\\/[0-9]+)*$" )) {
            flagNext=false;
            Toast.makeText(this, "Please fill in correct birth date!", Toast.LENGTH_SHORT).show();
        }

        //find if amka is being used
        flagUnique = true;
        reference1 = database.getReference("parent");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {
                        };
                        String amka = String.valueOf(snapshots.child("amka").getValue());
                        if (coParentAmkaEditText.getText().toString().equals(amka)) {
                            flagUnique = false;
                            coParent = new Parent(snapshots.getValue(t));
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

    //check restrictions and save co-parent
    private void check() {
        flagOnce=false;
        flagOnce1=false;
        if (!flagUnique) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            //add parent to database
                            parent.setPartnersAmka(coParentAmkaEditText.getText().toString());
                            parent.setPartner(true);
                            reference2 = FirebaseDatabase.getInstance().getReference("parent");
                            reference2.child(currentUserUID).child("partner").setValue(true);
                            reference2.child(currentUserUID).child("partnersAmka").setValue(coParentAmkaEditText.getText().toString());
                            coParentAddRelativeLayout.setVisibility(View.INVISIBLE);
                            viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
                            getUser();
                           flagOnce = true;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            //Do nothing
                    }
                }
            };
            try {
                if (!flagOnce) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Parent exists already.Do you want to continue with this parent??").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                }catch(Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        } else {
            if (flagNext && flagUnique) {
                flagOnce1=false;
                //if there are empty textBoxes show message
                //add parents to database
                parent.setPartnersAmka(coParentAmkaEditText.getText().toString());
                parent.setPartner(true);
                reference2 = FirebaseDatabase.getInstance().getReference("parent");
                reference2.child(currentUserUID).child("partner").setValue(true);
                reference2.child(currentUserUID).child("partnersAmka").setValue(coParentAmkaEditText.getText().toString());
                coParent = new Parent(coParentNameEditText.getText().toString(), coParentSurnameEditText.getText().toString(),
                        coParentAmkaEditText.getText().toString(), coParentPhoneNumberEditText.getText().toString(),
                        coParentEmailEditText.getText().toString(), coParentBirthDateEditText.getText().toString(),
                        coParentBloodTypeSpinner.getSelectedItem().toString(), parent.getAmka(), true, null);//, null);
                //getting number of parents on database
                reference2.child(coParentAmkaEditText.getText().toString()).setValue(coParent);
                //go to app main screen
                if(!flagOnce1){
                    Toast.makeText(this, "Info saved successfully!!", Toast.LENGTH_SHORT).show();
                }
                //go to app main screen
                coParentAddRelativeLayout.setVisibility(View.INVISIBLE);
                viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
                getUser();
                flagOnce1=true;
            }
        }
    }

    //view co-parent info
    private void coParent(){
        viewUserInfoRelativeLayout.setVisibility(View.INVISIBLE);
        coParentRelativeLayout.setVisibility(View.VISIBLE);
        noCoParentRelativeLayout.setVisibility(View.INVISIBLE);
        coParentExistsRelativeLayout.setVisibility(View.VISIBLE);
        if(parent.getPartner()){
            //if there is a partner
            //find co-parent info
            reference1 = database.getReference("parent");
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot!=null){
                        for(DataSnapshot snapshots:snapshot.getChildren()){
                            GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {};
                            if(snapshots.getValue(t).getAmka().equals(parent.getPartnersAmka())){
                                coParent = snapshots.getValue(t);
                            }
                        }
                    }
                    coParentAmkaTextView.setText(coParent.getAmka());
                    coParentFullNameTextView.setText(coParent.getName() + " " + coParent.getSurname());
                    coParentEmailTextView.setText(coParent.getEmail());
                    coParentBirthDateTextView.setText(coParent.getDateOfBirth());
                    coParentPhoneNumberTextView.setText(coParent.getPhoneNumber());
                    coParentBloodTypeTextView.setText(coParent.getBloodType());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            //if there is not a partner
            noCoParentRelativeLayout.setVisibility(View.VISIBLE);
            coParentExistsRelativeLayout.setVisibility(View.INVISIBLE);
        }
    }

    //show password button
    public void showPassword(View view){
        if(provider.equals("email")) {
            viewUserInfoRelativeLayout.setVisibility(View.INVISIBLE);
            changePasswordRelativeLayout.setVisibility(View.VISIBLE);
            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (newPasswordEditText.getText().toString().equals(newPasswordVerificationEditText.getText().toString())) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        //add parent to database
                                        user.updatePassword(newPasswordEditText.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(userAccount.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                                                            changePasswordRelativeLayout.setVisibility(View.INVISIBLE);
                                                            viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                });
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        //Do nothing
                                }
                            }
                        };
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(userAccount.this);
                            builder.setMessage("Are you sure you want to update password?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    } else {
                        Toast.makeText(userAccount.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }else {
            Toast.makeText(this, "You cannot change the password!", Toast.LENGTH_SHORT).show();
        }
    }

    //show email button
    public void showEmail(View view){
        if(provider.equals("email")) {
            viewUserInfoRelativeLayout.setVisibility(View.INVISIBLE);
            changeEmailRelativeLayout.setVisibility(View.VISIBLE);
            if(userType.equals("parent")){
                emailEditText.setText(parent.getEmail());
            }else if(userType.equals("doctor")){
                emailEditText.setText(doctor.getEmail());
            }
            changeEmailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    //add parent to database
                                    if (userType.equals("parent")) {
                                        if (!parent.getEmail().equals(emailEditText.getText().toString())) {
                                            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                                            u.updateEmail(emailEditText.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                reference1 = database.getReference("parent");
                                                                reference1.child(currentUserUID).child("email").setValue(emailEditText.getText().toString());
                                                                Toast.makeText(userAccount.this, "Email updated successfully!", Toast.LENGTH_SHORT).show();
                                                                changeEmailRelativeLayout.setVisibility(View.INVISIBLE);
                                                                viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
                                                                getUser();
                                                            }
                                                        }
                                                    });
                                        }else{
                                            Toast.makeText(userAccount.this, "Email is the same!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (userType.equals("doctor")) {
                                        if (!doctor.getEmail().equals(emailEditText.getText().toString())) {
                                            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                                            u.updateEmail(emailEditText.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                reference2 = database.getReference("doctor");
                                                                reference2.child(currentUserUID).child("email").setValue(emailEditText.getText().toString());
                                                                Toast.makeText(userAccount.this, "Email updated successfully!", Toast.LENGTH_SHORT).show();
                                                                changeEmailRelativeLayout.setVisibility(View.INVISIBLE);
                                                                viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
                                                                getUser();
                                                            }
                                                        }
                                                    });
                                        }else{
                                            Toast.makeText(userAccount.this, "Email is the same!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    //Do nothing
                            }
                        }
                    };
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(userAccount.this);
                        builder.setMessage("Are you sure you want to update the email address?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
            });
        }else{
            Toast.makeText(this, "You can't change the email address because you are connected via " + provider, Toast.LENGTH_SHORT).show();
        }
    }

    //getting user info from database
    private void getUser(){
        if(userType.equals("parent")) {
            reference1 = database.getReference("parent");
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null) {
                        for (DataSnapshot snapshots : snapshot.getChildren()) {
                            String UID = snapshots.getKey();
                            if (UID.equals(currentUserUID)) {
                                GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {};
                                parent = snapshots.getValue(t);
                            }
                        }
                        loadData("parent");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            reference1 = database.getReference("doctor");
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null) {
                        for (DataSnapshot snapshots : snapshot.getChildren()) {
                            String UID = snapshots.getKey();
                            if (UID.equals(currentUserUID)) {
                                GenericTypeIndicator<Doctor> t = new GenericTypeIndicator<Doctor>() {};
                                doctor = snapshots.getValue(t);
                            }
                        }
                        loadData("doctor");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    //check connection provider
    private void viewProvider(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<? extends UserInfo> infos = user.getProviderData();
        for (UserInfo ui : infos) {
            if (ui.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID)) {
                provider="google";
            }else if(ui.getProviderId().equals(FacebookAuthProvider.PROVIDER_ID))
            {
                provider = "facebook";
            }else if(ui.getProviderId().equals(EmailAuthProvider.PROVIDER_ID)){
                provider = "email";
            }
        }

    }

    //load data
    private void loadData(String user){
        if(user.equals("parent")){
            userFullnameTextView.setText(parent.getName() + " " + parent.getSurname());
            userEmailTextView.setText(parent.getEmail());
            userPhoneNumberTextView.setText(parent.getPhoneNumber());
            try {
                userKidsTextView.setText(String.valueOf(parent.getKids().size()));
            }catch (Exception e){
                System.out.println(e.getLocalizedMessage());
                userKidsTextView.setText("0");
            }
            userBloodTypeTextView.setVisibility(View.VISIBLE);
            userBirthDateTextView.setVisibility(View.VISIBLE);
            userPartnerSwitch.setVisibility(View.VISIBLE);
            userBirthDateTextView.setText(parent.getDateOfBirth());
            userBloodTypeTextView.setText(parent.getBloodType());
            userAmkaTextView.setText(parent.getAmka());
            userPartnerSwitch.setChecked(parent.getPartner());
            coParentButton.setVisibility(View.VISIBLE);
            if(parent.getPartner()){
                userPartnerSwitch.setText(parent.getPartnersAmka());
            }else{
                userPartnerSwitch.setText("No partner");
            }
        }else{
            coParentButton.setVisibility(View.INVISIBLE);
            userFullnameTextView.setText(doctor.getName() + " " + doctor.getSurname());
            userEmailTextView.setText(doctor.getEmail());
            userPhoneNumberTextView.setText(doctor.getPhoneNumber());
            try {
                userKidsTextView.setText(String.valueOf(doctor.getKids().size()));
            }catch (Exception e){
                userKidsTextView.setText("0");
            }
            userAmkaTextView.setText(doctor.getMedicalID());
            userBloodTypeTextView.setVisibility(View.GONE);
            userBirthDateTextView.setVisibility(View.GONE);
            userPartnerSwitch.setVisibility(View.GONE);
        }
    }

    //update users info
    private void updateUserInfo(){
        viewUserInfoRelativeLayout.setVisibility(View.INVISIBLE);
        updateUserInfoRelativeLayout.setVisibility(View.VISIBLE);
        if(userType.equals("parent")) {
            position = 0;
            nameEditText.setText(parent.getName());
            surnameEditText.setText(parent.getSurname());
            amkaEditText.setText(parent.getAmka());
            phoneNumberEditText.setText(parent.getPhoneNumber());
            birthDateEditText.setText(parent.getDateOfBirth());
            for (int i = 0; i < bloodType.length; i++) {
                if (bloodType[i].equals(parent.getBloodType())) {
                    position = i;
                }
            }
            birthDateEditText.setVisibility(View.VISIBLE);
            userBirthTextInputLayout.setVisibility(View.VISIBLE);
            calendarButton.setVisibility(View.VISIBLE);
            bloodTypeSpinner.setVisibility(View.VISIBLE);
            bloodTypeSpinner.setSelection(position);
        }else{
            nameEditText.setText(doctor.getName());
            surnameEditText.setText(doctor.getSurname());
            phoneNumberEditText.setText(doctor.getPhoneNumber());
            amkaEditText.setText(doctor.getMedicalID());
            birthDateEditText.setVisibility(View.INVISIBLE);
            userBirthTextInputLayout.setVisibility(View.INVISIBLE);
            calendarButton.setVisibility(View.INVISIBLE);
            bloodTypeSpinner.setVisibility(View.INVISIBLE);
        }
    }

    //update parent
    private void updateParent(){
        next = true;
        if (TextUtils.isEmpty(nameEditText.getText())) {
            nameEditText.setError("Please enter a name!");
            next = false;
        }
        if (TextUtils.isEmpty(surnameEditText.getText())) {
            surnameEditText.setError("Please enter a surname!");
            next = false;
        }
        if (TextUtils.isEmpty(amkaEditText.getText()) || (amkaEditText.getText().length() != 11)) {
            amkaEditText.setError("Amka number should have length 11 numbers!");
            next = false;
        }
        if (TextUtils.isEmpty(phoneNumberEditText.getText()) || (phoneNumberEditText.getText().length() != 10)) {
            phoneNumberEditText.setError("Phone number should have length 10 numbers!");
            next = false;
        }
        if (bloodTypeSpinner.getSelectedItem().equals("Blood Type")) {
            ((TextView) bloodTypeSpinner.getSelectedView()).setError("Please choose a blood type!");
            next = false;
        }
        if(!birthDateEditText.getText().toString().matches("^[0-9]+(\\/[0-9]+)*$" )) {
            next=false;
            Toast.makeText(this, "Please fill in correct birth date!", Toast.LENGTH_SHORT).show();
        }

        //checking users Amka
        flagUnique = true;
        reference1 = database.getReference("parent");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {};
                        if (amkaEditText.getText().toString().equals(snapshots.getValue(t).getAmka())) {
                            flagUnique = false;
                            userUID = snapshots.getKey();
                        }
                    }
                }
                if(userUID.equals(currentUserUID)){
                    flagUnique = true;
                }
                if(flagUnique && next){
                    Parent p = new Parent(nameEditText.getText().toString(), surnameEditText.getText().toString(),
                            amkaEditText.getText().toString(),phoneNumberEditText.getText().toString(),
                            parent.getEmail(), birthDateEditText.getText().toString(),
                            bloodTypeSpinner.getSelectedItem().toString(), "amka Partner", false, parent.getKids());
                    reference1.child(currentUserUID).removeValue();
                    reference1.child(currentUserUID).setValue(p);
                    if(!p.getAmka().equals(parent.getAmka()) && parent.getKids().size()>0) {
                        updateDatabase("update");
                    }
                    updateUserInfoRelativeLayout.setVisibility(View.INVISIBLE);
                    viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
                    getUser();
                }else if(!flagUnique){
                    amkaEditText.setError("Amka number should be unique! Please try again");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //update info in database
    private void updateDatabase(String action){
        try{
        for(int i=0;i<parent.getKids().size();i++) {
            String babyAmka = parent.getKids().get(i).getAmka();
            reference3 = database.getReference("baby");
            reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null) {
                        for (DataSnapshot snapshots : snapshot.getChildren()) {
                            if (snapshots.getKey().equals(babyAmka)) {
                                GenericTypeIndicator<Baby> t = new GenericTypeIndicator<Baby>() {
                                };
                                Baby b = snapshots.getValue(t);
                                if (action.equals("update")) {
                                    //update info on baby database
                                    if (b.getParentOneAmka().equals(parent.getAmka())) {
                                        b.setParentOneAmka(amkaEditText.getText().toString());
                                    } else if (b.getParentTwoAmka().equals(parent.getAmka())) {
                                        b.setParentTwoAmka(amkaEditText.getText().toString());
                                    }
                                    reference3.child(babyAmka).removeValue();
                                    reference3.child(babyAmka).setValue(b);
                                } else if (action.equals("delete")) {
                                    //delete info on baby database
                                    if (b.getParentTwoAmka().equals("00000000000")) {
                                        //baby doesn't have parent 2
                                        reference3.child(babyAmka).removeValue();
                                        updateDoctorsDatabase(babyAmka);
                                    } else {
                                        //if there is a parent 2
                                        b.setParentOneAmka(b.getParentTwoAmka());
                                        b.setParentTwoAmka("00000000000");
                                        reference3.child(babyAmka).removeValue();
                                        reference3.child(babyAmka).setValue(b);
                                    }

                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        }catch (Exception e){
            //nothing here
        }


        //check if there is a partner and change info in database
        if(parent.getPartner()){
            reference2 = FirebaseDatabase.getInstance().getReference("parent");
            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot!=null){
                        for(DataSnapshot snapshots:snapshot.getChildren()){
                            GenericTypeIndicator<Parent> t = new GenericTypeIndicator<Parent>() {};
                            if(snapshots.getValue(t).getPartnersAmka().equals(parent.getAmka())){
                                Parent p1 = snapshots.getValue(t);
                                if(action.equals("update")) {
                                    p1.setPartnersAmka(amkaEditText.getText().toString());
                                }else if(action.equals("delete")){
                                    p1.setPartner(false);
                                    p1.setPartnersAmka("00000000000");
                                }
                                reference2.child(snapshots.getKey()).removeValue();
                                reference2.child(snapshots.getKey()).setValue(p1);
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

    //update doctor info in database
    private void updateDoctorsDatabase(String babyAmka){
        //update babies on doctor
        reference1 = database.getReference("doctor");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        GenericTypeIndicator<Doctor> t = new GenericTypeIndicator<Doctor>() {};
                        try{
                            int size = snapshots.getValue(t).getKids().size();
                            for(int i=0;i<size;i++){
                                if(snapshots.getValue(t).getKids().get(i).getAmka().equals(babyAmka)){
                                    ArrayList<Baby> k = snapshots.getValue(t).getKids();
                                    k.remove(i);
                                    Doctor d = snapshots.getValue(t);
                                    Doctor d1 = new Doctor(d.getName(), d.getMedicalID(), d.getPhoneNumber(), d.getEmail(), k, d.getSurname());
                                    reference1.child(snapshots.getKey()).removeValue();
                                    reference1.child(snapshots.getKey()).setValue(d1);
                                }
                            }
                        }catch (Exception e){
                            //do nothing
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //update developments on monitoring devs
        reference2 = FirebaseDatabase.getInstance().getReference("monitoringDevelopment");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {
                if(snapshots!=null){
                    for(DataSnapshot dataSnapshot:snapshots.getChildren()){
                        GenericTypeIndicator<Development> t = new GenericTypeIndicator<Development>(){};
                        Development dev = dataSnapshot.getValue(t);
                        if(dev.getAmka().equals(babyAmka)){
                            reference2 = dataSnapshot.getRef();
                            reference2.removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //update doctor
    private void updateDoctor() {
        next = true;
        if (TextUtils.isEmpty(nameEditText.getText())) {
            nameEditText.setError("Please enter a name!");
            next = false;
        }
        if (TextUtils.isEmpty(surnameEditText.getText())) {
            surnameEditText.setError("Please enter a surname!");
            next = false;
        }
        if (TextUtils.isEmpty(amkaEditText.getText()) || (amkaEditText.getText().length() != 11)) {
            amkaEditText.setError("Medical ID should have length 11 numbers!");
            next = false;
        }
        if (TextUtils.isEmpty(phoneNumberEditText.getText()) || (phoneNumberEditText.getText().length() != 10)) {
            phoneNumberEditText.setError("Phone number should have length 10 numbers!");
            next = false;
        }

        //checking users medical ID
        flagUnique = true;
        reference1 = database.getReference("doctor");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        GenericTypeIndicator<Doctor> t = new GenericTypeIndicator<Doctor>() { };
                        if (amkaEditText.getText().toString().equals(snapshots.getValue(t).getMedicalID())) {
                            flagUnique = false;
                            userUID = snapshots.getKey();
                        }
                    }
                    if(userUID.equals(currentUserUID)){
                        flagUnique = true;
                    }
                    if(flagUnique && next){
                        Doctor d = new Doctor(nameEditText.getText().toString(), amkaEditText.getText().toString(),
                                phoneNumberEditText.getText().toString(), doctor.getEmail(),doctor.getKids(),
                                surnameEditText.getText().toString());
                        reference1.child(currentUserUID).removeValue();
                        reference1.child(currentUserUID).setValue(d);
                        updateUserInfoRelativeLayout.setVisibility(View.INVISIBLE);
                        viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
                        getUser();
                    }else if(!flagUnique){
                        amkaEditText.setError("Medical ID should be unique! Please try again");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //on resume page
    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_account);
        getUser();
        super.onResume();
    }

    //on back button pressed
    @Override
    public void onBackPressed() {
        if(calendarView.getVisibility() == View.VISIBLE){
            calendarView.setVisibility(View.INVISIBLE);
            if(cal.equals("user update")){
                updateUserInfoRelativeLayout.setVisibility(View.VISIBLE);
            }else{
                coParentAddRelativeLayout.setVisibility(View.VISIBLE);
            }
        }else if(updateUserInfoRelativeLayout.getVisibility() == View.VISIBLE){
            updateUserInfoRelativeLayout.setVisibility(View.INVISIBLE);
            viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
            getUser();
        }else if(viewUserInfoRelativeLayout.getVisibility() == View.VISIBLE){
            if(userType.equals("doctor")){
                Intent intent = new Intent(userAccount.this, mainScreenDoctor.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(userAccount.this, mainScreenParents.class);
                startActivity(intent);
            }
        }else if(changeEmailRelativeLayout.getVisibility() == View.VISIBLE){
            changeEmailRelativeLayout.setVisibility(View.INVISIBLE);
            viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
        }else if(changePasswordRelativeLayout.getVisibility() == View.VISIBLE){
            changePasswordRelativeLayout.setVisibility(View.INVISIBLE);
            viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
        }else if(coParentRelativeLayout.getVisibility() == View.VISIBLE){
            coParentRelativeLayout.setVisibility(View.INVISIBLE);
            getUser();
            viewUserInfoRelativeLayout.setVisibility(View.VISIBLE);
        }else if(coParentAddRelativeLayout.getVisibility() == View.VISIBLE){
            getUser();
            coParentAddRelativeLayout.setVisibility(View.INVISIBLE);
            coParentRelativeLayout.setVisibility(View.VISIBLE);
        }

    }

    //delete user button
    public void deleteUser(View view){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        //add parent to database
                        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                        u.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(userType.equals("doctor")) {
                                    reference1 = database.getReference("doctor");
                                    reference1.child(currentUserUID).removeValue();
                                }else{ if(userType.equals("parent"))
                                    reference1 = database.getReference("parent");
                                    reference1.child(currentUserUID).removeValue();
                                    updateDatabase("delete");
                                }
                                Intent intent = new Intent(userAccount.this, loginRegister.class);
                                startActivity(intent);
                            }
                        });
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        //Do nothing
                }
            }
        };
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this user? Deletion will be permanent").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}