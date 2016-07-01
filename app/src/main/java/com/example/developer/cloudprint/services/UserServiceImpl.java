package com.example.developer.cloudprint.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.developer.cloudprint.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import org.json.*;

import java.io.UnsupportedEncodingException;


public class UserServiceImpl implements UserService {

    public UserServiceImpl() {

    }

    @Override
    public void login(final User user, final Context context) {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("email", user.getEmail());
            jsonParams.put("password", user.getPassword());
        } catch (Exception e) {
            Log.e("JsonParam", "Unable to construct JSON param object");
        }

        String jsonString = jsonParams.toString();
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonString);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        UserClient.post(context, "auth/app", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseString) {
                //super.onSuccess(statusCode, headers, responseString);
                if (statusCode == 200) {
                    String str;
                    String email = "", id = "", fname = "", lname = "";
                    str = new String(responseString);

                    try {
                        JSONObject jsonObj = new JSONObject(str);
                        email = jsonObj.getString("email");
                        id = jsonObj.getString("_id");

                        // fname = jsonObj.getString("first_name");
                        // lname = jsonObj.getString("last_name");

                    } catch (Exception e) {
                        Log.e("Json", "Error");
                    }

                    if (email != "") {
                        Log.i("Login", "Successfully logged in");
                        user.set_id(id);
                        user.setFirstName(fname);
                        user.setLastName(lname);
                        for (Header header : headers) {
                            if (header.getName().equals("X-Auth-Token")) {
                                user.setToken(header.getValue());
                            }

                        }
                    } else {
                        Log.i("Login", "Email not found");
                    }

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Log.e("Post", "failure: " + new String(responseBody));
                Log.e("Post", "failurecode: " + statusCode);
                String str = new String(responseBody);
                String message = "";

                try {
                    JSONObject resp = new JSONObject(str);
                    message = resp.getString("message");
                } catch (Exception e) {
                    Log.e("Failure Response", "Error");
                }
                if (statusCode == 401) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

                //super.onFailure(statusCode, headers, responseBody, throwable);
            }


        });

    }


    @Override
    public void register(final User user, final Context context) {
        try {

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(user);
            Log.i("JSON", json);
            StringEntity entity = new StringEntity(json);

            UserClient.post(context, "api/users", entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseString) {
                    //super.onSuccess(statusCode, headers, responseString);
                    if (statusCode == 200) {
                        String str = new String(responseString);

                        for (Header header : headers) {
                            if (header.getName().equals("X-Auth-Token")) {
                                Log.i("header", "Key : " + header.getName() + " ,Value : " + header.getValue());
                            }

                        }
                        String id = "";
                        try {
                            JSONObject jsonObj = new JSONObject(str);
                            id = jsonObj.getString("_id");

                        } catch (Exception e) {
                            Log.e("Json", "Error");
                        }
                        Log.i("ID", id);
                        user.set_id(id);
                        Log.i("Post", "Successfully posted registration records");

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                    Log.e("Post", "failure: " + new String(responseBody));
                    Log.e("Post", "failurecode: " + statusCode);

                    String str = new String(responseBody);
                    String message = "";

                    try {
                        JSONObject resp = new JSONObject(str);
                        message = resp.getString("message");
                    } catch (Exception e) {
                        Log.e("Failure Response", "Error");
                    }
                    if (statusCode == 401 || statusCode == 422) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    //super.onFailure(statusCode, headers, responseBody, throwable);
                }


            });
        } catch (Exception e) {
            Log.e("Post", "Error");
        }
    }


    @Override
    public void history(final User user, final Context context) {


        JSONObject userParams = new JSONObject();
        JSONObject finalParams = new JSONObject();
        try {

            userParams.put("_id", user.get_id());
            finalParams.put("user", userParams);

        } catch (Exception e) {
            Log.e("JsonParam", "Unable to construct JSON param object");
        }


        String jsonString = finalParams.toString().replace("\\", "");
        Log.i("jsonString", jsonString);
        System.out.println("history json before post" + jsonString);

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonString);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        System.out.println(user.getToken());
        UserClient.addHeader("X-Auth-Token", user.getToken());
        UserClient.post(context, "/api/users/authenticateToken", entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseString) {
                //super.onSuccess(statusCode, headers, responseString);
                if (statusCode == 200) {
                    String str = new String(responseString);
                    System.out.println("Inside success history json" + responseString);
                    try {
                        JSONObject jsonObj = new JSONObject(str);
                        System.out.println("json object ::: " + jsonObj);

                        user.setUserDetails(jsonObj);

                        JSONObject userDetails1 = user.getUserDetails();
                        System.out.println("Inside USERSERVICE :" + userDetails1);

                    } catch (Exception e) {
                        Log.e("Json", "Error");
                    }


                    String id = "";
                    try {
                        JSONObject jsonObj = new JSONObject(str);
                        id = jsonObj.getString("_id");

                    } catch (Exception e) {
                        Log.e("Json", "Error");
                    }
                    Log.i("ID", id);
                    Log.i("Post", "Successfully posted user records");

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Log.e("Post", "failure: " + new String(responseBody));
                Log.e("Post", "failurecode: " + statusCode);

                String str = new String(responseBody);
                System.out.println("history json" + responseBody);
                String message = "";

                try {
                    JSONObject resp = new JSONObject(str);
                    message = resp.getString("message");
                } catch (Exception e) {
                    Log.e("Failure Response", "Error");
                }
                if (statusCode == 401 || statusCode == 422) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

                //super.onFailure(statusCode, headers, responseBody, throwable);
            }


        });
    }

}
