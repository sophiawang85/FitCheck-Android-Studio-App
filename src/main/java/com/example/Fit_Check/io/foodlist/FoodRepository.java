package com.example.Fit_Check.io.foodlist;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.Fit_Check.io.FitCheckDatabase;
import com.example.Fit_Check.model.Food;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodRepository {
    private final FoodDao foodDao;
    //private final LiveData<List<Food>> foodList;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public FoodRepository(Context context) {
        FitCheckDatabase database = FitCheckDatabase.getInstance(context);
        foodDao = database.foodDao();
    }

    public LiveData<List<Food>> getFoodForDate(int dateId) {
        return foodDao.getFoodForDate(dateId);
    }

    public LiveData<List<Food>> getFoods() {
        return foodDao.getFoods();
    }

    public void insert(Food food) {
        executorService.execute(() -> foodDao.insert(food));
    }

    public void update(Food food) {
        executorService.execute(() -> foodDao.update(food));
    }

    public void delete(Food food) {
        executorService.execute(() -> foodDao.delete(food));
    }
}
