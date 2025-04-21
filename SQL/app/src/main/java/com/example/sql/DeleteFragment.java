package com.example.sql;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class DeleteFragment extends Fragment {

    private DBHelper DB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

        // Initialize DBHelper
        DB = new DBHelper(requireContext());

        // Initialize views
        EditText nameInput = view.findViewById(R.id.editTextText);
        Button deleteButton = view.findViewById(R.id.button);

        // Set button click listener
        deleteButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call the delete function in DBHelper
            boolean isDeleted = DB.deleteData(name);

            if (isDeleted) {
                Toast.makeText(requireContext(), "Record Deleted Successfully!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), "No record found!", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
