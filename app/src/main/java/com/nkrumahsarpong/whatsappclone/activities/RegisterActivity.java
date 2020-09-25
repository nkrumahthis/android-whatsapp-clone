package com.nkrumahsarpong.whatsappclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nkrumahsarpong.whatsappclone.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    // declare views
    EditText etPassword, etUsername, etEmail;
    Button btRegister, btLogin;
    ProgressBar progressBar;

    // Firebase
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);

        btRegister = findViewById(R.id.btRegister);
        btRegister.setOnClickListener(register);

        btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(goToLogIn);

        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        hideProgressBar();
    }

    View.OnClickListener register = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showProgressBar();

            final String password = etPassword.getText().toString();
            final String email = etEmail.getText().toString();
            final String username = etUsername.getText().toString();

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    hideProgressBar();

                    if(task.isSuccessful()){
                        String userid = auth.getCurrentUser().getUid();
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userid);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", userid);
                        map.put("username", username);
                        map.put("imageUrl", "default");
                        usersRef.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Error writing user data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error Registering", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    View.OnClickListener goToLogIn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    };

    private void showProgressBar(){
        btRegister.setVisibility(View.INVISIBLE);
        btLogin.setVisibility(View.INVISIBLE);
        (findViewById(R.id.tvGoToLogin)).setVisibility(View.INVISIBLE);

        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        btRegister.setVisibility(View.VISIBLE);
        btLogin.setVisibility(View.VISIBLE);
        (findViewById(R.id.tvGoToLogin)).setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.INVISIBLE);
    }

}