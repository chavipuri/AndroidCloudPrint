package com.example.developer.cloudprint.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import org.json.JSONObject;

public class Customer implements Serializable {

    @Expose
    private String _id;
    @Expose
    private String printerId;
    @Expose
    private long lat;
    @Expose
    private long long1;
    @Expose
    private String status;
    @Expose
    private String printerName;
    @Expose
    private JSONObject printerDetails;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String name) {
        this.printerName = printerName;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String url) {
        this.printerId = printerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String name) {
        this.status = status;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long name) {
        this.lat = lat;
    }

    public long getLong1() {
        return long1;
    }

    public void setLong1(long name) {
        this.long1 = long1;
    }

    public void setMapDetails(JSONObject json) {
        this.printerDetails = json;
    }

    ;

    public JSONObject getMapDetails() {
        return printerDetails;
    }

    ;


}

