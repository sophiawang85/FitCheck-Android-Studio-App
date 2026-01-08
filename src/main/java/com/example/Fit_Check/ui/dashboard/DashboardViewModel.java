package com.example.Fit_Check.ui.dashboard;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.Fit_Check.io.addcalories.AddCaloriesRepository;
import com.example.Fit_Check.io.date.DateRepository;
import com.example.Fit_Check.io.datewithfood.DateWithFoodRepository;
import com.example.Fit_Check.model.AddCalories;
import com.example.Fit_Check.model.DateWithFoodList;

import java.util.List;

import lombok.NonNull;

public class DashboardViewModel extends ViewModel {

    private final AddCaloriesRepository addCaloriesRepository;
    private final LiveData<List<AddCalories>> addcalories;

    private final DateWithFoodRepository dateWithFoodRepository;
    private final DateRepository dateRepository;

    private final LiveData<List<DateWithFoodList>> allDates;
    public DashboardViewModel(@NonNull Context context) {
        this.addCaloriesRepository = new AddCaloriesRepository(context);
        this.addcalories = addCaloriesRepository.getAddcalories();
        this.dateWithFoodRepository = new DateWithFoodRepository(context);
        this.dateRepository = new DateRepository(context);

        allDates = dateWithFoodRepository.getAllDates();
    }

    public LiveData<List<DateWithFoodList>> getAllDates() {
        return allDates;
    }

    public LiveData<List<AddCalories>> getAddcalories() {
        return addcalories;
    }
}