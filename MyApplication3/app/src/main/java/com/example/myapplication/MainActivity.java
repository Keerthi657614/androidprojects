package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.UniversityGuidelinesActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAcademicQueries = findViewById(R.id.btnAcademicQueries);
        Button btnUniversityGuidelines = findViewById(R.id.btnUniversityGuidelines);
        Button btnChatGPT = findViewById(R.id.btnChatGPT);

        btnAcademicQueries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UniversityGuidelinesActivity.class).putExtra("url", "https://vignan.ac.in/contact.php"));
            }
        });

        btnUniversityGuidelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UniversityGuidelinesActivity.class).putExtra("url", "https://vignan.ac.in/vignantest/home.php"));
            }
        });

        btnChatGPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UniversityGuidelinesActivity.class).putExtra("url", "https://chat.openai.com"));
            }
        });
    }
}

