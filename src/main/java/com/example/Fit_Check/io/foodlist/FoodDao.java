package com.example.Fit_Check.io.foodlist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.Fit_Check.model.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Food food);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM Food WHERE dateId = :groupId")
    LiveData<List<Food>> getFoodForDate(int groupId);

    @Query("SELECT * FROM Food")
    LiveData<List<Food>> getFoods();
}
