package com.aca.binding;

import android.app.Activity;
import android.widget.Spinner;

import com.aca.dbflow.PerluasanPremi;
import com.aca.dbflow.PerluasanPremi_Table;
import com.aca.dbflow.StandardField;
import com.aca.dbflow.StandardField_Table;
import com.aca.util.Var;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.key;
import static android.R.attr.value;

/**
 * Created by Marsel on 26/4/2016.
 */
public class LabbaikTujuanBind {

    public static void bind(Activity activity, Spinner spinner) {
        try {

            List<SpinnerItem> arrList = new ArrayList<>();
            List<StandardField> standardFieldList = new Select()
                    .from(StandardField.class)
                    .where(StandardField_Table.FieldCode.eq(Var.LABBAIK))
                    .and(StandardField_Table.IsActive.eq(String.valueOf(true)))
                    .queryList();

            SpinnerItem spinnerItem;
            String key, value;

            for (StandardField standardField: standardFieldList) {
                key = standardField.FieldNameDt;
                value = standardField.Description;
                String description = standardField.Value;
                spinnerItem = new SpinnerItem(key, value, description);
                arrList.add(spinnerItem);
            }


            SpinnerBinding.bindWithKey(activity, spinner, arrList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getID(Spinner spinner) {
        return SpinnerBinding.getKey(spinner);
    }

    public static String getValue(Spinner spinner) {
        return SpinnerBinding.getDescription(spinner);
    }

    public static void select(Spinner spinner, String id) {
        SpinnerBinding.selectWithKey(spinner, id);
    }


}
