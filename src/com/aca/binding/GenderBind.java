package com.aca.binding;

import android.app.Activity;
import android.widget.Spinner;

import com.aca.dbflow.PerluasanPremi;
import com.aca.dbflow.PerluasanPremi_Table;
import com.aca.util.Var;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marsel on 26/4/2016.
 */
public class GenderBind {

    public static void bind(Activity activity, Spinner spinner) {
        try {
            List<SpinnerItem> arrList = new ArrayList<>();
            arrList.add(new SpinnerItem("1", "Pria"));
            arrList.add(new SpinnerItem("2", "Wanita"));

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
