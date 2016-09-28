package com.aca.amm;

import com.aca.amm.R;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class PaidSPPAActivity extends ControlNormalActivity {

	private ProgressBar progress;
	
	private class MyWebViewClient extends WebChromeClient {
       
		@Override
		public void onProgressChanged(WebView view, int newProgress) {			
			PaidSPPAActivity.this.setValue(newProgress);
			super.onProgressChanged(view, newProgress);
		}
		
	}
	

	public void setValue(int progress) {
		this.progress.setProgress(progress);		
	}

	public void btnHomeClick(View v)
	{
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
		Intent i = new Intent(getBaseContext(),  SyncActivity.class);
		startActivity(i);
		this.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_paid_sppa);
		
		Intent i = getIntent();
		Bundle b = i.getExtras();
		
		
		progress = (ProgressBar) findViewById(R.id.progressBar);
		progress.setMax(100);

		
		WebView wv = (WebView)findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptEnabled(true); 
		wv.setWebChromeClient(new MyWebViewClient());
		wv.loadUrl("http://www.klikaca.com/mobile/defaultm.aspx?sppano=" + b.getString("sppano") + "&amount=" + String.valueOf(b.getDouble("amount")));
		wv.requestFocus();
		PaidSPPAActivity.this.progress.setProgress(0);
		
		final Activity activity = this;

		
		wv.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            	Utility.showCustomDialogInformation(PaidSPPAActivity.this, "Informasi", 
            			description);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.paid_sppa, menu);
		return true;
	}

}
