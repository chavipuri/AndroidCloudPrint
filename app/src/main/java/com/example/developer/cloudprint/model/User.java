package com.example.developer.cloudprint.model;

import com.google.gson.annotations.Expose;

import org.json.JSONObject;

import java.io.Serializable;

@SuppressWarnings("serial") //with this annotation we are going to hide compiler warning
public class User implements Serializable {

    private String _id;
    private String token;
    private String historyDoc;
    @Expose
    private String email;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String password;
    @Expose
    private JSONObject UserDetails;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHistoryDoc() {
        return historyDoc;
    }

    public void setHistoryDoc(String historyDoc) {
        this.historyDoc = historyDoc;
    }


    public void setUserDetails(JSONObject json) {
        this.UserDetails = json;
    }

    ;

    public JSONObject getUserDetails() {
        return UserDetails;
    }

    ;


}

