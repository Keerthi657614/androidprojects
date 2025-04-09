package com.example.t2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ChatGptActivity extends AppCompatActivity {

    private EditText userInput;
    private Button sendButton;
    private ImageButton newChatButton;
    private TextView chatOutput;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyAWYjqCazoBmeV9yEnoj_f3ORCv861WwiY";
    private String currentChatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_gpt);

        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);
        newChatButton = findViewById(R.id.newChatButton);
        chatOutput = findViewById(R.id.chatOutput);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(currentUser.getUid());
        currentChatId = "chat_" + System.currentTimeMillis();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        loadPreviousChats();

        sendButton.setOnClickListener(v -> sendMessage());

        newChatButton.setOnClickListener(v -> {
            chatOutput.setText("");
            currentChatId = "chat_" + System.currentTimeMillis();
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            showChatHistory(item.getTitle().toString());
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void sendMessage() {
        String userMessage = userInput.getText().toString().trim();
        if (userMessage.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        chatOutput.append("User: " + userMessage + "\n");
        userInput.setText("");

        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject jsonRequest = new JSONObject();
                JSONArray contentsArray = new JSONArray();
                JSONObject part = new JSONObject();
                part.put("text", userMessage);
                JSONArray parts = new JSONArray();
                parts.put(part);
                JSONObject userMessageObj = new JSONObject();
                userMessageObj.put("parts", parts);
                contentsArray.put(userMessageObj);
                jsonRequest.put("contents", contentsArray);

                OutputStream os = connection.getOutputStream();
                os.write(jsonRequest.toString().getBytes());
                os.flush();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                String botResponse = jsonResponse.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text");

                runOnUiThread(() -> {
                    chatOutput.append("Bot: " + botResponse + "\n\n");
                    saveMessageToFirebase(userMessage, botResponse);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ChatGptActivity.this,
                        "Error communicating with server", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void saveMessageToFirebase(String userMessage, String botResponse) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("User", userMessage);
        messageMap.put("Bot", botResponse);
        databaseReference.child(currentChatId).push().setValue(messageMap);
    }

    private void loadPreviousChats() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                navigationView.getMenu().clear();
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    navigationView.getMenu().add(chatSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatGptActivity.this, "Failed to load chats", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showChatHistory(String chatId) {
        chatOutput.setText("");
        currentChatId = chatId;
        databaseReference.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Map<String, String> message = (Map<String, String>) messageSnapshot.getValue();
                    chatOutput.append("User: " + message.get("User") + "\n");
                    chatOutput.append("Bot: " + message.get("Bot") + "\n\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatGptActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
