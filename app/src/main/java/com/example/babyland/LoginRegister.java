package com.example.babyland;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginRegister extends AppCompatActivity { //implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView facebookButton, googleButton, image, loading;
    CallbackManager callbackManager;
    public FirebaseAuth mfirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private final static int RC_SIGN_IN = 123;
    private EditText email, password;
    long delay = 1000;
    long lastEditText = 0;
    FrameLayout loadingFrame;
    Handler handler = new Handler();
    TextView createUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        //finding views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loading = findViewById(R.id.loading);
        loadingFrame = findViewById(R.id.loadingFrame);
        mfirebaseAuth = FirebaseAuth.getInstance();
        createUser = findViewById(R.id.createNewUser);
        image = findViewById(R.id.image);

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
                        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                        startActivity(intent);

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
