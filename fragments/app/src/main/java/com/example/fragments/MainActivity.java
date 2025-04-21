package com.example.fragments;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button b, c, d;
        b = findViewById(R.id.btn_education);
        c = findViewById(R.id.btn_personal);
        d = findViewById(R.id.btn_skills);

        // Load the default fragment
        loadFragment(new PersonalFragment());

        b.setOnClickListener(view -> loadFragment(new EducationFragment()));
        c.setOnClickListener(view -> loadFragment(new PersonalFragment()));
        d.setOnClickListener(view -> loadFragment(new SkillsFragment()));
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
