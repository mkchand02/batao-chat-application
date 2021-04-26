package com.milan.bataochatapplication.signin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.milan.bataochatapplication.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    View view;
    EditText etUserName;
    MaterialButton btnSubmit;
    TextView tvSignin;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        etUserName = view.findViewById(R.id.et_username);
        btnSubmit = view.findViewById(R.id.btn_change_password);
        tvSignin = view.findViewById(R.id.tv_login);

        if(auth.getCurrentUser() != null){
            etUserName.setText(auth.getCurrentUser().getEmail());
            etUserName.setEnabled(false);
        }

        tvSignin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });



        btnSubmit.setOnClickListener(v -> {
            if(TextUtils.isEmpty(etUserName.getText())){
                makeToast("Empty Username");
                return;
            }
            btnSubmit.setVisibility(View.INVISIBLE);
            String email=etUserName.getText().toString().trim();
            triggerPasswordReset(email);
        });
    }

    private void makeToast(String text) {

        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

    }

    private void triggerPasswordReset(String email){

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){
                        makeToast("Please check your e-mail inbox");
                        Intent intent = new Intent(ForgetPasswordActivity.this, ForgetPasswordActivity.class);
                        startActivity(intent);
                        finish();


                    }else{
                        makeToast("Error ocurred, please try later");
                        btnSubmit.setVisibility(View.VISIBLE);
                    }


                });

    }
}