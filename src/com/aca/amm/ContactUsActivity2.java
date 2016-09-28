package com.aca.amm;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.amm.R;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContactUsActivity2 extends ControlListActivity {

	private ArrayList<HashMap<String, String>> newsList; 

 	private ListView lv;
	
	private Bundle b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news);
		
		b = new Bundle();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
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
	private void setListView (){

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("NAMA", "WISMA ASIA LANTAI 10, 12-15");
		map.put("ALAMAT", "Jln. Letjen S.Parman Kav 79 Jakarta 11420");
		map.put("TELP", "(021)56998288, 56998222");
		map.put("FAX", "(021)5638029");
		
		newsList.add(map);
		
		map = new HashMap<String, String>();
		map.put("NAMA", "MAL AMBASADOR - RUKO 2 & 3");
		map.put("ALAMAT", "Jln. Prof.Dr.Satrio Jakarta 12940");
		map.put("TELP", "(021)5760608");
		map.put("FAX", "(021)5760607");
		
		newsList.add(map);
		
		
		ListAdapter adapter = new SimpleAdapter(ContactUsActivity2.this
    			,newsList
                ,R.layout.list_item_contact_us
                ,new String[] { "NAMA", "ALAMAT", "TELP", "FAX"}
    			,new int[] { R.id.txtNewsHeadline, R.id.txtNewsContent, R.id.txtNewsDate })
		{
			@Override
			public void setViewText(TextView v, String text) {
				switch (v.getId()) {
					case R.id.txtNewsHeadline:
						super.setViewText(v, text.toUpperCase());
						break;
					default : super.setViewText(v, text);
				}
			
			}
		}; 
		
        setListAdapter(adapter);

        lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				TextView tv = (TextView)arg1.findViewById(R.id.txtNewsHeadline);
				
				Intent i = new Intent(getBaseContext(), ReadNewsActivity.class);
				b.putString("NEWS_TITLE", tv.getText().toString());
				i.putExtras(b);
				startActivity(i);
				ContactUsActivity2.this.finish();
				
			}
		});
	}
}
