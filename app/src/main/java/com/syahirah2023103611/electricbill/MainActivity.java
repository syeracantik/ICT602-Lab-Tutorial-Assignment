
package com.syahirah2023103611.electricbill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText editTextUnit;
    RadioGroup radioGroupRebate;
    Button buttonCalculate, buttonSave, buttonViewBills;
    TextView textViewTotalCharges, textViewFinalCost;

    String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("⚡ Electricity Bill Calculator ⚡");
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editTextUnit = findViewById(R.id.editTextUnit);
        radioGroupRebate = findViewById(R.id.radioGroupRebate);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonViewBills = findViewById(R.id.buttonViewBills);
        textViewTotalCharges = findViewById(R.id.textViewTotalCharges);
        textViewFinalCost = findViewById(R.id.textViewFinalCost);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(monthAdapter);

        dbHelper = new DatabaseHelper(this);

        buttonCalculate.setOnClickListener(view -> calculateBill());

        buttonSave.setOnClickListener(v -> {
            String unitStr = editTextUnit.getText().toString().trim();

            if (unitStr.isEmpty() || radioGroupRebate.getCheckedRadioButtonId() == -1) {
                Toast.makeText(MainActivity.this, "Please fill in all input fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int unit = Integer.parseInt(unitStr);
            if (unit < 1 || unit > 9999) {
                Toast.makeText(this, "Unit must be between 1 and 9999 kWh", Toast.LENGTH_SHORT).show();
                return;
            }

            double rebate = getSelectedRebate();

            double totalCharges = calculateTotalCharges(unit);
            double finalCost = totalCharges - (totalCharges * rebate);
            String selectedMonth = spinnerMonth.getSelectedItem().toString();

            boolean inserted = dbHelper.insertBill(selectedMonth, unit, totalCharges, rebate, finalCost);
            if (inserted) {
                Toast.makeText(MainActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to save!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonViewBills.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MonthlyBillActivity.class);
            startActivity(intent);
        });
    }

    private void calculateBill() {
        String unitStr = editTextUnit.getText().toString().trim();

        if (unitStr.isEmpty() || radioGroupRebate.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please fill in all input fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int unit = Integer.parseInt(unitStr);
        if (unit < 1 || unit > 9999) {
            Toast.makeText(this, "Unit must be between 1 and 9999 kWh", Toast.LENGTH_SHORT).show();
            return;
        }

        double rebatePercent = getSelectedRebate();
        double totalCharges = calculateTotalCharges(unit);
        double finalCost = totalCharges - (totalCharges * rebatePercent);

        textViewTotalCharges.setText(String.format("Total Charges: RM %.2f", totalCharges));
        textViewFinalCost.setText(String.format("Final Cost: RM %.2f", finalCost));
    }

    private double getSelectedRebate() {
        int selectedId = radioGroupRebate.getCheckedRadioButtonId();
        RadioButton selectedButton = findViewById(selectedId);
        String rebateText = selectedButton.getText().toString().replace("%", "");
        return Double.parseDouble(rebateText) / 100.0;
    }

    private double calculateTotalCharges(int unit) {
        if (unit <= 200) {
            return unit * 0.218;
        } else if (unit <= 300) {
            return 200 * 0.218 + (unit - 200) * 0.334;
        } else if (unit <= 600) {
            return 200 * 0.218 + 100 * 0.334 + (unit - 300) * 0.516;
        } else {
            return 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (unit - 600) * 0.546;
        }
    }
}
