package com.example.covidcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView createAccount;
    EditText Username;
    EditText Password;
    TextView ForgotPassword;
    Button Login;
    ProgressDialog progressDialog;
    FirebaseAuth authentication;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText)findViewById(R.id.usernameEditText);
        Password = (EditText)findViewById(R.id.passwordEditText);
        ForgotPassword =(TextView) findViewById(R.id.forgotPasswordTextView);
        Login = (Button)findViewById(R.id.loginBtn);
        progressDialog = new ProgressDialog(this);
        authentication =  FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = Username.getText().toString().trim().toLowerCase();
                String password = Password.getText().toString().trim();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Please enter your correct credentials.", Toast.LENGTH_SHORT).show();
                }

                else if (!username.matches("[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    Toast.makeText(Login.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                }
                else if ((!(password.matches("^[a-zA-Z0-9]*$"))
                        && !(password.matches(".*[`~!@#$%^&*()\\\\-_=+\\\\\\\\|\\\\[{\\\\]};:'\\\",<.>/?].*")) && (password.length()<8))) {
                    Toast.makeText(Login.this, "The requirement of password are: \n " +
                            "At least 8 character long" +
                            "At least 1 digit and Symbol", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    checkValid(username, password);
                }
            }
        });


        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBox();
            }
        });






        createAccount= (TextView)findViewById(R.id.createAccountTextView);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });


    }


    public void DialogBox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(Login.this).inflate(R.layout.forgot_password_dialog_box, null);
        builder.setView(view);
        EditText email = view.findViewById(R.id.editTextTextEmailAddress);

       // Button submit = view.findViewById(R.id.submitBtn);

        builder.setPositiveButton("SUBMIT                                       ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String Email = email.getText().toString().trim();
                if (TextUtils.isEmpty(Email)){
                    Toast.makeText(Login.this, "Enter your email", Toast.LENGTH_SHORT).show();
                }

            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();






    }



    private void checkValid(String username, String password) {

        authentication.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                   progressDialog.dismiss();
                   Intent intent = new Intent(Login.this,dashboard.class);
                   startActivity(intent);
               }
               else{
                   progressDialog.dismiss();
                   Toast.makeText(Login.this, "Please verify your email", Toast.LENGTH_SHORT).show();
               }
            }
        });


    }
}