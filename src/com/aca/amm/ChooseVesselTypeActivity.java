package com.aca.amm;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.amm.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchManager.OnCancelListener;
import android.app.SearchManager.OnDismissListener;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.MenuItem.OnActionExpandListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseVesselTypeActivity extends ControlListActivity {

	private Bundle b;
	private ListView lv;

	private ArrayList<HashMap<String, String>> vesselTypeList; 

	private static String NAMESPACE = "http://tempuri.org/";     
    private static String URL = Utility.getURL();      
    private static String SOAP_ACTION = "http://tempuri.org/GetVesselType";     
    private static String METHOD_NAME = "GetVesselType";

    private boolean error = false;
    String errorMessage;
    
    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;

    private ProgressDialog progressBar;
    private RetriveVesselTypeWS sc;
    private String VesselFlag = "";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_choose_vessel_type);
		
		setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
		
		b = getIntent().getExtras();
		VesselFlag = getIntent().getStringExtra("VesselFlag");
				
		sc = new RetriveVesselTypeWS();
    	sc.execute("");
		
    	handleIntent(getIntent());
		
		// gets the activity's default ActionBar
        ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
	}

	@Override
	public void onNewIntent(Intent intent) {
		
	    super.onNewIntent(intent);      
	    setIntent(intent);
	    handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
        
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("SEARCH", query);
            sc = new RetriveVesselTypeWS();
            sc.execute(query);
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.choose_vessel_type, menu);
	    
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    searchManager.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel() {
				Log.d("Cancel", "Cancel");
				sc = new RetriveVesselTypeWS();
				sc.execute("");
			}
		});
	    
	    searchManager.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				sc = new RetriveVesselTypeWS();
				sc.execute("");
			}
		});
	    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    MenuItem menuItem = menu.findItem(R.id.menu_search);
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
	    searchView.setIconified(false);
	    
	    menuItem.setOnActionExpandListener(new OnActionExpandListener() {
	        @Override
	        public boolean onMenuItemActionCollapse(MenuItem item) {
	        	sc = new RetriveVesselTypeWS();
	        	sc.execute("");
	            return true; 
	        }

	        @Override
	        public boolean onMenuItemActionExpand(MenuItem item) {
	            // Do something when expanded
	            return true;  // Return true to expand action view
	        }
	    });
	    
	    
	    
	    return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putAll(b);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		b = savedInstanceState;
	}
	
	public void btnHomeClick(View v)
	{
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void Back(){
		Intent i = new Intent(getBaseContext(),  FillCargoActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	private class RetriveVesselTypeWS extends AsyncTask<String, Void, Void>{

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			
			progressBar = new ProgressDialog(ChooseVesselTypeActivity.this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Please wait ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.show();
			
			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
			envelope.implicitTypes = true;
	    	envelope.dotNet = true;	//used only if we use the webservic e from a dot net file (asmx)
			androidHttpTransport = new HttpTransportSE(URL);
			
			requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
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

	            responseBody = (SoapObject) envelope.getResponse();
	            responseBody = (SoapObject) responseBody.getProperty(1);
	            table = (SoapObject) responseBody.getProperty(0);
	            
	            vesselTypeList = new ArrayList<HashMap<String, String>>(); 
	            
	            int iTotalDataFromWebService = table.getPropertyCount();
	            for(int i = 0; i < iTotalDataFromWebService; i++)
	            {
	            	tableRow = (SoapObject) table.getProperty(i);
	            	
		            HashMap<String, String> map = new HashMap<String, String>(); 
		            map.put("Type", tableRow.getPropertySafelyAsString("type").toString()); 
		            vesselTypeList.add(map);
	            }				
			}catch(ArrayIndexOutOfBoundsException e){
				e.printStackTrace();
				error = true;
				errorMessage = e.getMessage();
			}
			catch(ConnectTimeoutException e){
				e.printStackTrace();
				error = true;
				errorMessage = "ConnectTimeoutException " +  e.getMessage();
			}
			catch(SocketTimeoutException e){
				e.printStackTrace();
				error = true;
				errorMessage = "SocketTimeoutException " +  e.getMessage();
			}
			catch(Exception e){
				e.printStackTrace();
				error = true;
				errorMessage = e.getMessage();
			}finally{

			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			
			try
			{
				progressBar.hide();
				progressBar.dismiss();
			}
			catch(Exception ex)
			{
				
			}
			
			if(error){
				Toast.makeText(getBaseContext(), "Retrive vessel type failed due to : " + errorMessage, Toast.LENGTH_SHORT).show();
			}else{
				
				try{

					ListAdapter adapter = new SimpleAdapter(ChooseVesselTypeActivity.this
	            			,vesselTypeList
	                        ,R.layout.list_item_vessel_type
	                        ,new String[] { "Type"}
	            			,new int[] { R.id.txtVesselType})
					{
						@SuppressLint("DefaultLocale")
						@Override
						public void setViewText(TextView v, String text) {
							
							switch (v.getId()) {
								case R.id.txtVesselType:
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
		    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    					long arg3) {
		    				
		    				
		    				TextView tv1 = (TextView)arg1.findViewById(R.id.txtVesselType);
		    				
		    				if (VesselFlag.equals("NORMAL"))
		    					b.putString("VESSEL_TYPE",  tv1.getText().toString().trim());
		    				else
		    					b.putString("PRINTED_VESSEL_TYPE",  tv1.getText().toString().trim());
		    					
		    				Log.d("Selected", tv1.getText().toString());
		    				
		    				Intent i = null;
		    				
		    				i = new Intent(getBaseContext(), FillCargoActivity.class);
		    				
		    				i.putExtras(b);
		    				startActivity(i);
		    				ChooseVesselTypeActivity.this.finish();
		    			}
		    		});
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
}
