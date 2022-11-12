package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class OpenScreen extends AppCompatActivity {
    private EditText email, password;
    long delay=1000;
    long lastEditText=0;
    protected FirebaseAuth firebaseAuth;
    ImageView loading;
    FrameLayout loadingFrame;
    Handler handler = new Handler();
    TextView createUser;

    //runnable to check if user has stopped writing
    private Runnable inputFinish = new Runnable() {
        @Override
        public void run() {
            if(System.currentTimeMillis() > (lastEditText+delay-500)) {
                if((!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()))) {
                    loadingFrame.setVisibility(View.VISIBLE);
                    checkLogin();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);

        //finding views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loading = findViewById(R.id.loading);
        loadingFrame = findViewById(R.id.loadingFrame);
        firebaseAuth = FirebaseAuth.getInstance();
        createUser = findViewById(R.id.createNewUser);

        //setting visibilities
        loadingFrame.setVisibility(View.INVISIBLE);

        //setting design
        createUser.setPaintFlags(createUser.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        //check if user is writing in email textView
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(inputFinish);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0){
                    lastEditText = System.currentTimeMillis();
                    handler.postDelayed(inputFinish, delay);
                }else{

                }
            }
        });

        //check if user is writing in password textView
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(inputFinish);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0){
                    lastEditText = System.currentTimeMillis();
                    handler.postDelayed(inputFinish, delay);
                }else{

                }
            }
        });



    }



    //checking users' credentials (email and password)
    public void checkLogin() {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loadingFrame.setVisibility(View.INVISIBLE);
                        //continue to next page
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingFrame.setVisibility(View.INVISIBLE);
                        showMessage("Error! Wrong credentials!", "Please try again!!");
                    }
                });
    }


    //showing messages to users
    public void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

    public void createUser(View view){
        //go to page createNewUser
    }

}