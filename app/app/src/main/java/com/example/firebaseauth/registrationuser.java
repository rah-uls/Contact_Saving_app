package com.example.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class registrationuser extends AppCompatActivity {
    private EditText regName, regEmail, regPass;
    private Button register;
    private TextView login;
    private String name, email, pass;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrationuser);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        register = findViewById(R.id.userRegister);
        login = findViewById(R.id.openLogin);
        auth=FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registrationuser.this, loginactivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void validateUser() {

        name = regName.getText().toString();
        email = regEmail.getText().toString();
        pass = regPass.getText().toString();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            registerUser();
        }
    }

    private void registerUser() {
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(registrationuser.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    updateUser();
                }
                else {
                    Toast.makeText(registrationuser.this, "failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUser() {
        UserProfileChangeRequest usr=new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        auth.getCurrentUser().updateProfile(usr);
        auth.signOut();
        openlogin();
    }

    private void openlogin() {
        startActivity(new Intent(registrationuser.this,loginactivity.class));
        finish();
    }
}