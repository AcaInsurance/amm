package com.aca.amm;

import android.os.Bundle;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.aca.dbflow.GeneralSetting;
import com.aca.util.Var;

public class ChooseProductActivity extends ControlNormalActivity {
    private Dialog dialog;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose_product);

		/****** Navigation Drawer **********/
//		
//		 FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
//			// inflate the custom activity layout
//			LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			View activityView = layoutInflater.inflate(R.layout.activity_choose_product, null,false);
//			// add the custom layout of this activity to frame layout.
//			frameLayout.addView(activityView);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choos_product, menu);
		return true;
	}

	public void btnHomeClick(View v) {
		Home();
	}

	private void Home() {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}

	@Override
	public void onBackPressed()
	{
		Back();
	}

	private void Back() {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}

	public void btnOtomateClick(View v) {
        ShowDialogOtomate("Otomate Product");

	/*	Intent i = new Intent(getBaseContext(),  InfoOtomateActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "OTOMATE");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();*/

	}

	public void btnASRIClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoAsriActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "ASRI");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public void btnTravelClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoTravelActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "TRAVELSAFE");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public void btnTravelDomClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoTravelDomActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "TRAVELDOM");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public void btnMedisafeClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoMedisafeActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "MEDISAFE");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public void btnExecutiveSafeClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoExecutiveActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "EXECUTIVESAFE");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public void btnOfficeClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoTokoActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "TOKO");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public void btnOtomateSyariahClick(View v) {
        ShowDialogOtomateSyariah("Otomate Syariah Product");
/*
        Intent i = new Intent(getBaseContext(),  InfoOtomateSyariahActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "OTOMATESYARIAH");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();*/
	}

	public void btnASRISyariahClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoAsriSyariahActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "ASRISYARIAH");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public void btnPAAmanahClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoPAAmanahActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "PAAMANAH");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public void btnACAMobilClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoACAMobilActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "ACAMOBIL");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}


	public void btnCargoClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoCargoActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "CARGO");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}


	public void btnWellWomanClick(View v) {
		Intent i = new Intent(getBaseContext(),  InfoWellWomanActivity.class);
		Bundle b = new Bundle();
		b.putString("PRODUCT_TYPE", "WELLWOMAN");
		b.putString("PRODUCT_ACTION", "NEW");
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public void btnConvensionalClick(View v) {
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_konvensional_choose_product);
		dialog.setTitle("Choose Product");


		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.Black));


		Button dialogCompre = (Button) dialog.findViewById(R.id.dlgCompre);
		Button dialogTLO = (Button) dialog.findViewById(R.id.dlgTLO);

		// if button is clicked, close the custom dialog
		dialogCompre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),  InfoKonvensionalActivity.class);
				Bundle b = new Bundle();
				b.putString("PRODUCT_TYPE", "KONVENSIONAL");
				b.putString("PRODUCT_ACTION", "NEW");
				b.putString("TIPE_KONVENSIONAL", "COMPREHENSIVE");
				i.putExtras(b);
				startActivity(i);
				ChooseProductActivity.this.finish();

				dialog.dismiss();
			}
		});

		dialogTLO.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),  InfoKonvensionalActivity.class);
				Bundle b = new Bundle();
				b.putString("PRODUCT_TYPE", "KONVENSIONAL");
				b.putString("PRODUCT_ACTION", "NEW");
				b.putString("TIPE_KONVENSIONAL", "TLO");
				i.putExtras(b);
				startActivity(i);
				ChooseProductActivity.this.finish();

				dialog.dismiss();
			}
		});



		dialog.show();
	}

	public void btnDNOClick(View v) {
		ShowDialogDno(R.layout.dialog_dno_choose_language, "Pilih Bahasa");
	}


	private void ShowDialogDno(int layoutID, String title)
	{
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(layoutID);
		dialog.setTitle(title);


		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.Black));


		Button dialogindo= (Button) dialog.findViewById(R.id.dlgIndonesia);
		Button dialoginggris = (Button) dialog.findViewById(R.id.dlgInggris);

		// if button is clicked, close the custom dialog
		dialogindo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),  InfoDNOActivity.class);
				Bundle b = new Bundle();
				b.putString("PRODUCT_TYPE", "DNO");
				b.putString("PRODUCT_ACTION", "NEW");
				b.putString("LANGUAGE", "INDONESIA");
				i.putExtras(b);
				startActivity(i);
				ChooseProductActivity.this.finish();

				dialog.dismiss();
			}
		});


		dialoginggris.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),  InfoDNOActivity.class);
				Bundle b = new Bundle();
				b.putString("PRODUCT_TYPE", "DNO");
				b.putString("PRODUCT_ACTION", "NEW");
				b.putString("LANGUAGE", "ENGLISH");
				i.putExtras(b);
				startActivity(i);
				ChooseProductActivity.this.finish();

				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void ShowDialogOtomate(String title)
	{
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_otomate_choose_product);
		dialog.setTitle(title);

		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.Black));

		Button dlgOtomate = (Button) dialog.findViewById(R.id.dlgOtomate);
		Button dlgOtomateSmart = (Button) dialog.findViewById(R.id.dlgOtomateSmart);
		Button dlgOtomateSolitaire = (Button) dialog.findViewById(R.id.dlgOtomateSolitaire);

        dlgOtomate.setOnClickListener(otomateChooseClickListener(Var.OTOMATE));
        dlgOtomateSmart.setOnClickListener(otomateChooseClickListener(Var.OTOMATE_SMART));
        dlgOtomateSolitaire.setOnClickListener(otomateChooseClickListener(Var.OTOMATE_SOLITAIRE));

		dialog.show();
	}

	private void ShowDialogOtomateSyariah(String title)
	{
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_otomate_syariah_choose_product);
		dialog.setTitle(title);

		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
		TextView tv = (TextView) dialog.findViewById(textViewId);
		tv.setTextColor(getResources().getColor(R.color.Black));

		Button dlgOtomate = (Button) dialog.findViewById(R.id.dlgOtomate);
		Button dlgOtomateSmart = (Button) dialog.findViewById(R.id.dlgOtomateSmart);
		Button dlgOtomateSolitaire = (Button) dialog.findViewById(R.id.dlgOtomateSolitaire);

        dlgOtomate.setOnClickListener(otomateChooseClickListener(Var.OTOMATE_SYARIAH));
        dlgOtomateSmart.setOnClickListener(otomateChooseClickListener(Var.OTOMATE_SYARIAH_SMART));
        dlgOtomateSolitaire.setOnClickListener(otomateChooseClickListener(Var.OTOMATE_SYARIAH_SOLITAIRE));

		dialog.show();
	}

    private OnClickListener otomateChooseClickListener(final String product) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {	Intent i = new Intent(getBaseContext(),  InfoOtomateActivity.class);
                GeneralSetting.insert(Var.GN_OTOMATE_PICKED, product);

                Bundle b = new Bundle();
                b.putString(Var.OTOMATE_PICKED, product);
                b.putString("PRODUCT_TYPE", "OTOMATE");
                b.putString("PRODUCT_ACTION", "NEW");
                i.putExtras(b);
                startActivity(i);
                ChooseProductActivity.this.finish();

                dialog.dismiss();

            }
        };
    }


}
