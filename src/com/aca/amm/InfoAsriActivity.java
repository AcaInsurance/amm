package com.aca.amm;

import com.aca.amm.R;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InfoAsriActivity extends ControlNormalActivity {

	private Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_info_asri);

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
		getMenuInflater().inflate(R.menu.info_asri, menu);
		return true;
	}
	
	public void btnHomeClick(View v) {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}

	public void btnInsuredRiskClick(View v) {
		ShowDialog(R.layout.dialog_asri_riskinsured, "Resiko Dijamin", R.id.dlgOK);
	}
	
	public void btnFacilityClick(View v) {
		ShowDialog(R.layout.dialog_asri_facility, "Fasilitas Tambahan", R.id.dlgOK);
	}
	
	public void btnProcedureClaimClick(View v) {
		ShowDialog(R.layout.dialog_asri_procedure, "Prosedur Klaim", R.id.dlgOK);
	}
	
	
	private void ShowDialog(int layoutID, String title, int buttonOKId)
	{
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
	
	public void btnBuyClick(View v) {
		
		if (Utility.IsAllowAddSPPA(getBaseContext()))
		{
			Intent i = new Intent(getBaseContext(),  FillAsriActivity.class);
			i.putExtras(b);
			startActivity(i);
			this.finish();
		}
		else
			Toast.makeText(getBaseContext(), Utility.ERROR_OVER_CAPACITY_SPPA, Toast.LENGTH_LONG).show();
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
