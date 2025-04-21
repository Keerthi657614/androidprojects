package com.example.sql;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class UpdateFragment extends Fragment {

    private DBHelper DB; // Declare DBHelper

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        // Initialize DBHelper
        DB = new DBHelper(requireContext());

        // Initialize views
        EditText nameInput = view.findViewById(R.id.editTextText);
        EditText regInput = view.findViewById(R.id.editTextText2);
        EditText branchInput = view.findViewById(R.id.editTextText3);
        Button updateButton = view.findViewById(R.id.button);

        // Set button click listener
        updateButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String reg = regInput.getText().toString().trim();
            String branch = branchInput.getText().toString().trim();

            if (name.isEmpty() || reg.isEmpty() || branch.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean isUpdated = DB.updateData(name, reg, branch);
                if (isUpdated) {
                    Toast.makeText(requireContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                    // Clear fields after successful update
                    nameInput.setText("");
                    regInput.setText("");
                    branchInput.setText("");
                } else {
                    Toast.makeText(requireContext(), "Update Failed! Record Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
