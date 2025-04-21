package com.example.t2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    Button btnAcademicQueries, btnChatGPT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize buttons
        btnAcademicQueries = findViewById(R.id.btn_academic_queries);
        btnChatGPT = findViewById(R.id.btn_chatgpt);

        // Click listeners
        btnAcademicQueries.setOnClickListener(v -> openWebPage("https://vignan.ac.in/contact.php"));

        btnChatGPT.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChatGptActivity.class);
            startActivity(intent);
        });
    }

    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            confirmLogout();
            return true;
        } else if (id == R.id.action_guidelines) {
            openWebPage("https://vignan.ac.in/vignantest/home.php");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Confirm logout
    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logoutUser())
                .setNegativeButton("No", null)
                .show();
    }

    // Perform logout
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // Open a webpage
    private void openWebPage(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to open link", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
