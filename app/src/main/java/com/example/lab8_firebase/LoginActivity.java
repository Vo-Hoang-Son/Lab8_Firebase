package com.example.lab8_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth fAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    GoogleSignInAccount account;

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        //updateUI(account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_screen);

        EditText Email = findViewById(R.id.txtEmail);
        EditText password = findViewById(R.id.txtPassword);

        fAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleApiClient=new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this,LoginActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        signIn();

                    // ...

            }
        });



        Button btnLogin = findViewById(R.id.btnSingIn_Singinpage);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Email.getText().toString().trim();
                String passwordFeild = password.getText().toString().trim();

                if(email.equalsIgnoreCase("") || passwordFeild.equalsIgnoreCase("")){
                    Toast.makeText(LoginActivity.this, "Error: email, password not empty", Toast.LENGTH_SHORT).show();
                    if(email.equalsIgnoreCase("")) {
                        Email.selectAll();
                        Email.requestFocus();
                    }else if(passwordFeild.equalsIgnoreCase("")){
                        password.selectAll();
                        password.requestFocus();
                    }
                }else {

                    fAuth.signInWithEmailAndPassword(email, passwordFeild).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            int bool = 0;
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                bool = 1;
                            } else {
                                Toast.makeText(LoginActivity.this, "Error !!!", Toast.LENGTH_SHORT).show();
                            }
                            if (bool == 1) {
                                Intent intent = new Intent(LoginActivity.this, FaceActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
        TextView btnReg = findViewById(R.id.textViewRegister);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, com.example.lab8_firebase.RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            Toast.makeText(getApplicationContext(),"Sign in Successfully! Wellcome:  "+account.getDisplayName(),Toast.LENGTH_LONG).show();
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile(){
        Intent intent=new Intent(LoginActivity.this,FaceActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
