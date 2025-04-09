package com.example.sql;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    private DBHelper DB; // Declare DBHelper

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize DBHelper
        DB = new DBHelper(requireContext());

        // Initialize views
        EditText nameInput = view.findViewById(R.id.editTextText);
        Button searchButton = view.findViewById(R.id.button);
        TextView resultView = view.findViewById(R.id.textViewResult);

        // Set button click listener
        searchButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            } else {
                Cursor cursor = DB.getData();
                boolean found = false;
                StringBuilder result = new StringBuilder();

                while (cursor.moveToNext()) {
                    String dbName = cursor.getString(0);
                    String reg = cursor.getString(1);
                    String branch = cursor.getString(2);

                    if (dbName.equalsIgnoreCase(name)) {
                        result.append("Reg No: ").append(reg).append("\n")
                                .append("Branch: ").append(branch);
                        found = true;
                        break;
                    }
                }
                cursor.close();

                if (found) {
                    resultView.setText(result.toString());
                } else {
                    resultView.setText("No record found!");
                }
            }
        });

        return view;
    }
}
