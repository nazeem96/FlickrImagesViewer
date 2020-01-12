package com.mohamednazeem.listviewwithimages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView newImage = findViewById(R.id.imageView);
        Intent newIntent = getIntent();
        Bitmap myImage = newIntent.getParcelableExtra("BitmapImage");
        newImage.setImageBitmap(myImage);


    }
}
