package com.example.sql;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewFragment extends Fragment {

    private DBHelper DB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        DB = new DBHelper(requireContext());

        Button viewButton = view.findViewById(R.id.button2); // Make sure you have a button in your layout

        viewButton.setOnClickListener(v -> {
            Cursor res = DB.getData();
            if (res.getCount() == 0) {
                Toast.makeText(requireContext(), "No entries exist", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder buffer = new StringBuilder();
            while (res.moveToNext()) {
                buffer.append("Name: ").append(res.getString(0)).append("\n");
                buffer.append("RegNo: ").append(res.getString(1)).append("\n");
                buffer.append("Branch: ").append(res.getString(2)).append("\n\n");
            }
            res.close();

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setCancelable(true);
            builder.setTitle("User Entries");
            builder.setMessage(buffer.toString());
            builder.show();
        });

        return view;
    }
}
