package com.example.developer.cloudprint.services;


import android.content.Context;

import com.example.developer.cloudprint.model.User;

import org.json.JSONException;


public interface UserService {
    public void login(User user, Context context);

    public void register(User user, Context context);

    public void history(User user, Context context);
}

