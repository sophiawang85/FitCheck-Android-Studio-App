package com.example.Fit_Check.io.datewithfood;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.Fit_Check.model.DateWithFoodList;

import java.util.List;

@Dao
public interface DateWithFoodDao {
    @Transaction
    @Query("SELECT * FROM Date")
    LiveData<List<DateWithFoodList>> getDateWithFood();

    @Query("SELECT * FROM date ORDER BY name ASC")
    LiveData<List<DateWithFoodList>> getalldates();
}
