package com.example.developer.cloudprint.services;

import java.util.Comparator;


public class HistoryContent {
    private String printerName;
    private String printerId;
    private String customerId;
    private String printerSelectedId;
    private String printerSelectedCustomer;
    private String response;
    private String date;
    private int printerSelectedPrice;
    private int printerSelectedCopy;
    private int price;
    private String Name1;
    private String Name2;


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

    public int getPrinterSelectedPrice() {
        return printerSelectedPrice;
    }

    public int getPrinterSelectedCopy() {
        return printerSelectedCopy;
    }

    public String getPrinterSelectedId() {
        return printerSelectedId;
    }


    public String getPrinterSelectedCustomer() {
        return printerSelectedCustomer;
    }


    public void setPrinterSelectedPrice(int printerSelectedPrice) {
        this.printerSelectedPrice = printerSelectedPrice;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
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

    private String DocUrl;

    public String getDocUrl() {
        return DocUrl;
    }

    public void setDocUrl(String DocUrl) {
        this.DocUrl = DocUrl;
    }

    private String DocId;

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String DocId) {
        this.DocId = DocId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


