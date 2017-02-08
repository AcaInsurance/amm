package com.aca.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.aca.HelperClass.TouchImageView;
import com.aca.amm.R;

/**
 * Created by marsel on 3/10/2016.
 */
public class UtilImage {

    public static Dialog popupImage(final Context context, final int drawable) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_touch_image);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        final TouchImageView touchImageView = (TouchImageView) dialog.findViewById(R.id.imgPhoto);
        final ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);

        touchImageView.setImageDrawable(context.getResources().getDrawable(drawable));

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        return dialog;
    }

}
