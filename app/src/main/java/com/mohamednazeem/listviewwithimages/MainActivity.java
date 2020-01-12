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
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    Bitmap tempImage = null;
    ArrayList<String> imagesURLs = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> users = new ArrayList<>();

    Button showMoreBtn;
    int setIndex = 0;
    int getIndex = 0;

    boolean isFirstTime = true;

    String result;
    CustomAdapter adapter;

    ArrayList<RowItem> rowItems;
    ListView myListView;

    public class DownloadTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... urls) {
            result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            JSONArray arr = null;
            JSONObject jsonPart;

            RowItem item;

            try {

                if (isFirstTime == true) {
                    url = new URL(urls[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);

                    int data = reader.read();

                    while (data != -1) {
                        char current = (char) data;
                        result += current;
                        data = reader.read();
                    }

                    JSONObject jsonObject = new JSONObject(result);

                    String myPhotos = jsonObject.getString("photos");

                    JSONObject myPhotosJson = new JSONObject(myPhotos);
                    String allPhotos = myPhotosJson.getString("photo");
                    Log.i("All photos", allPhotos);

                    arr = new JSONArray(allPhotos);
                    Log.i("No. of Items: ", String.valueOf(arr.length()));

                    isFirstTime = false;


                for(int i=0; i<20; i++) {
                    jsonPart = arr.getJSONObject(i);

                    String tempPhoto = "https://farm" + jsonPart.getString("farm")
                            + ".staticflickr.com/" + jsonPart.getString("server")
                            + "/" + jsonPart.getString("id")
                            + "_" + jsonPart.getString("secret") + ".jpg";

                    imagesURLs.add(tempPhoto);

                    users.add(jsonPart.getString("id"));
                    titles.add(jsonPart.getString("title"));

                    Log.i("Image " + i, imagesURLs.get(i));


                    url = new URL(tempPhoto);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    tempImage = BitmapFactory.decodeStream(inputStream);
                    //bitmaps.add(tempImage);

                    rowItems.add(new RowItem(users.get(i),
                            tempImage,
                            titles.get(i)));

                    /*item = new RowItem("User: " + users.get(i),
                            bitmaps.get(i),
                            "Title: " + titles.get(i));*/

                    /*item = new RowItem(jsonPart.getString("id"),
                            tempImage,
                            jsonPart.getString("title"));*/


                }
                }




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



            showMoreBtn.setText("Show More");
            myListView.addFooterView(showMoreBtn);


            myListView.setAdapter(adapter);

            for (int y=0; y<5; y++){
                Log.i("Check 2."+y, rowItems.get(y).getUserId());
            }

            Log.i("Number of Elements: ", String.valueOf(adapter.getCount()));

        }

        /*@Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {

                *//*final Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 5000);

                    }
                };*//*

                ImageDownloader img = new ImageDownloader();

                String myPhotos;

                JSONObject jsonObject = new JSONObject(result);

                myPhotos = jsonObject.getString("photos");

                JSONObject myPhotosJson = new JSONObject(myPhotos);
                String allPhotos = myPhotosJson.getString("photo");
                Log.i("All photos", allPhotos);

                JSONArray arr = new JSONArray(allPhotos);

                for(int i=0; i<1; i++){
                    JSONObject jsonPart = arr.getJSONObject(i);

                    String tempPhoto = "https://farm" + jsonPart.getString("farm")
                            +".staticflickr.com/" + jsonPart.getString("server")
                            +"/" + jsonPart.getString("id")
                            + "_" + jsonPart.getString("secret") + ".jpg";
                    //imagesURLs.add(tempPhoto);
                    //Log.i("Image " +i, imagesURLs.get(i));

                    Log.i("Image " +i, tempPhoto);
                    myImage = img.execute(tempPhoto).get();
                    //handler.post(run);


                    bitmaps.add(myImage);
                    users.add(jsonPart.getString("id"));
                    titles.add(jsonPart.getString("title"));


                }


                *//*for (int i=0; i<1; i++){
                    RowItem item = new RowItem(users.get(i),
                            bitmaps.get(i),
                            titles.get(i));
                    Log.i("Test ",i + users.get(i));

                    rowItems.add(item);
                }*//*

                myListView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }*/
    }

    /*public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        task.execute("https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=e1d6073135c3567f58c2546cf5740632&format=json&nojsoncallback=1");

        showMoreBtn = new Button(this);
        showMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask().execute();
            }
        });

        rowItems = new ArrayList<>();


       /* for (int i=0; i<3; i++){

            RowItem rowItem = new RowItem(memberNames[i],
                    profilePics.getResourceId(i, -1),
                    contactTypes[i]);

            rowItems.add(rowItem);
        }*/

        myListView = findViewById(R.id.list);
        adapter = new CustomAdapter(this, rowItems);
        //myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*String memberName = rowItems.get(position).getUserId();
        Toast.makeText(getApplicationContext(),""+memberName,Toast.LENGTH_SHORT).show();*/

        Intent fullScreenIntent = new Intent(this, FullScreenImageActivity.class);
        fullScreenIntent.putExtra("BitmapImage", bitmaps.get(position));

        startActivity(fullScreenIntent);
    }

}
