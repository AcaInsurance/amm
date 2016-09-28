package com.aca.amm;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


class RetrieveCustomerById extends AsyncTask<String, Void, Void>{
	private Activity ctx ;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 

    private static String SOAP_ACTION = "http://tempuri.org/GetCustomerById";     
    private static String METHOD_NAME= "GetCustomerById";
    
    private String idKtp;
    
    private ArrayList<HashMap<String, String>> custList;

	private ProgressDialog progressBar;
    
	public InterfaceCustomer customerInterface = null;
	
	public RetrieveCustomerById (Activity ctx, String idKtp ){
		this.ctx = ctx;
		this.idKtp = idKtp;
		
	}
	
	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		
//		Log.d("context", );
//
		progressBar = new ProgressDialog(ctx);
		progressBar.setCancelable(false);
		progressBar.setMessage("Please wait ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();
		
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		envelope.implicitTypes = true;
		envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
		androidHttpTransport = new HttpTransportSE(URL);

	}
	
	@Override
	protected Void doInBackground(String... params) {
		
		error = false;
		
		try{
			SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
	        SoapObject tableRow = null;                     // Contains row of table
	        SoapObject responseBody = null;					// Contains XML content of dataset
			

			requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
	        requestretrive.addProperty(Utility.GetPropertyInfo("param", idKtp, String.class));
	        
	    	envelope.setOutputSoapObject(requestretrive);
	    	envelope.bodyOut = requestretrive;
    		androidHttpTransport.call(SOAP_ACTION, envelope);  
    			
            responseBody = (SoapObject) envelope.getResponse();
            responseBody = (SoapObject) responseBody.getProperty(1);
            
            if(responseBody.getPropertyCount() > 0) {
            	table = (SoapObject) responseBody.getProperty(0);
            	tableRow = (SoapObject) table.getProperty(0);
            	custList = new ArrayList<HashMap<String, String>>(); 
                 
                HashMap<String, String> map = new HashMap<String, String>(); 
                map.put("CUSTOMER_NO", tableRow.getPropertySafelyAsString("CUSTOMER_NO").toString()); 
	            map.put("CUSTOMER_NAME", tableRow.getPropertySafelyAsString("CUSTOMER_NAME").toString()); 
	            map.put("CUSTOMER_GENDER", tableRow.getPropertySafelyAsString("CUSTOMER_GENDER").toString());
	            map.put("CUSTOMER_STATUS", tableRow.getPropertySafelyAsString("CUSTOMER_STATUS").toString());
	            map.put("CUSTOMER_PHONE_NO", tableRow.getPropertySafelyAsString("CUSTOMER_PHONE_NO").toString());
	            map.put("CUSTOMER_EMAIL", tableRow.getPropertySafelyAsString("CUSTOMER_EMAIL").toString());
	            map.put("CUSTOMER_DOB", tableRow.getPropertySafelyAsString("CUSTOMER_DOB").toString()); 
	            map.put("CUSTOMER_POB", tableRow.getPropertySafelyAsString("CUSTOMER_POB").toString());

	            map.put("CUSTOMER_ID", tableRow.getPropertySafelyAsString("CUSTOMER_ID").toString()); 
	            map.put("CUSTOMER_ADDRESS", tableRow.getPropertySafelyAsString("CUSTOMER_ADDRESS").toString());
	            map.put("CUSTOMER_CITY", tableRow.getPropertySafelyAsString("CUSTOMER_CITY").toString());
	            map.put("CUSTOMER_CITY_CODE", tableRow.getPropertySafelyAsString("CUSTOMER_CITY_CODE").toString());
	            map.put("CUSTOMER_KODE_POS", tableRow.getPropertySafelyAsString("CUSTOMER_KODE_POS").toString());
	            
	            map.put("CUSTOMER_OCCUPATION", tableRow.getPropertySafelyAsString("CUSTOMER_OCCUPATION").toString());
	            map.put("CUSTOMER_BUSINESS", tableRow.getPropertySafelyAsString("CUSTOMER_BUSINESS").toString());
	            map.put("CUSTOMER_RANGE", tableRow.getPropertySafelyAsString("CUSTOMER_RANGE").toString());
	             
	            map.put("CRE_BY", tableRow.getPropertySafelyAsString("CRE_BY").toString());
                custList.add(map);
        	}
            else {
            	error = true;
            	errorMessage = "Data customer tidak ditemukan";
            			
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
		progressBar.hide();
		progressBar.dismiss();
		try{	
			if(error){
				Toast.makeText(ctx, errorMessage, Toast.LENGTH_SHORT).show();
			}
			
			customerInterface.getCustomerList(custList);
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public ArrayList<HashMap<String, String>> getCustomerData (){
		return custList;
	}
	
}