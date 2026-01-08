package com.example.Fit_Check.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DateWithFoodList implements Parcelable {
    @Embedded
    public Date date;

    @Relation(
            parentColumn = "id",
            entityColumn = "dateId"
    )
    public List<Food> foodList; // a list of all the foods in today's list

    public DateWithFoodList() {
    }

    protected DateWithFoodList(Parcel in) {
        date = in.readParcelable(Date.class.getClassLoader());
        foodList = in.createTypedArrayList(Food.CREATOR);
    }

    public static final Creator<DateWithFoodList> CREATOR = new Creator<DateWithFoodList>() {
        @Override
        public DateWithFoodList createFromParcel(Parcel in) {
            return new DateWithFoodList(in);
        }

        @Override
        public DateWithFoodList[] newArray(int size) {
            return new DateWithFoodList[size];
        }
    };
    public double totalCalories()
    {
        double total = 0;

        for (Food i:foodList)
        {
            total += (i.getServings()*i.getCaloriesPerServing());
        }
        return total;
    }

    public double totalCaloriess(int i)
    {
        double total = 0;
        if (i>=foodList.size())return 0;

        return (foodList.get(i).getServings()*foodList.get(i).getCaloriesPerServing() + totalCaloriess(i+1));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(date, flags);
        dest.writeTypedList(foodList);
    }
}