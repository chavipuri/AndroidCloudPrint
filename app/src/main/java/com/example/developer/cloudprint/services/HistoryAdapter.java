package com.example.developer.cloudprint.services;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developer.cloudprint.R;
import com.example.developer.cloudprint.ui.History;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;


public class HistoryAdapter extends BaseAdapter {
    ArrayList<HistoryContent> myHList = new ArrayList<HistoryContent>();
    ImageLoaderConfiguration config;

    static DisplayImageOptions imgDisplayOptions = new DisplayImageOptions.Builder()
            //.showStubImage(R.drawable.stub_image)
            .cacheInMemory()
            .cacheOnDisc()
                    //.imageScaleType(ImageScaleType.EXACT)
            .build();
    //ImageLoader.getInstance().init(config);
    static ImageLoader imageLoader = ImageLoader.getInstance();

    Context context;

    public HistoryAdapter(Context context, ArrayList<HistoryContent> myHList) {
        this.myHList = myHList;
        this.context = context;
        config = new ImageLoaderConfiguration.Builder(this.context)
                .memoryCacheSize(41943040)
                .discCacheSize(104857600)
                .threadPoolSize(10)
                .build();
        imageLoader.init(config);
    }

    @Override
    public int getCount() {
        return myHList.size();
    }

    @Override
    public HistoryContent getItem(int position) {
        return myHList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class MyViewHolder {
        TextView printerId, customerId, copies, DocId, Date;
        ImageView DocUrl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder hViewHolder;


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.historyrow_style, null);
            hViewHolder = new MyViewHolder();

            hViewHolder.DocUrl = (ImageView) convertView.findViewById(R.id.imageView1);
            hViewHolder.printerId = (TextView) convertView.findViewById(R.id.textView1);
            hViewHolder.customerId = (TextView) convertView.findViewById(R.id.textView2);
            hViewHolder.DocId = (TextView) convertView.findViewById(R.id.textView4);
            hViewHolder.Date = (TextView) convertView.findViewById(R.id.textView5);

            convertView.setTag(hViewHolder);
        } else {
            hViewHolder = (MyViewHolder) convertView.getTag();
        }


        // Image loader
        //ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
// Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
//  which implements ImageAware interface)

// Load image, decode it to Bitmap and return Bitmap synchronously
        // Bitmap bmp = imageLoader.loadImageSync("https://cloudprint.s3.amazonaws.com/Screenshot_2015-11-30-06-54-41.png");

        // System.out.println("Bitmap  :"+bmp);
        HistoryContent hc = getItem(position);

        imageLoader.displayImage("", hViewHolder.DocUrl); //clears previous one

        imageLoader.displayImage(
                // "http://img.mysite.com/processes/resize_android.php?image=" + article.photopath + "&size=150&quality=80",
                hc.getDocUrl(),
                hViewHolder.DocUrl,
                imgDisplayOptions
        );
        hViewHolder.DocUrl.setVisibility(View.VISIBLE);


        // hViewHolder.DocUrl.setImageBitmap(imageLoader.loadImageSync(hc.getDocUrl()));
        hViewHolder.printerId.setText(hc.getPrinterId());
        hViewHolder.customerId.setText(hc.getCustomerId());
        // hViewHolder.copies.setText(hc.getC);
        hViewHolder.DocId.setText(hc.getDocId());
        hViewHolder.Date.setText(hc.getDate());

        //  mViewHolder.dist.setText(mc.getDist() + " " + "miles");

        //setOnClickListener(new View.OnClickListener() {});
        return convertView;

    }

}

