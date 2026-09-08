package com.example.activitydasbord.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.activitydasbord.R;
import com.example.activitydasbord.database.DatabaseHelper;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;
import java.util.Map;

public class LogActivityFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private String currentCategory = "Transport";
    
    // UI Elements
    private LinearLayout layoutTransport, layoutEnergy, layoutGeneral;
    private ChipGroup chipGroup;
    private Spinner spinnerTransport, spinnerSubtype;
    private EditText etDistance, etTrips, etPassengers, etKwh, etAmount;
    private TextView tvEstimatedFootprint, tvSubtypeLabel, tvAmountLabel, tvUnit;
    
    // Data
    private final Map<String, Double> transportFactors = new HashMap<>();
    private final Map<String, Double> foodFactors = new HashMap<>();
    private final Map<String, Double> shoppingFactors = new HashMap<>();
    private final Map<String, Double> wasteFactors = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_activity, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        
        initData();
        initViews(view);
        setupCategorySwitching();
        setupRealTimeCalculation();
        
        view.findViewById(R.id.btn_back).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> saveEntry());
        
        return view;
    }

    private void initData() {
        transportFactors.put("Motorcycle", 0.266);
        transportFactors.put("Tricycle", 0.266);
        transportFactors.put("Car", 0.319);
        transportFactors.put("Taxi", 0.292);
        transportFactors.put("Jeepney", 0.415);
        transportFactors.put("Bus", 1.097);
        transportFactors.put("UV/Van", 0.415);

        foodFactors.put("High-Impact Meat", 27.0);
        foodFactors.put("Medium-Impact Meat", 8.0);
        foodFactors.put("Low-Impact Meat", 6.0);
        foodFactors.put("Seafood", 5.0);
        foodFactors.put("Dairy & Eggs", 4.0);
        foodFactors.put("Grains & Staples", 2.5);
        foodFactors.put("Fruits & Vegetables", 0.9);
        foodFactors.put("Legumes & Plant-Based", 1.0);
        foodFactors.put("Processed & Packaged", 2.5);
        foodFactors.put("Sweets & Desserts", 5.0);
        foodFactors.put("Beverages", 1.5);

        shoppingFactors.put("Clothing & Footwear", 15.0);
        shoppingFactors.put("Electronics", 100.0);
        shoppingFactors.put("Furniture", 50.0);
        shoppingFactors.put("Household Products", 5.0);
        shoppingFactors.put("Personal Care", 2.0);
        shoppingFactors.put("Paper Products", 1.5);
        shoppingFactors.put("Plastic Products", 3.0);
        shoppingFactors.put("Other Retail Goods", 5.0);

        wasteFactors.put("Food Waste", 0.5);
        wasteFactors.put("Paper & Cardboard", 1.5);
        wasteFactors.put("Plastic", 3.0);
        wasteFactors.put("Glass", 0.9);
        wasteFactors.put("Metal", 2.0);
        wasteFactors.put("Textiles", 5.0);
        wasteFactors.put("Electronic Waste", 20.0);
        wasteFactors.put("Mixed/General Waste", 2.0);
    }

    private void initViews(View v) {
        layoutTransport = v.findViewById(R.id.layout_transport);
        layoutEnergy = v.findViewById(R.id.layout_energy);
        layoutGeneral = v.findViewById(R.id.layout_general);
        chipGroup = v.findViewById(R.id.chip_group_category);
        
        spinnerTransport = v.findViewById(R.id.spinner_transport_type);
        spinnerSubtype = v.findViewById(R.id.spinner_subtype);
        
        etDistance = v.findViewById(R.id.et_distance);
        etTrips = v.findViewById(R.id.et_trips);
        etPassengers = v.findViewById(R.id.et_passengers);
        etKwh = v.findViewById(R.id.et_kwh);
        etAmount = v.findViewById(R.id.et_amount);
        
        tvEstimatedFootprint = v.findViewById(R.id.tv_estimated_footprint);
        tvSubtypeLabel = v.findViewById(R.id.tv_subtype_label);
        tvAmountLabel = v.findViewById(R.id.tv_amount_label);
        tvUnit = v.findViewById(R.id.tv_unit);
        
        // Initial setup
        setupSpinner(spinnerTransport, transportFactors.keySet().toArray(new String[0]));
    }

    private void setupCategorySwitching() {
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            layoutTransport.setVisibility(View.GONE);
            layoutEnergy.setVisibility(View.GONE);
            layoutGeneral.setVisibility(View.GONE);
            
            if (checkedId == R.id.chip_transport) {
                currentCategory = "Transport";
                layoutTransport.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.chip_energy) {
                currentCategory = "Energy";
                layoutEnergy.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.chip_food) {
                currentCategory = "Food";
                layoutGeneral.setVisibility(View.VISIBLE);
                updateGeneralUI("Food", "Serving/Amount", "kg", foodFactors.keySet().toArray(new String[0]));
            } else if (checkedId == R.id.chip_shopping) {
                currentCategory = "Shopping";
                layoutGeneral.setVisibility(View.VISIBLE);
                updateGeneralUI("Shopping", "Number of Items", "items/kg", shoppingFactors.keySet().toArray(new String[0]));
            } else if (checkedId == R.id.chip_waste) {
                currentCategory = "Waste";
                layoutGeneral.setVisibility(View.VISIBLE);
                updateGeneralUI("Waste", "Weight", "kg", wasteFactors.keySet().toArray(new String[0]));
            }
            calculateFootprint();
        });
    }

    private void updateGeneralUI(String label, String amountLabel, String unit, String[] types) {
        tvSubtypeLabel.setText(label + " Type");
        tvAmountLabel.setText(amountLabel);
        tvUnit.setText(unit);
        setupSpinner(spinnerSubtype, types);
    }

    private void setupSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateFootprint();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRealTimeCalculation() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateFootprint();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        
        etDistance.addTextChangedListener(tw);
        etTrips.addTextChangedListener(tw);
        etPassengers.addTextChangedListener(tw);
        etKwh.addTextChangedListener(tw);
        etAmount.addTextChangedListener(tw);
    }

    private void calculateFootprint() {
        double result = 0;
        switch (currentCategory) {
            case "Transport":
                String tType = spinnerTransport.getSelectedItem().toString();
                double dist = getDouble(etDistance);
                double trips = getDouble(etTrips);
                double pass = getDouble(etPassengers);
                if (pass == 0) pass = 1;
                result = (dist * trips * transportFactors.get(tType)) / pass;
                break;
            case "Energy":
                result = getDouble(etKwh) * 0.70;
                break;
            case "Food":
                String fType = spinnerSubtype.getSelectedItem().toString();
                result = getDouble(etAmount) * foodFactors.get(fType);
                break;
            case "Shopping":
                String sType = spinnerSubtype.getSelectedItem().toString();
                result = getDouble(etAmount) * shoppingFactors.get(sType);
                break;
            case "Waste":
                String wType = spinnerSubtype.getSelectedItem().toString();
                result = getDouble(etAmount) * wasteFactors.get(wType);
                break;
        }
        tvEstimatedFootprint.setText(String.format("%.1f kg CO2e", result));
    }

    private void saveEntry() {
        if (!validateInputs()) return;

        double co2e = Double.parseDouble(tvEstimatedFootprint.getText().toString().split(" ")[0]);
        String subtype = "";
        double val = 0;
        
        switch (currentCategory) {
            case "Transport":
                subtype = spinnerTransport.getSelectedItem().toString();
                val = getDouble(etDistance);
                break;
            case "Energy":
                subtype = "Electricity";
                val = getDouble(etKwh);
                break;
            default:
                subtype = spinnerSubtype.getSelectedItem().toString();
                val = getDouble(etAmount);
                break;
        }
        
        dbHelper.addActivity(currentCategory, subtype, val, co2e);
        Toast.makeText(requireContext(), "Activity Logged!", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }

    private boolean validateInputs() {
        boolean valid = true;
        switch (currentCategory) {
            case "Transport":
                if (etDistance.getText().toString().isEmpty()) {
                    etDistance.setError("Required");
                    valid = false;
                }
                if (etTrips.getText().toString().isEmpty()) {
                    etTrips.setError("Required");
                    valid = false;
                }
                if (etPassengers.getText().toString().isEmpty()) {
                    etPassengers.setError("Required");
                    valid = false;
                }
                break;
            case "Energy":
                if (etKwh.getText().toString().isEmpty()) {
                    etKwh.setError("Required");
                    valid = false;
                }
                break;
            default:
                if (etAmount.getText().toString().isEmpty()) {
                    etAmount.setError("Required");
                    valid = false;
                }
                break;
        }
        if (!valid) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    private double getDouble(EditText et) {
        try {
            return Double.parseDouble(et.getText().toString());
        } catch (Exception e) {
            return 0;
        }
    }
}