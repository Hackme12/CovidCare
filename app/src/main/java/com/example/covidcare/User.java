package com.example.covidcare;

public class User {
    public String fullName;
    public String email;
    public String password;
    public String phoneNumber;
    public String emerPhone;
    public String dob;
    public String sex;

    public User(){

    }
    public User(String fullName, String email, String password, String phoneNumber, String emerPhone, String dob, String sex){
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.emerPhone = emerPhone;
        this.dob = dob;
        this.sex = sex;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmerPhone() {
        return emerPhone;
    }

    public void setEmerPhone(String emerPhone) {
        this.emerPhone = emerPhone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
