package com.example.sisirkumarnanda.tourist.Model;

/**
 * Created by user on 7/7/2018.
 */

public class UserRegistration {
    String fname , lname,country,cause;

    public UserRegistration(String fname, String lname, String country, String cause) {
        this.fname = fname;
        this.lname = lname;
        this.country = country;
        this.cause = cause;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
