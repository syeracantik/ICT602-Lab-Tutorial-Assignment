package com.syahirah2023103611.electricbill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electricity.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "bill";
    private static final String COL_ID = "id";
    private static final String COL_MONTH = "month";
    private static final String COL_UNIT = "unit";
    private static final String COL_TOTAL = "total";
    private static final String COL_REBATE = "rebate";
    private static final String COL_FINAL = "final";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MONTH + " TEXT, " +
                COL_UNIT + " INTEGER, " +
                COL_TOTAL + " REAL, " +
                COL_REBATE + " REAL, " +
                COL_FINAL + " REAL)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertBill(String month, int unit, double total, double rebate, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MONTH, month);
        values.put(COL_UNIT, unit);
        values.put(COL_TOTAL, total);
        values.put(COL_REBATE, rebate);
        values.put(COL_FINAL, finalCost);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public ArrayList<HashMap<String, String>> getAllBills() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(cursor.getColumnIndexOrThrow(COL_ID)));
                map.put("month", cursor.getString(cursor.getColumnIndexOrThrow(COL_MONTH)));
                map.put("final", String.format("Final Cost: RM %.2f",
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_FINAL))));
                list.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public void deleteAllBills() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public Cursor getBillById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?", new String[]{id});
    }
}
