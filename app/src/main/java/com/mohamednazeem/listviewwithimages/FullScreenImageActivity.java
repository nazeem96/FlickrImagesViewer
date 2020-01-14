package com.mohamednazeem.listviewwithimages;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FullScreenImageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String image;
    ImageView newImage;
    Bitmap myBitmap;


    public class DownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Is the selected photo downloaded?
            Log.i("test", image);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                URL url = new URL(image);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(inputStream);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Show the full-screen selected photo
            newImage.setImageBitmap(myBitmap);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        Intent newIntent = getIntent();
        image = newIntent.getStringExtra("Bitmap");
        newImage = findViewById(R.id.imageView);

        DownloadTask downloader = new DownloadTask();
        downloader.execute();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
