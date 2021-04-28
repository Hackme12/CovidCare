package com.example.covidcare.Review;

public class Review {
    private String review;
    private String username;

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    private String currenttime;

    public Review() {
    }

    public Review(String review, String username) {
        this.review = review;
        this.username = username;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
