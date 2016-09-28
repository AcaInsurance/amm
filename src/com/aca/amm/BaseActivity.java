package com.aca.amm;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BaseActivity extends Activity {

	 public static final long DISCONNECT_TIMEOUT = 1000 * 60 * 20; // 5 min = 5 * 60 * 1000 ms

	    private Handler disconnectHandler = new Handler(){
	        public void handleMessage(Message msg) {
	        }
	    };
	    

		protected String TAG = "Base Activity";

	    private Runnable disconnectCallback = new Runnable() {
	        @Override
	        public void run() {
	            // Perform any required operation on disconnect
	        	Log.i(TAG , "Disconnect callback - logout");
	        	
	        }
	    };

	    public void resetDisconnectTimer(){
	        disconnectHandler.removeCallbacks(disconnectCallback);
	        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
	    }

	    public void stopDisconnectTimer(){
	        disconnectHandler.removeCallbacks(disconnectCallback);
	    }

	    @Override
	    public void onUserInteraction(){
	        resetDisconnectTimer();
	    }

	    @Override
		protected void onResume() {
	        super.onResume();
	        resetDisconnectTimer();
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	        stopDisconnectTimer();
	    }
}
