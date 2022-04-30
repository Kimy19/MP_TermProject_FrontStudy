package com.example.wiuh;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.model.User;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;

    private EditText mEtEmail;
    private EditText mEtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth  = FirebaseAuth.getInstance();

        mEtEmail    = findViewById(R.id.et_userID);
        mEtPwd      = findViewById(R.id.et_password);

        findViewById(R.id.btn_login).setOnClickListener(v -> emailLogin());
        findViewById(R.id.btn_signin).setOnClickListener(v -> startSignUp());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!FirebaseUtil.isAvailable() || !isNetworkAvailable()) {
            ToastUtil.showText(this, "connection error");
            return;
        }

        if(mFirebaseAuth.getCurrentUser() != null) startMain();
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void emailLogin() {
        String strEmail = mEtEmail.getText().toString();
        String strPwd   = mEtPwd.getText().toString();

        mFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd)
                     .addOnCompleteListener(LoginActivity.this, task -> {
                        if(task.isSuccessful()) startMain();
                        else ToastUtil.showText(this, "Login Failed");
                        });
    }
}