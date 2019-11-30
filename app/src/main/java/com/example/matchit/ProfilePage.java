package com.example.matchit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.matchit.ui.gallery.ProfileResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ProfileTask(getApplicationContext(), this).execute();
        setContentView(R.layout.activity_profile_page);
    }
}

class ProfileTask extends AsyncTask<String, Integer, ProfileResult> {

    private Context context;
    private Activity activity;
    private String username;
    private SharedPreferences sharedPreferences;

    protected ProfileTask(Context context, Activity activity){
        this.context = context.getApplicationContext();
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("database", Context.MODE_PRIVATE);
    }
    @Override
    protected ProfileResult doInBackground(String... strings) {
        String line;
        StringBuffer jsonString = new StringBuffer();
        Integer responseCode = 0;
        ProfileResult profileResult;
        Log.d("testing","testing 123");
        String id = sharedPreferences.getString("id", "");
        Log.d("Id number",id);
        try {
            URL url = new URL(Constant.PATH + "/profile/get/" + id);
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
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                Gson gson = new Gson();
                while((line = br.readLine()) != null){
                    jsonString.append(line);
                }
                br.close();
                profileResult = gson.fromJson(jsonString.toString(), ProfileResult.class);
                return profileResult;
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Response", responseCode.toString());
        return null;
    }

    @Override
    protected void onPostExecute(ProfileResult profileResult) {
        super.onPostExecute(profileResult);
        if(profileResult != null){
            TextView name =  activity.findViewById(R.id.name);
            name.setText(profileResult.name);
            TextView bio =  activity.findViewById(R.id.bio);
            bio.setText(profileResult.tag);
            TextView interest =  activity.findViewById(R.id.interest);
            interest.setText(profileResult.interest);
            TextView city =  activity.findViewById(R.id.city);
            city.setText(profileResult.city);
            TextView movies =  activity.findViewById(R.id.movies);
            movies.setText(profileResult.movies);
            TextView sports =  activity.findViewById(R.id.sports);
            sports.setText(profileResult.sports);
            TextView food =  activity.findViewById(R.id.food);
            food.setText(profileResult.food);
            TextView age =  activity.findViewById(R.id.age);
            age.setText(profileResult.age.toString());
            TextView social_media =  activity.findViewById(R.id.social_media);
            social_media.setText(profileResult.social_media);
        } else {
            Toast.makeText(context, "Username / Password Error", Toast.LENGTH_SHORT).show();
        }
    }
}
