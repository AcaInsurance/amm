package com.aca.amm;


import java.util.Locale;

import com.aca.database.DBA_MASTER_AGENT;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class ControlListActivity extends ListActivity {

	  private static final String TAG=ControlListActivity.class.getName();
	  private int SESSION_TIME = Utility.getDurationLogoutSession();

	    @Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			Log.i("control activity", "on create session Logout");
		}
	    
		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			
			Log.i ("Control activity", "on pause is triggered");
			SharedPreferences sharedPreferences = this.getSharedPreferences("com.aca.amm", Context.MODE_PRIVATE);
			sharedPreferences
				.edit()
				.putLong("SESSION", System.currentTimeMillis())
				.apply();
		}

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			Locale locale = new Locale("en");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
			      getBaseContext().getResources().getDisplayMetrics());
			

			Log.i ("Control activity", "on resume is triggered");
			
			SharedPreferences sharedPreferences = this.getSharedPreferences("com.aca.amm", Context.MODE_PRIVATE);
			long sessionBefore =  sharedPreferences.getLong("SESSION", 0);
			long sessionNow = System.currentTimeMillis();
			
			Log.i ("session shared preference", sessionNow + "");
			Log.i ("session shared preference", sessionBefore + "");
			
			
			if ((sessionNow - sessionBefore) > (SESSION_TIME)) {
				Log.i("Session timeout", "logout is triggered");
				
				DBA_MASTER_AGENT dba = new DBA_MASTER_AGENT(ControlListActivity.this);
				dba.open();
				dba.updateStatusLogout();
				dba.close();
				
				Intent i = new Intent(ControlListActivity.this, LoginActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				
			}
		}
		
		
}
