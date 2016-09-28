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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CurrentNews extends AsyncTask<String, Void, Void>{

	private ArrayList<HashMap<String, String>> newsList; 
	
	private static String NAMESPACE = "http://tempuri.org/";     
    private static String URL = Utility.getURL();      
    private static String SOAP_ACTION = "http://tempuri.org/GetCurrentNews";     
    private static String METHOD_NAME = "GetCurrentNews";

    private boolean error = false;
    private String errorMessage = "";
    
    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;

    private Context ctx; 
	
	
	public CurrentNews (Context ctx)
	{
		this.ctx = ctx;
	}
	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		
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
			
	        requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
	        
	    	envelope.setOutputSoapObject(requestretrive);
    		envelope.bodyOut = requestretrive;
    		androidHttpTransport.call(SOAP_ACTION, envelope);  

            responseBody = (SoapObject) envelope.getResponse();
            responseBody = (SoapObject) responseBody.getProperty(1);
            
            if(responseBody.getPropertyCount() > 0) {
            	table = (SoapObject) responseBody.getProperty(0);
	            newsList = new ArrayList<HashMap<String, String>>(); 
	            
	            int iTotalDataFromWebService = table.getPropertyCount();
	            for(int i = 0; i < iTotalDataFromWebService; i++)
	            {
	            	tableRow = (SoapObject) table.getProperty(i);
		            HashMap<String, String> map = new HashMap<String, String>(); 
		            map.put("NEWS_TITLE", tableRow.getPropertySafelyAsString("NEWS_TITLE").toString()); 
		            map.put("NEWS_CONTENT", tableRow.getPropertySafelyAsString("NEWS_CONTENT").toString()); 
		            map.put("NEWS_DATE", Utility.getFormatDate(tableRow.getPropertySafelyAsString("NEWS_DATE").toString().substring(0,10))); 
		            
		            newsList.add(map);
	            }
            }
            else  {
            	error = true;
            	errorMessage = "Tidak dapat load data";
            }
		}
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}
        
        finally
        {}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {

		super.onPostExecute(result);
		
		try{
			if(error){
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);
			}else{
					;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public ArrayList<HashMap<String, String>> getCurrentNews () {
		return newsList;
	}
	
}
