package com.example.sql;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Buttons initialization
        Button btnHome, btnInsert, btnUpdate, btnSearch, btnView,btnDelete;
        btnHome = findViewById(R.id.btn_home);
        btnInsert = findViewById(R.id.btn_insert);
        btnUpdate = findViewById(R.id.btn_update);
        btnSearch = findViewById(R.id.btn_search);
        btnDelete=findViewById(R.id.btn_delete);
        btnView = findViewById(R.id.btn_view);

        // Load the default fragment (HomeFragment)
        loadFragment(new HomeFragment());

        // Set click listeners to switch fragments
        btnHome.setOnClickListener(view -> loadFragment(new HomeFragment()));
        btnInsert.setOnClickListener(view -> loadFragment(new InsertFragment()));
        btnUpdate.setOnClickListener(view -> loadFragment(new UpdateFragment()));
        btnSearch.setOnClickListener(view -> loadFragment(new SearchFragment()));
        btnDelete.setOnClickListener(view -> loadFragment(new DeleteFragment()));
        btnView.setOnClickListener(view -> loadFragment(new ViewFragment())); // Added for viewing data
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
