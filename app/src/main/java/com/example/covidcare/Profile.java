package com.example.covidcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private EditText Fname, Email, PhoneNumber,  Dob;
    private Button Submitbtn;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseUser current_user;
    DatabaseReference reference;
    User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_drawer_profile, container, false);

        Fname = view.findViewById(R.id.fNameEditText_update);
        Email = view.findViewById(R.id.emailEditText_update);
        PhoneNumber = view.findViewById(R.id.phoneEditText_update);

        Dob = view.findViewById(R.id.Dob_editText_update);
        // user = new User();
        Submitbtn = view.findViewById(R.id.SubmitBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        reference = database.getReference("User");

        viewProfile();

        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                * First check if user has changed information into our app
                * Update user information to database only if it's changed
                */
                updateProfile();



            }
        });


        return view;
    }


    public boolean isNameChanged() {
        if (Fname != (Fname.getEditableText())) {
            reference.child(current_user.getUid()).child(user.getFullName()).setValue(Fname.getText().toString());
            return true;
        }
        return false;

    }

    public boolean isEmailChanged() {
        if (Email != (Email.getEditableText())) {
            reference.child(current_user.getUid()).child("email").setValue(Email.getText().toString());
            return true;
        }
        return false;

    }

    public boolean isPhoneChanged() {
        if (PhoneNumber != (PhoneNumber.getEditableText())) {
            reference.child(current_user.getUid()).child("phoneNumber").setValue(PhoneNumber.getText().toString());
            return true;
        }
        return false;
    }



    public boolean isDobChanged() {
        if (Dob != (Dob.getEditableText())) {
            reference.child(current_user.getUid()).child("dob").setValue(Dob.getText().toString());
            return true;
        }
        return false;

    }

    public void updateProfile(){
        if (isNameChanged() || isEmailChanged() || isPhoneChanged() || isDobChanged()) {
            Toast.makeText(getContext(), "Information updated!", Toast.LENGTH_SHORT).show();
            //getChildFragmentManager().beginTransaction().replace(R.id.frame_layout,new fragment_buttom_home()).commit();
            Intent intent = new Intent(getContext(), dashboard.class);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "No new information.", Toast.LENGTH_SHORT).show();
        }

    }

    public void viewProfile(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                user = snapshot.child(current_user.getUid()).getValue(User.class);

                Fname.setText(user.getFullName());
                Email.setText(user.getEmail());
                PhoneNumber.setText(user.getPhoneNumber());
                Dob.setText(user.getDob());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });

    }



}