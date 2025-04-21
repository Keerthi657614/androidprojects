package com.example.sqlconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Userdata.db";
    private static final String TABLE_NAME = "Userdetails";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE " + TABLE_NAME + " (name TEXT PRIMARY KEY, reg TEXT, branch TEXT)");
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
        long result = DB.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean updateData(String name, String reg, String branch) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("reg", reg);
        values.put("branch", branch);

        Cursor cursor = DB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE name=?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.update(TABLE_NAME, values, "name=?", new String[]{name});
            cursor.close();
            return result != -1;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean deleteData(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE name=?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.delete(TABLE_NAME, "name=?", new String[]{name});
            cursor.close();
            return result != -1;
        } else {
            cursor.close();
            return false;
        }
    }

    public Cursor getData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
