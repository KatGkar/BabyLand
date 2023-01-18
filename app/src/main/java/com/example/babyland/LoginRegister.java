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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.Login;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class LoginRegister extends AppCompatActivity { //implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView facebookButton, googleButton, image, loading;
    private CallbackManager callbackManager;
    private FirebaseAuth mfirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private final static int RC_SIGN_IN = 123;
    private EditText email, password;
    private long delay = 1000;
    private long lastEditText = 0;
    private FrameLayout loadingFrame;
    private Handler handler = new Handler();
    private TextView createUser;
    private androidx.appcompat.app.AlertDialog.Builder builder;
    private long pressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        //getting views from xml file
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loading = findViewById(R.id.loading);
        loadingFrame = findViewById(R.id.loadingFrame);
        mfirebaseAuth = FirebaseAuth.getInstance();
        createUser = findViewById(R.id.createNewUser);
        image = findViewById(R.id.image);

        //builder to show messages
        builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        //setting image
        Picasso.get().load(R.drawable.baby_girl).into(image);

        //setting visibilities
        loadingFrame.setVisibility(View.INVISIBLE);
        loadingFrame.setBackgroundColor(getResources().getColor(R.color.purple_700));

        //setting design
        createUser.setPaintFlags(createUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


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



        /*name = findViewById(R.id.textView2);
        photo = findViewById(R.id.imageView5);
        logout = findViewById(R.id.logout);
        googleButton = findViewById(R.id.googleButton);
        facebookButton = findViewById(R.id.facebookButton);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());


        //facebook logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut();
                LoginManager.getInstance().logOut();
                finish();
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
                        showMessage("Warning!", "Facebook is not responding. Please try again later!!");
                    }
                });


        //facebook login button
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginRegister.this, Arrays.asList("public_profile"));

            }
        });


        //google sign in button
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        //configure google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

         */
    }


    //on resume
    @Override
    protected void onResume() {
        super.onResume();
        email.setText("");
        email.setHint("Email");
        password.setText("");
        password.setHint("Password");

    }

    //on back button pressed
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            builder.setMessage("Are you sure??").setTitle("Exit app")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth.getInstance().signOut();
                            LoginRegister.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(this, "Press back again if you want to go back",
                    Toast.LENGTH_LONG).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    public void createUser(View view){
        Intent intent = new Intent(this, RegisterUsernamePasswordActivity.class);
        startActivity(intent);
    }



    //runnable to check if user has stopped writing
    private Runnable inputFinish = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() > (lastEditText + delay - 500)) {
                if ((!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()))) {
                    loadingFrame.setVisibility(View.VISIBLE);
                    checkLogin();
                }
            }
        }
    };

    //checking users' credentials (email and password)
    public void checkLogin() {
        mfirebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loadingFrame.setVisibility(View.INVISIBLE);
                        //continue to next page
                        FirebaseUser user = mfirebaseAuth.getCurrentUser();
                       /* if(!user.isEmailVerified()){
                            user.sendEmailVerification();
                            Toast.makeText(LoginRegister.this, "Please check your email ", Toast.LENGTH_SHORT).show();
                        }else{*/
                           //Intent intent = new Intent(LoginRegister.this, MainScreenParents.class);
                            //Intent intent = new Intent(LoginRegister.this, MainScreenDoctor.class);
                        Intent intent = new Intent(LoginRegister.this, doctorParentChoose.class);
                        startActivity(intent);
                        //}
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingFrame.setVisibility(View.INVISIBLE);
                        showMessage("Error! Wrong credentials!", e.getMessage());
                    }
                });
    }


    //showing messages to users
    public void showMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }


    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            try {
                //String url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
                String name2 = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                name.setText(name2);
                showMessage("in", name2);
               // Picasso.get().load(url).into(photo);
            }catch (Exception e){
                showMessage("Error", e.getMessage());
            }
        }
    }*/
}

    /*

    //facebook
    private void handleFacebookToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential((token.getToken()));
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    showResults("facebook");
                } else {
                    showMessage("Warning!", task.getException().getMessage());
                }
            }
        });
    }

    public void showMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

    //facebook and google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult();
                firebaseAuthWithGoogle(account);
            }catch (Exception e){
                showMessage("Error", "Google Error. Please try again later!");

            }
        }
    }

    //google sign in
    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            showResults("google");
                        }else{
                            showMessage("Warning!", task.getException().getMessage());
                        }
                    }
                });
*/
//    }


/*
    //take user info
    public void showResults(String type){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(type.equals("facebook")) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            //code here
                            try {
                                String fullname = object.getString("name");
                                String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                name.setText(fullname);
                                Picasso.get().load(url).into(photo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();
        }else{
            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if(signInAccount != null){
                name.setText(signInAccount.getDisplayName());
                Picasso.get().load(signInAccount.getPhotoUrl()).placeholder(R.mipmap.ic_launcher_round).into(photo);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/
