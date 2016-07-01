package com.example.developer.cloudprint.services;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.developer.cloudprint.model.Customer;
import com.example.developer.cloudprint.model.User;

import java.io.UnsupportedEncodingException;

/**
 * Created by piyus on 11/10/2015.
 */
public interface  CustomerService {

    public JSONObject getMapDetails(final User customer, Context context, final Customer cs) throws JSONException, UnsupportedEncodingException;


}