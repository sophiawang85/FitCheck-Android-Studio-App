package com.example.Fit_Check.io.date;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.Fit_Check.model.Date;
import com.example.Fit_Check.model.DateWithFoodList;

import java.util.List;

@Dao
public interface DateDao {
    @Insert
    void insert(Date date);

    @Update
    void update(Date date);

    @Delete
    void delete(Date date);

    @Query("SELECT * FROM date ORDER BY name ASC")
    LiveData<List<Date>> getAllDates();

    @Query("SELECT * FROM date ORDER BY name")
    LiveData<List<DateWithFoodList>> getAllDateWithFood();

}
