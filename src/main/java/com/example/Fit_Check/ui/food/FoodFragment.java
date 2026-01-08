package com.example.Fit_Check.ui.food;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.Fit_Check.model.AddCalories;
import com.example.Fit_Check.model.DateWithFoodList;
import com.example.Fit_Check.ui.formatter.inttextedit.IntInputFilter;
import com.example.Fit_Check.ui.formatter.inttextedit.IntTextWatcher;
import com.example.Fit_Check.R;
import com.example.Fit_Check.model.Food;
import com.example.Fit_Check.ui.formatter.doubletextedit.DoubleInputFilter;
import com.example.Fit_Check.ui.formatter.doubletextedit.DoubleTextWatcher;
import com.example.Fit_Check.ui.foodlist.FoodListViewModel;
import com.example.Fit_Check.ui.foodlist.FoodListViewModelFactory;

import org.apache.commons.lang3.StringUtils;

public class FoodFragment extends Fragment {

    private FoodListViewModel foodListViewModel;
    private FoodViewModel viewModel;
    private DateWithFoodList dateWithFoodList;
    private Food updateFood;
    private EditText editTextName;
    private AutoCompleteTextView editTextFoodType;
    private EditText editTextcalsperserving;
    //private EditText editTextcalsperserving;
    private EditText editTextservings;

    public static FoodFragment newInstance() {
        return new FoodFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_addfood, container, false);
        this.viewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        this.dateWithFoodList = getSelectedDate();
        this.updateFood = getSelectedFood();

        final FoodListViewModelFactory foodListViewModelFactory = new FoodListViewModelFactory(requireContext());
        this.foodListViewModel = new ViewModelProvider(requireActivity(), foodListViewModelFactory).get(FoodListViewModel.class);

        this.editTextName = rootView.findViewById(R.id.editTextName);
        this.editTextFoodType = rootView.findViewById(R.id.editTextFoodType);
        //this.editTextActual
        // Price = rootView.findViewById(R.id.editTextActual
        // Price);
        this.editTextcalsperserving = rootView.findViewById(R.id.editTextActualPrice);
        this.editTextservings = rootView.findViewById(R.id.editTextPurchasedUnits);

        if (this.updateFood != null) {
            this.editTextName.setText(updateFood.getName());
            this.editTextFoodType.setText(updateFood.getType());
            if (updateFood.getCaloriesPerServing() != null) {
                this.editTextcalsperserving.setText(String.valueOf(updateFood.getCaloriesPerServing()));
            }
        }

        setAutoCompleteTextView();
        setupTextEditFormatter();
        setupButtonOnClickListener(rootView);

        return rootView;
    }

    private void setAutoCompleteTextView() {
        final String[] merchandiseTypes = getFoodTypes();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, merchandiseTypes);
        editTextFoodType.setAdapter(adapter);
        editTextFoodType.setThreshold(1);
    }

    private void setupTextEditFormatter() {
        final InputFilter[] doubleInputFilters = {new DoubleInputFilter()};
        final InputFilter[] intInputFilters = {new IntInputFilter()};

        this.editTextcalsperserving.setFilters(doubleInputFilters);
        //this.editTextcalsperserving.setFilters(doubleInputFilters);

        this.editTextcalsperserving.addTextChangedListener(new DoubleTextWatcher(this.editTextcalsperserving));
        //this.editTextcalsperserving.addTextChangedListener(new DoubleTextWatcher(this.editTextcalsperserving));

        this.editTextservings.setFilters(intInputFilters);
        this.editTextservings.addTextChangedListener(new IntTextWatcher((this.editTextservings)));
    }

    private void setupButtonOnClickListener (final View rootView) {
        final Button commitButton = rootView.findViewById(R.id.buttonCommit);
        final Button cancelButton = rootView.findViewById(R.id.buttonCancel);

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRequiredFields()) return;

                final Food food = updateFood != null ? updateFood() : createFood();

                final Double purchasePrice = getcal(editTextcalsperserving);
                final Integer purchaseUnit = getInt(editTextservings);
                //if (purchasePrice != null) {
                final AddCalories addCalories = viewModel.createCal(dateWithFoodList.date, food, purchasePrice, purchaseUnit);

                foodListViewModel.insert(addCalories);

                if (addCalories != null && !getActivity().isFinishing()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Calories Recorded")
                            .setMessage(String.format(
                                    "A new calorie item has been recorded at %s for %s, with %.2f calories per serving and %d servings eaten, for a total of %.2f calories.",
                                    addCalories.getDateName(),
                                    addCalories.getFoodName(),
                                    addCalories.getCalperserving(),
                                    addCalories.getServings(),
                                    addCalories.getServings() * addCalories.getCalperserving() // Total calories
                            ))
                            .setPositiveButton("OK", null)
                            .show();
                }
                Navigation.findNavController(v).navigateUp();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    private Food updateFood() {
        //update a existing merchandise
        updateFood.setName(editTextName.getText().toString());
        updateFood.setType(editTextFoodType.getText().toString());
        updateFood.setCaloriesPerServing(getcal(editTextcalsperserving));
        foodListViewModel.update(updateFood);
        return updateFood;
    }

    private Food createFood() {
        //create a new food
        Food food = viewModel.createFood(
                editTextName.getText().toString(),
                editTextFoodType.getText().toString(),
                Integer.parseInt(editTextservings.getText().toString()),
                getcal(editTextcalsperserving),
                dateWithFoodList.date.getId());
        foodListViewModel.insert(food);
        return food;
    }


    private Double getcal(EditText editText) {
        if (StringUtils.isBlank(editText.getText().toString())) {
            return null;
        } else {
            return Double.parseDouble(editText.getText().toString().trim());
        }
    }

    private Integer getInt(EditText editText) {
        if (StringUtils.isBlank(editText.getText().toString())) {
            return null;
        } else {
            return Integer.parseInt(editText.getText().toString().trim());
        }
    }

    private boolean checkRequiredFields () {
        if (StringUtils.isBlank(editTextName.getText().toString())) {
            showAlert("Name");
            return false;
        }
        if (StringUtils.isBlank(editTextFoodType.getText().toString())) {
            showAlert("Food Type");
            return false;
        }
        if (StringUtils.isNoneBlank(editTextcalsperserving.getText().toString())
                && StringUtils.isBlank(editTextservings.getText().toString())) {
            showAlert("Servings Eaten");
            return false;
        }
        return true;
    }

    private void showAlert(String requiredFieldName) {
        new AlertDialog.Builder(getContext()).setTitle("Missing Value")
                .setMessage(String.format("Please enter a value for: %s.", requiredFieldName))
                .setPositiveButton("OK", null)
                .show();
    }

    private DateWithFoodList getSelectedDate() {
        if (getArguments() != null && getArguments().containsKey("selectedDate")) {
            return getArguments().getParcelable("selectedDate");
        } else {
            throw new RuntimeException("Missing date!");
        }
    }

    private Food getSelectedFood() {
        if (getArguments() != null && getArguments().containsKey("selectedMerchandise")) {
            return getArguments().getParcelable("selectedMerchandise");
        } else {
            return null;
        }
    }

    private String[] getFoodTypes() {
        if (getArguments() != null && getArguments().containsKey("merchandiseTypes")) {
            return getArguments().getStringArray("merchandiseTypes");
        } else {
            return new String[0];
        }
    }
}