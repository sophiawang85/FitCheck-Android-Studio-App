package com.example.Fit_Check.io.datewithfood;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.Fit_Check.model.DateWithFoodList;
import com.example.Fit_Check.io.FitCheckDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateWithFoodRepository {
    private final DateWithFoodDao dateWithFoodDao;
    private final LiveData<List<DateWithFoodList>> allDates;
    private final LiveData<List<DateWithFoodList>> dateWithFood;
    private final ExecutorService executorService;

    public DateWithFoodRepository(Context context) {
        FitCheckDatabase database = FitCheckDatabase.getInstance(context);
        dateWithFoodDao = database.dateWithFoodDao();

        allDates = dateWithFoodDao.getalldates();
        dateWithFood = dateWithFoodDao.getDateWithFood();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<DateWithFoodList>> getAllDates() {
        return allDates;
    }

    //public LiveData<List<DateWithFoodList>> getDateWithFood() {
       // return dateWithFood;
   // }
}
