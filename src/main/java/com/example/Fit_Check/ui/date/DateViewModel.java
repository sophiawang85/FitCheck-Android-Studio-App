package com.example.Fit_Check.ui.date;

import androidx.lifecycle.ViewModel;

import com.example.Fit_Check.model.Date;

public class DateViewModel extends ViewModel {
    public DateViewModel() {
    }

    public Date createDate(String name, String description) {
        //add validation
        return new Date(name, description);
    }
}
