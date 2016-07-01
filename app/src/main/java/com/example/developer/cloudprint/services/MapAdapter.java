package com.example.developer.cloudprint.services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developer.cloudprint.R;

import java.util.ArrayList;
import java.util.Map;

public class MapAdapter extends BaseAdapter {
    ArrayList<MapContent> myList = new ArrayList<MapContent>();

    Context context;

    public MapAdapter(Context context, ArrayList<MapContent> myList) {
        this.myList = myList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public MapContent getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class MyViewHolder {
        TextView printerId, customerId, dist, printerName, customerName, price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_style, null);
            mViewHolder = new MyViewHolder();


            mViewHolder.printerName = (TextView) convertView.findViewById(R.id.textView1);
            mViewHolder.customerName = (TextView) convertView.findViewById(R.id.textView2);
            mViewHolder.price = (TextView) convertView.findViewById(R.id.textView3);
            mViewHolder.dist = (TextView) convertView.findViewById(R.id.textView4);

            mViewHolder.printerId = (TextView) convertView.findViewById(R.id.textView5);
            mViewHolder.customerId = (TextView) convertView.findViewById(R.id.textView6);


            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        MapContent mc = getItem(position);


        mViewHolder.printerId.setText(mc.getPrinterId());
        mViewHolder.price.setText(Integer.toString(mc.getPrice()));
        mViewHolder.customerId.setText(mc.getCustomerId());
        mViewHolder.dist.setText(mc.getDist() + " " + "miles");

        mViewHolder.printerName.setText(mc.getPrinterName());
        mViewHolder.customerName.setText(mc.getCustomerName());

        return convertView;

    }
}
