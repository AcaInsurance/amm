package com.aca.amm;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class Waiter extends Thread {
	 private static final String TAG=Waiter.class.getName();
	    private long lastUsed;
	    private long period;
	    private boolean stop;
	    private Context context;

	    public Waiter(long period, ControlNormalActivity context)
	    {
	        this.period=period;
	        this.context = context;
	        stop=false;
	        
	        Log.i(TAG, "waiter stard");
	    }

	    public void run()
	    {
	    	Looper.prepare();
	    	
	        long idle=0;
	        this.touch();
	        do
	        {
	            idle=System.currentTimeMillis()-lastUsed;
	            try
	            {
	                Thread.sleep(5000); //check every 5 seconds
	            }
	            catch (InterruptedException e)
	            {
	                Log.d(TAG, "Waiter interrupted!");
	            }
	            Log.d(TAG, "Application is idle for "+idle +" ms");
	            Log.i(TAG, "period= " + period);
	            
	            if(idle > period)
	            {
	                idle=0;
	                //do something here - e.g. call popup or so
	                Log.i(TAG, "Logout is trigger");
	                stop = true;
	            }
	        }
	        while(!stop);
	        Log.d(TAG, "Finishing Waiter thread");
	    }
	    
	    public synchronized boolean isStopped () {
	    	return stop;
	    }

	    public synchronized void touch()
	    {
	        lastUsed=System.currentTimeMillis();
	    }

	    public synchronized void forceInterrupt()
	    {
	        this.interrupt();
	    }


	    public synchronized void setPeriod(long period)
	    {
	        this.period=period;
	    }
}
