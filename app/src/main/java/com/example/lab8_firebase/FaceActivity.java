package com.example.lab8_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FaceActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private FirebaseAuth fAuth;
    private GoogleSignInAccount account;
    ImageView btnHappy;
    ImageView btnUnHappy;
    ImageView btnNormal;
    DatabaseReference mDatasbase;
    int happy = 0;
    int normal = 0;
    int unhappy = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_screen);

        fAuth = FirebaseAuth.getInstance();
        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        Button btnLogin = findViewById(R.id.btnReturn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()){
                                    Toast.makeText(getApplicationContext(),"Account: " +account.getEmail()+" is close", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(FaceActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Session not close", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        btnHappy = findViewById(R.id.imgHappy);
        btnNormal = findViewById(R.id.imgNornal);
        btnUnHappy = findViewById(R.id.imgUnhappy);

        mDatasbase = FirebaseDatabase.getInstance().getReference("User");
        String userId = mDatasbase.push().getKey();
        mDatasbase.child(userId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                Log.d(String.valueOf(this), "User name: " + user.getName() + ", email " + user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(String.valueOf(this), "Failed to read value.", error.toException());

            }
        });
        btnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happy+=1;
                mDatasbase.child(userId).child("happy").setValue(happy).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Happy is update.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Happy don't update.", Toast.LENGTH_LONG).show();
                        }
                    }

                );
                Toast.makeText(getApplicationContext(),"Happy: "+happy, Toast.LENGTH_LONG).show();
            }
        });

        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatasbase.child(userId).child("normal").setValue(normal+=1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Normal is update.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Normal don't update.", Toast.LENGTH_LONG).show();
                    }
                }

                );
                Toast.makeText(getApplicationContext(),"Normal: "+normal, Toast.LENGTH_LONG).show();
            }
        });

        btnUnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatasbase.child(userId).child("unhappy").setValue(unhappy+=1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"UnHappy is update.", Toast.LENGTH_LONG).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"UnHappy don't update.", Toast.LENGTH_LONG).show();
                    }
                }
                );
                Toast.makeText(getApplicationContext(),"Unhappy: "+unhappy, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
            Toast.makeText(FaceActivity.this,"Sign in Successfully! Wellcome:  "+account.getDisplayName(),Toast.LENGTH_LONG).show();
            mDatasbase = FirebaseDatabase.getInstance().getReference("User");
            String userId = mDatasbase.push().getKey();
            String name = account.getDisplayName();
            String image = "";
            String email = account.getEmail();
            User user = new User(name, image, normal, happy, unhappy, email);
            mDatasbase.child(userId).setValue(user);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            account=result.getSignInAccount();

        }else{
            gotoMainActivity();
        }
    }

    private void gotoMainActivity(){
        Intent intent=new Intent(this,FaceActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
