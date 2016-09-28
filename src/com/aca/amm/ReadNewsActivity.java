package com.aca.amm;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.amm.R;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

public class ReadNewsActivity extends ControlNormalActivity {

	ArrayList<HashMap<String, String>> newsList; 


	private static String NAMESPACE = "http://tempuri.org/";     
    private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/GetNews";     
    private static String METHOD_NAME = "GetNews";
    
    private boolean error = false;
    private String errorMessage = "";
    
    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;

    private ProgressDialog progressBar;

    private Bundle b;
    
    private String newsTitle;
    
    private TextView news_title;
    private EditText news_content;
    private WebView  news_content_html;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_read_news);
		
		requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
		
		try {
			Intent i = getIntent();
			b = i.getExtras();
			newsTitle = b.getString("NEWS_TITLE");
		}
		catch(Exception e){	
			e.printStackTrace();
		}
		
		initControl();
		
    	
		RetriveNewsContentWS sc = new RetriveNewsContentWS();
    	sc.execute(newsTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.read_news, menu);
		return true;
	}
	
	private void initControl (){
		news_content = (EditText)findViewById(R.id.txtNewsContent);
		news_title = (TextView)findViewById(R.id.txtNewsTitle);
	}
	
	public void btnBackClick(View v) {
		Back();
	}
	
	public void btnHomeClick(View v){
		Home();
	}
	
	private void Home() {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void Back() {
		Intent i = new Intent(getBaseContext(),  NewsActivity.class);
		startActivity(i);
		this.finish();
	}
	
	private class RetriveNewsContentWS extends AsyncTask<String, Void, Void>{

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			
			progressBar = new ProgressDialog(ReadNewsActivity.this);
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.show();
			
			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
			envelope.implicitTypes = true;
	    	envelope.dotNet = true;	//used only if we use the webservic e from a dot net file (asmx)
			androidHttpTransport = new HttpTransportSE(URL);
		}
		
		@Override
		protected Void doInBackground(String... params) {

			error = false;
			
			try{
				
				SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
		        SoapObject tableRow = null;                     // Contains row of table
		        SoapObject responseBody = null;					// Contains XML content of dataset
				
	    		requestretrive.addProperty(Utility.GetPropertyInfo("newsTitle", newsTitle, String.class));
	    		
		        envelope.setOutputSoapObject(requestretrive);
	    		envelope.bodyOut = requestretrive;
	    		androidHttpTransport.call(SOAP_ACTION, envelope);  

	    		// This step: get file XML
	            responseBody = (SoapObject) envelope.getResponse();
	            // remove information XML,only retrieved results that returned
	            responseBody = (SoapObject) responseBody.getProperty(1);
	            // get information XMl of tables that is returned
	            if (responseBody.getPropertyCount() == 0) {
	            	error = true;
	            	errorMessage = "Data tidak dapat ditemukan";
	            	return null;	   
	            }
	            
	            table = (SoapObject) responseBody.getProperty(0);
	            //Get information each row in table,0 is first row
	            
	            newsList = new ArrayList<HashMap<String, String>>(); 
	            
	            int iTotalDataFromWebService = table.getPropertyCount();
	            for(int i = 0; i < iTotalDataFromWebService; i++)
	            {
	            	tableRow = (SoapObject) table.getProperty(i);
		            HashMap<String, String> map = new HashMap<String, String>(); 
		            map.put("NEWS_CONTENT", tableRow.getPropertySafelyAsString("NEWS_CONTENT").toString()); 
		            newsList.add(map);
	            }
	            
			}	
			catch (Exception e) {
        		error = true;
        		e.printStackTrace();	        		
				errorMessage = new MasterExceptionClass(e).getException();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			
			try{
				progressBar.hide();
				progressBar.dismiss();
				
				if(error){
					Utility.showCustomDialogInformation(ReadNewsActivity.this, "Informasi", 
							errorMessage);		
				}else{
					news_content.setText(newsList.get(0).get("NEWS_CONTENT").toString());
					news_title.setText(newsTitle);
					
//					news_content_html.loadData(newsList.get(0).get("NEWS_CONTENT_HTML").toString(), "text/html", "UTF-8");
					
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
}
