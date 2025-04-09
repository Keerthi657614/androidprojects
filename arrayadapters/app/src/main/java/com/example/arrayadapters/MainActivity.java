package com.example.arrayadapters;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail;
    private RadioGroup radioGroupGender;
    private Button submitButton;
    private ListView userListView;
    private ArrayList<String> userList;
    private ArrayAdapter<String> arrayAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        submitButton = findViewById(R.id.submitButton);
        userListView = findViewById(R.id.userListView);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        // Initialize ArrayAdapter and ListView
        userList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(arrayAdapter);

        // Submit button click listener
        submitButton.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();

            // Get selected gender
            int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
            String gender = "";
            if (selectedGenderId == R.id.radioMale) {
                gender = "Male";
            } else if (selectedGenderId == R.id.radioFemale) {
                gender = "Female";
            }

            if (!name.isEmpty() && !email.isEmpty() && !gender.isEmpty()) {
                String userId = databaseReference.push().getKey();
                User user = new User(name, email, gender);
                databaseReference.child(userId).setValue(user);
                editTextName.setText("");
                editTextEmail.setText("");
                radioGroupGender.clearCheck(); // Clear selected radio button
            }
        });

        // Listen for changes in Firebase
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    userList.add(user.getName() + " - " + user.getEmail() + " (" + user.getGender() + ")");
                    arrayAdapter.notifyDataSetChanged(); // Refresh the list
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // User class
    public static class User {
        private String name;
        private String email;
        private String gender;

        public User() {}

        public User(String name, String email, String gender) {
            this.name = name;
            this.email = email;
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getGender() {
            return gender;
        }
    }
}
