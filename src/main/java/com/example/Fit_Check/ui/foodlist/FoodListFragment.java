package com.example.Fit_Check.ui.foodlist;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Fit_Check.model.Date;
import com.example.Fit_Check.model.DateWithFoodList;
import com.example.Fit_Check.R;
import com.example.Fit_Check.model.Food;
import com.example.Fit_Check.ui.adapter.DateFoodListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FoodListFragment extends Fragment {
    private final static String TITLE = "Date: %s";
    private FoodListViewModel viewModel;
    private DateFoodListAdapter adapter;
    private DateWithFoodList selectedDate;
    //public ArrayList<DateWithFoodList> alldates;
    public static FoodListFragment newInstance() {
        return new FoodListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //alldates = new ArrayList<>();
        /*Bundle bundle = new Bundle();
        bundle.putParcelable("selectedDate", selectedDate); // Ensure it's passed
        navController.navigate(R.id.FoodListFragment, bundle);*/
        FoodListFragmentArgs args = FoodListFragmentArgs.fromBundle(getArguments());
        this.selectedDate = args.getSelectedDate();
        //this.selectedDate = getSelectedDate();

        final FoodListViewModelFactory foodListViewModelFactory = new FoodListViewModelFactory(requireContext());
        this.viewModel = new ViewModelProvider(requireActivity(), foodListViewModelFactory).get(FoodListViewModel.class);

        final View rootView = inflater.inflate(R.layout.fragment_foods_list, container, false);
        TextView label = rootView.findViewById(R.id.textViewDate);
        label.setText(String.format(TITLE, selectedDate.date.getName()));

        //set ListView header and adapter
        View headerView = inflater.inflate(R.layout.food_list_item_header, null);
        ListView listView = rootView.findViewById(R.id.foods_list_view);
        listView.addHeaderView(headerView);

        adapter = new DateFoodListAdapter(getContext(), new ArrayList<>(), this.viewModel, this.selectedDate);
        listView.setAdapter(adapter);

        viewModel.getFoods().observe(getViewLifecycleOwner(), foods -> {
            adapter.clear();
            List<Food> foodList = foods.stream()
                    .filter(m -> m.getDateId() == selectedDate.date.getId())
                    .collect(Collectors.toList());
            adapter.addAll(foodList);
        });

        setUpEventListener(rootView);
        return rootView;
    }


    private void setUpEventListener(View rootView) {
        Button addButton = rootView.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedDate", selectedDate);
                bundle.putStringArrayList("foodTypes", viewModel.getExistingFoodTypes());
                Navigation.findNavController(v).navigate(R.id.action_add_food_to_date, bundle);
                //alldates.add(selectedDate);
            }
        });
    }

    private DateWithFoodList getSelectedDate() {
        //ShoppingListFragmentArgs is not generated, so let's use the following method to get object passed by Safe Args
        if (getArguments() != null && getArguments().containsKey("selectedDate")) {
            return getArguments().getParcelable("selectedDate");
        } else {
            throw new RuntimeException("Missing Date!");
        }
    }
}
