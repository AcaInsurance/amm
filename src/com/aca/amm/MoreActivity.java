package com.aca.amm;

import com.aca.amm.R;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class MoreActivity extends ControlNormalActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_more);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.more, menu);
		return true;
	}
	
	public void btnHomeClick(View v) {
		Home();
	}
	
	private void Home(){
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Back();
	}
	
	private void Back(){
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnACAShopClick(View v) {
		Uri uriUrl = Uri.parse("http://www.acashop.com/");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		startActivity(launchBrowser);  

	}
	
	public void  btnMitraACAClick(View v) {
		Uri uriUrl = Uri.parse("http://www.aca.co.id/mitraca/default.aspx");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		startActivity(launchBrowser);  
	}
	
	public void  btnKlikACAClick(View v) {
		Uri uriUrl = Uri.parse("http://www.klikaca.com");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		startActivity(launchBrowser);  
	}

	public void btnContactUs (View v){
		Intent i = new Intent (getBaseContext(), ContactUsActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void  btnPolicyWording(View v) {
		Uri uriUrl = Uri.parse("http://www.aca.co.id/acaonlinewording");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		startActivity(launchBrowser);  
	}


}
