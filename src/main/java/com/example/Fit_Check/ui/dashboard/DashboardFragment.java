package com.example.Fit_Check.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.Fit_Check.io.datewithfood.DateWithFoodDao;
import com.example.Fit_Check.io.datewithfood.DateWithFoodRepository;
import com.example.Fit_Check.model.AddCalories;
import com.example.Fit_Check.model.DateWithFoodList;
import com.example.Fit_Check.ui.adapter.HistoryListAdapter;
import com.example.Fit_Check.R;
import com.example.Fit_Check.ui.date.DateFragment;
import com.example.Fit_Check.ui.foodlist.FoodListFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;
    private HistoryListAdapter adapter;
    private DateWithFoodRepository dateWithFoodRepository;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final DashboardViewModelFactory dashboardViewModelFactory = new DashboardViewModelFactory(requireContext());
        this.viewModel = new ViewModelProvider(requireActivity(), dashboardViewModelFactory).get(DashboardViewModel.class);

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final ListView addcalListView = root.findViewById(R.id.listViewaddcal);

        View headerView = inflater.inflate(R.layout.history_list_header, null);
        addcalListView.addHeaderView(headerView);
        setupClickListenerForHeader(headerView);
        setupQueryButtonClickListener(root);
        viewModel.getAddcalories().observe(getViewLifecycleOwner(), addcalList -> {
            populateAutoCompleteFilters(root, addcalList);
        });

        adapter = new HistoryListAdapter(getContext(), new ArrayList<>());
        addcalListView.setAdapter(adapter);
        refreshData(root, new ArrayList<>(), new ArrayList<>());

        //Log.d("Main Activity", viewModel.getAllDates().getValue().get(0).date.getName());
        return root;
    }
    private void refreshData(final View root, List<String> foodNameFilers, List<String> foodTypeFilters) {
        viewModel.getAddcalories().observe(getViewLifecycleOwner(), transactionList -> {
            adapter.clear();
            List<AddCalories> addcalAfterNameFilters = transactionList.stream().filter(transaction -> foodNameFilers.isEmpty()
                    || foodNameFilers.contains(transaction.getFoodName())).collect(Collectors.toList());

            List<AddCalories> addcalAfterTypeFilters = addcalAfterNameFilters.stream().filter(transaction -> foodTypeFilters.isEmpty()
                    || foodTypeFilters.contains(transaction.getFoodType())).collect(Collectors.toList());
            adapter.addAll(addcalAfterTypeFilters);

            populateFoodTypePieChart(root, addcalAfterTypeFilters);


        });
    }
    private void populateAutoCompleteFilters(final View root, final List<AddCalories> addCaloriesList) {
        List<String> merchandiseNames = addCaloriesList
                .stream()
                .map(AddCalories::getFoodName)
                .distinct()
                .collect(Collectors.toList());
        MultiAutoCompleteTextView multiAutoCompleteTextViewFoodName = root.findViewById(R.id.multiAutoCompleteTextViewFoodName);
        ArrayAdapter<String> foodNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, merchandiseNames);
        multiAutoCompleteTextViewFoodName.setAdapter(foodNameAdapter);
        multiAutoCompleteTextViewFoodName.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        List<String> merchandiseTypes = addCaloriesList
                .stream()
                .map(AddCalories::getFoodType)
                .distinct()
                .collect(Collectors.toList());
        MultiAutoCompleteTextView multiAutoCompleteTextViewFoodType = root.findViewById(R.id.multiAutoCompleteTextViewFoodType);
        ArrayAdapter<String> foodTypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, merchandiseTypes);
        multiAutoCompleteTextViewFoodType.setAdapter(foodTypeAdapter);
        multiAutoCompleteTextViewFoodType.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void populateFoodTypePieChart(final View root, final List<AddCalories> addCaloriesList) {
        final PieChart pieChart = root.findViewById(R.id.pieChart);
        final Map<String, Double> amountByFoodType = addCaloriesList
                .stream()
                .collect(Collectors.groupingBy(AddCalories::getFoodType,
                        Collectors.summingDouble(calories -> calories.getServings() * calories.getCalperserving())));
        float total = 0f;
        final ArrayList<PieEntry> entries = new ArrayList<>();

        for(final Map.Entry<String, Double> entry : amountByFoodType.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
            total += entry.getValue().floatValue();
        }

        final PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        final PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentValueFormatter(total));
        data.setValueTextSize(12f);
        pieChart.setData(data);

        pieChart.setDrawHoleEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.getLegend().setEnabled(true);
        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

        pieChart.invalidate();
    }

    private void setupQueryButtonClickListener(final View root) {
        final Button buttonFoodNameQuery = root.findViewById(R.id.buttonFoodNameQuery);
        buttonFoodNameQuery.setVisibility(View.INVISIBLE);
        final Button buttonFoodTypeQuery = root.findViewById(R.id.buttonFoodTypeQuery);
        buttonFoodTypeQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedMerchandiseNames = getValuesFromMultiAutoCompleteTextView(root, R.id.multiAutoCompleteTextViewFoodName);
                List<String> selectedMerchandiseTypes = getValuesFromMultiAutoCompleteTextView(root, R.id.multiAutoCompleteTextViewFoodType);
                refreshData(root, selectedMerchandiseNames, selectedMerchandiseTypes);
            }
        });
    }

    private List<String> getValuesFromMultiAutoCompleteTextView(final View root, int resId) {
        MultiAutoCompleteTextView multiAutoCompleteTextView = root.findViewById(resId);
        String text = multiAutoCompleteTextView.getText().toString().trim();
        if (StringUtils.isEmpty(text.trim())) {
            return new ArrayList<>();
        }
        return Arrays.stream(text.split(",")).map(String::trim).collect(Collectors.toList());
    }
    private void setupClickListenerForHeader(View headerView) {

        final TextView foodName = headerView.findViewById(R.id.textViewFoodName);
        final TextView foodType = headerView.findViewById(R.id.textViewFoodType);
        final TextView DateName = headerView.findViewById(R.id.textViewDate);
        final TextView purchasePrice = headerView.findViewById(R.id.textViewCaloriesPerServing);
        final TextView purchasedUnits = headerView.findViewById(R.id.textViewServingsEaten);

        foodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortDataByColumn(0);
            }
        });

        foodType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortDataByColumn(1);
            }
        });

        DateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortDataByColumn(2);
            }
        });

        purchasePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortDataByColumn(3);
            }
        });
        purchasedUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortDataByColumn(4);
            }
        });
    }

    private static class PercentValueFormatter extends ValueFormatter {
        private float total = 0f;
        public PercentValueFormatter(float total) {
            this.total = total;
        }
        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            float originalValue = pieEntry.getValue();
            float percentage = (originalValue / total) * 100;
            return String.format(Locale.getDefault(), "%.2f (%.0f%%)", originalValue, percentage);
        }
    }
}