package com.example.babyland;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUsernamePasswordActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView, passwordValidTextView;
    private String email, password, passwordValid;
    private FirebaseAuth firebaseAuth;
    private Button registerButton;
    private ConstraintLayout constraintLayout;
    private Boolean flagNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_username_password);

        //getting views from xml file
        emailTextView = findViewById(R.id.emailTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
        passwordValidTextView = findViewById(R.id.passwordValidTextView);
        registerButton = findViewById(R.id.registerUserButton);
        constraintLayout = findViewById(R.id.constrainLayoutRegisterUsernamePassword);

        //setting database
        firebaseAuth = FirebaseAuth.getInstance();

        //UI
        constraintLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient));

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(view);
            }
        });
    }

    //register user button
    public void createUser(View view) {
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        passwordValid = passwordValidTextView.getText().toString();
        flagNext = true;
        if(email.isEmpty()) {
            flagNext = false;
            emailTextView.setError("Please fill in an email!!");
            emailTextView.requestFocus();
        }
        if(password.isEmpty()){
            flagNext = false;
            passwordTextView.setError("Please choose a password!!");
            passwordTextView.requestFocus();
        }
        if(passwordValid.isEmpty()){
            flagNext = false;
            passwordValidTextView.setError("Please confirm password!!");
            passwordValidTextView.requestFocus();
        }
        if(!password.equals(passwordValid)){
            flagNext = false;
            passwordValidTextView.setError("Password do not match!!");
        }
        if(flagNext) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(RegisterUsernamePasswordActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterUsernamePasswordActivity.this, LoginRegister.class);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegisterUsernamePasswordActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(task.getException().getMessage());
                    }
                }
            });
        }
     /*   firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });    }

*/
    }
}