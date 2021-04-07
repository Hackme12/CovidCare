package com.example.covidcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidcare.Review.ReviewAdapter;
import com.example.covidcare.Review.ReviewForm;

import java.util.ArrayList;


public class fragment_drawer_vaccine_review extends Fragment {


    RecyclerView recyclerView;
    ArrayList<ReviewForm> reviewList;
    ReviewAdapter reviewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer_vaccine_review, container, false);

        recyclerView = view.findViewById(R.id.review_recycle);
        reviewList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        String review = "HELLO THIS IS TEST";
        String UserName = "PRANAYA";
        ReviewForm reviewForm = new ReviewForm(review,UserName);
        ReviewForm reviewForm1 = new ReviewForm("TEST TEST TEST","BIBEK");
        reviewList.add(reviewForm);
        reviewList.add(reviewForm1);
        reviewAdapter = new ReviewAdapter(getContext(),reviewList);
        recyclerView.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();


        return view;
    }
}