package com.example.streetart;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Artist_List_Activity extends AppCompatActivity {

    ArrayList<artist_data> artistDataArrayList;
    RecyclerView artist_list_recyclerView;
    RecycleAdapterArtist recycleAdapter;

    String CategoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_all_list);

        artist_list_recyclerView = findViewById(R.id.Artist_List_recycler_view);

        artistDataArrayList = new ArrayList<artist_data>();

        recycleAdapter = new RecycleAdapterArtist(artistDataArrayList,getApplicationContext());

        artist_list_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        artist_list_recyclerView.setAdapter(recycleAdapter);

        if(getIntent()!=null){
            CategoryName = getIntent().getStringExtra("CategoryName");
        }

        loadData();

    }

    public void loadData(){
        MyTask process = new MyTask(getApplicationContext(),CategoryName);
        process.execute();
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        String message;
        String CategoryName;
        Context context;

        public MyTask(Context context,String CategoryName) {
            this.context = context;
            this.CategoryName = CategoryName;
        }

        @Override
        protected String doInBackground(Void... params){


            URL url = null;
            try {

                url = new URL("http://192.168.3.109:8080/StreetArtGallery/streetart/database/artistListbyCategoryname&"+ CategoryName);

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();

                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);

                InputStreamReader myInput= new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                message = response.toString();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }

            catch (IOException e) {
                e.printStackTrace();
            }

            return message;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            parseJsonData(result);
        }
        private void parseJsonData(String jsonResponse){
            try
            {
                JSONObject responseObj = new JSONObject(jsonResponse);
                System.out.println("Response: " + responseObj);
                final JSONArray jsonArray = responseObj.getJSONArray("ArtistList:");
                for(int i=0;i<jsonArray.length();i++)
                {
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    artist_data item = new artist_data(
                            jsonObject.getInt("Artist_ID"),
                            jsonObject.getString("First_Name"),
                            jsonObject.getString("Last_Name"));
                              artistDataArrayList.add(item);
                    recycleAdapter.notifyDataSetChanged();

                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
