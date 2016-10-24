package com.aca.amm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.aca.amm.R;
import com.aca.amm.R.drawable;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;
import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_MASTER_COUNTRY;
import com.aca.database.DBA_PRODUCT_ASRI;
import com.aca.database.DBA_PRODUCT_ASRI_SYARIAH;
import com.aca.database.DBA_PRODUCT_CARGO;
import com.aca.database.DBA_PRODUCT_CONVENSIONAL;
import com.aca.database.DBA_PRODUCT_DNO;
import com.aca.database.DBA_PRODUCT_EXECUTIVE_SAFE;
import com.aca.database.DBA_PRODUCT_MEDISAFE;
import com.aca.database.DBA_PRODUCT_OTOMATE;
import com.aca.database.DBA_PRODUCT_OTOMATE_SYARIAH;
import com.aca.database.DBA_PRODUCT_PA_AMANAH;
import com.aca.database.DBA_PRODUCT_TOKO;
import com.aca.database.DBA_PRODUCT_TRAVEL_DOM;
import com.aca.database.DBA_PRODUCT_TRAVEL_SAFE;
import com.aca.database.DBA_PRODUCT_WELLWOMAN;
import com.aca.dbflow.PaketOtomate;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;

import static android.R.attr.width;


public class FirstActivity extends ControlNormalActivity {

	ViewPager viewPager;    
	PagerAdapter adapter;
	int[] flag;
	Timer t;
	int viewPagerCurrentItem = 5;
	IdleTimer idltimer;
	private ArrayList<HashMap<String, String>> newsList = null;
	
	private String tag = "First Activity Alarm";

	private AlarmManager alarms;
	private Intent alarmIntent;
	private PendingIntent pIntent;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first);


	    flag = new int[] {R.drawable.ztravelsafe, R.drawable.ztravelsafedom, R.drawable.zpaamanah, 
				R.drawable.zasrisyariah, R.drawable.zotomatesyariah, R.drawable.zotomate, R.drawable.zacamobil,
				R.drawable.zasri, R.drawable.zmedisafe, R.drawable.zwellwoman, R.drawable.zcargo, R.drawable.zdno, R.drawable.zkonvensional
				}; 
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		adapter = new ViewPagerAdapter(this, flag, viewPager);
		
		viewPager.setAdapter(adapter);		
		viewPager.setCurrentItem(viewPagerCurrentItem);

		getIntentData();
		
		t = new Timer();
		slideshowtimer();
	
		if(Utility.cekLogin(getBaseContext()))
			StartSchedule();
		
		if(Utility.cekLogin(getBaseContext()) && cekPopup()) 
		{
			ShowDialogPopupNews();
		}		
		
		clearData();
		
//		GetWSData data = new GetWSData(getBaseContext());
//		data.run();
	}

    private void clearData() {
		try {
			DBA_PRODUCT_CONVENSIONAL dbConven = new DBA_PRODUCT_CONVENSIONAL(this);
			DBA_PRODUCT_CARGO dbCargo = new DBA_PRODUCT_CARGO(this);
			DBA_PRODUCT_DNO dbDNO = new DBA_PRODUCT_DNO(this);
			DBA_PRODUCT_EXECUTIVE_SAFE dbES = new DBA_PRODUCT_EXECUTIVE_SAFE(this);
			DBA_PRODUCT_MEDISAFE dbMedisafe = new DBA_PRODUCT_MEDISAFE(this);
			DBA_PRODUCT_PA_AMANAH dbPA = new DBA_PRODUCT_PA_AMANAH(this);
			DBA_PRODUCT_TOKO dbToko = new DBA_PRODUCT_TOKO(this);
			DBA_PRODUCT_ASRI_SYARIAH dbAsriSyariah = new DBA_PRODUCT_ASRI_SYARIAH(this);
			DBA_PRODUCT_ASRI dbAsri = new DBA_PRODUCT_ASRI(this);
			DBA_PRODUCT_OTOMATE dbOtomate = new DBA_PRODUCT_OTOMATE(this);
			DBA_PRODUCT_OTOMATE_SYARIAH dbOtomateSyariah = new DBA_PRODUCT_OTOMATE_SYARIAH(this);
			DBA_PRODUCT_TRAVEL_DOM dbTravelDom = new DBA_PRODUCT_TRAVEL_DOM(this);
			DBA_PRODUCT_TRAVEL_SAFE dbTravelSafe = new DBA_PRODUCT_TRAVEL_SAFE(this);
			DBA_PRODUCT_WELLWOMAN dbWellWoman = new DBA_PRODUCT_WELLWOMAN(this);
			
			dbConven.clearData();
			dbCargo.clearData();
			dbDNO.clearData();
			dbES.clearData();
			dbMedisafe.clearData();
			dbPA.clearData();
			dbToko.clearData();
			dbAsriSyariah.clearData();
			dbAsri.clearData();
			dbOtomate.clearData();
			dbOtomateSyariah.clearData();
			dbTravelDom.clearData();
			dbTravelSafe.clearData();
			dbWellWoman.clearData();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private void getIntentData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		
		if (intent.getStringExtra("FAIL") == null)
			return;
		
		if (intent.getStringExtra("FAIL").equals("TRUE")) {
			endAplikasi();
		}
		else if (intent.getStringExtra("FAIL").equals("FALSE")){
		      
            SharedPreferences sharedPreferences = this.getSharedPreferences("com.aca.amm", Context.MODE_PRIVATE);
			sharedPreferences
				.edit()
				.putString("SUCCESS_SYNC", "TRUE")
				.apply();
            
//            String messageInit = getResources().getString(R.string.sync_message_not_init);

		}
			
	}

	private void endAplikasi () {
		
		final Dialog dialog = new Dialog(FirstActivity.this);
		dialog.setContentView(R.layout.dialog_information_all);
		dialog.setTitle("Error Sinkronisasi");
		dialog.setCancelable(false);
		
		((TextView)dialog.findViewById(R.id.tvWellWomanInfo))
		.setText("Aplikasi akan ditutup. Silahkan buka kembali aplikasi");
		
		((Button)dialog.findViewById(R.id.dlgOK))
		.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();			
				FirstActivity.this.finish();
			}
		});
		dialog.show();		
	}
	
	public void slideshowtimer()
    {
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
	           runOnUiThread(new Runnable() {
	               public void run() {
	                   viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
	               }
	
	           });
	       }

		}, 0, 5000);
    }
	

	public void StartSchedule() {
		 
		  try {
		   alarms = (AlarmManager) this
		     .getSystemService(Context.ALARM_SERVICE);
		 
		   alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
		   alarmIntent.putExtra("ACTION_ALARM", AlarmReceiver.ACTION_ALARM);
		 
		   pIntent = PendingIntent.getBroadcast(this,
				   AlarmReceiver.REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		   //untuk waktu yang lebih dynamic; sejak device booted
		   
		   alarms.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				   SystemClock.elapsedRealtime() + Utility.getDurationSyncImage(), 
				   Utility.getDurationSyncImage(), pIntent);
		   
		   /*
		   untuk waktu yang spesifik; ex: 9.00
		   
		   alarms.setInexactRepeating(AlarmManager.RTC,
				   System.currentTimeMillis(), 5000, pIntent);
		   */
		   
//		   toast("Alarm Started...");
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
	}
	
	public void cancelAlarm() {
		
		if (alarms != null)			
			alarms.cancel(pIntent);

		  Log.i(tag, "Alarm is cancelled");
	}
	
	@Override
  protected void onResume() {
      super.onResume();
      // Register for the particular broadcast based on ACTION string
      IntentFilter filter = new IntentFilter("com.aca.amm.TaskService");
      LocalBroadcastManager.getInstance(this).registerReceiver(receiverSyncImage, filter);
      // or `registerReceiver(testReceiver, filter)` for a normal broadcast
  }

	
	 private BroadcastReceiver receiverSyncImage = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            int resultCode = intent.getIntExtra("RESULTCODE", RESULT_CANCELED);
	            if (resultCode == RESULT_OK) {
//	            	cancelAlarm();
	            }
	        }
	    };
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first, menu);
		return true;
	}

	private void ShowDialogPopupNews()
	{
		// custom dialog
		DBA_MASTER_AGENT dba = null;
		
		try {
			dba = new DBA_MASTER_AGENT(getBaseContext());
			dba.open();
			
			Cursor c = dba.getRow();
			c.moveToFirst();
			
			if (c.getString(5).equals("0")) {
				try {
					newsList = getCurrentNews(getBaseContext());
					if(newsList != null) {
						final Dialog dialog = new Dialog(this);
						dialog.setContentView(R.layout.dialog_popup_menu);
						dialog.setTitle(newsList.get(0).get("NEWS_TITLE"));
						
						int textViewId = dialog.getContext().getResources().getIdentifier("android:id/title", null, null);
						TextView tv = (TextView) dialog.findViewById(textViewId);
						tv.setTextColor(getResources().getColor(R.color.Black));
						
						((TextView)dialog.findViewById(R.id.tvContentPopup))
						.setText(newsList.get(0).get("NEWS_CONTENT"));
						
						((Button)dialog.findViewById(R.id.dlgOK))
						.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								DBA_MASTER_AGENT dba = null;
								try {
									dba = new DBA_MASTER_AGENT(getBaseContext());
									dba.open();
									dba.updatePopup();
								}
								catch (Exception ex)
								{
									ex.printStackTrace();
								}
								finally
								{
									if (dba != null)
										dba.close();
									dialog.dismiss();
								}
								
														}
						});
						dialog.show();
					}
					
		
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		catch (Exception e) {
			Utility.showCustomDialogInformation(FirstActivity.this, "Informasi", 
					"Gagal mengambil news");
		}
 
		finally
		{
			if (dba != null)
				dba.close();
		}
	}
	
	public void popUpNews () {
		 DBA_MASTER_AGENT dba = null;
		
		try {
			
					
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	public ArrayList<HashMap<String, String>> getCurrentNews (Context ctx) throws InterruptedException{
		
		CurrentNews news = new CurrentNews(ctx);
		boolean error = false;
		try {
			news.execute();
			news.get(10000, TimeUnit.MILLISECONDS);
		} catch (ExecutionException e) {
			e.printStackTrace();
			error = true;
		} catch (TimeoutException e) {
			e.printStackTrace();
			error = true;
		}
	    finally {
	    }

		if (error)
			return null;
		else
			return news.getCurrentNews();
	}

	
	public boolean cekPopup (){
		DBA_MASTER_AGENT dba = null;
		Cursor c = null;
		int flag = 0;
		try{
			dba = new DBA_MASTER_AGENT(getBaseContext());
			dba.open();
			c = dba.getRow();
			c.moveToFirst();
			if(c.getCount() == 0)
				return false ;
			if(c.getString(5).equals("0")) {
				flag = 1;
			}
			else 
				flag = 0;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally {
			if (dba!= null)
				dba.close();
		}
		if(flag == 1)
			return true;
		else 
			return false;
			
	}
	
	public void btnChooseCustomerClick(View v)
	{
		if(!Utility.cekLogin(getBaseContext())) {
			Intent i = new Intent (getBaseContext(), RegisterOrLoginActivity.class);
			Bundle b = new Bundle ();
			b.putString("act", "customer");
			i.putExtras(b);
			startActivity(i);
			this.finish();
		}
		else {
			Bundle b = new Bundle();
			b.putString("FLAG", "CUSTOMER");
			b.putString("ACTION", "NEW");
			
			Intent i = new Intent(getBaseContext(),  ChooseCustomerActivity.class);
			i.putExtras(b);
			startActivity(i);
			this.finish();
		}
	}
	
	public void loginAct () {
		
	}
	public void btnSPPAClick(View v)
	{
		if(!Utility.cekLogin(getBaseContext())) {
			Intent i = new Intent (getBaseContext(), RegisterOrLoginActivity.class);
			Bundle b = new Bundle ();
			b.putString("act", "sppa");
			i.putExtras(b);
			startActivity(i);
			this.finish();
		}
		else {
			Intent i = new Intent(getBaseContext(),  SPPAActivity.class);
			startActivity(i);
			this.finish();
		}
	}
	
	public void btnSyncClick(View v)
	{
		if(!Utility.cekLogin(getBaseContext()))
		{
			Intent i = new Intent (getBaseContext(), RegisterOrLoginActivity.class);
			Bundle b = new Bundle ();
			b.putString("act", "sync");
			i.putExtras(b);
			startActivity(i);
			this.finish();
		}
		else 
		{
			Intent i = new Intent(getBaseContext(),  SyncActivity.class);
			startActivity(i);
			this.finish();
		}
	}
	
	public void btnNewsClick(View v)
	{
		Intent i = new Intent(getBaseContext(),  NewsActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnProductClick(View v)
	{
		if(!Utility.cekLogin(getBaseContext())) {
			Intent i = new Intent (getBaseContext(), RegisterOrLoginActivity.class);
			Bundle b = new Bundle ();
			b.putString("act", "product");
			i.putExtras(b);
			startActivity(i);
			this.finish();
		}else 
		{
			Intent i = new Intent(getBaseContext(),  ChooseProductActivity.class);
			startActivity(i);
			this.finish();
		}
	}
	
	public void  btnMoreClick(View v) {
		Intent i = new Intent(getBaseContext(),  MoreActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnMyProfileClick(View v) {
		if(!Utility.cekLogin(getBaseContext())) {
			Intent i = new Intent (getBaseContext(), RegisterOrLoginActivity.class);
			Bundle b = new Bundle ();
			b.putString("act", "product");
			i.putExtras(b);
			startActivity(i);
			this.finish();
		}else 
		{
			Intent i = new Intent(getBaseContext(),  MyProfileActivity.class);
			startActivity(i);
			this.finish();
		}	
	}
	
	public void  btnLogoutClick(View v) {
	    Utility.ConfirmDialog(FirstActivity.this, "Konfirmasi", "Anda yakin ingin keluar dari aplikasi ini?", "Ya", "Tidak");
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

    	if ((keyCode == KeyEvent.KEYCODE_BACK)) {
    		Utility.ConfirmDialog(FirstActivity.this, "Konfirmasi", "Anda yakin ingin keluar dari aplikasi ini?", "Ya", "Tidak");
    	}
    	return false;
    }

}
