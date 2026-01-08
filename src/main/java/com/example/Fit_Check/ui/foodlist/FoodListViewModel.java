package com.example.Fit_Check.ui.foodlist;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.Fit_Check.io.addcalories.AddCaloriesRepository;
import com.example.Fit_Check.model.AddCalories;
import com.example.Fit_Check.model.Food;
import com.example.Fit_Check.io.foodlist.FoodRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;

public class FoodListViewModel extends ViewModel {
    private final FoodRepository foodRepository;
    private final AddCaloriesRepository addCaloriesRepository;

    private final LiveData<List<Food>> foods;

    public FoodListViewModel(@NonNull Context context) {
        this.foodRepository = new FoodRepository(context);
        this.addCaloriesRepository = new AddCaloriesRepository(context);
        this.foods = this.foodRepository.getFoods();
    }

    public LiveData<List<Food>> getFoods() {
        return foods;
    }

    public ArrayList<String> getExistingFoodTypes() {
        if (foods.getValue() == null) return new ArrayList<String>(0);
        return foods.getValue()
                .stream()
                .map(Food::getType)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public void insert(Food food) {
        foodRepository.insert(food);
    }

    public void update(Food food) {
        if (food != null) {
            foodRepository.update(food);
        }
    }

    public void delete(Food food) {
        if (food != null) {
            foodRepository.delete(food);
        }
    }

    public void insert(AddCalories addCalories) {
        addCaloriesRepository.insert(addCalories);}
}