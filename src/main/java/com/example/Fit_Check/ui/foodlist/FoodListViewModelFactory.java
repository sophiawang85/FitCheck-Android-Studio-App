package com.example.Fit_Check.ui.foodlist;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FoodListViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    public FoodListViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FoodListViewModel.class)) {
            return (T) new FoodListViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
