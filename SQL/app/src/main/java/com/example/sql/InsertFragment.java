package com.example.sql;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class InsertFragment extends Fragment {

    private DBHelper DB; // Declare DBHelper

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insert, container, false);

        // Initialize DBHelper
        DB = new DBHelper(requireContext());

        // Initialize views
        EditText nameInput = view.findViewById(R.id.editTextText);
        EditText regInput = view.findViewById(R.id.editTextText2);
        EditText branchInput = view.findViewById(R.id.editTextText3);
        Button insertButton = view.findViewById(R.id.button);

        // Set button click listener
        insertButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String reg = regInput.getText().toString().trim();
            String branch = branchInput.getText().toString().trim();

            if (name.isEmpty() || reg.isEmpty() || branch.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = DB.insertData(name, reg, branch);
                if (isInserted) {
                    Toast.makeText(requireContext(), "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    // Clear fields after successful insertion
                    nameInput.setText("");
                    regInput.setText("");
                    branchInput.setText("");
                } else {
                    Toast.makeText(requireContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
