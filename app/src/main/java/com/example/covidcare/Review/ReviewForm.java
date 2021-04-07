package com.example.covidcare.Review;

public class ReviewForm {
    private String review;
    private String userName;

    public ReviewForm() {
    }

    public ReviewForm(String review, String userName) {
        this.review = review;
        this.userName = userName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
