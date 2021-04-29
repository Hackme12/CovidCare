package com.example.covidcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText fullName, email, password, phoneNumber, Dob;
    String sex = " ";
    Button register_btn;
    RadioGroup radioGroup;
    ProgressDialog LoadingBar;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        LoadingBar = new ProgressDialog(this);

        setData();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FullName = fullName.getText().toString().trim();
                String Email = email.getText().toString().trim().toLowerCase();
                String Password = password.getText().toString().trim();
                String PhoneNumber = phoneNumber.getText().toString().trim();
                String DOB = Dob.getText().toString().trim();

                if (TextUtils.isEmpty(FullName) || (TextUtils.isEmpty(Email)) || (TextUtils.isEmpty(Password))
                        || (TextUtils.isEmpty(PhoneNumber))  || (TextUtils.isEmpty(DOB)) || (TextUtils.isEmpty(sex))) {
                    Toast.makeText(Register.this, "Please enter all the required information above.", Toast.LENGTH_LONG).show();
                } else if (!FullName.matches("^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)")) {
                    Toast.makeText(Register.this, "Please enter valid name", Toast.LENGTH_LONG).show();
                } else if (!Email.matches("[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    Toast.makeText(Register.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                } else if ((!(Password.matches("^[a-zA-Z0-9]*$"))
                        && !(Password.matches(".*[`~!@#$%^&*()\\\\-_=+\\\\\\\\|\\\\[{\\\\]};:'\\\",<.>/?].*")) && (Password.length() < 8))) {
                    Toast.makeText(Register.this, "The requirement of password are: \n " +
                            "At least 8 character long" +
                            "At least 1 digit and Symbol", Toast.LENGTH_SHORT).show();
                } else if (!PhoneNumber.matches("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$")) {
                    Toast.makeText(Register.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();

                }  else if (!DOB.matches("^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d$")) {
                    Toast.makeText(Register.this, "Please enter valid DOB: MM/DD/YYYY", Toast.LENGTH_SHORT).show();

                } else {
                    LoadingBar.setTitle("Processing...");
                    LoadingBar.setCanceledOnTouchOutside(false);
                    LoadingBar.show();
                    validation(FullName, Email, Password, PhoneNumber, DOB, sex);

                }
            }


        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.maleRbtn:
                        sex = "Male";
                        break;
                    case R.id.femaleRbtn:
                        sex = "Female";
                        break;
                    case R.id.otherRbtn:
                        sex = "Others";
                        break;
                }
            }
        });
    }

    private void validation(String fullName, String email, String password, String phoneNumber, String dob, String sex) {
        //Call method createuserWithEmailAndPassword(string,string) of firebase authentication library which check if the email enter by user is already exists or not
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // if the email doesn't exist in database and there is no any other problem then task will be successful
                if (task.isSuccessful()) {
                    User user = new User(fullName, email, phoneNumber, dob, sex);
                    // Verify email using sendEmailVerification() of Firebase Authentication library
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // User data will be update to the database
                                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //if task is successful user will be redirect to  login page
                                                    LoadingBar.dismiss();
                                                    Toast.makeText(Register.this, "Successfully Account Created! Please verify your email to login.",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Register.this, Login.class);
                                                    startActivity(intent);
                                                } else {
                                                    LoadingBar.dismiss();
                                                    Toast.makeText(Register.this, "Failed to update info", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(Register.this, "Email Doesn't Exist. Please use correct email address.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    //if user email enter wrong information then App will toast message saying "Account failed to create"
                    LoadingBar.dismiss();
                    Toast.makeText(Register.this, "Account failed to create", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void setData() {
        fullName = (EditText) findViewById(R.id.fNameEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        phoneNumber = (EditText) findViewById(R.id.phoneEditText);
        Dob = (EditText) findViewById(R.id.Dob_editText);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        register_btn = (Button) findViewById(R.id.RegisterBtn);
    }

}