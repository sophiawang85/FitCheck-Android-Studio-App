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
import androidx.navigation.Navigation;

import com.example.Fit_Check.model.DateWithFoodList;
import com.example.Fit_Check.R;
import com.example.Fit_Check.ui.dashboard.DashboardViewModel;
import com.example.Fit_Check.ui.home.HomeViewModel;
import com.example.Fit_Check.ui.date.DateFragment;
import java.util.List;

public class FoodListAdapter extends ArrayAdapter<DateWithFoodList> {
    private final Context context;
    private final HomeViewModel homeViewModel;
   //private final DashboardViewModel dashboardViewModel;

    public FoodListAdapter(@NonNull Context context, @NonNull List<DateWithFoodList> objects, @NonNull HomeViewModel homeViewModel) {
        super(context, 0, objects);
        this.context = context;
        this.homeViewModel = homeViewModel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.date_item, parent, false);
        }

        final DateWithFoodList dateWithFoodList = getItem(position);

        ((TextView) convertView.findViewById(R.id.textViewName))
                .setText(dateWithFoodList.date.getName());
        ((TextView) convertView.findViewById(R.id.textViewFoods))
                .setText(String.valueOf(dateWithFoodList.foodList == null ? 0 : dateWithFoodList.foodList.size()));

        ((TextView) convertView.findViewById(R.id.textViewCalories))
                .setText(String.valueOf(dateWithFoodList.foodList == null ? 0 : dateWithFoodList.totalCalories())); //compute the total number of calories and display it

        final ImageButton buttonRemove = convertView.findViewById(R.id.buttonRemove);
        //set position to the tag of remove button, so it can be used to get selected items in button click listener
        buttonRemove.setTag(position);
        convertView.setTag(position);

        setupViewItemClickListener(convertView, position);
        setupDeleteButtonClickListener(buttonRemove);
        return convertView;
    }

    private void setupDeleteButtonClickListener(ImageButton buttonRemove) {
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = (int) v.getTag();
                DateWithFoodList selectedShoppingGroup = getItem(clickedPosition);

                if (selectedShoppingGroup != null) {
                    new AlertDialog.Builder(context).setTitle("Confirm Deletion")
                            .setMessage(String.format("Are you sure you want to delete this date, %s?",
                                    selectedShoppingGroup.date.getName()))
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                            //    No need to delete merchandise manually because have set up Cascade Deletion for Food,
                            //    see entity annotation for class Food
                                homeViewModel.delete(selectedShoppingGroup.date);

                                //dashboardViewModel.

                    }).setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

    }

    private void setupViewItemClickListener(View convertView, int position) {
        convertView.setOnClickListener(v -> {
            DateWithFoodList selectedDate = getItem(position);
            if (selectedDate != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedDate", selectedDate); // Ensure it's passed
                //navController.navigate(R.id.FoodListFragment, bundle);
                bundle.putParcelable("selectedDate", getItem(position));
                Navigation.findNavController(v).navigate(R.id.action_edit_shopping_list, bundle);
                //NavDirections action = HomeFragmentDirections.actionEditShoppingList(selectedDate);
                //Navigation.findNavController(v).navigate(action);
            }
        });
    }
}
