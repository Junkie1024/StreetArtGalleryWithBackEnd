package com.example.streetart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    ArrayList<categorydata> categoryDataArrayList;
    RecyclerView category_List_recyclerView;
    RecycleAdapter recycleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist_recycleview);

        category_List_recyclerView = findViewById(R.id.categoryList_recycler_view);


        categoryDataArrayList = new ArrayList<categorydata>();

        recycleAdapter = new RecycleAdapter(categoryDataArrayList,getApplicationContext());


        category_List_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        category_List_recyclerView.setAdapter(recycleAdapter);

        loadData();

    }

    public void loadData(){
        MyTask process = new MyTask(getApplicationContext());
        process.execute();
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        String message;
        Context context;

        public MyTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {


            URL url = null;
            try {

                url = new URL("http://192.168.3.109:8080/StreetArtGallery/streetart/database/CategoryList");

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();

                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);

                InputStreamReader myInput = new InputStreamReader(client.getInputStream());

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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
                final JSONArray jsonArray = responseObj.getJSONArray("Category List: ");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    categorydata item = new categorydata(jsonObject.getString("CATEGORYNAME"), jsonObject.getInt("NUMBEROFART"));
                    categoryDataArrayList.add(item);
                    recycleAdapter.notifyDataSetChanged();
                    recycleAdapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                            int position = viewHolder.getAdapterPosition();
                            Intent i = new Intent(MainActivity.this,Artist_List_Activity.class);
                            i.putExtra("CategoryName",categoryDataArrayList.get(position).getCategoryName());
                            startActivity(i);
                        }
                    });



                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }




}
