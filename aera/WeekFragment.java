package com.example.analytics;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class WeekFragment extends Fragment {

    private BarChart barChart;
    private PieChart weekPieChart;
    private TextView tvTotalWeekEmissions;
    private TextView tvWeekComparison;

    private TextView tvTransportKg, tvTransportPercent;
    private TextView tvEnergyKg, tvEnergyPercent;
    private TextView tvFoodKg, tvFoodPercent;
    private TextView tvShoppingKg, tvShoppingPercent;
    private TextView tvWasteKg, tvWastePercent;

    private static final float HIGH_EMISSION_THRESHOLD = 5.0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barChart = view.findViewById(R.id.barChart);
        weekPieChart = view.findViewById(R.id.weekPieChart);
        tvTotalWeekEmissions = view.findViewById(R.id.tvTotalWeekEmissions);
        tvWeekComparison = view.findViewById(R.id.tvWeekComparison);

        // Bind Category TextViews
        tvTransportKg = view.findViewById(R.id.tvTransportKg);
        tvTransportPercent = view.findViewById(R.id.tvTransportPercent);
        tvEnergyKg = view.findViewById(R.id.tvEnergyKg);
        tvEnergyPercent = view.findViewById(R.id.tvEnergyPercent);
        tvFoodKg = view.findViewById(R.id.tvFoodKg);
        tvFoodPercent = view.findViewById(R.id.tvFoodPercent);
        tvShoppingKg = view.findViewById(R.id.tvShoppingKg);
        tvShoppingPercent = view.findViewById(R.id.tvShoppingPercent);
        tvWasteKg = view.findViewById(R.id.tvWasteKg);
        tvWastePercent = view.findViewById(R.id.tvWastePercent);

        barChart.post(this::setupBarChart);
        weekPieChart.post(this::setupPieChart);
    }

    private void setupPieChart() {
        weekPieChart.setDrawHoleEnabled(true);
        weekPieChart.setHoleColor(Color.WHITE);
        weekPieChart.setHoleRadius(82f);
        weekPieChart.setTransparentCircleRadius(0f);
        weekPieChart.setCenterTextRadiusPercent(100f);
        weekPieChart.setCenterTextSize(20f);

        weekPieChart.getDescription().setEnabled(false);
        weekPieChart.getLegend().setEnabled(false);
        weekPieChart.setRotationEnabled(false);

        String text = "Emissions\n0 kg";
        SpannableString spannableText = new SpannableString(text);

        spannableText.setSpan(new RelativeSizeSpan(0.75f), 0, 9, 0);
        spannableText.setSpan(new ForegroundColorSpan(Color.parseColor("#6B7280")), 0, 9, 0);

        int startOfSecondLine = 10;
        spannableText.setSpan(new RelativeSizeSpan(1.7f), startOfSecondLine, text.length(), 0);
        spannableText.setSpan(new StyleSpan(Typeface.BOLD), startOfSecondLine, text.length(), 0);
        spannableText.setSpan(new ForegroundColorSpan(Color.parseColor("#2D6A4F")), startOfSecondLine, text.length(), 0);

        weekPieChart.setCenterText(spannableText);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(1f, ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.parseColor("#E5E7EB"));
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        weekPieChart.setData(data);
        weekPieChart.notifyDataSetChanged();
        weekPieChart.invalidate();
    }

    private void setupBarChart() {
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawBorders(false);

        // X-Axis Configuration
        String[] days = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.parseColor("#6B7280"));
        xAxis.setTextSize(12f);

        // Hide Y-Axis numbers (0.2, 0.4, etc.)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawLabels(false); // REMOVES THE NUMBERS ON THE LEFT
        leftAxis.setDrawGridLines(false); // Removes horizontal grid lines
        leftAxis.setDrawAxisLine(false); // Removes left axis line

        barChart.getAxisRight().setEnabled(false);

        float[] initialWeekData = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f};
        updateBarChartData(initialWeekData);
    }

    public void updateBarChartData(float[] dailyValues) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for (int i = 0; i < dailyValues.length && i < 7; i++) {
            float value = dailyValues[i];
            entries.add(new BarEntry(i, value));

            if (value > HIGH_EMISSION_THRESHOLD) {
                colors.add(Color.parseColor("#EF4444")); // Red
            } else {
                colors.add(Color.parseColor("#2D6A4F")); // Green
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "Weekly Emissions");
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.4f);

        barChart.setData(barData);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }
}
