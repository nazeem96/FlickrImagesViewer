package com.mohamednazeem.listviewwithimages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity{

    Bitmap tempImage = null;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> users = new ArrayList<>();
    ArrayList<RowItem> rowItems;
    ListView myListView;
    CustomAdapter adapter;
    Button refreshBtn;

    //boolean isFirsTime = true;

    public class DownloadTask extends AsyncTask<String, Void, Void>{ //Receiving the API Link

               @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... urls) {
            String fullResult = "";
            URL url;
            HttpURLConnection urlConnection = null;
            JSONArray arr;
            JSONObject jsonPart;

            try {

                //if (isFirstTime == true) {
                    url = new URL(urls[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);

                    int data = reader.read();

                    while (data != -1) {
                        char current = (char) data;
                        fullResult += current;
                        data = reader.read();
                    }

                    //Extracting all elements returned from the API
                    JSONObject jsonObject = new JSONObject(fullResult);
                    //Storing the result into a String
                    String myPhotos = jsonObject.getString("photos");

                    //Splitting the photos from the full result returned
                    JSONObject myPhotosJson = new JSONObject(myPhotos);
                    String allPhotos = myPhotosJson.getString("photo");

                    //Checking the result returned (All details of all photos)
                    Log.i("All photos", allPhotos);

                    //Storing the photos into a JSON Array
                    arr = new JSONArray(allPhotos);

                    //isFirstTime = false;


                    //Taking out all photos returned one by one (100 photos)
                    for(int i=0; i<100; i++) {

                        //Taking each element of the JSON Array
                        jsonPart = arr.getJSONObject(i);

                        //Extracting and Forming the link from the whole JSON Object
                        String extractedLink = "https://farm" + jsonPart.getString("farm")
                                + ".staticflickr.com/" + jsonPart.getString("server")
                                + "/" + jsonPart.getString("id")
                                + "_" + jsonPart.getString("secret") + ".jpg";

                        //Storing needed information (Image, Title)
                        users.add(jsonPart.getString("id"));
                        titles.add(jsonPart.getString("title"));

                        //Checking every image link
                        Log.i("Image " + i, extractedLink);

                        //Downloading the Image
                        url = new URL(extractedLink);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        //Storing the Image
                        tempImage = BitmapFactory.decodeStream(inputStream);
                        //Constructing the ListView item (Link, Image, title)
                        rowItems.add(new RowItem(extractedLink,
                                tempImage,
                                titles.get(i)));
                    }
                //}

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

            refreshBtn.setText("Refresh");
            myListView.addFooterView(refreshBtn);

            myListView.setAdapter(adapter);

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        task.execute("https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=e1d6073135c3567f58c2546cf5740632&format=json&nojsoncallback=1");

        refreshBtn = new Button(this);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadTask task = new DownloadTask();
                task.execute("https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=e1d6073135c3567f58c2546cf5740632&format=json&nojsoncallback=1");


            }
        });

        rowItems = new ArrayList<>();
        myListView = findViewById(R.id.list);
        adapter = new CustomAdapter(this, rowItems);


    }
}
