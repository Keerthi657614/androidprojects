package com.example.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Userdata.db";
    private static final String TABLE_NAME = "Userdetails";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        try {
            DB.execSQL("CREATE TABLE " + TABLE_NAME + " (name TEXT PRIMARY KEY, reg TEXT, branch TEXT)");
        } catch (SQLException e) {
            Log.e("DBHelper", "Error creating table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(DB);
    }

    public boolean insertData(String name, String reg, String branch) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("reg", reg);
        values.put("branch", branch);

        try {
            long result = DB.insert(TABLE_NAME, null, values);
            DB.close(); // Close database
            return result != -1;
        } catch (SQLException e) {
            Log.e("DBHelper", "Insert Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateData(String name, String reg, String branch) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("reg", reg);
        values.put("branch", branch);

        try {
            long result = DB.update(TABLE_NAME, values, "name=?", new String[]{name});
            DB.close(); // Close database
            return result > 0;
        } catch (SQLException e) {
            Log.e("DBHelper", "Update Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteData(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        try {
            long result = DB.delete(TABLE_NAME, "name=?", new String[]{name});
            DB.close(); // Close database
            return result > 0;
        } catch (SQLException e) {
            Log.e("DBHelper", "Delete Error: " + e.getMessage());
            return false;
        }
    }

    public Cursor getData() {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
