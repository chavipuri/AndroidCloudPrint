package com.example.developer.cloudprint.services;

import android.content.Context;
import com.example.developer.cloudprint.model.User;
import com.example.developer.cloudprint.model.Document;


/**
 * Created by Chavi on 11/8/15.
 */
public interface MapService {
    public void postPrinter(User user, Document doc, Context context, MapContent mapContent);
   // public void delete(User user, Document doc, Context context);
}
