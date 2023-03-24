package com.example.comicapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ComicAdapter comicAdapter;
    ComicSQLiteHelper comicSQLiteHelper;
    RecyclerView recyclerView;
    ArrayList<JSONObject> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        comicSQLiteHelper = new ComicSQLiteHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = comicSQLiteHelper.getReadableDatabase();
        comicAdapter = new ComicAdapter(comicSQLiteHelper);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(comicAdapter);

        if (comicSQLiteHelper.getCount() == 0){
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        }

    }

    private class MyAsyncTask extends AsyncTask<Object, Void, ArrayList<JSONObject>> {
        @Override
        protected ArrayList<JSONObject> doInBackground(Object... objects) {
            HttpURLConnection httpURLConnection = null;
            Log.v("Here", "Before Try");
            try{
                URL url = new URL("https://xkcd.com/info.0.json");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                if(httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String JSONtext = bufferedReader.readLine();
                JSONObject jsonObject = new JSONObject(JSONtext);
                list.add(jsonObject);

                while(list.size() < 100){
                    int num = jsonObject.getInt("num");
                    num = num - 1;
                    String newURL = "https://xkcd.com/" + num + "/info.0.json";
                    url = new URL(newURL);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    inputStream = httpURLConnection.getInputStream();
                    if(httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
                        return null;
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    JSONtext = bufferedReader.readLine();
                    jsonObject = new JSONObject(JSONtext);
                    list.add(jsonObject);
                }
                return list;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> jsonObjectArray) {
            if(jsonObjectArray != null){
                try {
                    for(int i = 0; i < jsonObjectArray.size(); i++) {
                        int num = jsonObjectArray.get(i).getInt("num");
                        String name = jsonObjectArray.get(i).getString("title");
                        String url = jsonObjectArray.get(i).getString("img");
                        comicSQLiteHelper.insert(name, num, url);
                        comicAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.v("Here", "exit");
        }
    }
}