package com.example.developer.cloudprint.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developer.cloudprint.R;
import com.example.developer.cloudprint.model.User;

public class Profile extends AppCompatActivity {

    private final int SELECT_PHOTO = 1;
    private ImageView imageView;
    private TextView name1, lastname1, email1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent i = this.getIntent();
        User user1 = (User) i.getSerializableExtra("User");

        setContentView(R.layout.activity_profile);

        name1 = (TextView) findViewById(R.id.textView2);
        name1.setText("First Name : " + user1.getFirstName());


        TextView lastname1 = (TextView) findViewById(R.id.textView3);
        lastname1.setText("Last Name : " + user1.getLastName());

        TextView email1 = (TextView) findViewById(R.id.textView4);
        email1.setText("Email : " + user1.getEmail());


        System.out.println("yoyoy" + user1.getFirstName());
        String name = user1.getFirstName() + " " + user1.getLastName();

        imageView = (ImageView) findViewById(R.id.imageView);
        Button pickImage = (Button) findViewById(R.id.btn_pick);
        pickImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Intent i = this.getIntent();
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {


                        e.printStackTrace();
                    }

                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}

