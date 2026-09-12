package com.example.aera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ComputationsFragment extends Fragment {

    private static final String TRANSPORT_FORMULA = "(Distance × Trips × Vehicle EF) ÷ Passengers";
    private static final String ELECTRICITY_FORMULA = "kWh × 0.70 kg CO₂e/kWh";
    private static final String FOOD_FORMULA = "Amount (kg) × Food EF";
    private static final String SHOPPING_FORMULA = "Quantity × Product EF";
    private static final String WASTE_FORMULA = "Amount (kg) × Waste EF";

    private static final String[][] TRANSPORT_ROWS = {
            {"Motorcycle", "(km × trips × 0.266) ÷ passengers"},
            {"Tricycle", "(km × trips × 0.266) ÷ passengers"},
            {"Car", "(km × trips × 0.319) ÷ passengers"},
            {"Taxi", "(km × trips × 0.292) ÷ passengers"},
            {"Jeepney", "(km × trips × 0.415) ÷ passengers"},
            {"Bus", "(km × trips × 1.097) ÷ passengers"},
            {"UV/Van", "(km × trips × 0.415) ÷ passengers"},
    };

    private static final String[][] ELECTRICITY_ROWS = {};

    private static final String[][] FOOD_ROWS = {
            {"High-Impact Meat", "kg × 27.0 kg CO₂e/kg"},
            {"Medium-Impact Meat", "kg × 8.0 kg CO₂e/kg"},
            {"Low-Impact Meat", "kg × 6.0 kg CO₂e/kg"},
            {"Seafood", "kg × 5.0 kg CO₂e/kg"},
            {"Dairy & Eggs", "kg × 4.0 kg CO₂e/kg"},
            {"Grains & Staples", "kg × 2.5 kg CO₂e/kg"},
            {"Fruits & Vegetables", "kg × 0.9 kg CO₂e/kg"},
            {"Legumes & Plant-Based", "kg × 1.0 kg CO₂e/kg"},
            {"Processed & Packaged", "kg × 2.5 kg CO₂e/kg"},
            {"Sweets & Desserts", "kg × 5.0 kg CO₂e/kg"},
            {"Beverages", "kg × 1.5 kg CO₂e/kg"},
    };

    private static final String[][] SHOPPING_ROWS = {
            {"Clothing & Footwear", "items × 15 kg CO₂e/item"},
            {"Electronics", "items × 100 kg CO₂e/item"},
            {"Furniture", "items × 50 kg CO₂e/item"},
            {"Household Products", "items × 5 kg CO₂e/item"},
            {"Personal Care", "items × 2 kg CO₂e/item"},
            {"Paper Products", "items × 1.5 kg CO₂e/item"},
            {"Plastic Products", "items × 3 kg CO₂e/item"},
            {"Other Retail Goods", "items × 5 kg CO₂e/item"},
    };

    private static final String[][] WASTE_ROWS = {
            {"Food Waste", "kg × 0.5 kg CO₂e/kg"},
            {"Paper & Cardboard", "kg × 1.5 kg CO₂e/kg"},
            {"Plastic", "kg × 3.0 kg CO₂e/kg"},
            {"Glass", "kg × 0.9 kg CO₂e/kg"},
            {"Metal", "kg × 2.0 kg CO₂e/kg"},
            {"Textiles", "kg × 5.0 kg CO₂e/kg"},
            {"Electronic Waste", "kg × 20 kg CO₂e/kg"},
            {"Mixed/General Waste", "kg × 2.0 kg CO₂e/kg"},
    };

    private static final int CATEGORY_GAP_DP = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.computations_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View backButton = view.findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v ->
                    requireActivity().getSupportFragmentManager().popBackStack());
        }

        LinearLayout table = view.findViewById(R.id.formulaTable);
        if (table != null) {
            LayoutInflater inflater = LayoutInflater.from(requireContext());
            addCategory(table, inflater, getString(R.string.transportation_title),
                    TRANSPORT_FORMULA, TRANSPORT_ROWS, false);
            addCategory(table, inflater, getString(R.string.electricity_title),
                    ELECTRICITY_FORMULA, ELECTRICITY_ROWS, true);
            addCategory(table, inflater, getString(R.string.food),
                    FOOD_FORMULA, FOOD_ROWS, true);
            addCategory(table, inflater, getString(R.string.shopping),
                    SHOPPING_FORMULA, SHOPPING_ROWS, true);
            addCategory(table, inflater, getString(R.string.waste),
                    WASTE_FORMULA, WASTE_ROWS, true);
        }
    }

    private void addCategory(LinearLayout table, LayoutInflater inflater, String title,
                             String formula, String[][] rows, boolean gapAbove) {
        addRow(table, inflater, title, formula, gapAbove);
        for (String[] row : rows) {
            addRow(table, inflater, row[0], row[1], false);
        }
    }

    private void addRow(LinearLayout table, LayoutInflater inflater, String name, String formula,
                        boolean gapAbove) {
        View row = inflater.inflate(R.layout.computations_row, table, false);

        TextView nameView = row.findViewById(R.id.rowName);
        if (nameView != null) {
            nameView.setText(name);
        }

        TextView formulaView = row.findViewById(R.id.rowFormula);
        if (formulaView != null) {
            formulaView.setText(formula);
        }

        if (gapAbove) {
            ViewGroup.LayoutParams params = row.getLayoutParams();
            if (params instanceof LinearLayout.LayoutParams) {
                float scale = getResources().getDisplayMetrics().density;
                ((LinearLayout.LayoutParams) params).topMargin =
                        (int) (CATEGORY_GAP_DP * scale + 0.5f);
            }
        }

        table.addView(row);
    }
}
