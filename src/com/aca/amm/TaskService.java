package com.aca.amm;


import com.aca.database.DBA_PRODUCT_MAIN;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TaskService extends IntentService {
	 
	private String tag = "TaskService";
	
	public TaskService() {
		  super("TaskService");
		  // TODO Auto-generated constructor stub
		  Log.i("TaskService","Service running");
	 }
		 

	@Override
	protected void onHandleIntent(Intent intent) {
		ConnectionDetector cDetector = new ConnectionDetector(getApplicationContext());
		boolean isConnect =  cDetector.isConnectingToInternet();
		
		if (!isConnect) {
			Log.i(tag, "No Internet Connection");
			return;
		}
			
		Log.i(tag,"Sync Photo is Running");
		
	    DBA_PRODUCT_MAIN dba = null ;
	     
		try {
		  dba =  new DBA_PRODUCT_MAIN(getApplicationContext());
	      dba.open();
	      
	      Cursor c = dba.getRowsUnPaid();
	      int count = 0;
	      
	      
	      if (c.moveToFirst()) {
		      do {
		    	  if (!TextUtils.isEmpty(c.getString(23))) {
						if (c.getString(23).equals("FALSE")) {
							Log.d("id row", "id = " + c.getString(0));
							count ++ ;
							new SyncImage(getApplicationContext(), true , Long.parseLong(c.getString(0))).execute();
							
						}
		    	  }
		      }
		      while (c.moveToNext());
			}
	      if (count == 0) {
	    	  this.stopSelf();
	    	  
	    	  Log.i(tag, "no photo to sync");
	    	  
	    	  Intent in = new Intent("com.aca.amm.TaskService");
		      in.putExtra("RESULTCODE", Activity.RESULT_OK);
		      LocalBroadcastManager.getInstance(this).sendBroadcast(in);
	      }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (dba != null)
				dba.close();
		}
	}


	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Log.i ("TASK SERVICE", "SERVICE STOP");
	}
	
	
}
