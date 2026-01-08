package com.example.Fit_Check.io;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.Fit_Check.io.addcalories.AddCaloriesDao;
import com.example.Fit_Check.io.date.DateDao;
import com.example.Fit_Check.io.datewithfood.DateWithFoodDao;
import com.example.Fit_Check.io.foodlist.FoodDao;
import com.example.Fit_Check.model.AddCalories;
import com.example.Fit_Check.model.Date;
import com.example.Fit_Check.model.Food;

@Database(entities = {Date.class, Food.class, AddCalories.class}, version = 14)
public abstract class FitCheckDatabase extends RoomDatabase {
    private static volatile FitCheckDatabase instance;
    public abstract DateDao dateDao();
    public abstract FoodDao foodDao();
    public abstract AddCaloriesDao addcalorieDao();
    public abstract DateWithFoodDao dateWithFoodDao();
    public static synchronized FitCheckDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FitCheckDatabase.class, "fitcheck_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}