package com.aca.binding;

import android.app.Activity;
import android.widget.Spinner;

import com.aca.dbflow.PaketOtomate;
import com.aca.dbflow.PaketOtomate_Table;
import com.aca.dbflow.StandardField;
import com.aca.dbflow.StandardField_Table;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marsel on 26/4/2016.
 */
public class EqBind {

    public static void bind(Activity activity, Spinner spinner, String kodeProduct) {
        try {
            List<StandardField> standardFieldList = new Select().from(StandardField.class)
                    .innerJoin(PaketOtomate.class)
                    .on(StandardField_Table.FieldCodeDt.eq(PaketOtomate_Table.Eq))
                    .where(PaketOtomate_Table.KodeProduct.eq(kodeProduct))
                    .queryList();

            List<SpinnerItem> arrList = new ArrayList<>();
            SpinnerItem spinnerItem;
            String key, value;

            for (StandardField s : standardFieldList) {
                key = s.Value;
                value = s.FieldNameDt;
                spinnerItem = new SpinnerItem(key, value);
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


    public static void select(Spinner spinner, String id) {
        SpinnerBinding.selectWithKey(spinner, id);
    }


}
