package com.aca.amm;

import java.util.ArrayList;
import java.util.HashMap;

import com.aca.amm.R;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;
import com.aca.database.DBA_PRODUCT_PHOTO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class PhotoListActivity extends ControlNormalActivity {

	
	private ArrayList <HashMap<String, String>> photoList;
	private ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo_list);
		
		photoList = new ArrayList <HashMap<String, String>>();
		lv = (ListView)findViewById(R.id.list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.photo_list, menu);
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
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	private void LoadPhotoList(long product_main_id) {
		
		photoList.clear();
		
		DBA_PRODUCT_PHOTO dba = null;
		Cursor c = null;
		
		try {
			dba = new DBA_PRODUCT_PHOTO(this);
			dba.open();
			
			c = dba.getRowsByProductMainID(product_main_id);
			
			while(c.moveToNext()) {
				HashMap <String, String> map = new HashMap <String, String>();
				
				map.put(DBA_PRODUCT_PHOTO.PHOTO_ROWID, String.valueOf(c.getLong(0)));
				map.put(DBA_PRODUCT_PHOTO.PHOTO_FILENAME, c.getString(2));
				map.put(DBA_PRODUCT_PHOTO.PHOTO_DESCRIPTION, c.getString(3));
				map.put(DBA_PRODUCT_PHOTO.PHOTO_DATE_TAKEN, c.getString(4));
				
				photoList.add(map);
			}
			
			ImageAdapter adapter = new ImageAdapter(this, photoList);
			lv.setAdapter(adapter);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			if (dba != null)
				dba.close();
		}
		
	}
}
