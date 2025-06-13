package com.syahirah2023103611.electricbill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;

public class MonthlyBillActivity extends AppCompatActivity {

    ListView listViewHistory;
    Button buttonClearAll;
    ArrayList<HashMap<String, String>> billList;
    DatabaseHelper dbHelper;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_bill);

        // ✅ Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Monthly Bill History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Optional back arrow
        }

        // ✅ Component setup
        listViewHistory = findViewById(R.id.listViewHistory);
        buttonClearAll = findViewById(R.id.buttonClearAll);
        dbHelper = new DatabaseHelper(this);

        // ✅ Load data
        loadBillList();

        // ✅ Item click to DetailActivity
        listViewHistory.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, String> selectedItem = billList.get(position);
            String selectedId = selectedItem.get("id");

            Intent intent = new Intent(MonthlyBillActivity.this, DetailActivity.class);
            intent.putExtra("id", selectedId);
            startActivity(intent);
        });

        // ✅ Delete all records
        buttonClearAll.setOnClickListener(v -> {
            dbHelper.deleteAllBills();
            Toast.makeText(this, "All records cleared!", Toast.LENGTH_SHORT).show();
            loadBillList();
        });
    }

    private void loadBillList() {
        billList = dbHelper.getAllBills();
        adapter = new SimpleAdapter(
                this,
                billList,
                android.R.layout.simple_list_item_2,
                new String[]{"month", "final"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        listViewHistory.setAdapter(adapter);
    }

    // ✅ Handle back arrow
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
