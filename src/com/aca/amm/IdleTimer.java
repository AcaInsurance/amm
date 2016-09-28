package com.aca.amm;

import java.util.Timer;
import java.util.TimerTask;

public class IdleTimer {
	
	    private Boolean isTimerRunning;
	    private IIdleCallback idleCallback;
	    private int maxIdleTime;
	    private Timer timer;

	    

	    public IdleTimer(int maxInactivityTime, IIdleCallback iIdleCallback) {
			// TODO Auto-generated constructor stub
	    	 maxIdleTime = maxInactivityTime;
		     idleCallback = iIdleCallback;
		}

		/*
	     * creates new timer with idleTimer params and schedules a task
	     */
	    public void startIdleTimer()
	    {
	        timer = new Timer();            
	        timer.schedule(new TimerTask() {

	            @Override
	            public void run() {             
	                idleCallback.inactivityDetected();
	            }
	        }, maxIdleTime);
	        isTimerRunning = true;
	    }

	    /*
	     * schedules new idle timer, call this to reset timer
	     */
	    public void restartIdleTimer()
	    {
	        stopIdleTimer();
	        startIdleTimer();
	    }

	    /*
	     * stops idle timer, canceling all scheduled tasks in it
	     */
	    public void stopIdleTimer()
	    {
	        timer.cancel();
	        isTimerRunning = false;
	    }

	    /*
	     * check current state of timer
	     * @return boolean isTimerRunning
	     */
	    public boolean checkIsTimerRunning()
	    {
	        return isTimerRunning;
	    }
	

	protected interface IIdleCallback
	{
	    public void inactivityDetected();
	}
}
