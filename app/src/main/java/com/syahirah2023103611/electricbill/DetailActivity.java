package com.syahirah2023103611.electricbill;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    TextView textViewMonth, textViewUnit, textViewTotal, textViewRebate, textViewFinal;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bill Details");

        textViewMonth = findViewById(R.id.textViewMonth);
        textViewUnit = findViewById(R.id.textViewUnit);
        textViewTotal = findViewById(R.id.textViewTotal);
        textViewRebate = findViewById(R.id.textViewRebate);
        textViewFinal = findViewById(R.id.textViewFinal);

        dbHelper = new DatabaseHelper(this);

        String id = getIntent().getStringExtra("id");
        if (id != null) {
            Cursor cursor = dbHelper.getBillById(id);
            if (cursor.moveToFirst()) {
                textViewMonth.setText(cursor.getString(cursor.getColumnIndexOrThrow("month")));
                textViewUnit.setText(cursor.getInt(cursor.getColumnIndexOrThrow("unit")) + " kWh");
                textViewTotal.setText(String.format("RM %.2f", cursor.getDouble(cursor.getColumnIndexOrThrow("total"))));
                textViewRebate.setText(String.format("%.2f%%", cursor.getDouble(cursor.getColumnIndexOrThrow("rebate")) * 100));
                textViewFinal.setText(String.format("RM %.2f", cursor.getDouble(cursor.getColumnIndexOrThrow("final"))));
            } else {
                Toast.makeText(this, "Bill record not found", Toast.LENGTH_SHORT).show();
                finish();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "No ID passed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
