package com.example.covidcare;

public class userInput {
    private String Status;
    private String Volunteer;

    public userInput() {
    }

    public userInput(String status, String volunteer) {
        Status = status;
        Volunteer = volunteer;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getVolunteer() {
        return Volunteer;
    }

    public void setVolunteer(String volunteer) {
        Volunteer = volunteer;
    }
}


