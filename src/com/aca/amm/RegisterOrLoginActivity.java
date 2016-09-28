package com.aca.amm;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class RegisterOrLoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_or_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_or_login, menu);
		return true;
	}
	
	public void btnRegisterClick (View v) {
		startActivity(new Intent(this, RegisterActivity.class).putExtra("PREVIOUS", "REGISTERORLOGIN"));
		finish();
	}

	public void btnLoginClick (View v) {
		startActivity(new Intent(this, LoginActivity.class).putExtra("PREVIOUS", "REGISTERORLOGIN"));
		finish();
	}
	
	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void Back(){
		try{

			Intent i = new Intent(getBaseContext(),  FirstActivity.class);
			
			startActivity(i);
			this.finish();
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
