package com.aca.util;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.aca.amm.R;

import static com.raizlabs.android.dbflow.config.FlowLog.Level.V;

/**
 * Created by Marsel on 20-Dec-16.
 */

public class UtilView {
    public static void enableView(EditText keterangan, boolean enable){
        try {
            keterangan.setText("");
            keterangan.setEnabled(enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validateEmptyViews(EditText... view){
        try {
            for (EditText v:view) {
                if (v.isEnabled() && TextUtils.isEmpty(v.getText().toString()))
                    return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void disableView( Activity activity, View... view) {
        for (View v: view ) {
            v.setEnabled(false);
            if (v instanceof EditText)
                v.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.edittextdisabled));
        }
    }
}
