package com.example.developer.cloudprint.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.developer.cloudprint.model.Document;
import com.example.developer.cloudprint.model.User;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class MapServiceImpl implements MapService {

    public MapServiceImpl() {
    }

    @Override
    public void postPrinter(final User user, final Document doc, final Context context, final MapContent mapContent) {
        JSONObject printerParams = new JSONObject();


        try {
            printerParams.put("userId", user.get_id());
            printerParams.put("documentId", doc.get_id());
            printerParams.put("printerId", mapContent.getPrinterSelectedId());
            printerParams.put("customerId", mapContent.getPrinterSelectedCustomer());
            printerParams.put("copies", mapContent.getPrinterSelectedCopy());


        } catch (Exception e) {
            Log.e("JsonParam", "Unable to construct JSON param object");
        }


        String jsonString = printerParams.toString().replace("\\", "");
        Log.i("jsonString", jsonString);

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonString);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        UserClient.addHeader("X-Auth-Token", user.getToken());
        UserClient.post(context, "api/users/transaction/", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseString) {
                //super.onSuccess(statusCode, headers, responseString);
                String str;
                str = new String(responseString);
                mapContent.setResponse(str);
                Log.i("Success", str);
                try {
                    JSONObject jsonObj = new JSONObject(str);

                } catch (Exception e) {
                    Log.e("Json", "Error");
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
                    message = resp.getString("error");
                } catch (Exception e) {
                    Log.e("Failure Response", "Error");
                }
                if (statusCode == 400) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

                //super.onFailure(statusCode, headers, responseBody, throwable);
            }


        });

    }


}

