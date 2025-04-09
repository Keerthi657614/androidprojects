package com.example.sqlconnect;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText name, reg_no, branch;
    Button insert, update, delete, view;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        reg_no = findViewById(R.id.reg);
        branch = findViewById(R.id.branch);
        insert = findViewById(R.id.btnInsert);
        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);
        view = findViewById(R.id.btnView);
        DB = new DBHelper(this);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTxt = name.getText().toString();
                String regTxt = reg_no.getText().toString();
                String branchTxt = branch.getText().toString();

                if (nameTxt.isEmpty() || regTxt.isEmpty() || branchTxt.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean checkInsertData = DB.insertData(nameTxt, regTxt, branchTxt);
                if (checkInsertData)
                    Toast.makeText(MainActivity.this, "New entry inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Insertion failed", Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTxt = name.getText().toString();
                String regTxt = reg_no.getText().toString();
                String branchTxt = branch.getText().toString();

                if (nameTxt.isEmpty() || regTxt.isEmpty() || branchTxt.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean checkUpdateData = DB.updateData(nameTxt, regTxt, branchTxt);
                if (checkUpdateData)
                    Toast.makeText(MainActivity.this, "Entry updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTxt = name.getText().toString();

                if (nameTxt.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter name to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean checkDeleteData = DB.deleteData(nameTxt);
                if (checkDeleteData)
                    Toast.makeText(MainActivity.this, "Entry deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = DB.getData();
                if (res.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "No entries exist", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuilder buffer = new StringBuilder();
                while (res.moveToNext()) {
                    buffer.append("Name: ").append(res.getString(0)).append("\n");
                    buffer.append("RegNo: ").append(res.getString(1)).append("\n");
                    buffer.append("Branch: ").append(res.getString(2)).append("\n\n");
                }
                res.close();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }
}
