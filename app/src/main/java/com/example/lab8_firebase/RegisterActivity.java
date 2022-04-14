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

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        fAuth = FirebaseAuth.getInstance();

        Button btn_register = findViewById(R.id.btnRegister_Registerpage);
        EditText editName = findViewById(R.id.txtName);
        EditText editEmail = findViewById(R.id.txtEmail_Registerpage);
        EditText editPass = findViewById(R.id.txtPassword_Registerpage_1);
        EditText editRePass = findViewById(R.id.txtPassword_Registerpage_2);

        if(fAuth.getCurrentUser() != null) {

        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String pass = editPass.getText().toString().trim();
                String rePass = editRePass.getText().toString().trim();

                if(pass.equals(rePass)) {
                    Toast.makeText(RegisterActivity.this, "SOS", Toast.LENGTH_SHORT).show();

                    fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "create success" , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "create Fail" , Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Pass không giống nhau", Toast.LENGTH_SHORT).show();
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
}
