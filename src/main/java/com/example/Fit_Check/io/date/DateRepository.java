package com.example.Fit_Check.io.date;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.Fit_Check.io.FitCheckDatabase;
import com.example.Fit_Check.model.Date;
import com.example.Fit_Check.model.DateWithFoodList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateRepository {
    private final DateDao dateDao;

    private final LiveData<List<Date>> dates;
    private final LiveData<List<DateWithFoodList>> datewithFoodList;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public DateRepository(Context context) {
        FitCheckDatabase database = FitCheckDatabase.getInstance(context);
        dateDao = database.dateDao();
        dates = dateDao.getAllDates();
        datewithFoodList = dateDao.getAllDateWithFood();
    }

    /*public LiveData<List<Date>> getShoppingGroups() {
        return dates;
    }*/

    public LiveData<List<DateWithFoodList>> getDatewithFoodList() {
        return datewithFoodList;
    }

    public void insert(Date date) {
        executorService.execute(() -> dateDao.insert(date));
    }

    public void update(Date date) {
        executorService.execute(() -> dateDao.update(date));
    }

    public void delete(Date date) {
        executorService.execute(() -> dateDao.delete(date));
    }
}
