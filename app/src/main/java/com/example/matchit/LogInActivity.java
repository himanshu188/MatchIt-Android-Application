package com.example.matchit;

import androidx.appcompat.app.AppCompatActivity;

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

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = this.getSharedPreferences("database", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("username", "").isEmpty()){
            Intent intent = new Intent(this, DashBoardActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);

        final TextView userview = findViewById(R.id.username);
        final TextView passwordview = findViewById(R.id.password);

        Button button = (Button) findViewById(R.id.button_sign_in);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userview.getText().toString();
                String password = passwordview.getText().toString();
                new LoginTask(LogInActivity.this.getApplicationContext(), LogInActivity.this).execute(username, password);
            }
        });
    }
}

class LoginTask extends AsyncTask<String, Integer, Integer> {

    private Context context;
    private LogInActivity logInActivity;
    private String username;

    protected LoginTask(Context context, LogInActivity logInActivity){
        this.context = context.getApplicationContext();
        this.logInActivity = logInActivity;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        Log.d("Testing","Reached");
        String line;
        StringBuffer jsonString = new StringBuffer();
        Integer responseCode = 0;
        try {
            URL url = new URL(Constant.PATH + "/user/get");
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
//                urlConnection.connect();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                while((line = br.readLine()) != null){
                    jsonString.append(line);
                }
                br.close();
                SharedPreferences sharedPreferences = logInActivity.getSharedPreferences("database", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.d("id : ", jsonString.toString());
                editor.putString("id", jsonString.toString());
                editor.commit();
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
            SharedPreferences sharedPreferences = logInActivity.getSharedPreferences("database", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.commit();
            Intent intent = new Intent(context, DashBoardActivity.class);
            context.startActivity(intent);
            logInActivity.finish();
        } else {
            Toast.makeText(context, "Username / Password Error", Toast.LENGTH_SHORT).show();
        }
    }
}