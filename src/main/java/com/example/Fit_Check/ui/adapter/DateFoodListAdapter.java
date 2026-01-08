package com.example.Fit_Check.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;

import com.example.Fit_Check.model.DateWithFoodList;
import com.example.Fit_Check.model.Food;
import com.example.Fit_Check.ui.dashboard.DashboardViewModel;
import com.example.Fit_Check.ui.foodlist.FoodListViewModel;
import com.example.Fit_Check.R;

import java.util.ArrayList;
import java.util.List;

public class DateFoodListAdapter extends ArrayAdapter<Food> {
    private final Context context;
    private final FoodListViewModel viewModel;
    private final DateWithFoodList selectedDate;
    //private final LiveData<List<DateWithFoodList>> alldates;
    private final ArrayList<String> foodTypes;

    //private final DashboardViewModel dashboardViewModel;
    public DateFoodListAdapter(@NonNull Context context, @NonNull List<Food> objects, @NonNull FoodListViewModel viewModel, @NonNull DateWithFoodList selectedDate) {
        super(context, 0, objects);
        this.context = context;
        this.viewModel = viewModel;
        //this.alldates = viewModel.
        this.selectedDate = selectedDate;
        this.foodTypes = viewModel.getExistingFoodTypes();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.food_list_item, parent, false);
        }

        final Food food = getItem(position);
        ((TextView) convertView.findViewById(R.id.textViewName)).setText(food.getName());
        ((TextView) convertView.findViewById(R.id.textViewType)).setText(food.getType().toString());
        if(food.getCaloriesPerServing() != null) {
            ((TextView) convertView.findViewById(R.id.textViewCaloriesPerServing)).setText(String.valueOf(food.getCaloriesPerServing()));
        }
        //if(food.getServings() != null) {
            ((TextView) convertView.findViewById(R.id.textViewTotalCalories)).setText(String.valueOf(food.gettotal()));
        //}

        ImageButton buttonRemove = convertView.findViewById(R.id.buttonRemove);
        buttonRemove.setTag(position);
        convertView.setTag(position);

        setupViewItemClickListener(convertView, position);
        setupDeleteButtonClickListener(buttonRemove);

        return convertView;
    }

    private void setupViewItemClickListener(View convertView, int position) {
        convertView.setOnClickListener(v -> {
            Food selectedFood = getItem(position);

            if (selectedFood != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("selectedDate", selectedDate);
                    bundle.putParcelable("selectedFood", selectedFood);
                    bundle.putStringArrayList("foodTypes", foodTypes);
                    Navigation.findNavController(v).navigate(R.id.action_add_food_to_date, bundle);
                    //NavDirections action = ShoppingListFragmentDirections.actionEditMerchandiseInShoppingGroup(selectedDate, selectedFood, foodTypes);
                    //Navigation.findNavController(v).navigate(action);
            }
        });
    }

    private void setupDeleteButtonClickListener(ImageButton buttonRemove) {
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = (int) v.getTag();
                Food selectedFood = getItem(clickedPosition);

                if (selectedFood != null) {
                    new AlertDialog.Builder(context)
                            .setTitle("Confirm Deletion")
                            .setMessage(String.format("Are you sure you want to delete record of eating %s with a total of %.2f calories?",
                                    selectedFood.getName(),
                                    selectedFood.getCaloriesPerServing() * selectedFood.getServings()))
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                viewModel.delete(selectedFood);

                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            }
        });

    }
}
