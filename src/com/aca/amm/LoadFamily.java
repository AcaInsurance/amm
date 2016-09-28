package com.aca.amm;

import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LoadFamily extends AsyncTask<String, Void, Void>{
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/LoadFamily";     
    private static String METHOD_NAME= "LoadFamily";
    
    private String SPPA_NO ;
    private String Famkey;
    
    private NumberFormat nf;
    
    private ArrayList<HashMap<String, String>> familyList;
    
    public LoadFamily (Context ctx, String SPPA_NO, String FamilyKey){
    	this.ctx = ctx;
    	this.SPPA_NO = SPPA_NO;
    	
    	Famkey = FamilyKey; 
    	
    	nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
    }
    
    
	protected void onPreExecute() {

		super.onPreExecute();
		
		progressBar = new ProgressDialog(ctx);
		progressBar.setCancelable(false);
		progressBar.setMessage("Please wait ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();
		
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		envelope.implicitTypes = true;
    	envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)
		androidHttpTransport = new HttpTransportSE(URL);
		requestRetrieve = new SoapObject(NAMESPACE, METHOD_NAME);
	}
	
	@Override
	protected Void doInBackground(String... params) {
		error = false;
		try{
			
			SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
	        SoapObject tableRow = null;                     // Contains row of table
	        SoapObject responseBody = null;					// Contains XML content of dataset
	        
	        requestRetrieve.addProperty(Utility.GetPropertyInfo("SPPANo", SPPA_NO, String.class));
	        requestRetrieve.addProperty(Utility.GetPropertyInfo("FamilyKey", Famkey, String.class));
	        
	    	envelope.setOutputSoapObject(requestRetrieve);
    		envelope.bodyOut = requestRetrieve;
    		androidHttpTransport.call(SOAP_ACTION, envelope);  
    		
            responseBody = (SoapObject) envelope.getResponse();
             
                
            responseBody = (SoapObject) responseBody.getProperty(1);
            
            if (responseBody.getPropertyCount() == 0) {
            	error = true;
            	errorMessage = "Data tidak dapat ditemukan";
            	return null;
            			
            }
            table = (SoapObject) responseBody.getProperty(0);
            
            familyList = new ArrayList<HashMap<String, String>>(); 
            
            int iTotalDataFromWebService = table.getPropertyCount();
            for(int i = 0; i < iTotalDataFromWebService; i++)
            {
            	tableRow = (SoapObject) table.getProperty(i);

	            HashMap<String, String> map = new HashMap<String, String>(); 
	            
	            map.put("SPPA_NO", tableRow.getPropertySafelyAsString("SPPA_NO").toString()); 
	            map.put("FAMILY_KEY", tableRow.getPropertySafelyAsString("FAMILY_KEY").toString()); 
	 	       
	            if(tableRow.getPropertySafelyAsString("NAME").toString().toLowerCase().contains("anytype"))
	            {
	                map.put("NAME", " "); 
		            map.put("DOB", " ");
		            map.put("ID_NO", " ");
		            map.put("PREMI",  "0");
		            
		            map.put("EFF_DATE", " ");
		            map.put("GENDER",  " ");
		            map.put("STATUS", " ");
		            
	            }
	            else {
		            map.put("NAME", tableRow.getPropertySafelyAsString("NAME").toString()); 
		            map.put("DOB", Utility.getFormatDate(tableRow.getPropertySafelyAsString("DOB").toString()));
		            
		            map.put("ID_NO", tableRow.getPropertySafelyAsString("ID_NO").toString());
		            map.put("PREMI", tableRow.getPropertySafelyAsString("PREMI").toString());
		            
		            map.put("EFF_DATE", tableRow.getPropertySafelyAsString("EFF_DATE").toString()); 
		            map.put("GENDER", tableRow.getPropertySafelyAsString("GENDER").toString()); 
		            map.put("STATUS", tableRow.getPropertySafelyAsString("STATUS").toString());
	            }
	            /*
	            map.put("CRE_DATE", tableRow.getPropertySafelyAsString("CRE_DATE").toString());
	            map.put("CRE_BY", tableRow.getPropertySafelyAsString("CRE_BY").toString());
	            map.put("CRE_IP_ADDRESS", tableRow.getPropertySafelyAsString("CRE_IP_ADDRESS").toString());
	            map.put("MOD_DATE", tableRow.getPropertySafelyAsString("MOD_DATE").toString());
	            map.put("MOD_BY", tableRow.getPropertySafelyAsString("MOD_BY").toString());
	            map.put("MOD_IP_ADDRESS", tableRow.getPropertySafelyAsString("MOD_IP_ADDRESS").toString());
	            */
	            familyList.add(map);
            
            }				
		}	
		catch (Exception e) {
    		error = true;
    		e.printStackTrace();	        		
			errorMessage = new MasterExceptionClass(e).getException();
		}
		return null;
	}
	
	protected void onPostExecute(Void result) {
		
		super.onPostExecute(result);
		
		progressBar.hide();
		progressBar.dismiss();
		
		try
		{
			if (error)
				Utility.showCustomDialogInformation(ctx, "Informasi", 
						errorMessage);	
			else
			{
				;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
		}
	}

	public ArrayList<HashMap<String, String>> getFamily (){
		return familyList;
	}
	
	
}
