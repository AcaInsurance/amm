package com.aca.binding;

import android.app.Activity;
import android.media.audiofx.AudioEffect;
import android.widget.Spinner;

import com.aca.dbflow.SppaMain;
import com.aca.dbflow.StandardField;
import com.aca.dbflow.StandardField_Table;
import com.aca.dbflow.SubProductPlan;
import com.aca.dbflow.SubProductPlan_Table;
import com.aca.util.Var;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Marsel on 26/4/2016.
 */
public class LabbaikPlanBind {

    public static void bind( Activity activity, Spinner spinner, Spinner spTujuan ) {
        try {

            String subProductCode = ((SpinnerItem)spTujuan.getSelectedItem()).getKey();
            List<SubProductPlan> subProductPlanList = new Select().from(SubProductPlan.class)
                    .where(SubProductPlan_Table.SubProductCode.eq(subProductCode))
                    .orderBy(SubProductPlan_Table.OrderNo, true)
                    .queryList();

            List<SpinnerItem> arrList = new ArrayList<>();
            String key, value, desc;

            for (SubProductPlan subProductPlan: subProductPlanList) {
                key = subProductPlan.PlanCode;
                value = subProductPlan.Description;
                desc = subProductPlan.OrderNo;
              /*  StringTokenizer tokenizer = new StringTokenizer (value, "(");
                value = tokenizer.nextToken();*/
                arrList.add(new SpinnerItem(key, value, desc));
            }

            SpinnerBinding.bindWithKey(activity, spinner, arrList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getID(Spinner spinner) {
        return SpinnerBinding.getKey(spinner);
    }

    public static String getOrder(Spinner spinner) {
        return SpinnerBinding.getDescription(spinner);
    }

    public static void select(Spinner spinner, String id) {
        SpinnerBinding.selectWithKey(spinner, id);
    }


}
