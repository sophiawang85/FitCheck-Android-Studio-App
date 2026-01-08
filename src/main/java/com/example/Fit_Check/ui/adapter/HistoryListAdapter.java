package com.example.Fit_Check.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.Fit_Check.model.AddCalories;
import com.example.Fit_Check.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<AddCalories> {
    private static final int NUM_COLUMNS = 5;
    private final Context context;
    private final List<AddCalories> addCalories;
    private final List<Boolean> sortAscendingFlags;

    public HistoryListAdapter(Context context, List<AddCalories> addCalories) {
        super(context, 0, addCalories);
        this.context = context;
        this.addCalories = addCalories;
        sortAscendingFlags = new ArrayList<>(Collections.nCopies(NUM_COLUMNS, true));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_list_food, parent, false);
            holder = new ViewHolder();
            holder.bind(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AddCalories addCalories = this.addCalories.get(position);
        holder.populate(addCalories);
        return convertView;
    }

    public void sortDataByColumn(int column) {
        boolean ascending = sortAscendingFlags.get(column);
        switch (column) {
            case 0:
                sortByFoodName(ascending);
                break;
            case 1:
                sortByFoodType(ascending);
                break;
            case 2:
                sortByDate(ascending);
                break;
            case 3:
                sortByFoodCalories(ascending);
                break;
            case 4:
                sortByServings(ascending);
                break;
        }
        super.notifyDataSetChanged();
        sortAscendingFlags.set(column, !ascending);
    }

    private void sortByDate(boolean ascending) {
        Collections.sort(addCalories, new Comparator<AddCalories>() {
            @Override
            public int compare(AddCalories item1, AddCalories item2) {
                return ascending ? item1.getTransactionDate().compareTo(item2.getTransactionDate()) :
                        item2.getTransactionDate().compareTo(item1.getTransactionDate());
            }
        });
    }

    private void sortByFoodName(boolean ascending) {
        Collections.sort(addCalories, new Comparator<AddCalories>() {
            @Override
            public int compare(AddCalories item1, AddCalories item2) {
                return ascending ? item1.getFoodName().compareTo(item2.getFoodName()) :
                        item2.getFoodName().compareTo(item1.getFoodName());
            }
        });
    }

    private void sortByFoodType(boolean ascending) {
        Collections.sort(addCalories, new Comparator<AddCalories>() {
            @Override
            public int compare(AddCalories item1, AddCalories item2) {
                return ascending ? item1.getFoodType().compareTo(item2.getFoodType()) :
                        item2.getFoodType().compareTo(item1.getFoodType());
            }
        });
    }

    private void sortByFoodCalories(boolean ascending) {
        Collections.sort(addCalories, new Comparator<AddCalories>() {
            @Override
            public int compare(AddCalories item1, AddCalories item2) {
                return ascending ? item1.getCalperserving().compareTo(item2.getCalperserving()) :
                        item2.getCalperserving().compareTo(item1.getCalperserving());
            }
        });
    }

    private void sortByServings(boolean ascending) {
        Collections.sort(addCalories, new Comparator<AddCalories>() {
            @Override
            public int compare(AddCalories item1, AddCalories item2) {
                return ascending ? item1.getServings().compareTo(item2.getServings()) :
                        item2.getServings().compareTo(item1.getServings());
            }
        });
    }

    private static class ViewHolder {
        TextView textViewFoodName;
        TextView textViewFoodType;
        TextView textViewdate;
        TextView textViewcaloriesperserving;
        TextView getTextViewServings;

        public void bind(View convertView) {
            this.textViewFoodName = convertView.findViewById(R.id.textViewFoodName);
            this.textViewFoodType = convertView.findViewById(R.id.textViewFoodType);
            this.textViewdate = convertView.findViewById(R.id.textViewDate);

            this.textViewcaloriesperserving = convertView.findViewById(R.id.textViewCaloriesPerServing);
            this.getTextViewServings = convertView.findViewById(R.id.textViewServingsEaten);
        }

        public void populate(AddCalories addCalories) {
            this.textViewFoodName.setText(addCalories.getFoodName());
            this.textViewFoodType.setText(addCalories.getFoodType());
            this.textViewdate.setText(addCalories.getTransactionDate());
            this.textViewcaloriesperserving.setText(String.valueOf(addCalories.getCalperserving()));
            this.getTextViewServings.setText(String.valueOf(addCalories.getServings()));
        }
    }
}
