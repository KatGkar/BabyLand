package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Arrays;

public class registerUsernamePasswordActivity extends AppCompatActivity{

    private TextInputEditText emailTextView, passwordTextView, passwordValidTextView;
    private String email, password, passwordValid;
    private FirebaseAuth firebaseAuth;
    private ImageView facebookButton, googleButton;
    private Button registerButton;
    private Boolean flagNext;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_username_password);

        //getting views from xml file
        emailTextView = findViewById(R.id.emailTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
        passwordValidTextView = findViewById(R.id.passwordValidationTextView);
        registerButton = findViewById(R.id.registerUserButton);
        facebookButton = findViewById(R.id.facebookButtonRegister);
        googleButton = findViewById(R.id.googleButtonRegister);

        //setting database
        firebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());


        //register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(view);
            }
        });

        //facebook sign in button
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(registerUsernamePasswordActivity.this, Arrays.asList("public_profile"));
            }
        });

        //login callback manager for facebook login
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(registerUsernamePasswordActivity.this, "Facebook is not responding. Please try again later!!", Toast.LENGTH_SHORT).show();
                    }
                });

        //google sign in
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //google sing in
        mGoogleSignInClient= GoogleSignIn.getClient(registerUsernamePasswordActivity.this
                ,googleSignInOptions);

        //google sign in button
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intent,100);
            }
        });

    }


    //register user button
    public void createUser(View view) {
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        passwordValid = passwordValidTextView.getText().toString();
        flagNext = true;
        if (email.isEmpty()) {
            flagNext = false;
            emailTextView.setError("Please fill in an email!!");
            emailTextView.requestFocus();
        }
        if (password.isEmpty()) {
            flagNext = false;
            passwordTextView.setError("Please choose a password!!");
            passwordTextView.requestFocus();
        }
        if (passwordValid.isEmpty()) {
            flagNext = false;
            passwordValidTextView.setError("Please confirm password!!");
            passwordValidTextView.requestFocus();
        }
        if (!password.equals(passwordValid)) {
            flagNext = false;
            passwordValidTextView.setError("Passwords do not match! Please try again");
        }
        if (flagNext) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(registerUsernamePasswordActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(registerUsernamePasswordActivity.this, doctorParentChoose.class);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(registerUsernamePasswordActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(task.getException().getMessage());
                    }
                }
            });
        }
    }

    //checking if email address is being used
    private void checkEmailUniqueness(GoogleSignInAccount googleSignInAccount, String emailUsed) {
        firebaseAuth.fetchSignInMethodsForEmail(emailUsed).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().size() == 0){
                    // email not existed
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                    firebaseAuth.signInWithCredential(authCredential)
                            .addOnCompleteListener(registerUsernamePasswordActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // When task is successful
                                        Intent intent = new Intent(registerUsernamePasswordActivity.this, doctorParentChoose.class);
                                        startActivity(intent);
                                    } else {
                                        // When task is unsuccessful
                                        Toast.makeText(registerUsernamePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }else {
                    // email existed
                    Toast.makeText(registerUsernamePasswordActivity.this, "The email address is already in use by another account!", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    //facebook
    private void handleFacebookToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential((token.getToken()));
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(registerUsernamePasswordActivity.this, doctorParentChoose.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(registerUsernamePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //facebook and google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount != null) {
                        String em = googleSignInAccount.getEmail();
                        checkEmailUniqueness(googleSignInAccount, em);
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}