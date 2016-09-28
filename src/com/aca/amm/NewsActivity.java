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
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewsActivity extends ControlListActivity {

	private ArrayList<HashMap<String, String>> newsList; 
	
	private static String NAMESPACE = "http://tempuri.org/";     
    private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/GetListNews";     
    private static String METHOD_NAME = "GetListNews";

    private boolean error = false;
    private String errorMessage = "";
    
    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;

    private ProgressDialog progressBar;

	private ListView lv;
	
	private Bundle b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news);
		
		
		
		b = new Bundle();
		
		requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
		
		RetriveNewsWS sc = new RetriveNewsWS();
    	sc.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
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
		Back();
	}
	
	private void Back(){
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	private class RetriveNewsWS extends AsyncTask<String, Void, Void>{

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressBar = new ProgressDialog(NewsActivity.this);
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
				
		    	envelope.setOutputSoapObject(requestretrive);
	    		envelope.bodyOut = requestretrive;
	    		androidHttpTransport.call(SOAP_ACTION, envelope);  

	    		// This step: get file XML
	            responseBody = (SoapObject) envelope.getResponse();
	            // remove information XML,only retrieved results that returned
	            responseBody = (SoapObject) responseBody.getProperty(1);
	            // get information XMl of tables that is returned
	            
	            if (responseBody.getPropertyCount() == 0 ) {
	            	error= true;
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
		            map.put("NEWS_TITLE", tableRow.getPropertySafelyAsString("NEWS_TITLE").toString()); 
		            map.put("NEWS_DATE", Utility.getFormatDate(tableRow.getPropertySafelyAsString("NEWS_DATE").toString().substring(0,10))); 
		            
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
					Utility.showCustomDialogInformation(NewsActivity.this, "Informasi", 
							errorMessage);		
				}else{

					ListAdapter adapter = new SimpleAdapter(NewsActivity.this
	            			,newsList
	                        ,R.layout.list_item_news
	                        ,new String[] { "NEWS_TITLE", "NEWS_DATE"}
	            			,new int[] { R.id.txtNewsHeadline, R.id.txtNewsDate })
					{
						@Override
						public void setViewText(TextView v, String text) {
							switch (v.getId()) {
								case R.id.txtNewsHeadline:
									super.setViewText(v, text.toUpperCase());
									break;
								default : super.setViewText(v, text);
							}
						
						}
					}; 
					
		            setListAdapter(adapter);
		
		            lv = getListView();
		            lv.setOnItemClickListener(new OnItemClickListener() {

		    			@Override
		    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		    				
		    				TextView tv = (TextView)arg1.findViewById(R.id.txtNewsHeadline);
		    				
		    				Intent i = new Intent(getBaseContext(), ReadNewsActivity.class);
		    				b.putString("NEWS_TITLE", tv.getText().toString());
		    				i.putExtras(b);
		    				startActivity(i);
		    				NewsActivity.this.finish();
		    				
		    			}
		    		});
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
}
