package com.example.matchit.ui.home.ui.post;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.matchit.Constant;
import com.example.matchit.DashBoardActivity;
import com.example.matchit.LogInActivity;
import com.example.matchit.R;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DynamicListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);

        CustomAdapter adapter = new CustomAdapter(this);

        setListAdapter(adapter);

        new PostTask(adapter).execute();
    }
}


class PostTask extends AsyncTask<String, Integer , ArrayList<SearchResult>> {

    private Context context;
    private String username;
    private CustomAdapter customAdapter;

    protected PostTask(CustomAdapter customAdapter){
        this.customAdapter = customAdapter;
    }
    @Override
    protected ArrayList<SearchResult> doInBackground(String... strings) {
        Log.d("Testing","Reached");
        String line;
        StringBuffer jsonString = new StringBuffer();
        Integer responseCode = 0;
        try {
            URL url = new URL(Constant.PATH + "/post/get");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
//            urlConnection.setDoOutput(true);
            try {
//                String requestBody = "username=" + URLEncoder.encode(strings[0], "UTF-8")  + "&password=" + URLEncoder.encode(strings[1], "UTF-8");
//                username = strings[0];
//                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
//                writer.write(requestBody);
//                writer.close();
//                urlConnection.connect();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                ArrayList<SearchResult> lst = new ArrayList<>();
                Gson gson = new Gson();
                while((line = br.readLine()) != null){
                    jsonString.append(line);
                }
                br.close();
                SearchResult[] searchResultAll = gson.fromJson(jsonString.toString(), SearchResult[].class);
                Log.d("Result", searchResultAll[0].title);
                for(int i = 0; i < searchResultAll.length; i++){
                   Log.d("Result", searchResultAll[i].title);
                   lst.add(searchResultAll[i]);
                }
                return lst;
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<SearchResult> searchResults) {
        super.onPostExecute(searchResults);
        try {
            if(searchResults != null){
                customAdapter.upDateEntries(searchResults);
            } else {
                Toast.makeText(context, "Not Working", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
