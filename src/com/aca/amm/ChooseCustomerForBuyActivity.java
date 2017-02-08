package com.aca.amm;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.aca.amm.R;
import com.aca.database.DBA_MASTER_AGENT;
import com.aca.util.Var;

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
import android.database.Cursor;
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

public class ChooseCustomerForBuyActivity extends ControlListActivity {

	private Bundle b;
	private String PRODUCT_TYPE;
	private ListView lv;

	private ArrayList<HashMap<String, String>> customerList; 

	private static String NAMESPACE = "http://tempuri.org/";     
    private static String URL = Utility.getURL();      
    private static String SOAP_ACTION = "http://tempuri.org/GetListCustomer";     
    private static String METHOD_NAME = "GetListCustomer";

    private boolean error = false;
    String errorMessage;
    
    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;

    private ProgressDialog progressBar;
    private RetriveCustomerWS sc;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_choose_customer_for_buy);
		setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
		
		b = getIntent().getExtras();
		PRODUCT_TYPE = b.getString("PRODUCT_TYPE");
		
		//sc = new RetriveCustomerWS();
    	//sc.execute("");
    	
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
        	// handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            
            Log.d("SEARCH", query);
            sc = new RetriveCustomerWS();
            sc.execute(query);
        }
    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
	    {
	    	case R.id.menu_search:
	    		onSearchRequested();
	    		break;
	    }
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.choose_customer_for_buy, menu);
	    
	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//	    searchManager.setOnCancelListener(new OnCancelListener() {
//			
//			@Override
//			public void onCancel() {
//				Log.d("Cancel", "Cancel");
//				sc = new RetriveCustomerWS();
//				sc.execute("");
//			}
//		});
//	    
//	    searchManager.setOnDismissListener(new OnDismissListener() {
//			
//			@Override
//			public void onDismiss() {
//				Log.d("Dismiss", "Dismiss");
//				sc = new RetriveCustomerWS();
//				sc.execute("");
//			}
//		});
	    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    MenuItem menuItem = menu.findItem(R.id.menu_search);
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
	    searchView.setIconified(false);
	    
	    menuItem.setOnActionExpandListener(new OnActionExpandListener() {
	        @Override
	        public boolean onMenuItemActionCollapse(MenuItem item) {
	            // Do something when collapsed
	        	Log.d("Collapse", "Collapse");
	        	sc = new RetriveCustomerWS();
	        	sc.execute("");
	            return true;  // Return true to collapse action view
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
		Intent i = PrevActivity(PRODUCT_TYPE);
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	private Intent PrevActivity(String act)
	{
		//NOTE : Kalau pakai switch hanya support di java 7
		Intent i = null;
		try{
			if(act.equalsIgnoreCase("OTOMATE")){
				i = new Intent(getBaseContext(),  FillOtomateActivity.class);
			}
			else if(act.equalsIgnoreCase("TRAVELDOM")){
				i = new Intent(getBaseContext(),  FillTravelDomActivity.class);
			}
			else if(act.equalsIgnoreCase("TRAVELSAFE")){
				i = new Intent(getBaseContext(),  FillTravelActivity.class);
			}
			else if(act.equalsIgnoreCase("TOKO")){
				i = new Intent(getBaseContext(),  FillTokoActivity.class);
			}
			else if(act.equalsIgnoreCase("MEDISAFE")){
				i = new Intent(getBaseContext(),  FillMedisafe.class);
			}
			else if(act.equalsIgnoreCase("ASRI")){			
				i = new Intent(getBaseContext(),  FillAsriActivity.class);
			}
			else if(act.equalsIgnoreCase("ASRISYARIAH")){
				i = new Intent(getBaseContext(),  FillAsriSyariahActivity.class);
			}
			else if(act.equalsIgnoreCase("OTOMATESYARIAH")){				
				i = new Intent(getBaseContext(),  FillOtomateSyariahActivity.class);
			}
			else if(act.equalsIgnoreCase("EXECUTIVESAFE")){
				i = new Intent(getBaseContext(),  FillExecutiveActivity.class);
			}
			else if(act.equalsIgnoreCase("PAAMANAH")){
				i = new Intent(getBaseContext(),  FillPAAmanahActivity.class);
			}
			else if(act.equalsIgnoreCase("ACAMOBIL")){
				i = new Intent(getBaseContext(),  FillACAMobilActivity.class);
			}
			else if(act.equalsIgnoreCase("CARGO")){
				i = new Intent(getBaseContext(),  FillCargoActivity.class);
			}
			else if(act.equalsIgnoreCase("WELLWOMAN")){
				i = new Intent(getBaseContext(),  FillWellWomanActivity.class);
			}
			else if(act.equalsIgnoreCase("DNO")){
				i = new Intent(getBaseContext(),  FillDNOActivity.class);
			}

			else if(act.equalsIgnoreCase("KONVENSIONAL")){
				i = new Intent(getBaseContext(),  FillKonvensionalActivity.class);
			}
			else if(act.equalsIgnoreCase(Var.LABBAIK)){
				i = new Intent(getBaseContext(),  FillLabbaikActivity.class);
			}

		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		return i;
		
	}
	
	
	private class RetriveCustomerWS extends AsyncTask<String, Void, Void>{

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			
			
			progressBar = new ProgressDialog(ChooseCustomerForBuyActivity.this);
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
			DBA_MASTER_AGENT dba = null;
			Cursor c = null;
			
			try{
				
				dba = new DBA_MASTER_AGENT(getListView().getContext());
				dba.open();
				
				c = dba.getRow();
				c.moveToFirst();
				
				SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
		        SoapObject tableRow = null;                     // Contains row of table
		        SoapObject responseBody = null;					// Contains XML content of dataset
				
		        PropertyInfo param = new PropertyInfo();
		        param.setName("param");         
		        param.setValue(params[0]);         
		        param.setType(String.class);         
		        requestretrive.addProperty(param);
		       
		        requestretrive.addProperty(Utility.GetPropertyInfo("AgentCode", c.getString(1), String.class));

		    	envelope.setOutputSoapObject(requestretrive);
	    		envelope.bodyOut = requestretrive;
	    		 

	    		androidHttpTransport.call(SOAP_ACTION, envelope);  

	            responseBody = (SoapObject) envelope.getResponse();
	            responseBody = (SoapObject) responseBody.getProperty(1);
	            
	            if (responseBody.getPropertyCount() == 0) {
	            	error = true;
	            	errorMessage = "Data customer tidak ditemukan";
	            	return null;
	            }
	            table = (SoapObject) responseBody.getProperty(0);
	            
	            customerList = new ArrayList<HashMap<String, String>>(); 
	            
	            int iTotalDataFromWebService = table.getPropertyCount();
	            for(int i = 0; i < iTotalDataFromWebService; i++)
	            {
	            	tableRow = (SoapObject) table.getProperty(i);
	            	
		            HashMap<String, String> map = new HashMap<String, String>(); 
		            map.put("CUSTOMER_NO", tableRow.getPropertySafelyAsString("CUSTOMER_NO").toString()); 
		            map.put("CUSTOMER_NAME", tableRow.getPropertySafelyAsString("CUSTOMER_NAME").toString()); 
		            map.put("CUSTOMER_PHONE_NO", tableRow.getPropertySafelyAsString("CUSTOMER_PHONE_NO").toString()); 
		            map.put("CUSTOMER_ADDRESS", tableRow.getPropertySafelyAsString("CUSTOMER_ADDRESS").toString());
		            customerList.add(map);
	            }				
			}catch (Exception e) {
        		error = true;
        		e.printStackTrace();	        		
				errorMessage = new MasterExceptionClass(e).getException();
			}finally{
				
				if (c != null)
					c.close();
				
				if (dba != null)
					dba.close();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			
			progressBar.hide();
			progressBar.dismiss();
			
			
			if(error){
				Utility.showCustomDialogInformation(ChooseCustomerForBuyActivity.this, "Warning", 
						errorMessage);
			}else{
				
				try{

					ListAdapter adapter = new SimpleAdapter(ChooseCustomerForBuyActivity.this
	            			,customerList
	                        ,R.layout.list_item_customer
	                        ,new String[] { "CUSTOMER_NO", "CUSTOMER_NAME", "CUSTOMER_PHONE_NO"}
	            			,new int[] { R.id.txtCustomerID, R.id.txtCustomerName, R.id.txtCustomerMobileNo })
					{
						@SuppressLint("DefaultLocale")
						@Override
						public void setViewText(TextView v, String text) {
							
							switch (v.getId()) {
								case R.id.txtCustomerName:
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
		    				
		    				
		    				TextView tv1 = (TextView)arg1.findViewById(R.id.txtCustomerID);
		    				TextView tv2 = (TextView)arg1.findViewById(R.id.txtCustomerName);
		    				
		    				b.putString("CUSTOMER_NAME",  tv2.getText().toString().trim());
		    				b.putString("CUSTOMER_NO", tv1.getText().toString().trim());
		    				
		    				Log.d("Selected", tv1.getText().toString());
		    				
		    				Intent i = null;
		    				
		    				if (b.getString("PRODUCT_TYPE").equals("OTOMATE"))
		    					i = new Intent(getBaseContext(), FillOtomateActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("ASRI"))
		    					i = new Intent(getBaseContext(), FillAsriActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("TRAVELSAFE"))
		    					i = new Intent(getBaseContext(), FillTravelActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("TRAVELDOM"))
		    					i = new Intent(getBaseContext(), FillTravelDomActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("MEDISAFE"))
		    					i = new Intent(getBaseContext(), FillMedisafe.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("EXECUTIVESAFE"))
		    					i = new Intent(getBaseContext(), FillExecutiveActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("TOKO"))
		    					i = new Intent(getBaseContext(), FillTokoActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("OTOMATESYARIAH"))
		    					i = new Intent(getBaseContext(), FillOtomateSyariahActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("ASRISYARIAH"))
		    					i = new Intent(getBaseContext(), FillAsriSyariahActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("PAAMANAH"))
		    					i = new Intent(getBaseContext(), FillPAAmanahActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("ACAMOBIL"))
		    					i = new Intent(getBaseContext(), FillACAMobilActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("CARGO"))
		    					i = new Intent(getBaseContext(), FillCargoActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("WELLWOMAN"))
		    					i = new Intent(getBaseContext(), FillWellWomanActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("DNO"))
		    					i = new Intent(getBaseContext(), FillDNOActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals("KONVENSIONAL"))
		    					i = new Intent(getBaseContext(), FillKonvensionalActivity.class);
		    				else if (b.getString("PRODUCT_TYPE").equals(Var.LABBAIK))
		    					i = new Intent(getBaseContext(), FillLabbaikActivity.class);
		    				
		    				
		    				i.putExtras(b);
		    				startActivity(i);
		    				ChooseCustomerForBuyActivity.this.finish();
		    			}
		    		});
				}catch (Exception e) {
	        		error = true;
	        		e.printStackTrace();	        		
					errorMessage = new MasterExceptionClass(e).getException();
				}
			}
		}
	}

}
