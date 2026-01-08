package com.example.Fit_Check.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "add_calories",
        foreignKeys = {
                @ForeignKey(entity = Date.class,
                        parentColumns = "id",
                        childColumns = "dateId",
                        onDelete = ForeignKey.CASCADE)
})

public class AddCalories {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    private String transactionDate;
    private Double calperserving;
    private Integer servings;
    @ColumnInfo(index = true) // Improves lookup performance
    private int foodId;
    private String foodName;
    private String foodType;
    private int dateId;
    private String dateName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

   //public Double getTargetPrice() {
       // return targetPrice;
   // }

    //public void setTargetPrice(Double targetPrice) {
      //  this.targetPrice = targetPrice;
    //}

    public Double getCalperserving() {
        return calperserving;
    }

    public void setCalperserving(Double calperserving) {
        this.calperserving = calperserving;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }
}
