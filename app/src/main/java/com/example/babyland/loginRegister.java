package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class loginRegister extends AppCompatActivity { //implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView facebookButton, googleButton, image, loadingFrame;
    private CallbackManager callbackManager;
    private FirebaseAuth myFirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText email, password;
    private long delay = 1000;
    private long lastEditText = 0;
    private Handler handler = new Handler();
    private TextView createUser;
    private androidx.appcompat.app.AlertDialog.Builder builder;
    private long pressedTime;
    private RelativeLayout loginRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        //getting views from xml file
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loadingFrame = findViewById(R.id.loadingFrame);
        myFirebaseAuth = FirebaseAuth.getInstance();
        createUser = findViewById(R.id.createNewUser);
        image = findViewById(R.id.image);
        loginRelativeLayout = findViewById(R.id.loginRelativeLayout);
        googleButton = findViewById(R.id.googleButton);
        facebookButton = findViewById(R.id.facebookButton);

        //UI
        createUser.setPaintFlags(createUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Picasso.get().load(R.drawable.babylandlogo).into(image);

        //builder to show messages
        builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        //setting visibilities
        loadingFrame.setVisibility(View.INVISIBLE);
        loginRelativeLayout.setVisibility(View.VISIBLE);


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
                if (editable.length() > 0) {
                    lastEditText = System.currentTimeMillis();
                    handler.postDelayed(inputFinish, delay);
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
                if (editable.length() > 0) {
                    lastEditText = System.currentTimeMillis();
                    handler.postDelayed(inputFinish, delay);
                } else {

                }
            }
        });

        //used for user login
        myFirebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());

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
                        Toast.makeText(loginRegister.this, "Facebook is not responding. Please try again later!!", Toast.LENGTH_SHORT).show();
                    }
                });


        //facebook sign in button
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(loginRegister.this, Arrays.asList("public_profile"));

            }
        });

        //google sign in
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

       //google sign in
        mGoogleSignInClient= GoogleSignIn.getClient(loginRegister.this
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


    //on resume page not saving email and password
    @Override
    protected void onResume() {
        super.onResume();
        loadingFrame.setVisibility(View.INVISIBLE);
        loginRelativeLayout.setVisibility(View.VISIBLE);
        email.setText("");
        email.setHint("Email");
        password.setText("");
        password.setHint("Password");

    }

    //on back button pressed checking if user wants to exit application
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            builder.setMessage("Are you sure??").setTitle("Exit app")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth.getInstance().signOut();
                            loginRegister.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(this, "Press back again if you want to exit",
                    Toast.LENGTH_LONG).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    //create user button
    public void createUser(View view) {
        Intent intent = new Intent(this, registerUsernamePasswordActivity.class);
        startActivity(intent);
    }


    //runnable to check if user has stopped writing
    private Runnable inputFinish = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() > (lastEditText + delay - 500)) {
                if ((!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()))) {
                    loadingFrame.setVisibility(View.VISIBLE);
                    loginRelativeLayout.setVisibility(View.INVISIBLE);
                    checkLogin();
                }
            }
        }
    };

    //checking users' credentials (email and password)
    public void checkLogin() {
        myFirebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loadingFrame.setVisibility(View.VISIBLE);
                        //checking if user has completed email validation
                        /*FirebaseUser user = myFirebaseAuth.getCurrentUser();
                        if(!user.isEmailVerified()){
                            user.sendEmailVerification();
                            loadingFrame.setVisibility(View.INVISIBLE);
                            loginRelativeLayout.setVisibility(View.VISIBLE);
                            Toast.makeText(loginRegister.this, "Please check your email!", Toast.LENGTH_SHORT).show();
                        }else{
                        */Intent intent = new Intent(loginRegister.this, doctorParentChoose.class);
                        startActivity(intent);
                        //}
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingFrame.setVisibility(View.INVISIBLE);
                        loginRelativeLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(loginRegister.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //facebook
    private void handleFacebookToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential((token.getToken()));
        myFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(loginRegister.this, doctorParentChoose.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(loginRegister.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                        checkUserRegistered(googleSignInAccount, em);
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //checking if email address is being used
    private void checkUserRegistered(GoogleSignInAccount googleSignInAccount, String emailUsed) {
        myFirebaseAuth.fetchSignInMethodsForEmail(emailUsed).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().size() == 0){
                    // email not existed
                    Toast.makeText(loginRegister.this, "There is no user. Please register!!", Toast.LENGTH_SHORT).show();

                }else {
                    // email existed
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                    myFirebaseAuth.signInWithCredential(authCredential)
                            .addOnCompleteListener(loginRegister.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // When task is successful
                                        Intent intent = new Intent(loginRegister.this, doctorParentChoose.class);
                                        startActivity(intent);
                                    } else {
                                        // When task is unsuccessful
                                        Toast.makeText(loginRegister.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

}