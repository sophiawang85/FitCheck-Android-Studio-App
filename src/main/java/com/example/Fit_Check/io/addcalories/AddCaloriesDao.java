package com.example.Fit_Check.io.addcalories;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.Fit_Check.model.AddCalories;

import java.util.List;

@Dao
public interface AddCaloriesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AddCalories addCalories);

    @Update
    void update(AddCalories addCalories);

    @Delete
    void delete(AddCalories addCalories);

    @Query("SELECT * FROM add_calories")
    LiveData<List<AddCalories>> getAddcalories();

    @Query("SELECT * FROM add_calories WHERE dateId = :dateId")
    LiveData<List<AddCalories>> getAddcaloriesForDate(int dateId);

    @Query("SELECT * FROM add_calories WHERE foodId = :foodId")
    LiveData<List<AddCalories>> getAddcaloriesForFood(int foodId);
}
