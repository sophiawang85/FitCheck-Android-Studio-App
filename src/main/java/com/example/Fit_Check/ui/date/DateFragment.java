package com.example.Fit_Check.ui.date;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.Fit_Check.model.Date;
import com.example.Fit_Check.model.DateWithFoodList;
import com.example.Fit_Check.ui.dashboard.DashboardFragment;
import com.example.Fit_Check.ui.foodlist.FoodListFragment;
import com.example.Fit_Check.ui.home.HomeViewModelFactory;
import com.example.Fit_Check.R;
import com.example.Fit_Check.ui.home.HomeViewModel;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class DateFragment extends Fragment {
    private DateViewModel viewModel;
    private HomeViewModel homeViewModel;
    private DateWithFoodList updateDate;
    private EditText editTextName;
    private EditText editTextDescription;
    private FoodListFragment foodListFragment;
    private DashboardFragment dashboardFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_date, container, false);

        this.viewModel = new ViewModelProvider(this).get(DateViewModel.class);
        final HomeViewModelFactory homeViewModelFactory = new HomeViewModelFactory(requireContext());
        this.homeViewModel = new ViewModelProvider(requireActivity(), homeViewModelFactory).get(HomeViewModel.class);

        this.editTextName = rootView.findViewById(R.id.editTextName);
        this.editTextDescription = rootView.findViewById(R.id.editTextDescription);

        setupCommitButtonClickListener(rootView);
        setupCancelButtonClickListener(rootView);
        //dashboardFragment.
        return rootView;
    }

    private void setupCommitButtonClickListener(final View rootView) {
        Button commitButton = rootView.findViewById(R.id.buttonCommit);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkRequiredFields()) return;
                if (updateDate != null) {
                    updateDate();
                } else {
                    createDate();
                }
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    private boolean checkRequiredFields () {
        if (StringUtils.isBlank(editTextName.getText().toString())) {
            new AlertDialog.Builder(getContext()).setTitle("Missing Value for Date")
                    .setMessage("Please enter a value for: Date.")
                    .setPositiveButton("OK", null)
                    .show();
            return false;
        }
        return true;
    }

    private void updateDate() {
        //update a existing shopping group
        updateDate.date.setName(editTextName.getText().toString());
        updateDate.date.setDescription(editTextDescription.getText().toString());
        homeViewModel.update(updateDate.date);
    }

    private void createDate() {
        //create a new shopping group
        Date date = viewModel.createDate(editTextName.getText().toString(), editTextDescription.getText().toString());
        homeViewModel.insert(date);
        //foodListFragment.alldates.add()
    }

    private void setupCancelButtonClickListener (final View rootView) {
        Button cancelButton = rootView.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("selectedShoppingGroup")) {
            updateDate = getArguments().getParcelable("selectedShoppingGroup");
            this.editTextName.setText(updateDate.date.getName());
            this.editTextDescription.setText(updateDate.date.getDescription());
        } else {
            updateDate = null;
        }
    }
}