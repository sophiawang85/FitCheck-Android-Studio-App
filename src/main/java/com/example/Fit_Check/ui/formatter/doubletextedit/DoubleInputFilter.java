package com.example.Fit_Check.ui.formatter.doubletextedit;

import android.text.InputFilter;
import android.text.Spanned;

public class DoubleInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        final StringBuilder builder = new StringBuilder(dest);
        builder.replace(dstart, dend, source.subSequence(start, end).toString());
        final String newValue = builder.toString();

        try {
            Double.parseDouble(newValue);
        } catch (final NumberFormatException e) {
            return "";
        }

        return null;
    }
}
