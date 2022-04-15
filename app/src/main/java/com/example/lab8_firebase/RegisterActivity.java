package com.example.lab8_firebase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 2;
    private FirebaseAuth fAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    private GoogleSignInAccount account;
    private GoogleSignInOptions gso;
    private  Button btn_register;
    private EditText editName;
    private EditText editEmail;
    private EditText editPass;
    private EditText editRePass;

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(RegisterActivity.this);
        //updateUI(account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        fAuth = FirebaseAuth.getInstance();
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleApiClient=new GoogleApiClient.Builder(RegisterActivity.this)
                .enableAutoManage(RegisterActivity.this,RegisterActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, gso);
        // Set the dimensions of the sign-in button.
        ImageView img = findViewById(R.id.imageView18);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                signIn();
                            }
                        });
            }
        });

        btn_register = findViewById(R.id.btnRegister_Registerpage);
        editName = findViewById(R.id.txtName);
        editEmail = findViewById(R.id.txtEmail_Registerpage);
        editPass = findViewById(R.id.txtPassword_Registerpage_1);
        editRePass = findViewById(R.id.txtPassword_Registerpage_2);

        if(fAuth.getCurrentUser() != null) {

        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String pass = editPass.getText().toString().trim();
                String rePass = editRePass.getText().toString().trim();

                if(name.equalsIgnoreCase("") || email.equalsIgnoreCase("") || pass.equalsIgnoreCase("")){
                    Toast.makeText(RegisterActivity.this, "Error: name, email, password not empty", Toast.LENGTH_SHORT).show();
                    if(name.equalsIgnoreCase("")) {
                        editName.selectAll();
                        editName.requestFocus();
                    }else if(email.equalsIgnoreCase("")){
                        editEmail.selectAll();
                        editEmail.requestFocus();
                    }else if(pass.equalsIgnoreCase("")){
                        editPass.selectAll();
                        editPass.requestFocus();
                    }
                }else {
                    if (pass.equals(rePass)) {
                        Toast.makeText(RegisterActivity.this, "SOS", Toast.LENGTH_SHORT).show();

                        fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "create success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "create Fail", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Pass không giống nhau", Toast.LENGTH_SHORT).show();
                        editRePass.selectAll();
                        editRePass.requestFocus();
                    }
                }
            }
        });

        TextView btnReg = findViewById(R.id.textViewSignIn);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
            //Intent intent=new Intent(RegisterActivity.this,RegisterActivity.class);
            //startActivity(intent);

        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            new RegisterActivity();
            account = GoogleSignIn.getLastSignedInAccount(RegisterActivity.this);
            editName.setText(account.getDisplayName());
            editEmail.setText(account.getEmail());
            Toast.makeText(RegisterActivity.this,"Register success:  "+account.getEmail(),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(RegisterActivity.this,"Register by google cancel",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
