package com.example.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import androidx.core.content.res.ResourcesCompat;
import android.text.style.RelativeSizeSpan;


import java.util.ArrayList;

public class DayFragment extends Fragment {

    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart = view.findViewById(R.id.pieChart);

        pieChart.post(this::setupPieChart);
    }
    private void setupPieChart() {
        // hole size and text radius
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(82f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setCenterTextRadiusPercent(100f);

        pieChart.setCenterTextSize(20f);

        // hide chart descriptions and legends
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setRotationEnabled(false);

        // set text scaling using relativesizespan
        String text = "Emissions\n0 kg";
        SpannableString spannableText = new SpannableString(text);

        // "Emissions"
        spannableText.setSpan(new RelativeSizeSpan(0.75f), 0, 9, 0);
        spannableText.setSpan(new ForegroundColorSpan(Color.parseColor("#6B7280")), 0, 9, 0);

        // "0 kg"
        int startOfSecondLine = 10;
        spannableText.setSpan(new RelativeSizeSpan(1.5f), startOfSecondLine, text.length(), 0);
        spannableText.setSpan(new StyleSpan(Typeface.BOLD), startOfSecondLine, text.length(), 0);
        spannableText.setSpan(new ForegroundColorSpan(Color.parseColor("#1F2937")), startOfSecondLine, text.length(), 0);

        pieChart.setCenterText(spannableText);

        // chart data setup
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(1f, ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.parseColor("#E5E7EB"));
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }
}
