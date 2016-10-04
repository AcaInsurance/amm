package com.aca.amm;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aca.amm.R.id;
import com.aca.dal.Scalar;
import com.aca.dbflow.GeneralSetting;
import com.aca.util.UtilImage;
import com.aca.util.Var;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InfoOtomateActivity extends ControlNormalActivity {

    @Bind(id.lblTitle)
    CustomTextViewBold lblTitle;
    @Bind(id.imgLogo)
    ImageView imgLogo;
    private Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_info_otomate);
        ButterKnife.bind(this);


        initLayout();
        try {
            Intent i = getIntent();
            b = i.getExtras();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initLayout() {
        try {
            String kodeProduk = GeneralSetting.getParameterValue(Var.GN_OTOMATE_PICKED);
            String namaProduk = Scalar.getProdukName(this, kodeProduk);
            lblTitle.setText(namaProduk);

            int gambar;
            if (kodeProduk.equalsIgnoreCase(Var.OTOMATE)        ||
                kodeProduk.equalsIgnoreCase(Var.OTOMATE_SMART)  ||
                kodeProduk.equalsIgnoreCase(Var.OTOMATE_SOLITAIRE)) {
                gambar = R.drawable.logootomate;
            }
            else {
                gambar = R.drawable.logootomatesyariah;
            }

            imgLogo.setImageDrawable(getResources().getDrawable(gambar));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info_otomate, menu);
        return true;
    }

    public void btnHomeClick(View v) {
        Intent i = new Intent(getBaseContext(), FirstActivity.class);
        startActivity(i);
        this.finish();
    }

    public void btnRateClick(View v) {
//        ShowDialog(R.layout.dialog_otomate_rate, "Rate/Tarif", id.dlgOK);
        UtilImage.popupImage(this, R.drawable.img_otomate_rate);
    }

    public void btnInsuredRiskClick(View v) {
//        ShowDialog(R.layout.dialog_otomate_riskinsured, "Resiko Dijamin", id.dlgOK);
        UtilImage.popupImage(this, R.drawable.img_otomate_rate);
    }

    public void btnFacilityClick(View v) {
//        ShowDialog(R.layout.dialog_otomate_facility, "Fasilitas Tambahan", id.dlgOK);
        UtilImage.popupImage(this, R.drawable.img_otomate_jaminan);
    }

    public void btnProcedureClaimClick(View v) {
        ShowDialog(R.layout.dialog_otomate_procedure, "Prosedur Klaim", id.dlgOK);
    }

    public void btnBuyClick(View v) {
        if (Utility.IsAllowAddSPPA(getBaseContext())) {
            Intent i = new Intent(getBaseContext(), FillOtomateActivity.class);
            i.putExtras(b);
            startActivity(i);
            this.finish();
        } else
            Toast.makeText(getBaseContext(), Utility.ERROR_OVER_CAPACITY_SPPA, Toast.LENGTH_LONG).show();
    }

    private void ShowDialog(int layoutID, String title, int buttonOKId) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(layoutID);
        dialog.setTitle(title);

        int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
        TextView tv = (TextView) dialog.findViewById(textViewId);
        tv.setTextColor(getResources().getColor(R.color.Black));
        tv.setSingleLine(false);


        Button dialogButton = (Button) dialog.findViewById(buttonOKId);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    private void Back() {
        Intent i = new Intent(getBaseContext(), ChooseProductActivity.class);
        startActivity(i);
        this.finish();
    }

}
