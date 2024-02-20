package com.example.checkyou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class TestsListActivity extends AppCompatActivity {

    StringBuffer class_number = new StringBuffer();
    StringBuffer school = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_list);


        // Reading from file class
        try {
            FileInputStream fileInputClass = openFileInput("class_number.txt");
            InputStreamReader readerClass = new InputStreamReader(fileInputClass);
            BufferedReader bufferClass = new BufferedReader(readerClass);
            String class_line;

            while ((class_line = bufferClass.readLine()) != null) {
                class_number.append(class_line + "\n");

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Reading from file school
        try {
            FileInputStream fileInputSchool = openFileInput("school.txt");
            InputStreamReader readerSchool = new InputStreamReader(fileInputSchool);
            BufferedReader bufferSchool = new BufferedReader(readerSchool);
            String school_line;

            while ((school_line = bufferSchool.readLine()) != null) {
                school.append(school_line + "\n");

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String url_path = ("https://haser.pythonanywhere.com/api/tests/?school=" + school.toString() + "&class-number=" + class_number.toString()).toString();

         URL path = null;
         try {
             path = new URL(url_path);

            GetTests task = new GetTests();
           task.execute(path);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);

        }
    }


    class GetTests extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url_path;
            url_path = urls[0];
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url_path.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                System.out.println(hasInput);

                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                urlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String response) {

            System.out.println(response);

            LinearLayout container = findViewById(R.id.layout_linear);
            if (response != null){
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name_test = jsonObject.getString("name");
                        String teacher_name = jsonObject.getString("teacher");
                        String subject = jsonObject.getString("subject");
                        String id_test = jsonObject.getString("id");

                        CardView new_card = new CardView(TestsListActivity.this);
                        new_card.setRadius(15.0f);
                        new_card.setMinimumHeight(500);

                        TextView testName = new TextView(TestsListActivity.this);
                        testName.setText(name_test.toString());
                        testName.setPadding(10, 100, 0, 0);

                        TextView testTeacher = new TextView(TestsListActivity.this);
                        testTeacher.setText(teacher_name.toString());
                        testTeacher.setPadding(10, 180, 0, 0);

                        TextView testSubject = new TextView(TestsListActivity.this);
                        testSubject.setText(subject.toString());
                        testSubject.setPadding(10, 260, 0, 0);

                        new_card.addView(testName);
                        new_card.addView(testSubject);
                        new_card.addView(testTeacher);

                        container.addView(new_card);

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}