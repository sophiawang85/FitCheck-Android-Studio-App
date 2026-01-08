package com.example.Fit_Check.ui.formatter.doubletextedit;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DoubleTextWatcher implements TextWatcher {
    private final EditText editText;

    public DoubleTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (s == null || s.toString().trim().isEmpty()) {
            //Integer.parseInt(s.toString());// Handle valid input
            return;
        } //else {
            //editText.setText("");// Handle empty input (backspace pressed)
        //}
        try {
            Double.parseDouble(s.toString());
        } catch (NumberFormatException e) {
            editText.removeTextChangedListener(this); // Temporarily remove listener
            editText.setText(""); // Clear invalid input
            editText.setSelection(0); // Reset cursor position
            editText.addTextChangedListener(this); // Re-add listener
        }
        /*
        try {
            Double.parseDouble(s.toString());
        } catch (final NumberFormatException e) {
            editText.setText("");
        }*/
    }
}
