package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity implements View.OnClickListener {
    TextView txtSignin;
    private static final String TAG = "signup";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtName;
    private EditText edtPhone;
    private Button btnSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // diatur sesuai id komponennya
        edtEmail = (EditText)findViewById(R.id.etEmail_Signup);
        edtPass = (EditText)findViewById(R.id.etPassword_Signup);
        edtName = (EditText)findViewById(R.id.etName);
        edtPhone = (EditText)findViewById(R.id.etPhone);
        btnSignup = (Button)findViewById(R.id.btn_Signup);
        txtSignin = findViewById(R.id.txt_Signin);
        btnSignup.setOnClickListener(this);
        txtSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(signup.this, LoginPage.class);
                startActivity(signup);
            }
        });
    }
    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }
        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        //hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(signup.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void onAuthSuccess(FirebaseUser user) {
        // membuat User
        writeNewUser(user.getUid(), user.getEmail(), edtName.getText().toString(),edtPhone.getText().toString());
        // Go to MainActivity
        startActivity(new Intent(signup.this, LoginPage.class));
        finish();
    }
    private void writeNewUser(String uid, String Email, String Name, String Nphone) {
        User user = new User(Email,Name,Nphone);
        mDatabase.child("user").child(uid).setValue(user);
    }
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(edtName.getText().toString())) {
            edtName.setError("Required");
            result = false;
        } else {
            edtName.setError(null);
        }

        if (TextUtils.isEmpty(edtPhone.getText().toString())) {
            edtPhone.setError("Required");
            result = false;
        } else {
            edtPhone.setError(null);
        }
        
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.setError("Required");
            result = false;
        } else {
            edtEmail.setError(null);
        }

        if (TextUtils.isEmpty(edtPass.getText().toString())) {
            edtPass.setError("Required");
            result = false;
        } else {
            edtPass.setError(null);
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_Signup) {
            signUp();
        }
    }
}