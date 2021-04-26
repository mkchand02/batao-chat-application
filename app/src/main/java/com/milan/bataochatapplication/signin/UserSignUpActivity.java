package com.milan.bataochatapplication.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.milan.bataochatapplication.HomeActivity;
import com.milan.bataochatapplication.R;
import com.milan.bataochatapplication.models.UserModel;
import com.milan.bataochatapplication.utils.SharedPrefHelper;

import java.util.ArrayList;

public class UserSignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText etUserFirstName,etUserLastName,etUserPhone,etUserEmail,etUsername, etUserPassword, etUserConfirmPassword;
    private TextView tvSignin;
    private Button btnUserSubmit;
    private Spinner spnUserState;
    private ProgressDialog progressDialog;
    private String firstName, lastName, type, phone, email, state, username, password, gender;
    private SharedPrefHelper sph;
    private RadioGroup rbGender;
    private RadioGroup rbType;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sph = new SharedPrefHelper(getApplicationContext());//Shared Preference
        mDatabase = FirebaseDatabase.getInstance().getReference();//Db Reference
        firebaseAuth = FirebaseAuth.getInstance();//firebase auth
        if(firebaseAuth.getCurrentUser()!=null) {
            Intent intent = new Intent(UserSignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_user_sign_up);
        //Intent to get type from previous activity

        setupForm();//Initialises the ui
    }

    private void setupForm(){
        setTitle("USER REGISTRATION");
        etUserFirstName = findViewById(R.id.etUserFirstName);
        etUserLastName = findViewById(R.id.etUserLastName);
        rbGender = findViewById(R.id.gender_rg);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserPassword = findViewById(R.id.et_password);
        etUserConfirmPassword = findViewById(R.id.et_c_password);
        etUsername = findViewById(R.id.etUsername);
        etUserPhone = findViewById(R.id.etUserPhone);
        tvSignin = findViewById(R.id.tv_signin);
        spnUserState = findViewById(R.id.spn_state_select);
        btnUserSubmit = findViewById(R.id.btnUserSubmit);
        progressDialog = new ProgressDialog(this);
        //Setup List of States
        ArrayList<String> states = new ArrayList<>();
        states.add("Select State/Union Territory");
        states.add("Maharashtra");
        states.add("Andaman and Nicobar Islands");
        states.add("Andhra Pradesh");
        states.add("Arunachal Pradesh");
        states.add("Assam");
        states.add("Bihar");
        states.add("Chhattisgarh");
        states.add("Dadra and Nagar Haveli");
        states.add("Daman and Diu");
        states.add("Delhi");
        states.add("Goa");
        states.add("Gujarat");
        states.add("Haryana");
        states.add("Himachal Pradesh");
        states.add("Jammu and Kashmir");
        states.add("Jharkhand");
        states.add("Karnataka");
        states.add("Kerala");
        states.add("Lakshadweep");
        states.add("Madhya Pradesh");
        states.add("Manipur");
        states.add("Meghalaya");
        states.add("Mizoram");
        states.add("Nagaland");
        states.add("Odisha");
        states.add("Punjab");
        states.add("Rajasthan");
        states.add("Sikkim");
        states.add("Tamil Nadu");
        states.add("Telangana");
        states.add("Tripura");
        states.add("Uttar Pradesh");
        states.add("Uttarakhand");
        states.add("West Bengal");

        ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,states);
        spnUserState.setAdapter(statesAdapter);
        spnUserState.setOnItemSelectedListener(this);
        btnUserSubmit.setOnClickListener(this);
        tvSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //Form Validation on clicking otp and submission button
            switch (v.getId()){
                case R.id.btnUserSubmit:

                    if(isValidForm())
                    if(type.equalsIgnoreCase("user")) {
                        registerUser();
                    }


                case R.id.tv_signin:
                    Intent i = new Intent(UserSignUpActivity.this,SignInActivity.class);
                    startActivity(i);
                    finish();
                    break;
            }
    }

    private boolean isValidForm() {

        firstName = etUserFirstName.getText().toString();
        if(firstName.length()==0)
        {
            etUserFirstName.setError("First name empty");
            etUserFirstName.requestFocus();
            return false;
        }

        lastName = etUserLastName.getText().toString();
        if(lastName.length()==0)
        {
            etUserLastName.setError("Last name empty");
            etUserLastName.requestFocus();
            return false;
        }

        gender = rbGender.getCheckedRadioButtonId()==R.id.gender_male?"Male":
                (rbGender.getCheckedRadioButtonId()==R.id.gender_female?"Female":"Other");

        username = etUsername.getText().toString();
        if(username.length()==0)
        {
            etUsername.setError("Username empty");
            etUsername.requestFocus();
            return false;
        }

        phone = etUserPhone.getText().toString();
        if(phone.length()==0)
        {
            etUserPhone.setError("Phone number empty");
            etUserPhone.requestFocus();
            return false;
        }
        if(!isValidPhoneNumber(phone))
        {
            etUserPhone.setError("Invalid Phone Number");
            etUserPhone.requestFocus();
            return false;
        }
        email = etUserEmail.getText().toString();

        if(email.length()==0) {
            etUserEmail.setError("Email empty.");
            etUserEmail.requestFocus();
            return false;
        }

        if(!isValidEmail(email))
        {
            etUserEmail.setError("Not a valid email.");
            etUserEmail.requestFocus();
            return false;
        }

        password = etUserPassword.getText().toString();
        String confirmPassword = etUserConfirmPassword.getText().toString();
        if(TextUtils.isEmpty(password)) {
            etUserEmail.setError("Please enter a Password.");
            etUserEmail.requestFocus();
            return false;
        }if(TextUtils.isEmpty(confirmPassword)) {
            etUserEmail.setError("Please confirm your Password");
            etUserEmail.requestFocus();
            return false;
        }if(password.length()<8){
            Toast.makeText(this,"Password should be minimum 8 characters long",Toast.LENGTH_LONG).show();
            return false;
        }if(!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and Confirm Password are not same.", Toast.LENGTH_SHORT).show();
            etUserConfirmPassword.setText("");
            return false;
        }

            if (spnUserState.getSelectedItem().toString().trim().equals("Select State/Union Territory")) {
            Toast.makeText(UserSignUpActivity.this, "Error - Choose a state", Toast.LENGTH_SHORT).show();
            return false;
        }

        type = "user";
        return true;
    }

    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    private void registerUser()
    {
        final String name = firstName + " " + lastName;


        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // user is successfully registered
                            final UserModel user = new UserModel(
                                    firstName, lastName, username, gender, email, type, phone, state
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        sph.storeUserDataOnSignUp(firstName, lastName, gender, email, type, username, phone, state);
                                        UserModel udm = new UserModel(firstName, lastName, username, gender, email, type, phone, state);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(udm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(UserSignUpActivity.this,"Registration Successful", Toast.LENGTH_SHORT);
                                                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                                                    finish();
                                                } else {
                                                    Log.d("Register Activity Inner",task.getResult().toString());
                                                    Toast.makeText(UserSignUpActivity.this," Could not register.Please try again",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        Toast.makeText(UserSignUpActivity.this,"Registration Successful", Toast.LENGTH_SHORT);
                                        startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                                        finish();
                                    } else {
                                        Log.d("Register Activity Inner",task.getResult().toString());
                                        Toast.makeText(UserSignUpActivity.this," Could not register.Please try again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Log.d("Register Activity",task.getResult().toString());
                            Toast.makeText(UserSignUpActivity.this," Could not register.Please try again",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }


                });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spn_state_select) {
            state = spnUserState.getSelectedItem().toString().trim();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}