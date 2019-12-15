package com.example.matchit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.matchit.ui.gallery.ProfileResult;
import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfilePage extends AppCompatActivity {

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        new ProfileTask(getApplicationContext(), this).execute();
        setContentView(R.layout.activity_profile_page);

        Button button = (Button) findViewById(R.id.profile_change);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileEditActivity.class);
                activity.startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();
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
            TextView bio =  activity.findViewById(R.id.bioText);
            bio.setText(profileResult.tag);
            TextView interest =  activity.findViewById(R.id.interestText);
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
            new DownloadTask(activity).execute(profileResult.photo);

        } else {
            Toast.makeText(context, "Username / Password Error", Toast.LENGTH_SHORT).show();
        }
    }
}
class DownloadTask extends AsyncTask<String, Bitmap, Bitmap> {

    Activity activity;
    public DownloadTask(Activity activity){
       this.activity = activity;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap result){
        ImageView imageView = activity.findViewById(R.id.profile_img);
        imageView.setImageBitmap(result);
    }
}
