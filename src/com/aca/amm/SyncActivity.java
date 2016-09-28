package com.aca.amm;


import com.aca.amm.R;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class SyncActivity extends ControlTabActivity {
	
	// TabSpec Names 
    private static final String INCOMPLETE_SPEC = "Belum Lengkap"; 
    private static final String UNSYNC_SPEC = "Belum Disubmit"; 
    private static final String UNPAID_SPEC = "Belum Dibayar"; 

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sync);
		
		TabHost tabHost = getTabHost(); 
		
        TabSpec incompleteSpec = tabHost.newTabSpec(INCOMPLETE_SPEC); 
        incompleteSpec.setIndicator(INCOMPLETE_SPEC); 
        Intent incompleteIntent = new Intent(this, SyncIncompleteActivity.class); 
        incompleteSpec.setContent(incompleteIntent); 
        
        
        TabSpec unsyncSpec = tabHost.newTabSpec(UNSYNC_SPEC); 
        unsyncSpec.setIndicator(UNSYNC_SPEC); 
        Intent unsyncIntent = new Intent(this, SyncUnsyncActivity.class); 
        unsyncSpec.setContent(unsyncIntent); 
          
        TabSpec unpaidSpec = tabHost.newTabSpec(UNPAID_SPEC); 
        unpaidSpec.setIndicator(UNPAID_SPEC); 
        Intent unpaidIntent = new Intent(this, SyncUnpaidActivity.class); 
        unpaidSpec.setContent(unpaidIntent); 
          
        // Adding all TabSpec to TabHost 
        tabHost.addTab(incompleteSpec);
        tabHost.addTab(unsyncSpec);
        tabHost.addTab(unpaidSpec);
        
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {

            //tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE); //Changing background color of tab

            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); /*for Selected Tab changing text color*/
            tv.setTextColor(Color.WHITE);
        }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sync, menu);
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
		// TODO Auto-generated method stub
		Back();
	}
	
	private void Back(){
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
}
