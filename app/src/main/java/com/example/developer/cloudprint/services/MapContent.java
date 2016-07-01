package com.example.developer.cloudprint.services;

import java.util.Comparator;

public class MapContent {
    private String printerName;
    private String printerId;
    private String customerId;
    private String printerSelectedId;
    private String printerSelectedCustomer;
    private String Name1;
    private String Name2;
    private String CustomerName;

    private String printerSelectedPrinterName;
    private String response;
    private int printerSelectedPrice;
    private int printerSelectedCopy;
    private int price;


    public int getPrinterSelectedPrice() {
        return printerSelectedPrice;
    }

    public int getPrinterSelectedCopy() {
        return printerSelectedCopy;
    }

    public String getPrinterSelectedId() {
        return printerSelectedId;
    }

    public String getprinterSelectedPrinterName() {
        return printerSelectedId;
    }


    public String getPrinterSelectedCustomer() {
        return printerSelectedCustomer;
    }


    public void setPrinterSelectedPrice(int printerSelectedPrice) {
        this.printerSelectedPrice = printerSelectedPrice;
    }

    public void setprinterSelectedPrinterName(String printerSelectedPrinterName) {
        this.printerSelectedPrinterName = printerSelectedPrinterName;
    }


    public String getPrinterSelectedName2() {
        return Name2;
    }

    public void setPrinterSelectedName2(String Name2) {
        this.Name2 = Name2;
    }

    public String getPrinterSelectedName1() {
        return Name1;
    }

    public void setPrinterSelectedName1(String Name1) {
        this.Name1 = Name1;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setPrinterSelectedCopy(int printerSelectedCopy) {
        this.printerSelectedCopy = printerSelectedCopy;
    }

    public void setprinterSelectedId(String printerSelectedId) {
        this.printerSelectedId = printerSelectedId;
    }

    public void setPrinterSelectedCustomer(String printerSelectedCustomer) {
        this.printerSelectedCustomer = printerSelectedCustomer;
    }


    public String getPrinterName() {
        return printerName;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPrinterId() {
        return printerId;
    }

    public int getPrice() {
        return price;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }

    private double dist;

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public static Comparator<MapContent> COMPARE_BY_DIST = new Comparator<MapContent>() {
        public int compare(MapContent one, MapContent other) {
            Double obj1 = new Double(one.getDist());
            Double obj2 = new Double(other.getDist());
            return obj1.compareTo(obj2);
        }
    };


}
