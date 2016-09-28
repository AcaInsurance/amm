package com.aca.amm;

import java.util.ArrayList;
import java.util.HashMap;

import com.aca.amm.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ContactUsActivity extends ControlListActivity{

	private ArrayList<HashMap<String, String>> contList; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contact_us);
		
		contList = new ArrayList<HashMap<String,String>>();
		setListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_us, menu);
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
		Intent i = new Intent(getBaseContext(),  MoreActivity.class);
		startActivity(i);
		this.finish();
	}
	private void setListView (){

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("NAMA", "WISMA ASIA LANTAI 10, 12-15");
		map.put("ALAMAT", "Jln. Letjen S.Parman Kav 79 Jakarta 11420");
		map.put("TELP", "(021)56998288, 56998222");
		map.put("FAX", "(021)5638029");
		
		contList.add(map);
		
		map = new HashMap<String, String>();
		map.put("NAMA", "MAL AMBASADOR - RUKO 2 & 3");
		map.put("ALAMAT", "Jln. Prof.Dr.Satrio Jakarta 12940");
		map.put("TELP", "(021)5760608");
		map.put("FAX", "(021)5760607");
		
		contList.add(map);
		
		
		ListAdapter adapter = new SimpleAdapter(ContactUsActivity.this
    			,contList
                ,R.layout.list_item_contact_us
                ,new String[] { "NAMA", "ALAMAT", "TELP", "FAX"}
    			,new int[] { R.id.txtNama, R.id.txtAlamat, R.id.txtPhone, R.id.txtFax})
		{
			@SuppressLint("DefaultLocale")
			@Override
			public void setViewText(TextView v, String text) {
				switch (v.getId()) {
					case R.id.txtNama:
						super.setViewText(v, text.toUpperCase());
						break;
					default : super.setViewText(v, text);
				}
			
			}
		}; 
		
        setListAdapter(adapter);
	}


}
