package com.example.developer.cloudprint.services;


import android.content.Context;
import android.util.Log;

import com.example.developer.cloudprint.model.Customer;
import com.example.developer.cloudprint.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class CustomerServiceImpl implements CustomerService {
    public CustomerServiceImpl() {
    }

    @Override
    public JSONObject getMapDetails(final User customer, Context context, final Customer cs) throws UnsupportedEncodingException {
        final JSONObject json = null;
        //  System.out.println("inside  "+customer.getToken());
        //  System.out.println("inside  "+customer.getEmail());


        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonO = gson.toJson(customer);
        Log.i("JSON", jsonO);
        StringEntity entity = new StringEntity(jsonO);

        UserClient.addHeader("X-Auth-Token", customer.getToken());
        UserClient.get(context, "api/customers/all/", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseString) {
                String str;
                str = new String(responseString);
                System.out.println("log ");

                System.out.println("log " + str);

                try {
                    JSONObject jsonObj = new JSONObject(str);
                    System.out.println("json object ::: " + jsonObj);

                    cs.setMapDetails(jsonObj);


                } catch (Exception e) {
                    Log.e("Json", "Error");
                }

                for (Header header : headers) {
                    if (header.getName().equals("X-Auth-Token")) {
                        Log.i("header", "Key : " + header.getName()
                                + " ,Value : " + header.getValue());
                    }
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Log.e("Post", "failure123: " + new String(responseBody));
                Log.e("Post", "failurecode123: " + statusCode);


            }
        });


        return json;
    }


}

