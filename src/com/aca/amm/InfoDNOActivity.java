package com.aca.amm;

import com.aca.amm.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InfoDNOActivity extends ControlNormalActivity {

private Bundle b;
private String language = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		try{
			Intent i = getIntent();
			b = i.getExtras();
			language = i.getStringExtra("LANGUAGE");
			
			if (language.equalsIgnoreCase("INDONESIA"))
				setContentView(R.layout.activity_info_dno);
			else			
				setContentView(R.layout.activity_info_dno_inggris);			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info_dno, menu);
		return true;
	}
	
	public void btnHomeClick(View v) {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnInsuredRiskClick(View v)
	{
		if (language.equalsIgnoreCase("INDONESIA"))
			ShowDialog(R.layout.dialog_dno_riskinsured, "Resiko Dijamin", R.id.dlgOK);
		else			
			ShowDialog(R.layout.dialog_dno_inggris_riskinsured, "Coverage", R.id.dlgOK);
		

	}
	
	public void btnFacilityClick(View v)
	{
		if (language.equalsIgnoreCase("INDONESIA"))
			ShowDialog(R.layout.dialog_dno_definisi_perusahaan, "Perusahaan yang dijamin", R.id.dlgOK);
		else			
			ShowDialog(R.layout.dialog_dno_inggris_definisi_perusahaan, "Company that can be covered", R.id.dlgOK);
		
	}
	
	public void btnProcedureClaimClick(View v)
	{
		if (language.equalsIgnoreCase("INDONESIA"))
			ShowDialog(R.layout.dialog_dno_dokumen, "Dokumen yang diperlukan", R.id.dlgOK);
		else			
			ShowDialog(R.layout.dialog_dno_inggris_dokumen, " Required Document", R.id.dlgOK);
		
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
	
	
	public void btnBuyClick(View v)
	{
		if (Utility.IsAllowAddSPPA(getBaseContext()))
		{
			Intent i = new Intent(getBaseContext(),  FillDNOActivity.class);
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
