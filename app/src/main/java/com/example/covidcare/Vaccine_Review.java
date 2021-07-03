package com.example.covidcare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidcare.Review.Review;
import com.example.covidcare.Review.ReviewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Vaccine_Review extends Fragment {


    RecyclerView recyclerView;
    ArrayList<Review> reviewList;
    ReviewAdapter reviewAdapter;
    EditText reviewWrite;
    Button btnSend;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    ProgressDialog loadingBar;
    FirebaseDatabase database;
    DatabaseReference reference;
    String userName;
    Review review;
    Review reviewObj;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer_vaccine_review, container, false);

        recyclerView = view.findViewById(R.id.review_recycle);
        reviewList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        reviewWrite = view.findViewById(R.id.ed_writeReview);
        btnSend = view.findViewById(R.id.btnSend);
        loadingBar = new ProgressDialog(getContext());
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        userName = Prevalent.currentOnlineUser.getFullName();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review = reviewWrite.getText().toString().trim();
                if(TextUtils.isEmpty(review)){
                    Toast.makeText(getContext(), "Review form can't be empty!", Toast.LENGTH_LONG).show();
                }
                else{

                    updateReviewToDatabase(currentUserId, userName, review);

                    loadingBar.setTitle("Review Submitting...");
                    loadingBar.setMessage("Thank you for your review! It really means a lot for your community.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.detach(Vaccine_Review.this);
                    fragmentTransaction.attach(Vaccine_Review.this);
                    fragmentTransaction.commit();
                    reviewWrite.setText("");
                }



            }
        });






        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        reference.child("Review").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {


                    for (DataSnapshot allReviewList : snapshot.getChildren()) {
                        review = allReviewList.getValue(Review.class);
                        reviewList.add(review);

                        reviewAdapter = new ReviewAdapter(getContext(), reviewList);
                        recyclerView.setAdapter(reviewAdapter);
                        reviewAdapter.notifyDataSetChanged();

                    }
                    for(int i =reviewList.size();i>0;i--){

                        reviewAdapter = new ReviewAdapter(getContext(), reviewList);
                        recyclerView.setAdapter(reviewAdapter);
                        reviewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //String review = "HELLO THIS IS TEST";
       // String UserName = "PRANAYA";

        //reviewAdapter = new ReviewAdapter(getContext(),reviewList);
        //recyclerView.setAdapter(reviewAdapter);
        //reviewAdapter.notifyDataSetChanged();


        return view;
    }

    private void updateReviewToDatabase(String currentUserId, String userName,String review) {
        // getting the current date which will be used to make primary key later in database
        Calendar calendar = Calendar.getInstance();
        String currentTime = DateFormat.getDateTimeInstance().format(calendar.getTime());
        String key = currentUserId +currentTime;
        // reference refer to the entity "Review Form" in our database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // updating the review provided by user and username into database
                reference.child("Review").child(key).child("username").setValue(userName);
                reference.child("Review").child(key).child("review").setValue(review);
                reference.child("Review").child(key).child("currenttime").setValue(currentTime);
                loadingBar.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
            }
        });

    }


}