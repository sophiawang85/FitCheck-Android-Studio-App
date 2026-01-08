package com.example.Fit_Check.io.addcalories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.Fit_Check.io.FitCheckDatabase;
import com.example.Fit_Check.model.AddCalories;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddCaloriesRepository {
    private final AddCaloriesDao addCaloriesDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public AddCaloriesRepository(Context context) {
        FitCheckDatabase database = FitCheckDatabase.getInstance(context);
        addCaloriesDao = database.addcalorieDao();
    }

    public LiveData<List<AddCalories>> getAddcalories() {
        return addCaloriesDao.getAddcalories();
    }

    public void insert(AddCalories addCalories) {
        executorService.execute(() -> addCaloriesDao.insert(addCalories));
    }

    public void update(AddCalories addCalories) {
        executorService.execute(() -> addCaloriesDao.update(addCalories));
    }

    public void delete(AddCalories addCalories) {
        executorService.execute(() -> addCaloriesDao.delete(addCalories));
    }
    public LiveData<List<AddCalories>> getTransactionsForShoppingGroup(int dateId) {
        return addCaloriesDao.getAddcaloriesForDate(dateId);
    }

    public  LiveData<List<AddCalories>> getTransactionsForMerchandise(int foodId) {
        return addCaloriesDao.getAddcaloriesForFood(foodId);
    }
}
