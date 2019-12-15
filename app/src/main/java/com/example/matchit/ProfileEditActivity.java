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
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ProfileEditActivity extends AppCompatActivity {

    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_profile_page_edit);

        Button submit = (Button) findViewById(R.id.profilePageSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hmap = new HashMap<>();
                EditText editText = findViewById(R.id.interest);
                hmap.put("interest", editText.getText().toString());
                EditText bio = findViewById(R.id.bio);
                hmap.put("bio", bio.getText().toString());
                EditText tag = findViewById(R.id.tagTextField);
                hmap.put("tag", tag.getText().toString());
                EditText city = findViewById(R.id.currentCityTextField);
                hmap.put("city", city.getText().toString());
                EditText movies = findViewById(R.id.favMoviesTextField);
                hmap.put("movies", movies.getText().toString());
                EditText sports = findViewById(R.id.favSportsTextField);
                hmap.put("sports", sports.getText().toString());
                EditText food = findViewById(R.id.foodTextField);
                hmap.put("food", food.getText().toString());
                new ProfileEditTask(v.getContext(), activity, hmap).execute(hmap);
            }
        });
    }

}

class ProfileEditTask extends AsyncTask<HashMap<String, String>, Integer, Integer> {

    private Context context;
    private Activity activity;
    private String username;
    private SharedPreferences sharedPreferences;

    protected ProfileEditTask(Context context, Activity activity, HashMap<String, String> hmap){
        this.context = context.getApplicationContext();
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("database", Context.MODE_PRIVATE);
    }
    private String getData(HashMap<String, String> hmap){
        StringBuilder output = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> data: hmap.entrySet()){
            if(data.getValue().isEmpty())
                continue;
            if(!first)
                output.append("&");
            else if(!data.getValue().isEmpty())
                first = false;
            output.append(data.getKey());
            output.append("=");
            output.append(data.getValue());
        }
        return output.toString();
    }
    @Override
    protected Integer doInBackground(HashMap... strings) {
        String ans = getData(strings[0]);
        String line;
        StringBuffer jsonString = new StringBuffer();
        String id = sharedPreferences.getString("id", "");
        Integer responseCode = 0;
        try {
            URL url = new URL(Constant.PATH + "/profile/update/" + id);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setDoOutput(true);
            try {
                String requestBody = ans;
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(requestBody);
                writer.close();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                while((line = br.readLine()) != null){
                    jsonString.append(line);
                }
                br.close();
//                SharedPreferences sharedPreferences = activity.getSharedPreferences("database", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                Log.d("id : ", jsonString.toString());
//                editor.putString("id", jsonString.toString());
//                editor.commit();
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
//            SharedPreferences sharedPreferences = activity.getSharedPreferences("database", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("username", username);
//            editor.commit();
//            Intent intent = new Intent(context, DashBoardActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//            activity.getParentActivityIntent().relo
//            activity.getParent().recreate();
            activity.finish();
//            Intent intent = activity.getParentActivityIntent();
//            Intent intent = new Intent(context, ProfilePage.class);
//            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Username / Password Error", Toast.LENGTH_SHORT).show();
        }
    }
}
