package com.aca.amm;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.util.UtilImage;

public class InfoLabbaikActivity extends ControlNormalActivity {

	private Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(layout.activity_info_labbaik);

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
		getMenuInflater().inflate(R.menu.info_travel, menu);
		return true;
	}

	public void btnHomeClick(View v)
	{
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}


	public void btnInsuredRiskClick(View v)
	{
//		ShowDialog(layout.dialog_travel_safe_riskinsured, "Table Premi", id.dlgOK);
        UtilImage.popupImage(this, R.drawable.img_labbaik_premi);

    }

	public void btnFacilityClick(View v)
	{
        UtilImage.popupImage(this, R.drawable.img_labbaik_manfaat);
//        ShowDialog(layout.dialog_travel_safe_facility, "Jaminan & Benefit", id.dlgOK);
	}

	public void btnProcedureClaimClick(View v)
	{
        UtilImage.popupImage(this, R.drawable.img_labbaik_ketentuan);
//        ShowDialog(layout.dialog_travel_safe_procedure, "Ketentuan", id.dlgOK);
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
			Intent i = new Intent(getBaseContext(),  FillLabbaikActivity.class);
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
