package com.aca.amm;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.amm.R.layout;
import com.aca.dbflow.VersionAndroid;
import com.aca.util.UtilDate;
import com.raizlabs.android.dbflow.sql.language.Delete;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class RetrieveLatestVersionApps extends AsyncTask<String, Void, HashMap<String, String>>{
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/GetLatestVersionAndroid";     
    private static String METHOD_NAME= "GetLatestVersionAndroid";
    
    public RetriveLastVersionAppsListener mCallbackListener = null;
    
    public RetrieveLatestVersionApps (Context ctx){
    	this.ctx = ctx;
    }
    
    
	protected void onPreExecute() {

		super.onPreExecute();
		
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		envelope.implicitTypes = true;
    	envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
		androidHttpTransport = new HttpTransportSE(URL);
		requestRetrieve = new SoapObject(NAMESPACE, METHOD_NAME);
	}
	
	@Override
	protected HashMap<String, String> doInBackground(String... params) {
		error = false;
		try{
			SoapObject responseBody = null;					// Contains XML content of dataset
			SoapObject table = null;					// Contains XML content of dataset
			SoapObject tableRow = null;					// Contains XML content of dataset
	        
	    	envelope.setOutputSoapObject(requestRetrieve);
    		androidHttpTransport.call(SOAP_ACTION, envelope);  
    		
            responseBody = (SoapObject) envelope.getResponse();
            responseBody = (SoapObject) responseBody.getProperty(1);
        	
            HashMap<String, String> map = new HashMap<String, String>();

            Delete.table(VersionAndroid.class);
            VersionAndroid versionAndroid;
            if(responseBody.getPropertyCount() > 0) {
            	table = (SoapObject) responseBody.getProperty(0);
            	tableRow = (SoapObject) table.getProperty(0);
            	
            	map.put("Version", tableRow.getPropertySafelyAsString("Version"));
            	map.put("Datetime", tableRow.getPropertySafelyAsString("Datetime"));
            	map.put("Maintenance", tableRow.getPropertySafelyAsString("Maintenance"));

                versionAndroid = new VersionAndroid();
                versionAndroid.Version = Integer.parseInt(map.get("Version"));
                versionAndroid.DateTime = UtilDate.parseUTC(map.get("Datetime")).toDate();
                versionAndroid.save();
            }
            else {
            	error = true;
            	errorMessage = "Data tidak ditemukan";
            	return null;
            }
            
            return map ;
		}	
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}
		return null;
		
	}
	
	
	protected void onPostExecute(HashMap<String, String> map) {
		
		super.onPostExecute(map);
		
		try
		{
			if (error) {
				Toast.makeText(ctx, errorMessage, Toast.LENGTH_SHORT).show();
				mCallbackListener.getLastVersionApps(null, null, null);
			}
			else {
				mCallbackListener.getLastVersionApps(
						map.get("Version"), 
						map.get("Datetime"),
						map.get("Maintenance"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
