package com.example.Fit_Check.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.Fit_Check.io.date.DateRepository;
import com.example.Fit_Check.model.Date;
import com.example.Fit_Check.model.DateWithFoodList;
import com.example.Fit_Check.ui.foodlist.FoodListFragment;

import java.util.List;

import lombok.NonNull;

public class HomeViewModel extends ViewModel {
    private DateRepository dateRepository;
    private FoodListFragment foodListFragment;
    //private AddCaloriesRepository caloriesRepository;
    private LiveData<List<DateWithFoodList>> dateWithFoodList;

    //private Add
    public HomeViewModel(@NonNull Context context) {
        this.dateRepository = new DateRepository(context);
        this.dateWithFoodList = dateRepository.getDatewithFoodList();
    }

    public LiveData<List<DateWithFoodList>> getDates() {
        return this.dateWithFoodList;
    }

    public void insert(Date date) {
        dateRepository.insert(date);
    }

    public void update(Date date) {
        if (date != null) {
            dateRepository.update(date);
        }
    }

    public void delete(Date date) {
        if (date != null) {
            dateRepository.delete(date);
            //foodListFragment.alldates.remove(dateWithFoodList);
        }
    }
}