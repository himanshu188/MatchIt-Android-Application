package com.example.matchit.ui.gallery;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import com.example.matchit.Constant;
import com.example.matchit.R;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DynamicListActivity extends ListFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.post_list, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CustomAdapter adapter = new CustomAdapter(getActivity());

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
            try {
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
