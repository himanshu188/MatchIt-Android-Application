package com.example.matchit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class SignUpActivity extends AppCompatActivity {

//    Signup Form for the New User
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_sign_up);
        final TextView userview = findViewById(R.id.signup_username);
        final TextView passwordview = findViewById(R.id.signup_password);
        final TextView nameview = findViewById(R.id.name);
        Button button = (Button) findViewById(R.id.button_sign_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userview.getText().toString();
                String password = passwordview.getText().toString();
                String name = nameview.getText().toString();
                new SignUpTask(view.getContext(), activity).execute(username, password);
                new SignUpProfileTask(view.getContext(), activity).execute(name);
            }
        });
    }
}

class SignUpTask extends AsyncTask<String, Integer, Integer> {

    private Context context;
    private Activity activity;
    private String username;

    protected SignUpTask(Context context, Activity activity){
        this.context = context.getApplicationContext();
        this.activity = activity;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        String line;
        StringBuffer jsonString = new StringBuffer();
        Integer responseCode = 0;
        try {
//            REST API to Create New User
            URL url = new URL(Constant.PATH + "/user/signup");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setDoOutput(true);
            try {
                String requestBody = "username=" + URLEncoder.encode(strings[0], "UTF-8")  + "&password=" + URLEncoder.encode(strings[1], "UTF-8");
                username = strings[0];
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(requestBody);
                writer.close();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                while((line = br.readLine()) != null){
                    jsonString.append(line);
                }
                br.close();
                responseCode = urlConnection.getResponseCode();
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Response", responseCode.toString());
        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if(integer == HttpsURLConnection.HTTP_OK){
            Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Username / Password Error", Toast.LENGTH_SHORT).show();
        }
    }
}


class SignUpProfileTask extends AsyncTask<String, Integer, Integer> {

    private Context context;
    private Activity activity;

    protected SignUpProfileTask(Context context, Activity activity){
        this.context = context.getApplicationContext();
        this.activity = activity;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        String line;
        StringBuffer jsonString = new StringBuffer();
        Integer responseCode = 0;
        try {
            URL url = new URL(Constant.PATH + "/profile/create");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setDoOutput(true);
            try {
                String requestBody = "name=" + URLEncoder.encode(strings[0], "UTF-8");
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(requestBody);
                writer.close();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                while((line = br.readLine()) != null){
                    jsonString.append(line);
                }
                br.close();
                responseCode = urlConnection.getResponseCode();
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Response", responseCode.toString());
        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if(integer == HttpsURLConnection.HTTP_OK){
            activity.finish();
        } else {
            Toast.makeText(context, "Username / Password Error", Toast.LENGTH_SHORT).show();
        }
    }
}
