package com.syahirah2023103611.electricbill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText editTextUnit, editTextRebate;
    Button buttonCalculate, buttonSave, buttonClearAll, buttonAbout;
    TextView textViewTotalCharges, textViewFinalCost;
    ListView listViewHistory;

    String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    DatabaseHelper dbHelper;
    ArrayList<HashMap<String, String>> billList;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editTextUnit = findViewById(R.id.editTextUnit);
        editTextRebate = findViewById(R.id.editTextRebate);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonClearAll = findViewById(R.id.buttonClearAll);
        buttonAbout = findViewById(R.id.buttonAbout);
        textViewTotalCharges = findViewById(R.id.textViewTotalCharges);
        textViewFinalCost = findViewById(R.id.textViewFinalCost);
        listViewHistory = findViewById(R.id.listViewHistory);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(monthAdapter);

        dbHelper = new DatabaseHelper(this);
        loadBillList();

        buttonCalculate.setOnClickListener(view -> calculateBill());

        buttonSave.setOnClickListener(v -> {
            String unitStr = editTextUnit.getText().toString().trim();
            String rebateStr = editTextRebate.getText().toString().trim();

            if (unitStr.isEmpty() || rebateStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in all input fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int unit = Integer.parseInt(unitStr);
            double rebate = Double.parseDouble(rebateStr) / 100.0;

            double totalCharges = 0;
            if (unit <= 200) {
                totalCharges = unit * 0.218;
            } else if (unit <= 300) {
                totalCharges = 200 * 0.218 + (unit - 200) * 0.334;
            } else if (unit <= 600) {
                totalCharges = 200 * 0.218 + 100 * 0.334 + (unit - 300) * 0.516;
            } else {
                totalCharges = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (unit - 600) * 0.546;
            }

            double finalCost = totalCharges - (totalCharges * rebate);
            String selectedMonth = spinnerMonth.getSelectedItem().toString();

            boolean inserted = dbHelper.insertBill(selectedMonth, unit, totalCharges, rebate, finalCost);
            if (inserted) {
                Toast.makeText(MainActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
                loadBillList();
            } else {
                Toast.makeText(MainActivity.this, "Failed to save!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonClearAll.setOnClickListener(v -> {
            dbHelper.deleteAllBills();
            Toast.makeText(MainActivity.this, "All data has been deleted", Toast.LENGTH_SHORT).show();
            loadBillList();
        });

        buttonAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        listViewHistory.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, String> selectedItem = billList.get(position);
            String selectedId = selectedItem.get("id");

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("id", selectedId);
            startActivity(intent);
        });
    }

    private void calculateBill() {
        String unitStr = editTextUnit.getText().toString().trim();
        String rebateStr = editTextRebate.getText().toString().trim();

        if (unitStr.isEmpty() || rebateStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all input fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int unit = Integer.parseInt(unitStr);
        double rebatePercent = Double.parseDouble(rebateStr) / 100.0;

        double totalCharges = 0;

        if (unit <= 200) {
            totalCharges = unit * 0.218;
        } else if (unit <= 300) {
            totalCharges = 200 * 0.218 + (unit - 200) * 0.334;
        } else if (unit <= 600) {
            totalCharges = 200 * 0.218 + 100 * 0.334 + (unit - 300) * 0.516;
        } else {
            totalCharges = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (unit - 600) * 0.546;
        }

        double finalCost = totalCharges - (totalCharges * rebatePercent);

        textViewTotalCharges.setText(String.format("Total Charges: RM %.2f", totalCharges));
        textViewFinalCost.setText(String.format("Final Cost: RM %.2f", finalCost));
    }

    private void loadBillList() {
        if (billList == null) {
            billList = new ArrayList<>();
        } else {
            billList.clear();
        }

        billList.addAll(dbHelper.getAllBills());

        if (adapter == null) {
            adapter = new SimpleAdapter(
                    this,
                    billList,
                    android.R.layout.simple_list_item_2,
                    new String[]{"month", "final"},
                    new int[]{android.R.id.text1, android.R.id.text2}
            );
            listViewHistory.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
