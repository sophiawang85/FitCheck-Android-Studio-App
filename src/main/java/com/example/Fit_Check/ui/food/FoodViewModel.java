package com.example.Fit_Check.ui.food;

import androidx.lifecycle.ViewModel;

import com.example.Fit_Check.model.Food;
import com.example.Fit_Check.model.Date;
import com.example.Fit_Check.model.AddCalories;

public class FoodViewModel extends ViewModel {
    public Food createFood(String name, String type, int servings, Double targetPrice, int dateId) {
        //add validation
        return new Food(name, targetPrice, type, servings, dateId);
    }

    public AddCalories createCal(Date date, Food food, Double purchasePrice, Integer purchasedUnits) {
        final AddCalories addCalories = new AddCalories();
        addCalories.setFoodId(food.getId());
        addCalories.setFoodName(food.getName());
        addCalories.setFoodType(food.getType());
        addCalories.setDateId(date.getId());
        addCalories.setDateName(date.getName());
        addCalories.setCalperserving(food.getCaloriesPerServing());

        addCalories.setCalperserving(purchasePrice);
        addCalories.setServings(purchasedUnits);
        addCalories.setTransactionDate(date.getName());
        return addCalories;
    }
}