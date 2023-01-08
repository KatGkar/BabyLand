package com.example.babyland;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUsernamePasswordActivity extends AppCompatActivity {

    EditText emailTextView, passwordTextView, passwordValidTextView;
    String email, password, passwordValid;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_username_password);

        //getting views
        emailTextView = findViewById(R.id.emailTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
        passwordValidTextView = findViewById(R.id.passwordValidTextView);


        //setting firebase
        firebaseAuth = FirebaseAuth.getInstance();


    }

    public void createUser(View view) {
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        passwordValid = passwordValidTextView.getText().toString();
        if (password.equals(passwordValid)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Intent intent = new Intent(RegisterUsernamePasswordActivity.this, LoginRegister.class);
                        startActivity(intent);

                    } else {
                        // If sign in fails, display a message to the user.
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