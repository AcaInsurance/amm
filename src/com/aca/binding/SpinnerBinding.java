package com.aca.binding;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.aca.amm.R;

import java.util.List;

/**
 * Created by Marsel on 26/4/2016.
 */
public class SpinnerBinding {
    public static String getKey (Spinner spinner) {
        SpinnerItem spinnerItem = (SpinnerItem) spinner.getSelectedItem();
        return  spinnerItem.getKey();
    }

    public static void selectWithKey(Spinner spinner, String key) {
        ArrayAdapter<SpinnerItem> adapter = (ArrayAdapter<SpinnerItem>) spinner.getAdapter();

        SpinnerItem spinnerItem;
        for (int i = 0; i < adapter.getCount(); i++) {
            spinnerItem = adapter.getItem(i);

            if(spinnerItem.getKey().equals(key)) {
                spinner.setSelection(i);
            }
        }
    }

    public static String getDescription (Spinner spinner) {
        SpinnerItem spinnerItem = (SpinnerItem) spinner.getSelectedItem();
        return  spinnerItem.getDescription();
    }
    public static void select(Spinner spinner, String item) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int position = adapter.getPosition(item);
        spinner.setSelection(position);
    }

    public static void bindWithKey(Activity activity, Spinner spinner, List<SpinnerItem> arrList) {
        try {
            ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<SpinnerItem>(activity, R.layout.simple_spinner_item, arrList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void bindWithKey(Activity activity, Spinner spinner, List<SpinnerItem> arrList, boolean whiteTextColor) {
        try {
            ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<SpinnerItem>(activity, whiteTextColor ? R.layout.simple_spinner_item_white_text : android.R.layout.simple_spinner_item, arrList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bind(Activity activity, Spinner spinner, List<String> arrList) {
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, arrList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
