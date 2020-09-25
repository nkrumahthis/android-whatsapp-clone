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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nkrumahsarpong.whatsappclone.R;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btLogin, btRegister;
    ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(logIn);

        btRegister = findViewById(R.id.btRegister);
        btRegister.setOnClickListener(goToRegister);

        progressBar = findViewById(R.id.progressBar);
        hideProgressBar();
    }

    private View.OnClickListener logIn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showProgressBar();

            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    hideProgressBar();

                    if(task.isSuccessful()){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };



    private View.OnClickListener goToRegister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    };

    private void showProgressBar(){
        btRegister.setVisibility(View.INVISIBLE);
        btLogin.setVisibility(View.INVISIBLE);
        (findViewById(R.id.tvGoToRegister)).setVisibility(View.INVISIBLE);

        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        btRegister.setVisibility(View.VISIBLE);
        btLogin.setVisibility(View.VISIBLE);
        (findViewById(R.id.tvGoToRegister)).setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.INVISIBLE);
    }


}