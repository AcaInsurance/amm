package com.aca.amm;

import android.app.Application;
import android.util.Log;

public class ControlApplication extends Application {

	private static final String TAG=ControlApplication.class.getName();
    private Waiter waiter;  //Thread which controls idle time

    // only lazy initializations here!
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(TAG, "Starting application"+this.toString());
//        waiter=new Waiter(10*1000, ControlApplication.this); //15 mins
//        waiter.start();
    }

    public void touch()
    {
        waiter.touch();
    }
    
    
    
    
    
}
