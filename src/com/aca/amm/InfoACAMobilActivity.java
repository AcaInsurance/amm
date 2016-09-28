package com.aca.amm;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class InfoACAMobilActivity extends ControlNormalActivity {

private Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_info_acamobil);
		
		
		try{
			Intent i = getIntent();
			b = i.getExtras();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info_acamobil, menu);
		return true;
	}
	
	public void btnHomeClick(View v)
	{
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnRateClick(View v)
	{
		ShowDialog(R.layout.dialog_acamobil_rate, "Rate/Tarif", R.id.dlgOK);
	}
	
	public void btnInsuredRiskClick(View v)
	{
		ShowDialog(R.layout.dialog_acamobil_riskinsured, "Resiko Dijamin", R.id.dlgOK);
	}
	
	public void btnFacilityClick(View v)
	{
		ShowDialog(R.layout.dialog_acamobil_facility, "Fasilitas Tambahan", R.id.dlgOK);
	}
	
	public void btnProcedureClaimClick(View v)
	{
		ShowDialog(R.layout.dialog_acamobil_procedure, "Prosedur Klaim", R.id.dlgOK);
	}
	
	public void btnBuyClick(View v)
	{
		if (Utility.IsAllowAddSPPA(getBaseContext()))
		{
			Intent i = new Intent(getBaseContext(),  FillACAMobilActivity.class);
			i.putExtras(b);
			startActivity(i);
			this.finish();
		}
		else
			Toast.makeText(getBaseContext(), Utility.ERROR_OVER_CAPACITY_SPPA, Toast.LENGTH_LONG).show();
	}
	
	private void ShowDialog(int layoutID, String title, int buttonOKId)
	{
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(layoutID);
		dialog.setTitle(title);

 
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
	
	private void Back(){
		Intent i = new Intent(getBaseContext(),  ChooseProductActivity.class);
		startActivity(i);
		this.finish();
	}

}
