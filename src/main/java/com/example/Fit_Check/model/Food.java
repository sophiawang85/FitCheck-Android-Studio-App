package com.example.Fit_Check.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "food",
        foreignKeys = @ForeignKey(entity = Date.class,
        parentColumns = "id",
        childColumns = "dateId",
        onDelete = ForeignKey.CASCADE))
public class Food implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Double caloriesPerServing;
    private String type;
    private int servings;
    private int dateId;
    public Food(String name, Double caloriesPerServing, String type, int servings, int shoppingGroupId) {
        this.name = name;
        this.caloriesPerServing = caloriesPerServing;
        this.type = type;
        this.dateId = shoppingGroupId;
        this.servings = servings;
    }
    public Food() {
        // Default constructor required by Room
    }

    public Double gettotal()
    {
        return this.caloriesPerServing*this.getServings();
    }
    protected Food(Parcel in) {
        id = in.readInt();
        name = in.readString();
        caloriesPerServing = in.readDouble();
        type = in.readString();
        dateId = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Double getCaloriesPerServing() {
        return caloriesPerServing;
    }

    public void setCaloriesPerServing(Double caloriesPerServing) {
        this.caloriesPerServing = caloriesPerServing;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public int getServings(){return servings;}
    public void setServings(int servings) {
        this.servings = servings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(caloriesPerServing);
        dest.writeString(type);
        dest.writeInt(dateId);
        dest.writeInt(servings);
    }
}
