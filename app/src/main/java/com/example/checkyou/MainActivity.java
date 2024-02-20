package com.example.checkyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView school;
    private TextView class_number;
    private Button button_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_start = findViewById(R.id.button_start);
        school = findViewById(R.id.school_field);
        class_number = findViewById(R.id.class_field);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String school_text = school.getText().toString();
                String class_text = class_number.getText().toString();

                try {
                    FileOutputStream fileOutputSchool = openFileOutput("school.txt", MODE_PRIVATE);
                    fileOutputSchool.write(school_text.getBytes());
                    fileOutputSchool.close();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    FileOutputStream fileOutputClass = openFileOutput("class_number.txt", MODE_PRIVATE);
                    fileOutputClass.write(class_text.getBytes());
                    fileOutputClass.close();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Intent intent = new Intent(MainActivity.this, TestsListActivity.class);
                startActivity(intent);

            }
        });
    }
}