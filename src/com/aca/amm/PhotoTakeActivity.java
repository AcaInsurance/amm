package com.aca.amm;

import com.aca.amm.Utility.Item;
import com.aca.amm.R;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class PhotoTakeActivity extends ControlNormalActivity {

	final Item[] items = {
		    new Item("Camera", android.R.drawable.ic_menu_camera),
		    new Item("Gallery", android.R.drawable.ic_menu_gallery),
		};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo_take);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.photo_take, menu);
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
		Back();
	}
	
	private void Back(){
		Intent i = new Intent(getBaseContext(),  PhotoListActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnSaveClick(View v) {
		
	}
}
