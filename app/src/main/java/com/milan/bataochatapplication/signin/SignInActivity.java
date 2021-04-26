package com.milan.bataochatapplication.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.milan.bataochatapplication.HomeActivity;
import com.milan.bataochatapplication.R;
import com.milan.bataochatapplication.utils.SharedPrefHelper;

public class SignInActivity extends AppCompatActivity {


    View view;
    EditText username, password;
    MaterialButton btnSignIn;
    TextView tvForgotPassword, tvRegisterUser;
    FirebaseDatabase database;
    DatabaseReference userRef;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_signin);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegisterUser = findViewById(R.id.tv_user_register);

        //database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, UserSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });





        btnSignIn.setOnClickListener(v -> {

            if(TextUtils.isEmpty(username.getText().toString().trim()) || TextUtils.isEmpty(password.getText().toString().trim())){
                makeToast("Empty Username or Password");
                return;
            }
            String passwordPlain = password.getText().toString().trim();
            String emailPlain = username.getText().toString().trim();

            //sign in user
            signinUser(emailPlain, passwordPlain);


        });
    }

    private void signinUser(String email, String password) {
        btnSignIn.setVisibility(View.INVISIBLE);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    btnSignIn.setVisibility(View.VISIBLE);
                    if(task.isSuccessful()){
                        makeToast("Login successful");
                        userRef = FirebaseDatabase.getInstance().getReference().child("users/" + auth.getCurrentUser().getUid() );
                        userRef.keepSynced(true);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    SharedPrefHelper sph = new SharedPrefHelper(getApplicationContext());
                                    //makeToast("sd");
                                    sph.saveUserProfile(dataSnapshot, email);

                                }else{
                                    makeToast("User data dose not exist, Please inform App Developers");
                                }
                                //user logged in
                                makeToast("Welcome " + new SharedPrefHelper(getApplicationContext()).getUserData("name"));
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else{
                        //mission failed ;)
                        makeToast("Sign In Failed, No Such User");
                    }
                });

    }




    private void makeToast(String text) {

        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

    }
}