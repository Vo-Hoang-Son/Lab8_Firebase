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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_screen);

        EditText Email = findViewById(R.id.txtEmail);
        EditText password = findViewById(R.id.txtPassword);

        fAuth = FirebaseAuth.getInstance();

        Button btnLogin = findViewById(R.id.btnSingIn_Singinpage);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Email.getText().toString().trim();
                String passwordFeild = password.getText().toString().trim();


                fAuth.signInWithEmailAndPassword(email, passwordFeild).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        int bool = 0;
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            bool = 1;
                        } else {
                            Toast.makeText(LoginActivity.this, "Error !!!", Toast.LENGTH_SHORT).show();
                        }
                        if(bool == 1){
                            Intent intent = new Intent(LoginActivity.this, com.example.lab8_firebase.FaceActivity.class);
                            startActivity(intent);
                        }
                    }
                });

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
}
