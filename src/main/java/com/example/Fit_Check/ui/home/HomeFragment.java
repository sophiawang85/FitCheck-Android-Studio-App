package com.example.Fit_Check.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.Fit_Check.model.DateWithFoodList;
import com.example.Fit_Check.ui.adapter.FoodListAdapter;
import com.example.Fit_Check.R;
import com.example.Fit_Check.databinding.FragmentHomeBinding;
import com.example.Fit_Check.ui.dashboard.DashboardViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListView dateListView;
    private HomeViewModel homeViewModel;
    private FoodListAdapter adapter;
    private View root;
    private DashboardViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final HomeViewModelFactory homeViewModelFactory = new HomeViewModelFactory(requireContext());
        this.homeViewModel = new ViewModelProvider(requireActivity(), homeViewModelFactory).get(HomeViewModel.class);

        this.binding = FragmentHomeBinding.inflate(inflater, container, false);

        View headerView = inflater.inflate(R.layout.date_list_header_layout, null);
        root = inflater.inflate(R.layout.fragment_home, null);
        this.dateListView = binding.dateListView;
        this.dateListView.addHeaderView(headerView);

        //set adapter for list view in order to show data in list view
        adapter = new FoodListAdapter(getContext(), new ArrayList<>(), homeViewModel);
        this.dateListView.setAdapter(adapter);

        homeViewModel.getDates().observe(getViewLifecycleOwner(), dates -> {
            adapter.clear();
            adapter.addAll(dates);
        });

        setUpEventListener();

        populateCalorieLineChart(binding.getRoot(), homeViewModel.getDates());
        return binding.getRoot();
    }

    private void populateCalorieLineChart(final View root, LiveData<List<DateWithFoodList>> alldates)
    {
        final LineChart lineChart = root.findViewById(R.id.lineChart);
        alldates.observe(getViewLifecycleOwner(), dates -> {
            if (dates == null ||dates.isEmpty()) {
                lineChart.clear();
                lineChart.invalidate();
                return;
            }
            List<Entry> entries = new ArrayList<>();

                int listSize = dates.size();
                for (int i=0;i<listSize;i++) {
                    entries.add(new Entry(i, (float) dates.get(i).totalCalories()));
                }

            LineDataSet dataSet = new LineDataSet(entries, "Calories Trend");

// Line and circle colors
            dataSet.setColor(Color.parseColor("#4A90E2")); // Soft Blue (Line Color)
            dataSet.setLineWidth(2.5f);
            dataSet.setCircleColor(Color.parseColor("#50E3C2")); // Teal-Green (Circle Color)
            dataSet.setCircleRadius(5f);
            dataSet.setCircleHoleColor(Color.WHITE); // White hole in circles
// Value text color
            dataSet.setValueTextColor(Color.parseColor("#2C3E50")); // Dark Blue-Gray (Text Color)
            dataSet.setValueTextSize(12f);
// Highlight color when a point is selected
            dataSet.setHighLightColor(Color.parseColor("#D0021B")); // Strong Red for Contrast
// Gradient fill for a modern look
            dataSet.setDrawFilled(true);
            dataSet.setFillColor(Color.parseColor("#BFD6F6")); // Light Blue Fill

        // Set data to LineChart
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        // Customize X-axis

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);  // Ensure one label per tick
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < dates.size()) {
                    return dates.get(index).date.getName();
                }
                return "";
            }
        });
        // Hide right Y-axis
        lineChart.getAxisRight().setEnabled(false);

        // Refresh chart
        lineChart.invalidate();

        });

    }
    private void setUpEventListener() {
        Button addButton = binding.addButton;

        TooltipCompat.setTooltipText(addButton, "Record a new Date");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_create_new_shopping_group);
            }
        });

        /*
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateWithFoodList selectedShoppingGroup = adapter.getSelectedShoppingGroup();
                if (selectedShoppingGroup != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("selectedShoppingGroup", selectedShoppingGroup);
                    Navigation.findNavController(v).navigate(R.id.action_create_new_shopping_group, bundle);
                }
            }
        });
    */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}