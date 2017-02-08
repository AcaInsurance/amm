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
public class PABind {

    public static void bind(Activity activity, Spinner spinner, String kodeProduct) {
        try {
            List<PerluasanPremi> perluasanPremiList = new Select().from(PerluasanPremi.class)
                    .where(PerluasanPremi_Table.Kode_Produk.eq(kodeProduct))
                    .and(PerluasanPremi_Table.Tipe.eq(Var.PA))
                    .orderBy(PerluasanPremi_Table.Amount, true)
                    .queryList();

            List<SpinnerItem> arrList = new ArrayList<>();
            SpinnerItem spinnerItem;
            String key, value, description;

            for (PerluasanPremi p : perluasanPremiList) {
                key = String.valueOf(p.Amount);
                value = p.Amount_Text;
                description = String.valueOf(p.Premi);
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

    public static String getPremi(Spinner spinner) {
        return SpinnerBinding.getDescription(spinner);
    }

    public static void select(Spinner spinner, String id) {
        SpinnerBinding.selectWithKey(spinner, id);
    }


}
