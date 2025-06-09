package com.syahirah2023103611.electricbill;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView textViewMonth, textViewUnit, textViewTotal, textViewRebate, textViewFinal;
    com.syahirah2023103611.electricbill.DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textViewMonth = findViewById(R.id.textViewMonth);
        textViewUnit = findViewById(R.id.textViewUnit);
        textViewTotal = findViewById(R.id.textViewTotal);
        textViewRebate = findViewById(R.id.textViewRebate);
        textViewFinal = findViewById(R.id.textViewFinal);

        dbHelper = new com.syahirah2023103611.electricbill.DatabaseHelper(this);

        String id = getIntent().getStringExtra("id");
        if (id != null) {
            Cursor cursor = dbHelper.getBillById(id);
            if (cursor.moveToFirst()) {
                textViewMonth.setText(cursor.getString(cursor.getColumnIndexOrThrow("month")));
                textViewUnit.setText(cursor.getInt(cursor.getColumnIndexOrThrow("unit")) + " kWh");
                textViewTotal.setText(String.format("RM %.2f", cursor.getDouble(cursor.getColumnIndexOrThrow("total"))));
                textViewRebate.setText(String.format(" %.2f%%", cursor.getDouble(cursor.getColumnIndexOrThrow("rebate")) * 100));
                textViewFinal.setText(String.format("RM %.2f", cursor.getDouble(cursor.getColumnIndexOrThrow("final"))));
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
