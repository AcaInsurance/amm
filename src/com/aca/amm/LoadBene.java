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

public class LoadBene extends AsyncTask<String, Void, Void>{
	private ProgressDialog progressBar;
	private Context ctx;
	
	private boolean error = false;
	private String errorMessage = "";
	
	private SoapObject requestRetrieve = null;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE androidHttpTransport = null;
    
    private static String NAMESPACE = "http://tempuri.org/";     
	private static String URL = Utility.getURL(); 
    private static String SOAP_ACTION = "http://tempuri.org/LoadBeneficiary";     
    private static String METHOD_NAME= "LoadBeneficiary";
    
    private String SPPA_NO ;
    private String benekey;
    
    private NumberFormat nf;
    
    private ArrayList<HashMap<String, String>> beneList;
    
    public LoadBene (Context ctx, String SPPA_NO, String BeneKey){
    	this.ctx = ctx;
    	this.SPPA_NO = SPPA_NO;
    	
    	benekey = BeneKey; 
    	
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
	        requestRetrieve.addProperty(Utility.GetPropertyInfo("BenefKey", benekey, String.class));
	        
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
            
            beneList = new ArrayList<HashMap<String, String>>(); 
            
            int iTotalDataFromWebService = table.getPropertyCount();
            for(int i = 0; i < iTotalDataFromWebService; i++)
            {
            	tableRow = (SoapObject) table.getProperty(i);

	            HashMap<String, String> map = new HashMap<String, String>(); 
	            
	            map.put("SPPA_NO", tableRow.getPropertySafelyAsString("SPPA_NO").toString()); 
	            map.put("BENEFICIARY_KEY", tableRow.getPropertySafelyAsString("BENEFICIARY_KEY").toString()); 
	            
	            if(tableRow.getPropertySafelyAsString("NAME").toString().toLowerCase().contains("anytype"))
	            {
	                map.put("NAME", " "); 
		            map.put("RELATION", " ");
		            map.put("ADDRESS", " ");
	            }
	            else {
	                map.put("NAME", tableRow.getPropertySafelyAsString("NAME").toString()); 
		            map.put("RELATION", tableRow.getPropertySafelyAsString("RELATION").toString()); 
		            map.put("ADDRESS", tableRow.getPropertySafelyAsString("ADDRESS").toString());
	            }
	            
	            beneList.add(map);
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

	public ArrayList<HashMap<String, String>> getBeneficiary (){
		return beneList;
	}
	
}
