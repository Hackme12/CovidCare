package com.example.covidcare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;


public class Status extends Fragment {

    private Button btnNext;
    private TextView tv_cvd_volunteer;
    private RadioGroup Rbtn_volunteer;
    boolean status_check = false;
    boolean volunteer_check = false;
    boolean check_if_selected = false;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference refExposedArea;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_status, container, false);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_status);
        btnNext = (Button) view.findViewById(R.id.btn_status_next);
        tv_cvd_volunteer = (TextView) view.findViewById(R.id.text_cvd_volunteer);
        Rbtn_volunteer = (RadioGroup) view.findViewById(R.id.radio_volunteer);
        btnNext.setVisibility(View.INVISIBLE);

        tv_cvd_volunteer.setVisibility(View.INVISIBLE);
        Rbtn_volunteer.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        refExposedArea = database.getReference().child("Exposed Area");
        currentUser = auth.getCurrentUser();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.radio_status_btn_yes:

                        tv_cvd_volunteer.setVisibility(View.VISIBLE);
                        Rbtn_volunteer.setVisibility(View.VISIBLE);
                        status_check = true;
                        btnNext.setVisibility(View.VISIBLE);

                        break;
                    case R.id.radio_status_btn_no:
                        tv_cvd_volunteer.setVisibility(View.INVISIBLE);
                        Rbtn_volunteer.setVisibility(View.INVISIBLE);
                        status_check = false;
                        btnNext.setVisibility(View.VISIBLE);

                        break;


                }
            }
        });


        Rbtn_volunteer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_volunteer_btn_yes:
                        volunteer_check = true;
                        btnNext.setVisibility(View.VISIBLE);

                        break;
                    case R.id.radio_volunteer_btn_no:
                        volunteer_check = false;
                        btnNext.setVisibility(View.VISIBLE);

                        break;


                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status_check && volunteer_check) {

                    /*
                        * Update user Covid status such as tested positive or tested negative and either yes or no to volunteer
                     */
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("User").child(currentUser.getUid()).exists()) {
                                reference.child("User").child(currentUser.getUid()).child("Covid Check").child("Status").setValue("Positive");
                                reference.child("User").child(currentUser.getUid()).child("Covid Check").child("Volunteer").setValue("Yes");
                            } else {
                                Toast.makeText(getContext(), "User not found in database!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    getFragmentManager().beginTransaction().replace(R.id.frame_layout, new Status_Next()).commit();
                } else if (status_check && !volunteer_check) {

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.child("User").child(currentUser.getUid()).exists()) {
                                reference.child("User").child(currentUser.getUid()).child("Covid Check").child("Status").setValue("Positive");
                                reference.child("User").child(currentUser.getUid()).child("Covid Check").child("Volunteer").setValue("No");
                            } else {
                                Toast.makeText(getContext(), "User not found in database!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    getFragmentManager().beginTransaction().replace(R.id.frame_layout, new Status_Next_No()).commit();
                } else if (!status_check && !volunteer_check) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.child("User").child(currentUser.getUid()).exists()) {
                                reference.child("User").child(currentUser.getUid()).child("Covid Check").child("Status").setValue("Negative");
                                reference.child("User").child(currentUser.getUid()).child("Covid Check").child("Volunteer").setValue("No");


                            } else {
                                Toast.makeText(getContext(), "User not found in database!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Query query = refExposedArea.orderByChild("Uid").equalTo(currentUser.getUid());
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String exposedArea = ds.getKey();
                                refExposedArea.removeValue();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
                        }
                    };
                    query.addListenerForSingleValueEvent(valueEventListener);

                    new AlertDialog.Builder(getContext())
                            .setMessage("Thank you for your information. Please stay safe and healthy!!")
                            .setPositiveButton("               OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getContext(), dashboard.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .setCancelable(false)
                            .show()
                    ;


                }


            }
        });

        return view;
    }


}