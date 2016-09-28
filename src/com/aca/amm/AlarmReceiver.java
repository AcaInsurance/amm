package com.aca.amm;


import com.aca.database.DBA_MASTER_AGENT;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Element;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	public static String ACTION_ALARM = "com.aca.amm.AlarmReceiver";
	public static String SESSION_LOGOUT = "SESSION_LOGOUT";
	
	public static int REQUEST_CODE = 12345;
	 
		
	 @Override
	 public void onReceive(Context context, Intent intent) {
		   
		  Log.i("Alarm Receiver", "Entered");
		    
		  Bundle bundle = intent.getExtras();
		  String action = bundle.getString("ACTION_ALARM");
		   			 
		  if (action.equals(ACTION_ALARM)) {
			 Log.i("Alarm Receiver", "alarm forsync image entered");
			 Intent inService = new Intent(context,TaskService.class);
			 context.startService(inService);
		  }
		  else if (action.equals(SESSION_LOGOUT)) {
			  Log.i("Alarm Receiver", "sesion logout masuk");
			  try {
				DBA_MASTER_AGENT dba = new DBA_MASTER_AGENT(context);
				dba.open();
				dba.updateStatusLogout();
				dba.close();
//				
//				Intent i = new Intent(context, LoginActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(i);
				
			  }
			  catch (Exception ex) {
				  ex.printStackTrace();
			  }
			 
		 
		  }
		  else{
			 Log.i("Alarm Receiver", "Else loop");
		  }
	 }
	 
	 
	 
	 
}
