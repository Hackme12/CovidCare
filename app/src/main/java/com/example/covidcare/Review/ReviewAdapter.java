package com.example.covidcare.Review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidcare.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.myViewHolder> {
    Context context;
    ArrayList<Review>reviewForms;


    public ReviewAdapter(Context context, ArrayList<Review>reviewForms){
        this.context = context;
        this.reviewForms = reviewForms;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.myViewHolder holder, int position) {
        holder.review.setText(reviewForms.get(position).getReview());
        holder.userName.setText(reviewForms.get(position).getUsername());

    }

    @Override
    public int getItemCount() {
        return reviewForms.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView review;
        TextView userName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            review = itemView.findViewById(R.id.tv_review);
            userName = itemView.findViewById(R.id.tvUserName);
        }
    }
}
